package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import vn.edu.fpt.R;
import vn.edu.fpt.adapter.TransactionAdapter;
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.Transaction;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    // UI Components
    private TextView tvBalance;
    private TextView tvIncome;
    private TextView tvExpense;
    private MaterialButton btnAddIncome;
    private MaterialButton btnAddExpense;
    private MaterialButton btnViewAll;
    private RecyclerView rvRecentTransactions;
    private TextView tvNoTransactions;
    
    // Adapter and Database
    private TransactionAdapter recentTransactionsAdapter;
    private DatabaseHelper databaseHelper;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Initialize database
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        
        // Initialize views
        initializeViews(view);
        setupBasicUI();
        
        // Load real data
        loadDashboardData();
        loadRecentTransactions();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvBalance = view.findViewById(R.id.tv_balance);
        tvIncome = view.findViewById(R.id.tv_income);
        tvExpense = view.findViewById(R.id.tv_expense);
        btnAddIncome = view.findViewById(R.id.btn_add_income);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
        btnViewAll = view.findViewById(R.id.btn_view_all);
        rvRecentTransactions = view.findViewById(R.id.rv_recent_transactions);
        tvNoTransactions = view.findViewById(R.id.tv_no_transactions);
    }
    
    private void setupBasicUI() {
        // Setup recent transactions RecyclerView
        setupRecentTransactionsRecyclerView();
        
        // Setup quick action click listeners
        btnAddIncome.setOnClickListener(v -> {
            navigateToAddTransaction("income");
        });
        
        btnAddExpense.setOnClickListener(v -> {
            navigateToAddTransaction("expense");
        });
        
        btnViewAll.setOnClickListener(v -> {
            navigateToTransactions();
        });
    }
    
    private void setupRecentTransactionsRecyclerView() {
        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvRecentTransactions.setLayoutManager(layoutManager);
        
        // Initialize adapter
        recentTransactionsAdapter = new TransactionAdapter();
        rvRecentTransactions.setAdapter(recentTransactionsAdapter);
        
        // Disable nested scrolling for smooth scrolling in parent ScrollView
        rvRecentTransactions.setNestedScrollingEnabled(false);
    }
    
    private void loadRecentTransactions() {
        // Load recent transactions (limit to 5)
        List<Transaction> recentTransactions = databaseHelper.getRecentTransactions(5);
        
        // Update adapter
        recentTransactionsAdapter.updateTransactions(recentTransactions);
        
        // Show/hide empty state
        updateRecentTransactionsVisibility(recentTransactions.isEmpty());
    }
    
    private void updateRecentTransactionsVisibility(boolean isEmpty) {
        if (isEmpty) {
            rvRecentTransactions.setVisibility(View.GONE);
            tvNoTransactions.setVisibility(View.VISIBLE);
        } else {
            rvRecentTransactions.setVisibility(View.VISIBLE);
            tvNoTransactions.setVisibility(View.GONE);
        }
    }
    
    private void navigateToAddTransaction(String transactionType) {
        // Get parent fragment (SpendWiseMainFragment) and navigate to add transaction
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).navigateToAddTransaction(transactionType);
        }
    }
    
    private void navigateToTransactions() {
        // Get parent fragment (SpendWiseMainFragment) and navigate to transactions
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).navigateToTransactions();
        }
    }
    
    private void loadDashboardData() {
        // Load overall financial data
        double totalIncome = databaseHelper.getTotalIncome();
        double totalExpense = databaseHelper.getTotalExpense();
        double currentBalance = databaseHelper.getCurrentBalance();
        
        // Update UI with formatted currency
        tvBalance.setText(formatVND(currentBalance));
        tvIncome.setText(formatVND(totalIncome));
        tvExpense.setText(formatVND(totalExpense));
    }
    
    // Format currency in Vietnamese Dong
    private String formatVND(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }
    
    // Public method to refresh data (called from other fragments)
    public void refreshData() {
        if (databaseHelper != null) {
            loadDashboardData();
            loadRecentTransactions();
        }
    }

    // Lifecycle management
    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to fragment
        refreshData();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Basic cleanup
        tvBalance = null;
        tvIncome = null;
        tvExpense = null;
        btnAddIncome = null;
        btnAddExpense = null;
        btnViewAll = null;
        rvRecentTransactions = null;
        tvNoTransactions = null;
        recentTransactionsAdapter = null;
        databaseHelper = null;
    }
} 