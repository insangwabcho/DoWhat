package com.comnawa.dowhat.sangjin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DBManager;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.kwanwoo.PositionActivity;

import java.util.List;


public class DetailActivity extends AppCompatActivity {
    //일정, 장소, 시작일, 종료일, 시작시간, 종료시간, 메모, 알람, 일행
    public static EditText editTag;
    static String taggg="-";
    EditText editTitle, editPlace, editMemo;
    TextView txtSdate, txtStime, txtEdate, txtEtime;
    CheckBox cbAlarm, cbRepeat; //알람설정,반복설정
    ImageButton btnPlace, btnFriend;
    DatePicker dp; //데이트피커
    TimePicker tp; //타임피커
    Spinner spinner; //이벤트 스피너
    boolean dateOk; //시작일과 종료일을 구분할 변수
    boolean timeOk; //시작시간, 종료시간을 구분할 변수
    DatePickerDialog Ddialog; //데이트피커 다이얼로그
    TimePickerDialog Tdialog; //타임피커 다이얼로그
    String event; //DB에 저장할 이벤트
    String DBstime, DBetime; //DB에 저장할 시작시간, 종료시간
    int alarm, repeat; //DB에 저장할 알람, 반복
    private boolean check; //신규 , 수정 판별 변수 (true:신규)
    int Num;
    public static String address; //지도에 입력한 값을 넘길 변수
    int index;

    //주소로 위도, 경도 구하기
    public static Location findGeoPoint(Context mcontext, String address){
        Location loc = new Location("");
        Geocoder coder = new Geocoder(mcontext);
        List<Address> addr = null;
        try {
            addr = coder.getFromLocationName(address, 5);
        }catch (Exception e){
            e.printStackTrace();
        }
        if( addr != null){
            for (int i =0; i<addr.size(); i++){
                Address lating = addr.get(i);
                double lat = lating.getLatitude();
                double lon = lating.getLongitude();
                loc.setLatitude(lat);
                loc.setLongitude(lon);
            }
        }
        return loc;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DoWhat.checkPermission(this, DoWhat.access_fine_location, DoWhat.access_coarse_location);
        editPlace.setText(
          address==null ?
            CalendarActivity.items.get(index).getPlace().equals("-") ? "" : CalendarActivity.items.get(index).getPlace()
            : address
        );
    }

