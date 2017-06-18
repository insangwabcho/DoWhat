package com.comnawa.dowhat.insang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DateChangedReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
      context.stopService(new Intent(context, AlarmService.class));
      context.startService(new Intent(context, AlarmService.class));
    }
  }
}
