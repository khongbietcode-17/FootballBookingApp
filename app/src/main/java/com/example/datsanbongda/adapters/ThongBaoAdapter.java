package com.example.datsanbongda.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datsanbongda.R;
import com.example.datsanbongda.models.ThongBaoDatSan;

import java.util.List;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.ThongBaoViewHolder> {

    private Context context;
    private List<ThongBaoDatSan> danhSachThongBao;

    public ThongBaoAdapter(Context context, List<ThongBaoDatSan> danhSachThongBao) {
        this.context = context;
        this.danhSachThongBao = danhSachThongBao;
    }

    @NonNull
    @Override
    public ThongBaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.item_thongbao, parent, false);
        return new ThongBaoViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull ThongBaoViewHolder holder, int position) {
        ThongBaoDatSan thongBao = danhSachThongBao.get(position);

        holder.tvTenNguoiDung.setText("Tên: " + thongBao.getTenNguoiDung());
        holder.tvSoDienThoai.setText("SĐT: " + thongBao.getSoDienThoai());
        holder.tvNgayDat.setText("Ngày sử dụng: " + thongBao.getNgayDat());
        holder.tvGioBatDau.setText("Giờ bắt đầu: " + thongBao.getGioBatDau());
        holder.tvGioSuDung.setText("Giờ sử dụng: " + thongBao.getGioSuDung());
    }

    @Override
    public int getItemCount() {
        return danhSachThongBao.size();
    }

    public static class ThongBaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenNguoiDung, tvSoDienThoai, tvGioBatDau, tvGioSuDung, tvNgayDat;

        public ThongBaoViewHolder(View itemView) {
            super(itemView);
            tvTenNguoiDung = itemView.findViewById(R.id.tv_ten_nguoi_dung);
            tvSoDienThoai = itemView.findViewById(R.id.tv_so_dien_thoai);
            tvGioBatDau = itemView.findViewById(R.id.tv_gio_bat_dau);
            tvGioSuDung = itemView.findViewById(R.id.tv_gio_su_dung);
            tvNgayDat = itemView.findViewById(R.id.tv_ngay_dat_san);
        }
    }
}
