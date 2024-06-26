package com.example.appbanthietbidientu.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.example.appbanthietbidientu.Adapter.GioHangAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.ultil.CheckConnect;
import com.google.gson.Gson;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    Toolbar toolbarGioHang;
    ListView lvGioHang;
    TextView txtThongBao;
    static TextView txtTongTien;
    TextView btnThanhToanTien,btnTiepTucMua;
    GioHangAdapter gioHangAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);

        overridePendingTransition(R.anim.animation_down_to_up,R.anim.animation_exit_left);

        Khaibao();
        Actionbar();
        CheckData();
        EvenUltil();
        CatchOnItemListView();
    }

    private void CatchOnItemListView() {
        lvGioHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(GioHangActivity.this);
                builder.setTitle("Xác nhận xoá sản phẩm");
                builder.setMessage("Bạn có chắc muốn xoá sản phẩm khỏi giỏ hàng?");
                builder.setIcon(R.drawable.zzz);
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(MainActivity.gioHangArrayList.size() <= 0){
                            txtThongBao.setVisibility(View.VISIBLE);
                        }else {
                            MainActivity.gioHangArrayList.remove(position);
                            gioHangAdapter.notifyDataSetChanged();
                            EvenUltil();
                            if(MainActivity.gioHangArrayList.size() <= 0){
                                txtThongBao.setVisibility(View.VISIBLE);
                            }else {
                                txtThongBao.setVisibility(View.INVISIBLE);
                                gioHangAdapter.notifyDataSetChanged();
                                EvenUltil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public static void EvenUltil() {
        long tongTien = 0;
        for(int i=0; i<MainActivity.gioHangArrayList.size(); i++){
            tongTien += MainActivity.gioHangArrayList.get(i).getGiasp();
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongTien)+"₫");
    }

    private void CheckData() {
        if(MainActivity.gioHangArrayList.size() <= 0){
            gioHangAdapter.notifyDataSetChanged();
            txtThongBao.setVisibility(View.VISIBLE);
            lvGioHang.setVisibility(View.INVISIBLE);
        }else {
            gioHangAdapter.notifyDataSetChanged();
            txtThongBao.setVisibility(View.INVISIBLE);
            lvGioHang.setVisibility(View.VISIBLE);
        }
    }

    private void Khaibao() {
        toolbarGioHang=findViewById(R.id.ToolbarGioHang);
        lvGioHang=findViewById(R.id.listviewGioHang);
        txtThongBao=findViewById(R.id.textviewThongbao);
        txtTongTien=findViewById(R.id.textviewTongTien);
        btnThanhToanTien=findViewById(R.id.ThanhToanGioHang);
        btnTiepTucMua=findViewById(R.id.TiepTucMuaHang);
        gioHangAdapter=new GioHangAdapter(GioHangActivity.this,MainActivity.gioHangArrayList);
        lvGioHang.setAdapter(gioHangAdapter);

        //set thông báo xác nhận
        progressDialog=new ProgressDialog(GioHangActivity.this);
        progressDialog.setMessage("Please wait ...");

        Typeface medium = ResourcesCompat.getFont(this,R.font.svn_gilroy_medium);
        btnThanhToanTien.setTypeface(medium);
        btnTiepTucMua.setTypeface(medium);

        btnTiepTucMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"Mời bạn tiếp tục mua hàng",Toast.LENGTH_SHORT).show();
            }
        });

        btnThanhToanTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.gioHangArrayList.size() >= 1) {
                    progressDialog.show();
                    CountDownTimer countDownTimer = new CountDownTimer(3000, 3000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // Nothing to do on tick
                        }

                        @Override
                        public void onFinish() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(GioHangActivity.this, ThongTinKhachHang.class);
                            // Convert the list of products to a JSON string
                            Gson gson = new Gson();
                            String productsJson = gson.toJson(MainActivity.gioHangArrayList);
                            // Pass the JSON string as an extra with the intent
                            intent.putExtra("ProductsJson", productsJson);
                            intent.putExtra("TotalMoney", String.valueOf(txtTongTien.getText()));
                            startActivity(intent);
                        }
                    }.start();
                } else {
                    CheckConnect.ShowToast_Short(getApplicationContext(), "Giỏ hàng của bạn chưa có sản phẩm");
                }
            }
        });

    }

    private void Actionbar() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationIcon(R.drawable.ic_action_back);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.animation_enter_left,R.anim.animation_up_to_down);
    }
}