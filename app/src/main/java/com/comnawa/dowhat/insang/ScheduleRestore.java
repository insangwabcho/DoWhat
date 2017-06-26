package com.comnawa.dowhat.insang;

import android.content.Context;
import android.util.Log;

import com.comnawa.dowhat.sangjin.ScheduleDTO;
import com.comnawa.dowhat.sungwon.Common;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by insang on 2017. 6. 19..
 */

public class ScheduleRestore extends Thread {
  private Context context;

  public ScheduleRestore(Context context) {
    this.context = context;
  }

  @Override
  public void run() {
    getSchedule();
  }

  private void getSchedule() {

    PrefManager prefManager= new PrefManager(context);
    String id= prefManager.getUserInfo().get("id");

    ArrayList<ScheduleDTO> items = null;
    try {
      items = new ArrayList<ScheduleDTO>();
      String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/detail.do";
      HttpClient http = new DefaultHttpClient();
      ArrayList<NameValuePair> postData = new ArrayList<>();

      postData.add(new BasicNameValuePair("id", id));
      //한글, 특수문자 등이 잘 전달될 수 있도록 인코딩
      final UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData, "utf-8");
      //post 방식으로 데이터 전달
      HttpPost httpPost = new HttpPost(page);
      httpPost.setEntity(request);
      HttpResponse response = http.execute(httpPost);
      String body = EntityUtils.toString(response.getEntity());
      JSONObject jsonObj = new JSONObject(body);
      JSONArray jArray = (JSONArray) jsonObj.get("sendData");
      Log.i("test", jsonObj + "");
      Log.i("tes", jArray + "");
      for (int i = 0; i < jArray.length(); i++) {
        JSONObject row = jArray.getJSONObject(i);
        ScheduleDTO dto = new ScheduleDTO();
        dto.setTitle(row.getString("title"));
        dto.setEvent(row.getString("event"));
        dto.setStartdate(row.getString("startdate"));
        dto.setEnddate(row.getString("enddate"));
        dto.setStarttime(row.getString("starttime"));
        dto.setEndtime(row.getString("endtime"));
        dto.setPlace(row.getString("place"));
        dto.setMemo(row.getString("memo"));
        dto.setAlarm(row.getInt("alarm"));
        dto.setRepeat(row.getInt("repeat"));
        items.add(dto);
      }
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    DBManager dbManager= new DBManager(context);
    dbManager.requestAllScheduleForServer(items);
  }
}
