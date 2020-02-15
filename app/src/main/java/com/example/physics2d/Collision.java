package com.example.physics2d;

import java.util.ArrayList;

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
    protected static Vector2D[] getCollision(Polygon a, Polygon b) {
        Vector2D[] returnable;
        ArrayList<Vector2D> intersections = new ArrayList<>();
        Vector2D[] bVert = b.getVertexes();
        Vector2D[] aVert = a.getVertexes();
        dotIndexCheck(aVert, bVert, intersections);
        dotIndexCheck(bVert, aVert, intersections);
        returnable = new Vector2D[intersections.size()];
        intersections.toArray(returnable);
        return returnable;
    }
    protected static Vector2D[] getCollision(Circle a, Polygon b){
        Vector2D[] returnable;
        ArrayList<Vector2D> intersections = new ArrayList<>();
        Vector2D cent = a.getCenter();
        double rad = a.getRadius();
        for (Vector2D i : b.getVertexes()){
            if(i.sub(cent).length <= rad)
                intersections.add(i);
        }
        returnable = new Vector2D[intersections.size()];
        intersections.toArray(returnable);
        return returnable;
    }

    private static void dotIndexCheck(Vector2D[] a, Vector2D[] b, ArrayList<Vector2D> arrayList){
        for (Vector2D p : a) {
            double angle = 0;
            Vector2D k = b[b.length - 1].sub(p);
            for (Vector2D j : b) {
                j = j.sub(p);
                double curAngle = Vector2D.angleBetween(k, j);
                double vPZ = Vector2D.prodZ(k, j);
                if (vPZ < 0) angle -= curAngle;
                else angle += curAngle;
                k = j;
            }
            angle /= 2 * Math.PI;
            int angInt = (int) angle;
            if (angInt == 1 || angInt == -1) {
                arrayList.add(p);
            }
        }
    }
}
