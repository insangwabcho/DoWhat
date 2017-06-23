package com.comnawa.dowhat.insang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.comnawa.dowhat.R;

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

    public MyFragment(Activity content, Intent serviceIntent) {
      ac = content;
      this.serviceIntent = serviceIntent;
    }

    SwitchPreference autoSync, autoLogin, pushService;
    Preference logId, logName, logKakao, logoutKakao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preference_insang);

      autoSync = (SwitchPreference) findPreference("autoSync");
      autoLogin = (SwitchPreference) findPreference("autoLogin");
      pushService = (SwitchPreference) findPreference("pushService");
      logId = (Preference) findPreference("logId");
      logKakao = (Preference) findPreference("logKakao");
      logoutKakao = (Preference) findPreference("logoutKakao");
      logName = (Preference) findPreference("logName");

      //계정정보구역
      PrefManager pm = new PrefManager(ac);
      HashMap<String, String> userinfo = pm.getUserInfo();
      logId.setSummary(userinfo.get("id"));
      logKakao.setSummary(userinfo.get("token"));
      logName.setSummary(userinfo.get("name"));

      logoutKakao.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
          Toast.makeText(ac, "기능구현중", Toast.LENGTH_SHORT).show();
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

      //자동동기화 리스너
      autoSync.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

          return true;
        }
      }); //자동동기화 리스너

    }
  } // 환경설정 화면구현//

  public void onClick(Preference p) {
    Log.i("insang", p.toString());
  }

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
