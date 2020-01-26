package com.example.physics2d;

public class Polygon extends Figure2D {
    @Override
    public Vector2D getCollision(Figure2D f) {
        return f.getCollision((Polygon) this);
    }

    @Override
    public Vector2D getCollision(Circle c) {
        return null;
    }

    @Override
    public Vector2D getCollision(Polygon p) {
        return null;
    }
}
