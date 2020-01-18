package com.example.physics2d;

import androidx.annotation.NonNull;

public class Vector2D {

    public double x, y;

    public Vector2D() {
        x = 0;
        y = 0;
    }

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D v){
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public Vector2D sub(Vector2D v){
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
        return new Vector2D(-this.x, -this.y);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
