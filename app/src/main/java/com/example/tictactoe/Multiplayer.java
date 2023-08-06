package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class Multiplayer extends AppCompatActivity {
    public View decorView;
    public Dialog dialog;
    public MediaPlayer btn_clicked;
    public Animation scale_down, scale_up;

    public TextView status, score;
    public Button reset, home;

    public String p1_name, p2_name;
    public int p1_score = 0;
    public int p2_score = 0;
    public int p1 = 0, p2 = 1;

    public int activePlayer;

    int[] gameState = {2, 2 , 2, 2, 2, 2, 2, 2, 2};
    int[][] winPositions =  {{3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {2,4,6}, {0,1,2}, {0,4,8}};

    public boolean isBoardFull() {
        for (int i: gameState) { if (i == 2) return false; }
        return true;
    }

    public boolean checkWinner(int player) {
        for (int[] winPosition: winPositions) {
            if(gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]]==player)
                return true;
        }
        return false;
    }

    @SuppressLint("SetTextI18n")
    public void gameReset(View view) {
        activePlayer = 0;
        Arrays.fill(gameState, 2);

        ((ImageView)findViewById(R.id.imageView0)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView1)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView2)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView3)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView4)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView5)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView6)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView7)).setImageResource(0);
        ((ImageView)findViewById(R.id.imageView8)).setImageResource(0);

        dialog.dismiss();

        if (activePlayer == 0) status.setText(p1_name+"'s Turn");
        else status.setText(p2_name+"'s Turn");
    }

    @SuppressLint("SetTextI18n")
    public void playerTap(View view) {
        btn_clicked.start();

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (gameState[tappedImage] == 2) {
            img.setScaleX(0f);
            img.setScaleY(0f);
            gameState[tappedImage] = activePlayer;

            if (activePlayer == 1) {
                img.setImageResource(R.drawable.ic_o);
                status.setText(p1_name+"'s Turn");
                activePlayer = 0;
            } else {
                img.setImageResource(R.drawable.ic_x);
                status.setText(p2_name+"'s Turn");
                activePlayer = 1;
            }
            img.animate().scaleXBy(1f).scaleYBy(1f).setInterpolator(new OvershootInterpolator()).setDuration(300);
        }

        if (checkWinner(p1)) showDialog(status, "win", "p1");
        else if (checkWinner(p2)) showDialog(status, "win", "p2");
        else if (isBoardFull()) showDialog(status, "draw", null);
    }

    public void gotoHome(View view) {
        Intent intent = new Intent(Multiplayer.this, Menu.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    public void showDialog(TextView status, String type, String winner) {
        if (type.equals("win")) dialog.setContentView(R.layout.popup_win);
        else dialog.setContentView(R.layout.popup_draw);

        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.popupAnimation;

        dialog.setCancelable(false);
        dialog.show();

        TextView dialog_text = dialog.findViewById(R.id.text);
        if (type.equals("win")) {
            String msg = winner.equals("p1") ? p1_name+" Win" : p2_name+" Win";
            dialog_text.setText(msg);

            if (winner.equals("p1")) p1_score++;
            else if (winner.equals("p2")) p2_score++;

            score.setText(String.format("%d : %d", p1_score, p2_score));
        }


        Button popup_home = dialog.findViewById(R.id.popup_home);
        Button popup_reset = dialog.findViewById(R.id.popup_reset);

        MediaPlayer button_clicked = MediaPlayer.create(this, R.raw.sound);

        popup_home.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                popup_home.startAnimation(scale_down);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                popup_home.startAnimation(scale_up);
                button_clicked.start();
                popup_home.performClick();
            }
            return true;
        });

        popup_reset.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                popup_reset.startAnimation(scale_down);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                popup_reset.startAnimation(scale_up);
                button_clicked.start();
                popup_reset.performClick();
            }
            return true;
        });
    }


    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        decorView = getWindow().getDecorView();
        dialog = new Dialog(this);
        btn_clicked = MediaPlayer.create(this, R.raw.sound);

        Bundle bundle =getIntent().getExtras();
        if (bundle != null) {
            p1_name = bundle.getString("player1");
            p2_name = bundle.getString("player2");
        } else {
            p1_name = "Player 1";
            p2_name = "Player 2";
        }
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);

        text1.setText(p1_name);
        text2.setText(p2_name);

        status = findViewById(R.id.status);
        score = findViewById(R.id.score);
        home = findViewById(R.id.home);
        reset = findViewById(R.id.reset);

        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        activePlayer = 0;

        if (activePlayer == 0) status.setText(p1_name+"'s Turn");
        else status.setText(p2_name+"'s Turn");

        home.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                home.startAnimation(scale_down);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                home.startAnimation(scale_up);
                btn_clicked.start();
                home.performClick();
            }
            return true;
        });

        reset.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                reset.startAnimation(scale_down);
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                reset.startAnimation(scale_up);
                btn_clicked.start();
                reset.performClick();
            }
            return true;
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