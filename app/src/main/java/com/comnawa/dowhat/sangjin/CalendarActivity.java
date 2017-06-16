package com.comnawa.dowhat.sangjin;


import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.comnawa.dowhat.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;


public class CalendarActivity extends ListActivity {
    CalendarView calview; //달력
    TextView txtDate; //날짜표시
    ArrayList<ScheduleDTO> items; //일정을 담을 리스트
    String id, startdate;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ScheduleAdapter adapter = new ScheduleAdapter(CalendarActivity.this, R.layout.clist_sangjin, items);
            setListAdapter(adapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_sangjin);
        txtDate = (TextView) findViewById(R.id.txtDate);
        calview = (CalendarView) findViewById(R.id.calview);

        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //날짜를 눌렀을때
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                txtDate.setText(year + "년 " + (month + 1) + "월 " + day + "일 일정"); //텍스트에 날짜표시
                id = "root1";
                if((month+1)< 10){
                    startdate = year + "-" +"0"+(month + 1) + "-" + day;
                }else{
                    startdate = year + "-" + (month + 1) + "-" + day;
                }
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            items = new ArrayList<ScheduleDTO>();
                            String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/show.do";
                            HttpClient http = new DefaultHttpClient();
                            ArrayList<NameValuePair> postData = new ArrayList<>();
                            postData.add(new BasicNameValuePair("id", id));
                            postData.add(new BasicNameValuePair("startdate", startdate)); //아이디와 날짜를 넘김
                            //한글, 특수문자 등이 잘 전달될 수 있도록 인코딩
                            final UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData, "utf-8");
                            //post 방식으로 데이터 전달
                            HttpPost httpPost = new HttpPost(page);
                            httpPost.setEntity(request);
                            HttpResponse response = http.execute(httpPost);
                            String body = EntityUtils.toString(response.getEntity());
                            JSONObject jsonObj = new JSONObject(body);
                            JSONArray jArray = (JSONArray) jsonObj.get("sendData");
                            Log.i("test",jsonObj+"");
                            Log.i("tes",jArray+"");
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject row = jArray.getJSONObject(i);
                                ScheduleDTO dto = new ScheduleDTO();
         /*                       dto.setStartdate(row.getString("startdate"));
                                dto.setEnddate(row.getString("enddate"));
                                starttime;
                                endtime;
                                place;
                                memeo;
                                alarm; i;
                                repeat; i*/
                                dto.setEvent(row.getString("event"));
                                dto.setTitle(row.getString("title"));
                                items.add(dto);
                            }
                            handler.sendEmptyMessage(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
    }

    class ScheduleAdapter extends ArrayAdapter<ScheduleDTO> {
        public ScheduleAdapter(Context context, int textViewResourceId, ArrayList<ScheduleDTO> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.clist_sangjin, null);
            }
            ScheduleDTO dto = items.get(position);
            if (dto != null) {
                TextView txtSchedule = (TextView) v.findViewById(R.id.txtSchedule); //일정
                ImageView img1 = (ImageView) v.findViewById(R.id.img1);
                txtSchedule.setText(dto.getTitle());
                String event = dto.getEvent();
                if (event.equals("공휴일")) {
                    img1.setImageResource(R.drawable.red);
                } else if (event.equals("생일")) {
                    img1.setImageResource(R.drawable.blue);
                } else if (event.equals("기념일")) {
                    img1.setImageResource(R.drawable.green);
                }
            }
            return v;
        }
    }
}