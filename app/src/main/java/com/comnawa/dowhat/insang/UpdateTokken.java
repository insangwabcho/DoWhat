package com.comnawa.dowhat.insang;

import android.content.Context;
import android.util.Log;

import com.comnawa.dowhat.sungwon.Common;
import com.comnawa.dowhat.sungwon.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by insang on 2017. 6. 19..
 */

public class UpdateTokken extends Thread {
  private Context context;
  private String id;
  private String tokken;

  public UpdateTokken(Context context, String id, String tokken) {
    this.context = context;
    this.id= id;
    this.tokken= tokken;
  }

  @Override
  public void run() {
    getSchedule();
  }

  private void getSchedule() {

    PrefManager prefManager= new PrefManager(context);
    String id= prefManager.getUserInfo().get("id");

    try {
      String page = Common.SERVER_URL + "/Dowhat/Member_servlet/updatepush.do";
      HashMap<String,String> map= new HashMap<>();
      map.put("id",id);
      map.put("pushtoken",tokken);
      String body= JsonObject.objectType(page,map);
      JSONObject jobj= new JSONObject(body);
      int result= (int)jobj.get("sendData");
      if (result == 1){
        Log.i("test","성공");
      } else {
        Log.i("test","실패");
      }
    } catch (Exception e){
    }

  }
}