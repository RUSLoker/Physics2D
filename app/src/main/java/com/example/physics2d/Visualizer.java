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
    public static double x0 = 0;
    public static double y0 = 0;
    public static double scale = 0.5;

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
            if(MainActivity.pointer != null && i == MainActivity.pointer)
                o.draw(x0, y0, scale, canvas, paintSpec);
            else
                o.draw(x0, y0, scale, canvas, paint);
        }
//        MotionEvent event = MainActivity.motionEvent;
//        if(event != null) {
//            for (int i = 0; i < event.getPointerCount(); i++) {
//                canvas.drawCircle(event.getX(i) - 30, event.getY(i) - 250, 50f, paint);
//            }
//        }
//        MainActivity.cpsT.setText(String.format("%.3g", MainActivity.cps));
//        MainActivity.checkerT.setText(Long.toString((long) MainActivity.checker));
//        if(MainActivity.objs.length > 1) {
//            Vector2D k;
//            if ((k = MainActivity.objs[1].getCenter()).x != 600) {
//                //System.out.println(123);
//            }
//        }
        invalidate();
    }

}