package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Figure2D extends Collision implements Cloneable {
    public abstract Vector2D[] getCollision(Figure2D figure);
    public abstract Vector2D[] getCollision(Circle circle);
    public abstract Vector2D[] getCollision(Polygon polygon);
    public abstract boolean isInside(Vector2D point);
    public abstract Vector2D[] getNormals(Vector2D[] points);
    public abstract Orientation borderCollision(Border border);
    public abstract void draw(Canvas canvas, Paint paint);
    public abstract Vector2D getCenter();
    public abstract void move(Vector2D movement);
    public abstract void rotate(double angle ,Vector2D point);
    public abstract double perimeter();
    public abstract double square();
    @Override
    public abstract Figure2D clone();
    public abstract void setCenter(Vector2D point);
}
