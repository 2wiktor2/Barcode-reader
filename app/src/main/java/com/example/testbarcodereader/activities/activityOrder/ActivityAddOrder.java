package com.example.testbarcodereader.activities.activityOrder;

import androidx.appcompat.app.AppCompatActivity;

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

        Button buttonCreateOrder = findViewById(R.id.button_create_order);
        buttonCreateOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ActivityCreateOrder.class);
        startActivity(intent);
    }
}