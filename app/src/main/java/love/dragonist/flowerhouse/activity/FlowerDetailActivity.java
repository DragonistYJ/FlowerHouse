package love.dragonist.flowerhouse.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.bean.Flower;

public class FlowerDetailActivity extends AppCompatActivity {
    private ImageView imgBack;
    private TextView textName;
    private TextView textTitle;
    private TextView textLanguage;
    private TextView textPoem;
    private TextView textIntroduce;
    private ImageView imgFlower;
    private TextView textMeet;
    private Gson gson = new Gson();
    private Flower flower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower_detail);

        initData();
        initView();
        initListener();
    }

    private void initView() {
        flower = gson.fromJson(getIntent().getStringExtra("flowerInfo"), Flower.class);
        imgBack = findViewById(R.id.detail_back);
        textName = findViewById(R.id.detail_flower_name);
        textName.setText(flower.getName());
        textTitle = findViewById(R.id.detail_title_name);
        textTitle.setText(flower.getName() + "信息");
        textLanguage = findViewById(R.id.detail_flower_language);
        textLanguage.setText(flower.getLanguage());
        textPoem = findViewById(R.id.detail_poem);
        textPoem.setText(flower.getPoem());
        textIntroduce = findViewById(R.id.detail_context);
        textIntroduce.setText(flower.getIntroduce());
        imgFlower = findViewById(R.id.detail_flower_img);
        Glide.with(this).load(flower.getImg()).into(imgFlower);
        textMeet = findViewById(R.id.detail_meet);
    }

    private void initData() {

    }

    private void initListener() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlowerDetailActivity.this, MapActivity.class));
            }
        });
    }
}
