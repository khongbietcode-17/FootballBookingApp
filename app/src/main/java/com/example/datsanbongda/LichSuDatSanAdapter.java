package com.example.datsanbongda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class LichSuDatSanAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> lichSuList;
    private LayoutInflater inflater;

    public LichSuDatSanAdapter(Context context, List<Map<String, String>> lichSuList) {
        this.context = context;
        this.lichSuList = lichSuList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lichSuList.size();
    }

    @Override
    public Object getItem(int position) {
        return lichSuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView tvTenNguoiDung, tvSoDienThoai, tvGioBatDau, tvGioSuDung;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lichsudatsan_item, parent, false);
            holder = new ViewHolder();
            holder.tvTenNguoiDung = convertView.findViewById(R.id.tvTenNguoiDat);
            holder.tvSoDienThoai = convertView.findViewById(R.id.tvSoDienThoai);
            holder.tvGioBatDau = convertView.findViewById(R.id.tvGioBatDau);
            holder.tvGioSuDung = convertView.findViewById(R.id.tvGioSuDung);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, String> lichSu = lichSuList.get(position);
        holder.tvTenNguoiDung.setText(lichSu.get("tenNguoiDung"));
        holder.tvSoDienThoai.setText("SĐT: " + lichSu.get("soDienThoai"));
        holder.tvGioBatDau.setText("Giờ bắt đầu: " + lichSu.get("gioBatDau"));
        holder.tvGioSuDung.setText("Thời lượng: " + lichSu.get("gioSuDung") + " giờ");

        return convertView;
    }
}
