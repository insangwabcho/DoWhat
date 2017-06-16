package com.comnawa.dowhat.sangjin;

public class ScheduleDTO {
    private int num;
    private String id;
    private String startdate;
    private String enddate;
    private String starttime;
    private String endtime;
    private String title;
    private String event;
    private String place;
    private String memo;
    private int alarm;
    private int repeat;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public ScheduleDTO(int num, String id, String startdate, String enddate, String starttime, String endtime,
                       String title, String event, String place, String memo, int alarm, int repeat) {
        super();
        this.num = num;
        this.id = id;
        this.startdate = startdate;
        this.enddate = enddate;
        this.starttime = starttime;
        this.endtime = endtime;
        this.title = title;
        this.event = event;
        this.place = place;
        this.memo = memo;
        this.alarm = alarm;
        this.repeat = repeat;
    }

    public ScheduleDTO() {
    }

    @Override
    public String toString() {
        return "ScheduleDTO [num=" + num + ", id=" + id + ", startdate=" + startdate + ", enddate=" + enddate
                + ", starttime=" + starttime + ", endtime=" + endtime + ", title=" + title + ", event=" + event
                + ", place=" + place + ", memo=" + memo + ", alarm=" + alarm + ", repeat=" + repeat + "]";
    }

}

