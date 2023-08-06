package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Form extends AppCompatActivity {

    public View decorView;
    public EditText edit1, edit2;
    public Button submit;
    public Animation scale_down, scale_up;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        decorView = getWindow().getDecorView();

        edit1 = findViewById(R.id.edit1);
        edit2 = findViewById(R.id.edit2);
        submit = findViewById(R.id.submit);

        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        final MediaPlayer button_clicked = MediaPlayer.create(this, R.raw.sound);

        submit.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                submit.startAnimation(scale_down);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                submit.startAnimation(scale_up);
                button_clicked.start();
                submit.performClick();
            }
            return true;
        });

        submit.setOnClickListener(v -> {
            String p1_name = edit1.getText().toString().trim();
            String p2_name = edit2.getText().toString().trim();

            Intent intent = new Intent(Form.this, Multiplayer.class);

            if (!p1_name.isEmpty() && !p2_name.isEmpty()) {
                intent.putExtra("player1", p1_name);
                intent.putExtra("player2", p2_name);
            }

            startActivity(intent);
            finish();
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