package com.comnawa.dowhat.sangjin;


import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DBManager;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class CalendarActivity extends ListActivity implements Serializable {
    CalendarView calview; //달력
    TextView txtDate; //날짜표시
    static ArrayList<ScheduleDTO> items; //일정을 담을 리스트
    String id;
    static String startdate; //아이디와 선택 날짜
    ImageView btnPlus, btnAdd, btnMic;
    PrefManager manager;
    DBManager dbManager;
    boolean isClick;

    private static final int RESULT_SPEECH=1;

    private Intent i;
    private SpeechRecognizer mRecognizer;
    TextToSpeech tts;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ScheduleAdapter adapter = new ScheduleAdapter(CalendarActivity.this, R.layout.layout, items);
            setListAdapter(adapter);
        }
    };

    //기존 일정 수정창 띄우기
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("index",position);
        intent.putExtra("check",1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로 고정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_sangjin);
        manager=new PrefManager(this);
        final HashMap<String, String> UserInfo=manager.getUserInfo();
        Log.i("Test2",manager.getUserInfo().toString());
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnPlus = (ImageView)findViewById(R.id.btnPlus);
        btnAdd = (ImageView)findViewById(R.id.btnAdd);
        btnMic = (ImageView)findViewById(R.id.btnMic);
        calview = (CalendarView) findViewById(R.id.calview);
        dbManager= new DBManager(this);
        items= dbManager.todaySchedule(UserInfo.get("id"));
        Calendar cal=Calendar.getInstance();
        StartDay(calview,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DATE));
        btnPlus.setImageResource(R.drawable.plus);
        btnAdd.setImageResource(R.drawable.add);
        btnMic.setImageResource(R.drawable.mic);
        btnAdd.setVisibility(View.INVISIBLE);
        btnMic.setVisibility(View.INVISIBLE);

        //플러스 버튼을 눌렀을때 이벤트
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isClick){
                    //버튼 2개 표시후 X로 이미지 변경
                    btnAdd.setVisibility(View.VISIBLE);
                    btnMic.setVisibility(View.VISIBLE);
                    btnPlus.setImageResource(R.drawable.x);
                    isClick=!isClick;
                    //추가버튼 눌렀을때 이벤트
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
                            intent.putExtra("check",0);
                            startActivity(intent);
                        }
                    });
                    //음성인식
                    btnMic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            STT();
                        }
                    });
                }else{
                    btnAdd.setVisibility(View.INVISIBLE);
                    btnMic.setVisibility(View.INVISIBLE);
                    btnPlus.setImageResource(R.drawable.plus);
                    isClick=!isClick;
                }
            }
        });


        //날짜를 눌렀을 때 목록표시 이벤트
        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                id = UserInfo.get("id");
                String n=String.valueOf(year);
                String w=String.valueOf(month+1);
                String i=String.valueOf(day);
                int nyun= Integer.parseInt(n);
                int wol= Integer.parseInt(w);
                int il= Integer.parseInt(i);
                if((month+1)< 10) { //1~9월에는 앞에 0을 붙임
                    w = "0" + String.valueOf(month + 1);
                }
                if(day<10){ //1~9일에는 앞에 0을 붙임
                    i = "0" + String.valueOf(day);
                }
                startdate=n+"-"+w+"-"+i;
                txtDate.setText(n+"년 "+w+"월 "+i+"일 일정"); //텍스트뷰에 날짜표시
                items= dbManager.getSchedule(id,nyun,wol,il);
                handler.sendEmptyMessage(0);
