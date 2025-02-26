package com.example.quizgenie;

public class QuizCategory {
    private String name; // Field for category name
    private int icon;    // Field for category icon (resource ID)

    // Constructor
    public QuizCategory(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for icon
    public int getIcon() {
        return icon;
    }
}