package com.comnawa.dowhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.sangjin.CalendarActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClick(View v) {
    Intent intent = null;
    switch (v.getId()) {
      case R.id.btnInsang:
        intent = new Intent(this, Preferences.class);
        break;
      case R.id.btnSangjin:
        intent = new Intent(this, CalendarActivity.class);
        break;
    }
    startActivity(intent);
  }

}
