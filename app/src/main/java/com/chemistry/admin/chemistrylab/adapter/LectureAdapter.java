package com.chemistry.admin.chemistrylab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chemistry.admin.chemistrylab.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/19/2016.
 */

public class LectureAdapter extends BaseAdapter {
    private List<ItemLecture> listItemLecture;
    private LayoutInflater inflater;

    public LectureAdapter(Context context){
        inflater = LayoutInflater.from(context);
        initListLecture();
    }

    private void initListLecture() {
        listItemLecture = new ArrayList<>();
        listItemLecture.add(new ItemLecture("Chap1_KhaiNiemVaDinhLuatCoBan.pdf",
                                            "Khái Niệm Và Các Định Luật Cơ Bản"));
        listItemLecture.add(new ItemLecture("Chap2_CauTaoNTvaHTTH.pdf",
                                            "Cấu Tạo Nguyên Tố và Hệ Thống Tuần Hoàn"));
        listItemLecture.add(new ItemLecture("Chap3_CauTaoPTvaLienKetHH.pdf",
                                            "Cấu Tạo Phân Tử và Liên Kết Hoá Học"));
        listItemLecture.add(new ItemLecture("Chap4_NhietDongHH.pdf",
                                            "Nhiệt Động Hoá Học"));
        listItemLecture.add(new ItemLecture("Chap5_DongHoaHoc.pdf",
                                            "Động Hoá Học"));
        listItemLecture.add(new ItemLecture("Chap6_CanBangHH.pdf",
                                            "Cân Bằng Hoá Học"));
        listItemLecture.add(new ItemLecture("Chap7_DungDich.pdf",
                                            "Dung Dịch"));
        listItemLecture.add(new ItemLecture("Chap8_DungDichDienLi.pdf",
                                            "Dung Dịch Điện Li"));
        listItemLecture.add(new ItemLecture("Chap9_HoaKeo.pdf",
                                            "Hoá Keo"));
        listItemLecture.add(new ItemLecture("Chap10_ DienHoaHoc.pdf",
                                            "Diện Hoá Học"));
        listItemLecture.add(new ItemLecture("Chap11_PhucLuc.pdf",
                                            "Phục Lục"));
    }

    @Override
    public int getCount() {
        return listItemLecture.size();
    }

    @Override
    public ItemLecture getItem(int position) {
        return listItemLecture.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_lecture, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textLectureTitle = (TextView) convertView.findViewById(R.id.txt_lecture_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textLectureTitle.setText(listItemLecture.get(position).getTitle());
        return convertView;
    }

    private class ViewHolder{
        TextView textLectureTitle;
    }

    public static class ItemLecture{
        private String path;
        private String title;

        public ItemLecture(String path, String title) {
            this.path = path;
            this.title = title;
        }

        public String getPath() {
            return path;
        }

        public String getTitle() {
            return title;
        }
    }
}
