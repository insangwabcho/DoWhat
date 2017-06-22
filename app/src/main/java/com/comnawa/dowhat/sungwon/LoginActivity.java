package com.comnawa.dowhat.sungwon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.sangjin.CalendarActivity;
import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.kakao.util.helper.Utility.getPackageInfo;

public class LoginActivity extends Activity {

    SessionCallback callback;

    EditText editid, editpwd;
    Button btnLogin, btnSignUp;
    CheckBox cb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        //해시 키 출력
        Log.i("hashkey",getKeyHash(getApplicationContext()));

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

        /**카카오톡 로그아웃 요청**/
        //한번 로그인이 성공하면 세션 정보가 남아있어서 로그인창이 뜨지 않고 바로 onSuccess()메서드를 호출합니다.
        //테스트 하시기 편하라고 매번 로그아웃 요청을 수행하도록 코드를 넣었습니다 ^^
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
    }
    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                    Log.e("UserProfile", userProfile.toString());
                    Toast.makeText(LoginActivity.this, userProfile.getId()+"", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, CalendarActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.i("SessionCallback", "Error");
            if(exception != null) {
                exception.printStackTrace();
            }
        }
    }

    //앱 내 자바 코드로 키해시 구하기
    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;
        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }
}

