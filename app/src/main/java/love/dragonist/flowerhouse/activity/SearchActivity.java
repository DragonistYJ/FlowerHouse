package love.dragonist.flowerhouse.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.adapter.SearchListAdapter;
import love.dragonist.flowerhouse.bean.Flower;
import love.dragonist.flowerhouse.net.GetInfo;
import love.dragonist.flowerhouse.net.GetIntro;
import love.dragonist.flowerhouse.net.GetIntroByName;
import love.dragonist.flowerhouse.net.PostIdentify;

public class SearchActivity extends AppCompatActivity {
    private ListView listSearchResult;

    private SearchListAdapter searchListAdapter;
    private EditText editSearch;
    private List<Flower> flowers = new ArrayList<>();
    private Gson gson;
    private GetIntroByName getIntroByName;
    private GetIntro getIntro;
    private PostIdentify postIdentify;
    private GetInfo getInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initData();
        initView();
        initListener();
    }

    private void initView() {
        listSearchResult = findViewById(R.id.search_list);
        listSearchResult.setAdapter(searchListAdapter);
        editSearch = findViewById(R.id.search_search_edit);
    }

    private void initData() {
        gson = new Gson();
        searchListAdapter = new SearchListAdapter(this, flowers);
        getIntro = new GetIntro();
        getIntro.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                searchListAdapter.notifyDataSetChanged();
            }
        });
        getIntroByName = new GetIntroByName(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Flower flower = (Flower) msg.obj;
                for (Flower flower1 : flowers) {
                    if (flower.equals(flower1)) return;
                }
                flowers.add(0, flower);
                if (flowers.size() == 21) flowers.remove(20);
                searchListAdapter.notifyDataSetChanged();
                getIntro.get(flowers.get(0).getId(), flowers.get(0));
            }
        });
        postIdentify = new PostIdentify(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<String> nameList = (List<String>) msg.obj;
                for (String s : nameList) {
                    getIntroByName.get(s);
                }
            }
        });
        getInfo = new GetInfo();
        getInfo.setHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                flowers.addAll((Collection<? extends Flower>) msg.obj);
                for (int i = 0; i < flowers.size(); i++) {
                    getIntro.get(flowers.get(i).getId(), flowers.get(i));
                }
            }
        });

        Intent intent = getIntent();
        String imgUri = intent.getStringExtra("imgUri");
        if (imgUri != null) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imgUri)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
            byte[] datas = baos.toByteArray();

            double latitude = intent.getDoubleExtra("latitude", 30);
            double longitude = intent.getDoubleExtra("longitude", 104);
            postIdentify.post(latitude, longitude, Base64.encodeToString(datas, Base64.DEFAULT));
        }

        String clazz = intent.getStringExtra("class");
        if (clazz != null) {
            Pair<String, String> pair = new Pair<>("class", clazz);
            getInfo.get(pair);
        }
    }

    private void initListener() {
        listSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(SearchActivity.this, FlowerDetailActivity.class)
                        .putExtra("flowerInfo", gson.toJson(flowers.get(position))));
            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) return;
                getIntroByName.get(s.toString());
            }
        });
    }
}
