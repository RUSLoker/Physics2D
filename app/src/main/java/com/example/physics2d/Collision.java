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
        boolean flag = false;
        Vector2D inter;
        Vector2D[] bVert = b.getVertexes();
        Vector2D[] aVert = a.getVertexes();
        for (Vector2D i : aVert) {
            for (int j = 1; j < bVert.length; j++) {
                if(polColCheck(i, bVert[j], bVert[j-1])){
                    flag = !flag;
                }
            }
            if(polColCheck(i, bVert[0], bVert[bVert.length-1])){
                flag = !flag;
            }
            if (flag) return i;
        }
        for (Vector2D i : bVert) {
            for (int j = 1; j < aVert.length; j++) {
                if(polColCheck(i, aVert[j], aVert[j-1])){
                    flag = !flag;
                }
            }
            if(polColCheck(i, aVert[0], aVert[aVert.length-1])){
                flag = !flag;
            }
            if (flag) return i;
        }
        return null;
    }
    protected static Vector2D getCollision(Circle a, Polygon b){
        return null;
    }

    private static boolean polColCheck(Vector2D point, Vector2D a, Vector2D b){
        boolean flag;
        Vector2D inter = Vector2D.intersection(point, new Vector2D(point.x + 1, point.y), a, b);
        double
                L = a.x <= b.x ? a.x : b.x,
                R = a.x > b.x ? a.x : b.x,
                T = a.y >= b.y ? a.y : b.y,
                B = a.y < b.y ? a.y : b.y;
        flag = inter.x >= L &&
                inter.x <= R &&
                inter.y <= T &&
                inter.y >= B &&
                inter.x >= point.x;
        return flag;
    }
}
