package com.example.quizgenie;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerCategories, recyclerRecommended;
    private CategoryAdapter categoryAdapter;
    private RecommendedAdapter recommendedAdapter;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TextView txtUserName, txtWelcomeMessage;

    // Views for different sections
    private LinearLayout homeSection, savedSection, statsSection;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        txtUserName = findViewById(R.id.txtUserName);
        txtWelcomeMessage = findViewById(R.id.txtWelcomeMessage);

        recyclerCategories = findViewById(R.id.recyclerCategories);
        recyclerRecommended = findViewById(R.id.recyclerRecommended);

        // Initialize sections
        homeSection = findViewById(R.id.homeSection);
        savedSection = findViewById(R.id.savedSection);
        statsSection = findViewById(R.id.statsSection);

        // Initialize bottom navigation
        bottomNav = findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                showHomeSection();
                return true;
            } else if (id == R.id.nav_saved) {
                showSavedSection();
                return true;
            } else if (id == R.id.nav_stats) {
                showStatsSection();
                return true;
            } else if (id == R.id.nav_profile) {
                showProfilePopup();
                return true;
            }

            return false;
        });


        setupQuizCategories();
        setupRecommendedQuizzes();

        MaterialButton btnGenerateQuiz = findViewById(R.id.btnGenerateQuiz);
        btnGenerateQuiz.setOnClickListener(v -> {
            Toast.makeText(this, "Generate Quiz Clicked", Toast.LENGTH_SHORT).show();
        });

        MaterialButton btnQuizFromPDF = findViewById(R.id.btnQuizFromPDF);
        btnQuizFromPDF.setOnClickListener(v -> {
            Toast.makeText(this, "Quiz From PDF Clicked", Toast.LENGTH_SHORT).show();
        });

        checkFirstTimeUser();
    }

    private void showHomeSection() {
        homeSection.setVisibility(View.VISIBLE);
        savedSection.setVisibility(View.GONE);
        statsSection.setVisibility(View.GONE);
    }

    private void showSavedSection() {
        homeSection.setVisibility(View.GONE);
        savedSection.setVisibility(View.VISIBLE);
        statsSection.setVisibility(View.GONE);
        // Load saved data here (e.g., from Firebase or local database)
    }

    private void showStatsSection() {
        homeSection.setVisibility(View.GONE);
        savedSection.setVisibility(View.GONE);
        statsSection.setVisibility(View.VISIBLE);
        // Load stats data here (e.g., from Firebase or local database)
    }

    private void showProfilePopup() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_profile);
        dialog.setCancelable(true);

        // Set dialog width to match parent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // Find views
        TextView txtProfileName = dialog.findViewById(R.id.txtProfileName);
        TextView txtProfileEmail = dialog.findViewById(R.id.txtProfileEmail);
        TextView txtProfileDifficulty = dialog.findViewById(R.id.txtProfileDifficulty);
        TextView txtProfileLanguage = dialog.findViewById(R.id.txtProfileLanguage);
        TextView txtProfileInterest = dialog.findViewById(R.id.txtProfileInterest);
        ImageView imgProfileClose = dialog.findViewById(R.id.imgProfileClose);

        // Fetch user data from Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String email = currentUser.getEmail();
                        String difficulty = snapshot.child("difficulty").getValue(String.class);
                        String language = snapshot.child("language").getValue(String.class);
                        String quizInterest = snapshot.child("quizInterest").getValue(String.class);

                        // Set data to TextViews
                        txtProfileName.setText("Name: " + name);
                        txtProfileEmail.setText("Email: " + email);
                        txtProfileDifficulty.setText("Difficulty: " + difficulty);
                        txtProfileLanguage.setText("Language: " + language);
                        txtProfileInterest.setText("Quiz Interest: " + quizInterest);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failed to fetch profile data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        imgProfileClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


    private void fetchUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.getValue(String.class);
                        txtUserName.setText(userName);
                        txtWelcomeMessage.setText("Hey " + userName + "! Ready for a new challenge?");
                    } else {
                        txtUserName.setText("User");
                        txtWelcomeMessage.setText("Hey! Ready for a new challenge?");
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    private void checkFirstTimeUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (!snapshot.hasChild("name")) {
                        showPreferencesDialog();
                    } else {
                        fetchUserData();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }

    private void showPreferencesDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_user_preferences);
        dialog.setCancelable(false);

        // Set dialog width to match parent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        EditText etName = dialog.findViewById(R.id.etName);
        Spinner spinnerQuizInterest = dialog.findViewById(R.id.spinnerQuizInterest);
        Spinner spinnerDifficulty = dialog.findViewById(R.id.spinnerDifficulty);
        Spinner spinnerLanguage = dialog.findViewById(R.id.spinnerLanguage);
        Button btnSavePreferences = dialog.findViewById(R.id.btnSavePreferences);

        ArrayAdapter<CharSequence> quizInterestAdapter = ArrayAdapter.createFromResource(this,
                R.array.quiz_interests, android.R.layout.simple_spinner_item);
        spinnerQuizInterest.setAdapter(quizInterestAdapter);

        ArrayAdapter<CharSequence> difficultyAdapter = ArrayAdapter.createFromResource(this,
                R.array.difficulty_levels, android.R.layout.simple_spinner_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);

        ArrayAdapter<CharSequence> languageAdapter = ArrayAdapter.createFromResource(this,
                R.array.languages, android.R.layout.simple_spinner_item);
        spinnerLanguage.setAdapter(languageAdapter);

        btnSavePreferences.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String quizInterest = spinnerQuizInterest.getSelectedItem().toString();
            String difficulty = spinnerDifficulty.getSelectedItem().toString();
            String language = spinnerLanguage.getSelectedItem().toString();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String userId = currentUser.getUid();
                UserPreferences userPreferences = new UserPreferences(name, quizInterest, difficulty, language);
                mDatabase.child("users").child(userId).setValue(userPreferences)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();
                                fetchUserData();
                                dialog.dismiss();
                            }
                        });
            }
        });

        dialog.show();
    }


    private void setupQuizCategories() {
        List<QuizCategory> categories = new ArrayList<>();
        categories.add(new QuizCategory("General Knowledge", R.drawable.ic_general_knowledge));
        categories.add(new QuizCategory("Science", R.drawable.ic_science));
        categories.add(new QuizCategory("Maths", R.drawable.ic_maths));
        categories.add(new QuizCategory("Java", R.drawable.ic_java));
        categories.add(new QuizCategory("Python", R.drawable.ic_python));
        categoryAdapter = new CategoryAdapter(categories);
        recyclerCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
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