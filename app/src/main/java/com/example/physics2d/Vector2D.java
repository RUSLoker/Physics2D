package com.example.physics2d;

import androidx.annotation.NonNull;

public class Vector2D implements Cloneable {

    public double x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D(Vector2D v){
        this(v.x, v.y);
    }

    public Vector2D add(@NonNull Vector2D v){
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D sub(@NonNull Vector2D v){
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D scale(double mult){
        return new Vector2D(this.x * mult, this.y * mult);
    }

    public Vector2D reverse(){
        return new Vector2D(-this.x, -this.y);
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public Vector2D rotate(double angle){
        return rotate(Math.sin(angle), Math.cos(angle));
    }

    public Vector2D rotate(double sinA, double cosA){
        double x = this.x * cosA - this.y * sinA;
        double y = this.y * cosA + this.x * sinA;
        return new Vector2D(x, y);
    }

    public static Vector2D zero() {
        return new Vector2D();
    }

    public Vector2D normalize(){
        return new Vector2D(x/length(), y/length());
    }

    public double scalar(Vector2D v){
        return this.x * v.x + this.y * v.y;
    }

    public Vector2D intersection(Vector2D a, Vector2D b, Vector2D c, Vector2D d){
        return new Vector2D(
                ((a.x*b.y - a.y*b.x)*(c.x - d.x) - (a.x - b.x)*(c.x*d.y - c.y*d.x))/
                        ((a.x - b.x)*(c.y - d.y) - (a.y - b.y)*(c.x - b.x)),
                ((a.x*b.y - a.y*b.x)*(c.y - d.y) - (a.y - b.y)*(c.x*d.y - c.y*d.x))/
                        ((a.x - b.x)*(c.y - d.y) - (a.y - b.y)*(c.x - b.x))
                );
    }



    @NonNull
    @Override
    public Vector2D clone() {
        return new Vector2D(this);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
