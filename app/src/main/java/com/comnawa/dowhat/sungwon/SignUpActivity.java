package com.comnawa.dowhat.sungwon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.comnawa.dowhat.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {


    EditText editName,editId,editPwd1,editPwd2;
    Button btnSignup,btnCheck;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        editName = (EditText) findViewById(R.id.editName);
        editId = (EditText) findViewById(R.id.editId);
        editPwd1 = (EditText) findViewById(R.id.editPwd1);
        editPwd2 = (EditText) findViewById(R.id.editPwd2);
        btnSignup = (Button) findViewById(R.id.btnSignup);
        btnCheck = (Button) findViewById(R.id.btnCheck);

        String name = editName.getText().toString();
        String tmppwd1 = editPwd1.getText().toString();
        String tmppwd2 = editPwd2.getText().toString();

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String page = Common.SERVER_URL+"/Dowhat/Member_servlet/idcheck.do";
                            HttpClient http = new DefaultHttpClient();
                            ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
                            postData.add(new BasicNameValuePair("id",editId.getText().toString()));
                            UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData,"utf-8");
                            HttpPost httpPost = new HttpPost(page);
                            httpPost.setEntity(request);
                            HttpResponse response = http.execute(httpPost);
                            String body = EntityUtils.toString(response.getEntity());
                            JSONObject jsonObj = new JSONObject(body);
                            if((Boolean)jsonObj.get("id")){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                check =true;
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SignUpActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        check = false;
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