package com.example.appbanthietbidientu.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.ExchangeRateResponse;
import com.example.appbanthietbidientu.model.GioHang;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.response.RetrofitClient;
import com.example.appbanthietbidientu.response.SignInResponse;
import com.example.appbanthietbidientu.ultil.ApiSp;
import com.example.appbanthietbidientu.ultil.CheckConnect;
import com.example.appbanthietbidientu.ultil.ExchangeRateService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.reflect.TypeToken;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.approve.OnApprove;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CaptureOrderResult;
import com.paypal.checkout.order.OnCaptureComplete;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.paymentbutton.PaymentButtonContainer;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ThongTinKhachHang extends AppCompatActivity {
    Toolbar toolbarThongTinKhachHang;
    EditText NhapTenKhachHang,NhapSoDienThoai,DiaChiKhachHang;
    TextView XacNhan,Huy,txtThanhToanTien,DatHang;
    ProgressDialog progressDialog;
    PaymentButtonContainer paymentButtonContainer;
    private static final String TAG = "MyTag";
    // Khai báo các biến name, sdt, diachi ở mức độ lớp

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);

        overridePendingTransition(R.anim.animation_scale_enter_right,R.anim.animation_scale_exit_left);

        //set thông báo xác nhận
        progressDialog=new ProgressDialog(ThongTinKhachHang.this);
        progressDialog.setMessage("Please wait ...");

        Khaibao();
        Actionbar();
        ClickXacNhan();
        ClickDatHang();

        ClickHuy();

    }
    private boolean validateFields() {
        String strTen = NhapTenKhachHang.getText().toString().trim();
        String strSDT = NhapSoDienThoai.getText().toString().trim();
        String diachi = DiaChiKhachHang.getText().toString().trim();

        if (strTen.isEmpty()) {
            NhapTenKhachHang.setError("Tên không được để trống");
            return false;
        }

        if (strSDT.isEmpty()) {
            NhapSoDienThoai.setError("Số điện thoại không được để trống");
            return false;
        }

        if (diachi.isEmpty()) {
            DiaChiKhachHang.setError("Địa chỉ không được để trống");
            return false;
        }

        return true;
    }

    private void ClickDatHang() {
        DatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra dữ liệu trước khi đặt hàng
                if (validateFields()) {
                    // Lấy dữ liệu từ EditText
                    String strTen = NhapTenKhachHang.getText().toString().trim();
                    String strSDT = NhapSoDienThoai.getText().toString().trim();
                    String diachi = DiaChiKhachHang.getText().toString().trim();

                    // Gọi phương thức thanhtoan và truyền dữ liệu vào
                    dathang(strTen, strSDT, diachi);
                }
            }
        });
    }


    private void ClickHuy() {
        Huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ClickXacNhan() {
        XacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra dữ liệu trước khi đặt hàng
                if (validateFields()) {
                    // Lấy dữ liệu từ EditText
                    String strTen = NhapTenKhachHang.getText().toString().trim();
                    String strSDT = NhapSoDienThoai.getText().toString().trim();
                    String diachi = DiaChiKhachHang.getText().toString().trim();

                    // Gọi phương thức thanhtoan và truyền dữ liệu vào
                    thanhtoan(strTen, strSDT, diachi);
                }
            }
        });
    }


    private void Actionbar() {
        setSupportActionBar(toolbarThongTinKhachHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarThongTinKhachHang.setNavigationIcon(R.drawable.ic_action_back);
        toolbarThongTinKhachHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Khaibao() {
        toolbarThongTinKhachHang=findViewById(R.id.ToolbarThongTinKhachHang);
        NhapTenKhachHang = findViewById(R.id.TenKhachHang);
        NhapSoDienThoai = findViewById(R.id.SoDienThoai);
        DiaChiKhachHang = findViewById(R.id.EmailKhachHang);
        XacNhan = findViewById(R.id.XacNhan);
        DatHang = findViewById(R.id.DatHang);
        Huy = findViewById(R.id.Huy);
        txtThanhToanTien = findViewById(R.id.txtThanhToanTien);
        // Thêm đoạn mã sau đây
        paymentButtonContainer = findViewById(R.id.payment_button_container);
        String nd = getIntent().getStringExtra("TotalMoney");

// Remove the comma and the currency symbol before parsing
        nd = nd.replaceAll("[,₫]", "");

        txtThanhToanTien.setText("Thanh toán số tiền: "+nd);
    }

    private void thanhtoan(String name, String sdt, String diachi) {
        String nd = getIntent().getStringExtra("TotalMoney");

// Remove the comma and the currency symbol before parsing
        nd = nd.replaceAll("[,₫]", "");

        double amountVND = Double.parseDouble(nd);
        // Assuming 25000 is the exchange rate from VND to USD
        double exchangeRate = 25000;
        double amountUSD = amountVND / exchangeRate;
        // Format the amountUSD to have only two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedAmountUSD = decimalFormat.format(amountUSD);

        // Retrieve the JSON string extra from the intent
        String productsJson = getIntent().getStringExtra("ProductsJson");
        // Deserialize the JSON string into a list of products
        Gson gson = new Gson();
        ArrayList<GioHang> products = gson.fromJson(productsJson, new TypeToken<ArrayList<GioHang>>(){}.getType());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            paymentButtonContainer.setup(

                    new CreateOrder() {

                        @Override
                        public void create(@NotNull CreateOrderActions createOrderActions) {
                            ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                            purchaseUnits.add(
                                    new PurchaseUnit.Builder()
                                            .amount(
                                                    new Amount.Builder()
                                                            .currencyCode(CurrencyCode.USD)
                                                            .value(formattedAmountUSD)
                                                            .build()
                                            )
                                            .build()
                            );
                            OrderRequest order = new OrderRequest(
                                    OrderIntent.CAPTURE,
                                    new AppContext.Builder()
                                            .userAction(UserAction.PAY_NOW)
                                            .build(),
                                    purchaseUnits
                            );
                            createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                        }
                    },
                    new OnApprove() {
                        @Override
                        public void onApprove(@NotNull Approval approval) {
                            approval.getOrderActions().capture(new OnCaptureComplete() {
                                @Override
                                public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                    Log.d(TAG, String.format("CaptureOrderResult: %s", result));

                                    // Create a Firebase database reference
                                    // Tham chiếu đến node "donhang"
                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("donhang");

// Lấy số lượng phần tử hiện có trong node "donhang"
                                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            // Lấy số lượng phần tử hiện có
                                            long orderCount = dataSnapshot.getChildrenCount();

                                            // Tạo một key tự động cho đơn hàng mới
                                            String newOrderKey = String.valueOf(orderCount);

                                            // Tạo dữ liệu cho đơn hàng mới
                                            Map<String, Object> orderData = new HashMap<>();
                                            orderData.put("user", "5");
                                            orderData.put("total", amountVND);
                                            orderData.put("name",name);
                                            orderData.put("sdt",sdt);
                                            orderData.put("diachi",diachi);

                                            // Thêm các sản phẩm vào đơn hàng mới
                                            Map<String, Object> productsData = new HashMap<>();
                                            for (int i = 0; i < products.size(); i++) {
                                                GioHang product = products.get(i);
                                                Map<String, Object> productData = new HashMap<>();
                                                productData.put("idsp", product.getIdsp());
                                                productData.put("tensp", product.getTensp());
                                                productData.put("soluongsp", product.getSoluongsp());
                                                productData.put("giasp", product.getGiasp());
                                                productData.put("hinhsp", product.getHinhsp());
                                                productsData.put(String.valueOf(i), productData);
                                            }
                                            orderData.put("dssanpham", productsData);

                                            // Lấy thời gian hiện tại
                                            LocalDateTime currentTime;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                currentTime = LocalDateTime.now();
                                                long timestamp = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                                                orderData.put("thoigian", timestamp);
                                            }

                                            // Đặt trạng thái của đơn hàng
                                            orderData.put("trangthai", "Đã thanh toán");

                                            // Đẩy dữ liệu của đơn hàng mới lên Firebase với key mới
                                            databaseRef.child(newOrderKey).setValue(orderData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d(TAG, "Order successfully added to Firebase");
                                                            Toast.makeText(ThongTinKhachHang.this, "Order successfully placed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.e(TAG, "Error adding order to Firebase", e);
                                                            Toast.makeText(ThongTinKhachHang.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            // Xử lý khi có lỗi xảy ra
                                            Log.e(TAG, "Error accessing database: " + databaseError.getMessage());
                                            Toast.makeText(ThongTinKhachHang.this, "Failed to access database", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                                }
                            });

                        }
                    }
            );
        }
    }


    private void dathang(String name, String sdt, String diachi) {
        String nd = getIntent().getStringExtra("TotalMoney");

        // Remove the comma and the currency symbol before parsing
        nd = nd.replaceAll("[,₫]", "");

        double amountVND = Double.parseDouble(nd);
        // Assuming 25000 is the exchange rate from VND to USD
        double exchangeRate = 25000;
        double amountUSD = amountVND / exchangeRate;
        // Format the amountUSD to have only two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedAmountUSD = decimalFormat.format(amountUSD);

        // Retrieve the JSON string extra from the intent
        String productsJson = getIntent().getStringExtra("ProductsJson");
        // Deserialize the JSON string into a list of products
        Gson gson = new Gson();
        ArrayList<GioHang> products = gson.fromJson(productsJson, new TypeToken<ArrayList<GioHang>>(){}.getType());

        // Create a Firebase database reference
        // Tham chiếu đến node "donhang"
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("donhang");

        // Lấy số lượng phần tử hiện có trong node "donhang"
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy số lượng phần tử hiện có
                long orderCount = dataSnapshot.getChildrenCount();

                // Tạo một key tự động cho đơn hàng mới
                String newOrderKey = String.valueOf(orderCount);

                // Tạo dữ liệu cho đơn hàng mới
                Map<String, Object> orderData = new HashMap<>();
                orderData.put("user", "5");
                orderData.put("total", amountVND);
                orderData.put("name", name);
                orderData.put("sdt", sdt);
                orderData.put("diachi", diachi);

                // Thêm các sản phẩm vào đơn hàng mới
                Map<String, Object> productsData = new HashMap<>();
                for (int i = 0; i < products.size(); i++) {
                    GioHang product = products.get(i);
                    Map<String, Object> productData = new HashMap<>();
                    productData.put("idsp", product.getIdsp());
                    productData.put("tensp", product.getTensp());
                    productData.put("soluongsp", product.getSoluongsp());
                    productData.put("giasp", product.getGiasp());
                    productData.put("hinhsp", product.getHinhsp());
                    productsData.put(String.valueOf(i), productData);
                }
                orderData.put("dssanpham", productsData);

                // Lấy thời gian hiện tại
                LocalDateTime currentTime;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    currentTime = LocalDateTime.now();
                    long timestamp = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                    orderData.put("thoigian", timestamp);
                }

                // Đặt trạng thái của đơn hàng
                orderData.put("trangthai", "Đã xác nhận");

                // Đẩy dữ liệu của đơn hàng mới lên Firebase với key mới
                databaseRef.child(newOrderKey).setValue(orderData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Order successfully added to Firebase");
                                Toast.makeText(ThongTinKhachHang.this, "Order successfully placed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "Error adding order to Firebase", e);
                                Toast.makeText(ThongTinKhachHang.this, "Failed to place order", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
                Log.e(TAG, "Error accessing database: " + databaseError.getMessage());
                Toast.makeText(ThongTinKhachHang.this, "Failed to access database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_scale_enter_left,R.anim.animation_scale_exit_right);
    }
}