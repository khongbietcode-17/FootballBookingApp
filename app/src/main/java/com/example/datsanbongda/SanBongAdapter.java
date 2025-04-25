package com.example.datsanbongda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.List;

public class SanBongAdapter extends BaseAdapter {
    private Context context;
    private List<SanBong> danhSachSanBong;

    public SanBongAdapter(Context context, List<SanBong> danhSachSanBong) {
        this.context = context;
        this.danhSachSanBong = danhSachSanBong;
    }

    @Override
    public int getCount() {
        return danhSachSanBong.size();
    }

    @Override
    public Object getItem(int position) {
        return danhSachSanBong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_san_bong, parent, false);
        }

        // Ánh xạ View
        TextView tvIdSan = convertView.findViewById(R.id.tv_id_san);
        TextView tvTenSan = convertView.findViewById(R.id.tv_ten_san);
        TextView tvGiaSan = convertView.findViewById(R.id.tv_gia_san);
        TextView tvDiaChi = convertView.findViewById(R.id.tv_dia_chi);

        // Lấy dữ liệu
        SanBong sanBong = danhSachSanBong.get(position);

        // Hiển thị dữ liệu
        tvIdSan.setText("ID: " + sanBong.idSan);
        tvTenSan.setText("" + sanBong.tenSan);
        tvGiaSan.setText("Giá: " + sanBong.giaSan + ".000 VNĐ");
        tvDiaChi.setText("Địa chỉ: " + sanBong.getDiaChi());

        return convertView;
    }
}
