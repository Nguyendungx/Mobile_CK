package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.ContextMenu;
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

import com.androidnetworking.utils.Utils;
import com.example.appbanthietbidientu.Activity.ChiTietSanPhamActivity;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.EventBus.SuaXoaEvent;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.model.Sanphammoi;
import com.example.appbanthietbidientu.ultil.CheckConnect;
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.eventbus.EventBus;
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
        if (sanpham.getHinhanhsanpham().contains("http")) {
            Glide.with(context).load(sanpham.getHinhanhsanpham()).into(holder.imgSp);
        }else {
            String hinh = Utils.BASE_URL+"images/"+sanpham.getHinhanhsanpham();
            Glide.with(context).load(hinh()).into(holder.imgSp);
        }

        Typeface semibold = ResourcesCompat.getFont(context,R.font.svn_gilroy_semibold);
        holder.txtTensp.setTypeface(semibold);

        Picasso.with(context).load(sanpham.getHinhanhsanpham())
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

    public class SanphamViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener, View.OnLongClickListener {
        private ImageView imgSp;
        private TextView txtTensp,txtGiasp;

        public SanphamViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSp=itemView.findViewById(R.id.img_sp);
            txtTensp=itemView.findViewById(R.id.txtTensp);
            txtGiasp=itemView.findViewById(R.id.txtGiasp);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!isLongClick){
                        //click
                        Intent intent=new Intent(context, ChiTietSanPhamActivity.class);
                        intent.putExtra("thongtinsanpham",sanphamList.get(getPosition()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        CheckConnect.ShowToast_Short(context,sanphamList.get(getPosition()).getTensanpham());
                        context.startActivity(intent);
                    }else{
                        EventBus.getDefault().postSticky(new SuaXoaEvent(sanphammoi));
                    }

                }
            });
        }

        public setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0,0,getAdapterPosition(), "Sửa");
            contextMenu.add(0,0,getAdapterPosition(), "Xóa");
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
            return false;
        }
    }


}
