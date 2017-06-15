package com.comnawa.dowhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.comnawa.dowhat.insang.Preferences;

public class MainActivity extends AppCompatActivity {

  Button btnInsang;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    //인상
    btnInsang = (Button) findViewById(R.id.btnInsang);
    //인상
  }

  public void test(View v) {
    Intent intent = null;
    switch (v.getId()) {
      //인상
      case R.id.btnInsang:
        intent = new Intent(this, Preferences.class);
        break;
      //인상
    }
    startActivity(intent);
  }
}
