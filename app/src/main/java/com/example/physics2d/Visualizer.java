package com.example.physics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Visualizer extends View {

    Paint paint = new Paint();

    public Visualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(PhysObj i : MainActivity.objs){
            i.draw(canvas, paint);
        }
        invalidate();
    }

}