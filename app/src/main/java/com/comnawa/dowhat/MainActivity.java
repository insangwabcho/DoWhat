package com.comnawa.dowhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.sangjin.CalendarActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DoWhat.fixedScreen(this, DoWhat.sero); //세로화면 고정
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onClick(View v) {
    Intent intent = null;
    switch (v.getId()) {
      case R.id.btnInsang:
        intent = new Intent(this, Preferences.class);
        break;
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = 6;
//        int date = cal.get(Calendar.DATE);
//        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        int min = cal.get(Calendar.MINUTE);
//        DoWhat.setAlarm(this, year, month, date, hour, min, 100);
//        return;
      case R.id.btnSangjin:
        intent = new Intent(this, CalendarActivity.class);
        break;
    }
    startActivity(intent);
  } //onClick

}
