package com.comnawa.dowhat.insang;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.comnawa.dowhat.R;

public class Preferences extends android.preference.PreferenceActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    Lib.fixedScreen(this, Lib.sero);
    super.onCreate(savedInstanceState);

    getFragmentManager().beginTransaction().replace(android.R.id.content, new MyFragment()).commit();


  } //환경설정 화면구현

  public static class MyFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      addPreferencesFromResource(R.xml.preference_insang);
    }
  } // 환경설정 화면구현//

}
