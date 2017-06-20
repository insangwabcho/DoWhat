package com.comnawa.dowhat.sangjin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import com.comnawa.dowhat.R;


public class DetailActivity extends AppCompatActivity {
    EditText editTitle, editPlace, editSdate, editEdate,
             editStime, editEtime, editMemo, editAlarm, editFriend;
    CheckBox cbRepeat;
    DatePicker dp;
    TimePicker Stp, Etp;
    LinearLayout layout1, layout2;
    boolean isOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sangjin);
        editTitle = (EditText)findViewById(R.id.editTitle);
        editPlace = (EditText)findViewById(R.id.editPlace);
        editSdate = (EditText)findViewById(R.id.editSdate);
        editEdate = (EditText)findViewById(R.id.editEdate);
        editStime = (EditText)findViewById(R.id.editStime);
        editEtime = (EditText)findViewById(R.id.editEtime);
        editMemo = (EditText)findViewById(R.id.editMemo);
        editAlarm = (EditText)findViewById(R.id.editAlarm);
        editFriend = (EditText)findViewById(R.id.editFriend);
        cbRepeat = (CheckBox)findViewById(R.id.cbRepeat);
        dp = (DatePicker)findViewById(R.id.datePicker);
        Stp = (TimePicker)findViewById(R.id.timePicker);
        Etp = (TimePicker)findViewById(R.id.timePicker);
        layout1 = (LinearLayout)findViewById(R.id.layout1);
        layout2 = (LinearLayout)findViewById(R.id.layout2);

        editSdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                dp.setVisibility(View.VISIBLE);
                isOk=true;
                return false;
            }
        });

        editEdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                dp.setVisibility(View.VISIBLE);
                isOk=false;
                return false;
            }
        });

        dp.init(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dp.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.VISIBLE);
                String startdate="";
                String n=String.valueOf(year);
                String w=String.valueOf(monthOfYear+1);
                String i=String.valueOf(dayOfMonth);
                if((monthOfYear+1)< 10) {
                    w = "0" + String.valueOf(monthOfYear + 1);
                }
                if(dayOfMonth<10){
                    i = "0" + String.valueOf(dayOfMonth);
                }
                startdate=n+"-"+w+"-"+i;
                if(isOk) {
                    editSdate.setText(startdate);
                }else{
                    editEdate.setText(startdate);
                }

            }
        });

    }
}
