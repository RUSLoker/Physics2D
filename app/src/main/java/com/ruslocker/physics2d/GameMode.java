package com.ruslocker.physics2d;

public enum GameMode {
    Time,
    MaxScore,
    MaxRounds;

    public static GameMode byId(int id){
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
