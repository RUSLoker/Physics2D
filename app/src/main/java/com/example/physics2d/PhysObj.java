package com.example.physics2d;

import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;

public class PhysObj {
    public final Figure2D body;
    public final Vector2D speed, acceleration;

    public PhysObj(Figure2D body, Vector2D speed, Vector2D acceleration) {
        this.body = body;
        this.speed = speed;
        this.acceleration = acceleration;
    }
    public PhysObj(Figure2D body) {
        this(body, Vector2D.zero(), Vector2D.zero());
    }

    public void draw(Canvas canvas, Paint paint){
        body.draw(canvas, paint);
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
