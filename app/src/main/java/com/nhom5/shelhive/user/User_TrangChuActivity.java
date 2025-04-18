package com.nhom5.shelhive.user;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.nhom5.shelhive.R;
import com.nhom5.shelhive.customviews.CustomTypefaceSpan;

public class User_TrangChuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.user_trangchu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_trangchu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hello textview xử lý phần xin chào
        TextView helloText = findViewById(R.id.hello);
        String name = "Ngô Nhựt Trường";
        String fullText = "Xin chào, " + name + "!";

        SpannableString spannable = new SpannableString(fullText);

        int start = fullText.indexOf(name);
        int end = start + name.length();

        Typeface dancing = ResourcesCompat.getFont(this, R.font.dancing_script_bold);
        Typeface nunito = ResourcesCompat.getFont(this, R.font.nunito_semibold);

        // Set font và màu riêng cho <name>
        spannable.setSpan(new CustomTypefaceSpan(dancing), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(getColor(R.color.darkyellow)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set phần còn lại font Nunito + màu brown
        helloText.setText(spannable);
        helloText.setTypeface(nunito);
        helloText.setTextColor(getColor(R.color.brown));

        // Ánh xạ view thông tin phòng trọ
        LinearLayout infoContainer = findViewById(R.id.info_container);
        TextView emptyMessage = findViewById(R.id.empty_message);
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);
        TextView text3 = findViewById(R.id.text3);

        // Dữ liệu giả định
        String maphong = "PH123406";
        String tenTro = "Nhà Trọ Trường Phát";
        String diaChi = "123 Đường ABC, Quận XYZ, TP.HCM";

        if (maphong != null && tenTro != null && diaChi != null) {
            infoContainer.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);

            String soCuoiMaPhong = maphong.length() >= 2 ?
                    maphong.substring(maphong.length() - 2) : maphong;

            text1.setText("Phòng " + soCuoiMaPhong);
            text2.setText(tenTro);
            text3.setText(diaChi);
        } else {
            infoContainer.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        }
    }
}
