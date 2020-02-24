package com.example.physics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Visualizer extends View {

    Paint paint = new Paint();
    Paint paintSpec = new Paint();

    public Visualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        paintSpec.setStrokeWidth(10);
        paintSpec.setStyle(Paint.Style.STROKE);
        paintSpec.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < MainActivity.objs.length; i++){
            PhysObj o = MainActivity.objs[i];
            if(i == MainActivity.pointer)
                o.draw(canvas, paintSpec);
            else
                o.draw(canvas, paint);
        }
        MainActivity.cpsT.setText(Long.toString((long) MainActivity.cps));
        if(MainActivity.objs.length > 1) {
            Vector2D k;
            if ((k = MainActivity.objs[1].getCenter()).x != 600) {
                //System.out.println(123);
            }
        }
        invalidate();
    }

}