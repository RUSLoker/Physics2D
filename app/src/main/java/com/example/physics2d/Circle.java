package com.example.physics2d;

public class Circle extends Figure2D {

    @Override
    public Vector2D getCollision(Figure2D f) {
        return f.getCollision((Circle) this);
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
