package com.comnawa.dowhat.insang;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.comnawa.dowhat.R;

import java.util.Calendar;

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
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(TestActivity.this, AlarmBroadcast.class);

        PendingIntent sender = PendingIntent.getBroadcast(TestActivity.this, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        //알람시간 calendar에 set해주기

        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 13, 25, 0);

        //알람 예약
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);

      }
    });


  } // clicklistener
}
