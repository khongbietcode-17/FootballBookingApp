package com.example.datsanbongda.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datsanbongda.adapters.LichSuDatSanAdapter;
import com.example.datsanbongda.R;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LichSuDatSanActivity extends AppCompatActivity {
    private ListView lvLichSu;
    private DatabaseReference databaseReference;
    private List<Map<String, String>> danhSachLichSu;
    private LichSuDatSanAdapter adapterLichSu;
    private String soDienThoaiNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.datsanbongda.R.layout.lichsudatsan);

        lvLichSu = findViewById(R.id.lv_lichsu);
        soDienThoaiNguoiDung = getIntent().getStringExtra("SO_DIEN_THOAI");

        danhSachLichSu = new ArrayList<>();
        adapterLichSu = new LichSuDatSanAdapter(this, danhSachLichSu);
        lvLichSu.setAdapter(adapterLichSu);


        databaseReference = FirebaseDatabase.getInstance()
                .getReference("ThongBaoDatSan");

        loadLichSuDatSan();
    }

    private void loadLichSuDatSan() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                danhSachLichSu.clear();

                for (DataSnapshot datSanSnapshot : snapshot.getChildren()) {
                    String soDienThoai = datSanSnapshot.child("soDienThoai").getValue(String.class);

                    if (soDienThoai != null && soDienThoai.equals(soDienThoaiNguoiDung)) {
                        Map<String, String> lichSu = new HashMap<>();
                        lichSu.put("tenNguoiDung", datSanSnapshot.child("tenNguoiDung").getValue(String.class));
                        lichSu.put("soDienThoai", soDienThoai);
                        lichSu.put("gioBatDau", String.valueOf(datSanSnapshot.child("gioBatDau").getValue()));
                        lichSu.put("gioSuDung", String.valueOf(datSanSnapshot.child("gioSuDung").getValue()));


                        danhSachLichSu.add(lichSu);
                    }
                }
                adapterLichSu.notifyDataSetChanged();


                adapterLichSu.notifyDataSetChanged();
                if (danhSachLichSu.isEmpty()) {
                    Toast.makeText(LichSuDatSanActivity.this, "Không có lịch sử đặt sân!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LichSuDatSanActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
