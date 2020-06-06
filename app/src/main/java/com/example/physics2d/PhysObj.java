package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class PhysObj implements Cloneable{
    private Figure2D body;
    private Vector2D velocity, acceleration, force;
    private double mass;
    HashMap<Integer, Vector2D> forces = new HashMap<>();
    private boolean borderChecked = false;


    PhysObj(Figure2D body, double mass, Vector2D velocity, Vector2D acceleration, Vector2D force) {
        this.body = body;
        this.velocity = velocity.clone();
        this.acceleration = acceleration.clone();
        this.mass = mass;
        this.force = force.clone();
    }
    PhysObj(Figure2D body , double mass) {
        this(body, mass, Vector2D.zero(), Vector2D.zero(), Vector2D.zero());
    }
    public PhysObj(Figure2D body , double mass, Vector2D velocity) {
        this(body, mass, velocity, Vector2D.zero(), Vector2D.zero());
    }

    void draw(double x, double y, double scale, Canvas canvas, Paint paint){
        body.draw(x, y, scale, canvas, paint);
    }

    void calcAccel(){
        force = Vector2D.zero();
        for (Vector2D i : forces.values()){
            force = force.add(i);
        }
        acceleration = force.scale(1/mass);
    }

    void move(double time){
        body.move(new Vector2D(
                velocity.x*time + acceleration.x * time*time/2,
                velocity.y*time + acceleration.y * time*time/2
        ));
        if(!acceleration.equals(Vector2D.zero))
            velocity = velocity.add(acceleration.scale(time));
    }

    Vector2D getCenter(){
        return body.getCenter();
    }
    Vector2D getSpeed(){
        return velocity;
    }
    Vector2D getAcceleration(){
        return acceleration;
    }
    Vector2D getForce(){
        return force;
    }
    Figure2D getBody(){
        return body;
    }
    double getMass() {
        return mass;
    }

    void setMass(double mass){
        this.mass = mass;
    }

    void setForce(Integer hash, Vector2D force){
        forces.put(hash, force);
    }

    void delForce(Integer hash)  {
        if(forces.get(hash) != null){
            forces.remove(hash);
        }
    }

    void setVelocity(Vector2D velocity){
        this.velocity = velocity;
    }

    void checkCollisions(PhysObj obj, double time){
        Vector2D[] collisions;
        if((collisions = this.body.getCollision(obj.body)).length != 0){
            Vector2D[] normals = obj.body.getNormals(collisions);
            for (int i = 0; i < normals.length; i++) {
                Vector2D a = normals[i].reverse();
                double dt = 0;

                double dx = collisions[i].sub(this.getCenter()).length;
                dx =  ((Circle)body).getRadius() - dx;
                separate:
                {
                    Vector2D relSpeed = obj.velocity.sub(this.velocity);
                    Vector2D relAcc = obj.acceleration.sub(this.acceleration);
                    double vel = relSpeed.projection(a.reverse());
                    double acc = relAcc.projection(a.reverse());
                    double sqrtD = Math.sqrt(vel*vel - 2*acc*dx);
                    double t = (sqrtD - vel) / acc;
                    if(t <= 0 || t > time || Double.isNaN(t)){
                        t = (-sqrtD - vel) / acc;
                    }
                    if(!Double.isNaN(t) && Double.isFinite(t)) {
                        dt = time - t;
                        this.move(-t);
                        obj.move(-t);
                    } else {
                        Vector2D movement = a.scale(dx * 0.5);
                        this.body.move(movement);
                        obj.body.move(movement.reverse());
                        break separate;
                    }
                }

                double projA = this.velocity.projection(a);
                double projB = obj.velocity.projection(a);
                double v1 = (2*obj.mass*projB + projA*(this.mass - obj.mass)) / (this.mass + obj.mass);
                double v2 = (2*this.mass*projA + projB*(obj.mass - this.mass)) / (this.mass + obj.mass);
                v1 -= projA;
                v2 -= projB;
                this.velocity = this.velocity.add(a.scale(v1));
                obj.velocity = obj.velocity.add(a.scale(v2));
                if(dt != 0 && !Double.isNaN(dt) && Double.isFinite(dt)) {
                    this.move(dt);
                    obj.move(dt);
                }
            }
        }
    }

    boolean checkBorder(Border border){
        switch (body.borderCollision(border)){
            case left: {
                if (borderChecked) {
                    double dx = getCenter().x - ((Circle) body).getRadius() - border.L;
                    body.move(new Vector2D(-dx, 0));
                }
                borderChecked = true;
                velocity = velocity.reverseX();
                break;
            }
            case right: {
                if (borderChecked) {
                    double dx = getCenter().x + ((Circle) body).getRadius() - border.R;
                    body.move(new Vector2D(-dx, 0));
                }
                velocity = velocity.reverseX();
                borderChecked = true;
                break;
            }
            case down: {
                if (borderChecked) {
                    double dy = getCenter().y - ((Circle) body).getRadius() - border.D;
                    body.move(new Vector2D(0, -dy));
                }
                borderChecked = true;
                velocity = velocity.reverseY();
                break;
            }
            case up: {
                if (borderChecked) {
                    double dy = getCenter().y + ((Circle) body).getRadius() - border.U;
                    body.move(new Vector2D(0, -dy));
                }
                borderChecked = true;
                velocity = velocity.reverseY();
                break;
            }
            case ltNdn: {
                if (borderChecked) {
                    double dx = getCenter().x - ((Circle) body).getRadius() - border.L;
                    double dy = getCenter().y - ((Circle) body).getRadius() - border.D;
                    body.move(new Vector2D(-dx, -dy));
                }
                borderChecked = true;
                velocity = velocity.reverse();
                break;
            }
            case ltNup: {
                if (borderChecked) {
                    double dx = getCenter().x - ((Circle) body).getRadius() - border.L;
                    double dy = getCenter().y + ((Circle) body).getRadius() - border.U;
                    body.move(new Vector2D(-dx, -dy));
                }
                borderChecked = true;
                velocity = velocity.reverse();
                break;
            }
            case rtNdn: {
                if (borderChecked) {
                    double dx = getCenter().x + ((Circle) body).getRadius() - border.R;
                    double dy = getCenter().y - ((Circle) body).getRadius() - border.D;
                    body.move(new Vector2D(-dx, -dy));
                }
                borderChecked = true;
                velocity = velocity.reverse();
                break;
            }
            case rtNup: {
                if (borderChecked) {
                    double dx = getCenter().x + ((Circle) body).getRadius() - border.R;
                    double dy = getCenter().y + ((Circle) body).getRadius() - border.U;
                    body.move(new Vector2D(-dx, -dy));
                }
                borderChecked = true;
                velocity = velocity.reverse();
                break;
            }
            default:
                borderChecked = false;
        }
        return borderChecked;
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
