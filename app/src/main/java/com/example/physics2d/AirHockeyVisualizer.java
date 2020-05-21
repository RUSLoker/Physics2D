package com.example.physics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AirHockeyVisualizer extends View {

    Paint paint = new Paint();
    Paint paintSpec = new Paint();
    public static double x0 = 0;
    public static double y0 = 0;
    public static double scale = 0.5;

    public AirHockeyVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paintSpec.setStrokeWidth(10);
        paintSpec.setStyle(Paint.Style.FILL);
        paintSpec.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < AirHockey.objs.size(); i++) {
            PhysObj o = AirHockey.objs.get(i);
            if (AirHockey.pointer != null && i == AirHockey.pointer)
                o.draw(x0, y0, scale, canvas, paintSpec);
            else
                o.draw(x0, y0, scale, canvas, paint);
        }
//        MotionEvent event = AirHockey.motionEvent;
//        if(event != null) {
//            for (int i = 0; i < event.getPointerCount(); i++) {
//                canvas.drawCircle(event.getX(i) - 30, event.getY(i) - 250, 50f, paint);
//            }
//        }
//        AirHockey.checkerT.setText(Long.toString((long) AirHockey.checker));
//        if(AirHockey.objs.size() > 1) {
//            Vector2D k;
//            if ((k = AirHockey.objs[1].getCenter()).x != 600) {
//                //System.out.println(123);
//            }
//        }
        invalidate();
    }

}