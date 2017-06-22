package com.comnawa.dowhat.insang;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.comnawa.dowhat.sangjin.ScheduleDTO;

import java.util.ArrayList;
import java.util.Calendar;

public class DBManager extends SQLiteOpenHelper {

  public static String tableName = "Schedule";
  Cursor rs;

  @Override
  public void onCreate(SQLiteDatabase db) {
    String sql =
      "create table schedule(" +
        "num integer," +
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
    db.execSQL(sql);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

  }

  public DBManager(Context context, String name, @Nullable SQLiteDatabase.CursorFactory factory, @Nullable int version) {
    super(context, name, null, 1);
  }

  public ArrayList<ScheduleDTO> selectTodaySchedule() {
    ArrayList<ScheduleDTO> items = new ArrayList<>();
    Calendar cal = Calendar.getInstance();
    String year = cal.get(Calendar.YEAR) + "";
    String month = (cal.get(Calendar.MONTH) + 1 < 10) ? "0" + (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + "";
    String date = (cal.get(Calendar.DATE) + 1 < 10) ? "0" + (cal.get(Calendar.DATE) + 1) : cal.get(Calendar.DATE) + "";
    String today = year + "-" + month + "-" + date;
    String sql = "select * from schedule where id='" + "아이디처리 해야함" + "' and startdate='" + today + "'";
    SQLiteDatabase db = getReadableDatabase();
    rs = db.rawQuery(sql, null);
    while (!rs.isAfterLast()) {
      ScheduleDTO dto = new ScheduleDTO();
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

  public ArrayList<ScheduleDTO> selectAllSchedule() {
    ArrayList<ScheduleDTO> items = new ArrayList<>();
    String sql = "select * from schedule";
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
      String sql = "insert into schedule values('" + dto.getId() + "','" + dto.getStartdate() + "','" +
        dto.getEnddate() + "','" + dto.getStarttime() + "','" + dto.getEndtime() + "','" +
        dto.getTitle() + "','" + dto.getEvent() + "','" +
        dto.getPlace() + "','" + dto.getMemo() + "'," + dto.getAlarm() + "," + dto.getRepeat() + ")";
      db.execSQL(sql);
    }
  }

  //일정 수정
  public void update(ScheduleDTO dto) {
    SQLiteDatabase db = getWritableDatabase();
    String sql = "update schedule set id='" + dto.getId() + "', startdate='" + dto.getStartdate() + "', enddate='" +
      dto.getEnddate() + "', starttime='" + dto.getStarttime() + "', endtime='" + dto.getEndtime() + "', title='" +
      dto.getTitle() + "', event='" + dto.getEvent() + "', place='" +
      dto.getPlace() + "',memo='" + dto.getMemo() + "', alarm=" + dto.getAlarm() + ", repeat=" + dto.getRepeat() +
      " where num=" + dto.getNum();
  }

  //일정 삭제
  public void delete(ScheduleDTO dto) {
    SQLiteDatabase db = getWritableDatabase();
    String sql = "delete from schedule where=" + dto.getNum();
    db.execSQL(sql);
  }

  public void deleteAllSchedule() {
    SQLiteDatabase db = getWritableDatabase();
    String sql = "delete from schedule";
    db.execSQL(sql);
  }
}
