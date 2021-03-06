package com.ruslocker.physics2d.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ruslocker.physics2d.engine.Border;
import com.ruslocker.physics2d.engine.Circle;
import com.ruslocker.physics2d.game.GameCounter;
import com.ruslocker.physics2d.game.GameMode;
import com.ruslocker.physics2d.game.Manipulator;
import com.ruslocker.physics2d.engine.PhysObj;
import com.ruslocker.physics2d.game.Player;
import com.ruslocker.physics2d.R;
import com.ruslocker.physics2d.engine.Vector2D;

public class AirHockey extends AppCompatActivity {

    static Border border = new Border(0, 1920, 1080, 0);
    static final Border borderGoal = new Border(-500, 2420, 805, 275);
    static final Border borderStandard = new Border(0, 1920, 1080, 0);
    static final Border borderWide = new Border(-500, 2920, 2080, -500);
    static final Border border0 = new Border(0, 960, 1080, 0);
    static final Border border1 = new Border(960, 1920, 1080, 0);
    static final PhysObj pluckLeft = new PhysObj(
            new Circle(new Vector2D(500, 540), 70),
            50.0
    );
    static final PhysObj pluckRight = new PhysObj(
            new Circle(new Vector2D(1420, 540), 70),
            50.0
    );

    static final public PhysObj[] objs = new PhysObj[3];
    static final public PhysObj[] goalCorner = new PhysObj[4];
    static final public Manipulator[] manips = new  Manipulator[2];
    Thread myThread = new Thread(AirHockey::vrun);
    static boolean work = false;
    static double cps = 0;
    public static MotionEvent motionEvent = null;
    static final double maxBatSpeed = 3200;
    static final double maxPluckSpeed = 3800;
    static boolean cycleF;
    public static GameCounter game;
    public static AirHockey airHockey;
    public static TextView scoreFirst, scoreSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_hockey);
        scoreFirst = findViewById(R.id.scoreFirst);
        scoreSecond = findViewById(R.id.scoreSecond);
        View manip1 = findViewById(R.id.manip1);
        View manip2 = findViewById(R.id.manip2);
        manip1.setOnTouchListener(this::moveBody);
        manip2.setOnTouchListener(this::moveBody);

        Bundle b = getIntent().getExtras();
        if(b != null) {
            if(b.getBoolean("isRestart")){
                game = new GameCounter(game);
            } else {
                GameMode gameMode = GameMode.byId(b.getInt("gameMode"));
                int limit = b.getInt("limit");
                game = new GameCounter(gameMode, limit);
            }
        } else game = new GameCounter();

        manips[0] = new Manipulator();
        manips[1] = new Manipulator();
        setDefault();
        myThread.start();
        hideSystemUI();
        Button pause = findViewById(R.id.pause);
        Button resume = findViewById(R.id.resume);
        LinearLayout pauseScreen = findViewById(R.id.pauseScreen);
        pause.setOnClickListener(v -> pause());
        resume.setOnClickListener(v -> {
            game.resume();
            myThread = new Thread(AirHockey::vrun);
            cycleF = true;
            myThread.start();
            manip1.setVisibility(View.VISIBLE);
            manip2.setVisibility(View.VISIBLE);
            pauseScreen.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        });
        if(game.gameMode != GameMode.Time){
            TextView timer1 = AirHockey.airHockey.findViewById(R.id.timer1);
            TextView timer2 = AirHockey.airHockey.findViewById(R.id.timer2);
            timer1.setVisibility(View.GONE);
            timer2.setVisibility(View.GONE);
        }
    }

    void setDefault(){
        objs[0] = new PhysObj(
                new Circle(new Vector2D(200, 540), 100),
                100.0
        );
        objs[1] = new PhysObj(
                new Circle(new Vector2D(1720, 540), 100),
                100.0
        );
        if (game.getCurrentTurn() == Player.Blue){
            objs[2] = pluckLeft.clone();
        } else if(game.getCurrentTurn() == Player.Red) {
            objs[2] = pluckRight.clone();
        }
        goalCorner[0] = new PhysObj(
                new Circle(new Vector2D(borderStandard.L-15, borderGoal.U+15), 15),
                100000000.0
        );
        goalCorner[1] = new PhysObj(
                new Circle(new Vector2D(borderStandard.L-15, borderGoal.D-15), 15),
                100000000.0
        );
        goalCorner[2] = new PhysObj(
                new Circle(new Vector2D(borderStandard.R+15, borderGoal.U+15), 15),
                100000000.0
        );
        goalCorner[3] = new PhysObj(
                new Circle(new Vector2D(borderStandard.R+15, borderGoal.D-15), 15),
                100000000.0
        );
        work = true;
        cycleF = true;
        border = borderStandard;
        airHockey = this;
    }

    @Override
    protected void onPause() {
        game.pause();
        cycleF = false;
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(!cycleF){
            Button pause = findViewById(R.id.pause);
            LinearLayout pauseScreen = findViewById(R.id.pauseScreen);
            View manip1 = findViewById(R.id.manip1);
            View manip2 = findViewById(R.id.manip2);
            manip1.setVisibility(View.GONE);
            manip2.setVisibility(View.GONE);
            pauseScreen.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        }
        super.onResume();
    }

    void pause(){
        game.pause();
        Button pause = findViewById(R.id.pause);
        LinearLayout pauseScreen = findViewById(R.id.pauseScreen);
        View manip1 = findViewById(R.id.manip1);
        View manip2 = findViewById(R.id.manip2);
        manip1.setVisibility(View.GONE);
        manip2.setVisibility(View.GONE);
        pauseScreen.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        cycleF = false;
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void vrun() {
        long gap = 0;
        double time = Math.pow(2, -10);
        long countReal = 0;
        double countSim = 0;
        double log2 = Math.log(2);
        long prev = 0;
        while (cycleF) {
            if (work) {
                synchronized (objs) {
                    time = ((double) gap) / 1000000000;
                    if(time > 0.01) time = 0.01;
                    objs[0].setVelocity(manips[0].getDelta().scale(maxBatSpeed));
                    objs[1].setVelocity(manips[1].getDelta().scale(maxBatSpeed));
                    if(objs[0].checkBorder(border0)){
                        objs[0].setVelocity(Vector2D.zero());
                    }
                    if(objs[1].checkBorder(border1)){
                        objs[1].setVelocity(Vector2D.zero());
                    }
                    PhysObj a, b;
                    for (int i = 0; i < objs.length; i++) {
                        a = objs[i];
                        if (a != null) {
                            for (int j = i + 1; j < objs.length; j++) {
                                b = objs[j];
                                if (b != null) {
                                    a.checkCollisions(b, time);
                                }
                            }
                        }
                    }

                    if(objs[2].getSpeed().length > maxPluckSpeed)
                        objs[2].setVelocity(objs[2].getSpeed().setLength(maxPluckSpeed));

                    for (PhysObj cur : objs) {
                        if (cur != null && !cur.getSpeed().equals(Vector2D.zero)) {
                            cur.move(time);
                        }
                    }

                    if ((objs[2].getBody().getLeft() < borderStandard.L &&
                            objs[2].getBody().getDown() > borderGoal.D
                            && objs[2].getBody().getUp() < borderGoal.U)
                            || (objs[2].getBody().getRight() > borderStandard.R &&
                            objs[2].getBody().getDown() > borderGoal.D
                            && objs[2].getBody().getUp() < borderGoal.U)
                    )border = borderGoal;
                    else if (objs[2].getBody().getLeft() < borderStandard.L &&
                            (
                                (objs[2].getCenter().y < borderGoal.U
                                && objs[2].getBody().getUp() >= borderGoal.U
                                && objs[2].getSpeed().x <= 0
                                && objs[2].getCenter().y < borderGoal.U)
                                || (objs[2].getBody().getDown() <= borderGoal.D
                                && objs[2].getCenter().y > borderGoal.D
                                && objs[2].getSpeed().x <= 0
                                && objs[2].getCenter().y > borderGoal.D)
                            ) ||
                            objs[2].getBody().getRight() > borderStandard.R &&
                                    (
                                            (objs[2].getCenter().y < borderGoal.U
                                                    && objs[2].getBody().getUp() >= borderGoal.U
                                                    && objs[2].getSpeed().x >= 0)
                                                    || (objs[2].getBody().getDown() <= borderGoal.D
                                                    && objs[2].getCenter().y  > borderGoal.D
                                                    && objs[2].getSpeed().x >= 0)
                                    )
                            || objs[2].getCenter().x  < borderStandard.L
                            || objs[2].getCenter().x  > borderStandard.R
                    ) {
                        border = borderWide;
                        for (PhysObj physObj : goalCorner) {
                            a = physObj;
                            a.checkCollisions(objs[2], time);
                        }
                    } else {
                        border = borderStandard;
                    }

                    if(objs[2] != null) {
                        objs[2].checkBorder(border);
                    }
                    objs[0].checkBorder(border0);
                    objs[1].checkBorder(border1);

                    gap = System.nanoTime() - prev;
                    countReal += System.nanoTime() - prev;
                    countSim += time * 1000000000;
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (countReal >= 300000000) {
                cps = countReal / countSim;
                countReal = 0;
                countSim = 0;
            }
            prev = System.nanoTime();

        }
    }

    public void toggle(View view) {
        work = !work;
    }

    public boolean checkGame(){
        if(objs[2].getCenter().x < -100){
            game.goal(Player.Red);
        }else if(objs[2].getCenter().x > 2020){
            game.goal(Player.Blue);
        }
        game.checkGame();
        if(!game.isPlaying()){
            cycleF = false;
            Intent intent = new Intent(this, EndGameScreen.class);
            Bundle info = new Bundle();
            info.putInt("scoreRed", game.getFirstScore());
            info.putInt("scoreBlue", game.getSecondScore());
            if(game.getWinner() == Player.Blue) info.putInt("winner", 2);
            else if(game.getWinner() == Player.Red) info.putInt("winner", 1);
            else if(game.getWinner() == Player.None) info.putInt("winner", 3);
            intent.putExtras(info);
            startActivity(intent);
            finish();
            return false;
        }
        if(objs[2].getCenter().x < -100 || objs[2].getCenter().x > 2020){
            work = false;
            setDefault();
        }
        return true;
    }

    public boolean moveBody(View v, MotionEvent event){
        double x = event.getX(0);
        double y = event.getY(0);
        Vector2D point = new Vector2D(x, y);
        motionEvent = event;
        int num = Integer.parseInt((String) v.getTag());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                if(num == 1) {
                    manips[0].set(point);
                } else if(num == 2) {
                    manips[1].set(point);
                }
                break;
            case MotionEvent.ACTION_MOVE: // движение
                try {
                    if (num == 1) {
                        manips[0].calcDelta(point);
                    } else if (num == 2) {
                        manips[1].calcDelta(point);
                    }
                } catch (Exception ignored) {}
                break;

            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                if(num == 1) {
                    manips[0].unSet();
                } else if(num == 2) {
                    manips[1].unSet();
                }
                break;
        }
        return true;
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
