package com.comnawa.dowhat.sangjin;

import android.content.Context;

import com.comnawa.dowhat.sungwon.Common;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by insang on 2017. 6. 19..
 */

public class UpdateNewSchedule extends Thread {
  private Context context;
  private final String pathAdd = "add.do";
  private final String pathUpdate = "";
  private boolean select; //true일경우 add false일경우 update
  private ScheduleDTO dto;

  public UpdateNewSchedule(Context context, boolean select, ScheduleDTO dto) {
    this.context = context;
    this.select = select;
    this.dto = dto;
  }

  @Override
  public void run() {
    sendData();
  }

  private void sendData() {

    ArrayList<ScheduleDTO> items = null;
    try {
      items = new ArrayList<ScheduleDTO>();
      String page;
      if (select) {
        page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/" + pathAdd;
      } else {
        page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/" + pathUpdate;
      }
      HttpClient http = new DefaultHttpClient();
      ArrayList<NameValuePair> postData = new ArrayList<>();
      postData.add(new BasicNameValuePair("id", dto.getId()));
      postData.add(new BasicNameValuePair("event", dto.getEvent()));
      postData.add(new BasicNameValuePair("startdate", dto.getStartdate()));
      postData.add(new BasicNameValuePair("enddate", dto.getEnddate()));
      postData.add(new BasicNameValuePair("starttime", dto.getStarttime()));
      postData.add(new BasicNameValuePair("endtime", dto.getEndtime()));
      postData.add(new BasicNameValuePair("alarm", dto.getAlarm() + ""));
      postData.add(new BasicNameValuePair("repeat", dto.getRepeat() + ""));
      postData.add(new BasicNameValuePair("memo", dto.getMemo()));
      //한글, 특수문자 등이 잘 전달될 수 있도록 인코딩
      final UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData, "utf-8");
      //post 방식으로 데이터 전달
      HttpPost httpPost = new HttpPost(page);
      httpPost.setEntity(request);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
