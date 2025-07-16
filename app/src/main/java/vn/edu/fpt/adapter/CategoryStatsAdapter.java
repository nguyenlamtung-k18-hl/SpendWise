package vn.edu.fpt.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import vn.edu.fpt.R;
import vn.edu.fpt.model.CategoryStats;

import java.util.ArrayList;
import java.util.List;

public class CategoryStatsAdapter extends RecyclerView.Adapter<CategoryStatsAdapter.CategoryStatsViewHolder> {
    
    private List<CategoryStats> categoryStats;
    
    public CategoryStatsAdapter() {
        this.categoryStats = new ArrayList<>();
    }
    
    @NonNull
    @Override
    public CategoryStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_stats, parent, false);
        return new CategoryStatsViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull CategoryStatsViewHolder holder, int position) {
        CategoryStats stats = categoryStats.get(position);
        holder.bind(stats);
    }
    
    @Override
    public int getItemCount() {
        return categoryStats.size();
    }
    
    public void updateCategoryStats(List<CategoryStats> newStats) {
        this.categoryStats.clear();
        if (newStats != null) {
            this.categoryStats.addAll(newStats);
        }
        notifyDataSetChanged();
    }
    
    public static class CategoryStatsViewHolder extends RecyclerView.ViewHolder {
        
        private View colorIndicator;
        private TextView tvCategoryName;
        private TextView tvTransactionCount;
        private TextView tvTotalAmount;
        
        public CategoryStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            
            colorIndicator = itemView.findViewById(R.id.view_color_indicator);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvTransactionCount = itemView.findViewById(R.id.tv_transaction_count);
            tvTotalAmount = itemView.findViewById(R.id.tv_total_amount);
        }
        
        public void bind(CategoryStats stats) {
            // Set category name
            tvCategoryName.setText(stats.getCategoryName() != null ? 
                stats.getCategoryName() : "Unknown");
            
            // Set transaction count
            tvTransactionCount.setText(stats.getTransactionCount() + " transactions");
            
            // Set total amount formatted as VND
            tvTotalAmount.setText(String.format("%,.0f VNĐ", stats.getTotalAmount()));
            
            // Set color indicator
            if (colorIndicator != null && stats.getCategoryColor() != null) {
                try {
                    int categoryColor = Color.parseColor(stats.getCategoryColor());
                    colorIndicator.setBackgroundColor(categoryColor);
                } catch (Exception e) {
                    // Default color if parsing fails
                    colorIndicator.setBackgroundColor(Color.parseColor("#2196F3"));
                }
            }
        }
    }
} 