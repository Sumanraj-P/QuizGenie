package com.example.quizgenie;

public class UserPreferences {
    private String name;
    private String quizInterest;
    private String difficulty;
    private String language;

    public UserPreferences() {
        // Default constructor required for Firebase
    }

    public UserPreferences(String name, String quizInterest, String difficulty, String language) {
        this.name = name;
        this.quizInterest = quizInterest;
        this.difficulty = difficulty;
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public String getQuizInterest() {
        return quizInterest;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getLanguage() {
        return language;
    }
}