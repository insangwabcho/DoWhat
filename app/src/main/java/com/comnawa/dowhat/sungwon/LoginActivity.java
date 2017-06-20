package com.comnawa.dowhat.sungwon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.PrefManager;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText editid, editpwd;
    Button btnLogin, btnSignUp;
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        editid = (EditText) findViewById(R.id.editid);
        editpwd = (EditText) findViewById(R.id.editpwd);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        cb = (CheckBox)findViewById(R.id.cb);
        if(new PrefManager(this).getAutoLogin()){
            Intent intent = new Intent(LoginActivity.this,CalendarActivity.class);
            HashMap<String,String> map = new PrefManager(this).getUserInfo();
            intent.putExtra("id",map.get("id"));
            intent.putExtra("password",map.get("password"));
            intent.putExtra("name",map.get("name"));
            intent.putExtra("friendid",map.get("friendid"));
            startActivity(intent);
            finish();
        }
        //회원가입 버튼 클릭 이벤트
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
        //로그인 버튼 클릭 이벤트
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String page = Common.SERVER_URL + "/Dowhat/Member_servlet/login.do";
                            Log.i("test", page);
                            HttpClient http = new DefaultHttpClient();
                            ArrayList<NameValuePair> postData = new ArrayList<>();
                            postData.add(new BasicNameValuePair("id", editid.getText().toString()));
                            postData.add(new BasicNameValuePair("password", editpwd.getText().toString()));
                            UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData, "utf-8");
                            HttpPost httpPost = new HttpPost(page);
                            httpPost.setEntity(request);
                            HttpResponse response = http.execute(httpPost);

                            String body = EntityUtils.toString(response.getEntity());
                            JSONObject jsonObj = new JSONObject(body);
                            final JSONArray jArray = (JSONArray) jsonObj.get("sendData");
                            if (jArray.length() > 0) {
                                JSONObject jlist = (JSONObject) jArray.get(0);
                                String id = jlist.get("id").toString();
                                String pwd = jlist.get("password").toString();
                                String name = jlist.get("name").toString();
                                String friendid = jlist.get("friendid").toString();
                                PrefManager pm = new PrefManager(LoginActivity.this);
                                if(cb.isChecked()){
                                    pm.setAutoLogin(id,pwd,name,friendid);
                                }
                                Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("password", pwd);
                                intent.putExtra("name", name);
                                intent.putExtra("friendid", friendid);
                                startActivity(intent);
                                finish();
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 확인해 주세요", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
    }
}