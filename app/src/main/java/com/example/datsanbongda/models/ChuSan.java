package com.example.datsanbongda.models;

public class ChuSan {
    public String idChuSan, tenNguoiDung, soDienThoai, taiKhoan, matKhau;

    public ChuSan() { }

    public ChuSan(String idChuSan, String tenChuSan, String soDienThoai, String taiKhoan, String matKhau) {
        this.idChuSan = idChuSan;
        this.tenNguoiDung = tenChuSan;
        this.soDienThoai = soDienThoai;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
    }
}