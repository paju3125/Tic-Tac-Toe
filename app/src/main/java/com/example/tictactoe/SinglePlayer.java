package com.example.tictactoe;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class SinglePlayer extends AppCompatActivity {

    public View decorView;
    public Dialog dialog;
    public MediaPlayer btn_clicked;
    public Animation scale_down, scale_up;

    public TextView status, score;
    public Button reset, home;

    public int ai_score = 0;
    public int human_score = 0;
    public int ai = 0, human = 1;

    public boolean gameActive = true;
    public int activePlayer = 1;
    public int currentPlayer;

    int[] gameState = {2, 2 , 2, 2, 2, 2, 2, 2, 2};
    int[][] winPositions =  {{3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {2,4,6}, {0,1,2}, {0,4,8}};

    public String getGameState() {
        String all = "";
        for (int i: gameState) {
           all.concat(i+" ");
        }
        return all;
    }

    public boolean isBoardFull() {
        for (int i: gameState) {
            if (i == 2)
                return false;
        }
        return true;
    }

    public boolean checkWinner(int player) {
        for (int[] winPosition: winPositions) {
            if(gameState[winPosition[0]] == gameState[winPosition[1]] && gameState[winPosition[1]] == gameState[winPosition[2]] && gameState[winPosition[0]]==player) {
                return true;
            }
        }
        return false;
    }

    public void gameReset(View view) {
        activePlayer = (currentPlayer == ai) ?human :ai;
        currentPlayer = activePlayer;

        gameActive = true;
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

        if (activePlayer == 0) {
            status.setText(R.string.ai_turn);
            bestMove();
        } else {
            status.setText(R.string.human_turn);
        }
    }

    public void playerTap(View view) {
        btn_clicked.start();
        Log.i("PlayerTap", "Game Active "+gameActive);

        if (!gameActive) return;
        if (activePlayer != human) return;

        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (gameState[tappedImage] == 2) {
            img.setScaleX(0f);
            img.setScaleY(0f);
            gameState[tappedImage] = activePlayer;

            if (activePlayer == 1) {
                img.setImageResource(R.drawable.ic_o);
                status.setText(R.string.ai_turn);
                activePlayer = 0;
            }
            img.animate().scaleXBy(1f).scaleYBy(1f).setInterpolator(new OvershootInterpolator()).setDuration(300);
        }

        if (checkWinner(ai)) showDialog(status, "win", "ai");
        else if (checkWinner(human)) showDialog(status, "win", "human");
        else if (isBoardFull()) showDialog(status, "draw", null);
        else bestMove();
    }

    public void mark(int move) {
        LinearLayout layout = findViewById(R.id.board_layout);
        LinearLayout layer = (LinearLayout) layout.getChildAt(move/3);
        ImageView img;

        for(int i=0; i<layer.getChildCount(); i++) {
            img = (ImageView) layer.getChildAt(i);
            if (Integer.parseInt((String) img.getTag()) == move) {
                if (gameState[move] == 2) {
                    img.setScaleX(0f);
                    img.setScaleY(0f);

                    if (activePlayer == 0) {
                        gameState[move] = activePlayer;
                        img.setImageResource(R.drawable.ic_x);
                        status.setText(R.string.human_turn);
                        activePlayer = 1;
                    }
                    img.animate().scaleXBy(1f).scaleYBy(1f).setInterpolator(new OvershootInterpolator()).setStartDelay(300).setDuration(300);
                }
            }
        }
    }

    public int minimax(int depth, boolean isMax) {
        if (checkWinner(ai)) return 1;
        else if (checkWinner(human)) return -1;
        else if (isBoardFull()) return 0;

        int bestScore, score;

        if (isMax) {
            bestScore = -10;
            for (int i=0; i<gameState.length; i++) {
                if (gameState[i] == 2) {
                    gameState[i] = ai;
                    score = minimax(depth+1, false);
                    gameState[i] = 2;
                    bestScore = Math.max(score, bestScore);
                }
            }
        }
        else {
            bestScore = 10;
            for (int i=0; i<gameState.length; i++) {
                if (gameState[i] == 2) {
                    gameState[i] = human;
                    score = minimax(depth+1, true);
                    gameState[i] = 2;
                    bestScore = Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }

    public void bestMove() {
        int bestScore = -10, bestMove = -1;

        for (int i=0; i<gameState.length; i++) {
            if (gameState[i] == 2) {
                gameState[i] = 0;
                int score = minimax(0, false);
                gameState[i] = 2;
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }
        mark(bestMove);

        if (checkWinner(ai)) showDialog(status, "win", "ai");
        else if (checkWinner(human)) showDialog(status, "win", "human");
        else if (isBoardFull()) showDialog(status, "draw", null);
    }

    public void gotoHome(View view) {
        Intent intent = new Intent(SinglePlayer.this, Menu.class);
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
            int msg = winner.equals("human") ? R.string.human_win : R.string.ai_win;
            dialog_text.setText(msg);

            if (winner.equals("human")) human_score++;
            else if (winner.equals("ai")) ai_score++;

            score.setText(String.format("%d : %d", ai_score, human_score));
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        decorView = getWindow().getDecorView();
        dialog = new Dialog(this);
        btn_clicked = MediaPlayer.create(this, R.raw.sound);

        status = findViewById(R.id.status);
        score = findViewById(R.id.score);
        home = findViewById(R.id.home);
        reset = findViewById(R.id.reset);

        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        status.setText(R.string.human_turn);

        currentPlayer = activePlayer;
        new Handler().postDelayed(() -> {
            if (activePlayer == 0) bestMove();
        }, 1000);

        home.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                reset.clearAnimation();
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
                home.clearAnimation();
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