package com.example.studybee;

public class Dosezek {

    public int id;
    public String name;
    public String description;
    public String date;
    public boolean unlocked;
    public boolean hidden;

    public Dosezek(int id, String name, String description, boolean unlocked, String date, boolean hidden) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unlocked = unlocked;
        this.date = date;
        this.hidden = hidden;
    }

}
