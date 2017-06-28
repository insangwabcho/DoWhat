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

public class GetFriend extends Thread {
  private Context context;
  private String id;

  public GetFriend(Context context, String id) {
    this.context = context;
    this.id= id;
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
      String body= JsonObject.objectType(page,map);
      JSONObject jobj= new JSONObject(body);
      //result == 친구목록
      String result= (String)jobj.get("sendData");
      if (result == "fail"){
        Log.i("test","실패");
      } else {
        Log.i("test","성공");
      }
    } catch (Exception e){

    }
  }
}