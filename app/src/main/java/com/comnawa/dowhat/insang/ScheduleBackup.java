package com.comnawa.dowhat.insang;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.comnawa.dowhat.sangjin.ScheduleDTO;
import com.comnawa.dowhat.sungwon.Common;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ScheduleBackup extends Thread {

  Context context;
  Handler handler;

  public ScheduleBackup(Context context, Handler handler) {
    this.context = context;
    this.handler= handler;
  }

  @Override
  public void run() {
    sendSchedule();
  }

  private void sendSchedule() {
    DBManager dbManager = new DBManager(context);
    ArrayList<ScheduleDTO> items = dbManager.getAllSchedule(new PrefManager(context).getUserInfo().get("id"));
    if (items==null || items.size()== 0){
      handler.sendEmptyMessage(1);
      return;
    }
    JSONArray itemsArr = new JSONArray();
    JSONObject jsonMain = new JSONObject();
    int aCount = 0;
    try {
      for (ScheduleDTO dto : items) {
        JSONObject jobj = new JSONObject();
        jobj.put("num", dto.getNum());
        jobj.put("id", dto.getId());
        jobj.put("startdate", dto.getStartdate());
        jobj.put("enddate", dto.getEnddate());
        jobj.put("starttime", dto.getStarttime());
        jobj.put("endtime", dto.getEndtime());
        jobj.put("title", dto.getTitle());
        jobj.put("event", dto.getEvent());
        jobj.put("place", dto.getPlace());
        jobj.put("memo", dto.getMemo());
        jobj.put("alarm", dto.getAlarm());
        jobj.put("repeat", dto.getRepeat());
        itemsArr.put(aCount, jobj);
        aCount++;
      }
      jsonMain.put("sendData", itemsArr);

      String serverurl = Common.SERVER_URL;
      HttpClient client = new DefaultHttpClient();
      HttpPost post = new HttpPost(serverurl + "/Dowhat/Schedule_servlet/backup.do");

      String jj = jsonMain.toString();
      Log.i("test",jj);
      ArrayList<NameValuePair> data= new ArrayList<>();
      data.add(new BasicNameValuePair("sendData",jj));
      UrlEncodedFormEntity ent= new UrlEncodedFormEntity(data,"utf-8");
      post.setEntity(ent);
      client.execute(post);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
