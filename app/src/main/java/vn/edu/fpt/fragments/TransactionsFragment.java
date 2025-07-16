package vn.edu.fpt.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import vn.edu.fpt.R;
import vn.edu.fpt.adapter.TransactionAdapter;
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment implements TransactionAdapter.OnTransactionClickListener {
    
    private RecyclerView rvTransactions;
    private TransactionAdapter transactionAdapter;
    private DatabaseHelper databaseHelper;
    private ChipGroup chipGroupFilter;
    private Chip chipAll, chipIncome, chipExpense;
    private LinearLayout layoutEmptyState;
    
    private List<Transaction> allTransactions = new ArrayList<>();
    private String currentFilter = "all"; // "all", "income", "expense"
    
    public TransactionsFragment() {
    }

    public static TransactionsFragment newInstance() {
        return new TransactionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);
        
        // Initialize database
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        
        initializeViews(view);
        setupBasicUI();
        loadTransactions();
        
        return view;
    }
    
    private void initializeViews(View view) {
        rvTransactions = view.findViewById(R.id.rv_transactions);
        chipGroupFilter = view.findViewById(R.id.chip_group_filter);
        chipAll = view.findViewById(R.id.chip_all);
        chipIncome = view.findViewById(R.id.chip_income);
        chipExpense = view.findViewById(R.id.chip_expense);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
    }
    
    private void setupBasicUI() {
        // Set up RecyclerView
        rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        // Initialize and set adapter
        transactionAdapter = new TransactionAdapter();
        transactionAdapter.setOnTransactionClickListener(this);
        rvTransactions.setAdapter(transactionAdapter);
        
        // Setup filter chip listeners
        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_all)) {
                currentFilter = "all";
            } else if (checkedIds.contains(R.id.chip_income)) {
                currentFilter = "income";
            } else if (checkedIds.contains(R.id.chip_expense)) {
                currentFilter = "expense";
            }
            applyFilter();
        });
    }
    
    private void loadTransactions() {
        // Load all transactions from database
        allTransactions = databaseHelper.getAllTransactions();
        
        // Apply current filter
        applyFilter();
    }
    
    private void applyFilter() {
        List<Transaction> filteredTransactions = new ArrayList<>();
        
        for (Transaction transaction : allTransactions) {
            if (currentFilter.equals("all") || 
                transaction.getType().equals(currentFilter)) {
                filteredTransactions.add(transaction);
            }
        }
        
        // Update adapter with filtered data
        transactionAdapter.updateTransactions(filteredTransactions);
        
        // Show/hide empty state
        updateEmptyState(filteredTransactions.isEmpty());
    }
    
    private void updateEmptyState(boolean isEmpty) {
        if (isEmpty) {
            rvTransactions.setVisibility(View.GONE);
            layoutEmptyState.setVisibility(View.VISIBLE);
        } else {
            rvTransactions.setVisibility(View.VISIBLE);
            layoutEmptyState.setVisibility(View.GONE);
        }
    }
    
    // Implement OnTransactionClickListener interface
    @Override
    public void onTransactionClick(Transaction transaction) {
        // TODO: Show transaction details or navigate to edit screen
        Toast.makeText(getActivity(), "Clicked: " + transaction.getDescription(), Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public void onTransactionLongClick(Transaction transaction) {
        showTransactionOptionsDialog(transaction);
    }
    
    private void showTransactionOptionsDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Transaction Options");
        
        String[] options = {"Edit", "Delete"};
        
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: // Edit
                    // TODO: Navigate to edit transaction screen
                    Toast.makeText(getActivity(), "Edit functionality will be implemented", Toast.LENGTH_SHORT).show();
                    break;
                case 1: // Delete
                    showDeleteConfirmationDialog(transaction);
                    break;
            }
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void showDeleteConfirmationDialog(Transaction transaction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Transaction");
        builder.setMessage("Are you sure you want to delete this transaction?\n\n" +
                          transaction.getDescription() + "\n" +
                          transaction.getFormattedAmount());
        
        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteTransaction(transaction);
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    private void deleteTransaction(Transaction transaction) {
        int result = databaseHelper.deleteTransaction(transaction.getId());
        
        if (result > 0) {
            Toast.makeText(getActivity(), "Transaction deleted successfully", Toast.LENGTH_SHORT).show();
            // Refresh transactions list
            loadTransactions();
            
            // Notify other fragments to refresh their data
            notifyOtherFragmentsToRefresh();
        } else {
            Toast.makeText(getActivity(), "Failed to delete transaction", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void notifyOtherFragmentsToRefresh() {
        // Get parent fragment (SpendWiseMainFragment) and refresh all fragments
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).refreshAllFragments();
        }
    }
    
    // Public method to refresh data (called from other fragments)
    public void refreshTransactions() {
        if (databaseHelper != null && transactionAdapter != null) {
            loadTransactions();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to fragment
        refreshTransactions();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        rvTransactions = null;
        transactionAdapter = null;
        databaseHelper = null;
        chipGroupFilter = null;
        chipAll = null;
        chipIncome = null;
        chipExpense = null;
        layoutEmptyState = null;
        allTransactions = null;
    }
} 