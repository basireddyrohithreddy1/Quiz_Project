package com.example.quizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView cardNature = findViewById(R.id.cardNature);
        CardView cardScience = findViewById(R.id.cardScience);
        CardView cardComputerScience = findViewById(R.id.cardComputerScience);
        View buttonHistory = findViewById(R.id.buttonHistory);

        // Set click listeners for each card
        cardNature.setOnClickListener(v -> openQuiz("Nature"));
        cardScience.setOnClickListener(v -> openQuiz("Science"));
        cardComputerScience.setOnClickListener(v -> openQuiz("ComputerScience"));

        // Set click listener for the history button
        buttonHistory.setOnClickListener(v -> openHistory());
    }

    private void openQuiz(String topic) {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    private void openHistory() {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}
