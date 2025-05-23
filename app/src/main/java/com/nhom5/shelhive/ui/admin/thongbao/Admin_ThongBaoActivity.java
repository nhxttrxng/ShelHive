package com.nhom5.shelhive.ui.admin.thongbao;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.adapter.ThongBaoAdapter;
import com.nhom5.shelhive.ui.admin.Admin_TrangChuActivity;
import com.nhom5.shelhive.ui.admin.thongke.Admin_ThongKe_NhaTro;

import java.util.Arrays;
import java.util.List;

public class Admin_ThongBaoActivity extends AppCompatActivity {

    TextView tabThongBaoChung, tabThongBaoHoaDon;
    RecyclerView recyclerView;
    ThongBaoAdapter adapter;

    List<String> thongBaoChungList;
    List<String> thongBaoHoaDonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_thongbao);

        // Ánh xạ
        tabThongBaoChung = findViewById(R.id.tab_thong_bao_chung_admin);
        tabThongBaoHoaDon = findViewById(R.id.tab_thong_bao_hoa_don_admin);
        recyclerView = findViewById(R.id.recycler_view_thong_bao);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ImageView btnThem = findViewById(R.id.btn_bottom_image);
        ImageView btnBack=findViewById(R.id.btn_back);
        btnThem.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_ThongBaoActivity.this, Admin_TaoThongBao.class); // thay bằng Activity chính admin của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(Admin_ThongBaoActivity.this, Admin_ThongBao_NhaTro.class); // thay bằng Activity chính admin của bạn
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        // Dữ liệu mẫu
        thongBaoChungList = Arrays.asList(
                "Ngày mai, lúc 19h 06/03/2025, nhà trọ tổ chức 8/3, mời mọi người cùng tham gia",
                "Hôm nay 10/03/2025, đã đến hạn thanh toán tiền điện",
                "Ngày mai, 15/03/2025, nhà trọ có lịch cắt điện theo địa phương",
                "Còn 3 ngày nữa sẽ hết hạn hợp đồng vào ngày 29/03/2025"
        );

        thongBaoHoaDonList = Arrays.asList(
                "Phòng 1 đã đóng tiền trọ Tháng 1/2025",
                "Phòng 1 vừa xin gia hạn tiền trọ Tháng 2/2025"
        );

        // Adapter ban đầu là tab chung
        adapter = new ThongBaoAdapter(thongBaoChungList, this, this::xuLyClickThongBao);
        recyclerView.setAdapter(adapter);

        // Gán sự kiện click cho tab
        tabThongBaoChung.setOnClickListener(v -> chuyenTab(true));
        tabThongBaoHoaDon.setOnClickListener(v -> chuyenTab(false));
    }

    private void chuyenTab(boolean laTabChung) {
        if (laTabChung) {
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung);
            tabThongBaoChung.setTextColor(Color.WHITE);
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don);
            tabThongBaoHoaDon.setTextColor(Color.parseColor("#755200"));
            adapter.capNhatDuLieu(thongBaoChungList);
        } else {
            tabThongBaoHoaDon.setBackgroundResource(R.drawable.bg_tab_hoa_don_1);
            tabThongBaoHoaDon.setTextColor(Color.WHITE);
            tabThongBaoChung.setBackgroundResource(R.drawable.bg_tab_chung_1);
            tabThongBaoChung.setTextColor(Color.parseColor("#755200"));
            adapter.capNhatDuLieu(thongBaoHoaDonList);
        }
    }

    private void xuLyClickThongBao(String noiDung) {
        Intent intent = new Intent(this, Admin_XoaThongBaoActivity.class);
        intent.putExtra("noiDungThongBao", noiDung);
        startActivity(intent);
    }
}
