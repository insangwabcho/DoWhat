package com.comnawa.dowhat.insang;

import android.content.Context;
import android.util.Log;

import com.comnawa.dowhat.sungwon.Common;
import com.comnawa.dowhat.sungwon.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by insang on 2017. 6. 19..
 */

public class GetFriend extends Thread {
  private Context context;
  private String id;
  private ArrayList<String> items;

  public GetFriend(Context context, String id, ArrayList<String> items) {
    this.context = context;
    this.id = id;
    this.items= items;
  }

  @Override
  public void run() {
    try {
      String page = Common.SERVER_URL + "/Dowhat/Member_servlet/findfriend.do";
      HashMap<String, String> map = new HashMap<String, String>();
      map.put("id", id);
      String body = JsonObject.objectType(page, map);
      JSONObject jsonObj = new JSONObject(body);
      Log.i("body", body);
      if (jsonObj.get("sendData").equals("fail")) {
        Log.i("getFriend", "실패");
      } else {
        String[] friendList = jsonObj.get("sendData").toString().split(",");
        for (int i = 0; i < friendList.length; i++) {
          items.add(friendList[i]);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}