//                Thread th = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            items = new ArrayList<ScheduleDTO>();
//                            String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/simple.do";
//                            HttpClient http = new DefaultHttpClient();
//                            ArrayList<NameValuePair> postData = new ArrayList<>();
//                            //postData에 id와 시작날짜를 붙임
//                            postData.add(new BasicNameValuePair("id", id));
//                            postData.add(new BasicNameValuePair("startdate", startdate));
//                            //한글, 특수문자 등이 잘 전달될 수 있도록 인코딩
//                            final UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData, "utf-8");
//                            //post 방식으로 데이터 전달
//                            HttpPost httpPost = new HttpPost(page);
//                            httpPost.setEntity(request);
//                            HttpResponse response = http.execute(httpPost);
//                            String body = EntityUtils.toString(response.getEntity());
//                            JSONObject jsonObj = new JSONObject(body);
//                            JSONArray jArray = (JSONArray) jsonObj.get("sendData");
//                            //JArray에서 받아온 자료를 dto에 담은후 ArrayList에 저장
//                            for (int i = 0; i < jArray.length(); i++) {
//                                JSONObject row = jArray.getJSONObject(i);
//                                ScheduleDTO dto = new ScheduleDTO();
//                                dto.setNum(row.getInt("num"));
//                                dto.setTitle(row.getString("title"));
//                                dto.setEvent(row.getString("event"));
//                                dto.setStartdate(row.getString("startdate"));
//                                dto.setEnddate(row.getString("enddate"));
//                                dto.setStarttime(row.getString("starttime"));
//                                dto.setEndtime(row.getString("endtime"));
//                                dto.setPlace(row.getString("place"));
//                                dto.setMemo(row.getString("memo"));
//                                dto.setAlarm(row.getInt("alarm"));
//                                dto.setRepeat(row.getInt("repeat"));
//                                items.add(dto);
//                            }
//                            handler.sendEmptyMessage(0);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                th.start(); //5
            }
        });
    }

    private void STT() {
        i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "일정을 말씀해주세요.");

        Toast.makeText(CalendarActivity.this, "음성인식 시작", Toast.LENGTH_SHORT).show();

        try {
            startActivityForResult(i, RESULT_SPEECH);
        }catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "말하기 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            e.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK&&(requestCode==RESULT_SPEECH)){
            ArrayList<String> sstResult=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            final String result_stt = sstResult.get(0);
            Toast.makeText(CalendarActivity.this, result_stt, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder ab=new AlertDialog.Builder(this);
            ab.setTitle("녹음 확인");
            ab.setMessage("일정 : [ "+result_stt+" ]\n일시 : [ "+startdate+" ]")
              .setCancelable(true)
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ScheduleDTO dto=new ScheduleDTO();
                            dto.setTitle(result_stt);
                            dto.setStartdate(startdate);
                            dbManager.insertSchedule(dto);
                        }
                    })
                    .setNeutralButton("다시 녹음", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            STT();
                        }
                    })
                    .setNegativeButton("취소", null);
            AlertDialog alertDialog = ab.create();
            alertDialog.show();
        }
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
                        m1 = "0" + String.valueOf(Sminute);
                    } else {
                        m1 = String.valueOf(Sminute);
                    }
                    if (Eminute < 10) { //1~9분일경우 앞에 0을 붙임
                        m2 = "0" + String.valueOf(Eminute);
                    } else {
                        m2 = String.valueOf(Eminute);
                    }
                    if(Shour>12 && Shour<22){
                        setStime = "오후 0"+(Shour-12) + "시 " + m1 +"분";
                    }else if(Shour>21){
                        setStime = "오후 "+(Shour-12) + "시 " + m1 +"분";
                    }else if(Shour==12){
                        setStime = "오후 "+h1+"시 "+m1+"분";
                    }else{
                        setStime = "오전 "+h1+"시 "+m1+"분";
                    }
                    if(Ehour>12){
                        setEtime = "오후 0"+(Ehour-12) + "시 " + m2 +"분";
                    }else if(Ehour>21) {
                        setEtime = "오후 " + (Ehour - 12) + "시 " + m2 + "분";
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

    public void StartDay(CalendarView view, int year, int month, int day){
        final String id= manager.getUserInfo().get("id");
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
        int nyun= Integer.parseInt(n);
        int wol= Integer.parseInt(w);
        int il= Integer.parseInt(i);
        items= dbManager.getSchedule(id,nyun,wol,il);
        handler.sendEmptyMessage(0);
        /*Thread th = new Thread(new Runnable() {
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
                        dto.setNum(row.getInt("num"));
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
        th.start();*/
    }

    public ScheduleDTO getSchedule(int index){
        return items.get(index);
    }

}