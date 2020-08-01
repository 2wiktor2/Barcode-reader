package com.example.testbarcodereader.activities.activityOrder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testbarcodereader.R;
import com.example.testbarcodereader.data.TempHolderTypeAndData;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterForTypeAndCounOfMeters extends RecyclerView.Adapter<AdapterForTypeAndCounOfMeters.ViewHolder> {

    //private HashMap<String, String> hashMapMetersCount;
    private ArrayList<TempHolderTypeAndData> arrayListTempHolder;

    public AdapterForTypeAndCounOfMeters(ArrayList<TempHolderTypeAndData> arrayListTempHolder) {
        this.arrayListTempHolder = arrayListTempHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_for_activity_create_order, parent, false);
        return new AdapterForTypeAndCounOfMeters.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Установка цвета на recyclerView
        holder.changeBackground(position);
        TempHolderTypeAndData tempHolderTypeAndData = arrayListTempHolder.get(position);
        holder.textViewType.setText(tempHolderTypeAndData.getType());
        holder.textViewCount.setText(tempHolderTypeAndData.getCount());
    }

    @Override
    public int getItemCount() {
        return arrayListTempHolder.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout constraintLayout;
        private TextView textViewType;
        private TextView textViewCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.constrainLayout);
            textViewType = itemView.findViewById(R.id.textView_type_in_create_order);
            textViewCount = itemView.findViewById(R.id.textView_count_in_create_order);
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
