package com.ruslocker.physics2d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.ruslocker.physics2d.activities.AirHockey;

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
        LimitSetter limitSetter = (LimitSetter) getSupportFragmentManager().findFragmentById(R.id.limitSetter);
        mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                limitSetter.changeCase((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        hideSystemUI();
    }

    void create(){
        Bundle b = new Bundle();
        LimitSetter limitSetter = (LimitSetter) getSupportFragmentManager().findFragmentById(R.id.limitSetter);
        Spinner mode = findViewById(R.id.modeSelector);
        int modeId = (int)mode.getSelectedItemId();
        b.putInt("gameMode", modeId);
        int limit = limitSetter.getLimit();
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