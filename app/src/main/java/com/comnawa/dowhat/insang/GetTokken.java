package com.comnawa.dowhat.insang;

import android.content.Context;
import android.util.Log;

import com.comnawa.dowhat.sungwon.Common;
import com.comnawa.dowhat.sungwon.JsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by insang on 2017. 6. 19..
 */

public class GetTokken extends Thread {
  private Context context;
  private String id;
  private Map<String,String> tokk;

  public GetTokken(Context context, String id, Map<String,String> tokk) {
    this.context = context;
    this.id= id;
    this.tokk= tokk;
  }

  @Override
  public void run() {
    getSchedule();
  }

  private void getSchedule() {

    PrefManager prefManager= new PrefManager(context);
    String id= prefManager.getUserInfo().get("id");

    try {
      String page = Common.SERVER_URL + "/Dowhat/Member_servlet/selecttoken.do";
      HashMap<String,String> map= new HashMap<>();
      map.put("id",id);
      String body= JsonObject.objectType(page,map);
      JSONObject jobj= new JSONObject(body);
      String result= (String)jobj.get("sendData");
      if (result == "fail"){
        Log.i("test","실패");
        tokk.put("tokken","fail");
      } else {
        Log.i("sdlkfjkwlejfklw",result);
        tokk.put("tokken",result);
      }
    } catch (Exception e){

    }

  }
}