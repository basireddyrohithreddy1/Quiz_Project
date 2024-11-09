package com.example.quizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView questionText, scoreTracker;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private List<DocumentSnapshot> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int totalQuestions = 5;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionText = findViewById(R.id.questionText);
        scoreTracker = findViewById(R.id.scoreTracker);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);

        loadQuestions();

        nextButton.setOnClickListener(v -> {
            checkAnswer();
            currentQuestionIndex++;
            if (currentQuestionIndex < totalQuestions) {
                displayQuestion();
            } else {
                saveScore();
                Intent intent = new Intent(QuizActivity.this, ScoreSummaryActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
                finish();
            }
        });
    }

    private void loadQuestions() {
        db.collection("questions")
                .whereEqualTo("topic", getIntent().getStringExtra("topic"))
                .limit(totalQuestions)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    questionList = queryDocumentSnapshots.getDocuments();
                    displayQuestion();
                });
    }

    private void displayQuestion() {
        if (timer != null) {
            timer.cancel();
        }

        DocumentSnapshot questionDoc = questionList.get(currentQuestionIndex);
        questionText.setText(questionDoc.getString("question"));

        List<String> options = (List<String>) questionDoc.get("options");
        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));
        option4.setText(options.get(3));

        optionsGroup.clearCheck();

        timer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                scoreTracker.setText("Time left: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                currentQuestionIndex++;
                if (currentQuestionIndex < totalQuestions) {
                    displayQuestion();
                } else {
                    saveScore();
                    Intent intent = new Intent(QuizActivity.this, ScoreSummaryActivity.class);
                    intent.putExtra("score", score);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timer.start();
    }

    private void checkAnswer() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedOption = findViewById(selectedId);
            DocumentSnapshot questionDoc = questionList.get(currentQuestionIndex);
            String correctAnswer = questionDoc.getString("correctAnswer");

            if (selectedOption.getText().equals(correctAnswer)) {
                score++;
            }
        }
    }

    private void saveScore() {
        String userId = "Guest";  // You can replace this with actual user ID if you have user registration

        // Create a new Score object
        Score scoreObj = new Score(userId, score);

        // Reference to the "scores" collection in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Add the score to Firestore
        db.collection("scores")  // "scores" is the collection name
                .add(scoreObj)  // Add the score object
                .addOnSuccessListener(documentReference -> {
                    // Successfully added score
                    System.out.println("Score saved with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Failed to add score
                    System.err.println("Error saving score: " + e.getMessage());
                });
    }

}
