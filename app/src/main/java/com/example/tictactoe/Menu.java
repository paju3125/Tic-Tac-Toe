package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    public View decorView;
    public Button ai, human;
    private Animation scale_down, scale_up;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        decorView = getWindow().getDecorView();
        ai = findViewById(R.id.ai_btn);
        human = findViewById(R.id.human_btn);

        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        final MediaPlayer btn_click = MediaPlayer.create(this, R.raw.sound);

        // Single PLayer Button Click
        ai.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                human.clearAnimation();
                ai.startAnimation(scale_down);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                ai.startAnimation(scale_up);
                btn_click.start();
                ai.performClick();
            }
            return true;
        });

        ai.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, SinglePlayer.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Multiplayer Button Click
        human.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                ai.clearAnimation();
                human.startAnimation(scale_down);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                human.startAnimation(scale_up);
                btn_click.start();
                human.performClick();
            }
            return true;
        });

        human.setOnClickListener(v -> {
            Intent intent = new Intent(Menu.this, Form.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}