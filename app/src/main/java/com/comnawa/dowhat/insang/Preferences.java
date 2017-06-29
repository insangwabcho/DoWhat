package com.comnawa.dowhat.insang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.comnawa.dowhat.MainActivity;
import com.comnawa.dowhat.R;
import com.comnawa.dowhat.sungwon.LoginActivity;

import java.util.HashMap;

public class Preferences extends android.preference.PreferenceActivity {

  boolean serviceStatus;
  Intent serviceIntent;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DoWhat.fixedScreen(this, DoWhat.sero);
    super.onCreate(savedInstanceState);

    serviceIntent = new Intent(this, AlarmService.class);

    getFragmentManager().beginTransaction().replace(android.R.id.content, new MyFragment(this, serviceIntent)).commit();
    serviceStatus = new PrefManager(this).getPushAlarm();
  } //환경설정 화면구현

  public static class MyFragment extends PreferenceFragment {

    Activity ac;
    Intent serviceIntent;
    String restoreOrBackup;

    public MyFragment(Activity content, Intent serviceIntent) {
      ac = content;
      this.serviceIntent = serviceIntent;
    }

    SwitchPreference autoLogin, pushService;
    Preference logId, logName, logoutKakao, backup, restore;
    Preference developers;

    //네트워크상태
    boolean connWifi;
    boolean connMobile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preference_insang);

      autoLogin = (SwitchPreference) findPreference("autoLogin");
      pushService = (SwitchPreference) findPreference("pushService");
      logId = (Preference) findPreference("logId");
      logoutKakao = (Preference) findPreference("logoutKakao");
      logName = (Preference) findPreference("logName");
      backup = (Preference) findPreference("backup");
      restore = (Preference) findPreference("restore");
      developers= (Preference) findPreference("developers");

      developers.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          Intent intent= new Intent(ac, MainActivity.class);
          startActivity(intent);
          return false;
        }
      });

      //네트워크 상태체크
      ConnectivityManager manager = (ConnectivityManager) ac.getSystemService(Context.CONNECTIVITY_SERVICE);
      final NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
      final NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
      connWifi = wifi.isConnected();
      connMobile = mobile.isConnected();
      //백업버튼
      backup.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          if (!connWifi && !connMobile) {
            Toast.makeText(ac, "인터넷에 연결되어있지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
          }else{
            Network nw = new Network();
            nw.execute();
          }
          return false;
        }
      });

      //복원버튼
      restore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          if (!connWifi && !connMobile) {
            Toast.makeText(ac, "인터넷에 연결되어있지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
          }
          restoreOrBackup = "복원";
          Network nw = new Network();
          nw.execute();
          return false;
        }
      });

      final PrefManager pm = new PrefManager(ac);

      //계정정보구역
      HashMap<String, String> userinfo = pm.getUserInfo();
      logId.setSummary(userinfo.get("id"));
      logName.setSummary(userinfo.get("name"));

      //카카오톡 로그아웃
      logoutKakao.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          LoginActivity.kakaoLogout();
          pm.setAutoLogin(null, null, null, null, null, false);
          Toast.makeText(ac, "계정 로그아웃 완료", Toast.LENGTH_SHORT).show();
          Intent intent = ac.getPackageManager().getLaunchIntentForPackage("com.comnawa.dowhat");
          startActivity(intent);
          ac.finishAndRemoveTask();
          return false;
        }
      });

      //푸시설정 리스너
      pushService.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
          if ((boolean) newValue) {
            ac.startService(serviceIntent);
          } else {
            ac.stopService(serviceIntent);
          }
          return true;
        }
      }); //푸시설정 리스너

    }

    class Network extends AsyncTask<Void, Void, Void> {

      ProgressDialog dialog = new ProgressDialog(ac);

      @Override
      protected void onPreExecute() {
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("일정 " + restoreOrBackup + "중입니다 잠시만 기다려주세요");
        dialog.show();
        super.onPreExecute();
      }

      @Override
      protected Void doInBackground(Void... params) {
        if (restoreOrBackup.equals("복원")) {
          ScheduleRestore gs = new ScheduleRestore(ac);
          gs.start();
          boolean current = true;
          try {
            gs.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
            current = false;
          }
          if (current) {
            Log.i("ac", "성공!");
          } else {
            Log.i("ac", "실패");
          }
        } else if (restoreOrBackup.equals("백업")) {
          ScheduleBackup b = new ScheduleBackup(ac);
          b.start();
          boolean current = true;
          try {
            b.join();
          } catch (InterruptedException e) {
            e.printStackTrace();
            current = false;
          }
          if (current) {
            Log.i("ac", "성공");
          } else {
            Log.i("ac", "실패");
          }
        }
        return null;
      }

      @Override
      protected void onPostExecute(Void aVoid) {
        dialog.dismiss();
        Toast.makeText(ac, "완료", Toast.LENGTH_SHORT).show();
        super.onPostExecute(aVoid);
      }
    }
  } // 환경설정 화면구현//

  @Override
  protected void onDestroy() {
    super.onDestroy();
    PrefManager pm = new PrefManager(this);
    if (pm.getPushAlarm() && !serviceStatus) {
      Intent intent = new Intent(this, AlarmService.class);
      startService(intent);
    } else if (serviceStatus && !pm.getPushAlarm()) {
      Intent intent = new Intent(this, AlarmService.class);
      stopService(intent);
    }
  }
}
