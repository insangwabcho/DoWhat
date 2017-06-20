package com.comnawa.dowhat.sangjin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class DetailActivity extends AppCompatActivity {
    EditText editSdate;
    DatePicker Sdp, Edp;
    TimePicker Stp, Etp;
    LinearLayout layout1, layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.detail_sangjin);
//        editSdate = (EditText)findViewById(R.id.editSdate);
//        Sdp = (DatePicker)findViewById(R.id.datePicker);
//        layout1 = (LinearLayout)findViewById(R.id.layout1);
//        layout2 = (LinearLayout)findViewById(R.id.layout2);

        editSdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                layout1.setVisibility(View.INVISIBLE);
                layout2.setVisibility(View.INVISIBLE);
                Sdp.setVisibility(View.VISIBLE);
                return false;
            }
        });

        Sdp.init(Sdp.getYear(), Sdp.getMonth(), Sdp.getDayOfMonth(), new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Sdp.setVisibility(View.GONE);
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
                editSdate.setText(startdate);
            }
        });

    }
}
