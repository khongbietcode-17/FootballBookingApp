package com.example.datsanbongda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datsanbongda.R;
import com.example.datsanbongda.models.ChuSan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangKyChuSanActivity extends AppCompatActivity {
    private EditText etTenChuSan, etSoDienThoai, etTaiKhoan, etMatKhau;
    private Button btnDangKy;
    private DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangkychusan);

        // ✅ Đảm bảo sử dụng đúng URL Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("ChuSan");

        // Ánh xạ View
        etTenChuSan = findViewById(R.id.et_tenchusan);
        etSoDienThoai = findViewById(R.id.et_sodienthoai);
        etTaiKhoan = findViewById(R.id.et_taikhoan);
        etMatKhau = findViewById(R.id.et_matkhau);
        btnDangKy = findViewById(R.id.btn_dangkys);

        btnDangKy.setOnClickListener(v -> {

            dangkyChuSan();
        });
    }

    private void dangkyChuSan() {
        String tenChuSan = etTenChuSan.getText().toString().trim();
        String soDienThoai = etSoDienThoai.getText().toString().trim();
        String taiKhoan = etTaiKhoan.getText().toString().trim();
        String matKhau = etMatKhau.getText().toString().trim();

        // ✅ Kiểm tra dữ liệu nhập
        if (TextUtils.isEmpty(tenChuSan) || TextUtils.isEmpty(soDienThoai) ||
                TextUtils.isEmpty(taiKhoan) || TextUtils.isEmpty(matKhau)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.orderByChild("soDienThoai").equalTo(soDienThoai)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Log.d("Firebase", "⚠️ Số điện thoại đã tồn tại: " + soDienThoai);
                            Toast.makeText(DangKyChuSanActivity.this,
                                    "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Firebase", "✅ Số điện thoại chưa tồn tại, tiếp tục đăng ký...");
                            dangkyChuSanFirebase(tenChuSan, soDienThoai, taiKhoan, matKhau);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "❌ Lỗi khi kiểm tra số điện thoại: " + error.getMessage());
                    }
                });
    }

    private void dangkyChuSanFirebase(String tenChuSan, String soDienThoai, String taiKhoan, String matKhau) {
        String idChuSan = "NCS" + soDienThoai;
        ChuSan user = new ChuSan(idChuSan, tenChuSan, soDienThoai, taiKhoan, matKhau);

        databaseReference.child(idChuSan).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "🎉 Đăng ký thành công với ID: " + idChuSan);
                    Toast.makeText(DangKyChuSanActivity.this,
                            "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    // ✅ Chuyển màn hình và truyền ID chủ sân
                    Intent intent = new Intent(DangKyChuSanActivity.this, DangKySanActivity.class);
                    intent.putExtra("idChuSan", idChuSan); // Truyền ID chủ sân
                    startActivity(intent);
                    finish(); // Đóng màn hình đăng ký chủ sân
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "❌ Đăng ký thất bại: " + e.getMessage());
                    Toast.makeText(DangKyChuSanActivity.this,
                            "Đăng ký thất bại: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}