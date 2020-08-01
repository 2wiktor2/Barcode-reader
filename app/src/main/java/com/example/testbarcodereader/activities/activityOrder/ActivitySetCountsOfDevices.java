package com.example.testbarcodereader.activities.activityOrder;

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
import com.example.testbarcodereader.utils.Constants;

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
        if (!editTextCountOfDevices.getText().toString().equals("")) {
            String type = spinnerTypeOfDevise.getSelectedItem().toString();
            String count = editTextCountOfDevices.getText().toString();
            sendMessage(type, count);
        } else {
            Toast.makeText(this, "Укажите количество!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessage(String type, String count) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_FOR_TYPE_OF_METER, type);
        bundle.putString(Constants.KEY_FOR_COUNT_OF_METERS, count);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
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