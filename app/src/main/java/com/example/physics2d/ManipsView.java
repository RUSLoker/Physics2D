package com.example.physics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ManipsView extends View {
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint paint3 = new Paint();
    boolean created = false;
    Vector2D vCenter, hCenter;

    public ManipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint1.setStyle(Paint.Style.FILL);
        paint1.setColor(Color.parseColor("#ff2400"));
        paint2.setStyle(Paint.Style.FILL);
        paint2.setColor(Color.parseColor("#ff2400"));
        paint2.setAlpha(100);
        paint3.setStyle(Paint.Style.FILL);
        paint3.setColor(Color.parseColor("#ff2400"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!created) {
            vCenter = new Vector2D(0,(double) getHeight() / 2);
            hCenter = new Vector2D((double) getWidth() / 2, 0);
            created = true;
        }
        for (int i = 0; i < AirHockey.manips.length; i++){
            Vector2D st = AirHockey.manips[i].getStPos(),
                    fin = AirHockey.manips[i].getManipPoint();
            if(st != null && fin != null) {
                if(i == 1){
                    st = st.add(hCenter);
                    fin = fin.add(hCenter);
                }
                canvas.drawCircle(
                        (float) st.x,
                        (float) st.y,
                        (float) 60,
                        paint1
                );
                canvas.drawCircle(
                        (float) st.x,
                        (float) st.y,
                        (float) Manipulator.maxLen,
                        paint2
                );
                canvas.drawCircle(
                        (float) fin.x,
                        (float) fin.y,
                        (float) 60,
                        paint3
                );
            }
        }
        invalidate();
    }

}
