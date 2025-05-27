package com.nhom5.shelhive.ui.user.hoadon;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.util.Log;

import com.nhom5.shelhive.R;

public class VnpayWebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vnpay_webview);

        WebView webView = findViewById(R.id.webview_vnpay);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            // Android 6.0+
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                Log.d("VNPAY_WEBVIEW", "Đang duyệt URL: " + uri.toString());

                if ("shelhive".equals(uri.getScheme())) {
                    // Nếu là deep link, mở app qua Intent, xong đóng webview lại
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        Toast.makeText(VnpayWebViewActivity.this, "Trả kết quả thanh toán về app...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VnpayWebViewActivity.this, "App không nhận được deep link!", Toast.LENGTH_LONG).show();
                    }
                    finish();
                    return true;
                }
                return false; // Các link khác cứ load trong webview
            }
            // Android < 6.0
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("VNPAY_WEBVIEW", "Đang duyệt URL (old): " + url);
                Uri uri = Uri.parse(url);
                if ("shelhive".equals(uri.getScheme())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                        Toast.makeText(VnpayWebViewActivity.this, "Trả kết quả thanh toán về app...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(VnpayWebViewActivity.this, "App không nhận được deep link!", Toast.LENGTH_LONG).show();
                    }
                    finish();
                    return true;
                }
                return false;
            }
        });

        String url = getIntent().getStringExtra("url");
        Log.d("VNPAY_WEBVIEW", "Link nạp vào: " + url);
        if (url != null) {
            // Nếu BE trả về bị lỗi "http://https://" thì xử lý lại cho đúng https://
            if (url.startsWith("http://https://")) {
                url = url.replace("http://https://", "https://");
            }
            webView.loadUrl(url);
        } else {
            Toast.makeText(this, "Không có link thanh toán!", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
