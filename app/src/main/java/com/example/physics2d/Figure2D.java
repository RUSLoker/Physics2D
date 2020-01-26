package com.example.physics2d;

public abstract class Figure2D extends Collision {
    public abstract Vector2D getCollision(Figure2D f);
    public abstract Vector2D getCollision(Circle c);
    public abstract Vector2D getCollision(Polygon p);
}
