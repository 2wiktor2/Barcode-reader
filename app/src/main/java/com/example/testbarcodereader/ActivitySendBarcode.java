package com.example.testbarcodereader;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ActivitySendBarcode extends AppCompatActivity {

    //todo получить список отсканипрованных штрих кодов
    //todo проверить с существующими
    //todo отправить данные

    private ArrayList<MyBarecode> resultsOfScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_barecodes);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
        if (bundle != null) {
            resultsOfScan = (ArrayList<MyBarecode>) bundle.getSerializable("ARRAY_LIST");
        }
        // проверка. Вывод списка в log
        if (resultsOfScan != null) {
            Log.d("qwerty", "Длина = " + resultsOfScan.size());
            for (int i = 0; i < resultsOfScan.size(); i++) {
                MyBarecode b = resultsOfScan.get(i);
                String s = b.getBarcodeResult();
                Log.d("qwerty", s);
            }
        }
    }
}
