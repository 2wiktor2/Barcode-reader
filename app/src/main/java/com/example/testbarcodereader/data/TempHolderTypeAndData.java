package com.example.testbarcodereader.data;

public class TempHolderTypeAndData {
    private String type;
    private String count;

    public TempHolderTypeAndData(String type, String count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
