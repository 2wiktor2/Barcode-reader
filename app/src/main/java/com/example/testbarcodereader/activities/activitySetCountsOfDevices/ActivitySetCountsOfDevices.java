package com.example.testbarcodereader.activities.activitySetCountsOfDevices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activities.activityOrder.ActivityCreateOrder;

public class ActivitySetCountsOfDevices extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinnerTypeOfDevise;
    private EditText editTextCountOfDevices;
    private Button buttonAddToOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_coutns_of_devices);

        spinnerTypeOfDevise = findViewById(R.id.spinner);
        editTextCountOfDevices = findViewById(R.id.edit_text_count);
        buttonAddToOrder = findViewById(R.id.button_add_to_order);
        buttonAddToOrder.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.meter_type_array,
                R.layout.support_simple_spinner_dropdown_item);
        spinnerTypeOfDevise.setOnItemSelectedListener(this);
        spinnerTypeOfDevise.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ActivityCreateOrder.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String s = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this, "Выбран " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}