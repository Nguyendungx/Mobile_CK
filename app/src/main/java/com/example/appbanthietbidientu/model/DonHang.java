package com.example.appbanthietbidientu.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DonHang implements Serializable {
    private String id;
    private ArrayList<GioHang> danhSachSanPham;
    private long thoiGian;
    private int total;
    private String trangThai;
    private String user;

    public DonHang(String number, ArrayList<GioHang> dssanpham, String thoigian, int total, String trangthai, String user) {
        // Constructor mặc định (cần thiết cho Firebase)
    }

    public DonHang() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<GioHang> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(ArrayList<GioHang> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    public long getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(long thoiGian) {
        this.thoiGian = thoiGian;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DonHang{" +
                "id='" + id + '\'' +
                ", danhSachSanPham=" + danhSachSanPham +
                ", total=" + total +
                ", user='" + user + '\'' +
                ", thoiGian=" + thoiGian +
                '}';
    }

}