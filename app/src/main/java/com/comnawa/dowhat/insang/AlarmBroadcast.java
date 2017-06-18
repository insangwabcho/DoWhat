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

  String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;

  @Override
  public void onReceive(Context context, Intent intent) {

    NotificationManager notificationmanager =
      (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent pendingIntent =
      PendingIntent.getActivity(context, intent.getIntExtra("requestCode", 0), new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

    Notification.Builder builder = new Notification.Builder(context);

    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    builder.setSmallIcon(R.drawable.green)
      .setTicker("현재시각 일정이 있습니다")
      .setWhen(System.currentTimeMillis())
      .setContentTitle("푸쉬 제목" + intent.getIntExtra("requestCode", 0))
      .setContentText(intent.getStringExtra("push"))
      .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
      .setContentIntent(pendingIntent)
      .setAutoCancel(true);


    notificationmanager.notify(1, builder.build());
  }

//  setSmallIcon : 아이콘입니다 구 소스의 icon이랑 같습니다
//  setTicker : 알림이 뜰때 잠깐 표시되는 Text이며, 구 소스의 tickerText이랑 같습니다
//  setWhen : 알림이 표시되는 시간이며, 구 소스의 when이랑 같습니다
//  setNumber : 미확인 알림의 개수이며, 구 소스의 notification.number랑 같습니다
//  setContentTitle : 상단바 알림 제목이며, 구 소스의 contentTitle랑 같습니다
//  setContentText : 상단바 알림 내용이며, 구 소스의 contentText랑 같습니다
//  setDefaults : 기본 설정이며, 구 소스의 notification.defaults랑 같습니다
//  setContentIntent : 실행할 작업이 담긴 PendingIntent이며, 구 소스의 contentIntent랑 같습닏
//  setAutoCancel : 터치하면 자동으로 지워지도록 설정하는 것이며, 구 소스의 FLAG_AUTO_CANCEL랑 같습니다
//  setPriority : 우선순위입니다, 구 소스의 notification.priority랑 같습니다만 구글 개발자 API를 보면 API 16이상부터 사용이 가능하다고 합니다
//  setOngoing : 진행중알림 이며, 구 소스의 FLAG_ONGOING_EVENT랑 같습니다
//  addAction : 알림에서 바로 어떤 활동을 할지 선택하는 것이며, 스샷찍은다음 삭제/공유 같은것이 이에 해당합니다

}
