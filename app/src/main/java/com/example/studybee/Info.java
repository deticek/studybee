package com.example.studybee;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Info extends AppCompatActivity {

    DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    ArrayList<Session> sessionList;
    SessionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.info);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.historyRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sessionList = new ArrayList<>();

        loadSessions();

        adapter = new SessionAdapter(sessionList, dbHelper);
        recyclerView.setAdapter(adapter);
    }

    private void loadSessions(){

        Cursor cursor = dbHelper.getAllSessions();

        if(cursor.moveToFirst()){

            do{

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String start = cursor.getString(cursor.getColumnIndexOrThrow("start_time"));
                String end = cursor.getString(cursor.getColumnIndexOrThrow("end_time"));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow("duration"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));

                sessionList.add(new Session(id, date, start, end, duration));

            }while(cursor.moveToNext());
        }

        cursor.close();
    }

    // Navigacija (isti gumbi kot prej)
    public void nastavitve(android.view.View v){
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

    public void kuci(android.view.View v){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void cas(android.view.View v){
        Intent i = new Intent(this, Timer.class);
        startActivity(i);
    }

    public void infotocka(android.view.View v){
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }
}