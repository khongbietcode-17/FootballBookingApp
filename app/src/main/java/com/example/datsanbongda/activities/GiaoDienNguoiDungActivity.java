package com.example.datsanbongda.activities;

import android.content.Intent;
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

import com.example.datsanbongda.R;
import com.example.datsanbongda.adapters.SanBongAdapter;
import com.example.datsanbongda.models.SanBong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GiaoDienNguoiDungActivity extends AppCompatActivity {

    private Spinner spinnerTinh, spinnerThanhPhoHuyen;
    private Button btnTimKiem, btnXemLichSu;
    private ListView lvDanhSachSan;
    private DatabaseReference databaseReference;
    private List<String> danhSachSan;
    private List<SanBong> danhSachSanBong;
    private SanBongAdapter adapterSan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.giaodiennguoidung);

        // Ánh xạ view
        btnXemLichSu = findViewById(R.id.btn_xemlichsu);
        spinnerTinh = findViewById(R.id.spinner_tinh);
        spinnerThanhPhoHuyen = findViewById(R.id.spinner_thanhpho_huyen);
        btnTimKiem = findViewById(R.id.btn_timkiem);
        lvDanhSachSan = findViewById(R.id.lv_danhsachsan);
        TextView tvTenNguoiDung = findViewById(R.id.tv_ten_nguoi_dung);
        TextView tvSoDienThoai = findViewById(R.id.tv_so_dien_thoai);

        // Khởi tạo danh sách
        danhSachSanBong = new ArrayList<>();
        adapterSan = new SanBongAdapter(this, danhSachSanBong);
        lvDanhSachSan.setAdapter(adapterSan);


        // Nhận dữ liệu từ Intent
        String tenNguoiDung = getIntent().getStringExtra("TEN_NGUOI_DUNG");
        String soDienThoai = getIntent().getStringExtra("SO_DIEN_THOAI");

        // Hiển thị thông tin người dùng
        tvTenNguoiDung.setText("" + (tenNguoiDung != null ? tenNguoiDung : "Chưa có"));
        tvSoDienThoai.setText("SĐT: " + (soDienThoai != null ? soDienThoai : "Chưa có"));

        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("SanBong");

        // Load danh sách tỉnh
        loadDanhSachTinh();



        // Sự kiện tìm kiếm
        btnTimKiem.setOnClickListener(v -> timKiemSanBong());

        btnXemLichSu.setOnClickListener(v -> {
            Intent intent = new Intent(GiaoDienNguoiDungActivity.this, LichSuDatSanActivity.class);
            // Truyền số điện thoại để lấy lịch sử đặt sân của người dùng
            intent.putExtra("SO_DIEN_THOAI", getIntent().getStringExtra("SO_DIEN_THOAI"));
            startActivity(intent);
        });


        lvDanhSachSan.setOnItemClickListener((parent, view, position, id) -> {
            if (position < danhSachSanBong.size()) {
                SanBong sanChon = danhSachSanBong.get(position);

                Intent intent = new Intent(GiaoDienNguoiDungActivity.this, DatSanActivity.class);
                intent.putExtra("ID_SAN", sanChon.idSan);
                intent.putExtra("TEN_SAN", sanChon.tenSan);
                intent.putExtra("DIA_CHI_SAN", sanChon.getDiaChi()); // Sử dụng địa chỉ cụ thể
                intent.putExtra("GIA_SAN", sanChon.giaSan);
                intent.putExtra("TEN_NGUOI_DUNG", getIntent().getStringExtra("TEN_NGUOI_DUNG"));
                intent.putExtra("SO_DIEN_THOAI", getIntent().getStringExtra("SO_DIEN_THOAI"));

                startActivity(intent);
            } else {
                Toast.makeText(GiaoDienNguoiDungActivity.this, "Lỗi: Không tìm thấy sân!", Toast.LENGTH_SHORT).show();
            }
        });

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
        if (spinnerTinh.getSelectedItem() == null || spinnerThanhPhoHuyen.getSelectedItem() == null) {
            Toast.makeText(this, "Vui lòng chọn tỉnh và thành phố/huyện!", Toast.LENGTH_SHORT).show();
            return;
        }

        String tinhChon = spinnerTinh.getSelectedItem().toString();
        String thanhPhoHuyenChon = spinnerThanhPhoHuyen.getSelectedItem().toString();

        databaseReference.orderByChild("tinh").equalTo(tinhChon)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        danhSachSanBong.clear();

                        for (DataSnapshot sanSnapshot : snapshot.getChildren()) {
                            String idSan = sanSnapshot.child("idSan").getValue(String.class);
                            String tenSan = sanSnapshot.child("tenSan").getValue(String.class);
                            String tinh = sanSnapshot.child("tinh").getValue(String.class);
                            String thanhPhoHuyen = sanSnapshot.child("thanhPhoHuyen").getValue(String.class);
                            String giaSan = sanSnapshot.child("giaSan").getValue(String.class);

                            if (thanhPhoHuyen != null && thanhPhoHuyen.equals(thanhPhoHuyenChon)) {
                                danhSachSanBong.add(new SanBong(idSan, tenSan, tinh, thanhPhoHuyen, giaSan));
                            }
                        }

                        if (danhSachSanBong.isEmpty()) {
                            Toast.makeText(GiaoDienNguoiDungActivity.this, "Không tìm thấy sân bóng phù hợp!", Toast.LENGTH_SHORT).show();
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
