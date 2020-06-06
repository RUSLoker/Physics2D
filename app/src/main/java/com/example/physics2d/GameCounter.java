package com.example.physics2d;


public class GameCounter {
    private int firstScore, secondScore;
    private Player currentTurn;
    private boolean playing;

    final GameMode gameMode;

    final int limit;

    GameCounter(GameMode gameMode, int limit){
        this.gameMode = gameMode;
        if(gameMode == GameMode.MaxScore && limit <= 0) limit = 16;
        if(gameMode == GameMode.Time && limit <= 0) limit = 90;
        if(gameMode == GameMode.MaxRounds && limit <= 0) limit = 16;
        this.limit = limit;
        firstScore = 0;
        secondScore = 0;
        currentTurn = Player.Blue;
        playing = true;
    }

    GameCounter(GameCounter gC){
        GameMode gameMode = gC.gameMode;
        int limit = gC.limit;
        this.gameMode = gameMode;
        if(gameMode == GameMode.MaxScore && limit <= 0) limit = 16;
        if(gameMode == GameMode.Time && limit <= 0) limit = 90;
        if(gameMode == GameMode.MaxRounds && limit <= 0) limit = 16;
        this.limit = limit;
        firstScore = 0;
        secondScore = 0;
        currentTurn = Player.Blue;
        playing = true;
    }

    GameCounter(){
        this(GameMode.MaxScore, 16);
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
        switch (gameMode) {
            case MaxScore:
                if (firstScore >= limit || secondScore >= limit) {
                    playing = false;
                }
                break;
            case MaxRounds:
                if (firstScore + secondScore >= limit) {
                    playing = false;
                }
                break;
            case Time:

        }
    }

    public Player getWinner(){
        if(firstScore > secondScore) return Player.Blue;
        if(firstScore < secondScore) return Player.Red;
        if (firstScore == secondScore) return Player.None;
        return null;
    }

    public void pause(){

    }

    public void resume(){

    }


}
