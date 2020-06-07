package com.ruslocker.physics2d.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ruslocker.physics2d.engine.PhysObj;
import com.ruslocker.physics2d.R;
import com.ruslocker.physics2d.activities.AirHockey;

public class AirHockeyVisualizer extends View {
    public static double x0 = 0;
    public static double y0 = 0;
    public static double scale = 1;
    Bitmap red, blue, puck;

    public AirHockeyVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        red = BitmapFactory.decodeResource(getResources(), R.drawable.red_bat);
        blue = BitmapFactory.decodeResource(getResources(), R.drawable.blue_bat);
        puck = BitmapFactory.decodeResource(getResources(), R.drawable.puck);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        PhysObj o = AirHockey.objs[0];
        if (o != null) {
            float x = (float) o.getCenter().x,
                    y = (float) o.getCenter().y;
            canvas.drawBitmap(red, null,
                    new RectF(x - 100f,
                            y - 100f,
                            x + 100f,
                            y + 100f),
                    null);
        }
        o = AirHockey.objs[1];
        if (o != null) {
            float x = (float) o.getCenter().x,
                    y = (float) o.getCenter().y;
            canvas.drawBitmap(blue, null,
                    new RectF(x - 100f,
                            y - 100f,
                            x + 100f,
                            y + 100f),
                    null);
        }
        o = AirHockey.objs[2];
        if (o != null) {
            float x = (float) o.getCenter().x,
                    y = (float) o.getCenter().y;
            canvas.drawBitmap(puck, null,
                    new RectF(x - 70f,
                            y - 70f,
                            x + 70f,
                            y + 70f),
                    null);
        }
        invalidate();
    }

}