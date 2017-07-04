package com.comnawa.dowhat.sangjin;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.AddToFriend;
import com.comnawa.dowhat.insang.DBManager;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.insang.UpdateTokken;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class CalendarActivity extends ListActivity implements Serializable {
    CalendarView calview; //달력
    TextView txtDate; //날짜표시
    static ArrayList<ScheduleDTO> items; //일정을 담을 리스트
    String id;
    static String startdate; //아이디와 선택 날짜
    ImageView btnPlus, btnAdd, btnMic, btnSet;
    PrefManager manager;
    DBManager dbManager;
    ScheduleAdapter adapter;
    boolean isClick;
    ImageView dot0,dot1,dot2,dot3,dot4,dot5,dot6,dot7,dot8,dot9,dot10,
              dot11,dot12,dot13,dot14,dot15,dot16,dot17,dot18,dot19,dot20,
              dot21,dot22,dot23,dot24,dot25,dot26,dot27,dot28,dot29,dot30,
              dot31,dot32,dot33,dot34,dot35,dot36,dot37,dot38,dot39,dot40,dot41;
    ImageView[] dots;
    String STTdate;
    private static final int RESULT_SPEECH = 1;
    private Intent i;
    private float x; //좌표
    private int stat=0; //캘린더뷰 넘길때 왼쪽 오른쪽 ( 0 default, 1 left, 2 right )

    public Activity a(){
        return CalendarActivity.this;
    }

    private void SettingListview() {
        adapter = new ScheduleAdapter(CalendarActivity.this, R.layout.layout, items);
        setListAdapter(adapter);
        this.getListView().setLongClickable(true);
        this.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final ScheduleDTO dto = items.get(position);
                AlertDialog.Builder dialog = new AlertDialog.Builder(CalendarActivity.this);
                dialog.setTitle("일정삭제")
                        .setMessage(dto.getTag().equals("-") ? "일정을 삭제하시겠습니까?" : "일정을 삭제하시겠습니까? \n (태그된 친구들의 일정은 변하지않습니다.)")
                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DBManager(CalendarActivity.this).deleteSchedule(dto);
                                Toast.makeText(CalendarActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                DoWhat.delAlarm(CalendarActivity.this);
                                Calendar cal = Calendar.getInstance();
                                String[] days = startdate.split("-"); // 년= [0], 월= [1], 일= [2]
                                SetYMD(Integer.parseInt(days[0]), Integer.parseInt(days[1]) - 1, Integer.parseInt(days[2]));
                                String myId= new PrefManager(CalendarActivity.this).getUserInfo().get("name");
                                String myName= new PrefManager(CalendarActivity.this).getUserInfo().get("id");
//                                String[] friendss= dto.getTagId().split(",");
//                                Log.i("insecc del", friendss.toString());
//                                for (String t: friendss) {
//                                    new DoWhat().sendPushMsg(CalendarActivity.this, myName + " 님의 일정이 삭제되었습니다.", t, myId, myName, dto);
//                                    Log.i("insecc del", t);
//                                }
                                setDot();
                            }
                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CalendarActivity.this, "삭제가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create().show();
                return true;
            }
        });
    }

