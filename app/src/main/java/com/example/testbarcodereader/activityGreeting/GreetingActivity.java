package com.example.testbarcodereader.activityGreeting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activityWithBarcodeReader.MainActivity;

public class GreetingActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // todo Выключать вспышку при выходе из приложения

    private Switch flashLightSwitch;
    private Button buttonScanGateway;
    private Button buttonScanEnergyMeter;
    private Button buttonScanZigBee;
    private Button buttonScanRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_scan_gateway:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_scan_energy_meter:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.button_scan_zigBee:
                Intent intent3 = new Intent(this, MainActivity.class);
                startActivity(intent3);
                break;
            case R.id.button_scan_router:
                Intent intent4 = new Intent(this, MainActivity.class);
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
}
