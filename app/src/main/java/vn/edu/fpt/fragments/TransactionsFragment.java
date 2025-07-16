package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.fpt.R;

public class TransactionsFragment extends Fragment {
    
    private RecyclerView rvTransactions;
    
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
        
        initializeViews(view);
        setupBasicUI();
        
        return view;
    }
    
    private void initializeViews(View view) {
        rvTransactions = view.findViewById(R.id.rv_transactions);
    }
    
    private void setupBasicUI() {
        // Set up RecyclerView
        rvTransactions.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        // TODO: Set adapter
        // rvTransactions.setAdapter(adapter);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        rvTransactions = null;
    }
} 