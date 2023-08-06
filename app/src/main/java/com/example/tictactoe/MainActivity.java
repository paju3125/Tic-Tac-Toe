package com.example.tictactoe;

import android.animation.TimeInterpolator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static final int SPLASH_TIME = 3000;

    public View decorView;
    public ImageView circle, lines;
    public TextView text;
    public Animation circle_anim, lines_anim, text_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        decorView = getWindow().getDecorView();
        circle = findViewById(R.id.circle);
        lines = findViewById(R.id.lines);
        text = findViewById(R.id.text);

        circle_anim = AnimationUtils.loadAnimation(this, R.anim.circle_anim);
        lines_anim = AnimationUtils.loadAnimation(this, R.anim.lines_anim);
        text_anim = AnimationUtils.loadAnimation(this, R.anim.text_anim);

        circle.setAnimation(circle_anim);
        lines.setAnimation(lines_anim);
        text.setAnimation(text_anim);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Menu.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        }, SPLASH_TIME);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }
}