package com.comnawa.dowhat.sangjin;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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
import com.comnawa.dowhat.sungwon.Common;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;


public class CalendarActivity extends ListActivity implements Serializable {
    CalendarView calview; //달력
    TextView txtDate; //날짜표시
    static ArrayList<ScheduleDTO> items; //일정을 담을 리스트
    String id;
    static String startdate; //아이디와 선택 날짜
    ImageView btnAdd; //일정 추가버튼

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ScheduleAdapter adapter = new ScheduleAdapter(CalendarActivity.this, R.layout.layout, items);
            setListAdapter(adapter);
        }
    };

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("index",position);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로 고정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_sangjin);
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnAdd = (ImageView)findViewById(R.id.btnAdd);
        calview = (CalendarView) findViewById(R.id.calview);
        Calendar cal=Calendar.getInstance();
        StartDay(calview,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        btnAdd.setImageResource(R.drawable.plus);

        //일정 추가버튼을 눌렀을때 이벤트
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });

        //날짜를 눌렀을 때 목록표시 이벤트
        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                id = getIntent().getStringExtra("id");
                String n=String.valueOf(year);
                String w=String.valueOf(month+1);
                String i=String.valueOf(day);
                if((month+1)< 10) { //1~9월에는 앞에 0을 붙임
                    w = "0" + String.valueOf(month + 1);
                }
                if(day<10){ //1~9일에는 앞에 0을 붙임
                    i = "0" + String.valueOf(day);
                }
                startdate=n+"-"+w+"-"+i;
                txtDate.setText(n+"년 "+w+"월 "+i+"일 일정"); //텍스트뷰에 날짜표시
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            items = new ArrayList<ScheduleDTO>();
                            String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/simple.do";
                            HttpClient http = new DefaultHttpClient();
                            ArrayList<NameValuePair> postData = new ArrayList<>();
                            //postData에 id와 시작날짜를 붙임
                            postData.add(new BasicNameValuePair("id", id));
                            postData.add(new BasicNameValuePair("startdate", startdate));
                            //한글, 특수문자 등이 잘 전달될 수 있도록 인코딩
                            final UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData, "utf-8");
                            //post 방식으로 데이터 전달
                            HttpPost httpPost = new HttpPost(page);
                            httpPost.setEntity(request);
                            HttpResponse response = http.execute(httpPost);
                            String body = EntityUtils.toString(response.getEntity());
                            JSONObject jsonObj = new JSONObject(body);
                            JSONArray jArray = (JSONArray) jsonObj.get("sendData");
                            //JArray에서 받아온 자료를 dto에 담은후 ArrayList에 저장
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
                v = li.inflate(R.layout.layout, null);
            }
            ScheduleDTO dto = items.get(position);
            if (dto != null) {
                TextView txtSchedule = (TextView) v.findViewById(R.id.txtSchedule);
                TextView txtStartTime = (TextView)v.findViewById(R.id.txtStarttime);
                ImageView img1 = (ImageView) v.findViewById(R.id.img1);
                //일정 표시
                txtSchedule.setText(dto.getTitle());
                if(!dto.getStarttime().equals("")){
                    //내용이있으면 시간표시
                    int Shour=Integer.parseInt(dto.getStarttime().substring(0,2));
                    int Sminute=Integer.parseInt(dto.getStarttime().substring(3));
                    int Ehour=Integer.parseInt(dto.getEndtime().substring(0,2));
                    int Eminute=Integer.parseInt(dto.getEndtime().substring(3));
                    String h1,h2,m1,m2,setStime,setEtime;
                    if (Shour < 10) { //0~9시일경우 앞에 0을 붙임
                        h1 = "0" + String.valueOf(Shour);
                    } else {
                        h1 = String.valueOf(Shour);
                    }
                    if (Ehour < 10) { //0~9시일경우 앞에 0을 붙임
                        h2 = "0" + String.valueOf(Ehour);
                    } else {
                        h2 = String.valueOf(Ehour);
                    }
                    if (Sminute < 10) { //1~9분일경우 앞에 0을 붙임
                        m2 = "0" + String.valueOf(Sminute);
                    } else {
                        m2 = String.valueOf(Sminute);
                    }
                    if (Eminute < 10) { //1~9분일경우 앞에 0을 붙임
                        m1 = "0" + String.valueOf(Eminute);
                    } else {
                        m1 = String.valueOf(Eminute);
                    }
                    if(Shour>12){
                        setStime = "오후 0"+(Shour-12) + "시 " + m1 +"분";
                    }else if(Shour==12){
                        setStime = "오후 "+h1+"시 "+m1+"분";
                    }else{
                        setStime = "오전 "+h1+"시 "+m1+"분";
                    }
                    if(Ehour>12){
                        setEtime = "오후 0"+(Ehour-12) + "시 " + m1 +"분";
                    }else if(Shour==12){
                        setEtime = "오후 "+h2+"시 "+m2+"분";
                    }else{
                        setEtime = "오전 "+h2+"시 "+m2+"분";
                    }
                    String time=setStime+" - "+setEtime;
                    txtStartTime.setText(time);
                }else{
                    //내용이 없으면 하루종일로 표시
                    txtStartTime.setText("하루종일");
                }
                String event = dto.getEvent();
                    //이벤트가 있으면 이미지표시
                if (event.equals("공휴일")) {
                    img1.setImageResource(R.drawable.holiday);
                    img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else if (event.equals("생일")) {
                    img1.setImageResource(R.drawable.birthday);
                    img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else if (event.equals("기념일")) {
                    img1.setImageResource(R.drawable.heart);
                    img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                } else {
                    img1.setVisibility(View.GONE);
                }
            }
            return v;
        }
    }

    public void StartDay(@NonNull CalendarView view, int year, int month, int day){
        id = getIntent().getStringExtra("id");
        String n=String.valueOf(year);
        String w=String.valueOf(month+1);
        String i=String.valueOf(day);
        if((month+1)< 10) {
            w = "0" + String.valueOf(month + 1);
        }
        if(day<10){
            i = "0" + String.valueOf(day);
        }
        startdate=n+"-"+w+"-"+i;
        txtDate.setText(n+"년 "+w+"월 "+i+"일 일정"); //텍스트에 날짜표시
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

    public ScheduleDTO getSchedule(int index){
        return items.get(index);
    }

}