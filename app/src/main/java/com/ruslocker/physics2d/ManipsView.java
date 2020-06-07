package com.ruslocker.physics2d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ruslocker.physics2d.activities.AirHockey;

public class ManipsView extends View {
    Paint paintRed2 = new Paint();
    Paint paintRed3 = new Paint();
    Paint paintBlue2 = new Paint();
    Paint paintBlue3 = new Paint();
    Bitmap red1, blue1;
    boolean created = false;
    Vector2D vCenter, hCenter;

    public ManipsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintRed2.setStyle(Paint.Style.FILL);
        paintRed2.setColor(Color.parseColor("#ff2400"));
        paintRed2.setAlpha(50);
        paintRed3.setStyle(Paint.Style.FILL);
        paintRed3.setColor(Color.parseColor("#ff2400"));
        red1 = getBitmap(R.drawable.manipulator_center_red);
        paintBlue2.setStyle(Paint.Style.FILL);
        paintBlue2.setColor(Color.parseColor("#0022FF"));
        paintBlue2.setAlpha(50);
        paintBlue3.setStyle(Paint.Style.FILL);
        paintBlue3.setColor(Color.parseColor("#0022FF"));
        blue1 = getBitmap(R.drawable.manipulator_center_blue);
    }

    @SuppressLint({"DrawAllocation", "SetTextI18n"})
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!created) {
            vCenter = new Vector2D(0,(double) getHeight() / 2);
            hCenter = new Vector2D((double) getWidth() / 2, 0);
            created = true;
        }
        Vector2D st = AirHockey.manips[0].getStPos(),
                fin = AirHockey.manips[0].getManipPoint();
        if(st != null && fin != null) {
            float width = (float)red1.getWidth()/2;
            canvas.drawBitmap(red1,  null,
                    new RectF((float) (st.x - width),
                            (float) (st.y - width),
                            (float) (st.x + width),
                            (float) (st.y + width)),
                    null);
            canvas.drawCircle(
                    (float) st.x,
                    (float) st.y,
                    (float) Manipulator.maxLen,
                    paintRed2
            );
            canvas.drawCircle(
                    (float) fin.x,
                    (float) fin.y,
                    (float) width,
                    paintRed3
            );
        }
        st = AirHockey.manips[1].getStPos();
        fin = AirHockey.manips[1].getManipPoint();
        if(st != null && fin != null) {
            float width = (float)blue1.getWidth()/2;
            st = st.add(hCenter);
            fin = fin.add(hCenter);
            canvas.drawBitmap(blue1, null,
                    new RectF((float) (st.x - width),
                            (float) (st.y - width),
                            (float) (st.x + width),
                            (float) (st.y + width)),
                    null);
            canvas.drawCircle(
                    (float) st.x,
                    (float) st.y,
                    (float) Manipulator.maxLen,
                    paintBlue2
            );
            canvas.drawCircle(
                    (float) fin.x,
                    (float) fin.y,
                    (float) width,
                    paintBlue3
            );
        }
        AirHockey.scoreFirst.setText(Integer.toString(AirHockey.game.getFirstScore()));
        AirHockey.scoreSecond.setText(Integer.toString(AirHockey.game.getSecondScore()));

        if(AirHockey.game.gameMode == GameMode.Time) {
            TextView timer1 = AirHockey.airHockey.findViewById(R.id.timer1);
            TextView timer2 = AirHockey.airHockey.findViewById(R.id.timer2);
            long time = AirHockey.game.getTimeRest()*1000;
            String str = String.format("%tM:%tS", time, time);
            timer1.setText(str);
            timer2.setText(str);
        }
        if(AirHockey.airHockey.checkGame())
            invalidate();
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
