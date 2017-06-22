package com.comnawa.dowhat.sungwon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.comnawa.dowhat.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

import static com.comnawa.dowhat.sungwon.JsonObject.objectType;

public class SignUpActivity extends AppCompatActivity {


    EditText editName, editId, editPwd1, editPwd2;
    Button btnSignup, btnCheck;
    boolean checkid;
    ImageView imgResult;

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
        imgResult = (ImageView) findViewById(R.id.imgResult);

//아이디
        //editId 정규화
        editId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == false) {
                    if (checkEmailForm(editId.getText().toString()) == false) {
                        Toast.makeText(SignUpActivity.this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                        editId.setText("");
                        checkid = false;
                    }
                } else {
                    checkid = false;
                }
            }
        });

        //중복확인 체크 이벤트
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPwd1.requestFocus();
                if (editId.length() > 0 && checkEmailForm(editId.getText().toString())) {
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String page = Common.SERVER_URL + "/Dowhat/Member_servlet/idcheck.do";
                                HashMap<String,String> map = new HashMap<String, String>();
                                map.put("id", editId.getText().toString());
                                String body = objectType(page,map);
                                JSONObject jsonObj = new JSONObject(body);
                                if (jsonObj.get("sendData").equals("success")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUpActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    checkid = true;
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUpActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                            checkid = false;
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    th.start();
                } else {
                    Toast.makeText(SignUpActivity.this, "아이디를 형식에 맞게 입력하세요", Toast.LENGTH_SHORT).show();
                    checkid = false;
                }
            }
        });
//비밀번호
        //비밀번호 입력 포커스이벤트
        editPwd1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editPwd1.setText("");
                    imgResult.setImageResource(R.drawable.fail);
                }
            }
        });
        //비밀번호확인 포커스이벤트
        editPwd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editPwd2.setText("");
                    if (editPwd1.length() == 0 || editPwd1.length() < 8 || editPwd1.length() > 16) {
                        Toast.makeText(SignUpActivity.this, "비밀번호는 8~16자리로 입력해주세요", Toast.LENGTH_SHORT).show();
                        editPwd2.setText("");
                        editPwd1.requestFocus();
                    }
                }
            }
        });
        //텍스트 입력변화 이벤트
        editPwd2.addTextChangedListener(new TextWatcher() {
            //입력전
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            //입력중
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPwd1.getText().toString().equals(editPwd2.getText().toString())) {
                    imgResult.setImageResource(R.drawable.success);
                } else {
                    imgResult.setImageResource(R.drawable.fail);
                }
            }

            //입력이 끝난후
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // 회원가입 버튼 클릭 이벤트
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //중복확인 상태가 true 이고 이름의 글자수가 0이 아니고 비밀번호가 8~16자리사이이고 일치할때
                if (checkid && editName.length() > 0 && editPwd1.length() >= 8 && editPwd1.length() < 16
                        && editPwd1.getText().toString().equals(editPwd2.getText().toString())) {
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String page = Common.SERVER_URL + "/Dowhat/Member_servlet/signup.do";
                                HashMap<String,String> map = new HashMap<String, String>();
                                map.put("name", editName.getText().toString());
                                map.put("id", editId.getText().toString());
                                map.put("password", editPwd1.getText().toString());
                                String body = objectType(page,map);
                                JSONObject jsonMain = new JSONObject(body);
                                int result = (int) jsonMain.get("sendData");

                                if (result > 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUpActivity.this, "알수없는 오류로 회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    th.start();
                } else if (editName.length() == 0) {
                    Toast.makeText(SignUpActivity.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    editName.requestFocus();
                } else if (checkid == false) {
                    Toast.makeText(SignUpActivity.this, "아이디 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    editId.requestFocus();
                } else if (!editPwd1.getText().toString().equals(editPwd2.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    editPwd1.requestFocus();
                }
            }
        });

    }

    //Editid정규화 메소드
    public boolean checkEmailForm(String src) {
        String emailRegex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+[.]+[a-z.]+$";
        return Pattern.matches(emailRegex, src);
    }


}