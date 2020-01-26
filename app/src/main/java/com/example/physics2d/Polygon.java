package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Polygon extends Figure2D {
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
        return getCollision(polygon, this);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

    }
}
