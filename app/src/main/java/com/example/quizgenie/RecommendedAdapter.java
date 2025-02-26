package com.example.quizgenie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizgenie.R;

import java.util.List;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder> {

    private List<RecommendedQuiz> recommended;

    public RecommendedAdapter(List<RecommendedQuiz> recommended) {
        this.recommended = recommended;
    }

    @NonNull
    @Override
    public RecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_quiz, parent, false);
        return new RecommendedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedViewHolder holder, int position) {
        RecommendedQuiz quiz = recommended.get(position);
        holder.quizTitle.setText(quiz.getTitle());
        holder.difficultyLevel.setText(quiz.getDifficulty());
        holder.estimatedTime.setText(quiz.getTime());
    }

    @Override
    public int getItemCount() {
        return recommended.size();
    }

    public static class RecommendedViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle, difficultyLevel, estimatedTime;

        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.txtQuizTitle);
            difficultyLevel = itemView.findViewById(R.id.txtDifficultyLevel);
            estimatedTime = itemView.findViewById(R.id.txtEstimatedTime);
        }
    }
}