package com.comnawa.dowhat.insang;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) && new PrefManager(context).getPushAlarm()) {
      ComponentName cName = new ComponentName(context.getPackageName(), AlarmService.class.getName());
      ComponentName svcName = context.startService(new Intent().setComponent(cName));
    }
  }
}