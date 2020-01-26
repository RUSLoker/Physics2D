package com.example.physics2d;

public abstract class Figure2D {
    public abstract Vector2D getCollision(Figure2D f);
    public abstract Vector2D getCollision(Circle f);
    public abstract Vector2D getCollision(Polygon f);
}
