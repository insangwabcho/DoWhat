package com.comnawa.dowhat.insang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.comnawa.dowhat.sangjin.ScheduleDTO;

import java.util.ArrayList;
import java.util.Calendar;


public class DBManager {

  private final String dbName = "schedule.db";
  private final int nowVersion = 1;
  private DBM dbm;

  public DBManager(Context context) {

    dbm = new DBM(context, dbName, null, nowVersion);
  }

  public ArrayList<ScheduleDTO> getSchedule(String id, int year, int month, int date) {
    return dbm.selectSchedule(id, year, month, date);
  }

  public void requestAllScheduleForServer(ArrayList<ScheduleDTO> dto) {
    Log.i("test","Hello");
    dbm.deleteAllSchedule();
    dbm.insertAllSchedules(dto);
  }

  public void tableDrop(){
    dbm.tableDrop();
  }

  public ArrayList<ScheduleDTO> getAllSchedule(String id){
    return dbm.selectAllSchedule(id);
  }

  public ArrayList<ScheduleDTO> todaySchedule(String id) {
    return dbm.selectTodaySchedule(id);
  }

  public void updateSchedule(ScheduleDTO dto) {
    dbm.update(dto);
  }

  public void deleteSchedule(ScheduleDTO dto) {
    dbm.delete(dto);
  }

  public void insertSchedule(ScheduleDTO dto) {
    dbm.insert(dto);
  }

  class DBM extends SQLiteOpenHelper {

    Cursor rs;

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBM(Context context, String name, @Nullable SQLiteDatabase.CursorFactory factory, @Nullable int version) {
      super(context, name, null, 1);
      String sql =
        "create table schedule(" +
          "num integer primary key," +
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
          "repeat integer)";
//    String sql="drop table schedule";
      SQLiteDatabase db= getWritableDatabase();
      try {
        db.execSQL(sql);
      }catch(SQLiteException e){
        e.printStackTrace();
        Log.i("db","이미 테이블이 있ㄱ음");
      }
    }

    public void tableDrop(){
      SQLiteDatabase db= getWritableDatabase();
      String sql="drop table schedule";
      db.execSQL(sql);
    }

    public ArrayList<ScheduleDTO> selectTodaySchedule(String id) {
      ArrayList<ScheduleDTO> items = new ArrayList<>();
      Calendar cal = Calendar.getInstance();
      String year = cal.get(Calendar.YEAR) + "";
      String month = (cal.get(Calendar.MONTH) + 1 < 10) ?
        "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + "";
      String date = (cal.get(Calendar.DATE) + 1 < 10) ?
        "0" + (cal.get(Calendar.DATE) + 1) : cal.get(Calendar.DATE) + "";
      String today = year + "-" + month + "-" + date;
      String sql = "select * from schedule where id='" + id + "' and startdate='" + today + "'";
      SQLiteDatabase db = getReadableDatabase();
      rs = db.rawQuery(sql, null);
      while (rs.moveToNext()) {
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

    public ArrayList<ScheduleDTO> selectSchedule(String id, int year, int month, int date) {
      ArrayList<ScheduleDTO> items = new ArrayList<>();
      String monthly= month<10 ? "0"+month : month+"";
      String dately= date<10 ? "0"+date : date+"";
      String today = year + "-" + monthly + "-" + dately;
      Log.i("test",id+today);
      String sql = "select * from schedule where id='" + id + "' and startdate='" + today + "'";
      Log.i("query",sql);
      SQLiteDatabase db = getReadableDatabase();
      rs = db.rawQuery(sql, null);
      while (rs.moveToNext()) {
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
        Log.i("dto",dto.toString());
        items.add(dto);
      }
      return items;
    }

    public ArrayList<ScheduleDTO> selectAllSchedule(String id) {
      ArrayList<ScheduleDTO> items = new ArrayList<>();
      String sql = "select * from schedule where id='"+id+"'";
      SQLiteDatabase db = getReadableDatabase();
      rs = db.rawQuery(sql, null);
      while (rs.moveToNext()) {
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

    //동기화 처리
    public void insertAllSchedules(ArrayList<ScheduleDTO> items) {
      SQLiteDatabase db = getWritableDatabase();
      for (ScheduleDTO dto : items) {
        String sql = "insert into schedule values("+dto.getNum()+",'" + dto.getId() + "','" + dto.getStartdate() + "','" +
          dto.getEnddate() + "','" + dto.getStarttime() + "','" + dto.getEndtime() + "','" +
          dto.getTitle() + "','" + dto.getEvent() + "','" +
          dto.getPlace() + "','" + dto.getMemo() + "'," + dto.getAlarm() + "," + dto.getRepeat() + ")";
        Log.i("insert",dto.toString());
        db.execSQL(sql);
      }
    }

    public void insert(ScheduleDTO dto) {
      SQLiteDatabase db = getWritableDatabase();
      String sql1 = "insert into schedule (id, startdate, enddate, starttime, endtime," +
        " title, event, place, memo, alarm, repeat) ";
      String sql2 = "values('" + dto.getId() + "','" + dto.getStartdate() + "','" +
        dto.getEnddate() + "','" + dto.getStarttime() + "','" + dto.getEndtime() + "','" +
        dto.getTitle() + "','" + dto.getEvent() + "','" +
        dto.getPlace() + "','" + dto.getMemo() + "'," + dto.getAlarm() + "," + dto.getRepeat() + ")";
      String sql = sql1 + sql2;
      Log.i("insert",sql);
      db.execSQL(sql);
    }

    //일정 수정
    public void update(ScheduleDTO dto) {
      SQLiteDatabase db = getWritableDatabase();
      String sql = "update schedule set id='" + dto.getId() + "', startdate='" + dto.getStartdate() + "', enddate='" +
        dto.getEnddate() + "', starttime='" + dto.getStarttime() + "', endtime='" + dto.getEndtime() + "', title='" +
        dto.getTitle() + "', event='" + dto.getEvent() + "', place='" +
        dto.getPlace() + "',memo='" + dto.getMemo() + "', alarm=" + dto.getAlarm() + ", repeat=" + dto.getRepeat() +
        " where num=" + dto.getNum();
      Log.i("dto",dto.toString());
      db.execSQL(sql);
    }

    //일정 삭제
    public void delete(ScheduleDTO dto) {
      SQLiteDatabase db = getWritableDatabase();
      String sql = "delete from schedule where num=" + dto.getNum() + " and" + dto.getId();
      db.execSQL(sql);
    }

    public void deleteAllSchedule() {
      SQLiteDatabase db = getWritableDatabase();
      String sql = "delete from schedule";
      db.execSQL(sql);
    }

    public void test() {
      SQLiteDatabase db = getWritableDatabase();
      String sql = "drop table schedule";
      db.execSQL(sql);
    }
  }

}