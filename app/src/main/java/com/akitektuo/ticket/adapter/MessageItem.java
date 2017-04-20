package com.akitektuo.ticket.adapter;

/**
 * Created by AoD Akitektuo on 20-Apr-17 at 18:23.
 */

public class MessageItem {

    private int type;
    private String text;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    public MessageItem(int type, String text, int day, int month, int year, int hour, int minute) {
        setType(type);
        setText(text);
        setDay(day);
        setMonth(month);
        setYear(year);
        setHour(hour);
        setMinute(minute);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