/*    @Override
    protected void onResume() {
        Calendar cal= Calendar.getInstance();
        SetYMD(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        setDot();
        super.onResume();
    }*/

    private Handler handler1= new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==0){
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.comnawa.dowhat");
                startActivity(intent);
                finishAffinity();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle("안내");
            d.setMessage("프로그램을 종료하시겠습니까?");
            d.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    float a, b;
    // 오른쪽 화살표 왼쪽 856, 오른쪽 1002 || 왼쪽 화살표 왼쪽 77 오른쪽 218
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()== MotionEvent.ACTION_DOWN){
            a= ev.getX();
                Log.i("page", "page" + ev.getX());
            if (ev.getX() >77f && ev.getX() < 218f){
                Log.i("monthh",ev.getX()+"1");
                stat= 1;
            } else if (ev.getX() > 856f && ev.getX() < 1002f){
                Log.i("monthh",ev.getX()+"2");
                stat= 2;
            } else {
                Log.i("monthh",ev.getX()+"3");
                stat= 0;
            }
        }
        float last= ev.getX()-a;
        Log.i("asdf","last:"+last+"");

        if (last < -100 || last > 100){
            return false;
        }

        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pref, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //친구찾기 버튼을 눌렀을때 처리
        if (item.getItemId() == R.id.addFriend) {
            Intent intent=new Intent(this, AddFriendActivity.class);
            startActivity(intent);
            //환경설정 버튼을 눌렀을때 처리
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(CalendarActivity.this, Preferences.class));
        }
        return true;
    }

    //기존 일정 수정창 띄우기
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("index", position);
        intent.putExtra("check", 1);
        startActivity(intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로 고정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_sangjin);
        if (getIntent().getStringExtra("userid")!= null){
            Intent intent= new Intent(this, AddToFriend.class);
            intent.putExtra("userid",getIntent().getStringExtra("userid"));
            intent.putExtra("username",getIntent().getStringExtra("username"));
            startActivity(intent);
            finish();
        }
        manager = new PrefManager(this);
        final HashMap<String, String> UserInfo = manager.getUserInfo();
        Log.i("Test2", manager.getUserInfo().toString());
        txtDate = (TextView) findViewById(R.id.txtDate);
        btnPlus = (ImageView) findViewById(R.id.btnPlus);
        btnAdd = (ImageView) findViewById(R.id.btnAdd);
        btnMic = (ImageView) findViewById(R.id.btnMic);
        btnSet = (ImageView) findViewById(R.id.btnSet);
        calview = (CalendarView) findViewById(R.id.calview);
        calview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (stat== 1){
                    monthh-=1;
                } else if (stat == 2){
                    monthh+=1;
                }
                setDot();
            }
        });
        dotsConn();
        setDot();


        //pushTokken 업데이트
        FirebaseApp.initializeApp(this);
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_Token", token);
        new UpdateTokken(this, manager.getUserInfo().get("id"), FirebaseInstanceId.getInstance().getToken()).start();



