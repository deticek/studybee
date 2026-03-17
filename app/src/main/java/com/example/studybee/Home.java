package com.example.studybee;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Home extends AppCompatActivity {

    private TextView v, q;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);

        v = findViewById(R.id.miniinfo);
        q = findViewById(R.id.qout);

        // Inicializiraj dbHelper preden ga uporabiš
        dbHelper = new DatabaseHelper(this);

        hoursofstudy();
        getquotes();

        checkfirst();

    }

    private void checkfirst() {
        int achId = 1;
        Cursor c = dbHelper.getAchivement(achId);

        if (c != null && c.moveToFirst()) {
            int unlockValue = c.getInt(c.getColumnIndexOrThrow("unlock"));
            String name = c.getString(c.getColumnIndexOrThrow("name"));

            if (unlockValue == 0) {
                dbHelper.unlockAchievement(achId);
                Notifications.pushNotification(this, "New trophy unlocked!",name, Home.class);
            }
        }

        if (c != null) c.close();
    }

    private void getquotes(){

        ArrayList<String> quotes = new ArrayList<>();

        quotes.add("A little progress each day adds up to big results");
        quotes.add("Dream big. Start small. Act now.");
        quotes.add("Discipline beats motivation.");
        quotes.add("Success is built on daily habits.");
        quotes.add("Push yourself, because no one else will.");
        quotes.add("Great things never come from comfort zones.");
        quotes.add("Small steps every day.");
        quotes.add("Focus on progress, not perfection.");
        quotes.add("Your only limit is your mind.");
        quotes.add("Stay hungry. Stay foolish.");
        quotes.add("Make today count.");
        quotes.add("Done is better than perfect.");
        quotes.add("Consistency creates results.");
        quotes.add("The grind pays off.");
        quotes.add("Action cures fear.");
        quotes.add("Don't wish for it. Work for it.");
        quotes.add("Results happen over time, not overnight.");
        quotes.add("Hard work beats talent.");
        quotes.add("Your future is created today.");
        quotes.add("Start where you are.");
        quotes.add("Build discipline daily.");
        quotes.add("Success loves preparation.");
        quotes.add("Progress over excuses.");
        quotes.add("Think less. Do more.");
        quotes.add("Momentum is everything.");
        quotes.add("Execution beats ideas.");
        quotes.add("Every day is a fresh start.");
        quotes.add("Keep moving forward.");
        quotes.add("Trust the process.");
        quotes.add("Growth happens outside comfort.");
        quotes.add("Stay focused.");
        quotes.add("You get what you work for.");
        quotes.add("Be stronger than your excuses.");
        quotes.add("Fall seven times. Stand up eight.");
        quotes.add("Do it now.");
        quotes.add("Hustle in silence.");
        quotes.add("Energy flows where focus goes.");
        quotes.add("Keep building.");
        quotes.add("Your work matters.");
        quotes.add("Effort compounds daily.");
        quotes.add("Winners execute.");
        quotes.add("Progress compounds.");
        quotes.add("The best time is now.");
        quotes.add("Keep the momentum.");
        quotes.add("Learn. Build. Repeat.");
        quotes.add("Make it happen.");
        quotes.add("Stay relentless.");
        quotes.add("Your limits are negotiable.");
        quotes.add("Pressure builds diamonds.");
        quotes.add("Focus wins.");
        quotes.add("Be obsessed with improvement.");
        quotes.add("The grind never lies.");
        quotes.add("Success requires sacrifice.");
        quotes.add("Move with purpose.");
        quotes.add("Keep leveling up.");
        quotes.add("Work until it works.");
        quotes.add("Turn ideas into reality.");
        quotes.add("Execution is king.");
        quotes.add("Stay consistent.");
        quotes.add("The work compounds.");
        quotes.add("Rise and grind.");
        quotes.add("Progress creates confidence.");
        quotes.add("Effort builds mastery.");
        quotes.add("Growth takes time.");
        quotes.add("Chase improvement.");
        quotes.add("Winners adapt.");
        quotes.add("Discomfort builds strength.");
        quotes.add("Focus creates results.");
        quotes.add("Think big. Act bigger.");
        quotes.add("Win the day.");
        quotes.add("Commit to the process.");
        quotes.add("Stay sharp.");
        quotes.add("Build your future.");
        quotes.add("Keep pushing.");
        quotes.add("Never settle.");
        quotes.add("Move the needle daily.");
        quotes.add("Outwork yesterday.");
        quotes.add("Stay disciplined.");
        quotes.add("Do hard things.");
        quotes.add("Momentum beats motivation.");
        quotes.add("Improve one percent daily.");
        quotes.add("Earn your results.");
        quotes.add("Stay dangerous.");
        quotes.add("Your effort defines you.");
        quotes.add("Create your momentum.");
        quotes.add("Daily effort wins.");
        quotes.add("Keep showing up.");
        quotes.add("Success is rented daily.");
        quotes.add("Make progress inevitable.");
        quotes.add("Push past limits.");
        quotes.add("Keep building skills.");
        quotes.add("Every rep counts.");
        quotes.add("Focus on the mission.");
        quotes.add("Work beats talk.");
        quotes.add("Create the opportunity.");
        quotes.add("Stay locked in.");
        quotes.add("Build unstoppable habits.");
        quotes.add("Consistency wins long term.");
        quotes.add("Make it undeniable.");
        quotes.add("Progress never lies.");
        quotes.add("Stay on the grind.");

        Random rand = new Random();
        int randomIndex = rand.nextInt(quotes.size());

        q.setText(quotes.get(randomIndex));
    }

    private void hoursofstudy() {
        try {
            // Preberi skupni čas učenja iz baze
            String totalTime = dbHelper.getTotalStudyTime();
            v.setText("Hours of study: " + totalTime);
        } catch (Exception e) {
            e.printStackTrace();
            v.setText("Hours of study: 00:00:00");
            Toast.makeText(this, "Napaka pri branju baze", Toast.LENGTH_SHORT).show();
        }
    }

    public void nastavitve(View view){
        Intent i = new Intent(this, Nastavitve.class);
        startActivity(i);
    }

    public void kuci(View view){
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }

    public void cas(View view){
        Intent i = new Intent(this, Timer.class);
        startActivity(i);
    }

    public void infotocka(View view){
        Intent i = new Intent(this, Info.class);
        startActivity(i);
    }
}