package com.example.datsanbongda.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datsanbongda.R;
import com.example.datsanbongda.adapters.ThongBaoAdapter;
import com.example.datsanbongda.models.ThongBaoDatSan;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ThongBaoDatSanActivity  extends AppCompatActivity {
    private TextView tvNgayDangXem;
    private Button btnChonNgay;
    private RecyclerView rvThongBaoDatSan;
    private ThongBaoAdapter adapter;
    private ArrayList<ThongBaoDatSan> list;
    private DatabaseReference dbRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thongbaodatsan);

    tvNgayDangXem = findViewById(R.id.tvNgayDangXem);
    btnChonNgay = findViewById(R.id.btnChonNgay);
    rvThongBaoDatSan = findViewById(R.id.rvThongBaoDatSan);

    rvThongBaoDatSan.setLayoutManager(new LinearLayoutManager(this));
    list = new ArrayList<>();
    adapter = new ThongBaoAdapter(this, list);
    rvThongBaoDatSan.setAdapter(adapter);

    dbRef = FirebaseDatabase.getInstance().getReference("ThongBaoDatSan");

    // Load ngày hôm nay
    String ngayHienTai = getCurrentDate();
    tvNgayDangXem.setText("Ngày: " + ngayHienTai);
    loadDataTheoNgay(ngayHienTai);

    // Bắt sự kiện chọn ngày
    btnChonNgay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chonNgay();
        }
    });

    }
    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // tháng +1 vì Java tính tháng từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day + "/" + month + "/" + year;
    }
    private void loadDataTheoNgay(String ngayChon) {
        dbRef.orderByChild("ngayDat").equalTo(ngayChon)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ThongBaoDatSan thongBao = dataSnapshot.getValue(ThongBaoDatSan.class);
                            list.add(thongBao);
                        }
                        adapter.notifyDataSetChanged();
                    }


                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
    private void chonNgay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // KHÔNG THÊM SỐ 0 VÀO TRƯỚC
                String ngayChon = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvNgayDangXem.setText("Ngày: " + ngayChon);
                loadDataTheoNgay(ngayChon);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}
