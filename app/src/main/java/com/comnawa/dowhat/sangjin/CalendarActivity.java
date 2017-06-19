package com.comnawa.dowhat.sangjin;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.kwanwoo.CalendarCoreActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;


public class CalendarActivity extends ListActivity implements Serializable {
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
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent=null;
        intent=new Intent(this, CalendarCoreActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoWhat.fixedScreen(this, DoWhat.sero);
        setContentView(R.layout.calendar_sangjin);
        txtDate = (TextView) findViewById(R.id.txtDate);
        calview = (CalendarView) findViewById(R.id.calview);
        Calendar cal=Calendar.getInstance();
        StartDay(calview,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));

        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() { //날짜를 눌렀을때
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                txtDate.setText(year + "년 " + (month + 1) + "월 " + day + "일 일정"); //텍스트에 날짜표시
                id = getIntent().getStringExtra("id");
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
                            String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/simple.do";
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
                                dto.setTitle(row.getString("title"));
                                dto.setEvent(row.getString("event"));
                                dto.setStartdate(row.getString("startdate"));
                                dto.setEnddate(row.getString("enddate"));
                                dto.setStarttime(row.getString("starttime"));
                                dto.setEndtime(row.getString("endtime"));
                                dto.setPlace(row.getString("place"));
                                dto.setMemo(row.getString("memo"));
                                dto.setAlarm(row.getInt("alarm"));
                                dto.setRepeat(row.getInt("repeat"));
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
                TextView txtStartTime = (TextView)v.findViewById(R.id.txtStartTime);
                ImageView img1 = (ImageView) v.findViewById(R.id.img1);
                txtSchedule.setText(dto.getTitle());
                txtSchedule.setTextSize(new PrefManager(CalendarActivity.this).getTextSize());
                String starttime=dto.getStarttime().substring(0,5);
                txtStartTime.setText(starttime);
                String event = dto.getEvent();
                if (event.equals("공휴일")) {
                    img1.setColorFilter(Color.RED);
                    img1.setVisibility(View.VISIBLE);
                } else if (event.equals("생일")) {
                    img1.setColorFilter(Color.BLUE);
                    img1.setVisibility(View.VISIBLE);
                } else if (event.equals("기념일")) {
                    img1.setColorFilter(Color.GREEN);
                    img1.setVisibility(View.VISIBLE);
                } else {
                    img1.setVisibility(View.GONE);
                }
            }
            return v;
        }
    }

    private void StartDay(@NonNull CalendarView view, int year, int month, int day){
        txtDate.setText(year + "년 " + (month + 1) + "월 " + day + "일 일정"); //텍스트에 날짜표시
        id = getIntent().getStringExtra("id");
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
                    String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/simple.do";
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
                        dto.setTitle(row.getString("title"));
                        dto.setEvent(row.getString("event"));
                        dto.setStartdate(row.getString("startdate"));
                        dto.setEnddate(row.getString("enddate"));
                        dto.setStarttime(row.getString("starttime"));
                        dto.setEndtime(row.getString("endtime"));
                        dto.setPlace(row.getString("place"));
                        dto.setMemo(row.getString("memo"));
                        dto.setAlarm(row.getInt("alarm"));
                        dto.setRepeat(row.getInt("repeat"));
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

    public ArrayList<ScheduleDTO> getSchedule(){
        return items;
    }

}