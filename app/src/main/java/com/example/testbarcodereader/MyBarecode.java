package com.example.testbarcodereader;

import java.io.Serializable;

public class MyBarecode implements Serializable {
    private String barcodeResult;
    private int amountOfNumbers;
    private int amountOfLetters;

    public MyBarecode(String barcodeResult, int amountOfNumbers, int amountOfLetters) {
        this.barcodeResult = barcodeResult;
        this.amountOfNumbers = amountOfNumbers;
        this.amountOfLetters = amountOfLetters;
    }

    public String getBarcodeResult() {
        return barcodeResult;
    }

    public int getAmountOfNumbers() {
        return amountOfNumbers;
    }


    public int getAmountOfLetters() {
        return amountOfLetters;
    }

}
