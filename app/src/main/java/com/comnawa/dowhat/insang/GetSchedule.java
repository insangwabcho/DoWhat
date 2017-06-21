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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by insang on 2017. 6. 19..
 */

public class GetSchedule extends Thread {
  private Context context;

  public GetSchedule(Context context) {
    this.context = context;
  }

  @Override
  public void run() {
    getSchedule();
  }

  private void getSchedule() {

    HashMap<String, String> result = new HashMap<>();
    Properties prop = new Properties();
    try {
      prop.load(new InputStreamReader(new FileInputStream(
        new File("/data/data/" + context.getPackageName() + "/files/log.prop"))));
    } catch (IOException e) {
      e.printStackTrace();
    }
    String id = prop.getProperty("id");
    String pwd = prop.getProperty("pwd");
    String name = prop.getProperty("name");
    String friendid = prop.getProperty("friendid");

    result.put("id", id);
    result.put("passwd", pwd);
    result.put("name", name);
    result.put("friendid", friendid);

    ArrayList<ScheduleDTO> items = null;
    try {
      items = new ArrayList<ScheduleDTO>();
      String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/alarm.do";
      HttpClient http = new DefaultHttpClient();
      ArrayList<NameValuePair> postData = new ArrayList<>();
      Calendar cal = Calendar.getInstance();
      String startdate = "";
      String year = cal.get(Calendar.YEAR) + "";
      String month = (cal.get(Calendar.MONTH) + 1 < 10) ?
        "0" + (cal.get(Calendar.MONTH) + 1) : (cal.get(Calendar.MONTH) + 1) + "";
      String date = (cal.get(Calendar.DATE) + 1 < 10) ?
        "0" + (cal.get(Calendar.DATE) + 1) : (cal.get(Calendar.DATE) + 1) + "";

      startdate = year + "-" + month + "-" + date;

      postData.add(new BasicNameValuePair("id", id));
      postData.add(new BasicNameValuePair("startdate", startdate)); //아이디와 날짜를 넘김
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
//        dto.setStartdate(row.getString("startdate"));
//        dto.setEnddate(row.getString("enddate"));
        dto.setStarttime(row.getString("starttime"));
//        dto.setEndtime(row.getString("endtime"));
//        dto.setPlace(row.getString("place"));
        dto.setMemo(row.getString("memo"));
//        dto.setAlarm(row.getInt("alarm"));
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
    AlarmService.setlists(items);
  }
}
