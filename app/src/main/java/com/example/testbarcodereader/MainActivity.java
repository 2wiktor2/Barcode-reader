package com.example.testbarcodereader;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
    private TextView textViewCountScannedBarCode;

    SharedPreferences preferences;
    final String SAVED_TEXT = "saved_text";

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    //Колличество отсканирпованыых штрихкодов
    private int count;
    private int choosedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewResults = findViewById(R.id.recyclerView);
        textViewCountScannedBarCode = findViewById(R.id.textViewCountScanedBarCode);
        count = 5;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewResults.setLayoutManager(linearLayoutManager);

        rvAdapter = new RVAdapter(resultsOfScan);
        recyclerViewResults.setAdapter(rvAdapter);

        addBarcodeReaderFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
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
        count = resultsOfScan.size();
        textViewCountScannedBarCode.setText(String.valueOf(count));
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
        //int amountOfSymbols = 0;
        for (int i = 0; i < result.length(); i++) {
            if (Character.isDigit(result.charAt(i))) {
                amountOfNumbers++;
            } else if (Character.isLetter(result.charAt(i))) {
                amountOfLetters++;
            }
            //Количество остальных символов
            //amountOfSymbols = result.length() - amountOfNumbers - amountOfLetters;
        }
        return new MyBarecode(result, amountOfNumbers, amountOfLetters);
    }

    //Сохранение данных в sharedPreferences
    private void saveData() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        //ed.put(SAVED_TEXT, resultsOfScan);
        ed.apply();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        Log.d("qwerty", "список сохранен");
    }

    //Загрузка данных из sharedPreferences
    private void loadData() {
        preferences = getPreferences(MODE_PRIVATE);
        String savedText = preferences.getString(SAVED_TEXT, "");
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
        Log.d("qwerty", "список закгужен");
    }


    //Меню в toolbar-е
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    //обработка щелчка по меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        createDialog();
        return super.onOptionsItemSelected(item);
    }

    //Диалог выбора количетва сканируемых штрихкодов
    private void createDialog() {
        final AlertDialog.Builder dialogInfo = new AlertDialog.Builder(this);
        dialogInfo.setTitle("Колличество штрих-кодов")
                .setView(R.layout.dialog_choise_count)
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = dialogInfo.create();
        alert.show();
    }
}

