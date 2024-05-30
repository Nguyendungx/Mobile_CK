package com.example.appbanthietbidientu.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appbanthietbidientu.Adapter.AdminSanphammoiAdapter;
import com.example.appbanthietbidientu.Adapter.SanphammoiAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Loaisp;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.model.Sanphammoi;
import com.example.appbanthietbidientu.ultil.ApiSp;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSanPhamActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<Sanpham> dataList;
    AdminSanphammoiAdapter adapter;
    SearchView searchView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_san_pham);
        toolbar =findViewById(R.id.ToolbarDanhSachSP);
        fab = findViewById(R.id.fab);
        loadsp();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminSanPhamActivity.this, AdminUploadActivity.class);
                startActivity(intent);
            }
        });
        ActionBar();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R   .drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadsp() {
        recyclerView = findViewById(R.id.recyclerView);

        // Search edttext
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AdminSanPhamActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminSanPhamActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        dataList = new ArrayList<>();
        
        adapter = new AdminSanphammoiAdapter(AdminSanPhamActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("sanphammoinhat");
        dialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long spCount = snapshot.getChildrenCount();
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
//                    Sanphammoi sanphammoi = itemSnapshot.getValue(Sanphammoi.class);
//                    dataClass.setKey(itemSnapshot.getKey());
                    int id = Integer.parseInt(itemSnapshot.child("id").getValue(String.class)) ;
                    String tensanpham = itemSnapshot.child("tensanpham").getValue(String.class);
                    int giasanpham = Integer.parseInt(itemSnapshot.child("giasanpham").getValue(String.class));
                    String hinhanhsanpham = itemSnapshot.child("hinhanhsanpham").getValue(String.class);
                    String motasanpham = itemSnapshot.child("motasanpham").getValue(String.class);
                    int idsanpham = Integer.parseInt(itemSnapshot.child("idsanpham").getValue(String.class));

                    // Tạo đối tượng Sanphammoi từ dữ liệu và thêm vào danh sách
                    Sanpham sanphammoi = new Sanpham(id, tensanpham, giasanpham, hinhanhsanpham, motasanpham, idsanpham);

                    dataList.add(sanphammoi);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
                dialog.dismiss();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
    }

    private void searchList(String newText) {
        ArrayList<Sanpham> searchList = new ArrayList<>();
        for (Sanpham sanpham: dataList){
            if (sanpham.getTensanpham().toLowerCase().contains(newText.toLowerCase())){
                searchList.add(sanpham);
            }
        }
        adapter.searchDataList(searchList);
    }

}

