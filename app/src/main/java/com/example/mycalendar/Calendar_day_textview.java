package com.example.mycalendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by My Computer on 2017/12/5.
 */

public class Calendar_day_textview extends TextView {
    public  boolean isToday;
    private Paint paint=new Paint();

    public Calendar_day_textview(Context context) {
        super(context);
    }

    public Calendar_day_textview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public Calendar_day_textview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }
    public void initControl(){
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isToday){
            canvas.translate(getWidth()/2,getHeight()/2);
            canvas.drawCircle(0,0,getWidth()/2,paint);
        }
    }
}
