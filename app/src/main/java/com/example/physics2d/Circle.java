package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Figure2D {

    private Vector2D center;
    private double radius;

    public Circle(Vector2D center, double radius) {
        this.center = center.clone();
        this.radius = radius;
    }

    public double getRadius(){
        return radius;
    }

    @Override
    public Vector2D[] getCollision(Figure2D figure) {
        return figure.getCollision(this);
    }

    @Override
    public Vector2D[] getCollision(Circle circle) {
        return new Vector2D[]{getCollision(circle, this)};
    }

    @Override
    public Vector2D[] getCollision(Polygon polygon) {
        return getCollision(this, polygon);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle((float) center.x, (float) center.y, (float) radius, paint);
    }

    @Override
    public Vector2D getCenter() {
        return center;
    }

    @Override
    public void move(Vector2D movement) {
        center = center.add(movement);
    }

    @Override
    public void rotate(double angle, Vector2D point) {
    }

    @Override
    public double perimeter() {
        return 0;
    }


    @Override
    public double square() {
        return Math.PI * radius * radius;
    }

    @Override
    public Figure2D clone() {
        return new Circle(center.clone(), radius);
    }
}
