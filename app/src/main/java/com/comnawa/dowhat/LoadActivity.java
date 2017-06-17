package com.comnawa.dowhat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.comnawa.dowhat.insang.DoWhat;
import com.comnawa.dowhat.insang.PrefManager;

public class LoadActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DoWhat.fixedScreen(this, DoWhat.sero); //세로화면 고정
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.load_insang);
    super.onCreate(savedInstanceState);

    Loading loading = new Loading();
    loading.execute();

  }

  private class Loading extends AsyncTask<Void, Void, Void> {

    ProgressDialog dialog = new ProgressDialog(LoadActivity.this);

    @Override
    protected void onPreExecute() {
      dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      dialog.setMessage("로딩중입니다. 잠시만기다려주세요");
//      dialog.show();
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
      try {
        Thread.sleep(1500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      PrefManager pm= new PrefManager(LoadActivity.this);
      if (pm.getAutoLogin()){ //자동로그인 on일경우//
        Intent intent = new Intent(LoadActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(LoadActivity.this, "자동로그인on", Toast.LENGTH_SHORT).show();
      } else {
        /*
        * 요기요기해야하함 자동로그인 off일경우 로그인하는 창으로
        * 이동해야함니다 합니다 함니다 힙니다 힙섹 섹뚜
         */
        Intent intent = new Intent(LoadActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(LoadActivity.this, "자동로그인off", Toast.LENGTH_SHORT).show();
      }
      finish();
    }
  }
}
