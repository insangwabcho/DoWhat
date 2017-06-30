package com.comnawa.dowhat.insang;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.comnawa.dowhat.R;

public class AddToFriend extends AppCompatActivity {

  Button btnOk,btnNo;
  TextView txtResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.addtofriend_insang);
    String userid= getIntent().getStringExtra("userid");
    String username= getIntent().getStringExtra("username");

    btnOk= (Button) findViewById(R.id.btnOk);
    btnNo= (Button) findViewById(R.id.btnNo);
    txtResult= (TextView) findViewById(R.id.txtResult);

    txtResult.setText(username+"님을 친구로 등록하시겠습니까?");

    btnOk.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        addFriend();
        Toast.makeText(AddToFriend.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
        finishAffinity();
      }
    });

    btnNo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(AddToFriend.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
        finishAffinity();
      }
    });
  }

  private void addFriend(){

  }

}
