package com.example.quizgenie;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizgenie.CategoryAdapter;
import com.example.quizgenie.QuizCategory;
import com.example.quizgenie.R;
import com.example.quizgenie.RecommendedAdapter;
import com.example.quizgenie.RecommendedQuiz;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerCategories, recyclerRecommended;
    private CategoryAdapter categoryAdapter;
    private RecommendedAdapter recommendedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerViews
        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerRecommended = findViewById(R.id.recyclerRecommended);

        // Set up Quiz Categories
        setupQuizCategories();

        // Set up Recommended Quizzes
        setupRecommendedQuizzes();
        // Handle Generate Quiz Button Click
        MaterialButton btnGenerateQuiz = findViewById(R.id.btnGenerateQuiz);
        btnGenerateQuiz.setOnClickListener(v -> {
            Toast.makeText(this, "Generate Quiz Clicked", Toast.LENGTH_SHORT).show();
            // Add your logic here
        });

        // Handle Quiz From PDF Button Click
        MaterialButton btnQuizFromPDF = findViewById(R.id.btnQuizFromPDF);
        btnQuizFromPDF.setOnClickListener(v -> {
            Toast.makeText(this, "Quiz From PDF Clicked", Toast.LENGTH_SHORT).show();
            // Add your logic here
        });
    }

    private void setupQuizCategories() {
        List<QuizCategory> categories = new ArrayList<>();
        categories.add(new QuizCategory("General Knowledge", R.drawable.ic_general_knowledge));
        categories.add(new QuizCategory("Science", R.drawable.ic_science));
        categories.add(new QuizCategory("Maths", R.drawable.ic_maths));
        categories.add(new QuizCategory("Java", R.drawable.ic_java));
        categories.add(new QuizCategory("Python", R.drawable.ic_python));

        categoryAdapter = new CategoryAdapter(categories);
        recyclerCategories.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false));
        recyclerCategories.setAdapter(categoryAdapter);
    }

    private void setupRecommendedQuizzes() {
        List<RecommendedQuiz> recommended = new ArrayList<>();
        recommended.add(new RecommendedQuiz("Java Quiz: OOP Concepts", "Medium", "15 min"));
        recommended.add(new RecommendedQuiz("Python Quiz: Data Structures", "Easy", "10 min"));
        recommended.add(new RecommendedQuiz("MERN Stack Quiz: Full Stack Development", "Hard", "20 min"));

        recommendedAdapter = new RecommendedAdapter(recommended);
        recyclerRecommended.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerRecommended.setAdapter(recommendedAdapter);
    }
}