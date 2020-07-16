package com.example.testbarcodereader.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyBarcode implements Serializable {
    @SerializedName("barcodeResult")
    private String barcodeResult;
    @SerializedName("amountOfNumbers")
    private int amountOfNumbers;
    @SerializedName("amountOfLetters")
    private int amountOfLetters;

    public MyBarcode(String barcodeResult, int amountOfNumbers, int amountOfLetters) {
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
