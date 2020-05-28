package com.example.physics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AirHockeyVisualizer extends View {

    Paint paint = new Paint();
    Paint paintSpec = new Paint();
    public static double x0 = 0;
    public static double y0 = 0;
    public static double scale = 1;

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
        for (int i = 0; i < AirHockey.objs.length; i++) {
            PhysObj o = AirHockey.objs[i];
            if (o != null) {
                o.draw(x0, y0, scale, canvas, paint);
            }
        }
        AirHockey.scoreFirst.setText(Integer.toString(AirHockey.game.getFirstScore()));
        AirHockey.scoreSecond.setText(Integer.toString(AirHockey.game.getSecondScore()));
        invalidate();
    }

}