package com.comnawa.dowhat.insang;

import android.app.Activity;
import android.content.pm.ActivityInfo;

public class DoWhatLibrary {

  public static final int sero= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
  public static final int garo= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

  public static void fixedScreen(Activity activity, int option){
    if (option== sero){
      activity.setRequestedOrientation(sero);
    } else if (option== garo){
      activity.setRequestedOrientation(garo);
    }
  } //화면고정 라이브러리


}
