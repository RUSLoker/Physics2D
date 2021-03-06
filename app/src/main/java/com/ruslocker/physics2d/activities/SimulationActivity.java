package com.ruslocker.physics2d.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ruslocker.physics2d.engine.Border;
import com.ruslocker.physics2d.engine.Circle;
import com.ruslocker.physics2d.engine.PhysObj;
import com.ruslocker.physics2d.R;
import com.ruslocker.physics2d.engine.Vector2D;
import com.ruslocker.physics2d.views.Visualizer;

import java.util.ArrayList;
import java.util.Random;

public class SimulationActivity extends AppCompatActivity {

    static double G = 6.67 * Math.pow(10, -11);
    static Border border = new Border(-1000, 1000, 1000, -1000);
    static final public ArrayList<PhysObj> objs = new ArrayList<>(10);
    Thread myThread = new Thread(SimulationActivity::vrun);
    static boolean work = false;
    public static Integer pointer = null;
    static PhysObj standart = new PhysObj(
            new Circle(new Vector2D(500, 500), 100),
            100.0
    );
    public static double cps = 0;
    public static TextView cpsT;
    public static TextView checkerT;
    static double checker;
    static boolean gravity = false;
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
    static boolean cycleF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simulation_activity);
        cycleF = true;
        myThread.start();
        reloadFields(null);
        cpsT = findViewById(R.id.cps);
        checkerT = findViewById(R.id.checkerT);
        drawToggle();
        Switch gravitySw = findViewById(R.id.gravitySwitch);
        gravitySw.setOnCheckedChangeListener(this::gravChecker);
        View visulizer = findViewById(R.id.visualizer);
        visulizer.setOnTouchListener(this::moveBody);
        hideSystemUI();
    }

    protected void onPause() {
        cycleF = false;
        workPrev = work;
        work = false;
        drawToggle();
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
        while (cycleF) {
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


                    if (gravity) {
                        for (int i = 0; i < objs.size(); i++) {
                            a = objs.get(i);
                            for (int j = i + 1; j < objs.size(); j++) {
                                b = objs.get(j);
                                Vector2D force;
                                Vector2D dist = a.getCenter().sub(b.getCenter());
                                if (dist.length >= ((Circle) a.getBody()).getRadius() + ((Circle) b.getBody()).getRadius()) {
                                    double forceAbs = G * (a.getMass() * b.getMass())
                                            / (dist.length * dist.length);
                                    forceAbs = Double.isInfinite(forceAbs) ? 0 : forceAbs;
                                    force = dist.setLength(forceAbs);
                                } else {
                                    force = Vector2D.zero();
                                }
                                a.setForce(b.hashCode(), force.reverse());
                                b.setForce(a.hashCode(), force);
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
                                    //i.checkBorder(border);
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
        drawToggle();
    }

    public void drawToggle() {
        Button toggle = findViewById(R.id.button);
        if (work) {
            toggle.setText(R.string.pause);
        } else {
            toggle.setText(R.string.resume);
        }
    }

    public void add(View view) {
        if (add != null && add.isAlive()) {
            return;
        }
        int count = 1;
        String str = ((EditText) findViewById(R.id.AddCount)).getText().toString();
        if (!str.equals("")) {
            count = Integer.parseInt(str);
        }
        int finalCount = count;

        add = new Thread() {
            @Override
            public void run() {
                synchronized (objs) {
                    pauseSim();
                    pointer = objs.size();
                    for (int i = pointer; i < pointer+finalCount; i++) {
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
                    reloadFields(null);
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
            reloadFields(null);
            drawToggle();
        }
    }

    public void next(View view) {
        pointer++;
        reloadFields(null);
        drawToggle();
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
            drawToggle();
        }
    }

    public void clear(View view) {
        if (objs.size() != 0) {
            pauseSim();
            pointer = null;
            objs.clear();
            resumeSim();
        }
        reloadFields(null);
        drawToggle();

    }

    public void spCd(View view){
        TextView
                speed = (TextView) findViewById(R.id.speed),
                coords = (TextView) findViewById(R.id.coords),
                xA = (TextView) findViewById(R.id.xAmount),
                yA = (TextView) findViewById(R.id.yAmount),
                xSpeed = (TextView) findViewById(R.id.xSpeed),
                ySpeed = (TextView) findViewById(R.id.ySpeed);
        switch (speed.getVisibility()) {
            case View.VISIBLE: {
                speed.setVisibility(View.INVISIBLE);
                xSpeed.setVisibility(View.INVISIBLE);
                ySpeed.setVisibility(View.INVISIBLE);
                coords.setVisibility(View.VISIBLE);
                xA.setVisibility(View.VISIBLE);
                yA.setVisibility(View.VISIBLE);
                break;
            }
            case View.INVISIBLE: {
                speed.setVisibility(View.VISIBLE);
                xSpeed.setVisibility(View.VISIBLE);
                ySpeed.setVisibility(View.VISIBLE);
                coords.setVisibility(View.INVISIBLE);
                xA.setVisibility(View.INVISIBLE);
                yA.setVisibility(View.INVISIBLE);
                break;
            }
            case View.GONE:
                break;
        }
    }

    public void set(View view){
        if(pointer != null) {
            pauseSim();
            PhysObj cur = objs.get(pointer);
            double size = Double.parseDouble(((EditText) findViewById(R.id.sizeAmount)).getText().toString()),
                    mass = Double.parseDouble(((EditText) findViewById(R.id.massAmount)).getText().toString()),
                    xA = Double.parseDouble(((EditText) findViewById(R.id.xAmount)).getText().toString()),
                    yA = Double.parseDouble(((EditText) findViewById(R.id.yAmount)).getText().toString()),
                    xSpeed = Double.parseDouble(((EditText) findViewById(R.id.xSpeed)).getText().toString()),
                    ySpeed = Double.parseDouble(((EditText) findViewById(R.id.ySpeed)).getText().toString());
            ((Circle) cur.getBody()).setRadius(size);
            cur.getBody().setCenter(new Vector2D(xA, yA));
            cur.setMass(mass);
            cur.setVelocity(new Vector2D(xSpeed, ySpeed));
            reloadFields(null);
            resumeSim();
        }
    }

    public void maTg(View view){
        View
                toggles = findViewById(R.id.toggles),
                mass = findViewById(R.id.mass),
                massAm = findViewById(R.id.massAmount);
        switch (toggles.getVisibility()) {
            case View.VISIBLE: {
                toggles.setVisibility(View.INVISIBLE);
                mass.setVisibility(View.VISIBLE);
                massAm.setVisibility(View.VISIBLE);
                break;
            }
            case View.INVISIBLE: {
                toggles.setVisibility(View.VISIBLE);
                mass.setVisibility(View.INVISIBLE);
                massAm.setVisibility(View.INVISIBLE);
                break;
            }
            case View.GONE:
                break;
        }
    }

    public void rollLMenu(View view){
        View menu = findViewById(R.id.propMenu);
        AnimatorSet set = new AnimatorSet();
        if(view.getX() != 0) {
            set.playTogether(
                    ObjectAnimator.ofFloat(menu, View.X, 0, -menu.getRight()),
                    ObjectAnimator.ofFloat(view, View.X, menu.getRight(), 0)
            );
        } else {
            set.playTogether(
                    ObjectAnimator.ofFloat(menu, View.X, -menu.getRight(), 0),
                    ObjectAnimator.ofFloat(view, View.X, 0, menu.getRight())
            );
        }
        set.start();
    }

    public void gravChecker(CompoundButton buttonView, boolean isChecked){
        gravity = isChecked;
        if(!gravity){
            pauseSim();
            for (PhysObj i : objs){
                i.forces.clear();
            }
            resumeSim();
        }
    }

    public boolean moveBody(View v, MotionEvent event){
        int pointerCount = event.getPointerCount();
        double x = event.getX(0);
        double y = event.getY(0);
        Vector2D point = new Vector2D(x, y).sub(
                new Vector2D(Visualizer.x0, Visualizer.y0)).scale(1/Visualizer.scale);
        motionEvent = event;


        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: // нажатие
            {
                pauseSim();
                if(objs.size() != 0) {
                    if (SimulationActivity.pointer == null || !objs.get(pointer).getBody().isInside(point) && pointerCount == 1) {
                        boolean touched = false;
                        for (int i = 0; i < objs.size(); i++) {
                            if (objs.get(i).getBody().isInside(point)) {
                                touched = true;
                                pointer = i;
                                reloadFields(null);
                                break;
                            }
                        }
                        if(!touched && pointerCount < 2){
                            pointer = null;
                            reloadFields(null);
                        }
                    }
                }
                resumeSim();
                prevP = point;
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP: {
                if(SimulationActivity.pointer != null && pointerCount > 1) {
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
                if(SimulationActivity.pointer == null){
                    //Field moving
                    if(pointerCount == 1 && !scaling) {
                        if (pTX == -1 || pTY == -1) {
                            pTX = x;
                            pTY = y;
                        } else {
                            Visualizer.x0 += (x - pTX);
                            Visualizer.y0 += (y - pTY);
                            pTX = x;
                            pTY = y;
                        }
                    } else if(pointerCount > 1){
                        //Zooming
                        scaling = true;
                        Vector2D
                                p1 = new Vector2D(event.getX(0), event.getY(0)),
                                p2 = new Vector2D(event.getX(1), event.getY(1)),
                                mean = Vector2D.mean(p1, p2).sub(
                                        new Vector2D(Visualizer.x0, Visualizer.y0));
                        double length = p1.sub(p2).length;
                        if(prevLen == -1){
                            prevLen = length;
                        }
                        double sp = Visualizer.scale;
                        if(Visualizer.scale + 0.004 * (length - prevLen) > 0.01)
                            Visualizer.scale += 0.004 * (length - prevLen);
                        double delta = sp/Visualizer.scale;
                        mean = mean.sub(mean.scale(delta));
                        Visualizer.x0 -= mean.x;
                        Visualizer.y0 -= mean.y;
                        prevLen = length;
                    }
                }
                else if(objs.size() != 0) {
                    if (!scaling && pointerCount == 1 && objs.get(pointer).getBody().isInside(prevP)) {

                        objs.get(pointer).getBody().setCenter(point);
                    } else if(pointerCount > 1) {
                        scaling = true;
                        x = event.getX(0);
                        y = event.getY(0);
                        Vector2D point1 = new Vector2D(x, y);
                        x = event.getX(1);
                        y = event.getY(1);
                        Vector2D point2 = new Vector2D(x, y);
                        double nLen = point1.sub(point2).length;
                        dLen = nLen - prevLen;
                        prevLen = nLen;
                        double radius = ((Circle) objs.get(pointer).getBody()).getRadius();
                        if (radius + dLen > 10) {
                            ((Circle) objs.get(pointer).getBody()).setRadius(radius + dLen/Visualizer.scale);
                        }
                    }
                }
                prevP = point;
                reloadFields(null);
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

    public void reloadFields(View view){
        TextView number = (TextView) findViewById(R.id.number);
        EditText
                size = findViewById(R.id.sizeAmount),
                mass = findViewById(R.id.massAmount),
                xA = findViewById(R.id.xAmount),
                yA = findViewById(R.id.yAmount),
                xSpeed = findViewById(R.id.xSpeed),
                ySpeed = findViewById(R.id.ySpeed);
        Button
                prev = (Button) findViewById(R.id.prev),
                next = (Button) findViewById(R.id.next),
                toggle = (Button) findViewById(R.id.button);

        if(pointer != null) {
            PhysObj cur = objs.get(pointer);
            //number.setText(Integer.toString(pointer));
            size.setText(Double.toString(((Circle) cur.getBody()).getRadius()));
            mass.setText(Double.toString(cur.getMass()));
            xA.setText(Double.toString(cur.getCenter().x));
            yA.setText(Double.toString(cur.getCenter().y));
            xSpeed.setText(Double.toString(cur.getSpeed().x));
            ySpeed.setText(Double.toString(cur.getSpeed().y));
//            if (pointer < objs.size() - 1) {
//                next.setVisibility(View.VISIBLE);
//            } else {
//                next.setVisibility(View.INVISIBLE);
//            }
//            if (pointer == 0) {
//                prev.setVisibility(View.INVISIBLE);
//            } else {
//                prev.setVisibility(View.VISIBLE);
//            }
        } else {
            //number.setText("");
            size.setText("");
            mass.setText("");
            xA.setText("");
            yA.setText("");
            xSpeed.setText("");
            ySpeed.setText("");
//            next.setVisibility(View.INVISIBLE);
//            prev.setVisibility(View.INVISIBLE);
        }
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
