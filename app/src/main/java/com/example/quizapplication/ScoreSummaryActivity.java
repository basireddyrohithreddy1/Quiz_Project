package com.example.quizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_summary);

        TextView correctAnswersText = findViewById(R.id.correctAnswersText);
        TextView scorePercentageText = findViewById(R.id.scorePercentageText);
        Button buttonViewHistory = findViewById(R.id.buttonViewHistory);
        Button buttonRestartQuiz = findViewById(R.id.buttonRestartQuiz);

        int correctAnswers = getIntent().getIntExtra("score", 0);

        // Display the number of correct answers
        correctAnswersText.setText("Correct Answers: " + correctAnswers + "/5");

        // Display the score percentage
        int scorePercentage = 20 * correctAnswers;
        scorePercentageText.setText("Your Score: " + scorePercentage + "%");

        buttonViewHistory.setOnClickListener(v -> startActivity(new Intent(ScoreSummaryActivity.this, HistoryActivity.class)));

        buttonRestartQuiz.setOnClickListener(v -> {
            Intent intent = new Intent(ScoreSummaryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
