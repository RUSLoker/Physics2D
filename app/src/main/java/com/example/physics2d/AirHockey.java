package com.example.physics2d;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class AirHockey extends AppCompatActivity {

    static Border border = new Border(0, 1920, 1080, 0);
    static Border border0 = new Border(0, 960, 1080, 0);
    static Border border1 = new Border(960, 1920, 1080, 0);
    static final public PhysObj[] objs = new PhysObj[3];
    static final public Manipulator[] manips = new  Manipulator[2];
    Thread myThread = new Thread(AirHockey::vrun);
    static boolean work = false;
    static Integer pointer = null;
    static double cps = 0;
    boolean workPrev = work;
    boolean paused = false;
    public static MotionEvent motionEvent = null;
    static final double maxSpeed = 10;
    double[] nums = {-1, -1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_hockey);
        myThread.start();
        View visulizer = findViewById(R.id.manips);
        visulizer.setOnTouchListener(this::moveBody);
        objs[0] = new PhysObj(
                new Circle(new Vector2D(500, 500), 100),
                100.0
        );
        objs[1] = new PhysObj(
                new Circle(new Vector2D(1500, 500), 100),
                100.0
        );
        objs[2] = new PhysObj(
                new Circle(new Vector2D(1000, 500), 70),
                10.0
        );
        manips[0] = new Manipulator();
        manips[1] = new Manipulator();
        work = true;
    }

    protected void onPause() {
        workPrev = work;
        work = false;
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
        while (true) {
            if (work) {
                synchronized (objs) {
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
                    objs[2].checkBorder(border);
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

    public boolean moveBody(View v, MotionEvent event){
        int pointerCount = event.getPointerCount();
        double x = event.getX(0);
        double y = event.getY(0);
        Vector2D point = new Vector2D(x, y);
        motionEvent = event;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: // нажатие

                manips[0].set(point);
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE: // движение
                try {
                    manips[0].calcDelta(point);
                } catch (Exception ignored) {}
                break;

            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                manips[0].unSet();
                manips[1].unSet();
                break;
        }
        return true;
    }
    
}
