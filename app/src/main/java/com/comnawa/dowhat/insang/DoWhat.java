package com.comnawa.dowhat.insang;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

public class DoWhat {

  //화면고정 옵션
  public static final int sero = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
  public static final int garo = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
  //권한 옵션
  public static final String read_calendar = Manifest.permission.READ_CALENDAR;
  public static final String write_calendar = Manifest.permission.WRITE_CALENDAR;
  public static final String camera = Manifest.permission.CAMERA;
  public static final String read_contacts = Manifest.permission.READ_CONTACTS;
  public static final String write_contacts = Manifest.permission.WRITE_CONTACTS;
  public static final String get_accounts = Manifest.permission.GET_ACCOUNTS;
  public static final String access_fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
  public static final String access_coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
  public static final String record_audio = Manifest.permission.RECORD_AUDIO;
  public static final String read_phone_state = Manifest.permission.READ_PHONE_STATE;
  public static final String call_phone = Manifest.permission.CALL_PHONE;
  public static final String read_call_log = Manifest.permission.READ_CALL_LOG;
  public static final String write_call_log = Manifest.permission.WRITE_CALL_LOG;
  public static final String add_voicemail = Manifest.permission.ADD_VOICEMAIL;
  public static final String use_sip = Manifest.permission.USE_SIP;
  public static final String process_outgoing_calls = Manifest.permission.PROCESS_OUTGOING_CALLS;
  public static final String body_sensors = Manifest.permission.BODY_SENSORS;
  public static final String send_sms = Manifest.permission.SEND_SMS;
  public static final String read_sms = Manifest.permission.READ_SMS;
  public static final String receive_sms = Manifest.permission.RECEIVE_SMS;
  public static final String receive_wap_push = Manifest.permission.RECEIVE_WAP_PUSH;
  public static final String read_external_storage = Manifest.permission.READ_EXTERNAL_STORAGE;
  public static final String write_external_storage = Manifest.permission.WRITE_EXTERNAL_STORAGE;

  //화면고정
  public static void fixedScreen(Activity activity, int option) {
    if (option == sero) {
      activity.setRequestedOrientation(sero);
    } else if (option == garo) {
      activity.setRequestedOrientation(garo);
    }
  }
  //화면고정

  //권한체크 (Manifest.xml 에 먼저 정의해둔것만 실행됨)
  public static void checkPermission(final Activity activity, final String... args) {
    for (int i = 0; i < args.length; i++) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        int permissionResult = activity.checkSelfPermission(args[i]);

        if (permissionResult == PackageManager.PERMISSION_DENIED) {
          if (activity.shouldShowRequestPermissionRationale(args[i])) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            final int finalI = i;
            dialog.setTitle("권한이 필요합니다.")
              .setMessage("이 기능을 사용하기 위해서는 권한이 필요합니다. 계속하시겠습니까?")
              .setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(new String[]{args[finalI]}, 1000);
                  }

                }
              })
              .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  Toast.makeText(activity, "기능을 취소했습니다.", Toast.LENGTH_SHORT).show();
                }
              })
              .create()
              .show();
          } else {
            activity.requestPermissions(new String[]{args[i]}, 1000);
          }
        }
      }
    }
  }
  //권한체크 (Manifest.xml 에 먼저 정의해둔것만 실행됨)

  public static void setAlarm(Context context, int year, int month, int date, int hour, int min) {
    Intent intent = new Intent(context, AlarmPref.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra("year", year);
    intent.putExtra("month", month);
    intent.putExtra("date", date);
    intent.putExtra("hour", hour);
    intent.putExtra("min", min);
    context.startActivity(intent);
  } //서비스에서 호출시 null

}
