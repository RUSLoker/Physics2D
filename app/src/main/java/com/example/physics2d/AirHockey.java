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

    static double G = 6.67 * Math.pow(10, -11);
    static Border border = new Border(0, 1920, 1080, 0);
    static final public ArrayList<PhysObj> objs = new ArrayList<>(10);
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
        add(null);
        objs.add(new PhysObj(
                new Circle(new Vector2D(500, 500), 100),
                100.0
        ));
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
        double fastest;
        long countReal = 0;
        double countSim = 0;
        double log2 = Math.log(2);
        long prev = 0;
        while (true) {
            if (work) {
                synchronized (objs) {
                    fastest = 0;
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
                    time = time * 1000000000 > gap ? ((double) gap) / 1000000000 : time;
                    PhysObj a, b;
                    for (int i = 0; i < objs.size(); i++) {
                        a = objs.get(i);
                        for (int j = i + 1; j < objs.size(); j++) {
                            b = objs.get(j);
                            if (a != null && b != null) {
                                a.checkCollisions(b, time);
                            } else {
                                System.out.println(a + " " + b);
                            }
                        }
                    }

                    for (int i = 0; i < objs.size(); i+= 5){
                        double finalTime = time;
                        int finalI = i;
                        new Thread() {
                            @Override
                            public void run() {
                                for (int j = finalI; j < objs.size() && j < finalI + 5; j+= 1) {
                                    PhysObj cur = objs.get(j);
                                    cur.calcAccel();
                                    cur.move(finalTime);
                                    cur.checkBorder(border);
                                }
                            }
                        }.start();
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

    public void add(View view) {
        if (add != null && add.isAlive()) {
            return;
        }
        int count = 1;

        add = new Thread() {
            @Override
            public void run() {
                synchronized (objs) {
                    pauseSim();
                    pointer = objs.size();
                    for (int i = pointer; i < pointer+ count; i++) {
                        objs.add(standart.clone());
                    }
                    PhysObj a, b;
                    boolean needCheck = true;
                    while (needCheck) {
                        needCheck = false;
                        for (int i = 0; i < objs.size(); i++) {
                            a = objs.get(i);
                            for (int j = i + 1; j < objs.size(); j++) {
                                b = objs.get(j);
                                if (a != null && b != null) {
                                    Circle bA, bB;
                                    bA = (Circle) a.getBody();
                                    bB = (Circle) b.getBody();
                                    if (bA.getCenter().sub(bB.getCenter()).length == 0) {
                                        needCheck = true;
                                        Random random = new Random();
                                        Vector2D move = new Vector2D(random.nextDouble(), random.nextDouble()).
                                                setLength((bA.getRadius() + bB.getRadius() -
                                                        bA.getCenter().sub(bB.getCenter()).length) / 2 + 0.1);
                                        bA.move(move);
                                        bB.move(move.reverse());
                                    } else if (bA.getCenter().sub(bB.getCenter()).length <
                                            bA.getRadius() + bB.getRadius()) {
                                        needCheck = true;
                                        Vector2D move = bA.getCenter().sub(bB.getCenter()).
                                                setLength((bA.getRadius() + bB.getRadius() -
                                                        bA.getCenter().sub(bB.getCenter()).length) / 2 + 0.1);
                                        bA.move(move);
                                        bB.move(move.reverse());
                                    }
                                } else {
                                    System.out.println(a + " " + b);
                                }
                            }
                        }
                    }
                    
                    resumeSim();
                }
            }
        };
        add.start();
    }

    public void prev(View view) {
        if (pointer != null) {
            pointer--;
            if (pointer < 0 && objs.size() == 0) {
                pointer = null;
            } else if (pointer < 0) {
                pointer++;
            }
        }
    }

    public void next(View view) {
        pointer++;
        
    }

    public void delete(View view) {
        if (pointer != null && objs.size() != 0) {
            Thread a = new Thread() {
                @Override
                public void run() {
                    synchronized (objs) {
                        pauseSim();
                        if (pointer != null && objs.size() != 0) {
                            for (PhysObj i : objs) {
                                i.delForce(objs.get(pointer).hashCode());
                            }
                            objs.remove((int)pointer);

                        }
                        resumeSim();
                    }
                }
            };
            a.start();
            try {
                a.join();
            } catch (Exception ignored) {}
            prev(null);
            
        }
    }

    public void clear(View view) {
        if (objs.size() != 0) {
            pauseSim();
            pointer = null;
            objs.clear();
            resumeSim();
        }
        
        

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
                if(objs.size() != 0) {
                    if (AirHockey.pointer == null || !objs.get(pointer).getBody().isInside(point) && pointerCount == 1) {
                        boolean touched = false;
                        for (int i = 0; i < objs.size(); i++) {
                            if (objs.get(i).getBody().isInside(point)) {
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
                else if(objs.size() != 0) {
                    if (pointerCount == 1 && objs.get(pointer).getBody().isInside(prevP)) {
                        objs.get(pointer).getBody().setCenter(point);
                    } else if(pointerCount > 1) {
                        x = event.getX(0);
                        y = event.getY(0);
                        Vector2D point1 = new Vector2D(x, y).sub(
                                new Vector2D(AirHockeyVisualizer.x0, AirHockeyVisualizer.y0)).scale(1/AirHockeyVisualizer.scale);
                        x = event.getX(1);
                        y = event.getY(1);
                        Vector2D point2 = new Vector2D(x, y).sub(
                                new Vector2D(AirHockeyVisualizer.x0, AirHockeyVisualizer.y0)).scale(1/AirHockeyVisualizer.scale);
                        objs.get(0).getBody().setCenter(point1);
                        objs.get(0).checkBorder(border);
                        objs.get(1).getBody().setCenter(point2);
                        objs.get(1).checkBorder(border);
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
