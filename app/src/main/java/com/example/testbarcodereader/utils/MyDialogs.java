package com.example.testbarcodereader.utils;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.testbarcodereader.R;

public class MyDialogs {

    private Context context;

    public MyDialogs(Context context) {
        this.context = context;
    }

    //Диалог предупреждение о повторном сканировании штрихкода
    public void createWarningDuplicateBarcodeDialog(String rawBarcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Внимамие!!!")
                .setMessage(rawBarcode + "\n"+"Такой штрихкод уже отсканирован.\n")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Диалог предупреждение о несоответствии MAC-адреса
    public void createWarningWrongMacAdresDialog(String rawBarcode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Внимамие!!!")
                .setMessage(rawBarcode + "\n"+"MAC-не соответствует стандарту\nПопробуйте отсканировать штрихкод ещё раз")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


/*
    //Диалог выбора количетва сканируемых штрихкодов
    private void createDialog() {
        final String[] items = {"5", "10", "20", "50"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Максимальное количество штрих-кодов")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        //mDoneButton.setText(items[item]);
                        selectedQuantity = Integer.parseInt(items[item]);
                        textViewCountScannedBarCode.setText(updateInfoText(setSize));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Диалог отчистка списка
    private void clearListOfBarCodeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Удалить все записи?")
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //отчистка списка
                        barcodes.clear();
                        count = barcodes.size();
                        textViewCountScannedBarCode.setText(updateInfoText(setSize));
                        rvAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(R.string.cencel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Диалог предупреждение перед выходом из приложения
    private void createWarningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("У Вас есть несохраненные данные!")
                .setMessage("При выходе из приложения все несохраненные данные удалятся!\nВыйти из приложения?")
                .setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.cencel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
*/

}
