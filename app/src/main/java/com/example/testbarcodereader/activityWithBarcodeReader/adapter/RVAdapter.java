package com.example.testbarcodereader.activityWithBarcodeReader.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.data.MyBarcode;
import com.example.testbarcodereader.R;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private ArrayList<MyBarcode> resultsOfScan;

    public RVAdapter(ArrayList<MyBarcode> resultsOfScan) {
        this.resultsOfScan = resultsOfScan;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Установка цвета на recyclerView
        holder.changeBackground(position);

        MyBarcode barcode = resultsOfScan.get(position);
        holder.textViewResult.setText(barcode.getBarcodeResult());
        holder.textViewNumbers.setText(String.valueOf(barcode.getAmountOfNumbers()));
        holder.textViewLetters.setText(String.valueOf(barcode.getAmountOfLetters()));
    }

    @Override
    public int getItemCount() {
        return resultsOfScan.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout constraintLayout;
        private TextView textViewResult;
        private TextView textViewNumbers;
        private TextView textViewLetters;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constrainLayout);
            textViewResult = itemView.findViewById(R.id.textViewResultOfScan);
            textViewNumbers = itemView.findViewById(R.id.textViewNumbers);
            textViewLetters = itemView.findViewById(R.id.textViewLetters);
        }

        private void changeBackground(int position) {
            Context context = itemView.getContext();
            if (position % 2 == 0) {
                constraintLayout.setBackgroundColor(context.getColor(R.color.colorGray));
            } else {
                constraintLayout.setBackgroundColor(context.getColor(R.color.colorGrayLight));
            }
        }
    }

}
