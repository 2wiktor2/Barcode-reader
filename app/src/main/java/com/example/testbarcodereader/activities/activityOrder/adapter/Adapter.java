package com.example.testbarcodereader.activities.activityOrder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.activities.activityesWithBarcodeReader.adapter.RVAdapter;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_for_activity_create_order, parent, false);
        return new Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Установка цвета на recyclerView
        holder.changeBackground(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constrainLayout);
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
