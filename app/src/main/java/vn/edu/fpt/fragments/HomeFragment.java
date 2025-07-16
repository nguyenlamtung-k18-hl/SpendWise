package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import vn.edu.fpt.R;

public class HomeFragment extends Fragment {

    // UI Components
    private TextView tvBalance;
    private TextView tvIncome;
    private TextView tvExpense;
    private MaterialButton btnAddIncome;
    private MaterialButton btnAddExpense;
    private MaterialButton btnViewAll;

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
        
        // Initialize views
        initializeViews(view);
        setupBasicUI();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvBalance = view.findViewById(R.id.tv_balance);
        tvIncome = view.findViewById(R.id.tv_income);
        tvExpense = view.findViewById(R.id.tv_expense);
        btnAddIncome = view.findViewById(R.id.btn_add_income);
        btnAddExpense = view.findViewById(R.id.btn_add_expense);
        btnViewAll = view.findViewById(R.id.btn_view_all);
    }
    
    private void setupBasicUI() {
        // Chỉ setup UI cơ bản, chưa cần logic
        tvBalance.setText("$1,750.00");
        tvIncome.setText("$5,000.00");
        tvExpense.setText("$3,250.00");
        
        // Basic click listeners
        btnAddIncome.setOnClickListener(v -> {
            // TODO: Navigate to add transaction
        });
        
        btnAddExpense.setOnClickListener(v -> {
            // TODO: Navigate to add transaction
        });
        
        btnViewAll.setOnClickListener(v -> {
            // TODO: Navigate to transactions list
        });
    }

    // Lifecycle management
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
    }
} 