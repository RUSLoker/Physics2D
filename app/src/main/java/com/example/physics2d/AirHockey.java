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
    static final public PhysObj[] objs = new PhysObj[3];
    Thread myThread = new Thread(AirHockey::vrun);
    static boolean work = false;
    static Integer pointer = null;
    static PhysObj standart = new PhysObj(
            new Circle(new Vector2D(800, 1700), 100),
            100.0
    );
    static double cps = 0;
    static double checker;
    boolean workPrev = work;
    boolean paused = false;
    double prevLen = -1;
    double dLen;
    public static MotionEvent motionEvent = null;
    Vector2D prevP;
    boolean scaling = false;
    double pTX = -1;
    double pTY = -1;
    Thread add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_hockey);
        myThread.start();
        View visulizer = findViewById(R.id.visualizer);
        visulizer.setOnTouchListener(this::moveBody);
        objs[0] = new PhysObj(
                new Circle(new Vector2D(500, 500), 100),
                100.0
        );
        objs[1] = new PhysObj(
                new Circle(new Vector2D(1500, 500), 100),
                100.0
        );
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
/*                    fastest = 0;
                    for (PhysObj i : objs) {
                        double x = i.getSpeed().length + i.getAcceleration().length * time;
                        if (x > fastest)
                            fastest = x;
                    }

                    if (fastest <= 2) time = gap;
                    else {
                        checker = Math.log(fastest) / log2;
                        checker = Math.ceil(checker * (Math.pow(1.1, (checker - 5550) / 10) + 1));

                        time = Math.pow(2, -checker);
                    }
                    time = time * 1000000000 > gap ? ((double) gap) / 1000000000 : time;*/
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
                            cur.checkBorder(border);
                        }
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

    public boolean moveBody(View v, MotionEvent event){
        int pointerCount = event.getPointerCount();
        double x = event.getX(0);
        double y = event.getY(0);
        Vector2D point = new Vector2D(x, y).sub(
                new Vector2D(AirHockeyVisualizer.x0, AirHockeyVisualizer.y0)).scale(1/AirHockeyVisualizer.scale);
        motionEvent = event;


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: // нажатие
            {
                pauseSim();
                if(objs.length != 0) {
                    if (AirHockey.pointer == null || !objs[pointer].getBody().isInside(point) && pointerCount == 1) {
                        boolean touched = false;
                        for (int i = 0; i < objs.length; i++) {
                            if (objs[i].getBody().isInside(point)) {
                                touched = true;
                                pointer = i;
                                
                                break;
                            }
                        }
                        if(!touched && pointerCount < 2){
                            pointer = null;
                            
                        }
                    }
                }
                resumeSim();
                prevP = point;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP: {
                if(AirHockey.pointer != null && pointerCount > 1) {
                    x = event.getX(0);
                    y = event.getY(0);
                    Vector2D point1 = new Vector2D(x, y);
                    x = event.getX(1);
                    y = event.getY(1);
                    Vector2D point2 = new Vector2D(x, y);
                    prevLen = point1.sub(point2).length;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: // движение
            {
                if(AirHockey.pointer == null);
                else if(objs.length != 0) {
                    if (pointerCount == 1 && objs[pointer].getBody().isInside(prevP)) {
                        objs[pointer].getBody().setCenter(point);
                    } else if(pointerCount > 1) {
                        x = event.getX(0);
                        y = event.getY(0);
                        Vector2D point1 = new Vector2D(x, y).sub(
                                new Vector2D(AirHockeyVisualizer.x0, AirHockeyVisualizer.y0)).scale(1/AirHockeyVisualizer.scale);
                        x = event.getX(1);
                        y = event.getY(1);
                        Vector2D point2 = new Vector2D(x, y).sub(
                                new Vector2D(AirHockeyVisualizer.x0, AirHockeyVisualizer.y0)).scale(1/AirHockeyVisualizer.scale);
                        objs[0].getBody().setCenter(point1);
                        objs[0].checkBorder(border);
                        objs[1].getBody().setCenter(point2);
                        objs[1].checkBorder(border);
                    }
                }
                prevP = point;
                break;
            }

            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                resumeSim();
                prevLen = -1;
                scaling = false;
                pTX = -1;
                pTY = -1;
                break;
        }
        return true;
    }
    
}
