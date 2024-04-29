package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanthietbidientu.Activity.ChiTietSanPhamActivity;
import com.example.appbanthietbidientu.Activity.SanPhamManager;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.model.Sanphammoi;
import com.example.appbanthietbidientu.ultil.CheckConnect;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class SanphammoiAdapter extends RecyclerView.Adapter<SanphammoiAdapter.SanphamViewHolder> {
    List<Sanpham> sanphamList;
    Context context;

    public SanphammoiAdapter(List<Sanpham> sanphamList, Context context) {
        this.sanphamList = sanphamList;
        this.context = context;
    }

    public void addSanPham(Sanpham body) {
        SanphammoiAdapter sanphammoiAdapter = new SanphammoiAdapter(sanphamList, context);
        SanPhamManager sanphamManager = new SanPhamManager(sanphammoiAdapter, context);
        // Thêm sản phẩm
        Sanpham newSanpham = new Sanpham(0, "Tên sản phẩm", 10000, "URL_hình_ảnh", "Mô tả sản phẩm", 0);
        sanphamManager.addSanPham(newSanpham);
    }

    public void updateSanPham(int position, Sanpham sanpham) {
        SanphammoiAdapter sanphammoiAdapter = new SanphammoiAdapter(sanphamList, context);
        SanPhamManager sanphamManager = new SanPhamManager(sanphammoiAdapter, context);
        // Sửa sản phẩm
        Sanpham updatedSanpham = sanphamList.get(position);
        updatedSanpham.setTensanpham("Tên sản phẩm mới");
        updatedSanpham.setGiasanpham(20000); // Giả sử giá mới là 20000
        sanphamManager.updateSanPham(position, updatedSanpham);
    }

    public void deleteSanPham(int position) {
        SanphammoiAdapter sanphammoiAdapter = new SanphammoiAdapter(sanphamList, context);
        SanPhamManager sanphamManager = new SanPhamManager(sanphammoiAdapter, context);
        // Xóa sản phẩm
        Sanpham sanphamToDelete = sanphamList.get(position);
        sanphamManager.deleteSanPham(position, sanphamToDelete);
    }

    @NonNull
    @Override
    public SanphamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanpham_moinhat,parent,false);
        return new SanphamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SanphamViewHolder holder, int position) {
        Sanpham sanpham = sanphamList.get(position);
        holder.txtTensp.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiasp.setText("Giá: " + decimalFormat.format(sanpham.getGiasanpham()) + "₫");
        Typeface semibold = ResourcesCompat.getFont(context,R.font.svn_gilroy_semibold);
        holder.txtTensp.setTypeface(semibold);

        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.loadimage)
                .error(R.drawable.errorimage)
                .into(holder.imgSp);
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_spmoi);
//        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return sanphamList.size();
    }

    public class SanphamViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSp;
        private TextView txtTensp,txtGiasp;

        public SanphamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSp=itemView.findViewById(R.id.img_sp);
            txtTensp=itemView.findViewById(R.id.txtTensp);
            txtGiasp=itemView.findViewById(R.id.txtGiasp);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ChiTietSanPhamActivity.class);
                    intent.putExtra("thongtinsanpham",sanphamList.get(getPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CheckConnect.ShowToast_Short(context,sanphamList.get(getPosition()).getTensanpham());
                    context.startActivity(intent);
                }
            });
        }
    }
}
