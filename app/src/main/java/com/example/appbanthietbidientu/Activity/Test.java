package com.example.appbanthietbidientu.Activity;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Test extends AppCompatActivity {

    private DatabaseReference connectedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tham chiếu tới node ".info/connected" trong Firebase Realtime Database
        connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");

        // Thêm lắng nghe sự kiện để kiểm tra kết nối
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    // Kết nối thành công
                    Log.d("Firebase", "Đã kết nối với Firebase Realtime Database");
                } else {
                    // Mất kết nối
                    Log.d("Firebase", "Mất kết nối với Firebase Realtime Database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi nếu có
                Log.e("Firebase", "Lỗi khi kiểm tra kết nối với Firebase Realtime Database", error.toException());
            }
        });
    }
}

