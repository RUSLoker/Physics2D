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
            currentTurn = Player.Second;
        } else if (player == Player.Second){
            secondScore++;
            currentTurn = Player.First;
        }
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

    public Player getWinner(){
        if(firstScore > secondScore) return Player.First;
        if(firstScore < secondScore) return Player.Second;
        return null;
    }


}
