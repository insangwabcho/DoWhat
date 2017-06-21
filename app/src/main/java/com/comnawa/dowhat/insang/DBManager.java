package com.comnawa.dowhat.insang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.comnawa.dowhat.sangjin.ScheduleDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DBManager extends SQLiteOpenHelper {

  public static String tableName = "Schedule";
  Cursor rs;

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

  public ArrayList<ScheduleDTO> selectTodaySchedule(Context context) {
    ArrayList<ScheduleDTO> items = new ArrayList<>();
    PrefManager pf = new PrefManager(context);
    HashMap<String, String> map = pf.getUserInfo();
    Calendar cal = Calendar.getInstance();
    String year = cal.get(Calendar.YEAR) + "";
    String month = (cal.get(Calendar.MONTH) + 1 < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + "";
    String date = (cal.get(Calendar.DATE) + 1 < 10) ? "0" + (cal.get(Calendar.DATE) + 1) : cal.get(Calendar.DATE) + "";
    String today = year + "-" + month + "-" + date;
    String sql = "select * from schedule where id='" + map.get("id") + "' and startdate='" + today + "'";
    SQLiteDatabase db = getWritableDatabase();
    rs = db.rawQuery(sql, null);
    while (!rs.isAfterLast()) {
      ScheduleDTO dto = new ScheduleDTO();
      dto.setNum(rs.getInt(0));
      dto.setId(rs.getString(1));
      dto.setStartdate(rs.getString(2));
      dto.setEnddate(rs.getString(3));
      dto.setStarttime(rs.getString(4));
      dto.setEndtime(rs.getString(5));
      dto.setTitle(rs.getString(6));
      dto.setEvent(rs.getString(7));
      dto.setPlace(rs.getString(8));
      dto.setMemo(rs.getString(9));
      dto.setAlarm(rs.getInt(10));
      dto.setRepeat(rs.getInt(11));
      items.add(dto);
    }
    return items;
  }

  public void insert() {
    
  }

  public void update() {

  }

  public void delete() {

  }
}
