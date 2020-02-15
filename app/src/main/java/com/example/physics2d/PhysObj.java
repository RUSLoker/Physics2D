package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import androidx.annotation.NonNull;

public class PhysObj implements Cloneable{
    private Figure2D body;
    private Vector2D speed, acceleration, force;
    private double mass;
    private SparseArray<Vector2D> forces = new SparseArray<>();


    public PhysObj(Figure2D body, double mass, Vector2D speed, Vector2D acceleration, Vector2D force) {
        this.body = body;
        this.speed = speed.clone();
        this.acceleration = acceleration.clone();
        this.mass = mass;
        this.force = force.clone();
    }
    public PhysObj(Figure2D body , double mass) {
        this(body, mass, Vector2D.zero(), Vector2D.zero(), Vector2D.zero());
    }

    public void draw(Canvas canvas, Paint paint){
        body.draw(canvas, paint);
    }

    public void move(double time){
        acceleration = force.scale(1/mass);
        speed = speed.add(acceleration.scale(time));
        body.move(speed.scale(time));
    }

    public Vector2D getCenter(){
        return body.getCenter().clone();
    }
    public Vector2D getSpeed(){
        return speed.clone();
    }
    public Vector2D getAcceleration(){
        return acceleration.clone();
    }
    public Vector2D getForce(){
        return force.clone();
    }
    public Figure2D getBody(){
        return body.clone();
    }
    public double getMass() {
        return mass;
    }

    public void setForce(Integer hash, Vector2D force){
        Vector2D curF;
        if ((curF = forces.get(hash)) != null){
            this.force = force.add(force.sub(curF));
        } else {
            this.force = force.add(force);
        }
        forces.put(hash, force);
    }

    public void checkCollisions(PhysObj obj){

    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new PhysObj(body.clone(), mass, speed.clone(), acceleration.clone(), force.clone());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
