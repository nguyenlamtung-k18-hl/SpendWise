package vn.edu.fpt.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import vn.edu.fpt.R;
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.CategoryStats;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportsFragment extends Fragment {

    // UI Components
    private TextView tvTotalIncome;
    private TextView tvTotalExpense;
    private TextView tvNetBalance;
    private ChipGroup chipGroupPeriod;
    private Chip chipThisMonth, chipThisYear, chipCustom;
    
    // Database
    private DatabaseHelper databaseHelper;
    
    // Period tracking
    private String currentPeriod = "month"; // "month", "year", "custom"
    private Calendar customStartDate;
    private Calendar customEndDate;
    private SimpleDateFormat dateFormat;

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
        
        // Initialize date formatter
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        
        // Initialize custom date range to current month
        customStartDate = Calendar.getInstance();
        customEndDate = Calendar.getInstance();
        setCurrentMonthRange();
        
        initializeViews(view);
        setupBasicUI();
        loadReportsData();
        
        return view;
    }
    
    private void initializeViews(View view) {
        tvTotalIncome = view.findViewById(R.id.tv_total_income);
        tvTotalExpense = view.findViewById(R.id.tv_total_expense);
        tvNetBalance = view.findViewById(R.id.tv_net_balance);
        chipGroupPeriod = view.findViewById(R.id.chip_group_period);
        chipThisMonth = view.findViewById(R.id.chip_this_month);
        chipThisYear = view.findViewById(R.id.chip_this_year);
        chipCustom = view.findViewById(R.id.chip_custom);
    }
    
    private void setupBasicUI() {
        // Setup time period filter
        chipGroupPeriod.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_this_month)) {
                currentPeriod = "month";
                setCurrentMonthRange();
                loadReportsData();
            } else if (checkedIds.contains(R.id.chip_this_year)) {
                currentPeriod = "year";
                setCurrentYearRange();
                loadReportsData();
            } else if (checkedIds.contains(R.id.chip_custom)) {
                currentPeriod = "custom";
                showCustomDatePicker();
            }
        });
    }
    
    private void setCurrentMonthRange() {
        Calendar now = Calendar.getInstance();
        
        // Start of current month
        customStartDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1, 0, 0, 0);
        customStartDate.set(Calendar.MILLISECOND, 0);
        
        // End of current month
        customEndDate.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 
                         now.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        customEndDate.set(Calendar.MILLISECOND, 999);
    }
    
    private void setCurrentYearRange() {
        Calendar now = Calendar.getInstance();
        
        // Start of current year
        customStartDate.set(now.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0, 0);
        customStartDate.set(Calendar.MILLISECOND, 0);
        
        // End of current year
        customEndDate.set(now.get(Calendar.YEAR), Calendar.DECEMBER, 31, 23, 59, 59);
        customEndDate.set(Calendar.MILLISECOND, 999);
    }
    
    private void showCustomDatePicker() {
        // Show start date picker first
        DatePickerDialog startDatePicker = new DatePickerDialog(
            getActivity(),
            (view, year, month, day) -> {
                customStartDate.set(year, month, day, 0, 0, 0);
                customStartDate.set(Calendar.MILLISECOND, 0);
                showEndDatePicker();
            },
            customStartDate.get(Calendar.YEAR),
            customStartDate.get(Calendar.MONTH),
            customStartDate.get(Calendar.DAY_OF_MONTH)
        );
        
        startDatePicker.setTitle("Select Start Date");
        startDatePicker.show();
    }
    
    private void showEndDatePicker() {
        DatePickerDialog endDatePicker = new DatePickerDialog(
            getActivity(),
            (view, year, month, day) -> {
                customEndDate.set(year, month, day, 23, 59, 59);
                customEndDate.set(Calendar.MILLISECOND, 999);
                
                // Validate date range
                if (customEndDate.before(customStartDate)) {
                    Toast.makeText(getActivity(), "End date cannot be before start date", Toast.LENGTH_SHORT).show();
                    // Reset to current month
                    setCurrentMonthRange();
                    chipThisMonth.setChecked(true);
                    currentPeriod = "month";
                } else {
                    // Update chip text to show date range
                    String rangeText = dateFormat.format(customStartDate.getTime()) + 
                                     " - " + dateFormat.format(customEndDate.getTime());
                    chipCustom.setText(rangeText);
                }
                
                loadReportsData();
            },
            customEndDate.get(Calendar.YEAR),
            customEndDate.get(Calendar.MONTH),
            customEndDate.get(Calendar.DAY_OF_MONTH)
        );
        
        endDatePicker.setTitle("Select End Date");
        // Set minimum date to start date
        endDatePicker.getDatePicker().setMinDate(customStartDate.getTimeInMillis());
        endDatePicker.show();
    }
    
    private void loadReportsData() {
        double totalIncome, totalExpense;
        
        if (currentPeriod.equals("month")) {
            // Current month data
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            
            totalIncome = databaseHelper.getMonthlyIncome(currentYear, currentMonth);
            totalExpense = databaseHelper.getMonthlyExpense(currentYear, currentMonth);
            
        } else if (currentPeriod.equals("year")) {
            // Current year data (sum all months)
            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            
            totalIncome = 0;
            totalExpense = 0;
            
            for (int month = 1; month <= 12; month++) {
                totalIncome += databaseHelper.getMonthlyIncome(currentYear, month);
                totalExpense += databaseHelper.getMonthlyExpense(currentYear, month);
            }
            
        } else {
            // Custom date range - use the actual custom date range
            long startTimestamp = customStartDate.getTimeInMillis();
            long endTimestamp = customEndDate.getTimeInMillis();
            
            totalIncome = databaseHelper.getIncomeForDateRange(startTimestamp, endTimestamp);
            totalExpense = databaseHelper.getExpenseForDateRange(startTimestamp, endTimestamp);
        }
        
        double netBalance = totalIncome - totalExpense;
        
        // Update UI with formatted values
        tvTotalIncome.setText(formatVND(totalIncome));
        tvTotalExpense.setText(formatVND(totalExpense));
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
        chipGroupPeriod = null;
        chipThisMonth = null;
        chipThisYear = null;
        chipCustom = null;
        databaseHelper = null;
        customStartDate = null;
        customEndDate = null;
        dateFormat = null;
    }
} 