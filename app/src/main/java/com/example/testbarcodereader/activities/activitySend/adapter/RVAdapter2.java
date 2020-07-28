package com.example.testbarcodereader.activities.activitySend.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.data.MyBarcode;
import com.example.testbarcodereader.R;

import java.util.ArrayList;

public class RVAdapter2 extends RecyclerView.Adapter<RVAdapter2.ViewHolder> {

    private ArrayList<MyBarcode> barcodes;


    public RVAdapter2(ArrayList<MyBarcode> barcodes) {
        this.barcodes = barcodes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_for_activity_send, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyBarcode barcode = barcodes.get(position);
        holder.textViewRawBarcode.setText(barcode.getBarcodeResult());
    }

    @Override
    public int getItemCount() {
        return barcodes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewRawBarcode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRawBarcode = itemView.findViewById(R.id.rawBarCode);
        }
    }
}
