package com.example.datsanbongda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;
import java.util.List;

public class ThongBaoAdapter extends ArrayAdapter<ThongBaoDatSan> {
    private Context context;
    private List<ThongBaoDatSan> danhSachThongBao;

    public ThongBaoAdapter(Context context, List<ThongBaoDatSan> danhSachThongBao) {
        super(context, 0, danhSachThongBao);
        this.context = context;
        this.danhSachThongBao = danhSachThongBao;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_thongbao, parent, false);
        }

        ThongBaoDatSan thongBao = danhSachThongBao.get(position);

        TextView tvTenNguoiDung = convertView.findViewById(R.id.tv_ten_nguoi_dung);
        TextView tvSoDienThoai = convertView.findViewById(R.id.tv_so_dien_thoai);
        TextView tvGioBatDau = convertView.findViewById(R.id.tv_gio_bat_dau);
        TextView tvGioSuDung = convertView.findViewById(R.id.tv_gio_su_dung);

        tvTenNguoiDung.setText("Tên: " + thongBao.getTenNguoiDung());
        tvSoDienThoai.setText("SĐT: " + thongBao.getSoDienThoai());
        tvGioBatDau.setText("Giờ bắt đầu: " + thongBao.getGioBatDau());
        tvGioSuDung.setText("Giờ sử dụng: " + thongBao.getGioSuDung());

        return convertView;
    }
}
