package com.comnawa.dowhat.insang;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Properties;

public class PrefManager {

  private boolean autoLogin;
  private boolean pushAlarm;
  private boolean autoUpdate;
  private float textSize;

  private SharedPreferences prefs;
  private SharedPreferences.Editor edit;
  private Context context;

  public PrefManager(Context context) {
    this.context = context;
    startOrRefresh();
  }

  public boolean getPushAlarm() {
    return pushAlarm;
  }

  public boolean setPushAlarm(boolean onOrOff) {
    boolean result = edit.putBoolean("pushService", onOrOff).commit();
    startOrRefresh();
    return result;
  }

  public float getTextSize() {
    return textSize;
  } // 어플리케이션에 설정되어있는 tetSize 반환

  public boolean setAutoLogin(String userid, String userpasswd, String name) {
    if (!getAutoLogin()) {
      return false;
    }
    String id = userid;
    String passwd = userpasswd;
    if (id.equals("") || id == null) {
      id = "not Login";
    }
    if (passwd.equals("") || passwd == null) {
      passwd = "not Login";
    }

    boolean result = false;
    String path = "/data/data/" + context.getPackageName() + "/files";
    File f = null;
    f = new File(path);
    if (!f.exists()) {
      f.mkdir();
    }
    f = null;
    path = "/data/data/" + context.getPackageName() + "/files/log.prop";
    f = new File(path);
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(f));
      bw.write("id:" + id);
      bw.newLine();
      bw.write("passwd:" + passwd);
      bw.flush();
      bw.close();
      result = edit.putBoolean("autoLogin", true).commit();
    } catch (Exception e) {
      e.printStackTrace();
      result = false;
    }
    startOrRefresh();
    return result;
  } //자동로그인 설정 (성공시 ture반환, 실패시 false 반환함)

  public int getScheduleCount() {
    int result = Integer.parseInt(prefs.getString("aCountSchedule", "0"));
    return result;
  }

  public boolean setScheduleCount(int aCount) {
    boolean result = edit.putString("aCountSchedule", aCount + "").commit();
    startOrRefresh();
    return result;
  }

  public boolean resetScheduleCount() {
    boolean result = edit.putString("aCountSchedule", "0").commit();
    startOrRefresh();
    return result;
  }

  public boolean getAutoLogin() {
    return autoLogin;
  } //자동로그인 상태값 (설정되어있을시 true, 아닐시 false)

  public String getUserID() {
    String result = "";
    try {
      Properties prop = new Properties();
      prop.load(new InputStreamReader(new FileInputStream(
        new File("/data/data/" + context.getPackageName() + "/files/log.prop"))));
      result = prop.getProperty("id");
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (result.equals("") || result == null) {
      result = "null";
    }
    return result;
  } //자동로그인 on일시 유저아이디 리턴

  private void startOrRefresh() {
    prefs = PreferenceManager.getDefaultSharedPreferences(context);
    edit = prefs.edit();

    autoLogin = prefs.getBoolean("autoLogin", false);
    textSize = Float.parseFloat(prefs.getString("fontSize", "20"));
    pushAlarm = prefs.getBoolean("pushService", false);
    autoUpdate = prefs.getBoolean("autoUpdate", false);

  } //인상이꺼

}
