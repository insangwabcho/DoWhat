package com.comnawa.dowhat.insang;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.PowerManager;

import com.comnawa.dowhat.MainActivity;
import com.comnawa.dowhat.R;

public class AlarmBroadcast extends BroadcastReceiver {

  private static PowerManager.WakeLock wakeScreen;

  @Override
  public void onReceive(Context context, Intent intent) {

    if (wakeScreen != null) {
      return;
    }

    PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    wakeScreen = pm.newWakeLock(
      PowerManager.SCREEN_BRIGHT_WAKE_LOCK
        | PowerManager.ACQUIRE_CAUSES_WAKEUP
        | PowerManager.ON_AFTER_RELEASE, "");
    wakeScreen.acquire();

    if (wakeScreen != null) {
      wakeScreen.release();
      wakeScreen = null;
    }

    NotificationManager notificationmanager =
      (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent pendingIntent =
      PendingIntent.getActivity(context, intent.getIntExtra("requestCode", 0)
        , new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(context);

    builder.setSmallIcon(R.drawable.green)
      .setWhen(System.currentTimeMillis())
      .setContentTitle("일정제목 (오늘의 " + intent.getIntExtra("requestCode", 0) + "번째 일정)")
      .setContentText("탭하면 상세정보창으로 이동됩니다")
      .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)
      .setSmallIcon(R.drawable.alarm1)
      .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.alarm1))
      .setColor(0xff123456)
      .setPriority(Notification.PRIORITY_MAX);


    notificationmanager.notify(1, builder.build());
    PrefManager prefManager = new PrefManager(context);
    prefManager.setScheduleCount(prefManager.getScheduleCount() - 1);
  }

}
