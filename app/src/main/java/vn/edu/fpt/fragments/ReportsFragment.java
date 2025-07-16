package vn.edu.fpt.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.edu.fpt.R;

public class ReportsFragment extends Fragment {

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
        
        setupBasicUI();
        
        return view;
    }
    
    private void setupBasicUI() {
        // TODO: Setup UI components khi implement
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Basic cleanup
    }
} 