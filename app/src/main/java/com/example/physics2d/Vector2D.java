package com.example.physics2d;

import androidx.annotation.NonNull;

public class Vector2D implements Cloneable {

    public final double x, y, length, sqrLength;

    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
        sqrLength = sqrLength(x, y);
        length = length(x, y);
    }

    private Vector2D(Vector2D v){
        this(v.x, v.y, v.length, v.sqrLength);
    }

    private Vector2D(double x, double y, double len, double sqrLength){
        this.x = x;
        this.y = y;
        this.length = len;
        this.sqrLength = sqrLength;
    }

    private Vector2D() {
        x = 0;
        y = 0;
        length = 0;
        sqrLength = 0;
    }

    public Vector2D add(@NonNull Vector2D v){
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    public static Vector2D sum(Vector2D[] vArr){
        Vector2D vector = new Vector2D();
        for (Vector2D i : vArr) {
            vector = vector.add(i);
        }
        return vector;
    }

    public Vector2D sub(@NonNull Vector2D v){
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    public Vector2D scale(double mult){
        return new Vector2D(this.x * mult, this.y * mult,
                length * mult, sqrLength * mult*mult);
    }

    public Vector2D reverse(){
        return new Vector2D(-this.x, -this.y, length, sqrLength);
    }

    public static double length(double x, double y){
        return Math.sqrt(x*x + y*y);
    }

    public static double sqrLength(double x, double y){
        return x*x + y*y;
    }

    public Vector2D rotate(double angle){
        return rotate(Math.sin(angle), Math.cos(angle));
    }

    public Vector2D rotate(double sinA, double cosA){
        double x = this.x * cosA - this.y * sinA;
        double y = this.y * cosA + this.x * sinA;
        return new Vector2D(x, y).setLength(length);
    }

    public static Vector2D zero() {
        return new Vector2D();
    }

    public Vector2D normalize(){
        if (length == 0){
            return new Vector2D(this);
        }
        double len = length(x, y);
        return new Vector2D(x/len, y/len, 1, 1);
    }

    public double scalar(Vector2D v){
        return this.x * v.x + this.y * v.y;
    }

    public static Vector2D intersection(Vector2D a, Vector2D b, Vector2D c, Vector2D d){
        double[] x = {0, a.x, b.x, c.x, d.x},
                y = {0, a.y, b.y, c.y, d.y};
        double fp = x[1] * y[2] - y[1] * x[2];
        double sp = x[3] * y[4] - y[3] * x[4];
        double tp = (x[1] - x[2]) * (y[3] - y[4]) - (y[1] - y[2]) * (x[3] - x[4]);
        return new Vector2D(
                (fp *(x[3] - x[4]) - (x[1] - x[2])* sp)/ tp,
                (fp *(y[3] - y[4]) - (y[1] - y[2])* sp)/ tp

        );
    }

    public static double ProdZ(Vector2D a, Vector2D b){
        return a.x * b.y - a.y * b.x;
    }

    public double angleBetween(Vector2D v){
        return Math.acos(this.normalize().scalar(v.normalize()));
    }

    public Vector2D setLength(double len){
        if (length == len){
            return this.clone();
        }
        return this.normalize().scale(len);
    }



    @NonNull
    @Override
    public Vector2D clone() {
        return new Vector2D(this);
    }

    @NonNull
    @Override
    public String toString() {
        return "( " + x + ", " + y + " )";
    }

    @Override
    public boolean equals(Object obj) {
        if(obj.getClass() == getClass()) {
            Vector2D v = (Vector2D) obj;
            return x == v.x && y == v.y;
        }
        return false;
    }
}
