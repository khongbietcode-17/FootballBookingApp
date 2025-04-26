package com.example.datsanbongda.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datsanbongda.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DatSanActivity extends AppCompatActivity {

    private EditText edtGioBatDau, edtGioSuDung;
    private EditText edtNgayDat;
    private TextView tvThanhTien;
    private int giaSan = 0; // Giá sân nhận từ Intent

    private String idSan, tenNguoiDung, soDienThoai;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datsan);
        edtNgayDat = findViewById(R.id.edt_ngay_dat); // nhớ tạo EditText này bên XML
        edtGioBatDau = findViewById(R.id.edt_gio_bat_dau);
        edtGioSuDung = findViewById(R.id.edt_gio_su_dung);
        tvThanhTien = findViewById(R.id.tv_thanh_tien);
        Button btnDatSan = findViewById(R.id.btn_dat_san);
        edtNgayDat.setOnClickListener(v -> showDatePicker());



        String giaSanStr = getIntent().getStringExtra("GIA_SAN");
        if (giaSanStr != null) {
            giaSan = Integer.parseInt(giaSanStr); // Chuyển thành số nguyên
        }

        edtGioSuDung.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                tinhThanhTien();
            }
        });

        databaseReference = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ThongBaoDatSan");

        btnDatSan.setOnClickListener(v -> datSan());

        // Ánh xạ TextView
        TextView tvIdSan = findViewById(R.id.tv_id_san);
        TextView tvTenSan = findViewById(R.id.tv_ten_san);
        TextView tvDiaChiSan = findViewById(R.id.tv_dia_chi_san);
        TextView tvGiaSan = findViewById(R.id.tv_gia_san);
        TextView tvTenNguoiDung = findViewById(R.id.tv_ten_nguoi_dung);
        TextView tvSoDienThoai = findViewById(R.id.tv_so_dien_thoai);

        // Nhận dữ liệu từ Intent
        String idSan = getIntent().getStringExtra("ID_SAN");
        String tenSan = getIntent().getStringExtra("TEN_SAN");
        String diaChiSan = getIntent().getStringExtra("DIA_CHI_SAN");
        String giaSan = getIntent().getStringExtra("GIA_SAN");
        String tenNguoiDung = getIntent().getStringExtra("TEN_NGUOI_DUNG");
        String soDienThoai = getIntent().getStringExtra("SO_DIEN_THOAI");


        // Hiển thị thông tin
        tvIdSan.setText("ID Sân: " + (idSan != null ? idSan : "Không có"));
        tvTenSan.setText("Tên sân bóng: " + (tenSan != null ? tenSan : "Không có"));
        tvDiaChiSan.setText("Địa chỉ: " + (diaChiSan != null ? diaChiSan : "Không có"));
        tvGiaSan.setText("Giá sân: " + (giaSan != null ? giaSan : "Không có")+".000 VNĐ");
        tvTenNguoiDung.setText("Tên: " + (tenNguoiDung != null ? tenNguoiDung : "Chưa có"));
        tvSoDienThoai.setText("SĐT: " + (soDienThoai != null ? soDienThoai : "Chưa có"));
    }
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    edtNgayDat.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
    private void tinhThanhTien() {
        String gioSuDungStr = edtGioSuDung.getText().toString().trim();
        if (!gioSuDungStr.isEmpty()) {
            int gioSuDung = Integer.parseInt(gioSuDungStr);
            int thanhTien = gioSuDung * giaSan;
            tvThanhTien.setText(thanhTien + ".000 VNĐ");
        } else {
            tvThanhTien.setText("0 VNĐ");
        }
    }
    private void datSan() {
        String tenNguoiDung = getIntent().getStringExtra("TEN_NGUOI_DUNG");
        String soDienThoai = getIntent().getStringExtra("SO_DIEN_THOAI");
        String gioBatDau = edtGioBatDau.getText().toString().trim();
        String gioSuDungStr = edtGioSuDung.getText().toString().trim();
        String idSan = getIntent().getStringExtra("ID_SAN");
        String ngayDat = edtNgayDat.getText().toString().trim();

        if (TextUtils.isEmpty(gioBatDau) || TextUtils.isEmpty(gioSuDungStr)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int gioSuDung = Integer.parseInt(gioSuDungStr);
        int thanhTien = gioSuDung * giaSan; // Tính thành tiền dựa vào giá sân

        DatabaseReference datSanRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("ThongBaoDatSan");

        String idDatSan = datSanRef.push().getKey();

        // Lưu dữ liệu đặt sân gồm thành tiền
        Map<String, Object> datSanData = new HashMap<>();
        datSanData.put("ngayDat", ngayDat);
        datSanData.put("idDatSan", idDatSan);
        datSanData.put("tenNguoiDung", tenNguoiDung);
        datSanData.put("soDienThoai", soDienThoai);
        datSanData.put("gioBatDau", gioBatDau);
        datSanData.put("gioSuDung", gioSuDung);
        datSanData.put("thanhTien", thanhTien);
        datSanData.put("idSan", idSan);

        datSanRef.child(idDatSan).setValue(datSanData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DatSanActivity.this, "Đặt sân thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DatSanActivity.this, "Lỗi khi đặt sân!", Toast.LENGTH_SHORT).show();
                });
    }
}
