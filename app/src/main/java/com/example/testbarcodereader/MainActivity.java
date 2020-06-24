package com.example.testbarcodereader;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.notbytes.barcode_reader.BarcodeReaderFragment.newInstance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener {
    private TextView mTvResult;
    private RecyclerView recyclerViewResults;
    private ArrayList<String> resultsOfScan = new ArrayList<>();
    private HashMap<String, Pair> resultsOfScanMap = new HashMap<>();
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_fragment).setOnClickListener(this);
        mTvResult = findViewById(R.id.tv_result);
        recyclerViewResults = findViewById(R.id.recyclerView);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        recyclerViewResults.setLayoutManager(linearLayoutManager);

        // Контекст передается для доступа к ресурсам из адаптера, что бы менять цвет фона
        //rvAdapter = new RVAdapter(resultsOfScan, getApplicationContext());
        rvAdapter = new RVAdapter(resultsOfScanMap, getApplicationContext());
        recyclerViewResults.setAdapter(rvAdapter);

    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment;
        readerFragment = newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        androidx.fragment.app.FragmentManager supportFragmentManager = getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        addBarcodeReaderFragment();
    }

    @Override
    public void onScanned(Barcode barcode) {
        mTvResult.setText(barcode.rawValue);
        // Map
        resultsOfScanMap.put(barcode.rawValue, separateResult((barcode.rawValue)));
        //ArrayList
        //resultsOfScan.add(barcode.rawValue);
        rvAdapter.notifyDataSetChanged();
        Collections.reverse(resultsOfScan);
        Log.i("qwerty", "" + resultsOfScan.size());
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    private Pair<Integer, Integer> separateResult(String result) {
        int amountOfNumbers = 0;
        int amountOfLetters = 0;
        for (int i = 0; i < result.length(); i++) {
            if (Character.isDigit(result.charAt(i))) {
                amountOfNumbers++;
            }
            amountOfLetters = result.length() - amountOfNumbers;
        }
        return new Pair<>(amountOfNumbers, amountOfLetters);
    }
}

