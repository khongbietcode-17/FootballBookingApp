package com.example.datsanbongda;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DangKySanActivity extends AppCompatActivity {
    private EditText etTenSan, etGiaSan;
    private Spinner spinnerTinh, spinnerThanhPhoHuyen;
    private Button btnDangKy;
    private DatabaseReference databaseReference;
    private String idChuSan; // ID của chủ sân

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangkysan);

        // ✅ Nhận ID chủ sân từ Intent
        idChuSan = getIntent().getStringExtra("idChuSan");

        // ✅ Kết nối Firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("SanBong");

        // ✅ Ánh xạ View
        etTenSan = findViewById(R.id.et_tensan);
        spinnerTinh = findViewById(R.id.spinner_tinh);
        spinnerThanhPhoHuyen = findViewById(R.id.spinner_thanhpho_huyen);
        etGiaSan = findViewById(R.id.et_giasan);
        btnDangKy = findViewById(R.id.btn_dangkys);

        // ✅ Thiết lập dữ liệu cho Spinner Tỉnh
        String[] tinhOptions = {"An Giang"};
        ArrayAdapter<String> tinhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tinhOptions);
        spinnerTinh.setAdapter(tinhAdapter);

        // ✅ Thiết lập dữ liệu cho Spinner Thành phố/Huyện
        String[] thanhPhoOptions = {"TP. Long Xuyên", "Tri Tôn"};
        ArrayAdapter<String> thanhPhoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, thanhPhoOptions);
        spinnerThanhPhoHuyen.setAdapter(thanhPhoAdapter);

        btnDangKy.setOnClickListener(v -> dangkySanBong());
    }

    private void dangkySanBong() {
        String tenSanBong = etTenSan.getText().toString().trim();
        String tinh = spinnerTinh.getSelectedItem().toString();
        String thanhPhoHuyen = spinnerThanhPhoHuyen.getSelectedItem().toString();
        String giaSan = etGiaSan.getText().toString().trim();

        // ✅ Kiểm tra dữ liệu nhập
        if (TextUtils.isEmpty(tenSanBong) || TextUtils.isEmpty(giaSan)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Dùng idChuSan làm ID sân bóng
        String idSanBong = idChuSan;

        SanBong sanBong = new SanBong(idSanBong, tenSanBong, tinh, thanhPhoHuyen, giaSan);

        databaseReference.child(idSanBong).setValue(sanBong)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "🎉 Đăng ký sân bóng thành công với ID: " + idSanBong);
                    Toast.makeText(this, "Đăng ký sân bóng thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "❌ Đăng ký thất bại: " + e.getMessage());
                    Toast.makeText(this, "Đăng ký thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
