package com.example.datsanbongda;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GiaoDienNguoiDungActivity extends AppCompatActivity {

    private Spinner spinnerTinh, spinnerThanhPhoHuyen;
    private Button btnTimKiem;
    private ListView lvDanhSachSan;
    private DatabaseReference databaseReference;
    private List<String> danhSachSan;
    private ArrayAdapter<String> adapterSan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giaodiennguoidung);

        // Ánh xạ view
        spinnerTinh = findViewById(R.id.spinner_tinh);
        spinnerThanhPhoHuyen = findViewById(R.id.spinner_thanhpho_huyen);
        btnTimKiem = findViewById(R.id.btn_timkiem);
        lvDanhSachSan = findViewById(R.id.lv_danhsachsan);

        // Khởi tạo danh sách
        danhSachSan = new ArrayList<>();
        adapterSan = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, danhSachSan);
        lvDanhSachSan.setAdapter(adapterSan);

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("SanBong");

        // Load danh sách tỉnh
        loadDanhSachTinh();

        // Sự kiện tìm kiếm
        btnTimKiem.setOnClickListener(v -> timKiemSanBong());
    }

    private void loadDanhSachTinh() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> danhSachTinh = new ArrayList<>();
                for (DataSnapshot sanSnapshot : snapshot.getChildren()) {
                    String tinh = sanSnapshot.child("tinh").getValue(String.class);
                    if (tinh != null && !danhSachTinh.contains(tinh)) {
                        danhSachTinh.add(tinh);
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(GiaoDienNguoiDungActivity.this, android.R.layout.simple_spinner_dropdown_item, danhSachTinh);
                spinnerTinh.setAdapter(adapter);

                // Load Thành phố/Huyện khi chọn Tỉnh
                spinnerTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        loadDanhSachThanhPhoHuyen(danhSachTinh.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GiaoDienNguoiDungActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDanhSachThanhPhoHuyen(String tinh) {
        databaseReference.orderByChild("tinh").equalTo(tinh)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<String> danhSachThanhPhoHuyen = new ArrayList<>();
                        for (DataSnapshot sanSnapshot : snapshot.getChildren()) {
                            String thanhPhoHuyen = sanSnapshot.child("thanhPhoHuyen").getValue(String.class);
                            if (thanhPhoHuyen != null && !danhSachThanhPhoHuyen.contains(thanhPhoHuyen)) {
                                danhSachThanhPhoHuyen.add(thanhPhoHuyen);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(GiaoDienNguoiDungActivity.this, android.R.layout.simple_spinner_dropdown_item, danhSachThanhPhoHuyen);
                        spinnerThanhPhoHuyen.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GiaoDienNguoiDungActivity.this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void timKiemSanBong() {
        String tinhChon = spinnerTinh.getSelectedItem().toString();
        String thanhPhoHuyenChon = spinnerThanhPhoHuyen.getSelectedItem().toString();

        databaseReference.orderByChild("tinh").equalTo(tinhChon)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        danhSachSan.clear();
                        for (DataSnapshot sanSnapshot : snapshot.getChildren()) {
                            SanBong sanBong = sanSnapshot.getValue(SanBong.class);
                            if (sanBong != null && sanBong.thanhPhoHuyen.equals(thanhPhoHuyenChon)) {
                                danhSachSan.add(sanBong.tenSan + " - Giá: " + sanBong.giaSan + " VNĐ");
                            }
                        }
                        adapterSan.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(GiaoDienNguoiDungActivity.this, "Lỗi tìm kiếm!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
