package com.ruslocker.physics2d.engine;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circle extends Figure2D {

    private Vector2D center;
    private double radius;

    public Circle(Vector2D center, double radius) {
        this.center = center.clone();
        this.radius = radius;
    }

    public double getRadius(){
        return radius;
    }

    @Override
    public Vector2D getCenter() {
        return center;
    }

    public void setRadius(double radius){
        this.radius = radius;
    }

    public void setCenter(Vector2D point){
        center = point;
    }

    @Override
    public Vector2D[] getCollision(Figure2D figure) {
        return figure.getCollision(this);
    }

    @Override
    public Vector2D[] getCollision(Circle circle) {
        return getCollision(circle, this);
    }

    @Override
    public Vector2D[] getCollision(Polygon polygon) {
        return getCollision(this, polygon);
    }

    @Override
    public boolean isInside(Vector2D point) {
        return point.sub(center).length <= radius;
    }

    @Override
    public Vector2D[] getNormals(Vector2D[] points) {
        Vector2D[] finish = new Vector2D[points.length];
        for (int i = 0; i < finish.length; i++) {
            finish[i] = center.sub(points[i]).normalize();
        }
        return finish;
    }

    @Override
    public Orientation borderCollision(Border border) {

        boolean left = center.x - radius < border.L,
                right = center.x + radius > border.R,
                up = center.y + radius > border.U,
                down = center.y - radius < border.D;
        if(left && up){
            return Orientation.ltNup;
        }
        else if(left && down){
            return Orientation.ltNdn;
        }
        else if(right && down){
            return Orientation.rtNdn;
        }
        else if(right && up){
            return Orientation.rtNup;
        }
        else if(left){
            return Orientation.left;
        }
        else if(right){
            return Orientation.right;
        }
        else if(up){
            return Orientation.up;
        }
        else if(down){
            return Orientation.down;
        }
        return Orientation.none;
    }

    @Override
    public void draw(double x, double y, double scale, Canvas canvas, Paint paint) {
        canvas.drawCircle(
                (float) (center.x * scale + x),
                (float) (center.y * scale + y),
                (float) (radius*scale), paint
        );
    }


    @Override
    public void move(Vector2D movement) {
        center = center.add(movement);
    }

    @Override
    public void rotate(double angle, Vector2D point) {
    }

    @Override
    public double perimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public double square() {
        return Math.PI * radius * radius;
    }

    @Override
    public double getLeft() {
        return center.x - radius;
    }

    @Override
    public double getUp() {
        return center.y + radius;
    }

    @Override
    public double getDown() {
        return center.y - radius;
    }

    @Override
    public double getRight() {
        return center.x + radius;
    }

    @Override
    public Figure2D clone() {
        return new Circle(center.clone(), radius);
    }
}
