package com.example.testbarcodereader.device;

public class Router extends Device implements Validatable {
    @Override
    public boolean validate(String barCode) {
        return false;
    }
}
