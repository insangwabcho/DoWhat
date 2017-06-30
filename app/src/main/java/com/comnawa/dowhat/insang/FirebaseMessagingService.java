package com.comnawa.dowhat.insang;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.comnawa.dowhat.R;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

  private static final String TAG = "FirebaseMsgService";

  private String msg;
  private static PowerManager.WakeLock wakeScreen;

  /**
   * Called when message is received.
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */
  // [START receive_message]
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {

    Log.i("test","여기4");
    if (wakeScreen != null) {
      return;
    }

    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
    wakeScreen = pm.newWakeLock(
      PowerManager.SCREEN_BRIGHT_WAKE_LOCK
        | PowerManager.ACQUIRE_CAUSES_WAKEUP
        | PowerManager.ON_AFTER_RELEASE, "");
    wakeScreen.acquire();

    if (wakeScreen != null) {
      wakeScreen.release();
      wakeScreen = null;
    }

    // [START_EXCLUDE]
    // There are two types of messages data messages and notification messages. Data messages are handled
    // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
    // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
    // is in the foreground. When the app is in the background an automatically generated notification is displayed.
    // When the user taps on the notification they are returned to the app. Messages containing both notification
    // and data payloads are treated as notification messages. The Firebase console always sends notification
    // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
    // [END_EXCLUDE]

    // TODO(developer): Handle FCM messages here.
    // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
    Log.d(TAG, "From: " + remoteMessage.getFrom());

    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.d(TAG, "Message data payload: " + remoteMessage.getData());
    }

    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null) {
      Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
    }

    msg = remoteMessage.getNotification().getBody();
    String tag= remoteMessage.getNotification().getTag();
    //dongjak 1= 친구추가 2= 일정추가
    int dongjak=0;
    if (msg.indexOf("친구로")!= -1){
      dongjak= 1;
    } else if (msg.indexOf("일정을")!= -1){
      dongjak= 2;
    }
    String userid= tag.split(",")[0];
    String username= tag.split(",")[1];
    /*

    msg형식 : 누구누구가 || 일정을 or 친구로 || 추가하셨습니다.
    String[] str= msg.split("|");
    String who= str[0];
    String what= str[1];
    String last= str[2];

    if (str[1].equals("일정을")){

    } else if (str[1].equals("친구로") {

    }
     */

    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.

    // [END receive_message]

    Intent intent = null;
    if (dongjak==1){ // 친구추가
      intent= new Intent(this, AddToFriend.class);
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      intent.putExtra("userid",userid);
      intent.putExtra("username",username);
    } else if (dongjak==2) { //일정추가

    }

    PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
      intent, 0);

    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher)
      .setContentTitle("DoWhat")
      .setContentText(msg)
      .setAutoCancel(true)
      .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
      .setSmallIcon(R.drawable.alarm1)
      .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.alarm1))
      .setVibrate(new long[]{1, 1000});

    NotificationManager notificationManager =
      (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    notificationManager.notify(0 /* ID of notification */, mBuilder.build());


    mBuilder.setContentIntent(contentIntent);
  }
}