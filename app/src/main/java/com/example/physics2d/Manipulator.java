package com.example.physics2d;

public class Manipulator {
    private Vector2D stPos, deltaVector;
    private final static double maxLen = 100;
    private boolean started;
    Manipulator(){
        started = false;
    }

    void set(Vector2D p){
        stPos = p;
        deltaVector = Vector2D.zero();
        started = true;
    }

    void unSet(){
        started = false;
        deltaVector = Vector2D.zero();
        stPos = null;
    }

    void calcDelta(Vector2D p) throws Exception {
        if (!started)
            throw new Exception("You may call set() method before calculating delta Vector");
        if (p.length < maxLen) {
            deltaVector = p.sub(stPos).scale(1 / maxLen);
        } else {
            deltaVector = p.sub(stPos).normalize();
        }
    }

    Vector2D getDelta(){
        return deltaVector.clone();
    }

    Vector2D getManipPoint(){
        return stPos.add(deltaVector.scale(maxLen));
    }

    Vector2D getStPos(){
        return stPos;
    }

}
