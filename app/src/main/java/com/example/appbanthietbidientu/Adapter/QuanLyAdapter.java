package com.example.appbanthietbidientu.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Sanpham;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuanLyAdapter extends FirebaseRecyclerAdapter<Sanpham,QuanLyAdapter.myViewHolder> {
    public QuanLyAdapter(@NonNull FirebaseRecyclerOptions<Sanpham> options) {
        super(options);
    }

    @SuppressLint("RecyclerView")
    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull Sanpham sanpham) {
        holder.tensp.setText(sanpham.getTensanpham());
        holder.giasp.setText(sanpham.getGiasanpham());
        holder.motasp.setText(sanpham.getMotasanpham());

        Glide.with(holder.img.getContext())
                .load(sanpham.getSurl())
                .placeholder(com.google.maps.android.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);


        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.activity_suasp))
                        .setExpanded(true,1200)
                        .create();

                View view = dialogPlus.getHolderView();

                EditText tensp = view.findViewById(R.id.txttensp);
                EditText giasp = view.findViewById(R.id.txtgiasp);
                EditText motasp = view.findViewById(R.id.txtmotasp);
                EditText surl = view.findViewById(R.id.txtimageurl);

                Button btnCapNhat = view.findViewById(R.id.btnCapNhat);
                tensp.setText(sanpham.getTensanpham());
                giasp.setText(sanpham.getGiasanpham());
                motasp.setText(sanpham.getMotasanpham());
                surl.setText(sanpham.getHinhanhsanpham());

                dialogPlus.show();

                btnCapNhat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("tensanpham",tensp.getText().toString());
                        map.put("giasanpham",giasp.getText().toString());
                        map.put("motasanpham",motasp.getText().toString());
                        map.put("hinhanhsanpham",surl.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("sanphammoinhat")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.tensp.getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.tensp.getContext(),"Cập nhật không thành công",Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.tensp.getContext());
                builder.setTitle("Bạn chắc chứ?");
                builder.setMessage("Bạn không thể khôi phục sau khi xóa.");

                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       FirebaseDatabase.getInstance().getReference().child("sanphamoinhat")
                               .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.tensp.getContext(),"Đã hủy bỏ", Toast.LENGTH_SHORT).show();
                    }
                });
                        builder.show();

            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,parent,false);
        return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder{
        CircleImageView img;
        TextView tensp,giasp,motasp;

        Button btnSua,btnXoa, btnCapNhat;
        public myViewHolder(@NonNull View itemView){
            super(itemView);
            img = (CircleImageView) itemView.findViewById(R.id.img1);
            tensp = (TextView)itemView.findViewById(R.id.tensp);
            giasp = (TextView)itemView.findViewById(R.id.giasp);
            motasp = (TextView)itemView.findViewById(R.id.motasp);

            btnSua = (Button) itemView.findViewById(R.id.btnSua);
            btnXoa =(Button) itemView.findViewById(R.id.btnXoa);
        }
    }
}
