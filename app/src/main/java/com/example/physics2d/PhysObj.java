package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class PhysObj implements Cloneable{
    private Figure2D body;
    private Vector2D velocity, acceleration, force;
    private double mass;
    private HashMap<Integer, Vector2D> forces = new HashMap<>();


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

    public void calcAccel(){
        force = Vector2D.zero();
        for (Vector2D i : forces.values()){
            force = force.add(i);
        }
        acceleration = force.scale(1/mass);
    }

    public void move(double time){
        body.move(new Vector2D(
                velocity.x*time + acceleration.x * time*time/2,
                velocity.y*time + acceleration.y * time*time/2
        ));
        velocity = velocity.add(acceleration.scale(time));
    }

    public Vector2D getCenter(){
        return body.getCenter();
    }
    public Vector2D getSpeed(){
        return velocity;
    }
    public Vector2D getAcceleration(){
        return acceleration;
    }
    public Vector2D getForce(){
        return force;
    }
    public Figure2D getBody(){
        return body;
    }
    public double getMass() {
        return mass;
    }

    public void setMass(double mass){
        this.mass = mass;
    }

    public void setForce(Integer hash, Vector2D force){
//        Vector2D curF;
//        if ((curF = forces.get(hash)) != null){
//            this.force = force.add(force.sub(curF));
//        } else {
//            this.force = force.add(force);
//        }
        forces.put(hash, force);
    }

    public void setVelocity(Vector2D velocity){
        this.velocity = velocity;
    }

    public void checkCollisions(PhysObj obj, double time){
        Vector2D[] collisions;
        if((collisions = this.body.getCollision(obj.body)).length != 0){
            Vector2D[] normals = this.body.getNormals(collisions);
            for (int i = 0; i < normals.length; i++) {
                Vector2D a = normals[i];
                double dt = 0;

                double dx = collisions[i].sub(this.getCenter()).length;
                dx =  ((Circle)body).getRadius() - dx;
                if(dx > 1){
                    Vector2D relSpeed = obj.velocity.sub(this.velocity);
                    Vector2D relAcc = obj.acceleration.sub(this.acceleration);
                    double vel = relSpeed.projection(a.reverse());
                    double acc = relAcc.projection(a.reverse());
                    double sqrtD = Math.sqrt(vel*vel - 2*acc*dx);
                    double t = (sqrtD - vel) / acc;
                    if(t <= 0 || t > time){
                        t = (-sqrtD - vel) / acc;
                    }
                    dt = time - t;
                    this.move(-t);
                    obj.move(-t);
                }

                double projA = this.velocity.projection(a);
                double projB = obj.velocity.projection(a);
                double v1 = (2*obj.mass*projB + projA*(this.mass - obj.mass)) / (this.mass + obj.mass);
                double v2 = (2*this.mass*projA + projB*(obj.mass - this.mass)) / (this.mass + obj.mass);
                v1 -= projA;
                v2 -= projB;
                this.velocity = this.velocity.add(a.scale(v1));
                obj.velocity = obj.velocity.add(a.scale(v2));
                this.move(dt);
                obj.move(dt);
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
            case ltNdn:
            case ltNup:
            case rtNdn:
            case rtNup:
                velocity = velocity.reverse();
                break;
        }
    }

    @NonNull
    @Override
    protected PhysObj clone() {
        return new PhysObj(body.clone(), mass, velocity.clone(), acceleration.clone(), force.clone());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
