package com.example.datsanbongda.models;

public class NguoiDung {
    public String idNguoiDung, tenNguoiDung, soDienThoai, taiKhoan, matKhau;

    public NguoiDung() { }

    public NguoiDung(String idNguoiDung, String tenNguoiDung, String soDienThoai, String taiKhoan, String matKhau) {
        this.idNguoiDung = idNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.soDienThoai = soDienThoai;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
    }
}