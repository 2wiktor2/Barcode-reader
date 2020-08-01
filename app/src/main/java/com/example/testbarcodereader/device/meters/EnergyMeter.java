package com.example.testbarcodereader.device.meters;

import com.example.testbarcodereader.device.Meter;
import com.example.testbarcodereader.device.Validatable;

public class EnergyMeter extends Meter implements Validatable {

    private String barCodeSecond;

    @Override
    public boolean validate(String barCode) {
        return true;
    }

    public boolean validateSecondBarCode(String barCodeSecond) {
        return true;
    }
}
