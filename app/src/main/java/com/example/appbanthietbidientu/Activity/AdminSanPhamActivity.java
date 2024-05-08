package com.example.appbanthietbidientu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appbanthietbidientu.Adapter.SanphammoiAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Loaisp;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.ultil.ApiSp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSanPhamActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView listSanPhamMoi;
    Button btnadd, btnedit, btndelete;
    EditText editProductName,editProductPrice, editProductImage,editProductDescription;
    ArrayList<Loaisp> loaispArrayList;
    ArrayList<Sanpham> sanphamArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_san_pham);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminSanPhamActivity.this, AdminUploadActivity.class);
                startActivity(intent);
            }
        });
    }

}

