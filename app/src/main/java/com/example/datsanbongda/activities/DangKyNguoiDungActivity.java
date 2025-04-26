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
import com.example.datsanbongda.models.NguoiDung;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangKyNguoiDungActivity extends AppCompatActivity {
    private EditText etTenNguoiDung, etSoDienThoai, etTaiKhoan, etMatKhau;
    private Button btnDangKy;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangkynguoidung);

        // ✅ Đảm bảo sử dụng đúng URL Database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("NguoiDung");

        // ✅ Kiểm tra kết nối Firebase
        firebaseDatabase.getReference(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.d("Firebase", "✅ Firebase đã kết nối!");
                } else {
                    Log.e("Firebase", "❌ Firebase chưa kết nối!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi kết nối Firebase: " + error.getMessage());
            }
        });

        // Ánh xạ View
        etTenNguoiDung = findViewById(R.id.et_tennguoidung);
        etSoDienThoai = findViewById(R.id.et_sodienthoai);
        etTaiKhoan = findViewById(R.id.et_taikhoan);
        etMatKhau = findViewById(R.id.et_matkhau);
        btnDangKy = findViewById(R.id.btn_dangkys);

        btnDangKy.setOnClickListener(v -> {

            registerUser();
        });
    }

    private void registerUser() {
        String tenNguoiDung = etTenNguoiDung.getText().toString().trim();
        String soDienThoai = etSoDienThoai.getText().toString().trim();
        String taiKhoan = etTaiKhoan.getText().toString().trim();
        String matKhau = etMatKhau.getText().toString().trim();

        // ✅ Kiểm tra dữ liệu nhập
        if (TextUtils.isEmpty(tenNguoiDung) || TextUtils.isEmpty(soDienThoai) ||
                TextUtils.isEmpty(taiKhoan) || TextUtils.isEmpty(matKhau)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // ✅ Kiểm tra xem databaseReference có null không
        if (databaseReference == null) {
            Log.e("Firebase", "❌ databaseReference bị null!");
            return;
        }

        Log.d("Firebase", "🔍 Kiểm tra số điện thoại: " + soDienThoai);

        // ✅ Kiểm tra số điện thoại đã tồn tại chưa
        databaseReference.orderByChild("soDienThoai").equalTo(soDienThoai)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Log.d("Firebase", "⚠️ Số điện thoại đã tồn tại: " + soDienThoai);
                            Toast.makeText(DangKyNguoiDungActivity.this,
                                    "Số điện thoại đã được đăng ký!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Firebase", "✅ Số điện thoại chưa tồn tại, tiếp tục đăng ký...");
                            saveUserToFirebase(tenNguoiDung, soDienThoai, taiKhoan, matKhau);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "❌ Lỗi khi kiểm tra số điện thoại: " + error.getMessage());
                    }
                });
    }

    private void saveUserToFirebase(String tenNguoiDung, String soDienThoai, String taiKhoan, String matKhau) {
        String idNguoiDung = "NDS" + soDienThoai;
        NguoiDung user = new NguoiDung(idNguoiDung, tenNguoiDung, soDienThoai, taiKhoan, matKhau);

        databaseReference.child(idNguoiDung).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "🎉 Đăng ký thành công với ID: " + idNguoiDung);
                    Toast.makeText(DangKyNguoiDungActivity.this,
                            "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(DangKyNguoiDungActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Kết thúc activity hiện tại để không quay lại khi nhấn Back
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "❌ Đăng ký thất bại: " + e.getMessage());
                    Toast.makeText(DangKyNguoiDungActivity.this,
                            "Đăng ký thất bại: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}
