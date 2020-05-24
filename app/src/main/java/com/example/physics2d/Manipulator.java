package com.example.physics2d;

public class Manipulator {
    private Vector2D stPos, deltaVector;
    private final static double maxLen = 300;
    private boolean started;
    Manipulator(){
        deltaVector = Vector2D.zero();
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
        Vector2D lenVector = p.sub(stPos);
        if (lenVector.length < maxLen) {
            deltaVector = lenVector.scale(1 / maxLen);
        } else {
            deltaVector = lenVector.normalize();
        }
        System.out.println(deltaVector.length + " " + lenVector.length);
    }

    Vector2D getDelta(){
        return deltaVector.clone();
    }

    Vector2D getManipPoint(){
        if (started) return stPos.add(deltaVector.scale(maxLen));
        else return null;
    }

    Vector2D getStPos(){
        return stPos;
    }

}