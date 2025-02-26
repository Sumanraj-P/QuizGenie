package com.example.quizgenie;


public class RecommendedQuiz {
    private String title;
    private String difficulty;
    private String time;

    public RecommendedQuiz(String title, String difficulty, String time) {
        this.title = title;
        this.difficulty = difficulty;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getTime() {
        return time;
    }
}