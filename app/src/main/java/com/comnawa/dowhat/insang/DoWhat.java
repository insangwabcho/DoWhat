package com.comnawa.dowhat.insang;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.comnawa.dowhat.sangjin.ScheduleDTO;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

  public static boolean setAlarm(Context context, String date, int hour, int min, String subject) {
    String[] arr = date.split("-");
    return setAlarm(
      context, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), hour, min, subject
    );
  }

  public static boolean setAlarm
    (final Context context, int year, int month, int date, int hour, int min, String subject) {
    PrefManager pm = new PrefManager(context);
    int requestCode = pm.getScheduleCount() + 1;
    pm.setScheduleCount(requestCode);
    boolean current = false;
    if (!pm.getPushAlarm()) {
      AlertDialog.Builder dialog = new AlertDialog.Builder(context);
      dialog.setTitle("알람설정").setMessage("알람을 설정하시기 위해서는 \n 푸시알람설정이 필요합니다.\n  환경설정으로 이동하시겠습니까?")
        .setPositiveButton("이동", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Intent intent = new Intent(context, Preferences.class);
            intent.putExtra("msg", "hello");
            Toast.makeText(context, "푸시설정 -> 알람설정 을 켜주세요", Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
          }
        })
        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            Toast.makeText(context, "현재 푸시알람기능이 켜져있지 않습니다", Toast.LENGTH_LONG).show();
          }
        })
        .create().show();
    }

    pm = new PrefManager(context);
    if (pm.getPushAlarm()) {

      AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      Intent intent = new Intent(context, AlarmBroadcast.class);
      intent.putExtra("subject", subject);
      intent.putExtra("requestCode", requestCode);

      PendingIntent sender = PendingIntent.getBroadcast(context, requestCode, intent, 0);
//      PendingIntent sender = PendingIntent.getBroadcast(context, 1, intent, 0);

      Calendar calendar = Calendar.getInstance();
      calendar.set(year, month, date, hour, min, 0);

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
      } else {
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
      }


      current = true;
    }
    return current;
  } //서비스에서 호출시 null

  public static void getSchedule(Context context) {
    ScheduleRestore sc = new ScheduleRestore(context);
    sc.start();
    try {
      sc.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Log.i("test", "완료");
  } //스케쥴 일정 서버에서 받아오기

  public static void delAlarm(final Activity context) {
    resettAlarm(context, null);
    if (!new PrefManager(context).getPushAlarm()) {
      context.stopService(new Intent(context, AlarmService.class));
    }
  }

  public static void resetAlarm(final Activity context, @Nullable final Intent finIntent, final boolean check) {
    PrefManager pm = new PrefManager(context);
    String cbAlarm = "";
    if (finIntent.getStringExtra("cbAlarm") != null) {
      cbAlarm = finIntent.getStringExtra("cbAlarm");

      if (!pm.getPushAlarm() && cbAlarm.equals("설정")) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("알람설정").setMessage("알람을 설정하시기 위해서는 \n 푸시알람설정을 켜주셔야 합니다. \n 알람설정을 켜시겠습니까?")
          .setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              resettAlarm(context, finIntent);

              if (finIntent == null) {
                return;
              }
              if (finIntent.getBooleanExtra("newMod", false)) {
                Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show();
              } else if (!finIntent.getBooleanExtra("newMod", false)) {
                Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show();
              }
              return;
            }
          })
          .setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              Toast.makeText(context, "저장 되었습니다.", Toast.LENGTH_SHORT).show();
              context.startActivity(finIntent);
              context.finish();
              return;
            }
          })
          .create().show();
      } else {
        if (finIntent.getStringExtra("cbAlarm").equals("설정")) {
          resettAlarm(context, finIntent);
          if (!check) {
            Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show();
          }
        } else {
          boolean t = new PrefManager(context).getPushAlarm();
          resettAlarm(context, finIntent);
          if (!t) {
            new PrefManager(context).setPushAlarm(false);
          }
          if (!t) {
            context.stopService(new Intent(context, AlarmService.class));
          }
          if (!check) {
            Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show();
          }
        }
      }
    }
  }

  private static void resettAlarm(final Activity context, @Nullable final Intent finIntent) {
    final PrefManager pm = new PrefManager(context);
    for (int i = 1; i <= pm.getScheduleCount(); i++) {
      AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
      Intent intent = new Intent(context, AlarmBroadcast.class);
      PendingIntent sender = PendingIntent.getBroadcast(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);

      if (sender != null) {
        am.cancel(sender);
        sender.cancel();
        sender.cancel();
      }
    }
    pm.setScheduleCount(0);

    context.stopService(new Intent(context, AlarmService.class));
    context.startService(new Intent(context, AlarmService.class));

    if (finIntent != null) {
      context.startActivity(finIntent);
      context.finish();
    }
    pm.setPushAlarm(true);
  }


  public static void setTitleBar(AppCompatActivity activity, String title) {
    activity.getSupportActionBar().setTitle(title);
  }


  public void sendPushMsg(Context context, final String message, final String userid, @Nullable final String myId, @Nullable final String myname, @Nullable final ScheduleDTO dto) {
    final String svKey = "AAAARqvabTs:APA91bF_Ldp3AyUWQUo9-uNbcb70MGmYFHB1yuOT6eV4v-K5sTbs6-Vs8jD9ZK9Eln3XEmfs4yjbzulW5dcL9tY9lTbu9nTfF_FFF8FXGPjn-WfM4dlud43qrClW1xKpf4_MSfiEEv9P";

    Map<String, String> tokk = new HashMap<>();
    GetTokken th = new GetTokken(context, userid, tokk);
    th.start();
    try {
      th.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (tokk.get("tokken").equals("fail")) {
      return;
    }

    final String tok = tokk.get("tokken");

    /*

    private int num;
    private String id;
    private String startdate;
    private String enddate;
    private String starttime;
    private String endtime;
    private String title;
    private String event;
    private String place;
    private String memo;
    private String tag;
    private int alarm;
    private int repeat;

     */

    Log.i("nnnnn","1");
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          // FMC 메시지 생성 start
          JSONObject root = new JSONObject();
          JSONObject notification = new JSONObject();
          Log.i("msgg",message);
          notification.put("body", message);
          notification.put("tag", myId + "," + myname);
          if (dto == null) {
            notification.put("title", "DoWhat");
          } else {
            JSONObject jobj = new JSONObject();
            Log.i("msgg",dto.toString());
            try {
              Log.i("nnnnn","2");
              jobj.put("num", dto.getNum());
              jobj.put("id", dto.getId());
              jobj.put("startdate", dto.getStartdate());
              jobj.put("enddate", dto.getEnddate());
              jobj.put("starttime", dto.getStarttime());
              jobj.put("endtime", dto.getEndtime());
              jobj.put("title", dto.getTitle());
              jobj.put("event", dto.getEvent());
              jobj.put("place", dto.getPlace());
              jobj.put("memo", dto.getMemo());
              jobj.put("tag", dto.getTag());
              jobj.put("alarm", dto.getAlarm());
              jobj.put("repeat", dto.getRepeat());
              jobj.put("tagid",dto.getTagId());
            } catch (Exception e) {
              e.printStackTrace();
            }
            notification.put("title", jobj.toString());
            Log.i("nnnnn",jobj.toString());
          }
          Log.i("nnnnn","3");
//          root.put("notification",
// notification);
          root.put("data", notification);
          root.put("to", tok);
          // FMC 메시지 생성 end
          Log.i("nnnnn","4");
          URL Url = new URL("https://fcm.googleapis.com/fcm/send");
          HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          conn.setDoInput(true);
          conn.addRequestProperty("Authorization", "key=" + svKey);
          conn.setRequestProperty("Accept", "application/json");
          conn.setRequestProperty("Content-type", "application/json");
          OutputStream os = conn.getOutputStream();
          os.write(root.toString().getBytes("utf-8"));
          os.flush();
          conn.getResponseCode();
          Log.i("nnnnn","5");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }).start();
    Log.i("nnnnn","6");
  }


}

/*


--환경설정 메뉴 액션바 등록
@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_pref, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()== R.id.action_settings) {
      startActivity(new Intent(MainActivity.this, Preferences.class));
    }
    return false;
  }



--상진시꺼 저장 메뉴 액션바 등록
@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_savebutton, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()== R.id.action_settings) {
      startActivity(new Intent(MainActivity.this, Preferences.class));
    } else if (item.getItemId()== R.id.menu_select){
      --저장버튼 클릭시(상진씨 기능)


    }
    return false;
  }



 */
