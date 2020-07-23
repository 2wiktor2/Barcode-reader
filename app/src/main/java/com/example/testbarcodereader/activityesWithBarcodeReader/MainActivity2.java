package com.example.testbarcodereader.activityesWithBarcodeReader;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activitySend.ActivitySendBarcode;
import com.example.testbarcodereader.activityesWithBarcodeReader.adapter.RVAdapter;
import com.example.testbarcodereader.activityesWithBarcodeReader.fragments.FragmentStart;
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

public class MainActivity2 extends AppCompatActivity implements BarcodeReaderFragment.BarcodeReaderListener, SoundPool.OnLoadCompleteListener, View.OnClickListener {

    //todo ИСПРАВИТь иногда открываются сразу два окна акривити
    private ArrayList<MyBarcode> barcodes = new ArrayList<>();
    private RVAdapter rvAdapter;
    // Всего отсканировано
    private TextView textViewSetOfBarcodes;
    // порог отправки. Автосохранение
    private TextView textViewThresholdAutoSave;
    private HashSet setOfBarcode;
    private int setSize;

    Converter converter;
    MyDialogs dialogs;
    SharedPreferences preferences;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    //Колличество отсканированыых штрих-кодов
    private int count;
    //Ограничение на сканирование штрих-кодов. Автосохранение
    private int selectedQuantityAutoSave;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        RecyclerView recyclerViewResults = findViewById(R.id.recyclerView);
        textViewSetOfBarcodes = findViewById(R.id.textView_set_of_barcodes);
        textViewThresholdAutoSave = findViewById(R.id.textView_threshold);
        ImageButton imageButtonStartScan = findViewById(R.id.image_button_start_scan);
        ImageButton imageButtonStopScan = findViewById(R.id.image_button_stop_scan);
        imageButtonStartScan.setOnClickListener(this);
        imageButtonStopScan.setOnClickListener(this);

        setOfBarcode = new HashSet<>();
        converter = new Converter();
        dialogs = new MyDialogs(this);

        mp = MediaPlayer.create(this, R.raw.beep);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewResults.setLayoutManager(linearLayoutManager);

        rvAdapter = new RVAdapter(barcodes);
        recyclerViewResults.setAdapter(rvAdapter);

        //Добавление фрагмента при запуске активити
        addStartFragment();
        //addBarcodeReaderFragment();

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
        setSize = setOfBarcode.size();
        updateInfoTextViews(setSize);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setOfBarcode.clear();
        barcodes.clear();
        clearSharedPreferences();
    }

    @Override
    public void onBackPressed() {
        createWarningDialog();
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

    // Добавление фрагмента со сканером
    private void addBarcodeReaderFragment() {
        BarcodeReaderFragment readerFragment = newInstance(true, false, View.VISIBLE);
        readerFragment.setListener(this);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fm_container, readerFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    // Добавление стартового фрагмента
    private void addStartFragment() {
        FragmentStart fragmentStart = new FragmentStart();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction
                .replace(R.id.fm_container, fragmentStart)
                .commit();
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
                //Toast.makeText(this, "Такой штрих-код уже отсканирован", Toast.LENGTH_SHORT).show();
            }
        } else {
            dialogs.createWarningWrongMacAdresDialog(barcode.rawValue);
        }


        rvAdapter.notifyDataSetChanged();
        count = barcodes.size();

        updateInfoTextViews(setSize);
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
        }
        return new MyBarcode(result, amountOfNumbers, amountOfLetters);
    }

    //Сохранение данных в sharedPreferences
    private void saveData() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(Constants.KEY_FOR_SELECTED_QUANTITY, selectedQuantityAutoSave);
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
        //Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }

    //Загрузка данных из sharedPreferences
    private void loadData() {
        preferences = getPreferences(MODE_PRIVATE);
        selectedQuantityAutoSave = preferences.getInt(Constants.KEY_FOR_SELECTED_QUANTITY, 20);
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
        //Toast.makeText(this, "Text loaded", Toast.LENGTH_SHORT).show();
    }


    //Меню в toolbar-е
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_in_main_activity, menu);
        return true;
    }

    //обработка щелчка по меню
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.auto_save_set_count:
                createDialog();
                break;
            case R.id.check:
                startActivityForCheckAndSend();
                break;
            case R.id.delete:
                clearListOfBarCodeDialog();
                break;
            case R.id.info:
                dialogs.createInfoDialog();
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
                        selectedQuantityAutoSave = Integer.parseInt(items[item]);
                        updateInfoTextViews(setSize);
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
                        updateInfoTextViews(setSize);
                        rvAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Информация. Вывод пользователю
    private void updateInfoTextViews(int setSize) {
        textViewSetOfBarcodes.setText("Всего отсканировано : " + setSize);
        textViewThresholdAutoSave.setText("Автосохранение: " + count + "/" + selectedQuantityAutoSave);
    }

    //Отслеживать колличество отсканированных штрих-кодов и выводить сообщение при превышении порога
    //Останавливать сканер
    private void timeToSave() {
        if (count >= selectedQuantityAutoSave) {
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
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (resultCode) {
            case RESULT_OK:

                // Удаление отсканированных штрихкодов из SharedPreferences.
                preferences = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(Constants.KEY_FOR_COUNT);
                editor.remove("Status_size");
                editor.apply();

                count = barcodes.size();
                updateInfoTextViews(setSize);
                rvAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_CANCELED:
                Toast.makeText(this, "Данные не сохранены", Toast.LENGTH_SHORT).show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Валидатор
    // todo реализовать валидатор. Соответствует ли отсканированная строка какому-то шаблону
    private boolean validator(String s) {
        return true;
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_button_start_scan:
                addBarcodeReaderFragment();
                break;
            case R.id.image_button_stop_scan:
                addStartFragment();
                break;
        }

    }
}
