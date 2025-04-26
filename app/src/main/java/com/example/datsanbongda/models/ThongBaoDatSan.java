package com.example.datsanbongda.models;

public class ThongBaoDatSan {
    private String idDatSan;
    private String idSan;
    private String tenNguoiDung;
    private String soDienThoai;
    private String gioBatDau;

    private String ngayDat;
    private int gioSuDung;
    private int thanhTien;

    // Constructor không tham số (bắt buộc cho Firebase)
    public ThongBaoDatSan() {
    }

    // Constructor đầy đủ
    public ThongBaoDatSan(String idDatSan, String idSan, String tenNguoiDung, String soDienThoai,
                          String gioBatDau,String ngayDat, int gioSuDung, int thanhTien) {
        this.idDatSan = idDatSan;
        this.idSan = idSan;
        this.tenNguoiDung = tenNguoiDung;
        this.soDienThoai = soDienThoai;
        this.gioBatDau = gioBatDau;
        this.gioSuDung = gioSuDung;
        this.thanhTien = thanhTien;
        this.ngayDat = ngayDat;
    }

    // Getter & Setter
    public String getIdDatSan() {
        return idDatSan;
    }

    public void setIdDatSan(String idDatSan) {
        this.idDatSan = idDatSan;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }
    public String getIdSan() {
        return idSan;
    }

    public void setIdSan(String idSan) {
        this.idSan = idSan;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public int getGioSuDung() {
        return gioSuDung;
    }

    public void setGioSuDung(int gioSuDung) {
        this.gioSuDung = gioSuDung;
    }

    public int getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(int thanhTien) {
        this.thanhTien = thanhTien;
    }
}
