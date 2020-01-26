package com.example.physics2d;

public class Polygon extends Figure2D {
    @Override
    public Vector2D getCollision(Figure2D f) {
        return null;
    }

    @Override
    public Vector2D getCollision(Circle f) {
        return null;
    }

    @Override
    public Vector2D getCollision(Polygon f) {
        return null;
    }
}
