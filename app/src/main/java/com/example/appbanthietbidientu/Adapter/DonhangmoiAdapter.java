package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanthietbidientu.Activity.ChiTietDonHangActivity;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.DonHang;
import com.example.appbanthietbidientu.ultil.CheckConnect;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class DonhangmoiAdapter extends RecyclerView.Adapter<DonhangmoiAdapter.DonHangViewHolder> {
    private List<DonHang> donhangList;
    private Context context;

    public DonhangmoiAdapter(List<DonHang> donhangList, Context context) {
        this.donhangList = donhangList;
        this.context = context;
    }

    @NonNull
    @Override
    public DonHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new DonHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangViewHolder holder, int position) {
        DonHang donHang = donhangList.get(position);
        holder.txtIdDonHang.setText("ID Đơn hàng: " + donHang.getId());
        holder.txtIdUser.setText("ID Khách hàng: " + donHang.getUser());
        holder.txtQt.setText("Số lượng sản phẩm: " + donHang.getDanhSachSanPham().size());
        DecimalFormat decimalFormat = new DecimalFormat("$###,###,###");
        holder.txtTotal.setText("Tổng tiền: " + decimalFormat.format(donHang.getTotal()));
        holder.txtNgayDat.setText("Ngày đặt hàng: " + donHang.getThoiGian());

        // Nếu cần, bạn có thể sử dụng Picasso để tải ảnh hình minh họa cho đơn hàng ở đây
        // Picasso.get().load(donHang.getHinhDonHang()).placeholder(R.drawable.placeholder_image).into(holder.imgDonHang);
    }

    @Override
    public int getItemCount() {
        return donhangList.size();
    }

    public class DonHangViewHolder extends RecyclerView.ViewHolder {
        private TextView txtIdDonHang, txtIdUser, txtQt, txtTotal, txtNgayDat;
        private ImageView imgDonHang;

        public DonHangViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdDonHang = itemView.findViewById(R.id.idDonHang);
            txtIdUser = itemView.findViewById(R.id.idUser);
            txtQt = itemView.findViewById(R.id.idQt);
            txtTotal = itemView.findViewById(R.id.idTotal);
            txtNgayDat = itemView.findViewById(R.id.idNgayDat);

            // Xử lý sự kiện khi người dùng nhấp vào một mục đơn hàng
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Chuyển đến màn hình chi tiết đơn hàng khi nhấp vào
                    Intent intent = new Intent(context, ChiTietDonHangActivity.class);
                    intent.putExtra("thongtindonhang", donhangList.get(getAdapterPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CheckConnect.ShowToast_Short(context,donhangList.get(getPosition()).getId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
