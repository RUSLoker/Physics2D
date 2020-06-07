package com.ruslocker.physics2d.game;

import com.ruslocker.physics2d.engine.Vector2D;

public class Manipulator {
    private Vector2D stPos, deltaVector;
    public final static double maxLen = 250;
    private boolean started;
    public Manipulator(){
        deltaVector = Vector2D.zero();
        started = false;
    }

    public void set(Vector2D p){
        stPos = p;
        deltaVector = Vector2D.zero();
        started = true;
    }

    public void unSet(){
        started = false;
        deltaVector = Vector2D.zero();
        stPos = null;
    }

    public void calcDelta(Vector2D p) throws Exception {
        if (!started)
            throw new Exception("You may call set() method before calculating delta Vector");
        Vector2D lenVector = p.sub(stPos);
        if (lenVector.length < maxLen) {
            deltaVector = lenVector.scale(1 / maxLen);
        } else {
            deltaVector = lenVector.normalize();
        }
    }

    public Vector2D getDelta(){
        return deltaVector.clone();
    }

    public Vector2D getManipPoint(){
        if (started) return stPos.add(deltaVector.scale(maxLen));
        else return null;
    }

    public Vector2D getStPos(){
        return stPos;
    }

}
