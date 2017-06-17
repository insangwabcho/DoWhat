package com.comnawa.dowhat.insang;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class AlarmPref extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    int hour = intent.getIntExtra("hour", 0);
    int min = intent.getIntExtra("min", 0);
    int year= intent.getIntExtra("year",0);
    int month= intent.getIntExtra("month",0)-1;
    int date= intent.getIntExtra("date",0);
    setalarm(year,month,date,hour,min);
    finish();
  }

  public void setalarm(int... args) {
    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(this, AlarmBroadcast.class);
    intent.putExtra("push","푸시내요오오옹");
    int requestCode= 1;
    intent.putExtra("requestCode",requestCode);

//    PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);
    PendingIntent sender = PendingIntent.getBroadcast(this, requestCode, intent, 0);

    Calendar calendar = Calendar.getInstance();
    calendar.set(args[0], args[1], args[2], args[3], args[4], 0);

    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    Log.i("test:", "일정등록 완료");
  }
}
