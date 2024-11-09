package com.example.quizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_summary);

        TextView scoreSummaryText = findViewById(R.id.scoreSummaryText);
        Button buttonViewHistory = findViewById(R.id.buttonViewHistory);
        Button buttonRestartQuiz = findViewById(R.id.buttonRestartQuiz);

        int score = getIntent().getIntExtra("score", 0);
        scoreSummaryText.setText("Your Score: " + score + "%");

        buttonViewHistory.setOnClickListener(v -> startActivity(new Intent(ScoreSummaryActivity.this, HistoryActivity.class)));

        buttonRestartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreSummaryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
