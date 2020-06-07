package com.ruslocker.physics2d.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ruslocker.physics2d.PhysObj;
import com.ruslocker.physics2d.activities.SimulationActivity;

public class Visualizer extends View {

    Paint paint = new Paint();
    Paint paintSpec = new Paint();
    public static double x0 = 0;
    public static double y0 = 0;
    public static double scale = 0.5;

    public Visualizer(Context context, AttributeSet attrs) {
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
        for (int i = 0; i < SimulationActivity.objs.size(); i++) {
            PhysObj o = SimulationActivity.objs.get(i);
            if (SimulationActivity.pointer != null && i == SimulationActivity.pointer)
                o.draw(x0, y0, scale, canvas, paintSpec);
            else
                o.draw(x0, y0, scale, canvas, paint);
        }
//        MotionEvent event = SimulationActivity.motionEvent;
//        if(event != null) {
//            for (int i = 0; i < event.getPointerCount(); i++) {
//                canvas.drawCircle(event.getX(i) - 30, event.getY(i) - 250, 50f, paint);
//            }
//        }
        SimulationActivity.cpsT.setText(String.format("%.3g", SimulationActivity.cps));
//        SimulationActivity.checkerT.setText(Long.toString((long) SimulationActivity.checker));
//        if(SimulationActivity.objs.size() > 1) {
//            Vector2D k;
//            if ((k = SimulationActivity.objs[1].getCenter()).x != 600) {
//                //System.out.println(123);
//            }
//        }
        invalidate();
    }

}