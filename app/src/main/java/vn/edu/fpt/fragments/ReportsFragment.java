package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.edu.fpt.R;
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.CategoryStats;

import java.util.Calendar;
import java.util.List;

public class ReportsFragment extends Fragment {

    // UI Components
    private TextView tvTotalIncome;
    private TextView tvTotalExpense;
    private TextView tvNetBalance;
    
    // Database
    private DatabaseHelper databaseHelper;

    public ReportsFragment() {
    }

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        
        // Initialize database
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        
        initializeViews(view);
        setupBasicUI();
        loadReportsData();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvTotalIncome = view.findViewById(R.id.tv_total_income);
        tvTotalExpense = view.findViewById(R.id.tv_total_expense);
        tvNetBalance = view.findViewById(R.id.tv_net_balance);
    }
    
    private void setupBasicUI() {
        // Basic UI setup if needed
    }
    
    private void loadReportsData() {
        // Get current month for monthly stats
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        
        // Load monthly financial data
        double monthlyIncome = databaseHelper.getMonthlyIncome(currentYear, currentMonth);
        double monthlyExpense = databaseHelper.getMonthlyExpense(currentYear, currentMonth);
        double netBalance = monthlyIncome - monthlyExpense;
        
        // Update UI with formatted values
        tvTotalIncome.setText(formatVND(monthlyIncome));
        tvTotalExpense.setText(formatVND(monthlyExpense));
        tvNetBalance.setText(formatVND(netBalance));
    }
    
    // Format currency in Vietnamese Dong
    private String formatVND(double amount) {
        return String.format("%,.0f VNĐ", amount);
    }
    
    // Public method to refresh data (called from other fragments)
    public void refreshReports() {
        if (databaseHelper != null) {
            loadReportsData();
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to fragment
        refreshReports();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup
        tvTotalIncome = null;
        tvTotalExpense = null;
        tvNetBalance = null;
        databaseHelper = null;
    }
} 