package com.nhom5.shelhive.ui.admin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.ui.common.customviews.CustomTypefaceSpan;

public class Admin_TrangChuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.admin_trangchu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_trangchu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // TextView hello
        TextView helloText = findViewById(R.id.hello);
        String name = "Ngô Nhựt Trường";
        String fullText = "Xin chào, " + name + "!";

        SpannableString spannable = new SpannableString(fullText);
        int start = fullText.indexOf(name);
        int end = start + name.length();

        Typeface dancing = ResourcesCompat.getFont(this, R.font.dancing_script_bold);
        Typeface nunito = ResourcesCompat.getFont(this, R.font.nunito_semibold);

        spannable.setSpan(new CustomTypefaceSpan(dancing), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(getColor(R.color.darkyellow)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        helloText.setText(spannable);
        helloText.setTypeface(nunito);
        helloText.setTextColor(getColor(R.color.brown));

        // TextView thông tin phòng trọ
        LinearLayout infoContainer = findViewById(R.id.info_container);
        TextView emptyMessage = findViewById(R.id.empty_message);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);

        String tenTro = "Nhà Trọ Trường Phát";
        String diaChi = "123 Đường ABC, Quận XYZ, TP.HCM";

        if (tenTro != null && diaChi != null) {
            infoContainer.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);

            text2.setText(tenTro);
            text3.setText(diaChi);
        } else {
            infoContainer.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        }

        // Ánh xạ các FrameLayout và xử lý sự kiện chuyển Activity
        FrameLayout btnQuanLy = findViewById(R.id.quanly);
        FrameLayout btnHoaDon = findViewById(R.id.hoadon);
        FrameLayout btnThongBao = findViewById(R.id.thongbao);
        FrameLayout btnPhanAnh = findViewById(R.id.phananh);
        FrameLayout btnThongKe = findViewById(R.id.thongke);

        LinearLayout btnNavProfile = findViewById(R.id.nav_profile);

        btnQuanLy.setOnClickListener(v -> startActivity(new Intent(this, Admin_QuanLyActivity.class)));
        btnHoaDon.setOnClickListener(v -> startActivity(new Intent(this, Admin_HoaDonActivity.class)));
        btnThongBao.setOnClickListener(v -> startActivity(new Intent(this, Admin_ThongBaoActivity.class)));
        btnPhanAnh.setOnClickListener(v -> startActivity(new Intent(this, Admin_PhanAnhActivity.class)));
        btnThongKe.setOnClickListener(v -> startActivity(new Intent(this, Admin_ThongKeActivity.class)));

        btnNavProfile.setOnClickListener(v -> startActivity(new Intent(this, Admin_ThongTinActivity.class)));
    }
}
