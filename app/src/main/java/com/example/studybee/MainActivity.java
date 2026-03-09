package com.example.studybee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    TextView v;
    Button b;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        v = findViewById(R.id.asdf);
        b = findViewById(R.id.button);

        dbHelper = new DatabaseHelper(this);

        // Preberi username iz baze
        String username = dbHelper.getUsername();
        if (username == null || username.isEmpty()) {
            // Če username še ni nastavljen, pojdi na GetName
            Intent intent = new Intent(this, GetName.class);
            startActivity(intent);
            finish();
        } else {
            v.setText("Welcome " + username);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void domov(View view){
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}