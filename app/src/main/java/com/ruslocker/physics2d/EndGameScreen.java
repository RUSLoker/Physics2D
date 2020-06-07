package com.ruslocker.physics2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ruslocker.physics2d.activities.AirHockey;

public class EndGameScreen extends AppCompatActivity {
    static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game_screen);
        savedInstanceState = getIntent().getExtras();
        int winner = savedInstanceState.getInt("winner");
        int scoreRed = savedInstanceState.getInt("scoreRed");
        int scoreBlue = savedInstanceState.getInt("scoreBlue");
        TextView text = findViewById(R.id.text);
        TextView score = findViewById(R.id.score);
        if(winner == 1){
            text.setText(R.string.blueWins);
            text.setTextColor(getColor(R.color.blue));
            score.setText(scoreBlue + " - " + scoreRed);
            score.setTextColor(getColor(R.color.blue));
        } else if(winner == 2){
            text.setText(R.string.redWins);
            score.setText(scoreRed + " - " + scoreBlue);
            score.setTextColor(getColor(R.color.red));
            text.setTextColor(getColor(R.color.red));
        } else if(winner == 3){
            text.setText(R.string.draw);
            score.setText(scoreRed + " - " + scoreBlue);
            score.setTextColor(getColor(R.color.drawColor));
            text.setTextColor(getColor(R.color.drawColor));
        }
        Bundle b = new Bundle();
        b.putBoolean("isRestart", true);
        intent = new Intent(this, AirHockey.class);
        intent.putExtras(b);
        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener((x) -> listener());
        hideSystemUI();
    }

    void listener(){
        startActivity(intent);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
