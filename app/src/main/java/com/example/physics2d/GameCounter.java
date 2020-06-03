package com.example.physics2d;


public class GameCounter {
    private int firstScore, secondScore;
    private Player currentTurn;
    private boolean playing;

    final int MAX_SCORE;

    GameCounter(int maxScore){
        if(maxScore <= 0) maxScore = 16;
        MAX_SCORE = maxScore;
        firstScore = 0;
        secondScore = 0;
        currentTurn = Player.Blue;
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
        if(player == Player.Blue){
            firstScore++;
            currentTurn = Player.Red;
        } else if (player == Player.Red){
            secondScore++;
            currentTurn = Player.Blue;
        }
        checkGame();
        return playing;
    }

    private void turn(){
        if(currentTurn == Player.Blue)
            currentTurn = Player.Red;
        else
            currentTurn = Player.Blue;
    }

    private void checkGame(){
        if(firstScore >= MAX_SCORE || secondScore >= MAX_SCORE){
            playing = false;
        }
    }

    public Player getWinner(){
        if(firstScore > secondScore) return Player.Blue;
        if(firstScore < secondScore) return Player.Red;
        return null;
    }


}
