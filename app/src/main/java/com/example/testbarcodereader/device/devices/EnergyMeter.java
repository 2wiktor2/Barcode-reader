package com.example.testbarcodereader.device.devices;

import com.example.testbarcodereader.device.Device;
import com.example.testbarcodereader.device.Validatable;

public class EnergyMeter extends Device implements Validatable {

    private String barCodeSecond;

    @Override
    public boolean validate(String barCode) {
        return true;
    }

    public boolean validateSecondBarCode(String barCodeSecond) {
        return true;
    }
}
