package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import vn.edu.fpt.R;

public class SpendWiseMainFragment extends Fragment {
    
    private BottomNavigationView bottomNavigation;
    private HomeFragment homeFragment;
    private TransactionsFragment transactionsFragment;
    private AddTransactionFragment addTransactionFragment;
    private ReportsFragment reportsFragment;
    
    // Fragment quản lý toàn bộ navigation logic theo pattern CannonGame
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        
        // Initialize navigation
        bottomNavigation = view.findViewById(R.id.bottom_navigation);
        setupNavigation();
        
        // Load default fragment
        if (savedInstanceState == null) {
            showHomeFragment();
        }
        
        return view;
    }
    
    private void setupNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                showHomeFragment();
                return true;
            } else if (item.getItemId() == R.id.nav_transactions) {
                showTransactionsFragment();
                return true;
            } else if (item.getItemId() == R.id.nav_add) {
                showAddTransactionFragment();
                return true;
            } else if (item.getItemId() == R.id.nav_reports) {
                showReportsFragment();
                return true;
            }
            return false;
        });
    }
    
    private void showHomeFragment() {
        if (homeFragment == null) {
            homeFragment = HomeFragment.newInstance();
        }
        replaceFragment(homeFragment);
    }
    
    private void showTransactionsFragment() {
        if (transactionsFragment == null) {
            transactionsFragment = TransactionsFragment.newInstance();
        }
        replaceFragment(transactionsFragment);
    }
    
    private void showAddTransactionFragment() {
        if (addTransactionFragment == null) {
            addTransactionFragment = AddTransactionFragment.newInstance();
        }
        replaceFragment(addTransactionFragment);
    }
    
    private void showReportsFragment() {
        if (reportsFragment == null) {
            reportsFragment = ReportsFragment.newInstance();
        }
        replaceFragment(reportsFragment);
    }
    
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
    
    // Lifecycle management theo pattern CannonGame
    @Override
    public void onPause() {
        super.onPause();
        // Cleanup khi fragment bị pause
        if (homeFragment != null && homeFragment.isAdded()) {
            homeFragment.onPause();
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release resources khi fragment bị destroy
        homeFragment = null;
        transactionsFragment = null;
        addTransactionFragment = null;
        reportsFragment = null;
    }
} 