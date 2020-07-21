package com.example.testbarcodereader.activityesWithBarcodeReader;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activitySend.ActivitySendBarcode;
import com.example.testbarcodereader.activityesWithBarcodeReader.adapter.RVAdapter;
import com.example.testbarcodereader.data.MyBarcode;
import com.example.testbarcodereader.utils.Constants;
import com.example.testbarcodereader.utils.Converter;
import com.example.testbarcodereader.utils.MyDialogs;
import com.google.android.gms.vision.barcode.Barcode;
import com.notbytes.barcode_reader.BarcodeReaderFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.notbytes.barcode_reader.BarcodeReaderFragment.newInstance;

public class MainActivity extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener, SoundPool.OnLoadCompleteListener {

    //todo ИСПРАВИТь иногда открываются сразу два окна акривити
    private ArrayList<MyBarcode> barcodes = new ArrayList<>();
    private RVAdapter rvAdapter;
    private TextView textViewCountScannedBarCode;
    private HashSet setOfBarcode;
    private int setSize;

    Converter converter;
    MyDialogs dialogs;
    SharedPreferences preferences;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    //Колличество отсканированыых штрих-кодов
    private int count;
    //Ограничение на сканирование штрих-кодов
    private int selectedQuantity;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        //Установить стрелку в toolBar
/*        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/

        RecyclerView recyclerViewResults = findViewById(R.id.recyclerView);
        textViewCountScannedBarCode = findViewById(R.id.textViewCountScanedBarCode);

        setOfBarcode = new HashSet<>();
        converter = new Converter();
        dialogs = new MyDialogs(this);

        mp = MediaPlayer.create(this, R.raw.beep);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewResults.setLayoutManager(linearLayoutManager);

        rvAdapter = new RVAdapter(barcodes);
        recyclerViewResults.setAdapter(rvAdapter);

        addBarcodeReaderFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

        Log.i("qwerty123", "onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        setSize = setOfBarcode.size();
        textViewCountScannedBarCode.setText(updateInfoText(setSize));

        Log.i("qwerty123", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
        Log.i("qwerty123", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("qwerty123", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setOfBarcode.clear();
        barcodes.clear();
        clearSharedPreferences();
        Log.i("qwerty123", "onDestroy");
    }

    @Override
    public void onBackPressed() {
        createWarningDialog();
        // super.onBackPressed();
        Log.i("qwerty123", "onBackPressed");
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

        //Звук
        try {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(this, R.raw.beep);
            }
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // добавление отсканированного штрихкода в hashSet
        if (validator(barcode.displayValue)) {
            if (setOfBarcode.add(barcode.rawValue)) {
                barcodes.add(0, separateResult(barcode.rawValue));
                setSize = setOfBarcode.size();
            } else {
                dialogs.createWarningDuplicateBarcodeDialog(barcode.rawValue);
                Toast.makeText(this, "Такой штрих-код уже отсканирован", Toast.LENGTH_SHORT).show();
            }
        } else {
            dialogs.createWarningWrongMacAdresDialog(barcode.rawValue);
        }


        rvAdapter.notifyDataSetChanged();
        count = barcodes.size();
        textViewCountScannedBarCode.setText(updateInfoText(setSize));
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
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(Constants.KEY_FOR_SELECTED_QUANTITY, selectedQuantity);
        editor.putInt(Constants.KEY_FOR_COUNT, count);

        editor.putInt("Status_size", barcodes.size());
        for (int i = 0; i < barcodes.size(); i++) {
            MyBarcode barcode = barcodes.get(i);
            String s = barcode.getBarcodeResult();
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, s);
        }
        editor.putStringSet(Constants.KEY_FOR_SET, setOfBarcode);
        editor.apply();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    //Загрузка данных из sharedPreferences
    private void loadData() {
        preferences = getPreferences(MODE_PRIVATE);
        selectedQuantity = preferences.getInt(Constants.KEY_FOR_SELECTED_QUANTITY, 2);
        count = preferences.getInt(Constants.KEY_FOR_COUNT, 0);
        setOfBarcode = (HashSet<String>) preferences.getStringSet(Constants.KEY_FOR_SET, new HashSet<String>());
        if (setOfBarcode != null) {
            Log.i("qwertyu", "Длина SET-а = " + setOfBarcode.size());
        }
        barcodes.clear();

        int size = preferences.getInt("Status_size", 0);

        //todo Сделать добавлене в обратном порядке
        for (int i = 0; i < size; i++) {
            String sss = preferences.getString("Status_" + i, null);
            if (sss != null) {
                barcodes.add(0, separateResult(sss));
            }
        }
        //переворот в обратном порядке
        Collections.reverse(barcodes);

        rvAdapter.notifyDataSetChanged();
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
                        textViewCountScannedBarCode.setText(updateInfoText(setSize));
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
                        barcodes.clear();
                        count = barcodes.size();
                        textViewCountScannedBarCode.setText(updateInfoText(setSize));
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

    //Диалог предупреждение перед выходом из приложения
    private void createWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("У Вас есть несохраненные данные!")
                .setMessage("При переходе на главный экран\nвсе несохраненные данные удалятся!")
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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
    private String updateInfoText(int setSize) {
        return count + " из " + selectedQuantity + "  " + "set = " + setSize;
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
        bundle.putSerializable(Constants.KEY_FOR_SEND_ARRAY_LIST, barcodes);
        intent.putExtra(Constants.KEY_BUNDLE, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //Валидатор
    // todo реализовать валидатор. Соответствует ли отсканированная строка какому-то шаблону
    private boolean validator(String s) {
        if (s.length() != 16) {
            return false;
        } else {
            return true;
        }
    }

    //метод для отчистки SharedPreferences
    private void clearSharedPreferences() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.clear();
        editor.apply();
        finish();
    }

    //для работы со звуком
    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
        Log.i("qwertyu", "onLoadComplete, sampleId = " + sampleId + ", status = " + status);
    }
}

