package com.comnawa.dowhat.sangjin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DoWhat;


public class DetailActivity extends AppCompatActivity {
    //일정, 장소, 시작일, 종료일, 시작시간, 종료시간, 메모, 알람, 일행
    EditText editTitle, editPlace, editSdate, editEdate,
            editStime, editEtime, editMemo, editAlarm, editFriend;
    CheckBox cbRepeat; //반복설정
    DatePicker dp; //데이트피커
    boolean dateOk; //시작일과 종료일을 구분할 변수
    int timeOk; //시작시간, 종료시간, 알람을 구분할 변수
    DatePickerDialog Ddialog; //데이트피커 다이얼로그
    TimePickerDialog Tdialog; //타임피커 다이얼로그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로로 고정
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sangjin);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editPlace = (EditText) findViewById(R.id.editPlace);
        editSdate = (EditText) findViewById(R.id.editSdate);
        editEdate = (EditText) findViewById(R.id.editEdate);
        editStime = (EditText) findViewById(R.id.editStime);
        editEtime = (EditText) findViewById(R.id.editEtime);
        editMemo = (EditText) findViewById(R.id.editMemo);
        editAlarm = (EditText) findViewById(R.id.editAlarm);
        editFriend = (EditText) findViewById(R.id.editFriend);
        cbRepeat = (CheckBox) findViewById(R.id.cbRepeat);
        dp = (DatePicker) findViewById(R.id.datePicker);
        //데이트피커다이얼로그 생성(액티비티, 리스너, 년, 월, 일)
        Ddialog=new DatePickerDialog(this, listener2, dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        //타임피커다이얼로그 생성(액티비티, 리스너, 시, 분, 12시간구분)
        Tdialog=new TimePickerDialog(this, listener, 12, 00, false);

        //시작일 editText를 눌렀을때
        editSdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dateOk=true; //시작일
                Ddialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //종료일 editText를 눌렀을때
        editEdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dateOk=false; //종료일
                Ddialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //시작시간 editText를 눌렀을때
        editStime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeOk=1; //시작시간
                Tdialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //종료시간 editText를 눌렀을때
        editEtime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeOk=2; //종료시간
                Tdialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //알람 editText를 눌렀을때
        editAlarm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                timeOk=3; //알람시간
                Tdialog.show(); //다이얼로그 표시
                return false;
            }
        });
    }

    //데이트피커에서 날짜를 선택하고 확인버튼을 눌렀을때 이벤트
    private DatePickerDialog.OnDateSetListener listener2= new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String n = String.valueOf(year);
            String w = String.valueOf(month+1);
            String i = String.valueOf(dayOfMonth);
            if ((month+1) < 10) { //1~9월일경우 앞에 0을붙임
                w = "0" + String.valueOf(month+1);
            }
            if (dayOfMonth < 10) { //1~9일일경우 앞에 0을 붙임
                i = "0" + String.valueOf(dayOfMonth);
            }
            String date = n + "-" + w + "-" + i;
            //시작일 종료일을 구분하여 알맞는 editText에 입력
            if(dateOk){
                editSdate.setText(date);
            }else{
                editEdate.setText(date);
            }
        }
    };

    //타임피커에서 시간을 선택하고 확인버튼을 눌렀을때 이벤트
    private TimePickerDialog.OnTimeSetListener listener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String h="";
            String m="";
            if(hourOfDay<10){ //1~9시일경우 앞에 0을 붙임
                h="0"+String.valueOf(hourOfDay);
            }else{
                h=String.valueOf(hourOfDay);
            }

            if(minute<10){ //1~9분일경우 앞에 0을 붙임
                m="0"+String.valueOf(minute);
            }else{
                m=String.valueOf(minute);
            }
            String time=h+":"+m+":00"; //DB에는 넣어야하므로 초까지 표시
            //시작시간, 종료시간, 알람을 구분하여 알맞는 editText에
            //substring을 이용하여 초를 제거한 시:분 형식으로 입력
            if(timeOk==1){
                editStime.setText(time.substring(0,5));
            }else if(timeOk==2){
                editEtime.setText(time.substring(0,5));
            }else if(timeOk==3){
                editAlarm.setText(time.substring(0,5));
            }
        }
    };

}

