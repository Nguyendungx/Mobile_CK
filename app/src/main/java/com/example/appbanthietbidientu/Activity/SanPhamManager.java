package com.example.appbanthietbidientu.Activity;

import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanthietbidientu.Adapter.SanphammoiAdapter;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.ultil.ApiSp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SanPhamManager extends AppCompatActivity {
    private SanphammoiAdapter adapter;
    private Context context;



    public SanPhamManager(SanphammoiAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }


    public void addSanPham(Sanpham sanpham) {
        Call<Sanpham> call = ApiSp.apiDevice.addSanPham(sanpham);
        call.enqueue(new Callback<Sanpham>() {
            @Override
            public void onResponse(Call<Sanpham> call, Response<Sanpham> response) {
                if (response.isSuccessful()) {
                    adapter.addSanPham(response.body());
                    Toast.makeText(context, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Thêm sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Sanpham> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateSanPham(int position, Sanpham sanpham) {
        Call<Void> call = ApiSp.apiDevice.updateSanPham(String.valueOf(sanpham.getId()), sanpham);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.updateSanPham(position, sanpham);
                    Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cập nhật sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteSanPham(int position, Sanpham sanpham) {
        Call<Void> call = ApiSp.apiDevice.deleteSanPham(String.valueOf(sanpham.getId()));
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.deleteSanPham(position);
                    Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Xóa sản phẩm không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

