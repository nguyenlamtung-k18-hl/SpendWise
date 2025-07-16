package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.fpt.R;
import vn.edu.fpt.adapter.TransactionAdapter;
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.Transaction;

import java.util.List;

public class TransactionsFragment extends Fragment {
    
    private RecyclerView rvTransactions;
    private TransactionAdapter transactionAdapter;
    private DatabaseHelper databaseHelper;
    
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
    }
    
    private void setupBasicUI() {
        // Set up RecyclerView
        rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        // Initialize and set adapter
        transactionAdapter = new TransactionAdapter();
        rvTransactions.setAdapter(transactionAdapter);
    }
    
    private void loadTransactions() {
        // Load all transactions from database
        List<Transaction> transactions = databaseHelper.getAllTransactions();
        
        // Update adapter with loaded data
        transactionAdapter.updateTransactions(transactions);
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
    }
} 