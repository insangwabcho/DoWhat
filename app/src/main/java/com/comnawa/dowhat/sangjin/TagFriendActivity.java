package com.comnawa.dowhat.sangjin;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.sungwon.Common;
import com.comnawa.dowhat.sungwon.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class TagFriendActivity extends AppCompatActivity implements Filterable{
    ArrayList<String> items; //내친구 목록
    ArrayAdapter adapter; //아답터
    EditText editText; //검색바
    ListView listview1; //리스트뷰
    PrefManager manager;
    String id;

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        return super.onCreateOptionsMenu(menu);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new ArrayAdapter(TagFriendActivity.this, android.R.layout.simple_list_item_multiple_choice, items);
            listview1.setAdapter(adapter);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) { //환경 설정
            startActivity(new Intent(TagFriendActivity.this, Preferences.class));
        } else if (item.getItemId() == R.id.menu_add) { //확인 클릭시 코드
            //액티비티를 종료시키고 선택한 친구를 DetailActivity의 editText에 set함

        } else if (item.getItemId() == R.id.addFriend) { //친구 추가
            startActivity(new Intent(TagFriendActivity.this, AddFriendActivity.class));
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freindtag);
        listview1 = (ListView) findViewById(R.id.listview1);
        editText = (EditText) findViewById(R.id.editText);
        manager = new PrefManager(this);
        id = manager.getUserInfo().get("id");

        //서버에서 친구목록을 받아옴
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    items = new ArrayList<String>();
                    String page = Common.SERVER_URL + "/Dowhat/Member_servlet/findfriend.do";
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", id);
                    String body = JsonObject.objectType(page, map);
                    JSONObject jsonObj = new JSONObject(body);
                    Log.i("body", body);
                    if(jsonObj.get("sendData").equals("fail")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TagFriendActivity.this, "등록된 친구가 없습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else{
                        String[] friendList=jsonObj.get("sendData").toString().split(",");
                        for(int i=0; i<friendList.length; i++){
                            items.add(friendList[i]);
                        }
                        handler.sendEmptyMessage(0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

        //이름으로 검색
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String keyword=editText.getText().toString();
                if(keyword.length() > 0){
                    listview1.setFilterText(keyword);
                }else{
                    listview1.clearTextFilter();
                }
            }
        });

    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
