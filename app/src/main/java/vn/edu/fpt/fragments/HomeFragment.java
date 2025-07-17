package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import vn.edu.fpt.R;
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.Transaction;

import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvBalance;
    private TextView tvIncome;
    private TextView tvExpense;
    private MaterialButton btnAddIncome;
    private MaterialButton btnAddExpense;
    
    // Database
    private DatabaseHelper databaseHelper;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

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

        loadDashboardData();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvBalance = view.findViewById(R.id.tv_balance);
        tvIncome = view.findViewById(R.id.tv_income);
        tvExpense = view.findViewById(R.id.tv_expense);
        btnAddIncome = view.findViewById(R.id.btn_add_income);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
    }
    
    private void setupBasicUI() {
        // Setup quick action click listeners
        btnAddIncome.setOnClickListener(v -> {
            navigateToAddTransaction("income");
        });
        
        btnAddExpense.setOnClickListener(v -> {
            navigateToAddTransaction("expense");
        });
    }
    
    private void navigateToAddTransaction(String transactionType) {
        // Get parent fragment (SpendWiseMainFragment) and navigate to add transaction
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).navigateToAddTransaction(transactionType);
        }
    }
    
    private void loadDashboardData() {
        double totalIncome = databaseHelper.getTotalIncome();
        double totalExpense = databaseHelper.getTotalExpense();
        double currentBalance = databaseHelper.getCurrentBalance();

        tvBalance.setText(formatVND(currentBalance));
        tvIncome.setText(formatVND(totalIncome));
        tvExpense.setText(formatVND(totalExpense));
    }
    
    // Format currency in Vietnamese Dong
    private String formatVND(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }

    public void refreshData() {
        if (databaseHelper != null) {
            loadDashboardData();
        }
    }

    // Lifecycle management
    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        tvBalance = null;
        tvIncome = null;
        tvExpense = null;
        btnAddIncome = null;
        btnAddExpense = null;
        databaseHelper = null;
    }
} 