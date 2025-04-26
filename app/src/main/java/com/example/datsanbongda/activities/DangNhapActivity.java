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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhapActivity extends AppCompatActivity {
    private EditText etTaiKhoan, etMatKhau;
    private Button btnDangNhap;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dangnhap);

        // Ánh xạ View
        etTaiKhoan = findViewById(R.id.et_taikhoan);
        etMatKhau = findViewById(R.id.et_matkhau);
        btnDangNhap = findViewById(R.id.btn_dangnhap);

        // Sự kiện đăng nhập
        btnDangNhap.setOnClickListener(v -> dangNhap());
    }

    private void dangNhap() {
        String taiKhoan = etTaiKhoan.getText().toString().trim();
        String matKhau = etMatKhau.getText().toString().trim();

        // Kiểm tra dữ liệu nhập
        if (TextUtils.isEmpty(taiKhoan) || TextUtils.isEmpty(matKhau)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("ChuSan");

        // Kiểm tra trong danh sách chủ sân
        databaseReference.orderByChild("taiKhoan").equalTo(taiKhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String passwordDB = userSnapshot.child("matKhau").getValue(String.class);
                                if (passwordDB != null && passwordDB.equals(matKhau)) {
                                    String id = userSnapshot.getKey();
                                    xuLyDangNhap(id);
                                    return;
                                }
                            }
                        }

                        // Nếu không tìm thấy trong Chủ Sân, kiểm tra Người Dùng
                        kiemTraNguoiDung(taiKhoan, matKhau);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Lỗi khi kiểm tra tài khoản: " + error.getMessage());
                    }
                });
    }

    private void kiemTraNguoiDung(String taiKhoan, String matKhau) {
        DatabaseReference nguoiDungRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("NguoiDung");

        nguoiDungRef.orderByChild("taiKhoan").equalTo(taiKhoan)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String passwordDB = userSnapshot.child("matKhau").getValue(String.class);
                                if (passwordDB != null && passwordDB.equals(matKhau)) {
                                    String id = userSnapshot.getKey();
                                    xuLyDangNhap(id);
                                    return;
                                }
                            }
                        }
                        Toast.makeText(DangNhapActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Lỗi khi kiểm tra tài khoản: " + error.getMessage());
                    }
                });
    }

    private void xuLyDangNhap(String id) {
        DatabaseReference userRef;
        Intent intent;

        if (id.startsWith("NCS")) { // Nếu là Chủ Sân
            userRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("ChuSan").child(id);
            intent = new Intent(DangNhapActivity.this, GiaoDienChuSanActivity.class);
            intent.putExtra("ID_CHU_SAN", id);
        } else if (id.startsWith("NDS")) { // Nếu là Người Dùng
            userRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("NguoiDung").child(id);
            intent = new Intent(DangNhapActivity.this, GiaoDienNguoiDungActivity.class);
            intent.putExtra("ID_NGUOI_DUNG", id);
        } else {
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy thông tin chi tiết từ Firebase
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String tenNguoiDung = snapshot.child("tenNguoiDung").getValue(String.class);
                    String soDienThoai = snapshot.child("soDienThoai").getValue(String.class);

                    intent.putExtra("TEN_NGUOI_DUNG", tenNguoiDung);
                    intent.putExtra("SO_DIEN_THOAI", soDienThoai);

                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DangNhapActivity.this, "Không tìm thấy thông tin tài khoản!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Lỗi khi lấy thông tin người dùng: " + error.getMessage());
            }
        });
    }


}