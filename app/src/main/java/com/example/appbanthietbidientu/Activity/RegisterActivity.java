package com.example.appbanthietbidientu.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Account;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailedit, passedit, nameedit,txtbacklogin;
    private Button btn_signup;
    private Button btn_login;
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference db;
    private String email, pass, name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        emailedit = findViewById(R.id.email);
        passedit = findViewById(R.id.password);
        nameedit = findViewById(R.id.name);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login=findViewById(R.id.btn_login);



        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void register() {
        email=emailedit.getText().toString();
        pass=passedit.getText().toString();
        name=nameedit.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Vui lòng nhập email!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this, "Vui lòng nhập pass!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Vui lòng nhập tên!!", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();

                    // Lấy uid tk vừa tạo
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        uid = user.getUid();
                        Log.d("User UID", "UID: " + uid);

                        db = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference accountRef = db.getDatabase().getReference("account");

                        Account account = new Account(email, uid, 2);

                        // Tạo node tăng dần
                        String node = accountRef.push().getKey();
                        accountRef.child(uid).setValue(account)
                                .addOnSuccessListener(aVoid -> Log.d("Account", "Account data saved successfully"))
                                .addOnFailureListener(e -> Log.e("Account", "Failed to save account data: " + e.getMessage()));
                    }

                    Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Tạo object 1 account
//        Account account = new Account(email, uid, 2);

        // Lưu vào realtime
//        db = FirebaseDatabase.getInstance().getReference();
//        DatabaseReference accountRef = db.getDatabase().getReference("account");
//
//        // Tạo node tăng dần
//        String node = accountRef.push().getKey();
//        accountRef.child(uid).setValue(account)
//                .addOnSuccessListener(aVoid -> Log.d("Account", "Account data saved successfully"))
//                .addOnFailureListener(e -> Log.e("Account", "Failed to save account data: " + e.getMessage()));
    }
}