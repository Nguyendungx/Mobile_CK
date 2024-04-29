package com.example.appbanthietbidientu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.appbanthietbidientu.R;
import com.example.appbanthietbidientu.model.DonHang;

import java.util.ArrayList;
import java.util.List;

public class DonHangAdapter extends BaseAdapter implements Filterable {
    private List<DonHang> donHangList, donHangoldList;
    private Context context;

    public DonHangAdapter(List<DonHang> donHangList, Context context) {
        this.donHangList = donHangList;
        this.donHangoldList = donHangList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return donHangList.size();
    }

    @Override
    public Object getItem(int position) {
        return donHangList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_donhang, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.idDonHang = view.findViewById(R.id.idDonHang);
            viewHolder.idUser = view.findViewById(R.id.idUser);
            viewHolder.idQt = view.findViewById(R.id.idQt);
            viewHolder.idTotal = view.findViewById(R.id.idTotal);
            viewHolder.idNgayDat = view.findViewById(R.id.idNgayDat);
            viewHolder.idTrangThai= view.findViewById(R.id.idTrangThai);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        DonHang donHang = donHangList.get(position);

        // Hiển thị thông tin đơn hàng
        viewHolder.idDonHang.setText(donHang.getId());
        viewHolder.idUser.setText("ID Khách: " + donHang.getUser());
        viewHolder.idQt.setText("Qt: " + donHang.getDanhSachSanPham().size()); // Đổi thành kích thước danh sách sản phẩm
        viewHolder.idTotal.setText("Total: $" + donHang.getTotal());
        viewHolder.idNgayDat.setText(String.valueOf(donHang.getThoiGian())); // Sử dụng thời gian đặt hàng
        viewHolder.idTrangThai.setText("Trạng thái đơn hàng: "+donHang.getTrangThai()); // Sử dụng thời gian đặt hàng


        return view;
    }

    private static class ViewHolder {
        TextView idDonHang;
        TextView idUser;
        TextView idQt;
        TextView idTotal;
        TextView idNgayDat;
        TextView idTrangThai;
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString().toLowerCase();
                if (strSearch.isEmpty()) {
                    donHangList = donHangoldList;
                } else {
                    List<DonHang> list = new ArrayList<>();
                    for (DonHang sanpham : donHangoldList) {
                        if (sanpham.getUser().toLowerCase().contains(strSearch)) {
                            list.add(sanpham);
                        }
                    }
                    donHangList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = donHangList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                donHangList = (List<DonHang>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
