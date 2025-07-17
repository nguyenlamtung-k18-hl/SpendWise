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
        rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));

        transactionAdapter = new TransactionAdapter();
        transactionAdapter.setOnTransactionClickListener(this);
        rvTransactions.setAdapter(transactionAdapter);

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
        allTransactions = databaseHelper.getAllTransactions();

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

        transactionAdapter.updateTransactions(filteredTransactions);

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

    @Override
    public void onTransactionClick(Transaction transaction) {
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
            loadTransactions();

            notifyOtherFragmentsToRefresh();
        } else {
            Toast.makeText(getActivity(), "Failed to delete transaction", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void notifyOtherFragmentsToRefresh() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).refreshAllFragments();
        }
    }

    public void refreshTransactions() {
        if (databaseHelper != null && transactionAdapter != null) {
            loadTransactions();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
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