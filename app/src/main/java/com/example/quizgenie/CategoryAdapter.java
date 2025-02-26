package com.example.quizgenie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Update the import to use QuizCategory
import com.example.quizgenie.QuizCategory;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<QuizCategory> categories;

    // Update the constructor to use QuizCategory
    public CategoryAdapter(List<QuizCategory> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Get the QuizCategory object at the current position
        QuizCategory quizCategory = categories.get(position);

        // Bind data to the views
        holder.categoryName.setText(quizCategory.getName());       // Set category name
        holder.categoryIcon.setImageResource(quizCategory.getIcon()); // Set category icon
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // ViewHolder class
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.imgCategoryIcon);
            categoryName = itemView.findViewById(R.id.txtCategoryName);
        }
    }
}