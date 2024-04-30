package com.example.appbanthietbidientu.Activity;

import com.example.appbanthietbidientu.ultil.ApiSp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanthietbidientu.Adapter.CommentAdapter;
import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.Comment;
import com.example.appbanthietbidientu.model.GioHang;
import com.example.appbanthietbidientu.model.Sanpham;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    Toolbar toolbarChitietsp;
    ImageView anhChitietsp;
    TextView tenChiTiet, giaChiTiet, motaChiTiet;
    Spinner spinner;
    TextView themVaoGioHang;
    Sanpham sanpham;
    RecyclerView recyclerViewComments;
    List<Comment> commentList;
    EditText editTextComment;
    Button buttonSendComment;
    Button btn_Delete;
    CommentAdapter commentAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);

        // Khởi tạo các thành phần giao diện và thiết lập sự kiện
        Khaibao();
        Actionbar();
        CatchEvenSpinner();
        EvenClickThemGioHang();


        // Lấy thông tin sản phẩm và bình luận
        getInforsp();
    }


    private void EvenClickThemGioHang() {
        themVaoGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MainActivity.gioHangArrayList.size() > 0){
                    int sl=Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exits=false;

                    for (int i=0;i < MainActivity.gioHangArrayList.size();i++) {
                        if (MainActivity.gioHangArrayList.get(i).getIdsp() == sanpham.getId()) {
                            MainActivity.gioHangArrayList.get(i).setSoluongsp(MainActivity.gioHangArrayList.get(i).getSoluongsp() + sl);
                            if(MainActivity.gioHangArrayList.get(i).getSoluongsp() >= 10){
                                MainActivity.gioHangArrayList.get(i).setSoluongsp(10);
                            }
                            MainActivity.gioHangArrayList.get(i).setGiasp((long) sanpham.getGiasanpham() * MainActivity.gioHangArrayList.get(i).getSoluongsp());
                            exits=true;
                        }
                    }
                    if(!exits){
                        int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
                        long giaMoi= (long) soluong * sanpham.getGiasanpham();
                        MainActivity.gioHangArrayList.add(new GioHang(sanpham.getId(), sanpham.getTensanpham(), giaMoi, sanpham.getHinhanhsanpham(), soluong));
                    }
                }else{
                    int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
                    long giaMoi= (long) soluong * sanpham.getGiasanpham();
                    MainActivity.gioHangArrayList.add(new GioHang(sanpham.getId(), sanpham.getTensanpham(), giaMoi, sanpham.getHinhanhsanpham(), soluong));
                }
                Intent intent=new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.iconGioHang) {
            Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void CatchEvenSpinner() {
        Integer[] soluong=new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> integerArrayAdapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, soluong);
        spinner.setAdapter(integerArrayAdapter);
    }

    private void getInforsp() {
        // Lấy thông tin sản phẩm từ intent
        sanpham= (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");

        // Hiển thị thông tin sản phẩm
        tenChiTiet.setText(sanpham.getTensanpham());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        giaChiTiet.setText(String.format("Giá: %s₫", decimalFormat.format(sanpham.getGiasanpham())));
        motaChiTiet.setText(sanpham.getMotasanpham());
        Typeface regular = ResourcesCompat.getFont(ChiTietSanPhamActivity.this,R.font.svn_gilroy_regular);
        motaChiTiet.setTypeface(regular);
        Picasso.get().load(sanpham.getHinhanhsanpham())
                .placeholder(R.drawable.loadimage)
                .error(R.drawable.errorimage)
                .into(anhChitietsp);
        Log.d("Firebase", "Bình luận: " + sanpham.getId());
        // Gọi hàm để lấy comment của sản phẩm
        getCommentsForProduct();
    }
    private void getCommentsForProduct() {
        // Gửi yêu cầu để lấy danh sách comment từ API
        ApiSp.apiDevice.getlistComment().enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful()) {
                    List<Comment> comments = response.body();
                    // Lọc chỉ những bình luận có id="1"
                    List<Comment> filteredComments = new ArrayList<>();
                    for (Comment comment : comments) {
                        if (comment.getIdSp() == sanpham.getId() && Objects.equals(comment.getStatus(), "true")) {
                            filteredComments.add(comment);
                        }
                    }
                    // Xử lý danh sách comment ở đây
                    displayComments(filteredComments);
                } else {
                    // Xử lý nếu có lỗi khi nhận phản hồi từ server
                    Log.e("API Call", "Failed to get comments: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                // Xử lý nếu có lỗi trong quá trình gửi yêu cầu hoặc nhận phản hồi từ server
                Log.e("API Call", "onFailure: " + t.getMessage());
            }
        });
    }

    private void displayComments(List<Comment> comments) {
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter(comments);
        recyclerViewComments.setAdapter(commentAdapter);

        // Thiết lập CommentDeleteListener cho CommentAdapter
        commentAdapter.setCommentDeleteListener(new CommentAdapter.CommentDeleteListener() {
            @Override
            public void onCommentDelete(String commentId) {
                // Xử lý khi người dùng click vào button xóa comment
                deleteComment(commentId);
            }
        });
    }
    private void postComment() {
        sanpham.getId();
        // Lấy nội dung của comment từ EditText
        String commentText = editTextComment.getText().toString().trim();

        // Kiểm tra xem nội dung comment có rỗng không
        if (commentText.isEmpty()) {
            // Hiển thị thông báo nếu nội dung comment rỗng
            Toast.makeText(this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
            return;
        }
        LocalDateTime currentTime;
        // Tạo một đối tượng Comment mới
        Comment newComment = new Comment();
        newComment.setCommentText(commentText);
        newComment.setIdSp(sanpham.getId()); // Đặt idSp tương ứng với sản phẩm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalDateTime.now();
            long timestamp = currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            Log.d("CurrentTime", "currentTime: " + timestamp);
            newComment.setTimestamp(timestamp); // Đặt timestamp hiện tại
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = sharedPreferences.getString("email", "");

        newComment.setUserId(email); // Đặt userId (có thể là ID của người dùng đã đăng nhập)

        newComment.setStatus("true");

        // Lấy tham chiếu đến nút "comment" trong cơ sở dữ liệu Firebase
        DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("comment");

        // Đếm số lượng phần tử trong nút "comment"
        commentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy số lượng phần tử trong nút "comment"
                long commentCount = dataSnapshot.getChildrenCount();

                // Tạo một khóa tự động cho bình luận mới
                String newCommentKey = String.valueOf(commentCount );
                newComment.setIdCmt(commentCount);
                // Thêm bình luận mới vào cơ sở dữ liệu Firebase
                commentsRef.child(newCommentKey).setValue(newComment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xử lý thành công khi gửi comment
                            Log.d("API Call", "New comment posted successfully");
                            // Cập nhật danh sách comment
                            getCommentsForProduct();
                            // Xóa đi text trong EditText
                            editTextComment.setText("");
                        } else {
                            // Xử lý khi gặp lỗi
                            Log.e("API Call", "Failed to post new comment: " + task.getException().getMessage());
                            // Hiển thị thông báo nếu cần
                            Toast.makeText(ChiTietSanPhamActivity.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi gặp lỗi
                Log.e("Firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }
    private void deleteComment(String commentId) {
        // Get reference to the comment in the Firebase database
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("comment").child(commentId);

        // Set the new text for the comment
        commentRef.child("status").setValue("false").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Xử lý thành công khi gửi comment
                    Log.d("API Call", "New comment delete successfully");
                    // Cập nhật danh sách comment
                    getCommentsForProduct();
                    // Xóa đi text trong EditText
                    editTextComment.setText("");
                } else {
                    // Xử lý khi gặp lỗi
                    Log.e("API Call", "Failed to delete comment: " + task.getException().getMessage());
                    // Hiển thị thông báo nếu cần
                    Toast.makeText(ChiTietSanPhamActivity.this, "Failed to delete comment", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }


    public static String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(timestamp);
    }

    private void Actionbar() {
        setSupportActionBar(toolbarChitietsp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChitietsp.setNavigationIcon(R.drawable.ic_action_back);
        toolbarChitietsp.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Khaibao() {
        toolbarChitietsp = findViewById(R.id.ToolbarChitietSanPham);
        anhChitietsp = findViewById(R.id.imageviewChiTietSanPham);
        tenChiTiet = findViewById(R.id.textviewTenChiTietSanPham);
        giaChiTiet = findViewById(R.id.textviewGiaChiTietSanPham);
        motaChiTiet = findViewById(R.id.textviewMoTaChiTietSanPham);
        spinner = findViewById(R.id.spinnerChiTietSanPham);
        themVaoGioHang = findViewById(R.id.ThemGioHangChiTietSanPham);
        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        editTextComment = findViewById(R.id.editTextComment);
        buttonSendComment = findViewById(R.id.buttonSendComment);


        buttonSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi hàm postComment() khi người dùng nhấn nút "Gửi"
                postComment();
            }
        });

        // Initialize commentList and commentAdapter
        commentList = new ArrayList<>();

    }


    @Override
    public void finish() {
        super.finish();
    }
}