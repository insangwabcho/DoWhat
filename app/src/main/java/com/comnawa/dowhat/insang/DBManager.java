package com.comnawa.dowhat.insang;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

  public static String tableName = "Schedule";

  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql =
      "create table schedule(" +
        "id varchar(50)," +
        "startdate varchar(50)," +
        "enddate varchar(50)," +
        "starttime varchar(50)," +
        "endtime varchar(50)," +
        "title varchar(50)," +
        "event varchar(50)," +
        "place varchar(50)," +
        "memo varchar(50)," +
        "alarm integer," +
        "repeat integer";
    db.execSQL(sql);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  public void insert(){

  }

  public void update(){

  }

  public void delete(){

  }
}
