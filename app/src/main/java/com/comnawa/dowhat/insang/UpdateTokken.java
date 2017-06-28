package com.comnawa.dowhat.insang;

import android.content.Context;
import android.util.Log;

import com.comnawa.dowhat.sangjin.ScheduleDTO;
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

    ArrayList<ScheduleDTO> items = null;
    try {
      items = new ArrayList<ScheduleDTO>();
      String page = Common.SERVER_URL + "/Dowhat/Schedule_servlet/updatepush.do";
      HttpClient http = new DefaultHttpClient();
      ArrayList<NameValuePair> postData = new ArrayList<>();

      postData.add(new BasicNameValuePair("id", id));
      postData.add(new BasicNameValuePair("pushtokken", tokken));
      Log.i("tokkenUpdate", id+","+tokken);
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