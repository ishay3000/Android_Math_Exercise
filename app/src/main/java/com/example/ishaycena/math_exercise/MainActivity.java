package com.example.ishaycena.math_exercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnStartGame, btnBestScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartGame = findViewById(R.id.btnStartGame);
        btnBestScore = findViewById(R.id.btnBestScore);

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Transfer the user into the Game Activity using "Intent"
                startActivity(new Intent(MainActivity.this, Game_Activity.class));

            }
        });

        btnBestScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Transfer the user into a "Custom Dialog" Activity, and transfer the best score in shared preference.
            }
        });

/*
        startActivity(new Intent(MainActivity.this, Game_Activity.class));
*/
    }
}
