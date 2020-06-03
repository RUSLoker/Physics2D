package com.example.physics2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class CustomGame extends AppCompatActivity {

    Intent create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        create = new Intent(this, AirHockey.class);
        Button createB = findViewById(R.id.create);
        createB.setOnClickListener(v -> create());
    }

    void create(){
        Bundle b = new Bundle();
        EditText maxScoreView = findViewById(R.id.maxScore);
        String mSStr = maxScoreView.getText().toString();
        int maxScore = 0;
        if(mSStr.length() != 0){
            maxScore = Integer.parseInt(mSStr);
        }
        if(maxScore <= 0) maxScore = 16;
        b.putInt("maxScore", maxScore);
        create.putExtras(b);
        startActivity(create);
        finish();
    }
}