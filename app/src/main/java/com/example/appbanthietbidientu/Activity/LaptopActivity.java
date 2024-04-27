package com.example.appbanthietbidientu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import java.io.Serializable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanthietbidientu.Adapter.SanphamAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.ultil.ApiSp;
import com.example.appbanthietbidientu.ultil.BaseFunctionActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaptopActivity extends BaseFunctionActivity {
    SearchView searchView;
    Toolbar toolbarLapTop;
    ProgressBar loadLapTop;
    ListView listViewLapTop;
    ArrayList<Sanpham> laptopArrayList;
    SanphamAdapter laptopAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);

        Khaibao();
        ActionBar();
        getData();

        listViewLapTop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),ChiTietSanPhamActivity.class);
                intent.putExtra("thongtinsanpham",laptopArrayList.get(i));
                startActivity(intent);
            }
        });
    }

    private void getData() {
        loadLapTop.setVisibility(View.VISIBLE);
        ApiSp.apiDevice.getListsp().enqueue(new Callback<List<Sanpham>>() {
            @Override
            public void onResponse(Call<List<Sanpham>> call, Response<List<Sanpham>> response) {
                if (response.isSuccessful()) {
                    List<Sanpham> allProducts = response.body();
                    if (allProducts != null) {
                        // Lọc sản phẩm có idsanpham="2"
                        List<Sanpham> filteredProducts = new ArrayList<>();
                        for (Sanpham sanpham : allProducts) {
                            if (sanpham.getIdsanpham() == 2) {
                                filteredProducts.add(sanpham);
                            }
                        }
                        // Đưa tất cả sản phẩm vào laptopArrayList
                        laptopArrayList.addAll(filteredProducts);
                        // Hiển thị danh sách sản phẩm đã lọc
                        laptopAdapter = new SanphamAdapter(laptopArrayList, getApplicationContext());
                        listViewLapTop.setAdapter(laptopAdapter);
                    } else {
                        // Xử lý khi không nhận được danh sách sản phẩm
                        Toast.makeText(getApplicationContext(), "Không có sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Xử lý khi request không thành công
                    Toast.makeText(getApplicationContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
                }
                loadLapTop.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<List<Sanpham>> call, Throwable t) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                loadLapTop.setVisibility(View.INVISIBLE);
            }
        });
    }



    private void ActionBar() {
        setSupportActionBar(toolbarLapTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarLapTop.setNavigationIcon(R.drawable.ic_action_back);
        toolbarLapTop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        getMenuInflater().inflate(R.menu.search,menu);

        searchView = (SearchView) menu.findItem(R.id.ic_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                laptopAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                laptopAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.iconGioHang:
                Intent intent=new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Khaibao() {
        loadLapTop = findViewById(R.id.loadLapTop);
        toolbarLapTop=findViewById(R.id.ToolbarLapTop);
        listViewLapTop=findViewById(R.id.listviewLaptop);

        laptopArrayList=new ArrayList<>();
    }
}