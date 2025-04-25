package com.example.datsanbongda;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ThemDuLieuDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseReference chuSanRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("ChuSan");
        DatabaseReference sanBongRef = FirebaseDatabase.getInstance("https://dbdatsanbongda-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("SanBong");

        String[] hoTenChuSan = {
                "Nguyễn Văn A", "Trần Thị B", "Lê Hoàng C", "Phạm Văn D", "Đỗ Thị E",
                "Bùi Văn F", "Võ Thị G", "Huỳnh Văn H", "Đặng Thị I", "Ngô Văn J"
        };

        String[] tenSanBong = {
                "Sân Cỏ 123", "Sân Bóng Mặt Trời", "Sân Thể Thao Tân Phú", "Sân Vàng",
                "Sân Mini Long Xuyên", "Sân Đồng Quê", "Sân Thi Đấu", "Sân Hoàng Gia",
                "Sân Phố Núi", "Sân Chợ Mới"
        };

        String[] huyenAnGiang = {
                "TP. Long Xuyên", "TP. Châu Đốc", "Thị xã Tân Châu", "Huyện An Phú", "Huyện Châu Phú",
                "Huyện Châu Thành", "Huyện Phú Tân", "Huyện Thoại Sơn", "Huyện Tri Tôn", "Huyện Chợ Mới"
        };

        for (int i = 0; i < 10; i++) {
            String soDienThoai = "090000000" + i;
            String idChuSan = "NCS" + soDienThoai;

            ChuSan chuSan = new ChuSan(
                    idChuSan,
                    hoTenChuSan[i],
                    soDienThoai,
                    "taikhoan" + i,
                    "matkhau" + i
            );

            int gia = 150000 + (i * 10000);
            SanBong sanBong = new SanBong(
                    idChuSan,
                    tenSanBong[i],
                    "An Giang",
                    huyenAnGiang[i],
                    String.valueOf(gia)
            );

            chuSanRef.child(idChuSan).setValue(chuSan);
            sanBongRef.child(idChuSan).setValue(sanBong);
        }

        finish(); // Đóng Activity sau khi thêm dữ liệu
    }
}
