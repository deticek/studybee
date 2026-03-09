package com.example.studybee;

public class Session {

    int id;
    String date;
    String start;
    String end;
    long duration;

    public Session(int id, String date, String start, String end, long duration){
        this.id = id;
        this.date = date;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

}