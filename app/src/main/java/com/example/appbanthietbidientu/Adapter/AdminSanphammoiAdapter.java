package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanthietbidientu.Activity.AdminDetailActivity;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.model.Sanphammoi;

import java.util.ArrayList;
import java.util.List;

public class AdminSanphammoiAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context context;
    private List<Sanpham> dataList;
    public AdminSanphammoiAdapter(Context context, List<Sanpham> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_recycler_item, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getHinhanhsanpham()).into(holder.recImage);
        holder.recTitle.setText(dataList.get(position).getTensanpham());
        holder.recDesc.setText(dataList.get(position).getMotasanpham());
        holder.recPrice.setText(String.valueOf(dataList.get(position).getGiasanpham()));
        holder.recID.setText(String.valueOf(dataList.get(position).getId()));
        holder.recIdsanpham.setText(String.valueOf(dataList.get(position).getIdsanpham()));
        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AdminDetailActivity.class);
                intent.putExtra("Image", dataList.get(holder.getAdapterPosition()).getHinhanhsanpham());
                intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getMotasanpham());
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getTensanpham());
                intent.putExtra("ID", dataList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("IDSanPham", dataList.get(holder.getAdapterPosition()).getIdsanpham());
//                intent.putExtra("Key",dataList.get(holder.getAdapterPosition()).getId());
                intent.putExtra("Price", dataList.get(holder.getAdapterPosition()).getGiasanpham());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void searchDataList(ArrayList<Sanpham> searchList){
        dataList = searchList;
        notifyDataSetChanged();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    ImageView recImage;
    TextView recTitle, recDesc, recPrice, recID, recIdsanpham;
    CardView recCard;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recDesc = itemView.findViewById(R.id.recDesc);
        recPrice = itemView.findViewById(R.id.recPrice);
        recTitle = itemView.findViewById(R.id.recTitle);
        recID = itemView.findViewById(R.id.recid);
        recIdsanpham = itemView.findViewById(R.id.recidsanpham);
    }
}
