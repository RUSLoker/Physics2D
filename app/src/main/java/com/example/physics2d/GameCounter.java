package com.example.physics2d;


public class GameCounter {
    private int firstScore, secondScore;
    private Player currentTurn;
    private boolean playing;
    private long startTime;
    private boolean paused;

    final GameMode gameMode;

    private long limit;

    GameCounter(GameMode gameMode, long limit){
        this.gameMode = gameMode;
        if(gameMode == GameMode.MaxScore && limit <= 0) limit = 16;
        if(gameMode == GameMode.Time && limit <= 0) limit = 90;
        if(gameMode == GameMode.MaxRounds && limit <= 0) limit = 16;
        this.limit = limit;
        if(gameMode == GameMode.Time) startTime = System.currentTimeMillis()/1000;
        firstScore = 0;
        secondScore = 0;
        currentTurn = Player.Blue;
        playing = true;
        paused = false;
    }

    GameCounter(GameCounter gC){
        this(gC.gameMode, gC.limit);
    }

    GameCounter(){
        this(GameMode.MaxScore, 16);
    }

    public long getTimeRest(){
        if(!paused) {
            long now = System.currentTimeMillis() / 1000;
            return limit - (now - startTime);
        }
        return limit;
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
        return playing;
    }

    private void turn(){
        if(currentTurn == Player.Blue)
            currentTurn = Player.Red;
        else
            currentTurn = Player.Blue;
    }

    public void checkGame(){
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
                if(getTimeRest() <= 0) playing = false;
                break;
        }
    }

    public Player getWinner(){
        if(firstScore > secondScore) return Player.Blue;
        if(firstScore < secondScore) return Player.Red;
        if (firstScore == secondScore) return Player.None;
        return null;
    }

    public void pause(){
        if(gameMode == GameMode.Time)
            limit = getTimeRest();
        paused = true;
    }

    public void resume(){
        if(gameMode == GameMode.Time) {
            long now = System.currentTimeMillis() / 1000;
            startTime = now;
        }
        paused = false;
    }


}
