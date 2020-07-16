package com.example.testbarcodereader.utils;

import com.example.testbarcodereader.data.MyBarcode;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Converter {

    //Подключить библиотеку
//    implementation 'com.google.code.gson:gson:2.8.5'

    //Пометить все поля, в классе MyBarcode, аннотацией @SerializedName("barcodeResult")

    //каждый метод в классе Converter нужно пометить аннотацией
    //@TypeConverter из библиотеки room. Видимо для работы с базой данных???????????????????????????

    //Преобразование arrayList штрихкодов в строку
    public String arrayListBarcodesToString(ArrayList<MyBarcode> myBarcodeArrayList) {

            /*        //Длинный вариант:

                    // каждый штрихкод преобразуется в JSONArray и кладется в этот JSONArray
                    JSONArray jsonArray = new JSONArray();
                    for (MyBarcode myBarcode : myBarcodeArrayList) {
                        //Создаем JSONObject и кладем туда значения. потом обернуть в try/catch

                        JSONObject jsonObject = new JSONObject();
                        try {
                            //поле name должно соответствовать имени в аннотации  @SerializedName в классе MyBarcode
                            jsonObject.put("barcodeResult", myBarcode.getBarcodeResult());
                            jsonObject.put("amountOfNumbers", myBarcode.getAmountOfNumbers());
                            jsonObject.put("amountOfLetters", myBarcode.getAmountOfLetters());

                            //Кладем в jsonArray получившийся jsonObject
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return jsonArray.toString();*/


        //короткий вариант:
        //Все тоже самое может сделать GSon.
        //Создать объект GSon и поместить туда список
        return new Gson().toJson(myBarcodeArrayList);
    }

    //метод преобразует строку обратно в ArrayList<MyBarcode>
    public ArrayList<MyBarcode> stringToArrayListMyBarcodes(String myBarcodesAsString) {
        Gson gson = new Gson();
        // при преобразовании нельзя указывать параметризировенный тип
        //ArrayList<MyBarcode> myBarcodes = gson.fromJson(myBarcodesAsString, ArrayList<MyBarcode>.class);

        // Поэтому убераем параметр у ArrayList '<MyBarcode>'. Теперь это ArrayList общего типа Object.
        ArrayList objects = gson.fromJson(myBarcodesAsString, ArrayList.class);
        // Теперь приводим ArrayList типа Object к ArrayList типа MyBarCode
        ArrayList<MyBarcode> myBarcodes = new ArrayList<>();
        //теперь нужно поместить ArrayList типа Object в ArrayList типа MyBarCode в цикле
        for (Object o : objects) {

            myBarcodes.add(gson.fromJson(o.toString(), MyBarcode.class));
        }
        return myBarcodes;
    }
}
