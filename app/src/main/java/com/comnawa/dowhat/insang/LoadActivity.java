package com.comnawa.dowhat.insang;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.comnawa.dowhat.MainActivity;
import com.comnawa.dowhat.R;

public class LoadActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {//
    DoWhat.fixedScreen(this, DoWhat.sero); //세로화면 고정
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    setContentView(R.layout.load_insang);
    super.onCreate(savedInstanceState);

    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    boolean isNetWorkConnect = false;
    if (mobile.isConnected() || wifi.isConnected()) {
      isNetWorkConnect = true;
    }
    Loading loading = new Loading();
    loading.execute(isNetWorkConnect);

  }

  private class Loading extends AsyncTask<Boolean, Void, Void> {

    ProgressDialog dialog = new ProgressDialog(LoadActivity.this);

    @Override
    protected void onPreExecute() {
      dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      dialog.setMessage("업데이트 확인중입니다. 잠시만기다려주세요");
//      dialog.show();
      super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Boolean... params) {

      if (params[0] == false) {
        try {
          Toast.makeText(LoadActivity.this, "인터넷에 연결되어있지 않습니다.", Toast.LENGTH_SHORT).show();
          Thread.sleep(1500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } else {
        //서버와 달력 동기화 작업
        //새로운 클래스 만들어서 동기화 작업만

        //여기는 임시작업
        try {
          Toast.makeText(LoadActivity.this, "인터넷에 연결되어있습니다.", Toast.LENGTH_SHORT).show();
          Thread.sleep(1500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      PrefManager pm = new PrefManager(LoadActivity.this);
      if (pm.getAutoLogin()) { //자동로그인 on일경우//
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
