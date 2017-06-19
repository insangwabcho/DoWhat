package com.comnawa.dowhat.kwanwoo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.comnawa.dowhat.R;

public class CalendarCoreActivity extends AppCompatActivity {
  // 변수선언
    EditText txtTitle, txtPlace;
    ImageButton btnColor, btnPlace;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_core_kwanwoo);
    //생성
        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtPlace = (EditText)findViewById(R.id.txtPlace);
        btnColor = (ImageButton)findViewById(R.id.btnColor);
        btnPlace = (ImageButton)findViewById(R.id.btnPlace);
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
    }// onCreate
}
