package com.comnawa.dowhat.sungwon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.sangjin.CalendarActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class confrimActivity extends Activity {

    EditText editAccount;
    Button btnSearch,btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.confrim);

        final String kakaotoken =getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name");

        editAccount = (EditText)findViewById(R.id.editAccount);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnSearch =(Button)findViewById(R.id.btnSearch);
        //계정정보 Serch 이벤트
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                           String page = Common.SERVER_URL+"/Dowhat/Member_servlet/idcheck.do";
                            HashMap<String,String> map = new HashMap<String, String>();
                            map.put("id",editAccount.getText().toString());
                            String body = JsonObject.objectType(page,map);
                            JSONObject jsonObj = new JSONObject(body);
                            if(!jsonObj.get("sendData").equals("success")){ //입력한 계정이 DB에 있으면
                                String page2 = Common.SERVER_URL +"/Dowhat/Member_servlet/tokeninsert.do";
                                HashMap<String,String> map2 = new HashMap<String, String>();
                                map2.put("id",editAccount.getText().toString());
                                map2.put("kakaotoken",kakaotoken);
                                String body2 = JsonObject.objectType(page2,map2);
                                JSONObject jsonObj2 = new JSONObject(body2); //기존계정에 토큰추가
                                int result =  (int) jsonObj2.get("sendData");
                                if(result>0){ //기존계정에 토큰추가 성공
                                    String id =editAccount.getText().toString();
                                    String pwd="";
                                    String friendid ="";
                                    PrefManager pm = new PrefManager(confrimActivity.this);
                                    Log.i("tokeninsert",id+pwd+friendid+kakaotoken+name);
                                    pm.setAutoLogin(id,pwd,name,friendid,kakaotoken,false);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(confrimActivity.this, "계정이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                            Intent intent= new Intent(confrimActivity.this, CalendarActivity.class);
                                            startActivity(intent);

                                        }
                                    });
                                    finish();

                                }
                            }else { //입력한계정이 DB에 없으면
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(confrimActivity.this, "존재하지않는 계정입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
    }
}
