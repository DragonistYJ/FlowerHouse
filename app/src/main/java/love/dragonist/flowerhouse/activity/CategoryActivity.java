package love.dragonist.flowerhouse.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import love.dragonist.flowerhouse.R;

public class CategoryActivity extends AppCompatActivity {
    private ImageView imgDouble;
    private ImageView imgSingle;
    private ImageView imgQW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initData();
        initView();
        initListener();
    }

    private void initView() {
        imgDouble = findViewById(R.id.category_double);
        imgSingle = findViewById(R.id.category_single);
        imgQW = findViewById(R.id.category_qw);
    }

    private void initData() {
    }

    private void initListener() {
        imgDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, SearchActivity.class)
                        .putExtra("class", "双子叶"));
            }
        });
        imgSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, SearchActivity.class)
                        .putExtra("class", "单子叶"));
            }
        });
        imgQW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, SearchActivity.class)
                        .putExtra("class", "蔷薇"));
            }
        });
    }
}
