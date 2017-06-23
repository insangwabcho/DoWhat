package com.comnawa.dowhat.insang;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
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

  public boolean setAutoLogin(String userid, String userpasswd, String namee, String friendid, String kakaoToken, boolean current) {
    Log.i("test", "1");
    if (userid.equals("") || userid == null) {
      userid = "not Login";
    }
    if (userpasswd.equals("") || userpasswd == null) {
      userpasswd = "not Login";
    }
    if (namee.equals("") || namee == null) {
      namee = "not Login";
    }
    if (friendid.equals("") || friendid == null) {
      friendid = "not Login";
    }
    if (kakaoToken.equals("") || kakaoToken == null) {
      kakaoToken = "not Login";
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
      bw.write("id:" + userid);
      bw.newLine();
      bw.write("passwd:" + userpasswd);
      bw.newLine();
      bw.write("name:" + namee);
      bw.newLine();
      bw.write("friendid:" + friendid);
      bw.newLine();
      bw.write("kakaotoken:" + kakaoToken);
      bw.flush();
      bw.close();
      edit.putBoolean("autoLogin", current).apply();
      edit.commit();
      Log.i("test", "2");
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

  public HashMap<String, String> getUserInfo() {
    HashMap<String, String> result = new HashMap<>();
    try {
      File f= new File("/data/data/"+context.getPackageName()+"/files/log.prop");
      Properties prop = new Properties();
      prop.load(new InputStreamReader(new FileInputStream(f)));
      String id = prop.getProperty("id")== null || prop.getProperty("id").equals("") ?
        "not Login" : prop.getProperty("id");
      String pwd = prop.getProperty("passwd")== null || prop.getProperty("id").equals("") ?
        "not Login" : prop.getProperty("passwd");
      String name = prop.getProperty("name")== null || prop.getProperty("id").equals("") ?
        "not Login" : prop.getProperty("name");
      String friendid = prop.getProperty("friend")== null || prop.getProperty("id").equals("") ?
        "not Login" : prop.getProperty("friend");
      String token = prop.getProperty("kakaotoken")== null || prop.getProperty("id").equals("") ?
        "not Login" : prop.getProperty("kakaotoken");

      Log.i("zzo",id+pwd+name+friendid);

      result.put("id", id);
      result.put("passwd", pwd);
      result.put("name", name);
      result.put("friendid", friendid);
      result.put("kakaotoken", token);

    } catch (Exception e) {
      e.printStackTrace();
      Log.i("ZZo","error");
    }
    if (result.size() == 0 || result == null) {
      result.put("id", "not Login");
      result.put("password", "not Login");
      result.put("name", "not Login");
      result.put("friendid", "not Login");
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

  public void testAlarm(String content) {
    Log.i("test", content);
    edit.putString("testAlarm", prefs.getString("testAlarm", null) + content + ",").commit();
    resetScheduleCount();
  }

}
