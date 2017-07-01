package com.comnawa.dowhat.insang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comnawa.dowhat.R;
import com.comnawa.dowhat.sangjin.CalendarActivity;

import java.util.ArrayList;

public class AddToFriend extends AppCompatActivity {

  Button btnOk,btnNo;
  TextView txtResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addtofriend_insang);
    final String userid= getIntent().getStringExtra("userid");
    final String username= getIntent().getStringExtra("username");

//    btnOk= (Button) findViewById(R.id.btnOk);
//    btnNo= (Button) findViewById(R.id.btnNo);
    txtResult= (TextView) findViewById(R.id.txtResult);

    txtResult.setText(username+"님을 친구로 등록하시겠습니까?");

    btnOk.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addFriend(userid,username);
        Toast.makeText(AddToFriend.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
        finishAffinity();
        Intent intent= new Intent(AddToFriend.this, CalendarActivity.class);
        startActivity(intent);
      }
    });

    btnNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(AddToFriend.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
        finishAffinity();
        Intent intent= new Intent(AddToFriend.this, CalendarActivity.class);
        startActivity(intent);
      }
    });
  }

  private void addFriend(String userid, String username){
    String myId= new PrefManager(this).getUserInfo().get("id");
    String choice= username+"("+username+")";
    ArrayList<String> friendList = new ArrayList<>();
    GetFriend gf = new GetFriend(this, myId, friendList);
    Log.i("addf", "here2");
    gf.start();
    Log.i("addf", "here3");
    try {
      gf.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    String friendid = "";
    int count = 0;
    for (String t : friendList) {
      if (t.equals(choice)) {
        count++;
        break;
      }
      friendid += t;
      friendid += ",";
    }
    friendid += choice;
    if (count > 0) {
      Toast.makeText(this, "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show();
      return;
    }
    AddFriend af = new AddFriend(this, myId, friendid);
    Log.i("addf", "here4");
    af.start();
    Log.i("addf", "here5");
    try {
      af.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    //푸시메세지 코드
    DoWhat.sendPushMsg(this, username + " 님께서 친구로 추가하셨습니다.", userid, new PrefManager(this).getUserInfo().get("id"), username, null);
    Toast.makeText(this, "친구로 추가되었습니다.", Toast.LENGTH_SHORT).show();
  }

}
