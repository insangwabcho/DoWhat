package com.comnawa.dowhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.comnawa.dowhat.insang.DBManager;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.kwanwoo.CalendarCoreActivity;
import com.comnawa.dowhat.sangjin.CalendarActivity;
import com.comnawa.dowhat.sangjin.ScheduleDTO;
import com.comnawa.dowhat.sungwon.LoginActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DoWhat.fixedScreen(this, DoWhat.sero); //세로화면 고정
    super.onCreate(savedInstanceState);
    ActionBar actionBar= getSupportActionBar();
    DoWhat.setTitleBar(this, "DoWhat 개발자 디버깅전용화면");
    setContentView(R.layout.activity_main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_pref, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()== R.id.action_settings) {
      startActivity(new Intent(MainActivity.this, Preferences.class));
    }
    return false;
  }

  public void onClick(View v) {
    Intent intent = null;
    switch (v.getId()) {
      case R.id.btnInsang:
        Toast.makeText(this, "테스트중인거없습니다 ㅡ ㅡ", Toast.LENGTH_SHORT).show();
//        intent = new Intent(this, Preferences.class);
//        break;
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
        intent = new Intent(this, CalendarActivity.class);
        break;
      case R.id.btnKwanwoo:
        intent = new Intent(this, CalendarCoreActivity.class);
        break;
      case R.id.btnSungwon:
        intent = new Intent(this, LoginActivity.class);
        break;
      case R.id.btnInsang2:
        DBManager dbm= new DBManager(this);
        ScheduleDTO dto= new ScheduleDTO();
        dto.setTitle("title");
        dto.setId("id");
        ArrayList<ScheduleDTO> items= new ArrayList<>();
        items.add(dto);
        dbm.insertSchedule(dto);
        for (ScheduleDTO t: items) {
          Log.i("zzo", t.getTitle());
        }

        return;
      case R.id.btninsang3:
        PrefManager prefManager= new PrefManager(this);
        prefManager.setAutoLogin("dowhat@dowhat.com","dowhat1234","dowhat","","",true);
        return;
    }
    startActivity(intent);
  } //onClick

}
