package com.example.testbarcodereader;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Collections;
import java.util.List;

import static com.notbytes.barcode_reader.BarcodeReaderFragment.newInstance;

public class MainActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener {
    private RecyclerView recyclerViewResults;
    private ArrayList<MyBarcode> resultsOfScan = new ArrayList<>();
    private RVAdapter rvAdapter;
    private TextView textViewCountScannedBarCode;

    SharedPreferences preferences;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    //Колличество отсканированыых штрих-кодов
    private int count;
    //Ограничение на сканирование штрих-кодов
    private int selectedQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewResults = findViewById(R.id.recyclerView);
        textViewCountScannedBarCode = findViewById(R.id.textViewCountScanedBarCode);

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
        textViewCountScannedBarCode.setText(updateInfoText());
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
        textViewCountScannedBarCode.setText(updateInfoText());
        timeToSave();
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

    private MyBarcode separateResult(String result) {
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
        return new MyBarcode(result, amountOfNumbers, amountOfLetters);
    }

    //Сохранение данных в sharedPreferences
    private void saveData() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(Constants.KEY_FOR_SELECTED_QUANTITY, selectedQuantity);
        ed.putInt(Constants.KEY_FOR_COUNT, count);
        ed.putInt("Status_size", resultsOfScan.size());
        for (int i = 0; i < resultsOfScan.size(); i++) {
            MyBarcode barcode = resultsOfScan.get(i);
            String s = barcode.getBarcodeResult();
            ed.remove("Status_" + i);
            ed.putString("Status_" + i, s);
        }
        ed.apply();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    //Загрузка данных из sharedPreferences
    private void loadData() {
        preferences = getPreferences(MODE_PRIVATE);
        selectedQuantity = preferences.getInt(Constants.KEY_FOR_SELECTED_QUANTITY, 7);
        count = preferences.getInt(Constants.KEY_FOR_COUNT, -1);

        resultsOfScan.clear();
        int size = preferences.getInt("Status_size", 0);

        //todo Сделать добавлене в обратном порядке
        for (int i = 0; i < size; i++) {
            String sss = preferences.getString("Status_" + i, null);
            if (sss != null) {
                resultsOfScan.add(0, separateResult(sss));
            }
        }
        //переворот в обратном порядке
        Collections.reverse(resultsOfScan);
        Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
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
        switch (item.getItemId()) {
            case R.id.set_count:
                createDialog();
                break;
            case R.id.check:
                startActivityForCheckAndSend();
                break;
            case R.id.delete:
                clearListOfBarCodeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Диалог выбора количетва сканируемых штрихкодов
    private void createDialog() {
        final String[] items = {"5", "10", "20", "50"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Максимальное количество штрих-кодов")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        //mDoneButton.setText(items[item]);
                        selectedQuantity = Integer.parseInt(items[item]);
                        textViewCountScannedBarCode.setText(updateInfoText());
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Диалог отчистка списка
    private void clearListOfBarCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить все записи?")
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //отчистка списка
                        resultsOfScan.clear();
                        count = resultsOfScan.size();
                        textViewCountScannedBarCode.setText(updateInfoText());
                        rvAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cencel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Вставка текста в TextView
    private String updateInfoText() {
        return count + " из " + selectedQuantity;
    }

    //Отслеживать колличество отсканированных штрих-кодов и выводить сообщение при превышении порога
    //Останавливать сканер
    private void timeToSave() {
        if (count >= selectedQuantity) {
            Toast.makeText(this, "Пора сохраниться", Toast.LENGTH_LONG).show();
            //Если количество отсканированных штрих кодов праевышает погог,
            // то переходим на активити для проверки и отправки
            startActivityForCheckAndSend();
        }
    }

    private void startActivityForCheckAndSend() {
        Intent intent = new Intent(this, ActivitySendBarcode.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ARRAY_LIST", resultsOfScan);
        intent.putExtra("BUNDLE", bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

