package com.example.appbanthietbidientu.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Sanphammoi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;

public class AdminUploadActivity extends AppCompatActivity {
    ImageView uploadImage;
    Button saveButton;
    EditText uploadName, uploadPrice, uploadDesc;
    Spinner uploadSanPhamID;
    String imageURL;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_upload);

        uploadImage = findViewById(R.id.uploadImage);
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadName = findViewById(R.id.uploadTopic);
        uploadPrice = findViewById(R.id.uploadPrice);
//        uploadSanPhamID = findViewById(R.id.uploadSanPhamID);
        uploadSanPhamID = findViewById(R.id.uploadSanPhamID);
        saveButton = findViewById(R.id.saveButton);

        // Đẩy data vào spinner
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.item_id,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        uploadSanPhamID.setAdapter(adapter);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            uploadImage.setImageURI(uri);
                        } else {
                            Toast.makeText(AdminUploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images")
                .child(uri.getLastPathSegment());


        AlertDialog.Builder builder = new AlertDialog.Builder(AdminUploadActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageURL = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData(){
        //We are changing the child from title to currentDate,
        // because we will be updating title as well and it may affect child value.
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("sanphammoinhat");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long spCount = snapshot.getChildrenCount();
                        String childName = String.valueOf(spCount);

                        String title = uploadName.getText().toString();
                        String desc = uploadDesc.getText().toString();
                        int price = Integer.parseInt(uploadPrice.getText().toString());
                        int idsp = Integer.parseInt(uploadSanPhamID.getSelectedItem().toString());
                        int id= Integer.parseInt(String.valueOf(spCount + 1));
                        Sanphammoi sanphammoi = new Sanphammoi(id, title, price, imageURL,desc, idsp);

                        dbRef.child(childName)
                                .setValue(sanphammoi).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(AdminUploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(AdminUploadActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}