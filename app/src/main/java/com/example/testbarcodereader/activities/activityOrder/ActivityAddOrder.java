package com.example.testbarcodereader.activities.activityOrder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.testbarcodereader.R;

public class ActivityAddOrder extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(getResources().getString(R.string.barcode_scanner));
        setSupportActionBar(toolbar);

        Button buttonCreateOrder = findViewById(R.id.button_create_order);
        buttonCreateOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ActivityCreateOrder.class);
        startActivity(intent);
    }
}