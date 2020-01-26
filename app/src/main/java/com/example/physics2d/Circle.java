package com.example.physics2d;

public class Circle extends Figure2D {

    @Override
    public Vector2D getCollision(Figure2D f) {
        return f.getCollision(this);
    }

    @Override
    public Vector2D getCollision(Circle c) {
        return getCollision(c, this);
    }

    @Override
    public Vector2D getCollision(Polygon p) {
        return getCollision(this, p);
    }
}
