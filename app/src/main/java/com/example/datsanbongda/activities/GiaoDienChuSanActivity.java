package com.example.datsanbongda.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datsanbongda.R;
import com.example.datsanbongda.adapters.ThongBaoAdapter;
import com.example.datsanbongda.models.ThongBaoDatSan;
import com.example.datsanbongda.models.SanBong;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class GiaoDienChuSanActivity extends AppCompatActivity {
    private TextView tvTenSan, tvDiaChiSan, tvGiaSan;

    private String idChuSan;
    private String currentTenSan, currentGiaSan, currentDiaChiSan;
    private DatabaseReference databaseReference, sanBongRef;
    private List<ThongBaoDatSan> danhSachThongBao;
    private ThongBaoAdapter adapter;
    private Button btnXemThongBao,btn_thongtinsan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.datsanbongda.R.layout.giaodienchusan);

        // Ánh xạ View
        tvTenSan = findViewById(com.example.datsanbongda.R.id.tv_ten_san);
        tvDiaChiSan = findViewById(com.example.datsanbongda.R.id.tv_diachi_san);
        tvGiaSan = findViewById(com.example.datsanbongda.R.id.tv_gia_san);
        btnXemThongBao = findViewById(com.example.datsanbongda.R.id.btnXemThongBao);
        btn_thongtinsan = findViewById(com.example.datsanbongda.R.id.btn_thongtinsan);
        // Nhận ID Chủ Sân từ Intent
        idChuSan = getIntent().getStringExtra("ID_CHU_SAN");

        if (idChuSan != null) {
            taiDuLieuSanBong(idChuSan);
        } else {
            Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
        }

        Button btnXemThongBao = findViewById(R.id.btnXemThongBao);
        btnXemThongBao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiaoDienChuSanActivity.this, ThongBaoDatSanActivity.class);
                startActivity(intent);
            }
        });
        btn_thongtinsan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiaoDienChuSanActivity.this,CapNhatThongTinActivity.class);
                intent.putExtra("sanId", idChuSan); // Truyền ID sân
                intent.putExtra("tenSan", currentTenSan);
                intent.putExtra("giaSan", currentGiaSan);
                intent.putExtra("diaChiSan", currentDiaChiSan);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        if (idChuSan != null) {
            taiDuLieuSanBong(idChuSan); // Gọi lại hàm load dữ liệu mới
        }
    }
    /** 📌 Tải thông tin sân bóng theo ID chủ sân */
    private void taiDuLieuSanBong(String idChuSan) {
        sanBongRef = FirebaseDatabase.getInstance()
                .getReference("SanBong").child(idChuSan);

        sanBongRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanBong sanBong = snapshot.getValue(SanBong.class);
                    if (sanBong != null) {
                        tvTenSan.setText(sanBong.tenSan);
                        currentTenSan = sanBong.tenSan;
                        currentGiaSan = String.valueOf(sanBong.giaSan);
                        currentDiaChiSan = sanBong.tinh + ", " + sanBong.thanhPhoHuyen;
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
