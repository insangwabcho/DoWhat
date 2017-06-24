package com.comnawa.dowhat.kwanwoo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comnawa.dowhat.R;

public class CalendarCoreActivity extends AppCompatActivity {
    // 변수선언
    EditText txtTitle, txtPlace, txtStart, txtEnd;
    ImageButton btnColor, btnPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_core_kwanwoo);
        //생성
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtPlace = (EditText) findViewById(R.id.txtPlace);
        txtStart = (EditText) findViewById(R.id.txtStart);
        txtEnd = (EditText) findViewById(R.id.txtEnd);
        btnColor = (ImageButton) findViewById(R.id.btnColor);
        btnPlace = (ImageButton) findViewById(R.id.btnPlace);

//        TextView txtPlace = (TextView)findViewById(R.id.txtPlace);
//      txtPlace.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(v.isClickable()){
//
//                }
//            }
//        });
      //버튼클릭 이벤트
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog cb = new Dialog(CalendarCoreActivity.this);
                cb.setTitle("Color버튼 선택");
                TextView text = new TextView(CalendarCoreActivity.this);
                text.setText("Color버튼의 내용");
                //  cb.setContentView(text); //color버튼의 내용
                //다이얼로그의 화면을 XML로 지정
                cb.setContentView(R.layout.color_choice_kwanwoo);
                cb.show();  //컬러버튼을 화면에 표시
            }
        });

    /*    //지도버튼 눌렀을 때
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(
                       CalendarCoreActivity.this, PositionActivity.class);

                startActivity(intent);
            }
        });*/
}// onCreate
    public void onClick(View v){
        Intent intent = null;
        switch (v.getId()){
            case R.id.btnColor:
                intent = new Intent(this, ColorChoice.class);
                break;
            case R.id.btnPlace:
                intent = new Intent(this, PositionActivity.class);
                break;

        }
        startActivity(intent);
    }//onClick
}
