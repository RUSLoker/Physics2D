package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Polygon extends Figure2D {
    private Vector2D[] vertexes;
    private Vector2D center;

    public Polygon(Vector2D[] vertexes) {
        for(int i = 0; i < vertexes.length; i++){
            vertexes[i] = vertexes[i].clone();
        }
        this.vertexes = vertexes;
        this.center = centerCalc(vertexes);
    }

    @Override
    public Vector2D getCollision(Figure2D figure) {
        return figure.getCollision(this);
    }

    @Override
    public Vector2D getCollision(Circle circle) {
        return getCollision(circle, this);
    }

    @Override
    public Vector2D getCollision(Polygon polygon) {
        return getCollision(polygon, this);
    }

    @Override
    public void draw(Canvas canvas, Paint paint){
        int i = 1;
        for (; i < vertexes.length; i++) {
            canvas.drawLine(
                    (float) vertexes[i-1].x,
                    (float) vertexes[i-1].y,
                    (float) vertexes[i].x,
                    (float) vertexes[i].y,
                    paint
            );
        }
        i--;
        canvas.drawLine(
                (float) vertexes[0].x,
                (float) vertexes[0].y,
                (float) vertexes[i].x,
                (float) vertexes[i].y,
                paint
        );
    }

    @Override
    public Vector2D getCenter() {
        return center;
    }

    @Override
    public void move(Vector2D movement) {
        for (int i = 0; i < vertexes.length; i++) {
            Vector2D vertex = vertexes[i];
            vertexes[i] = vertex.add(movement);
        }
        center = center.add(movement);
    }

    @Override
    public void rotate(double angle, Vector2D point) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        for (int i = 0; i < vertexes.length; i++) {
            vertexes[i] = vertexes[i].sub(point).rotate(sin, cos).add(point);
        }
    }

    @Override
    public double perimeter() {
        double P = 0;
        for (int i = 1; i < vertexes.length; i++) {
            P += vertexes[i].sub(vertexes[i-1]).length;
        }
        P += vertexes[vertexes.length - 1].sub(vertexes[0]).length;
        return P;
    }

    @Override
    public double square() {
        return 0;
    }

    private static Vector2D centerCalc(Vector2D[] vertexes){
        double x = 0, y = 0;
        for (Vector2D i : vertexes){
            x += i.x;
            y += i.y;
        }
        return new Vector2D(x/vertexes.length, y/vertexes.length);
    }
}