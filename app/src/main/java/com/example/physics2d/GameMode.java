package com.example.physics2d;

import android.content.res.Resources;

public enum GameMode {
    Time,
    MaxScore,
    MaxRounds;

    static GameMode byId(int id){
        switch (id){
            default:
            case 0:
                return MaxScore;
            case 1:
                return Time;
            case 2:
                return MaxRounds;
        }
    }
}
