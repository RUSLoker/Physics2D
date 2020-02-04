package com.example.physics2d;

public abstract class Collision {

    protected static Vector2D getCollision(Circle a, Circle b){
        Vector2D dC = a.getCenter().sub(b.getCenter());
        double centDist = a.getRadius() + b.getRadius();
        if(dC.length <= centDist){
            dC = dC.setLength(dC.length / 2);
            return dC.add(b.getCenter());
        }
        return null;
    }
    protected static Vector2D getCollision(Polygon a, Polygon b){
        return null;
    }
    protected static Vector2D getCollision(Circle a, Polygon b){
        return null;
    }
}
