package com.example.testbarcodereader.device;

public class ZigBee extends Device implements Validatable{



    @Override
    public boolean validate(String barCode) {
        return false;
    }
}
