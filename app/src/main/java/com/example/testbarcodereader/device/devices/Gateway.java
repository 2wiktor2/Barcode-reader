package com.example.testbarcodereader.device.devices;

import com.example.testbarcodereader.device.Device;
import com.example.testbarcodereader.device.Validatable;

public class Gateway extends Device implements Validatable {





    @Override
    public boolean validate(String barCode) {
        return false;
    }
}
