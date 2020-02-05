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
    protected static Vector2D getCollision(Polygon a, Polygon b) {
        Vector2D inter;
        Vector2D[] bVert = b.getVertexes();
        Vector2D[] aVert = a.getVertexes();
        inter = dotIndexCheck(aVert, bVert);
        if (inter != null) return inter;
        inter = dotIndexCheck(bVert, aVert);
        return inter;
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

    private static Vector2D dotIndexCheck(Vector2D[] aVert, Vector2D[] bVert){
        for (Vector2D p : aVert) {
            double angle = 0;
            Vector2D k = bVert[bVert.length - 1].sub(p);
            for (Vector2D j : bVert) {
                j = j.sub(p);
                double curAngle = k.angleBetween(j);
                double vPZ = Vector2D.ProdZ(k, j);
                if (vPZ < 0) angle -= curAngle;
                else angle += curAngle;
                k = j;
            }
            angle /= 2 * Math.PI;
            int angInt = (int) angle;
            if (angInt == 1 || angInt == -1) {
                return p;
            }
        }
        return null;
    }
}
