package com.example.testbarcodereader.activitySend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activitySend.adapter.RVAdapter2;
import com.example.testbarcodereader.data.MyBarcode;
import com.example.testbarcodereader.utils.Constants;

import java.util.ArrayList;

public class ActivitySendBarcode extends AppCompatActivity implements View.OnClickListener {

    //todo получить список отсканипрованных штрих кодов
    //todo проверить с существующими
    //todo отправить данные
    //todo При отправке данных и закрытии второго активити отчищать список в первом активити

    private ArrayList<MyBarcode> resultsOfScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_barecodes);

        Toolbar toolbar = findViewById(R.id.toolbar_activity_send);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button buttonCheckAndSend = findViewById(R.id.button_check_and_send);

        buttonCheckAndSend.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(Constants.KEY_BUNDLE);
        if (bundle != null) {
            resultsOfScan = (ArrayList<MyBarcode>) bundle.getSerializable(Constants.KEY_FOR_SEND_ARRAY_LIST);
        }

        // проверка. Вывод списка в log
        if (resultsOfScan != null) {
            Log.d("qwertyu", "Длина = " + resultsOfScan.size());
            for (int i = 0; i < resultsOfScan.size(); i++) {
                MyBarcode b = resultsOfScan.get(i);
                String s = b.getBarcodeResult();
                Log.d("qwerty", "i = " + (i + 1) + "  " + s);
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            RVAdapter2 adapter = new RVAdapter2(resultsOfScan);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        setResult(RESULT_OK);
        finish();
/*        if (validator()) {
            Toast.makeText(this, "Данные успешно отправлены", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Данные не отправлены", Toast.LENGTH_SHORT).show();
        }*/
    }

    //Валидатор
    // todo реализовать валидатор. Есть ли такие данные на сервере
    private boolean validator() {
        return true;
    }
}
