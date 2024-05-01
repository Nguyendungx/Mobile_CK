package com.example.appbanthietbidientu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanthietbidientu.Adapter.GioHangAdapter;
import com.example.appbanthietbidientu.Adapter.GioHangRecyclerViewAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.GioHang;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChiTietDonHangActivity extends AppCompatActivity {

    Toolbar toolbarChiTietDonHang;
    TextView textThongTinKhachHang;
    TextView textTenKhachHang;
    TextView textTrangThaiDonHang;
    TextView textChiTietTrangThai;
    TextView textGiaTriTongTien;
    TextView textThoiGian;
    Button buttonXacNhan ;
    RecyclerView recyclerViewDonHangChiTiet;
    Spinner spinnerTrangThai;

    DatabaseReference donHangRef;
    // Khai báo trangThaiList ở mức lớp
    List<String> trangThaiList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chi_tiet_don_hang);

        // Khởi tạo các thành phần giao diện và thiết lập sự kiện
        Khaibao();
        getInfoDonHang();
        ActionBar();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    private void Khaibao() {
        toolbarChiTietDonHang = findViewById(R.id.ToolbarChiTietDonHang);
        textThongTinKhachHang = findViewById(R.id.TextThongTinKhachHang);
        textTenKhachHang = findViewById(R.id.TextTenKhachHang);
        textTrangThaiDonHang = findViewById(R.id.TextTrangThaiDonHang);
        textThoiGian=findViewById(R.id.textThoiGian);
        textGiaTriTongTien = findViewById(R.id.TextGiaTriTongTien);
        recyclerViewDonHangChiTiet = findViewById(R.id.recyclerViewDonHangChiTiet);
        spinnerTrangThai = findViewById(R.id.spinnerTrangThai);
        buttonXacNhan = findViewById(R.id.buttonXacNhan);
        // Khởi tạo đối tượng DatabaseReference
        donHangRef = FirebaseDatabase.getInstance().getReference("donhang");


        // Khởi tạo Spinner với danh sách trạng thái đơn hàng
        trangThaiList.add("Đang xử lý");
        trangThaiList.add("Đã xác nhận");
        trangThaiList.add("Đã giao hàng");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trangThaiList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangThai.setAdapter(adapter);

        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickXacNhan();
            }
        });
    }

    // Phương thức xử lý sự kiện khi nút xác nhận được click
    private void onClickXacNhan() {
        // Lấy trạng thái đơn hàng từ Spinner
        String trangThai = spinnerTrangThai.getSelectedItem().toString();

        // Lấy id đơn hàng từ Intent
        String idDonHang = getIntent().getStringExtra("idDonHang");

        // Cập nhật trạng thái đơn hàng vào cơ sở dữ liệu Firebase
        DatabaseReference donHangRef = FirebaseDatabase.getInstance().getReference("donhang").child(idDonHang);
        donHangRef.child("trangthai").setValue(trangThai)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Trong ChiTietDonHangActivity
                        Intent resultIntent = new Intent();

                        // Xử lý khi cập nhật thành công
                            Toast.makeText(ChiTietDonHangActivity.this, "Cập nhật trạng thái đơn hàng thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi cập nhật thất bại
                        Log.e("Firebase Error", "Lỗi khi cập nhật trạng thái đơn hàng: " + e.getMessage());
                        Toast.makeText(ChiTietDonHangActivity.this, "Lỗi khi cập nhật trạng thái đơn hàng", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void ActionBar() {
        setSupportActionBar(toolbarChiTietDonHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTietDonHang.setNavigationIcon(R.drawable.ic_action_back);
        toolbarChiTietDonHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // Bước 4: Khởi tạo adapter và gán cho RecyclerView
    private void setUpRecyclerView(List<GioHang> sanPhamList) {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerViewDonHangChiTiet.setLayoutManager(layoutManager);

        GioHangRecyclerViewAdapter adapter = new GioHangRecyclerViewAdapter(this, sanPhamList);

        recyclerViewDonHangChiTiet.setAdapter(adapter);
    }

    // Bước 5: Truyền danh sách sản phẩm từ phương thức getInfoDonHang() vào adapter
    private void getInfoDonHang() {
        String idDonHang = getIntent().getStringExtra("idDonHang");

        // Tham chiếu đến nút trong cơ sở dữ liệu Firebase Realtime
        donHangRef.child(idDonHang).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy dữ liệu từ dataSnapshot
                    String tenKhachHang = dataSnapshot.child("user").getValue(String.class);
                    long thoiGian = dataSnapshot.child("thoigian").getValue(Long.class);
                    int total = dataSnapshot.child("total").getValue(Integer.class);
                    String trangThai = dataSnapshot.child("trangthai").getValue(String.class);
                    textTenKhachHang.setText(tenKhachHang);
                    if ("Đã giao hàng".equals(trangThai)) {
                        spinnerTrangThai.setSelection(trangThaiList.indexOf("Đã giao hàng"));
                    } else if ("Đang xử lý".equals(trangThai)) {
                        spinnerTrangThai.setSelection(trangThaiList.indexOf("Đang xử lý"));
                    } else if ("Đã xác nhận".equals(trangThai)) {
                        spinnerTrangThai.setSelection(trangThaiList.indexOf("Đã xác nhận"));
                    }
                    textGiaTriTongTien.setText(String.valueOf(total));

                    Date thoiGianDate = new Date(thoiGian);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
                    String thoiGianFormatted = sdf.format(thoiGianDate);
                    textThoiGian.setText(thoiGianFormatted);

                    // Lấy danh sách sản phẩm
                    List<GioHang> sanPhamList = new ArrayList<>();
                    for (DataSnapshot sanPhamSnapshot : dataSnapshot.child("dssanpham").getChildren()) {
                        // Lấy thông tin của mỗi sản phẩm từ dataSnapshot
                        String tenSP = sanPhamSnapshot.child("tensp").getValue(String.class);
                        int giaSP = sanPhamSnapshot.child("giasp").getValue(Integer.class);
                        int soLuong = sanPhamSnapshot.child("soluongsp").getValue(Integer.class);
                        String hinhSP = sanPhamSnapshot.child("hinhsp").getValue(String.class);
                        int idSP = sanPhamSnapshot.child("idsp").getValue(Integer.class);

                        // Tạo đối tượng SanPham từ thông tin sản phẩm
                        GioHang sanPham = new GioHang();
                        sanPham.setIdsp(idSP);
                        sanPham.setTensp(tenSP);
                        sanPham.setGiasp(giaSP);
                        sanPham.setSoluongsp(soLuong);
                        sanPham.setHinhsp(hinhSP);

                        // Thêm sản phẩm vào danh sách sanPhamList
                        sanPhamList.add(sanPham);
                    }

                    // Hiển thị danh sách sản phẩm trong RecyclerView
                    setUpRecyclerView(sanPhamList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e("Firebase Error", "Lỗi khi truy xuất dữ liệu từ Firebase: " + databaseError.getMessage());
            }
        });
    }


}
