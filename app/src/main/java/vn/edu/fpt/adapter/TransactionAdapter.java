package vn.edu.fpt.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import vn.edu.fpt.R;
import vn.edu.fpt.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    
    private List<Transaction> transactions;
    private SimpleDateFormat dateFormat;
    
    public TransactionAdapter() {
        this.transactions = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }
    
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.bind(transaction);
    }
    
    @Override
    public int getItemCount() {
        return transactions.size();
    }
    
    public void updateTransactions(List<Transaction> newTransactions) {
        this.transactions.clear();
        if (newTransactions != null) {
            this.transactions.addAll(newTransactions);
        }
        notifyDataSetChanged();
    }
    
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        
        private MaterialCardView categoryIconBackground;
        private ImageView ivCategoryIcon;
        private TextView tvTransactionTitle;
        private TextView tvTransactionCategory;
        private TextView tvTransactionDate;
        private TextView tvTransactionAmount;
        
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            
            categoryIconBackground = itemView.findViewById(R.id.iv_category_icon).getParent() instanceof MaterialCardView ? 
                (MaterialCardView) itemView.findViewById(R.id.iv_category_icon).getParent() : null;
            ivCategoryIcon = itemView.findViewById(R.id.iv_category_icon);
            tvTransactionTitle = itemView.findViewById(R.id.tv_transaction_title);
            tvTransactionCategory = itemView.findViewById(R.id.tv_transaction_category);
            tvTransactionDate = itemView.findViewById(R.id.tv_transaction_date);
            tvTransactionAmount = itemView.findViewById(R.id.tv_transaction_amount);
        }
        
        public void bind(Transaction transaction) {
            // Set description as title
            tvTransactionTitle.setText(transaction.getDescription());
            
            // Set category name
            tvTransactionCategory.setText(transaction.getCategoryName() != null ? 
                transaction.getCategoryName() : "Unknown");
            
            // Set formatted date
            Date date = new Date(transaction.getDate());
            tvTransactionDate.setText(dateFormat.format(date));
            
            // Set amount with sign and currency
            String amountText;
            int amountColor;
            
            if (transaction.isExpense()) {
                amountText = "-" + transaction.getFormattedAmount();
                amountColor = Color.parseColor("#F44336"); // Red for expense
            } else {
                amountText = "+" + transaction.getFormattedAmount();
                amountColor = Color.parseColor("#4CAF50"); // Green for income
            }
            
            tvTransactionAmount.setText(amountText);
            tvTransactionAmount.setTextColor(amountColor);
            
            // Set category icon background color
            if (categoryIconBackground != null && transaction.getCategoryColor() != null) {
                try {
                    int categoryColor = Color.parseColor(transaction.getCategoryColor());
                    categoryIconBackground.setCardBackgroundColor(categoryColor);
                } catch (Exception e) {
                    // Default color if parsing fails
                    categoryIconBackground.setCardBackgroundColor(Color.parseColor("#2196F3"));
                }
            }
            
            // Set appropriate icon based on transaction type
            if (transaction.isExpense()) {
                ivCategoryIcon.setImageResource(R.drawable.ic_list); // Use existing icon for now
            } else {
                ivCategoryIcon.setImageResource(R.drawable.ic_add); // Use existing icon for now
            }
        }
    }
} 