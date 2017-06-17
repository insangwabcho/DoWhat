package com.comnawa.dowhat.insang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
      if (new PrefManager(context).getPushAlarm()) {
        Intent i = new Intent(context, AlarmService.class);
        context.startService(i);
      }
    }
  }
}