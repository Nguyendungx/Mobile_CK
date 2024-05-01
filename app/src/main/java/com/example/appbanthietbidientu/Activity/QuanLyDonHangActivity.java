package com.example.appbanthietbidientu.Activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanthietbidientu.Adapter.DonHangAdapter;
import com.example.appbanthietbidientu.Adapter.DonhangmoiAdapter;
import com.example.appbanthietbidientu.Adapter.SanphammoiAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.DonHang;
import com.example.appbanthietbidientu.model.GioHang;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.ultil.ApiSp;
import com.example.appbanthietbidientu.ultil.BaseFunctionActivity;
import com.example.appbanthietbidientu.ultil.CheckConnect;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyDonHangActivity extends BaseFunctionActivity {
    SearchView searchView;
    Toolbar toolbarQuanLyDonHang;
    ProgressBar loadDonHang;
    ListView listViewDonHang;
    ArrayList<DonHang> donHangArrayList;
    DonHangAdapter donHangAdapter;
    View footerView;
    private DatabaseReference mDatabase,m1Database;
    int page = 1;
    boolean isLoading = false;
    boolean limitData = false;
//    mHander mHander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_quanlydonhang);
        if (CheckConnect.haveNetworkConnected(getApplicationContext())) {
            Khaibao();
            ActionBar();
            getData();
        } else {
            CheckConnect.ShowToast_Short(getApplicationContext(), "Error Connect Internet");
        }

        listViewDonHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Lấy thông tin của đơn hàng đã chọn
                DonHang selectedDonHang = donHangArrayList.get(i);

                // Log thông tin của đơn hàng đã chọn
                        Log.d("Selected Don Hang", selectedDonHang.getId().toString());

// Tạo Intent và đưa thông tin đơn hàng vào Bundle
                Intent intent = new Intent(getApplicationContext(), ChiTietDonHangActivity.class);
                intent.putExtra("idDonHang",selectedDonHang.getId());
                finish();

// Chuyển sang activity ChiTietDonHangActivity
                startActivity(intent);

            }
        });


    }

    private void getData() {
        // Khởi tạo FirebaseApp (chỉ cần thực hiện một lần trong ứng dụng)

        // Tham chiếu đến nút trong cơ sở dữ liệu Firebase Realtime
        mDatabase = FirebaseDatabase.getInstance().getReference("donhang");

        // Lắng nghe sự kiện thay đổi dữ liệu trên Firebase Realtime Database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu của mỗi đơn hàng
                    Map<String, Object> donHangData = (Map<String, Object>) snapshot.getValue();
                    assert donHangData != null;

                    // Lấy các thông tin của đơn hàng
                    String thoigian = donHangData.get("thoigian").toString();
                    int total = Integer.parseInt(donHangData.get("total").toString());
                    String trangthai = donHangData.get("trangthai").toString();
                    String user = donHangData.get("user").toString();

                    // Khởi tạo đối tượng DonHang
                    DonHang donHang = new DonHang();
                    donHang.setId(snapshot.getKey());
                    donHang.setTotal(total);
                    donHang.setUser(user);
                    donHang.setTrangThai(trangthai);
                    donHang.setThoiGian(Long.parseLong(thoigian));

                    // Hiển thị thông tin đơn hàng
                    Log.d("Firebase Data", "Don Hang ID: " + snapshot.getKey());

                    // Lấy danh sách sản phẩm của đơn hàng
                    DatabaseReference dssanphamRef = FirebaseDatabase.getInstance().getReference("donhang").child(snapshot.getKey()).child("dssanpham");
                    dssanphamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<GioHang> gioHangList = new ArrayList<>();

                            for (DataSnapshot gioHangSnapshot : dataSnapshot.getChildren()) {
                                // Lấy thông tin của mỗi sản phẩm từ dataSnapshot
                                String tenSP = gioHangSnapshot.child("tensp").getValue(String.class);
                                int giaSP = gioHangSnapshot.child("giasp").getValue(Integer.class);
                                int soLuong = gioHangSnapshot.child("soluongsp").getValue(Integer.class);
                                String hinhSP = gioHangSnapshot.child("hinhsp").getValue(String.class);
                                int idSP = gioHangSnapshot.child("idsp").getValue(Integer.class);

                                // Tạo đối tượng GioHang từ thông tin sản phẩm
                                GioHang gioHang = new GioHang();
                                gioHang.setIdsp(idSP);
                                gioHang.setTensp(tenSP);
                                gioHang.setGiasp(giaSP);
                                gioHang.setSoluongsp(soLuong);
                                gioHang.setHinhsp(hinhSP);

                                // Thêm sản phẩm vào danh sách gioHangList
                                gioHangList.add(gioHang);
                            }

                            // Gán danh sách sản phẩm vào đơn hàng
                            donHang.setDanhSachSanPham(gioHangList);

                            // Hiển thị danh sách sản phẩm của đơn hàng
                            Log.d("Firebase Data", "Gio Hang: " + gioHangList);

                            // Thêm đơn hàng vào danh sách donHangArrayList
                            donHangArrayList.add(donHang);

                            // Chuyển đổi thành chuỗi JSON
                            String json = new Gson().toJson(donHangData);

                            // Log ra thông tin của đơn hàng
                            Log.d("Firebase Data", "DonHang: " + donHangArrayList);
                            DonhangmoiAdapter donhangmoiAdapter=new DonhangmoiAdapter(donHangArrayList, getApplicationContext());
                            donHangAdapter = new DonHangAdapter(new ArrayList<>(donHangArrayList), getApplicationContext());
                            listViewDonHang.setAdapter(donHangAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Xử lý khi có lỗi xảy ra
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(getApplicationContext(), "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void ActionBar() {

        setSupportActionBar(toolbarQuanLyDonHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarQuanLyDonHang.setNavigationIcon(R.drawable.ic_action_back);

        toolbarQuanLyDonHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.iconGioHang:
//                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
//                startActivity(intent);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void Khaibao() {
        loadDonHang = findViewById(R.id.loadDonHang);
        toolbarQuanLyDonHang = findViewById(R.id.ToolbarQuanLyDonHang);
        listViewDonHang = findViewById(R.id.listviewDonHang);

        donHangArrayList = new ArrayList<DonHang>();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.loading_layout, null);
    }
}
