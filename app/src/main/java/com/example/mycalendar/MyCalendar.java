package com.example.mycalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by My Computer on 2017/12/1.
 */

public class MyCalendar extends LinearLayout implements View.OnClickListener{
    private ImageView header_left;
    private ImageView header_right;
    private TextView  header_date;
    private GridView  grid;

    private Calendar curDate=Calendar.getInstance();

    private String displayFormat;


    public MyCalendar(Context context) {
        super(context);
    }

    public MyCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl(context,attrs);
    }

    public MyCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(context,attrs);
    }

    private  void initControl(Context context,AttributeSet attrs){
        bindControl(context);   //初始化数据
        bindControlEnent();     //设置绑定事件

        TypedArray ta=getContext().obtainStyledAttributes(attrs,R.styleable.MyCalendar);
        try {
            String format=ta.getString(R.styleable.MyCalendar_dateFormat);  //属性的读取
            displayFormat = format;
            if  (displayFormat==null){
                displayFormat="YYYY年MM月";   //未设定时的默认值
            }
        }
        finally {
            ta.recycle();
        }

        renderCalender();       //界面渲染
    }

        //为两个按钮设置事件监听
    private void bindControlEnent() {
        header_left.setOnClickListener(this);
        header_right.setOnClickListener(this);
    }

    private void bindControl(Context context) {
        LayoutInflater.from(context).inflate(R.layout.calendar,this);//显示

        header_left=(ImageView)findViewById(R.id.header_left);
        header_right=(ImageView)findViewById(R.id.header_right);
        header_date=(TextView)findViewById(R.id.header_txt);
        grid=(GridView)findViewById(R.id.gridview);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_left:                 //Calendar c= Calendar.getInstance(); 获取Calendar对象
                curDate.add(Calendar.MONTH,-1);    //add是对c进行操作；5年前c.add(Calendar.YEAT,-5);
                renderCalender();                  //get是获取c的日期值；c.get(Calendar.DAY);如果是接上面的话意思为5年前的今天
                break;                             //set是设置直接日期值;c.set(2017,12,5);
            case R.id.header_right:
                curDate.add(Calendar.MONTH,1);
                renderCalender();
                break;
        }
    }

    private void renderCalender() {
        SimpleDateFormat sdf=new SimpleDateFormat(displayFormat);
        header_date.setText(sdf.format(curDate.getTime()));

        List<Date> cells=new ArrayList<>();             //存日期的GridView的元素List
        Calendar calendar=(Calendar) curDate.clone();    //对原数据进行操作容易引起数据同步的问题

        calendar.set(Calendar.DAY_OF_MONTH,1);                //将日历的日期至于当前月份的第一天
        int prevDays=calendar.get(Calendar.DAY_OF_WEEK)-1;  //接上面的set操作此处获取次月第一天是星期几，若为星期一则-1后为0表示前面没有剩余
        calendar.add(Calendar.DAY_OF_MONTH,-prevDays);
        //以2017.12.01为例，1号周五，前面有5天，所以前移5天calendar为11月26日；然后依次get日期后加入cells并后移一位直到填充满42个
        int maxCellCounts=6*7;

        while (cells.size()<maxCellCounts){
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }
        grid.setAdapter(new CalendarAdapter(getContext(),R.layout.date_item, (ArrayList<Date>) cells));

    }
    private  class CalendarAdapter extends ArrayAdapter<Date>{
        private  int resurceId;
        public CalendarAdapter(Context context, int resource, ArrayList<Date> objects) {
            super(context, resource, objects);
            this.resurceId=resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView==null){
                view=LayoutInflater.from(getContext()).inflate(resurceId,parent,false);
            }else {
                view=convertView;
            }
            Date date=getItem(position);
            int day=date.getDate();         //getDay()则返回0 1 2 3 4 5 6
            ((TextView)view).setText(String.valueOf(day));

            Date now=new Date();                        //现在
            boolean isTheSameMonth=false;
            if (date.getMonth()==now.getMonth()){       //月份相同设为黑色否则设为灰色
                isTheSameMonth=true;
            }
            if (isTheSameMonth){
                ((Calendar_day_textview)view).setTextColor(Color.BLACK);
            }else {
                ((Calendar_day_textview)view).setTextColor(Color.GRAY);
            }
            //今天设为红色
            if (now.getDate()==date.getDate()&&now.getMonth()==date.getMonth()&&now.getYear()==date.getYear()){
                ((TextView)view).setTextColor(Color.RED);
                ( (Calendar_day_textview)view).isToday=true;
            }
            return view;


//            View view=LayoutInflater.from(getContext()).inflate(resurceId,parent,false);
//            Date date=getItem(position);
//            int day=date.getDate();
//            ((TextView)view).setText(String.valueOf(day));
//            return view;

        }
    }
}
