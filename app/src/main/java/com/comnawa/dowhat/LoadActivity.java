package com.comnawa.dowhat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class LoadActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setTheme(android.R.style.Theme_NoTitleBar_Fullscreen);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.load_insang);
    super.onCreate(savedInstanceState);
    ImageView img= (ImageView)findViewById(R.id.imageView2);

    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Toast.makeText(this, "환영합니다! 오늘뭐할까?!", Toast.LENGTH_SHORT).show();

    Intent intent= new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}
