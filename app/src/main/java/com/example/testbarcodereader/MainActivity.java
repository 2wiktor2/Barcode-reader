package com.example.testbarcodereader;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.List;

import static com.notbytes.barcode_reader.BarcodeReaderFragment.newInstance;

public class MainActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {
    private RecyclerView recyclerViewResults;
    private ArrayList<MyBarecode> resultsOfScan = new ArrayList<>();
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewResults = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewResults.setLayoutManager(linearLayoutManager);

        rvAdapter = new RVAdapter(resultsOfScan);
        recyclerViewResults.setAdapter(rvAdapter);

        addBarcodeReaderFragment();
    }

    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onScanned(Barcode barcode) {
        resultsOfScan.add(0, separateResult(barcode.rawValue));
        rvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Toast.makeText(this, "Scan error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_LONG).show();
    }

    private MyBarecode separateResult(String result) {
        int amountOfNumbers = 0;
        int amountOfLetters = 0;
        for (int i = 0; i < result.length(); i++) {
            if (Character.isDigit(result.charAt(i))) {
                amountOfNumbers++;
            }
            amountOfLetters = result.length() - amountOfNumbers;
        }
        return new MyBarecode(result, amountOfNumbers, amountOfLetters);
    }
}