    @Override
    protected void onDestroy() {
        address= null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_savebutton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //빽(취소)키가 눌렸을때 종료여부를 묻는 다이얼로그 띄움
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(this, CalendarActivity.class);
            intent.putExtra("sdate", txtSdate.getText().toString());
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //저장버튼을 눌렀을때 처리
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(DetailActivity.this, Preferences.class));
        } else if (item.getItemId() == R.id.menu_select) {
            String t=editTitle.getText().toString();
            if (t.equals("")) {
                Toast.makeText(this, "일정이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
            } else {
                ScheduleDTO dto = new ScheduleDTO();
                if(event==null || event.equals("")){
                    event="-";
                }
                String edmm=editMemo.getText().toString();
                if(edmm==null || edmm.equals("")){
                    edmm="-";
                }
                String edpc=editPlace.getText().toString();
                if(edpc==null || edpc.equals("")){
                    edpc="-";
                }
                Log.i("edpc",edpc);
                String friendtag=editTag.getText().toString();
                if(friendtag==null || friendtag.equals("")){
                    friendtag="-";
                }

                dto.setNum(Num);
                dto.setId(new PrefManager(this).getUserInfo().get("id"));
                dto.setTitle(editTitle.getText().toString());
                dto.setEvent(event);
                dto.setStartdate(txtSdate.getText().toString());
                dto.setPlace(edpc);
                dto.setEnddate(txtEdate.getText().toString());
                dto.setStarttime(DBstime);
                dto.setEndtime(DBetime);
                dto.setMemo(edmm);
                dto.setAlarm(alarm);
                dto.setRepeat(repeat);
                dto.setTag(friendtag);
                final ScheduleDTO sendDTO= dto;
                if (check) { //신규

                    if (!editTag.getText().toString().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("푸시 서비스")
                          .setMessage("추가된 일정을 친구에게 보내시겠습니까?")
                          .setPositiveButton("예", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  //추가된 날짜 startdate를 putExtra로 넣어서 CalendarActivity에 보내주고 실행 후 finish()
                                  Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                                  intent.putExtra("sdate", txtSdate.getText().toString());
                                  intent.putExtra("newMod", false);
                                  intent.putExtra("cbAlarm", cbAlarm.getText().toString());
                                  DoWhat.resetAlarm(DetailActivity.this, intent, check);
                                  new DBManager(DetailActivity.this).insertSchedule(sendDTO);
                                  ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                  final NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                  final NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                  boolean connWifi = wifi.isConnected();
                                  boolean connMobile = mobile.isConnected();
                                  if (connWifi || connMobile) {
                                      String tags = editTag.getText().toString();
                                      String[] friendssId= taggg.split(",");
                                      String myId = new PrefManager(DetailActivity.this).getUserInfo().get("id");
                                      String myName = new PrefManager(DetailActivity.this).getUserInfo().get("name");
                                      Log.i("nnnnn", taggg);
                                      for (String f : friendssId) {
                                          DoWhat.sendPushMsg(DetailActivity.this, myName + " 님의 일정이 추가되었습니다.", f, myId, myName, sendDTO);
                                      }
                                  } else {
                                      Toast.makeText(DetailActivity.this, "인터넷에 연결되어있지 않아 \n 메세지를 보내지 못하였습니다.", Toast.LENGTH_SHORT).show();
                                  }
                              }
                          })
                          .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                                  intent.putExtra("sdate", txtSdate.getText().toString());
                                  intent.putExtra("newMod", false);
                                  intent.putExtra("cbAlarm", cbAlarm.getText().toString());
                                  DoWhat.resetAlarm(DetailActivity.this, intent, check);
                                  new DBManager(DetailActivity.this).insertSchedule(sendDTO);
                              }
                          })
                          .create().show();

                        //              UpdateNewSchedule uns= new UpdateNewSchedule(this,false,dto);
                        //              uns.start();
                    } else {
                        Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                        intent.putExtra("sdate", txtSdate.getText().toString());
                        intent.putExtra("newMod", false);
                        intent.putExtra("cbAlarm", cbAlarm.getText().toString());
                        DoWhat.resetAlarm(DetailActivity.this, intent, check);
                        new DBManager(DetailActivity.this).insertSchedule(sendDTO);
                    }

                } else { //수정

                    if (!editTag.getText().toString().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("푸시 서비스")
                          .setMessage("수정된 일정을 친구에게 보내시겠습니까?")
                          .setPositiveButton("예", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  //수정한 날짜 startdate를 putExtra로 넣어서 CalendarActivity에 보내주고 실행 후 finish()
                                  Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                                  intent.putExtra("sdate", txtSdate.getText().toString());
                                  intent.putExtra("newMod", false);
                                  intent.putExtra("cbAlarm", cbAlarm.getText().toString());
                                  DoWhat.resetAlarm(DetailActivity.this, intent, check);
                                  new DBManager(DetailActivity.this).updateSchedule(sendDTO);
                                  ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                  final NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                  final NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                  boolean connWifi = wifi.isConnected();
                                  boolean connMobile = mobile.isConnected();
                                  if (connWifi || connMobile) {
                                      String tags = editTag.getText().toString();
                                      String[] friendssId= taggg.split(",");
                                      String myId = new PrefManager(DetailActivity.this).getUserInfo().get("id");
                                      String myName = new PrefManager(DetailActivity.this).getUserInfo().get("name");
                                      Log.i("nnnnn", taggg);
                                      for (String f : friendssId) {
                                          DoWhat.sendPushMsg(DetailActivity.this, myName + " 님의 일정이 수정되었습니다.", f, myId, myName, sendDTO);
                                      }
                                  } else {
                                      Toast.makeText(DetailActivity.this, "인터넷에 연결되어있지 않아 \n 메세지를 보내지 못하였습니다.", Toast.LENGTH_SHORT).show();
                                  }
                              }
                          })
                          .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                                  intent.putExtra("sdate", txtSdate.getText().toString());
                                  intent.putExtra("newMod", false);
                                  intent.putExtra("cbAlarm", cbAlarm.getText().toString());
                                  DoWhat.resetAlarm(DetailActivity.this, intent, check);
                                  new DBManager(DetailActivity.this).updateSchedule(sendDTO);
                              }
                          })
                          .create().show();

                        //              UpdateNewSchedule uns= new UpdateNewSchedule(this,false,dto);
                        //              uns.start();
                    } else {
                        Intent intent = new Intent(DetailActivity.this, CalendarActivity.class);
                        intent.putExtra("sdate", txtSdate.getText().toString());
                        intent.putExtra("newMod", false);
                        intent.putExtra("cbAlarm", cbAlarm.getText().toString());
                        DoWhat.resetAlarm(DetailActivity.this, intent, check);
                        new DBManager(DetailActivity.this).updateSchedule(sendDTO);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(this, CalendarActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로로 고정
        getSupportActionBar().setTitle("일정 등록");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sangjin);

        //신규 , 수정 판별코드
        if (getIntent().getIntExtra("check", 0) == 0) {
            check = true;
        }
         /*

         check 변수가 true면 신규 (insert)
         false일경우 수정 (update)

          */

        index = getIntent().getIntExtra("index", -1);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editPlace = (EditText) findViewById(R.id.editPlace);
        txtSdate = (TextView) findViewById(R.id.txtSdate);
        txtEdate = (TextView) findViewById(R.id.txtEdate);
        txtStime = (TextView) findViewById(R.id.txtStime);
        txtEtime = (TextView) findViewById(R.id.txtEtime);
        editMemo = (EditText) findViewById(R.id.editMemo);
        editTag = (EditText) findViewById(R.id.editTag);
        cbAlarm = (CheckBox) findViewById(R.id.cbAlarm);
        cbRepeat = (CheckBox) findViewById(R.id.cbRepeat);
        btnPlace = (ImageButton) findViewById(R.id.btnPlace);
        btnFriend = (ImageButton) findViewById(R.id.btnFriend);
        dp = (DatePicker) findViewById(R.id.datePicker);
        tp = (TimePicker) findViewById(R.id.timePicker);
        spinner = (Spinner) findViewById(R.id.spinner);

        //장소버튼 클릭 이벤트
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] args= {
                  DoWhat.access_fine_location, DoWhat.access_coarse_location
                };

                if (checkSelfPermission(DoWhat.access_fine_location)==PackageManager.PERMISSION_DENIED ||
                  checkSelfPermission(DoWhat.access_coarse_location)== PackageManager.PERMISSION_DENIED) {
                    for (int i = 0; i < args.length; i++) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            int permissionResult = checkSelfPermission(args[i]);

                            if (permissionResult == PackageManager.PERMISSION_DENIED) {
                                if (shouldShowRequestPermissionRationale(args[i])) {

                                    AlertDialog.Builder dialog = new AlertDialog.Builder(DetailActivity.this);
                                    final int finalI = i;
                                    dialog.setTitle("권한이 필요합니다.")
                                      .setMessage("이 기능을 사용하기 위해서는 권한이 필요합니다. 계속하시겠습니까?")
                                      .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {

                                              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                  requestPermissions(new String[]{args[finalI]}, 1000);
                                              }
                                          }
                                      })
                                      .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which) {
                                              Toast.makeText(DetailActivity.this, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                                          }
                                      })
                                      .create()
                                      .show();
                                } else {
                                    requestPermissions(new String[]{args[i]}, 1000);
                                }
                            }
                        }
                    }
                } else {
                    Intent intent = new Intent(DetailActivity.this, PositionActivity.class);
                    startActivity(intent);
                }
            }
        });

        //친구버튼 클릭 이벤트
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailActivity.this, TagFriendActivity.class);
                startActivity(intent);
            }
        });


        //데이트피커다이얼로그 생성(액티비티, 리스너, 년, 월, 일)
        Ddialog = new DatePickerDialog(this, listener2, dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        //타임피커다이얼로그 생성(액티비티, 리스너, 시, 분, 12시간구분)
        Tdialog = new TimePickerDialog(this, listener, tp.getHour(), tp.getMinute(), false);
        if (index == -1) { //신규
            BasicSet();
        } else { //수정
            ScheduleDTO dto = CalendarActivity.items.get(index);
            Num = dto.getNum();
            editTitle.setText(dto.getTitle());

            String plaace= dto.getPlace().equals("-")? "" : dto.getPlace();
            Log.i("plaace",plaace);
            editPlace.setText(plaace);
//            if (dto.getPlace().equals("-")) {
//                editPlace.setText("");
//            } else {
//                editPlace.setText(dto.getPlace());
//            }

            txtSdate.setText(dto.getStartdate());
            txtEdate.setText(dto.getEnddate());
            txtStime.setText(dto.getStarttime());
            txtEtime.setText(dto.getEndtime());
            DBstime = dto.getStarttime();
            DBetime = dto.getEndtime();
            if (dto.getEvent().equals("생일")) {
                spinner.setSelection(1);
            } else if (dto.getEvent().equals("공휴일")) {
                spinner.setSelection(2);
            } else if (dto.getEvent().equals("기념일")) {
                spinner.setSelection(3);
            } else {
                spinner.setSelection(0);
            }

            if (dto.getMemo().equals("-")) {
                editMemo.setText("");
            } else {
                editMemo.setText(dto.getMemo());
            }

            if (dto.getAlarm() == 0) {
                cbAlarm.setText("해제");
                alarm = 0;
            } else {
                cbAlarm.setChecked(true);
                cbAlarm.setText("설정");
                alarm = 1;
            }

            if (dto.getRepeat() == 0) {
                cbRepeat.setText("해제");
                repeat = 0;
            } else {
                cbRepeat.setChecked(true);
                cbRepeat.setText("설정");
                repeat = 1;
            }
            if(dto.getTag().equals("-")){
                editTag.setText("");
            }else{
                editTag.setText(dto.getTag());
            }
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //시작일 Textview를 눌렀을때
        txtSdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dateOk = true; //시작일
                timeOk = true; //시작시간
                Ddialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //종료일 Textview를 눌렀을때
        txtEdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dateOk = false; //종료일
                timeOk = false; //종료시간
                Ddialog.show(); //다이얼로그 표시
                return false;
            }
        });

        //시작시간 TextView를 눌렀을떄
        txtStime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeOk = true; //시작시간
                Tdialog.show();
                return false;
            }
        });

        //종료시간 TextView를 눌렀을때
        txtEtime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeOk = false;
                Tdialog.show();
                return false;
            }
        });

        cbAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbAlarm.isChecked()) {
                    alarm = 1;
                    cbAlarm.setText("설정");
                } else {
                    alarm = 0;
                    cbAlarm.setText("해제");
                }
            }
        });

        cbRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbRepeat.isChecked()) {
                    repeat = 1;
                    cbRepeat.setText("설정");
                } else {
                    repeat = 0;
                    cbRepeat.setText("해제");
                }
            }
        });
    }

    //데이트피커에서 날짜를 선택하고 확인버튼을 눌렀을때 이벤트
    private DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String n = String.valueOf(year);
            String w = String.valueOf(month + 1);
            String i = String.valueOf(dayOfMonth);
            if ((month + 1) < 10) { //1~9월일경우 앞에 0을붙임
                w = "0" + String.valueOf(month + 1);
            }
            if (dayOfMonth < 10) { //1~9일일경우 앞에 0을 붙임
                i = "0" + String.valueOf(dayOfMonth);
            }
            String date = n + "-" + w + "-" + i;
            //시작일 종료일을 구분하여 알맞는 editText에 입력
            if (dateOk) {
                txtSdate.setText(date);
            } else {
                txtEdate.setText(date);
            }

            //시작일이 종료일보다 나중일때 처리
            String[] Sdays = txtSdate.getText().toString().split("-");
            String[] Edays = txtEdate.getText().toString().split("-");
            int Ey1, Sy1, Em1, Sm1, Ed1, Sd1, Ey2, Sy2, Em2, Sm2, Ed2, Sd2;
            Ey1 = year;
            Sy1 = Integer.parseInt(Sdays[0]);
            Em1 = Integer.parseInt(w);
            Sm1 = Integer.parseInt(Sdays[1]);
            Ed1 = Integer.parseInt(i);
            Sd1 = Integer.parseInt(Sdays[2]);
            if (Ey1 < Sy1) {
                txtSdate.setText(date);
            } else if (Ey1 == Sy1 && Em1 < Sm1) {
                txtSdate.setText(date);
            } else if (Ey1 == Sy1 && Em1 == Sm1 && Ed1 < Sd1) {
                txtSdate.setText(date);
            }
            Sy2 = year;
            Ey2 = Integer.parseInt(Edays[0]);
            Sm2 = Integer.parseInt(w);
            Em2 = Integer.parseInt(Edays[1]);
            Sd2 = Integer.parseInt(i);
            Ed2 = Integer.parseInt(Edays[2]);
            if (Ey2 < Sy2) {
                txtEdate.setText(date);
            } else if (Ey2 == Sy2 && Em2 < Sm2) {
                txtEdate.setText(date);
            } else if (Ey2 == Sy2 && Em2 == Sm2 && Ed2 < Sd2) {
                txtEdate.setText(date);
            }

            //시간 설정 다이얼로그를 띄움
            Tdialog.show();
        }
    };

    //타임피커에서 시간을 선택하고 확인버튼을 눌렀을때 이벤트
    private TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String h = "";
            String m = "";
            if (hourOfDay < 10) { //0~9시일경우 앞에 0을 붙임
                h = "0" + String.valueOf(hourOfDay);
            } else {
                h = String.valueOf(hourOfDay);
            }
            if (minute < 10) { //1~9분일경우 앞에 0을 붙임
                m = "0" + String.valueOf(minute);
            } else {
                m = String.valueOf(minute);
            }
            if (timeOk == true) {
                DBstime = h + ":" + m;
                txtStime.setText(DBstime);
            } else if (timeOk == false) {
                DBetime = h + ":" + m;
                txtEtime.setText(DBetime);
            }

            //날짜가 같고 시작시간이 종료시간보다 나중일때 처리
            String[] StartDay = txtSdate.getText().toString().split("-");
            String[] EndDay = txtEdate.getText().toString().split("-");
            int Sy = Integer.parseInt(StartDay[0]);
            int Ey = Integer.parseInt(EndDay[0]);
            int Sm = Integer.parseInt(StartDay[1]);
            int Em = Integer.parseInt(EndDay[1]);
            int Sd = Integer.parseInt(StartDay[2]);
            int Ed = Integer.parseInt(EndDay[2]);
            if (Sy == Ey && Sm == Em && Sd == Ed) {
                String[] Stimes = txtStime.getText().toString().split(":");
                String[] Etimes = txtEtime.getText().toString().split(":");
                int Eh1, Sh1, Em1, Sm1, Eh2, Sh2, Em2, Sm2;
                Eh1 = Integer.parseInt(h);
                Sh1 = Integer.parseInt(Stimes[0]);
                Em1 = Integer.parseInt(m);
                Sm1 = Integer.parseInt(Stimes[1]);
                if (Eh1 < Sh1) {
                    String resultH;
                    if (Integer.parseInt(Etimes[0]) - 1 < 10) {
                        resultH = "0" + String.valueOf(Integer.parseInt(Etimes[0]) - 1);
                    } else {
                        resultH = String.valueOf(Integer.parseInt(Etimes[0]) - 1);
                    }
                    txtStime.setText(resultH + ":" + Etimes[1]);
                } else if (Eh1 == Sh1 && Em1 < Sm1) {
                    String resultH;
                    if (Integer.parseInt(Etimes[0]) - 1 < 10) {
                        resultH = "0" + String.valueOf(Integer.parseInt(Etimes[0]) - 1);
                    } else {
                        resultH = String.valueOf(Integer.parseInt(Etimes[0]) - 1);
                    }
                    txtStime.setText(resultH + ":" + Etimes[1]);
                }
                Sh2 = Integer.parseInt(h);
                Eh2 = Integer.parseInt(Etimes[0]);
                Sm2 = Integer.parseInt(m);
                Em2 = Integer.parseInt(Etimes[1]);
                if (Eh2 < Sh2) {
                    String resultH;
                    if (Integer.parseInt(Stimes[0]) + 1 < 10) {
                        resultH = "0" + String.valueOf(Integer.parseInt(Stimes[0]) + 1);
                    } else {
                        resultH = String.valueOf(Integer.parseInt(Stimes[0]) + 1);
                    }
                    txtEtime.setText(resultH + ":" + Stimes[1]);
                } else if (Eh2 == Sh2 && Em2 < Sm2) {
                    String resultH;
                    if (Integer.parseInt(Stimes[0]) + 1 < 10) {
                        resultH = "0" + String.valueOf(Integer.parseInt(Stimes[0]) + 1);
                    } else {
                        resultH = String.valueOf(Integer.parseInt(Stimes[0]) + 1);
                    }
                    txtEtime.setText(resultH + ":" + Stimes[1]);
                }
            }
        }
    };

    private void BasicSet() { //신규일정 등록시 기본 날짜와 시간 세팅
        txtSdate.setText(CalendarActivity.startdate);
        DBstime = "08:00";
        txtStime.setText(DBstime);
        txtEdate.setText(CalendarActivity.startdate);
        DBetime = "09:00";
        txtEtime.setText(DBetime);
    }//

    public void setTag(String tag){
        Log.i("tag:",tag);
        editTag.setText(tag);
    }
}