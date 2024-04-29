package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.GioHang;
import com.example.appbanthietbidientu.model.GioHangViewHolder;
import com.squareup.picasso.Picasso;
import java.text.DecimalFormat;
import java.util.List;

public class GioHangRecyclerViewAdapter extends RecyclerView.Adapter<GioHangViewHolder> {
    private Context context;
    private List<GioHang> gioHangList;

    public GioHangRecyclerViewAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public GioHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang_sanphamchitiet, parent, false);
        return new GioHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GioHangViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);

        // Gán dữ liệu vào các thành phần giao diện
        holder.textProductName.setText(gioHang.getTensp());
        holder.textQuantity.setText("Qt: " + gioHang.getSoluongsp());
        holder.textPrice.setText("Price: $" + gioHang.getGiasp());
        holder.textTotalPrice.setText("Total Price: $" + gioHang.getGiasp() * gioHang.getSoluongsp());

        // Load hình ảnh bằng Picasso hoặc Glide (tùy bạn)
        Picasso.get().load(gioHang.getHinhsp()).into(holder.imageProduct);
    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textProductName;
        public TextView textQuantity;
        public TextView textPrice;
        public TextView textTotalPrice;
        public ImageView imageProduct;

        public ViewHolder(View itemView) {
            super(itemView);
            // Ánh xạ các thành phần giao diện từ itemView
            textProductName = itemView.findViewById(R.id.textProductName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            textPrice = itemView.findViewById(R.id.textPrice);
            textTotalPrice = itemView.findViewById(R.id.textTotalPrice);
            imageProduct = itemView.findViewById(R.id.imageProduct);
        }
    }
}
