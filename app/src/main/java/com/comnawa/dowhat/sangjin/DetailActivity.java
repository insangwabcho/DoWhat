package com.comnawa.dowhat.sangjin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DoWhat;


public class DetailActivity extends AppCompatActivity {
    //일정, 장소, 시작일, 종료일, 시작시간, 종료시간, 메모, 알람, 일행
    EditText editTitle, editPlace, editMemo, editAlarm, editFriend;
    TextView txtSdate, txtStime, txtEdate, txtEtime;
    CheckBox cbRepeat; //반복설정
    DatePicker dp; //데이트피커
    TimePicker tp; //타임피커
    Spinner spinner; //이벤트 스피너
    String event; //이벤트를 저장할 변수
    boolean dateOk; //시작일과 종료일을 구분할 변수
    int timeOk; //시작시간, 종료시간, 알람을 구분할 변수
    DatePickerDialog Ddialog; //데이트피커 다이얼로그
    TimePickerDialog Tdialog; //타임피커 다이얼로그
    String DBstime, DBetime; //DB에 담을 시간
//    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로로 고정
        getSupportActionBar().setTitle("일정추가");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sangjin);
        int index=getIntent().getIntExtra("index",-1);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editPlace = (EditText) findViewById(R.id.editPlace);
        txtSdate = (TextView) findViewById(R.id.txtSdate);
        txtEdate = (TextView) findViewById(R.id.txtEdate);
        txtStime = (TextView) findViewById(R.id.txtStime);
        txtEtime = (TextView) findViewById(R.id.txtEtime);
        editMemo = (EditText) findViewById(R.id.editMemo);
        editFriend = (EditText) findViewById(R.id.editFriend);
        cbRepeat = (CheckBox) findViewById(R.id.cbRepeat);
        dp = (DatePicker) findViewById(R.id.datePicker);
        tp = (TimePicker) findViewById(R.id.timePicker);
        spinner = (Spinner) findViewById(R.id.spinner);
        //데이트피커다이얼로그 생성(액티비티, 리스너, 년, 월, 일)
        Ddialog = new DatePickerDialog(this, listener2, dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        //타임피커다이얼로그 생성(액티비티, 리스너, 시, 분, 12시간구분)
        Tdialog = new TimePickerDialog(this, listener, tp.getHour(), tp.getMinute(), false);
        if(index==-1){
            BasicSet();
        }else{
            ScheduleDTO dto=CalendarActivity.items.get(index);
            editTitle.setText(dto.getTitle());
            editPlace.setText(dto.getPlace());
            txtSdate.setText(dto.getStartdate());
            txtEdate.setText(dto.getEnddate());
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
            if(Ehour>12 && Ehour<22){
                setEtime = "오후 0"+(Ehour-12) + "시 " + m2 +"분";
            }else if(Ehour>21){
                setEtime = "오후 "+(Shour-12) + "시 " + m2 +"분";
            }else if(Ehour==12){
                setEtime = "오후 "+h2+"시 "+m2+"분";
            }else{
                setEtime = "오전 "+h2+"시 "+m2+"분";
            }
            txtStime.setText(setStime);
            txtEtime.setText(setEtime);
            if(dto.getEvent().equals("생일")) {
                spinner.setSelection(1);
            }else if(dto.getEvent().equals("공휴일")){
                spinner.setSelection(2);
            }else if(dto.getEvent().equals("기념일")){
                spinner.setSelection(3);
            }else{
                spinner.setSelection(0);
            }
            editMemo.setText(dto.getMemo());

        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event=String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //시작일 editText를 눌렀을때
        txtSdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            //    hideKeyboard();
                dateOk = true; //시작일
                Ddialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //종료일 editText를 눌렀을때
        txtEdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            //    hideKeyboard();
                dateOk = false; //종료일
                Ddialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //시작시간 editText를 눌렀을때
        txtStime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
           //     hideKeyboard();
                timeOk = 1; //시작시간
                Tdialog.show(); //다이얼로그 표시
                return false;
            }
        });
        //종료시간 editText를 눌렀을때
        txtEtime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            //    hideKeyboard();
                timeOk = 2; //종료시간
                Tdialog.show(); //다이얼로그 표시
                return false;
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
            int Ey1,Sy1,Em1,Sm1,Ed1,Sd1,Ey2,Sy2,Em2,Sm2,Ed2,Sd2;
            Ey1=year;
            Sy1=Integer.parseInt(txtSdate.getText().toString().substring(0,4));
            Em1=Integer.parseInt(w);
            Sm1=Integer.parseInt(txtSdate.getText().toString().substring(5,7));
            Ed1=Integer.parseInt(i);
            Sd1=Integer.parseInt(txtSdate.getText().toString().substring(8));
            if(Ey1<Sy1){
                txtSdate.setText(date);
            }else if(Ey1==Sy1 && Em1<Sm1){
                txtSdate.setText(date);
            }else if(Ey1==Sy1 && Em1==Sm1 && Ed1<Sd1){
                txtSdate.setText(date);
            }
            Sy2=year;
            Ey2=Integer.parseInt(txtEdate.getText().toString().substring(0,4));
            Sm2=Integer.parseInt(w);
            Em2=Integer.parseInt(txtEdate.getText().toString().substring(5,7));
            Sd2=Integer.parseInt(i);
            Ed2=Integer.parseInt(txtEdate.getText().toString().substring(8));
            if(Ey2<Sy2){
                txtEdate.setText(date);
            }else if(Ey2==Sy2 && Em2<Sm2){
                txtEdate.setText(date);
            }else if(Ey2==Sy2 && Em2==Sm2 && Ed2<Sd2){
                txtEdate.setText(date);
            }
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
            String eTime =""; //editText에 출력할 시간
            if(hourOfDay>12 && hourOfDay<22){
                eTime = "오후 0"+(hourOfDay-12) + "시 " + m +"분";
            }else if(hourOfDay>21){
                eTime = "오후 "+(hourOfDay-12) +"시 "+ m +"분";
            }else if(hourOfDay==12){
                eTime = "오후 "+h+"시 "+m+"분";
            }else{
                eTime = "오전 "+h+"시 "+m+"분";
            }
            //시작시간, 종료시간, 알람을 구분하여 알맞는 editText에 출력
            //DB에는 시:분 형식으로 저장
            if (timeOk == 1) {
                DBstime=h+":"+m;
                txtStime.setText(eTime);
            } else if (timeOk == 2) {
                DBetime=h+":"+m;
                txtEtime.setText(eTime);
            } else if (timeOk == 3) {
                editAlarm.setText(eTime);
            }
        }
    };

/*    private void init(){
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        editSdate = (EditText) findViewById(R.id.editSdate);
        editEdate = (EditText) findViewById(R.id.editEdate);
        editStime = (EditText) findViewById(R.id.editStime);
        editEtime = (EditText) findViewById(R.id.editEtime);
        editAlarm = (EditText) findViewById(R.id.editAlarm);
    }

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(editSdate.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editEdate.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editStime.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editEtime.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(editAlarm.getWindowToken(), 0);
    }*/

    private void BasicSet(){ //신규일정 등록시 기본 날짜와 시간 세팅
        txtSdate.setText(CalendarActivity.startdate);
        txtStime.setText("오전 08시 00분");
        DBstime="08:00";
        txtEdate.setText(CalendarActivity.startdate);
        txtEtime.setText("오전 09시 00분");
        DBetime="09:00";
    }

}

