package love.dragonist.flowerhouse.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import love.dragonist.flowerhouse.R;

public class LoginActivity extends AppCompatActivity {
    private ImageView imgPortrait;
    private TextView textTourist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    private void initView() {
        imgPortrait = findViewById(R.id.login_image_portrait);
        Glide.with(this).load(R.mipmap.login_portrait).into(imgPortrait);
        textTourist = findViewById(R.id.login_text_tourist);
    }

    private void initListener() {
        textTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }
}
