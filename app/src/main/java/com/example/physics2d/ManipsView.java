package com.example.physics2d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ManipsView extends View {
    Paint paint = new Paint();
    Paint paintSpec = new Paint();
    boolean created = false;
    Vector2D vCenter, hCenter;

    public ManipsView(Context context, AttributeSet attrs) {
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
        if(!created) {
            vCenter = new Vector2D(0,getHeight() / 2);
            hCenter = new Vector2D(getWidth() / 2, 0);
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
                        (float) 50,
                        paint
                );
                canvas.drawCircle(
                        (float) fin.x,
                        (float) fin.y,
                        (float) 50,
                        paint
                );
            }
        }
        invalidate();
    }

}
