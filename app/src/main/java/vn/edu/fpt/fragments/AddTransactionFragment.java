package vn.edu.fpt.fragments;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
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
import vn.edu.fpt.database.DatabaseHelper;
import vn.edu.fpt.model.Category;
import vn.edu.fpt.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddTransactionFragment extends Fragment {
    
    // UI Components
    private ChipGroup chipGroupType;
    private Chip chipExpense;
    private Chip chipIncome;
    private TextInputEditText etAmount;
    private TextInputEditText etDescription;
    private MaterialAutoCompleteTextView etCategory;
    private TextInputEditText etDate;
    private MaterialButton btnSaveTransaction;
    
    // Database & Data
    private DatabaseHelper databaseHelper;
    private String transactionType = "expense"; // default
    private Calendar selectedDate;
    private List<Category> currentCategories;
    private Category selectedCategory;

    public AddTransactionFragment() {
    }

    public static AddTransactionFragment newInstance() {
        return new AddTransactionFragment();
    }

    // Fragment lifecycle
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);
        
        // Initialize database
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        
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
        // Setup initial UI
        updateDateDisplay();
        loadCategoriesFromDatabase();
        
        // Type selection listeners
        chipGroupType.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.contains(R.id.chip_expense)) {
                transactionType = "expense";
                loadCategoriesFromDatabase();
            } else if (checkedIds.contains(R.id.chip_income)) {
                transactionType = "income";
                loadCategoriesFromDatabase();
            }
        });
        
        // Date picker
        etDate.setOnClickListener(v -> showDatePicker());
        
        // Category selection
        etCategory.setOnItemClickListener((parent, view, position, id) -> {
            if (currentCategories != null && position < currentCategories.size()) {
                selectedCategory = currentCategories.get(position);
            }
        });
        
        // Save transaction button
        btnSaveTransaction.setOnClickListener(v -> saveTransaction());
    }
    
    private void loadCategoriesFromDatabase() {
        currentCategories = databaseHelper.getCategoriesByType(transactionType);
        
        if (currentCategories != null && !currentCategories.isEmpty()) {
            String[] categoryNames = new String[currentCategories.size()];
            for (int i = 0; i < currentCategories.size(); i++) {
                categoryNames[i] = currentCategories.get(i).getName();
            }
            
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), 
                android.R.layout.simple_dropdown_item_1line, categoryNames);
            etCategory.setAdapter(adapter);
            
            // Clear previous selection
            etCategory.setText("");
            selectedCategory = null;
        }
    }
    
    private void saveTransaction() {
        // Validate input
        if (!validateInput()) {
            return;
        }
        
        try {
            // Parse amount
            double amount = Double.parseDouble(etAmount.getText().toString().trim());
            String description = etDescription.getText().toString().trim();
            long timestamp = selectedDate.getTimeInMillis();
            
            // Create transaction
            Transaction transaction = new Transaction(
                transactionType,
                amount,
                description,
                selectedCategory.getId(),
                timestamp
            );
            
            // Save to database
            long result = databaseHelper.addTransaction(transaction);
            
            if (result > 0) {
                Toast.makeText(getActivity(), "Giao dịch đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                clearForm();
                
                // Refresh other fragments
                refreshOtherFragments();
                
                // Navigate back to home
                navigateToHome();
            } else {
                Toast.makeText(getActivity(), "Lỗi khi lưu giao dịch!", Toast.LENGTH_SHORT).show();
            }
            
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Số tiền không hợp lệ!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private boolean validateInput() {
        // Validate amount
        if (TextUtils.isEmpty(etAmount.getText())) {
            etAmount.setError("Vui lòng nhập số tiền");
            return false;
        }
        
        try {
            double amount = Double.parseDouble(etAmount.getText().toString().trim());
            if (amount <= 0) {
                etAmount.setError("Số tiền phải lớn hơn 0");
                return false;
            }
        } catch (NumberFormatException e) {
            etAmount.setError("Số tiền không hợp lệ");
            return false;
        }
        
        // Validate description
        if (TextUtils.isEmpty(etDescription.getText())) {
            etDescription.setError("Vui lòng nhập mô tả");
            return false;
        }
        
        // Validate category
        if (selectedCategory == null) {
            Toast.makeText(getActivity(), "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        etAmount.setText("");
        etDescription.setText("");
        etCategory.setText("");
        selectedCategory = null;
        selectedDate = Calendar.getInstance();
        updateDateDisplay();
        // Reset to expense type
        chipExpense.setChecked(true);
        transactionType = "expense";
        loadCategoriesFromDatabase();
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        etDate.setText(dateFormat.format(selectedDate.getTime()));
    }

    // Lifecycle management
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Cleanup
        chipGroupType = null;
        chipExpense = null;
        chipIncome = null;
        etAmount = null;
        etDescription = null;
        etCategory = null;
        etDate = null;
        btnSaveTransaction = null;
        selectedDate = null;
        databaseHelper = null;
        currentCategories = null;
        selectedCategory = null;
    }

    private void refreshOtherFragments() {
        // Get parent fragment (SpendWiseMainFragment) and refresh all fragments
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).refreshAllFragments();
        }
    }
    
    private void navigateToHome() {
        // Get parent fragment (SpendWiseMainFragment) and navigate to home
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof SpendWiseMainFragment) {
            ((SpendWiseMainFragment) parentFragment).navigateToHome();
        }
    }
} 