package com.comnawa.dowhat.insang;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.util.Log;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.sangjin.CalendarActivity;

public class PushBroadcast extends BroadcastReceiver {

  private String msg;
  private static PowerManager.WakeLock wakeScreen;

  @Override
  public void onReceive(Context context, Intent intent) {

    Log.i("test","여기4");
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

    Log.i("zzoz",intent.getStringExtra("remoteMessage"));
    Log.i("zzoz",intent.getStringExtra("tagg"));
    msg = intent.getStringExtra("remoteMessage"); //remoteMessage.getNotification().getBody();
    String tagg= intent.getStringExtra("tagg");//remoteMessage.getNotification().gettagg();
    //dongjak 1= 친구추가 2= 일정추가
    int dongjak=0;
    if (msg.indexOf("친구로")!= -1){
      dongjak= 1;
    } else if (msg.indexOf("일정에")!= -1){
      dongjak= 2;
    } else {
      dongjak =0;
    }
    String userid= tagg.split(",")[0];
    String username= tagg.split(",")[1];
    /*

    msg형식 : 누구누구가 친구로 추가하셨습니다.
    msg형식 : 누구누구 님의 일정에 태그되었습니다.
    String[] str= msg.split("|");
    String who= str[0];
    String what= str[1];
    String last= str[2];

    if (str[1].equals("일정을")){

    } else if (str[1].equals("친구로") {

    }
     */

    Intent intentt = null;
    if (dongjak==1){ // 친구추가
      intentt= new Intent(context, CalendarActivity.class);
      intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intentt.putExtra("userid",userid);
      intentt.putExtra("username",username);
    } else if (dongjak==2) { //일정추가
      String titles= intent.getStringExtra("title");
      int start= titles.indexOf("{");
      int end= titles.indexOf("}");
      titles= titles.substring(start, end-1);

      int num;
      String id;
      String startdate;
      String enddate;
      String starttime;
      String endtime;
      String title;
      String event;
      String place;
      String memo;
      String tag;
      int alarm;
      int repeat;

      String[] arr= titles.split("=");

      Log.i("asdfasdf",arr.toString());
//      DBManager dbManager= new DBManager(context);
//      ScheduleDTO dto= new ScheduleDTO();
//      intentt= new Intent(context, CalendarActivity.class);
//      intentt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    NotificationManager notificationManager =
      (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent contentIntent = PendingIntent.getActivity(context, 3,
      intentt, PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(context)
      .setWhen(System.currentTimeMillis())
      .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
      .setContentTitle("DoWhat")
      .setContentIntent(contentIntent)
      .setContentText(msg)
      .setAutoCancel(true)
      .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
      .setSmallIcon(R.drawable.alarm1)
      .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
        dongjak==1 ? R.drawable.friend : R.drawable.alarm1))
      .setColor(0xff123456);
    if (dongjak!=1) {
      builder.setPriority(Notification.PRIORITY_MAX);
    }

    notificationManager.notify(3, builder.build());

  }
}
