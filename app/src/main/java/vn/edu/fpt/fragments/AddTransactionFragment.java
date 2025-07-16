package vn.edu.fpt.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import vn.edu.fpt.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTransactionFragment extends Fragment {
    
    // UI Components - chỉ cần basic cho giao diện
    private ChipGroup chipGroupType;
    private Chip chipExpense;
    private Chip chipIncome;
    private TextInputEditText etAmount;
    private TextInputEditText etDescription;
    private MaterialAutoCompleteTextView etCategory;
    private TextInputEditText etDate;
    private MaterialButton btnSaveTransaction;
    
    // Form data - đơn giản
    private String transactionType = "expense"; // default
    private Calendar selectedDate;
    private String[] expenseCategories = {"Food", "Transport", "Shopping", "Bills", "Entertainment", "Healthcare", "Other"};
    private String[] incomeCategories = {"Salary", "Business", "Freelance", "Investment", "Gift", "Other"};

    public AddTransactionFragment() {
    }

    public static AddTransactionFragment newInstance() {
        return new AddTransactionFragment();
    }

    // Fragment lifecycle theo pattern CannonGame - đơn giản
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        
        // Initialize components
        initializeViews(view);
        setupBasicUI();
        
        return view;
    }
    
    private void initializeViews(View view) {
        chipGroupType = view.findViewById(R.id.chip_group_type);
        chipExpense = view.findViewById(R.id.chip_expense);
        chipIncome = view.findViewById(R.id.chip_income);
        etAmount = view.findViewById(R.id.et_amount);
        etDescription = view.findViewById(R.id.et_description);
        etCategory = view.findViewById(R.id.et_category);
        etDate = view.findViewById(R.id.et_date);
        btnSaveTransaction = view.findViewById(R.id.btn_save_transaction);
        
        selectedDate = Calendar.getInstance();
    }
    
    private void setupBasicUI() {
        // Setup UI cơ bản
        updateDateDisplay();
        updateCategoryOptions();
        
        // Basic event listeners
        chipGroupType.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_expense)) {
                transactionType = "expense";
                updateCategoryOptions();
            } else if (checkedIds.contains(R.id.chip_income)) {
                transactionType = "income";
                updateCategoryOptions();
            }
        });
        
        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());
        
        // Save button - chỉ show toast để test UI
        btnSaveTransaction.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Transaction form UI working!", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void updateCategoryOptions() {
        String[] categories = transactionType.equals("expense") ? expenseCategories : incomeCategories;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), 
            android.R.layout.simple_dropdown_item_1line, categories);
        etCategory.setAdapter(adapter);
    }
    
    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
            getActivity(),
            (view, year, month, day) -> {
                selectedDate.set(year, month, day);
                updateDateDisplay();
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePicker.show();
    }
    
    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        etDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    // Lifecycle management theo pattern CannonGame
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Basic cleanup
        chipGroupType = null;
        chipExpense = null;
        chipIncome = null;
        etAmount = null;
        etDescription = null;
        etCategory = null;
        etDate = null;
        btnSaveTransaction = null;
        selectedDate = null;
    }
} 