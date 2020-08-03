package com.example.testbarcodereader.activities.activityChooseTypeOfScan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activities.activityesWithBarcodeReader.MainActivity;
import com.example.testbarcodereader.activities.activityesWithBarcodeReader.MainActivity2;
import com.example.testbarcodereader.activities.activityesWithBarcodeReader.MainActivity3;
import com.example.testbarcodereader.activities.activityesWithBarcodeReader.MainActivity4;
import com.example.testbarcodereader.utils.MyDialogs;

public class ActivityChooseTypeOfScan extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // todo Выключать вспышку при выходе из приложения

    private Switch flashLightSwitch;
    private Button buttonScanGateway;
    private Button buttonScanEnergyMeter;
    private Button buttonScanZigBee;
    private Button buttonScanRouter;

    private MyDialogs myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type_of_scan);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getResources().getString(R.string.barcode_scanner));
        setSupportActionBar(toolbar);

        buttonScanGateway = findViewById(R.id.button_scan_gateway);
        buttonScanEnergyMeter = findViewById(R.id.button_scan_energy_meter);
        buttonScanZigBee = findViewById(R.id.button_scan_zigBee);
        buttonScanRouter = findViewById(R.id.button_scan_router);

        buttonScanGateway.setOnClickListener(this);
        buttonScanEnergyMeter.setOnClickListener(this);
        buttonScanZigBee.setOnClickListener(this);
        buttonScanRouter.setOnClickListener(this);

        flashLightSwitch = findViewById(R.id.switcher_flash_light);
        flashLightSwitch.setOnCheckedChangeListener(this);

        myDialog = new MyDialogs(this);


        //тестовый краш
/*        Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_scan_gateway:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_scan_energy_meter:
                Intent intent2 = new Intent(this, MainActivity2.class);
                startActivity(intent2);
                break;
            case R.id.button_scan_zigBee:
                Intent intent3 = new Intent(this, MainActivity3.class);
                startActivity(intent3);
                break;
            case R.id.button_scan_router:
                Intent intent4 = new Intent(this, MainActivity4.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //todo Включить вспышку
        } else {
            //todo Выключить вспышку
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //todo выключить вспышку
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_memu_in_greeting_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                myDialog.createInfoDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}