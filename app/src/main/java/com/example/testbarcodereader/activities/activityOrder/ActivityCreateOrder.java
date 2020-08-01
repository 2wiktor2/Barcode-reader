package com.example.testbarcodereader.activities.activityOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activities.activitySetCountsOfDevices.ActivitySetCountsOfDevices;

public class ActivityCreateOrder extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCreateOrder;
    private Button buttonTakePicture;
    private Button buttonAddDevices;

    private ImageView imageView_status_number;
    private EditText edit_text_activity_create_order_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        imageView_status_number = findViewById(R.id.imageView_status_number);
        edit_text_activity_create_order_number = findViewById(R.id.edit_text_activity_create_order_number);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getResources().getString(R.string.barcode_scanner));
        setSupportActionBar(toolbar);

        buttonCreateOrder = findViewById(R.id.button_create_order);
        buttonTakePicture = findViewById(R.id.button_take_a_picture);
        buttonAddDevices = findViewById(R.id.button_add_devices);
        buttonCreateOrder.setOnClickListener(this);
        buttonTakePicture.setOnClickListener(this);
        buttonAddDevices.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_create_order:
                changeColorOfIcons(imageView_status_number, edit_text_activity_create_order_number);
                Toast.makeText(this, "Саявка создана\nОтсканируйте устройства!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_take_a_picture:
                Toast.makeText(this, "Фотка заявки", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_add_devices:
                Intent intent = new Intent(this, ActivitySetCountsOfDevices.class);
                startActivity(intent);
                break;
        }
    }

    private void changeColorOfIcons(ImageView imageView, EditText editText) {
        if (!editText.getText().equals("")) {
            imageView.setColorFilter(R.color.colorGreen);
        }
    }

}