package com.comnawa.dowhat.insang;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class AlarmService extends Service {
  boolean isRunning;

  @Override
  public void onCreate() {
    super.onCreate();
  } //서비스 생성될시 (최초 1회)

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    super.onStartCommand(intent, flags, startId);

    isRunning = true;
    Thread th = new Thread(new MyThread());
    th.start();

    return Service.START_REDELIVER_INTENT;
  } //서비스 시작

  @Override
  public void onDestroy() {
    super.onDestroy();
    isRunning = false;
  } // 서비스가 종료될때


  class MyThread extends Thread {
    @Override
    public void run() {
      while (isRunning) {
        handler.sendEmptyMessage(0);
        try {
          Thread.sleep(4000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      super.handleMessage(msg);
      Toast.makeText(AlarmService.this, "서비스 실행중", Toast.LENGTH_SHORT).show();
    }
  };


  public AlarmService() {

  }

  @Override
  public IBinder onBind(Intent intent) {
    // TODO: Return the communication channel to the service.
    throw new UnsupportedOperationException("Not yet implemented");
  }
}
