package com.example.physics2d;

import android.graphics.Canvas;

public abstract class Figure2D extends Collision {
    public abstract Vector2D getCollision(Figure2D figure);
    public abstract Vector2D getCollision(Circle circle);
    public abstract Vector2D getCollision(Polygon polygon);
    public abstract void draw(Canvas canvas);
}
