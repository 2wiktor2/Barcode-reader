package com.example.testbarcodereader;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> resultsOfScan;
    private HashMap<String, Pair> resultsOfScanMap;


    public RVAdapter( HashMap<String, Pair> resultsOfScanMap, Context context) {
        this.context = context;
        this.resultsOfScanMap = resultsOfScanMap;
    }

    /*    public RVAdapter(ArrayList<String> resultsOfScan, Context context) {
        this.resultsOfScan = resultsOfScan;
        this.context = context;
    }*/

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
        if (position % 2 == 0) {
            holder.constraintLayout.setBackgroundColor(context.getColor(R.color.colorYello));
        } else {
            holder.constraintLayout.setBackgroundColor(context.getColor(R.color.colorGreen));
        }

        //holder.textViewResult.setText(resultsOfScan.get(position));
        holder.textViewResult.setText(resultsOfScanMap.get(position))
    }

    @Override
    public int getItemCount() {
        return resultsOfScan.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout constraintLayout;
        private TextView textViewResult;
        private TextView textViewNumbers;
        private TextView textViewLellers;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constrainLayout);
            textViewResult = itemView.findViewById(R.id.textViewResultOfScan);
            textViewNumbers = itemView.findViewById(R.id.textViewNumbers);
            textViewLellers = itemView.findViewById(R.id.textViewLetters);
        }
    }

}
