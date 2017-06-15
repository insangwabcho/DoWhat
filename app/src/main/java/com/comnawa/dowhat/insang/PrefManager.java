package com.comnawa.dowhat.insang;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {

  private boolean autoLogin;
  private SharedPreferences prefs;
  private SharedPreferences.Editor edit;
  private Context context;

  public PrefManager(Context context){
    this.context= context;

    startOrRefresh();
  }

  public boolean setAutoLogin(boolean auto) {
    edit.putBoolean("autoLogin",auto).apply();
    boolean result= edit.commit();
    startOrRefresh();
    return result;
  }

  public boolean getAutoLogin(){
    return autoLogin;
  }

  private void startOrRefresh(){
    prefs= PreferenceManager.getDefaultSharedPreferences(context);
    edit= prefs.edit();

    autoLogin= prefs.getBoolean("autoLogin",false);
  }
}
