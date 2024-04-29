package com.example.appbanthietbidientu.ultil;

import com.example.appbanthietbidientu.model.Comment;
import com.example.appbanthietbidientu.model.MessageModel;
import com.example.appbanthietbidientu.model.Sanpham;
import com.example.appbanthietbidientu.response.SignInResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiSp {
    Gson gson=new GsonBuilder().setDateFormat("dd-MM-yyy").create();

    //https://firebasestorage.googleapis.com/v0/b/realtime-64f58.appspot.com/o/sanphammoinhat.json?alt=media&token=04505275-acd8-47cb-9bdb-5885d1fbaeff
    //https://firebasestorage.googleapis.com/v0/b/realtime-64f58.appspot.com/o/dienthoai.json?alt=media&token=d14eb726-c131-4860-a3e4-266d0aa206ed
    //https://firebasestorage.googleapis.com/v0/b/realtime-64f58.appspot.com/o/laptop.json?alt=media&token=4452ff5b-1980-4626-b646-5fa4c03159d0

    ApiSp apiDevice = new Retrofit.Builder()
            .baseUrl("https://empyrean-depth-408513-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiSp.class);

    @Multipart
    @POST("register.php")
    Call<SignInResponse> confirmRegister(@Part(Const.KEY_EMAIL) RequestBody email,
                                         @Part(Const.KEY_PASSWORD) RequestBody password);

    @Multipart
    @POST("login.php")
    Call<SignInResponse> confirmLogin(@Part(Const.KEY_EMAIL) RequestBody email,
                                         @Part(Const.KEY_PASSWORD) RequestBody password);

    @GET("sanphammoinhat.json")
    Call<List<Sanpham>> getListsp();
    @GET("comment.json")
    Call<List<Comment>> getlistComment();


    @GET("dienthoai.json")
    Call<List<Sanpham>> getlistDienThoai();

    @GET("laptop.json")
    Call<List<Sanpham>> getlistLapTop();

    @Multipart
    @POST("thongtinkhachhang.php")
    Call<SignInResponse> getThongTinKhachHang(@Part(Const.KEY_USERNAME) RequestBody tenkhachhang,
                                          @Part(Const.KEY_SDT) RequestBody sodienthoai,
                                          @Part(Const.KEY_EMAIL) RequestBody email);

    @POST("comment.json")
    Call<Void> postComment(@Body Comment newComment);

    @POST("sanphammoinhat.json") // Endpoint để thêm sản phẩm
    Call<Void> insertSp(@Body Sanpham sanpham,  @Path("id") String id);

    @PUT("sanphammoinhat/{id}.json") // Endpoint để cập nhật sản phẩm
    Call<Void> updateSp(@Body Sanpham sanpham, @Path("id") String id);

    @DELETE("sanphammoinhat/{id}.json") // Endpoint để xóa sản phẩm
    Call<Void> xoaSanPham(@Path("id") String id);

    @Multipart
    @POST("uploadFile")
    Call<MessageModel> uploadFile(@Part MultipartBody.Part file);


}
