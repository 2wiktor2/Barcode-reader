package com.example.testbarcodereader.device.meters;

import com.example.testbarcodereader.device.Meter;
import com.example.testbarcodereader.device.Validatable;

public class ZigBee extends Meter implements Validatable {

    @Override
    public boolean validate(String barCode) {
        return false;
    }
}
