package com.example.datsanbongda;

public class DatSan {
    public String idDatSan;
    public String tenNguoiDung;
    public String soDienThoai;
    public String gioBatDau;
    public String gioSuDung;
    public String idSan; // Thêm ID sân

    public DatSan() {
        // Firebase cần constructor rỗng
    }

    public DatSan(String idDatSan, String tenNguoiDung, String soDienThoai, String gioBatDau, String gioSuDung, String idSan) {
        this.idDatSan = idDatSan;
        this.tenNguoiDung = tenNguoiDung;
        this.soDienThoai = soDienThoai;
        this.gioBatDau = gioBatDau;
        this.gioSuDung = gioSuDung;
        this.idSan = idSan;
    }
}

