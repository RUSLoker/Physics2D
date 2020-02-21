package com.example.physics2d;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

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
    int pointer = 0;

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
}
