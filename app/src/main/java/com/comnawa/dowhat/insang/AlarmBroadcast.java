package com.comnawa.dowhat.insang;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.comnawa.dowhat.MainActivity;
import com.comnawa.dowhat.R;

public class AlarmBroadcast extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {

    Intent intent1 = new Intent(context, AlarmService.class);
    context.stopService(intent1);
    context.startService(intent1);

    NotificationManager notificationmanager =
      (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent pendingIntent =
      PendingIntent.getActivity(context, intent.getIntExtra("requestCode", 0)
        , new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(context);

    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    builder.setSmallIcon(R.drawable.green)
      .setTicker("현재시각 일정이 있습니다")
      .setWhen(System.currentTimeMillis())
      .setContentTitle("푸쉬 제목" + intent.getIntExtra("requestCode", 0))
      .setContentText(intent.getStringExtra("push"))
      .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true)
      .setPriority(Notification.PRIORITY_MAX);


    notificationmanager.notify(1, builder.build());
  }
}
