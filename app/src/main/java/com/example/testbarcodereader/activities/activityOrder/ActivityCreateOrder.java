package com.example.testbarcodereader.activities.activityOrder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activities.activityOrder.adapter.AdapterForTypeAndCounOfMeters;
import com.example.testbarcodereader.data.MyBarcode;
import com.example.testbarcodereader.data.TempHolderTypeAndData;
import com.example.testbarcodereader.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class ActivityCreateOrder extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCreateOrder;
    private Button buttonTakePicture;
    private Button buttonAddDevices;

    private ImageView imageView_status_number;
    private EditText edit_text_activity_create_order_number;

    private HashMap<String, String> hashMapMetersCount = new HashMap<>();
    private ArrayList<TempHolderTypeAndData> arrayListTempHolder = new ArrayList<>();

    private RecyclerView recyclerView;
    private AdapterForTypeAndCounOfMeters adapter;

    private SharedPreferences preferences;

    private HashSet setOfMeter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        imageView_status_number = findViewById(R.id.imageView_status_number);
        edit_text_activity_create_order_number = findViewById(R.id.edit_text_activity_create_order_number);
        recyclerView = findViewById(R.id.recyclerView_meters);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getResources().getString(R.string.barcode_scanner));
        setSupportActionBar(toolbar);

        buttonCreateOrder = findViewById(R.id.button_create_order);
        buttonTakePicture = findViewById(R.id.button_take_a_picture);
        buttonAddDevices = findViewById(R.id.button_add_devices);
        buttonCreateOrder.setOnClickListener(this);
        buttonTakePicture.setOnClickListener(this);
        buttonAddDevices.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AdapterForTypeAndCounOfMeters(arrayListTempHolder);
        recyclerView.setAdapter(adapter);

        setOfMeter = new HashSet();

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_add_devices:
                Intent intent = new Intent(this, ActivitySetCountsOfDevices.class);
                startActivityForResult(intent, Constants.REQUEST_ACCESS_TYPE);
                break;
            case R.id.button_take_a_picture:
                Toast.makeText(this, "Фотка заявки", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_create_order:
                changeColorOfIcons(imageView_status_number, edit_text_activity_create_order_number);
                Toast.makeText(this, "Саявка создана\nОтсканируйте устройства!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_ACCESS_TYPE) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra(Constants.KEY_FOR_BUNDLE_FOR_SEND_TYPE_AND_COUNT_OF_METERS)) {
                    Bundle bundle = data.getExtras();
                    String type = bundle.getString(Constants.KEY_FOR_TYPE_OF_METER);
                    String count = bundle.getString(Constants.KEY_FOR_COUNT_OF_METERS);
                    TempHolderTypeAndData tempHolderTypeAndData = new TempHolderTypeAndData(type, count);
                    arrayListTempHolder.add(tempHolderTypeAndData);
                    hashMapMetersCount.put(type, count);
                    Log.i("qwertyu", "arrayListTempHolder = " + arrayListTempHolder.size());
                    Log.i("qwertyu", "hashMapMetersCount = " + hashMapMetersCount.size());
                    adapter.notifyDataSetChanged();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Сохранение данных в sharedPreferences
    private void saveData() {
/*        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("Status_size", arrayListTempHolder.size());
        for (int i = 0; i < arrayListTempHolder.size(); i++) {
            TempHolderTypeAndData tempHolderTypeAndData = arrayListTempHolder.get(i);
            String s = tempHolderTypeAndData.getType();
            editor.remove("Status_" + i);
            editor.putString("Status_" + i, s);
        }
        editor.putStringSet(Constants.KEY_FOR_SET, setOfMeter);
        editor.apply();*/
    }


    //Загрузка данных из sharedPreferences
    private void loadData() {
        Log.i("qwertyu", "Загрузка данных из sharedPreferences");
    }

    private void changeColorOfIcons(ImageView imageView, EditText editText) {
        if (!editText.getText().equals("")) {
            imageView.setColorFilter(R.color.colorGreen);
        }
    }
}