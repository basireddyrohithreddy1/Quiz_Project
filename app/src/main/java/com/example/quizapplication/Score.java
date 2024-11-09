package com.example.quizapplication;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Score {

    private String userId;
    private int score;

    @ServerTimestamp
    private Date timestamp;

    // No-argument constructor required for Firestore
    public Score() {}

    // Constructor to create a new score
    public Score(String userId, int score) {
        this.userId = userId;
        this.score = score;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
