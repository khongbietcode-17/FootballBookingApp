package com.example.datsanbongda.models;

public class SanBong {
    public String idSan, tenSan, tinh, thanhPhoHuyen, giaSan, diaChi;

    public SanBong() { }

    public SanBong(String idSan, String tenSan, String tinh, String thanhPhoHuyen, String giaSan) {
        this.idSan = idSan;
        this.tenSan = tenSan;
        this.tinh = tinh;
        this.thanhPhoHuyen = thanhPhoHuyen;
        this.giaSan = giaSan;
        this.diaChi = thanhPhoHuyen + ", " + tinh; // Tạo địa chỉ cụ thể
    }

    public String getIdSan() {
        return idSan;
    }

    public String getTenSan() {
        return tenSan;
    }

    public String getTinh() {
        return tinh;
    }

    public String getThanhPhoHuyen() {
        return thanhPhoHuyen;
    }

    public String getGiaSan() {
        return giaSan;
    }

    public String getDiaChi() {
        return diaChi;
    }
}
