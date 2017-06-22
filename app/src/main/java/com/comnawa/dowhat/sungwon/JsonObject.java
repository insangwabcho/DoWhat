package com.comnawa.dowhat.sungwon;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sungwon on 2017-06-22.
 */

public class JsonObject {
    public static String objectType(String page,HashMap<String,String> map){
        String body="";
        try {
            HttpClient http = new DefaultHttpClient();
            ArrayList<NameValuePair> postData = new ArrayList<NameValuePair>();
            Iterator<String> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String)iterator.next();
                postData.add(new BasicNameValuePair(key, map.get(key)));
            }
            Log.i("asdas",postData+""+page);
            UrlEncodedFormEntity request = new UrlEncodedFormEntity(postData,"utf-8");
            HttpPost httpPost = new HttpPost(page);
            httpPost.setEntity(request);
            HttpResponse response = http.execute(httpPost);
            body = EntityUtils.toString(response.getEntity());
        }catch (Exception e){
        e.printStackTrace();
        }
        return  body;
    }
}
