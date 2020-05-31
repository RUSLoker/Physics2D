package com.example.physics2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class AirHockeyVisualizer extends View {

    Paint paint = new Paint();
    Paint paintSpec = new Paint();
    public static double x0 = 0;
    public static double y0 = 0;
    public static double scale = 1;
    Bitmap red, blue, puck;

    public AirHockeyVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paintSpec.setStrokeWidth(10);
        paintSpec.setStyle(Paint.Style.FILL);
        paintSpec.setColor(Color.RED);
        red = BitmapFactory.decodeResource(getResources(), R.drawable.red_bat);
        blue = BitmapFactory.decodeResource(getResources(), R.drawable.blue_bat);
        puck = BitmapFactory.decodeResource(getResources(), R.drawable.puck);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AirHockey.airHockey.checkGame();
        PhysObj o = AirHockey.objs[0];
        if (o != null) {
            canvas.drawBitmap(red, null,
                    new RectF((float) (o.getCenter().x - 100),
                            (float) (o.getCenter().y - 100),
                            (float) (o.getCenter().x + 100),
                            (float) (o.getCenter().y + 100)),
                    null);
        }
        o = AirHockey.objs[1];
        if (o != null) {
            canvas.drawBitmap(blue, null,
                    new RectF((float) (o.getCenter().x - 100),
                            (float) (o.getCenter().y - 100),
                            (float) (o.getCenter().x + 100),
                            (float) (o.getCenter().y + 100)),
                    null);
        }
        o = AirHockey.objs[2];
        if (o != null) {
            canvas.drawBitmap(puck, null,
                    new RectF((float) (o.getCenter().x - 70),
                            (float) (o.getCenter().y - 70),
                            (float) (o.getCenter().x + 70),
                            (float) (o.getCenter().y + 70)),
                    null);
        }
        AirHockey.scoreFirst.setText(Integer.toString(AirHockey.game.getFirstScore()));
        AirHockey.scoreSecond.setText(Integer.toString(AirHockey.game.getSecondScore()));
        invalidate();
    }

}