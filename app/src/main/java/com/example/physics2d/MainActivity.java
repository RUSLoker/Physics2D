package com.example.physics2d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static double G = 6.67 * Math.pow(10, -11);
    static Border border = new Border(0, 1000, 1000, 0);
    static public PhysObj[] objs = new PhysObj[]{};
    Thread myThread = new Thread(MainActivity::vrun);
    static boolean work = false;
    static Integer pointer = null;
    static PhysObj standart = new PhysObj(
                    new Circle( new Vector2D(500, 500), 100),
                    100.0
            );
    static double cps = 0;
    static TextView cpsT;
    static TextView checkerT;
    static double checker;
    static boolean gravity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myThread.start();
        reloadFields(null);
        cpsT = findViewById(R.id.cps);
        checkerT = findViewById(R.id.checkerT);
        drawToggle();
        Switch gravitySw = findViewById(R.id.gravitySwitch);
        gravitySw.setOnCheckedChangeListener((buttonView, isChecked) -> gravChecker(isChecked));
    }

    protected void onPause(){
        work = false;
        drawToggle();
        super.onPause();
    }

    static void vrun() {
        long gap = 0;
        double time;
        double fastest = 0;
        long countReal = 0;
        double countSim = 0;
        double log2 = Math.log(2);
        long prev = 0;
        while (true) {
            if (work) {
                fastest = 0;
                for (PhysObj i : objs) {
                    double x = i.getSpeed().length + i.getAcceleration().length;
                    if(x > fastest)
                        fastest = x;
                }

                if(fastest <= 2) time = gap;
                else {
                    //checker = Math.ceil(Math.log10(fastest) * Math.log(checker));
                    checker = Math.log(fastest) / log2;
                    checker = Math.ceil(checker * (Math.pow(1.1, (checker - 150) / 7.2) + 1));

                    time = Math.pow(2, -checker);
                }
                time = time * 1000000000 > gap ? ((double) gap) / 1000000000 : time;

                for (int i = 0; i < objs.length; i++) {
                    PhysObj a = objs[i];
                    for (int j = i + 1; j < objs.length; j++) {
                        PhysObj b = objs[j];
                        a.checkCollisions(b, time);
                    }
                }
                if (gravity){
                    for (int i = 0; i < objs.length; i++) {
                        PhysObj a = objs[i];
                        for (int j = i + 1; j < objs.length; j++) {
                            PhysObj b = objs[j];
                            Vector2D force;
                            Vector2D dist = a.getCenter().sub(b.getCenter());
                            if(dist.length >= ((Circle)a.getBody()).getRadius() + ((Circle)b.getBody()).getRadius()) {
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
                for (PhysObj i : objs) {
                    i.calcAccel();
                    i.move(time);
                    i.checkBorder(border);
                }
                gap = System.nanoTime() - prev;
                countReal += System.nanoTime() - prev;
                countSim += time * 1000000000;
            }
            else {
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

    public void toggle(View view){
        work = !work;
        drawToggle();
    }

    public void drawToggle(){
        Button toggle = (Button) findViewById(R.id.button);
        if(work){
            toggle.setText(R.string.pause);
        } else {
            toggle.setText(R.string.resume);
        }
    }

    public void add(View view){
        boolean pre = work;
        work = false;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pointer = objs.length;
        objs = Arrays.copyOf(objs, objs.length+1);
        objs[pointer] = standart.clone();
        reloadFields(null);
        work = pre;
        drawToggle();
    }

    public void prev(View view){
        pointer--;
        if(pointer<0 && objs.length == 0){
            pointer = null;
        } else if(pointer<0){
            pointer++;
        }
        reloadFields(null);
        drawToggle();
    }

    public void next(View view){
        pointer++;
        reloadFields(null);
        drawToggle();
    }

    public void delete(View view){
        boolean pre = work;
        work = false;
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(objs.length != 0) {
            for (PhysObj i : objs) {
                i.delForce(objs[pointer].hashCode());
            }
            PhysObj[] cutted = new PhysObj[objs.length - 1];
            PhysObj[] a = Arrays.copyOf(objs, pointer);
            PhysObj[] b = Arrays.copyOfRange(objs, pointer + 1, objs.length);
            System.arraycopy(a, 0, cutted, 0, a.length);
            System.arraycopy(b, 0, cutted, a.length, b.length);
            objs = cutted;
            prev(null);
        }
        work = pre;
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
            PhysObj cur = objs[pointer];
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

    public void gravChecker(boolean isChecked){
        gravity = isChecked;
        if(!gravity){
            boolean pre = work;
            work = false;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (PhysObj i : objs){
                i.forces.clear();
            }
            work = pre;
        }
    }


    public void reloadFields(View view){
        TextView number = (TextView) findViewById(R.id.number);
        EditText
                size = (EditText) findViewById(R.id.sizeAmount),
                mass = (EditText) findViewById(R.id.massAmount),
                xA = (EditText) findViewById(R.id.xAmount),
                yA = (EditText) findViewById(R.id.yAmount),
                xSpeed = (EditText) findViewById(R.id.xSpeed),
                ySpeed = (EditText) findViewById(R.id.ySpeed);
        Button
                prev = (Button) findViewById(R.id.prev),
                next = (Button) findViewById(R.id.next),
                toggle = (Button) findViewById(R.id.button);

        if(pointer != null) {
            PhysObj cur = objs[pointer];
            number.setText(Integer.toString(pointer));
            size.setText(Double.toString(((Circle) cur.getBody()).getRadius()));
            mass.setText(Double.toString(cur.getMass()));
            xA.setText(Double.toString(cur.getCenter().x));
            yA.setText(Double.toString(cur.getCenter().y));
            xSpeed.setText(Double.toString(cur.getSpeed().x));
            ySpeed.setText(Double.toString(cur.getSpeed().y));
            if (pointer < objs.length - 1) {
                next.setVisibility(View.VISIBLE);
            } else {
                next.setVisibility(View.INVISIBLE);
            }
            if (pointer == 0) {
                prev.setVisibility(View.INVISIBLE);
            } else {
                prev.setVisibility(View.VISIBLE);
            }
        } else {
            number.setText("");
            size.setText("");
            mass.setText("");
            xA.setText("");
            yA.setText("");
            xSpeed.setText("");
            ySpeed.setText("");
            next.setVisibility(View.INVISIBLE);
            prev.setVisibility(View.INVISIBLE);
        }
    }
}
