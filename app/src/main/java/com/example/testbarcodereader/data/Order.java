package com.example.testbarcodereader.data;

import com.example.testbarcodereader.device.Meter;

import java.util.ArrayList;

public class Order {
    private String orderDate;
    private int orderNumber;
    private String orderSender;
    private String orderGetter;
    private String orderSend;
    private String orderGet;
    private ArrayList<Meter> metersInOrder;
}
