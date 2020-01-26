package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Figure2D {

    public final Vector2D center;
    public final double radius;

    public Circle(Vector2D center, double radius) {
        this.center = center.clone();
        this.radius = radius;
    }

    @Override
    public Vector2D getCollision(Figure2D figure) {
        return figure.getCollision(this);
    }

    @Override
    public Vector2D getCollision(Circle circle) {
        return getCollision(circle, this);
    }

    @Override
    public Vector2D getCollision(Polygon polygon) {
        return getCollision(this, polygon);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle((float) center.x, (float) center.y, (float) radius, paint);
    }
}
