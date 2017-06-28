package com.comnawa.dowhat.kwanwoo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.comnawa.dowhat.R;

public class CalendarCoreActivity extends AppCompatActivity {
    // 변수선언
    EditText txtTitle;
    EditText txtPlace;
    ImageButton btnColor, btnPlace;
    Bundle extraBundle;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_core_kwanwoo);
        //생성
        txtTitle = (EditText) findViewById(R.id.txtTitle);
        txtPlace = (EditText) findViewById(R.id.txtPlace);
        btnColor = (ImageButton) findViewById(R.id.btnColor);
        btnPlace = (ImageButton) findViewById(R.id.btnPlace);

      //버튼클릭 이벤트
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog cb = new Dialog(CalendarCoreActivity.this);
                cb.setTitle("");
                TextView text = new TextView(CalendarCoreActivity.this);
                text.setText("");
                //  cb.setContentView(text); //color버튼의 내용
                //다이얼로그의 화면을 XML로 지정
                cb.setContentView(R.layout.color_choice_kwanwoo);
                cb.show();  //컬러버튼을 화면에 표시
            }
        });

       //지도버튼 눌렀을 때
        findViewById(R.id.btnPlace).setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(),PositionActivity.class);
                        startActivity(intent);

                    }
                }
        );
        //텍스트를 눌렀을 때 구글맵 이동
        txtPlace = (EditText) findViewById(R.id.txtPlace);
        txtPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                startActivity(intent);
               // startActivityForResult(intent,1);
               finish();
            }
        });
            extraBundle = new Bundle();
            extraBundle.putString("key1","testkey1");
            extraBundle.putString("key2","testkey2");

            intent = new Intent();
            //intent.putExtra(this.extraBundle);
            this.setResult(RESULT_OK,intent);


        //텍스트를 눌렀을 때
  /*      txtPlace.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });*/
        //저장버튼
        Button btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlOk = new AlertDialog.Builder(CalendarCoreActivity.this);
                dlOk.setTitle("저장알림")
                    .setMessage("내용을 저장하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CalendarCoreActivity.this,"일정이 저장되었습니다."
                            ,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CalendarCoreActivity.this,"설정작업을 계속합니다."
                                ,Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
               // finish();

            }
        });

    }// onCreate

    //취소버튼
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dlBack = new AlertDialog.Builder(CalendarCoreActivity.this);
            dlBack.setTitle("취소 알림")
                    .setMessage("취소하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CalendarCoreActivity.this,"일정이 저장되지 않았습니다."
                                    ,Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CalendarCoreActivity.this,"설정작업을 계속합니다."
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }).create().show();
    }//back

}
