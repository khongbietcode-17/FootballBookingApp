package com.example.datsanbongda;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GiaoDienChuSanActivity extends AppCompatActivity {
    private TextView tvTenSan, tvDiaChiSan, tvGiaSan;
    private DatabaseReference sanBongRef;
    private String idChuSan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giaodienchusan);

        // Ánh xạ View
        tvTenSan = findViewById(R.id.tv_ten_san);
        tvDiaChiSan = findViewById(R.id.tv_diachi_san);
        tvGiaSan = findViewById(R.id.tv_gia_san);

        // Nhận ID Chủ Sân từ Intent
        idChuSan = getIntent().getStringExtra("ID_CHU_SAN");

        if (idChuSan != null) {
            taiDuLieuSanBong(idChuSan);
        } else {
            Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
        }
    }

    private void taiDuLieuSanBong(String idChuSan) {
        sanBongRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("SanBong").child(idChuSan); // 🛑 Lấy sân bóng theo ID Chủ Sân

        sanBongRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanBong sanBong = snapshot.getValue(SanBong.class);
                    if (sanBong != null) {
                        // Cập nhật giao diện với dữ liệu lấy được
                        tvTenSan.setText(sanBong.tenSan);
                        tvDiaChiSan.setText(sanBong.tinh + ", " + sanBong.thanhPhoHuyen);
                        tvGiaSan.setText("Giá: " + sanBong.giaSan + " VNĐ");
                    }
                } else {
                    Log.e("Firebase", "❌ Không tìm thấy dữ liệu sân bóng!");
                    Toast.makeText(GiaoDienChuSanActivity.this, "Không tìm thấy dữ liệu sân!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "❌ Lỗi khi tải dữ liệu sân: " + error.getMessage());
                Toast.makeText(GiaoDienChuSanActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
