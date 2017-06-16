package com.comnawa.dowhat.insang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.comnawa.dowhat.R;

public class TestActivity extends AppCompatActivity {

  Button btnLogin;
  Button btnSwitch;
  PrefManager prefManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test_insang);

    btnLogin = (Button) findViewById(R.id.btnLogin);
    btnSwitch = (Button) findViewById(R.id.btnSwitch);
    prefManager = new PrefManager(TestActivity.this);

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        prefManager.setAutoLogin("","");
        Toast.makeText(TestActivity.this, prefManager.getUserID(), Toast.LENGTH_SHORT).show();
      }
    });

  } // clicklistener
}
