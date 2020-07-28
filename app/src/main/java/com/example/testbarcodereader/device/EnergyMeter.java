package com.example.testbarcodereader.device;

public class EnergyMeter extends Device implements Validatable{


    @Override
    public boolean validate(String barCode) {
        return false;
    }
}
