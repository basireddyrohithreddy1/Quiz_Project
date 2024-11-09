package com.example.quizapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private TextView questionTextView, scoreTextView, timerTextView;
    private RadioGroup optionsGroup;
    private RadioButton option1, option2, option3, option4;
    private Button nextButton;
    private ProgressBar timerProgressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<DocumentSnapshot> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int correctAnswers = 0;
    private CountDownTimer timer;
    private final int timePerQuestion = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        timerTextView = findViewById(R.id.timerTextView);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        nextButton = findViewById(R.id.nextButton);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        String topic = getIntent().getStringExtra("topic");
        fetchQuestionsFromFirestore(topic);

        nextButton.setOnClickListener(v -> {
            checkAnswer();
            loadNextQuestion();
        });
    }

    private void fetchQuestionsFromFirestore(String topic) {
        db.collection("questions")
                .whereEqualTo("topic", topic)
                .limit(5)  // Fetch 5 random questions
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    questionList = queryDocumentSnapshots.getDocuments();
                    Collections.shuffle(questionList); // Randomize question order
                    loadNextQuestion();
                });
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            DocumentSnapshot questionDoc = questionList.get(currentQuestionIndex);
            String question = questionDoc.getString("question");
            List<String> options = (List<String>) questionDoc.get("options");
            String correctAnswer = questionDoc.getString("correctAnswer");

            questionTextView.setText(question);
            option1.setText(options.get(0));
            option2.setText(options.get(1));
            option3.setText(options.get(2));
            option4.setText(options.get(3));

            startTimer();
            currentQuestionIndex++;
        } else {
            endQuiz();
        }
    }

    private void startTimer() {
        timerProgressBar.setMax(timePerQuestion / 1000);
        timer = new CountDownTimer(timePerQuestion, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time: " + millisUntilFinished / 1000 + "s");
                timerProgressBar.setProgress((int) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                checkAnswer();  // Mark current question as unanswered
                loadNextQuestion();
            }
        }.start();
    }

    private void checkAnswer() {
        if (timer != null) timer.cancel();
        RadioButton selectedOption = findViewById(optionsGroup.getCheckedRadioButtonId());
        if (selectedOption != null) {
            String userAnswer = selectedOption.getText().toString();
            DocumentSnapshot questionDoc = questionList.get(currentQuestionIndex - 1);
            String correctAnswer = questionDoc.getString("correctAnswer");
            if (userAnswer.equals(correctAnswer)) {
                score += 20; // Each correct answer gives 20 points
                correctAnswers++;
            }
        }
        optionsGroup.clearCheck(); // Reset options selection
    }

    private void endQuiz() {
        Intent intent = new Intent(QuizActivity.this, ScoreSummaryActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questionList.size());
        intent.putExtra("correctAnswers", correctAnswers);
        saveScoreToHistory(score);
        startActivity(intent);
        finish();
    }

    private void saveScoreToHistory(int score) {
        db.collection("history").add(new ScoreHistory(score));
    }
}
