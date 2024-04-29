package com.example.appbanthietbidientu.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanthietbidientu.R;

public class GioHangViewHolder extends RecyclerView.ViewHolder {
    public ImageView imageProduct;
    public TextView textProductName;
    public TextView textQuantity;
    public TextView textPrice;
    public TextView textTotalPrice;

    public GioHangViewHolder(@NonNull View itemView) {
        super(itemView);
        imageProduct = itemView.findViewById(R.id.imageProduct);
        textProductName = itemView.findViewById(R.id.textProductName);
        textQuantity = itemView.findViewById(R.id.textQuantity);
        textPrice = itemView.findViewById(R.id.textPrice);
        textTotalPrice = itemView.findViewById(R.id.textTotalPrice);
    }
}
