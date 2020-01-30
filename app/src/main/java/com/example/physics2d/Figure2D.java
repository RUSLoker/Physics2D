package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Figure2D extends Collision {
    public abstract Vector2D getCollision(Figure2D figure);
    public abstract Vector2D getCollision(Circle circle);
    public abstract Vector2D getCollision(Polygon polygon);
    public abstract void draw(Canvas canvas, Paint paint);
    public abstract Vector2D getCenter();
    public abstract void move(Vector2D movement);
    public abstract void rotate(double angle ,Vector2D point);
    public abstract double perimeter();
    public abstract double square();
}
