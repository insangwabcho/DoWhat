package com.comnawa.dowhat.sangjin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.DBManager;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.insang.Preferences;


public class DetailActivity extends AppCompatActivity {
    //일정, 장소, 시작일, 종료일, 시작시간, 종료시간, 메모, 알람, 일행
    EditText editTitle, editPlace, editMemo, editFriend;
    TextView txtSdate, txtStime, txtEdate, txtEtime;
    CheckBox cbAlarm, cbRepeat; //알람설정,반복설정
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_savebutton, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //저장버튼을 눌렀을때 처리
        if (item.getItemId()== R.id.action_settings) {
            startActivity(new Intent(DetailActivity.this, Preferences.class));
        } else if (item.getItemId()== R.id.menu_select){

            ScheduleDTO dto = new ScheduleDTO();
            dto.setNum(Num);
            dto.setId(new PrefManager(this).getUserInfo().get("id"));
            dto.setTitle(editTitle.getText().toString());
            dto.setEvent(event);
            dto.setStartdate(txtSdate.getText().toString());
            dto.setEnddate(txtEdate.getText().toString());
            dto.setStarttime(DBstime);
            dto.setEndtime(DBetime);
            dto.setMemo(editMemo.getText().toString());
            dto.setAlarm(alarm);
            dto.setRepeat(repeat);

            DBManager dbManager= new DBManager(this);
            if (check) { //신규
                dbManager.insertSchedule(dto);
                Toast.makeText(this, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
//              UpdateNewSchedule uns= new UpdateNewSchedule(this,true,dto);
//              uns.start();
            } else { //수정
                dbManager.updateSchedule(dto);
                Toast.makeText(this, "수정 되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
//              UpdateNewSchedule uns= new UpdateNewSchedule(this,false,dto);
//              uns.start();
            }

        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로로 고정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_sangjin);

        //신규 , 수정 판별코드
        if (getIntent().getIntExtra("check",0) == 0){
            check= true;
        }
        /*

        check 변수가 true면 신규 (insert)
        false일경우 수정 (update)

         */

        int index=getIntent().getIntExtra("index",-1);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editPlace = (EditText) findViewById(R.id.editPlace);
        txtSdate = (TextView) findViewById(R.id.txtSdate);
        txtEdate = (TextView) findViewById(R.id.txtEdate);
        txtStime = (TextView) findViewById(R.id.txtStime);
        txtEtime = (TextView) findViewById(R.id.txtEtime);
        editMemo = (EditText) findViewById(R.id.editMemo);
     //   editFriend = (EditText) findViewById(R.id.editFriend);
        cbAlarm = (CheckBox) findViewById(R.id.cbAlarm);
        cbRepeat = (CheckBox) findViewById(R.id.cbRepeat);
        dp = (DatePicker) findViewById(R.id.datePicker);
        tp = (TimePicker) findViewById(R.id.timePicker);
        spinner = (Spinner) findViewById(R.id.spinner);
        //데이트피커다이얼로그 생성(액티비티, 리스너, 년, 월, 일)
        Ddialog = new DatePickerDialog(this, listener2, dp.getYear(), dp.getMonth(), dp.getDayOfMonth());
        //타임피커다이얼로그 생성(액티비티, 리스너, 시, 분, 12시간구분)
        Tdialog = new TimePickerDialog(this, listener, tp.getHour(), tp.getMinute(), false);
        if(index==-1){ //신규
            BasicSet();
        }else{ //수정
            ScheduleDTO dto=CalendarActivity.items.get(index);
            Num=dto.getNum();
            editTitle.setText(dto.getTitle());
            if(dto.getPlace() != null){
                editPlace.setText(dto.getPlace());
            }else {
                editPlace.setText("");
            }
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

            if(editMemo!=null){
                editMemo.setText(dto.getMemo());
            }else{
                editMemo.setText("");
            }

            if(dto.getAlarm() == 0){
                cbAlarm.setText("해제");
            }else{
                cbAlarm.setChecked(true);
                cbAlarm.setText("설정");
            }

            if(dto.getRepeat() == 0){
                cbRepeat.setText("해제");
            }else{
                cbRepeat.setChecked(true);
                cbRepeat.setText("설정");
            }

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

        cbAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbAlarm.isChecked()){
                    alarm = 1;
                    cbAlarm.setText("설정");
                    Toast.makeText(DetailActivity.this, "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();
                }else {
                    alarm = 0;
                    cbAlarm.setText("해제");
                    Toast.makeText(DetailActivity.this, "해제되었습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cbRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbRepeat.isChecked()){
                    repeat = 1;
                    cbRepeat.setText("설정");
                }else {
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
            String time =""; //txtTime에 출력할 시간
            if(hourOfDay>12 && hourOfDay<22){
                time = "오후 0"+(hourOfDay-12) + "시 " + m +"분";
            }else if(hourOfDay>21){
                time = "오후 "+(hourOfDay-12) +"시 "+ m +"분";
            }else if(hourOfDay==12){
                time = "오후 "+h+"시 "+m+"분";
            }else{
                time = "오전 "+h+"시 "+m+"분";
            }
            //시작시간, 종료시간, 알람을 구분하여 알맞는 editText에 출력
            //DB에는 시:분 형식으로 저장
            if (timeOk == true) {
                DBstime=h+":"+m;
                txtStime.setText(time);
            } else if (timeOk == false) {
                DBetime=h+":"+m;
                txtEtime.setText(time);
            }

        }
    };

    private void BasicSet(){ //신규일정 등록시 기본 날짜와 시간 세팅
        txtSdate.setText(CalendarActivity.startdate);
        txtStime.setText("오전 08시 00분");
        DBstime="08:00";
        txtEdate.setText(CalendarActivity.startdate);
        txtEtime.setText("오전 09시 00분");
        DBetime="09:00";
    }

}

