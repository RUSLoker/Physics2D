package com.example.physics2d;


public class GameCounter {
    private int firstScore, secondScore;
    private Player currentTurn;
    private boolean playing;

    static final int MAX_SCORE = 16;

    GameCounter(){
        firstScore = 0;
        secondScore = 0;
        currentTurn = Player.First;
        playing = true;
    }

    public int getFirstScore() {
        return firstScore;
    }

    public int getSecondScore() {
        return secondScore;
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public boolean isPlaying() {
        return playing;
    }

    boolean goal(Player player){
        if(player == Player.First){
            firstScore++;
        } else if (player == Player.Second){
            secondScore++;
        }
        turn();
        checkGame();
        return playing;
    }

    private void turn(){
        if(currentTurn == Player.First)
            currentTurn = Player.Second;
        else
            currentTurn = Player.First;
    }

    private void checkGame(){
        if(firstScore >= MAX_SCORE || secondScore >= MAX_SCORE){
            playing = false;
        }
    }


}
