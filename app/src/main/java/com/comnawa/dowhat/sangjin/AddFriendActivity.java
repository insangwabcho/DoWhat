package com.comnawa.dowhat.sangjin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.AddFriend;
import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.GetFriend;
import com.comnawa.dowhat.insang.PrefManager;
import com.comnawa.dowhat.insang.Preferences;
import com.comnawa.dowhat.sungwon.Common;
import com.comnawa.dowhat.sungwon.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AddFriendActivity extends AppCompatActivity {
    ArrayList<String> items; //아이템리스트
    ArrayAdapter adapter; //아답터
    EditText editText; //검색바
    ImageButton btnSearch; //검색버튼
    ListView listview1; //리스트뷰

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            adapter = new ArrayAdapter(AddFriendActivity.this, android.R.layout.simple_list_item_single_choice, items);
            listview1.setAdapter(adapter);
        }
    };


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_settings) {
            startActivity(new Intent(AddFriendActivity.this, Preferences.class));
        } else if (item.getItemId()== R.id.menu_add){ //추가 클릭시 코드
            int idx= listview1.getCheckedItemPosition();
            String choice= items.get(idx);
            String[] arr= choice.split("[(]");
            String name= arr[0];
            String id= arr[1].substring(0,arr[1].length()-1);
            String myId= new PrefManager(this).getUserInfo().get("id");
            if (id.equals(myId)){
                Toast.makeText(this, "자기 자신을 추가할 수 없습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }

            //서버로 보낼 친구추가 코드
            //addfriend.do

            ArrayList<String> friendList= new ArrayList<>();
            GetFriend gf= new GetFriend(this,myId,friendList);
            gf.start();
            try {
                gf.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String friendid= "";
            for (String t: friendList){
                friendid+= t;
                friendid+=",";
            }
            friendid+= choice;

            AddFriend af= new AddFriend(this, myId, friendid);
            af.start();
            try {
                af.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //푸시메세지 코드
            DoWhat.sendPushMsg(this,myId+"님께서 친구로 추가하셨습니다.",id);
        }
        Toast.makeText(this, "친구로 추가되었습니다.", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DoWhat.fixedScreen(this, DoWhat.sero); //화면 세로로 고정
        setContentView(R.layout.freindsearch);
        listview1 = (ListView) findViewById(R.id.listview1);
        editText = (EditText) findViewById(R.id.editText);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);

//        items.get(listview1.getSelectedItemPosition());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            items = new ArrayList<String>();
                            String page = Common.SERVER_URL + "/Dowhat/Member_servlet/idcheck.do";
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("id", editText.getText().toString());
                            String body= JsonObject.objectType(page, map);
                            JSONObject jsonObj = new JSONObject(body);
                            Log.i("body",body);
                            JSONArray jArray = (JSONArray) jsonObj.get("sendData");
                            JSONObject jsonMain = (JSONObject) jArray.get(0);
                            Log.i("jsonObj:",jsonObj.toString()+"");
                            if (jsonMain.get("id").equals("success")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddFriendActivity.this, "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                MemberDTO dto=new MemberDTO();
                                dto.setFriendId(jsonMain.getString("id"));
                                dto.setFriendName(jsonMain.getString("name"));
                                items.add(dto.getFriendName()+"("+dto.getFriendId()+")");
                                handler.sendEmptyMessage(0);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            }
        });
    }

}
