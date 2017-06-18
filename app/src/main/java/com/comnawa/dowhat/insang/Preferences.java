package com.comnawa.dowhat.insang;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.comnawa.dowhat.R;

public class Preferences extends android.preference.PreferenceActivity {

  boolean serviceStatus;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DoWhat.fixedScreen(this, DoWhat.sero);

    super.onCreate(savedInstanceState);

    getFragmentManager().beginTransaction().replace(android.R.id.content, new MyFragment()).commit();
    serviceStatus = new PrefManager(this).getPushAlarm();
  } //환경설정 화면구현

  public static class MyFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preference_insang);
    }
  } // 환경설정 화면구현//

  @Override
  protected void onDestroy() {
    super.onDestroy();
    PrefManager pm = new PrefManager(this);
    if (pm.getPushAlarm() && !serviceStatus) {
      Intent intent = new Intent(this, AlarmService.class);
      startService(intent);
    } else if (serviceStatus && !pm.getPushAlarm()){
      Intent intent = new Intent(this, AlarmService.class);
      stopService(intent);
    }
  }
}
