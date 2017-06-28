package com.comnawa.dowhat.sangjin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.insang.Preferences;

import java.util.ArrayList;

public class TagFriendActivity extends AppCompatActivity {
    ArrayList<String> items; //내친구 목록
    ArrayAdapter adapter; //아답터
    EditText editText; //검색바
    ListView listview1; //리스트뷰

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tag, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== R.id.action_settings) { //환경 설정
            startActivity(new Intent(TagFriendActivity.this, Preferences.class));
        } else if (item.getItemId()== R.id.menu_add){ //확인 클릭시 코드
            //내장 파일에 가지고있을 친구목록 추가코드

            //서버로 보낼 친구추가 코드

            //푸시메세지 코드
        } else if (item.getItemId()== R.id.addFriend){ //친구 추가
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

    }

}
