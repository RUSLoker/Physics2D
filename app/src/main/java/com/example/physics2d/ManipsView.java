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
        for (Manipulator i : AirHockey.manips){
            Vector2D st = i.getStPos(),
                    fin = i.getManipPoint();
            if(st != null && fin != null) {
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
