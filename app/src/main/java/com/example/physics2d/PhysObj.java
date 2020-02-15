package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;

import androidx.annotation.NonNull;

public class PhysObj implements Cloneable{
    private Figure2D body;
    private Vector2D velocity, acceleration, force;
    private double mass;
    private SparseArray<Vector2D> forces = new SparseArray<>();


    public PhysObj(Figure2D body, double mass, Vector2D velocity, Vector2D acceleration, Vector2D force) {
        this.body = body;
        this.velocity = velocity.clone();
        this.acceleration = acceleration.clone();
        this.mass = mass;
        this.force = force.clone();
    }
    public PhysObj(Figure2D body , double mass) {
        this(body, mass, Vector2D.zero(), Vector2D.zero(), Vector2D.zero());
    }
    public PhysObj(Figure2D body , double mass, Vector2D velocity) {
        this(body, mass, velocity, Vector2D.zero(), Vector2D.zero());
    }

    public void draw(Canvas canvas, Paint paint){
        body.draw(canvas, paint);
    }

    public void move(double time){
        acceleration = force.scale(1/mass);
        velocity = velocity.add(acceleration.scale(time));
        body.move(velocity.scale(time));
    }

    public Vector2D getCenter(){
        return body.getCenter().clone();
    }
    public Vector2D getSpeed(){
        return velocity.clone();
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
        Vector2D[] collisions;
        if((collisions = this.body.getCollision(obj.body)).length != 0){
            Vector2D[] normals = body.getNormals(collisions);
            for (int i = 0; i < normals.length; i++) {
                Vector2D a = normals[i];
                double projA = this.velocity.projection(a);
                double projB = obj.velocity.projection(a);
                double v1 = (2*obj.mass*projB + projA*(this.mass - obj.mass)) / (this.mass + obj.mass);
                double v2 = (2*this.mass*projA + projB*(obj.mass - this.mass)) / (this.mass + obj.mass);
                v1 -= projA;
                v2 -= projB;
                this.velocity = this.velocity.add(a.scale(v1));
                obj.velocity = obj.velocity.add(a.scale(v2));
            }
        }
    }

    public void checkBorder(Border border){
        switch (body.borderCollision(border)){
            case left:
            case right:
                velocity = velocity.reverseX();
                break;
            case down:
            case up:
                velocity = velocity.reverseY();
                break;
        }
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new PhysObj(body.clone(), mass, velocity.clone(), acceleration.clone(), force.clone());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
