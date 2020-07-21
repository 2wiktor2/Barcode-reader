package com.example.testbarcodereader.activityesWithBarcodeReader.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.testbarcodereader.R;

public class FragmentStart extends Fragment {

    private Button buttonStartScan;
    private Button buttonStopScan;

    public static FragmentStart newInstanceFragmentStart() {
        Bundle args = new Bundle();
        FragmentStart fragmentStart = new FragmentStart();
        fragmentStart.setArguments(args);
        return fragmentStart;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        buttonStartScan = getActivity().findViewById(R.id.button_start_scan_in_fragment);
        buttonStopScan = getActivity().findViewById(R.id.button_stop_scan_in_fragment);
    }
}
