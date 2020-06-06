package com.example.physics2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class CustomGame extends AppCompatActivity {

    Intent create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        create = new Intent(this, AirHockey.class);
        Button createB = findViewById(R.id.create);
        createB.setOnClickListener(v -> create());
        Spinner mode = findViewById(R.id.modeSelector);
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout cMaxScore = findViewById(R.id.caseMaxScore);
                LinearLayout cTime = findViewById(R.id.caseTime);
                LinearLayout cRounds = findViewById(R.id.caseRounds);
                switch ((int) id){
                    case 0:
                        cMaxScore.setVisibility(View.VISIBLE);
                        cTime.setVisibility(View.GONE);
                        cRounds.setVisibility(View.GONE);
                        break;
                    case 1:
                        cMaxScore.setVisibility(View.GONE);
                        cTime.setVisibility(View.VISIBLE);
                        cRounds.setVisibility(View.GONE);
                        break;
                    case 2:
                        cMaxScore.setVisibility(View.GONE);
                        cTime.setVisibility(View.GONE);
                        cRounds.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hideSystemUI();
    }

    void create(){
        Bundle b = new Bundle();
        EditText limitView;
        Spinner mode = findViewById(R.id.modeSelector);
        int modeId = (int)mode.getSelectedItemId();
        b.putInt("gameMode", modeId);
        String str;
        int limit = 0;
        switch (modeId){
            case 0:
                limitView = findViewById(R.id.maxScore);
                str = limitView.getText().toString();
                if(str.length() != 0){
                    limit = Integer.parseInt(str);
                }
            case 1:
                limitView = findViewById(R.id.time);
                str = limitView.getText().toString();
                if(str.length() != 0){
                    limit = Integer.parseInt(str);
                }
            case 2:
                limitView = findViewById(R.id.rounds);
                str = limitView.getText().toString();
                if(str.length() != 0){
                    limit = Integer.parseInt(str);
                }
        }
        b.putInt("limit", limit);
        create.putExtras(b);
        startActivity(create);
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