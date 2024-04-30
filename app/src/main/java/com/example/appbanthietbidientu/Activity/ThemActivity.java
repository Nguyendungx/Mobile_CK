package com.example.appbanthietbidientu.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanthietbidientu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ThemActivity extends AppCompatActivity {

    EditText tensp,giasp,motasp,surl;
    Button btnThem,btnThoat;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        tensp = (EditText)findViewById(R.id.txttensp);
        giasp= (EditText)findViewById(R.id.txtgiasp);
        motasp= (EditText) findViewById(R.id.txtmotasp);
        surl= (EditText) findViewById(R.id.txtimageurl);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertData();
                clearAll();
            }
        });

        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void insertData(){
        Map<String,Object> map= new HashMap<>();
        map.put("tensanpham", tensp.getText().toString());
        map.put("giasanpham", giasp.getText().toString());
        map.put("motasanpham",motasp.getText().toString());
        map.put("hinhanhsanpham",surl.getText().toString());

        FirebaseDatabase.getInstance().getReference().child("sanphammoinhat").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                            Toast.makeText(ThemActivity.this,"Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                        }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ThemActivity.this,"Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void clearAll(){
        tensp.setText("");
        giasp.setText("");
        motasp.setText("");
        surl.setText("");
    }


}
