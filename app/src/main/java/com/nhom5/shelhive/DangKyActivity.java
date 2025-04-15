package com.nhom5.shelhive;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DangKyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dangky);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dangky), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Xử lý sự kiện nhấn vào TextView "signup"
        TextView signupTextView = findViewById(R.id.signin);
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangKyActivity.this, DangNhapActivity.class);
                startActivity(intent);
            }
        });

        setupTermsTextView(); // gọi hàm xử lý điều khoản
    }

    private void setupTermsTextView() {
        TextView termsTextView = findViewById(R.id.dieukhoan_text);
        String fullText = "Tôi đã đọc và đồng ý với Điều khoản sử dụng & chính sách bảo mật.";
        SpannableString spannableString = new SpannableString(fullText);

        int start = fullText.indexOf("Điều khoản sử dụng & chính sách bảo mật");
        int end = start + "Điều khoản sử dụng & chính sách bảo mật".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                new AlertDialog.Builder(DangKyActivity.this)
                        .setTitle("Điều khoản & Chính sách")
                        .setMessage("Đây là nội dung Điều khoản sử dụng và Chính sách bảo mật của ứng dụng. Bạn vui lòng đọc kỹ trước khi tiếp tục.")
                        .setPositiveButton("Tôi đã hiểu", null)
                        .show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int yellowColor = ContextCompat.getColor(DangKyActivity.this, R.color.yellow);
                ds.setColor(yellowColor);
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsTextView.setText(spannableString);
        termsTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termsTextView.setHighlightColor(Color.TRANSPARENT);
    }
}
