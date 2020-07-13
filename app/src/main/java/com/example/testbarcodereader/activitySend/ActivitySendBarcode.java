package com.example.testbarcodereader.activitySend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.Constants;
import com.example.testbarcodereader.model.MyBarcode;
import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activitySend.adapter.RVAdapter2;

import java.util.ArrayList;

public class ActivitySendBarcode extends AppCompatActivity {

    //todo получить список отсканипрованных штрих кодов
    //todo проверить с существующими
    //todo отправить данные

    private ArrayList<MyBarcode> resultsOfScan;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_barecodes);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.KEY_BUNDLE);
        if (bundle != null) {
            resultsOfScan = (ArrayList<MyBarcode>) bundle.getSerializable(Constants.KEY_FOR_SEND_ARRAY_LIST);
        }
        // проверка. Вывод списка в log
        if (resultsOfScan != null) {
            Log.d("qwerty", "Длина = " + resultsOfScan.size());
            for (int i = 0; i < resultsOfScan.size(); i++) {
                MyBarcode b = resultsOfScan.get(i);
                String s = b.getBarcodeResult();
                Log.d("qwerty", "i = " + (i + 1) + "  " + s);
            }
            recyclerView = findViewById(R.id.recyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            RVAdapter2 adapter = new RVAdapter2(resultsOfScan);
            recyclerView.setAdapter(adapter);


        }
    }
}
