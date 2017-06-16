package com.comnawa.dowhat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

public class LoadActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
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
      dialog.show();
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
      Intent intent = new Intent(LoadActivity.this, MainActivity.class);
      startActivity(intent);
      finish();
    }
  }
}
