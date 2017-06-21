package com.comnawa.dowhat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.kwanwoo.CalendarCoreActivity;
import com.comnawa.dowhat.sangjin.DetailActivity;
import com.comnawa.dowhat.sungwon.LoginActivity;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DoWhat.fixedScreen(this, DoWhat.sero); //세로화면 고정
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

//    DoWhat.setActionMenu(this, R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_insang, menu);
    return super.onCreateOptionsMenu(menu);
  }

  public void onClick(View v) {
    Intent intent = null;
    switch (v.getId()) {
      case R.id.btnInsang:
        intent = new Intent(this, Preferences.class);
        break;
//        new PrefManager(this).resetScheduleCount();
//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        int month = 6;
//        int date = cal.get(Calendar.DATE);
//        int hour = cal.get(Calendar.HOUR_OF_DAY);
//        int min = cal.get(Calendar.MINUTE);
//        DoWhat.setAlarm(this, year, month, date, hour, min, "");
//        return;
      case R.id.btnSangjin:
        intent = new Intent(this, DetailActivity.class);
        break;
      case R.id.btnKwanwoo:
        intent= new Intent(this, CalendarCoreActivity.class);
        break;
      case R.id.btnSungwon:
        intent= new Intent(this, LoginActivity.class);
        break;
      case R.id.btnInsang2:
        DoWhat.resetAlarm(this);
        return;
    }
    startActivity(intent);
  } //onClick

}
