package com.example.datsanbongda;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class GiaoDienChuSanActivity extends AppCompatActivity {
    private TextView tvTenSan, tvDiaChiSan, tvGiaSan;
    private ListView lvThongBao;
    private String idChuSan;

    private DatabaseReference databaseReference, sanBongRef;
    private List<ThongBaoDatSan> danhSachThongBao;
    private ThongBaoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giaodienchusan);

        // Ánh xạ View
        tvTenSan = findViewById(R.id.tv_ten_san);
        tvDiaChiSan = findViewById(R.id.tv_diachi_san);
        tvGiaSan = findViewById(R.id.tv_gia_san);
        lvThongBao = findViewById(R.id.lv_thongbao);

        // Khởi tạo danh sách và adapter
        danhSachThongBao = new ArrayList<>();
        adapter = new ThongBaoAdapter(this, danhSachThongBao);
        lvThongBao.setAdapter(adapter);

        // Nhận ID Chủ Sân từ Intent
        idChuSan = getIntent().getStringExtra("ID_CHU_SAN");

        if (idChuSan != null) {
            taiDuLieuSanBong(idChuSan);
            loadThongBao(idChuSan);
        } else {
            Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
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

    /** 📌 Tải danh sách thông báo đặt sân theo ID chủ sân */
    private void loadThongBao(String idChuSan) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("ThongBaoDatSan");

        databaseReference.orderByChild("idSan").equalTo(idChuSan)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        danhSachThongBao.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ThongBaoDatSan thongBao = dataSnapshot.getValue(ThongBaoDatSan.class);
                            if (thongBao != null) {
                                danhSachThongBao.add(thongBao);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "❌ Lỗi khi lấy danh sách thông báo: " + error.getMessage());
                    }
                });
    }

}
