package com.example.physics2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

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
        }
        intent = new Intent(this, AirHockey.class);
        Button restart = findViewById(R.id.restart);
        restart.setOnClickListener((x) -> listener());

    }

    void listener(){
        startActivity(intent);
        finish();
    }
}
