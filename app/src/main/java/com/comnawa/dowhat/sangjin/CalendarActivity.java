package com.comnawa.dowhat.sangjin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import com.comnawa.dowhat.R;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calview;
    TextView txtDate;
    ListView list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_sangjin);
        txtDate = (TextView) findViewById(R.id.txtDate);
        list1 = (ListView) findViewById(R.id.list1);
        calview = (CalendarView) findViewById(R.id.calview);

        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                txtDate.setText(year + "년 " + (month + 1) + "월 " + day + "일 일정");
            }
        });
    }
}