/*        calview.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                //필요한 작업

                //리스너 해제
                calview.getViewTreeObserver().removeOnGlobalFocusChangeListener(this);
            }
        });

        calview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //뷰의 생성된 후 크기와 위치 구하기
                calview.getWidth();
                calview.getHeight();
                calview.getX();
                calview.getY();


                //리스너 해제
                calview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });*/


        dbManager = new DBManager(this);
        items = new ArrayList<>();
        try {
            items = dbManager.todaySchedule(UserInfo.get("id"));
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();

        //calendaractivity 실행 전 액티비티에서 sdate라는 변수명으로 저장해준 값이 없다면
        if (getIntent().getStringExtra("sdate") == null || getIntent().getStringExtra("sdate").equals("null")) {
            //오늘날짜로 StartDay() 함수 실행
            SetYMD(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
            monthh= cal.get(Calendar.MONTH);
            String monthhh= (monthh+1)<10 ? ("0"+(monthh+1))+"" : (monthh+1)+"";
            String dayhhh= cal.get(Calendar.DATE)<10 ? ("0"+cal.get(Calendar.DATE))+"" : cal.get(Calendar.DATE)+"";
            STTdate=cal.get(Calendar.YEAR)+"-"+monthhh+"-"+dayhhh;
        } else { //그렇지 않다면
            //putExtra로 담아준 sdate변수값 가져오기
            String sdate = getIntent().getStringExtra("sdate");
            //넘어온값은 2017-12-12 형식이기때문에 -로 스플릿
            String[] days = sdate.split("-"); // 년= [0], 월= [1], 일= [2]
            //받아온 년,월-1,일로 날자 세팅 후 StartDay() 실행
            int year = Integer.parseInt(days[0]);
            int month = Integer.parseInt(days[1]) - 1;
            monthh= month;
            int day = Integer.parseInt(days[2]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            long setdate = calendar.getTimeInMillis();
            calview.setDate(setdate, true, true);
            SetYMD(Integer.parseInt(days[0]), Integer.parseInt(days[1]) - 1, Integer.parseInt(days[2]));
            setDot();
        }
        btnPlus.setImageResource(R.drawable.plus);
        isClick=true;

        //플러스 버튼을 눌렀을때 이벤트
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    //버튼 2개 표시후 X로 이미지 변경
                    btnAdd.setImageResource(R.drawable.add);
                    btnMic.setImageResource(R.drawable.mic);
                    btnSet.setImageResource(R.drawable.set);
                    btnAdd.setVisibility(View.VISIBLE);
                    btnMic.setVisibility(View.VISIBLE);
                    btnSet.setVisibility(View.VISIBLE);
                    btnPlus.setImageResource(R.drawable.x);
                    isClick = !isClick;
                    //추가버튼 눌렀을때 이벤트
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CalendarActivity.this, DetailActivity.class);
                            intent.putExtra("check", 0);
                            startActivity(intent);
                            finish();
                        }
                    });
                    //음성인식
                    btnMic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int permissionResult = checkSelfPermission(DoWhat.record_audio);

                            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                                STT();
                            } else {
                                DoWhat.checkPermission(CalendarActivity.this, DoWhat.record_audio);
                                Toast.makeText(CalendarActivity.this, "권한허용 후 다시시도.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //환경설정
                    btnSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(CalendarActivity.this, Preferences.class);
                            startActivity(intent);
                        }
                    });

                } else {
                    btnAdd.setVisibility(View.INVISIBLE);
                    btnMic.setVisibility(View.INVISIBLE);
                    btnSet.setVisibility(View.INVISIBLE);
                    btnPlus.setImageResource(R.drawable.plus);
                    isClick = !isClick;
                }
            }
        });


        //날짜를 눌렀을 때 목록표시 이벤트
        calview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                SetYMD(year, month, day);
                String monthhh= (month+1)<10 ? ("0"+(month+1))+"" : (month+1)+"";
                String dayhhh= day<10 ? ("0"+day)+"" : day+"";
                STTdate=year+"-"+monthhh+"-"+dayhhh;
                Log.i("getx:", calview.getX() + "");
                Log.i("getWidth:", calview.getWidth() + "");
                Log.i("getHeight:", calview.getHeight() + "");
            }
        });
    }

    private void STT() { //
        i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, '"' + "장소" + '"' + " 에서 " + '"' + "일정" + '"' + "\nex)" + '"' + "공원" + '"' + " 에서 " + '"' + "산책하기" + '"');

        Toast.makeText(CalendarActivity.this, "음성인식 시작", Toast.LENGTH_SHORT).show();

        try {
            startActivityForResult(i, RESULT_SPEECH);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "말하기 기능을 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            e.getStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK && (requestCode == RESULT_SPEECH)) {
                ArrayList<String> sstResult = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                final String result_stt = sstResult.get(0);
                final String[] Place_Title = result_stt.split("에서");
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("녹음 확인");
                ab.setMessage("일정 : [ " + Place_Title[1] + " ]\n일시 : [ " + STTdate + " ]\n장소 : [ " + Place_Title[0] + " ]")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ScheduleDTO dto = new ScheduleDTO();
                                id = new PrefManager(CalendarActivity.this).getUserInfo().get("id");
                                dto.setId(id);
                                dto.setTitle(Place_Title[1]);
                                dto.setPlace(Place_Title[0]);
                                Log.i("뭐지",Place_Title[0]);
                                dto.setStarttime("08:00");
                                dto.setEndtime("09:00");
                                dto.setEvent("-");
                                dto.setMemo("-");
                                dto.setTag("-");
                                dto.setStartdate(STTdate);
                                dto.setEnddate(STTdate);
                                dbManager.insertSchedule(dto);
                                Toast.makeText(CalendarActivity.this, "일정이 추가되었습니다.", Toast.LENGTH_SHORT).show();
////                            DoWhat.resetAlarm(CalendarActivity.this, asdf, true);
                                String[] days = STTdate.split("-"); // 년= [0], 월= [1], 일= [2]
                                SetYMD(Integer.parseInt(days[0]), Integer.parseInt(days[1]) -1, Integer.parseInt(days[2]));
                                setDot();
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
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "명령어 또는 네트워크 상태를 확인하세요.", Toast.LENGTH_SHORT).show();
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
            String startdatee= dto.getStartdate();
            String[] dateArr= startdatee.split("-");

            if (dto != null) {
                TextView txtSchedule = (TextView) v.findViewById(R.id.txtSchedule);
                TextView txtStartTime = (TextView) v.findViewById(R.id.txtStarttime);
                ImageView img1 = (ImageView) v.findViewById(R.id.img1);
                //일정 표시
                txtSchedule.setText(dto.getTitle());
                if (!dto.getStarttime().equals("")) {
                    //내용이있으면 시간표시
                    String[] Stimes = dto.getStarttime().split(":");
                    String[] Etimes = dto.getEndtime().split(":");
                    int Shour = Integer.parseInt(Stimes[0]);
                    int Sminute = Integer.parseInt(Stimes[1]);
                    int Ehour = Integer.parseInt(Etimes[0]);
                    int Eminute = Integer.parseInt(Etimes[1]);
                    String h1, h2, m1, m2, setStime, setEtime;
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
                    if (Shour > 12 && Shour < 22) {
                        setStime = "오후 0" + (Shour - 12) + "시 " + m1 + "분";
                    } else if (Shour > 21) {
                        setStime = "오후 " + (Shour - 12) + "시 " + m1 + "분";
                    } else if (Shour == 12) {
                        setStime = "오후 " + h1 + "시 " + m1 + "분";
                    } else {
                        setStime = "오전 " + h1 + "시 " + m1 + "분";
                    }
                    if (Ehour > 12 && Ehour < 22) {
                        setEtime = "오후 0" + (Ehour - 12) + "시 " + m2 + "분";
                    } else if (Ehour > 21) {
                        setEtime = "오후 " + (Ehour - 12) + "시 " + m2 + "분";
                    } else if (Shour == 12) {
                        setEtime = "오후 " + h2 + "시 " + m2 + "분";
                    } else {
                        setEtime = "오전 " + h2 + "시 " + m2 + "분";
                    }
                    String time = setStime + " - " + setEtime;
                    txtStartTime.setText(time);
                } else {
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
                    img1.setImageResource(R.drawable.love);
                    img1.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            }
            return v;
        }
    }

    public ScheduleDTO getSchedule(int index) {
        return items.get(index);
    }

    //날짜세팅 메소드
    private void SetYMD(int year, int month, int day) {
        final String id = new PrefManager(CalendarActivity.this).getUserInfo().get("id");
        String n = String.valueOf(year);
        String w = String.valueOf(month + 1);
        String i = String.valueOf(day);
        int nyun = Integer.parseInt(n);
        int wol = Integer.parseInt(w);
        int il = Integer.parseInt(i);
        if ((month + 1) < 10) { //1~9월에는 앞에 0을 붙임
            w = "0" + String.valueOf(month + 1);
        }
        if (day < 10) { //1~9일에는 앞에 0을 붙임
            i = "0" + String.valueOf(day);
        }
        startdate = n + "-" + w + "-" + i;
        txtDate.setText(n + "년 " + w + "월 " + i + "일 일정"); //텍스트뷰에 날짜표시
        items = new ArrayList<ScheduleDTO>();
        try {
            items = dbManager.getSchedule(id, nyun, wol, il);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        SettingListview();
    }

    private int test(@Nullable String leftOrRight){
        Calendar cal= Calendar.getInstance();
        int year= cal.get(Calendar.YEAR);
        year= Integer.parseInt("20"+(year+"").substring(1));
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH,monthh);
        cal.set(Calendar.DATE,1);


        /*
        1 일
        2 월
        3 화
        4 수
        5 목
        6 금
        7 토
         */
        int startDatee= cal.get(Calendar.DAY_OF_WEEK);
        Log.i("startdatee",startDatee+"");
        return startDatee;
    }

    private void dotsConn(){
        dot0= (ImageView)findViewById(R.id.dot0);
        dot0.setImageResource(R.drawable.dot);
        dot1= (ImageView)findViewById(R.id.dot1);
        dot1.setImageResource(R.drawable.dot);
        dot2= (ImageView)findViewById(R.id.dot2);
        dot2.setImageResource(R.drawable.dot);
        dot3= (ImageView)findViewById(R.id.dot3);
        dot3.setImageResource(R.drawable.dot);
        dot4= (ImageView)findViewById(R.id.dot4);
        dot4.setImageResource(R.drawable.dot);
        dot5= (ImageView)findViewById(R.id.dot5);
        dot5.setImageResource(R.drawable.dot);
        dot6= (ImageView)findViewById(R.id.dot6);
        dot6.setImageResource(R.drawable.dot);
        dot7= (ImageView)findViewById(R.id.dot7);
        dot7.setImageResource(R.drawable.dot);
        dot8= (ImageView)findViewById(R.id.dot8);
        dot8.setImageResource(R.drawable.dot);
        dot9= (ImageView)findViewById(R.id.dot9);
        dot9.setImageResource(R.drawable.dot);
        dot10= (ImageView)findViewById(R.id.dot10);
        dot10.setImageResource(R.drawable.dot);
        dot11= (ImageView)findViewById(R.id.dot11);
        dot11.setImageResource(R.drawable.dot);
        dot12= (ImageView)findViewById(R.id.dot12);
        dot12.setImageResource(R.drawable.dot);
        dot13= (ImageView)findViewById(R.id.dot13);
        dot13.setImageResource(R.drawable.dot);
        dot14= (ImageView)findViewById(R.id.dot14);
        dot14.setImageResource(R.drawable.dot);
        dot15= (ImageView)findViewById(R.id.dot15);
        dot15.setImageResource(R.drawable.dot);
        dot16= (ImageView)findViewById(R.id.dot16);
        dot16.setImageResource(R.drawable.dot);
        dot17= (ImageView)findViewById(R.id.dot17);
        dot17.setImageResource(R.drawable.dot);
        dot18= (ImageView)findViewById(R.id.dot18);
        dot18.setImageResource(R.drawable.dot);
        dot19= (ImageView)findViewById(R.id.dot19);
        dot19.setImageResource(R.drawable.dot);
        dot20= (ImageView)findViewById(R.id.dot20);
        dot20.setImageResource(R.drawable.dot);
        dot21= (ImageView)findViewById(R.id.dot21);
        dot21.setImageResource(R.drawable.dot);
        dot22= (ImageView)findViewById(R.id.dot22);
        dot22.setImageResource(R.drawable.dot);
        dot23= (ImageView)findViewById(R.id.dot23);
        dot23.setImageResource(R.drawable.dot);
        dot24= (ImageView)findViewById(R.id.dot24);
        dot24.setImageResource(R.drawable.dot);
        dot25= (ImageView)findViewById(R.id.dot25);
        dot25.setImageResource(R.drawable.dot);
        dot26= (ImageView)findViewById(R.id.dot26);
        dot26.setImageResource(R.drawable.dot);
        dot27= (ImageView)findViewById(R.id.dot27);
        dot27.setImageResource(R.drawable.dot);
        dot28= (ImageView)findViewById(R.id.dot28);
        dot28.setImageResource(R.drawable.dot);
        dot29= (ImageView)findViewById(R.id.dot29);
        dot29.setImageResource(R.drawable.dot);
        dot30= (ImageView)findViewById(R.id.dot30);
        dot30.setImageResource(R.drawable.dot);
        dot31= (ImageView)findViewById(R.id.dot31);
        dot31.setImageResource(R.drawable.dot);
        dot32= (ImageView)findViewById(R.id.dot32);
        dot32.setImageResource(R.drawable.dot);
        dot33= (ImageView)findViewById(R.id.dot33);
        dot33.setImageResource(R.drawable.dot);
        dot34= (ImageView)findViewById(R.id.dot34);
        dot34.setImageResource(R.drawable.dot);
        dot35= (ImageView)findViewById(R.id.dot35);
        dot35.setImageResource(R.drawable.dot);
        dot36= (ImageView)findViewById(R.id.dot36);
        dot36.setImageResource(R.drawable.dot);
        dot37= (ImageView)findViewById(R.id.dot37);
        dot37.setImageResource(R.drawable.dot);
        dot38= (ImageView)findViewById(R.id.dot38);
        dot38.setImageResource(R.drawable.dot);
        dot39= (ImageView)findViewById(R.id.dot39);
        dot39.setImageResource(R.drawable.dot);
        dot40= (ImageView)findViewById(R.id.dot40);
        dot40.setImageResource(R.drawable.dot);
        dot41= (ImageView)findViewById(R.id.dot41);
        dot41.setImageResource(R.drawable.dot);
        dots= new ImageView[]{
          dot0,dot1,dot2,dot3,dot4,dot5,dot6,dot7,dot8,dot9,dot10,
          dot11,dot12,dot13,dot14,dot15,dot16,dot17,dot18,dot19,dot20,
          dot21,dot22,dot23,dot24,dot25,dot26,dot27,dot28,dot29,dot30,
          dot31,dot32,dot33,dot34,dot35,dot36,dot37,dot38,dot39,dot40,dot41
        };

    }

    int monthh;
    private void setDot() {
        for (ImageView dot : dots) {
            dot.setVisibility(View.INVISIBLE);
        }
        DBManager sibal = new DBManager(this);
        Log.i("asdf", new PrefManager(this).getUserInfo().get("id"));
        Date now = new Date(calview.getDate());
        int year = now.getYear();
        year = Integer.parseInt("20" + (year + "").substring(1));
        Log.i("monthh", (monthh + 1) + "");
        Log.i("monthh", "stat:" + stat + "");
        final ArrayList<ScheduleDTO> lists = sibal.setDot(
          new PrefManager(this).getUserInfo().get("id"), year, monthh + 1
        );
        if (lists == null) {
            return;
        }

        String leftOrRight = null;
        if (stat == 0) {
            leftOrRight = null;
        } else if (stat == 1) {
            leftOrRight = "left";
        } else if (stat == 2) {
            leftOrRight = "right";
        }
        final int startdate = test(leftOrRight) - 2; //5
        DotAnimation dotAnimation= new DotAnimation();
        Animation anim= dotAnimation.alphaOn(0f,0f);
        Log.i("monthh", lists.toString());
        Log.i("monthh", "startdate" + startdate);
            for (ScheduleDTO dto : lists) {
                String startdatee = dto.getStartdate();
                String[] dateArr = startdatee.split("-"); //index 0년 1월 2일
                Log.i("asdf", "dot:" + (startdate + Integer.parseInt(dateArr[2])));
                Log.i("asdf", "date:" + dateArr[2]);
                dots[startdate + Integer.parseInt(dateArr[2])].setAnimation(anim);
                dots[startdate + Integer.parseInt(dateArr[2])].startAnimation(anim);
                dots[startdate + Integer.parseInt(dateArr[2])].setVisibility(View.VISIBLE);
            }
    }

    public void restartApp(){
        handler1.sendEmptyMessage(0);
    }
}