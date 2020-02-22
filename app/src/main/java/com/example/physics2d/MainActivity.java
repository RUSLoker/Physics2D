package com.example.physics2d;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static double G = 6.67 * Math.pow(10, -11);
    static Border border = new Border(0, 1000, 1000, 0);
    static public PhysObj[] objs = new PhysObj[]{
//            new PhysObj(
//                    new Circle( new Vector2D(500, 600), 30),
//                    1000000000000000000.0
//                    , new Vector2D(120, 0)
//            ),
//            new PhysObj(
//                    new Circle( new Vector2D(500, 500), 50),
//                    1000000000000000000.0
//                    , new Vector2D(-100, 0)
//            ),
//            new PhysObj(
//                    new Circle( new Vector2D(500, 200), 50),
//                    5000000000.0
//                    , new Vector2D(600, 0)
//            ),
//            new PhysObj(
//                    new Circle( new Vector2D(500, 800), 50),
//                    5000000000.0
//                    , new Vector2D(-700, 0)
//            )
    };
    Thread myThread = new Thread(this::vrun);
    static boolean work = false;
    static Integer pointer = null;
    static PhysObj standart = new PhysObj(
                    new Circle( new Vector2D(500, 500), 100),
                    100.0
            );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myThread.start();
    }

    protected void onPause(){
        work = false;
        super.onPause();
    }

    public void vrun() {
        double time = 0.000005;
        while (true) {
            if (work) {
                for (int i = 0; i < objs.length; i++) {
                    PhysObj a = objs[i];
                    for (int j = i + 1; j < objs.length; j++) {
                        PhysObj b = objs[j];
                        Vector2D dist = a.getCenter().sub(b.getCenter());
                        double forceAbs = G * (a.getMass() * b.getMass()) / (dist.length * dist.length);
                        forceAbs = forceAbs == 1.0 / 0.0 ? 0 : forceAbs;
                        Vector2D force = dist.setLength(forceAbs);
                        a.setForce(b.hashCode(), force.reverse());
                        b.setForce(a.hashCode(), force);
                    }
                }
                for (int i = 0; i < objs.length; i++) {
                    PhysObj a = objs[i];
                    for (int j = i + 1; j < objs.length; j++) {
                        PhysObj b = objs[j];
                        a.checkCollisions(b, time);
                    }
                }
                for (PhysObj i : objs) {
                    i.calcAccel();
                    i.move(time);
                    i.checkBorder(border);
                }
            }
            else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void toggle(View view){
        work = !work;
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
        reloadFields();
        work = pre;
    }

    public void prev(View view){
        pointer--;
        if(pointer<0 && objs.length == 0){
            pointer = null;
        } else if(pointer<0){
            pointer++;
        }
        reloadFields();
    }

    public void next(View view){
        pointer++;
        reloadFields();
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
            PhysObj[] cutted = new PhysObj[objs.length - 1];
            PhysObj[] a = Arrays.copyOf(objs, pointer);
            PhysObj[] b = Arrays.copyOfRange(objs, pointer + 1, objs.length);
            System.arraycopy(a, 0, cutted, 0, a.length);
            System.arraycopy(b, 0, cutted, a.length, b.length);
            objs = cutted;
            prev(null);
        }
        work = pre;
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
            case View.VISIBLE:
                speed.setVisibility(View.INVISIBLE);
                xSpeed.setVisibility(View.INVISIBLE);
                ySpeed.setVisibility(View.INVISIBLE);
                coords.setVisibility(View.VISIBLE);
                xA.setVisibility(View.VISIBLE);
                yA.setVisibility(View.VISIBLE);
                break;
            case View.INVISIBLE:
                speed.setVisibility(View.VISIBLE);
                xSpeed.setVisibility(View.VISIBLE);
                ySpeed.setVisibility(View.VISIBLE);
                coords.setVisibility(View.INVISIBLE);
                xA.setVisibility(View.INVISIBLE);
                yA.setVisibility(View.INVISIBLE);
                break;
            case View.GONE:
                break;
        }
    }

    public void set(View view){
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
        reloadFields();
    }


    public void reloadFields(){
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
                next = (Button) findViewById(R.id.next);

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
