package love.dragonist.flowerhouse.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import love.dragonist.flowerhouse.R;
import love.dragonist.flowerhouse.adapter.MainViewPagerAdapter;
import love.dragonist.flowerhouse.bean.Flower;
import love.dragonist.flowerhouse.bean.Record;
import love.dragonist.flowerhouse.fragment.FlowerFragment;
import love.dragonist.flowerhouse.fragment.GameFragment;
import love.dragonist.flowerhouse.fragment.HomeFragment;
import love.dragonist.flowerhouse.fragment.MineFragment;
import love.dragonist.flowerhouse.net.PostIdentify;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, FlowerFragment.OnFragmentInteractionListener, GameFragment.OnFragmentInteractionListener, MineFragment.OnFragmentInteractionListener {
    private BottomNavigationView bottomNavigationView; //底部导航栏
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;

    private List<Fragment> fragments;
    private Uri imageUri;

    private static final int TAKE_PHOTO = 1;
    private static final int SEARCH = 2;
    private static final int ALBUM = 3;
    private static final int CATEGORY = 4;
    private final static int DETAIL = 1;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        bottomNavigationView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.main_viewPager);
    }

    public void initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.navigation_flower:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.navigation_game:
                        viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_mine:
                        viewPager.setCurrentItem(3);
                        return true;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void initData() {
        fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("1", "1"));
        fragments.add(FlowerFragment.newInstance("2", "2"));
        fragments.add(GameFragment.newInstance("3", "3"));
        fragments.add(MineFragment.newInstance("4", "4"));
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mainViewPagerAdapter);
    }

    @Override
    public void onFragmentInteraction(JSONObject jsonObject) {
        String fragment = "";
        int action = 0;
        try {
            fragment = jsonObject.getString("fragment");
            action = jsonObject.getInt("action");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent;
        switch (fragment) {
            case "home":
                switch (action) {
                    case 1:
                        try {
                            startActivity(new Intent(MainActivity.this, FlowerDetailActivity.class)
                                    .putExtra("flowerInfo", jsonObject.getString("flowerInfo")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;
            case "flower":
                switch (action) {
                    case TAKE_PHOTO:
                        File outputImage = new File(Environment.getExternalStorageDirectory(), "output_image.jpg");
                        try {
                            if (outputImage.exists())
                                outputImage.delete();
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= 24)
                            imageUri = FileProvider.getUriForFile(MainActivity.this, "love.dragonist", outputImage);
                        else
                            imageUri = Uri.fromFile(outputImage);

                        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);
                        break;
                    case SEARCH:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        break;
                    case ALBUM:
                        intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(intent, ALBUM);
                        break;
                    case CATEGORY:
                        startActivity(new Intent(MainActivity.this, CategoryActivity.class));
                        break;
                }
                break;
            case "mine":
                switch (action) {
                    case DETAIL:
                        Flower flower = null;
                        try {
                            flower = ((Record) jsonObject.get("record")).getFlower();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(new Intent(MainActivity.this, FlowerDetailActivity.class)
                                .putExtra("flowerInfo", gson.toJson(flower)));
                        break;
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        switch (requestCode) {
            case TAKE_PHOTO:
                startActivity(new Intent(MainActivity.this, SearchActivity.class)
                        .putExtra("imgUri", imageUri.toString())
                        .putExtra("latitude", location.getLatitude())
                        .putExtra("longitude", location.getLongitude()));
                break;
            case ALBUM:
                if (data == null) return; //没选照片
                imageUri = data.getData();
                startActivity(new Intent(MainActivity.this, SearchActivity.class)
                        .putExtra("imgUri", imageUri.toString())
                        .putExtra("latitude", location.getLatitude())
                        .putExtra("longitude", location.getLongitude()));
                break;
        }
    }
}
