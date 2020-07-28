package com.example.testbarcodereader.device;

public class Gateway extends Device implements Validatable{





    @Override
    public boolean validate(String barCode) {
        return false;
    }
}
