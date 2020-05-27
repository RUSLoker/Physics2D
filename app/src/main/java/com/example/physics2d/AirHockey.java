package com.example.physics2d;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class AirHockey extends AppCompatActivity {

    static Border border = new Border(0, 1920, 1080, 0);
    static final Border borderGoal = new Border(-500, 2420, 805, 275);
    static final Border borderStandard = new Border(0, 1920, 1080, 0);
    static final Border border0 = new Border(0, 960, 1080, 0);
    static final Border border1 = new Border(960, 1920, 1080, 0);
    static final PhysObj pluckLeft = new PhysObj(
            new Circle(new Vector2D(500, 540), 70),
            10.0
    );
    static final PhysObj pluckRight = new PhysObj(
            new Circle(new Vector2D(1420, 540), 70),
            10.0
    );

    static final public PhysObj[] objs = new PhysObj[3];
    static final public Manipulator[] manips = new  Manipulator[2];
    Thread myThread = new Thread(AirHockey::vrun);
    static boolean work = false;
    static double cps = 0;
    boolean workPrev = work;
    boolean paused = false;
    public static MotionEvent motionEvent = null;
    static final double maxSpeed = 2300;
    int[] nums = {-1, -1};
    public static int vCenter = 0, hCenter = 0;
    static boolean cycleF;
    static GameCounter game = new GameCounter();
    static AirHockey airHockey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_hockey);
        View visulizer = findViewById(R.id.manips);
        visulizer.setOnTouchListener(this::moveBody);
        setDefault();
        myThread.start();
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
        if (game.getCurrentTurn() == Player.First){
            objs[2] = pluckLeft.clone();
        } else if(game.getCurrentTurn() == Player.Second) {
            objs[2] = pluckRight.clone();
        }
        manips[0] = new Manipulator();
        manips[1] = new Manipulator();
        work = true;
        cycleF = true;
        border = borderStandard;
        airHockey = this;
    }

    @Override
    protected void onPause() {
        cycleF = false;
        try {
            myThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    public void pauseSim() {
        if (!paused) {
            paused = true;
            workPrev = work;
            if (work) {
                work = false;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resumeSim() {
        work = workPrev;
        paused = false;
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
                    objs[0].setVelocity(manips[0].getDelta().scale(maxSpeed));
                    objs[1].setVelocity(manips[1].getDelta().scale(maxSpeed));
                    objs[0].checkBorder(border0);
                    objs[1].checkBorder(border1);
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

                    for (PhysObj cur : objs) {
                        if (cur != null) {
                            cur.move(time);
                        }
                    }

                    if (objs[2].getBody().getLeft() < borderStandard.L &&
                            (
                                (objs[2].getBody().getDown() < borderGoal.U
                                && objs[2].getBody().getUp() >= borderGoal.U
                                && objs[2].getSpeed().x <= 0
                                && objs[2].getCenter().y < borderGoal.U)
                                || (objs[2].getBody().getDown() <= borderGoal.D
                                && objs[2].getBody().getUp() > borderGoal.D
                                && objs[2].getSpeed().x <= 0
                                && objs[2].getCenter().y > borderGoal.D)
                                || (objs[2].getBody().getDown() > borderGoal.D
                                && objs[2].getBody().getUp() < borderGoal.U)
                            ) ||
                            objs[2].getBody().getRight() > borderStandard.R &&
                                    (
                                            (objs[2].getBody().getDown() < borderGoal.U
                                                    && objs[2].getBody().getUp() >= borderGoal.U
                                                    && objs[2].getSpeed().x >= 0)
                                                    || (objs[2].getBody().getDown() <= borderGoal.D
                                                    && objs[2].getBody().getUp() > borderGoal.D
                                                    && objs[2].getSpeed().x >= 0)
                                                    || (objs[2].getBody().getDown() > borderGoal.D
                                                    && objs[2].getBody().getUp() < borderGoal.U)
                                    )
                    )border = borderGoal;

                    if(objs[2] != null) {
                        objs[2].checkBorder(border);
                    }
                    objs[0].checkBorder(border0);
                    objs[1].checkBorder(border1);

                    if(objs[2].getCenter().x < -100){
                        game.goal(Player.Second);
                        airHockey.changeGame();
                    }else if(objs[2].getCenter().x > 2020){
                        game.goal(Player.First);
                        airHockey.changeGame();
                    }

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

    public void changeGame(){
        setDefault();
    }

    public boolean moveBody(View v, MotionEvent event){
        int pointerCount = event.getPointerCount();
        double x = event.getX(0);
        double y = event.getY(0);
        Vector2D point = new Vector2D(x, y);
        motionEvent = event;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_POINTER_UP:
                manips[0].unSet();
                manips[1].unSet();
                nums = new int[]{-1, -1};
                if(pointerCount > 1){
                    if(x > hCenter) {
                        manips[1].set(point);
                        nums[1] = 0;
                    } else {
                        manips[0].set(point);
                        nums[0] = 0;
                    }
                    x = event.getX(1);
                    y = event.getY(1);
                    point = new Vector2D(x, y);
                    if(nums[0] == -1) {
                        manips[0].set(point);
                        nums[0] = 1;
                    } else {
                        manips[1].set(point);
                        nums[1] = 1;
                    }
                    break;
                }
            case MotionEvent.ACTION_DOWN: // нажатие
                if(x > hCenter) {
                    manips[1].set(point);
                    nums[1] = 0;
                } else {
                    manips[0].set(point);
                    nums[0] = 0;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(pointerCount <= 2){
                    x = event.getX(1);
                    y = event.getY(1);
                    point = new Vector2D(x, y);
                    if(nums[0] == -1) {
                        manips[0].set(point);
                        nums[0] = 1;
                    } else {
                        manips[1].set(point);
                        nums[1] = 1;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE: // движение
                try {
                    if (nums[0] != -1) {
                        x = event.getX(nums[0]);
                        y = event.getY(nums[0]);
                        point = new Vector2D(x, y);
                        manips[0].calcDelta(point);
                    }
                } catch (Exception ignored) {}

                try {
                    if (nums[1] != -1) {
                        x = event.getX(nums[1]);
                        y = event.getY(nums[1]);
                        point = new Vector2D(x, y);
                        manips[1].calcDelta(point);
                    }
                } catch (Exception ignored) { }
                break;

            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                manips[0].unSet();
                manips[1].unSet();
                nums = new int[]{-1, -1};
                break;
        }
        return true;
    }
    
}
