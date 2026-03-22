/*
 * Author: [Aleks Detiček]
 * Project: StudyBee
 * Year: 2026
 *
 * This source code is proprietary and confidential.
 * Unauthorized copying, distribution or use is strictly prohibited.
 */
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