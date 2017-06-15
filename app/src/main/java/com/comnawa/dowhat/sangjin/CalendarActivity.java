package com.comnawa.dowhat.sangjin;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.comnawa.dowhat.R;

import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    CalendarView calview;
    TextView txtDate;
    ListView list1;
    CalDAO dao;
    public static List<CalDTO> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_sangjin);
        txtDate = (TextView) findViewById(R.id.txtDate);
        list1 = (ListView) findViewById(R.id.list1);
        calview = (CalendarView) findViewById(R.id.calview);
        dao = new CalDAO(this);

        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                txtDate.setText(year + "년 " + (month + 1) + "월 " + day + "일 일정");
                items = dao.list(); //ArrayList에 일정 출력
                MyAdapter adapter = new MyAdapter(this, R.layout.clist_sangjin, items);
                list1.setAdapter(adapter);
            }
        });
    }

    class MyAdapter extends ArrayAdapter<CalDTO> {
        public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CalDTO> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) getLayoutInflater();
                v = li.inflate(R.layout.clist_sangjin, null);
            }
            final CalDTO dto = items.get(position);
            TextView txtSchedule = (TextView) v.findViewById(R.id.txtSchedule); //일정
            ImageView img1 = (ImageView) v.findViewById(R.id.img1);
            txtSchedule.setText(dto.gteTitle());
            String event = dto.getEvent();
            if (event.equals("공휴일")) {
                img1.
            } else if (event.equals("생일")) {
                img1.
            } else if (event.equals("기념일")) {
                img1.
            }
            return v;
        }
    }
}
