package com.nhom5.shelhive.ui.user.xacthuc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.nhom5.shelhive.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User_XacThucActivity extends AppCompatActivity {

    private ImageView imgFront, imgBehind;
    private MaterialButton btnRecapture, btnNext;
    private ImageView btnBack;
    private Bitmap bitmapFront, bitmapBehind;
    private boolean hasFront = false, hasBehind = false;
    private String frontPath, behindPath;

    private HashMap<String, String> cccdInfo = new HashMap<>();
    private static final String TAG = "CCCD_OCR";
    private String email; // Nhận từ intent

    // Camera result launcher cho mặt trước/sau
    private final ActivityResultLauncher<Intent> cameraFrontLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> handleCameraResult(result.getResultCode(), result.getData(), true)
    );
    private final ActivityResultLauncher<Intent> cameraBehindLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> handleCameraResult(result.getResultCode(), result.getData(), false)
    );

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_idcard);

        // *** Lấy email từ intent ***
        email = getIntent().getStringExtra("email");
        Log.d("User_XacThuc", "Email nhận từ đăng nhập: " + email);

        imgFront = findViewById(R.id.front);
        imgBehind = findViewById(R.id.behind);
        btnRecapture = findViewById(R.id.recapture);
        btnNext = findViewById(R.id.next);
        btnBack = findViewById(R.id.back);

        btnBack.setOnClickListener(v -> finish());

        imgFront.setOnClickListener(v -> openCamera(true));
        imgBehind.setOnClickListener(v -> openCamera(false));

        btnRecapture.setEnabled(false);
        btnNext.setEnabled(false);

        btnRecapture.setOnClickListener(v -> {
            bitmapFront = null;
            bitmapBehind = null;
            cccdInfo.clear();
            hasFront = false;
            hasBehind = false;
            imgFront.setImageResource(R.drawable.front_idc);
            imgBehind.setImageResource(R.drawable.behind_idc);
            updateButtonState();
        });

        btnNext.setOnClickListener(v -> {
            if (bitmapFront != null && bitmapBehind != null && !cccdInfo.isEmpty()) {
                Intent intent = new Intent(User_XacThucActivity.this, User_KiemTraThongTinActivity.class);
                intent.putExtra("info", cccdInfo);
                intent.putExtra("email", email); // *** TRUYỀN EMAIL ***
                startActivity(intent);
            } else {
                Toast.makeText(this, "Hãy chụp đầy đủ CCCD và nhận diện thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        updateButtonState();
    }

    private void openCamera(boolean isFront) {
        Intent intent = new Intent(this, CameraCCCDActivity.class);
        intent.putExtra("side", isFront ? "front" : "back");
        if (isFront) {
            cameraFrontLauncher.launch(intent);
        } else {
            cameraBehindLauncher.launch(intent);
        }
    }

    private void handleCameraResult(int resultCode, Intent data, boolean isFront) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String imagePath = data.getStringExtra("imagePath");
            if (imagePath != null) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                if (isFront) {
                    imgFront.setImageBitmap(bitmap);
                    bitmapFront = bitmap;
                    hasFront = true;
                    frontPath = imagePath;
                    // **Auto OCR mặt trước**
                    runCCCDOcr(bitmapFront);
                } else {
                    imgBehind.setImageBitmap(bitmap);
                    bitmapBehind = bitmap;
                    hasBehind = true;
                    behindPath = imagePath;
                }
                updateButtonState();
            }
        }
    }

    private void runCCCDOcr(Bitmap bitmap) {
        if (bitmap == null) return;
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        com.google.mlkit.vision.text.TextRecognizer recognizer =
                TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(result -> {
                    String rawText = result.getText();
                    Log.d(TAG, "FULL OCR: \n" + rawText);
                    cccdInfo = extractCCCDInfo(rawText);
                    for (String key : cccdInfo.keySet()) {
                        Log.d(TAG, key + ": " + cccdInfo.get(key));
                    }
                    updateButtonState(); // Đảm bảo nút Next được cập nhật khi OCR xong
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Không thể nhận diện CCCD!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "OCR FAIL", e);
                });
    }

    private void updateButtonState() {
        boolean enable = hasFront && hasBehind && !cccdInfo.isEmpty();
        btnNext.setEnabled(enable);
        btnRecapture.setEnabled(hasFront || hasBehind);

        btnNext.setBackgroundTintList(
                getResources().getColorStateList(enable ? R.color.yellow : R.color.gray2, null));
        btnRecapture.setTextColor(getResources().getColor((hasFront || hasBehind) ? R.color.yellow : R.color.gray2, null));
        btnRecapture.setStrokeColorResource((hasFront || hasBehind) ? R.color.yellow : R.color.gray2);
    }

    // --- Phần nhận diện CCCD nâng cấp ---
    private HashMap<String, String> extractCCCDInfo(String ocrText) {
        HashMap<String, String> info = new HashMap<>();
        String[] lines = ocrText.split("\\n");
        // 1. Số CCCD: lấy chuỗi số dài nhất 9-12 số xuất hiện trên toàn văn bản (ưu tiên dòng chứa từ "số" hoặc "no")
        String cccd = null;
        for (String line : lines) {
            String l = line.toLowerCase();
            if (l.contains("số") || l.contains("so") || l.contains("só") || l.contains("no")) {
                Matcher m = Pattern.compile("(\\d{9,12})").matcher(line);
                if (m.find()) {
                    cccd = m.group(1);
                    break;
                }
            }
        }
        if (cccd == null) {
            Matcher m = Pattern.compile("(\\d{9,12})").matcher(ocrText);
            if (m.find()) cccd = m.group(1);
        }
        info.put("cccd", cccd);

        // 2. Họ tên: như cũ (tìm dòng sau "họ và tên" hoặc "full name", ghép dòng nếu IN HOA)
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].toLowerCase();
            if (line.contains("họ và tên") || line.contains("full name")) {
                String ten = "";
                if (i + 1 < lines.length) ten = lines[i + 1].trim();
                if (i + 2 < lines.length && lines[i + 2].trim().matches("[A-ZÀ-Ỹ ]{3,}"))
                    ten += " " + lines[i + 2].trim();
                ten = ten.replaceAll("^[^A-ZÀ-Ỹ]*", "");
                info.put("name", ten.trim());
                break;
            }
        }

        // 3. Ngày sinh: lấy date đầu tiên đúng định dạng
        Matcher dobMatcher = Pattern.compile("([0-9]{2}/[0-9]{2}/[0-9]{4})").matcher(ocrText);
        if (dobMatcher.find()) info.put("birthday", dobMatcher.group(1));

        // 4. Giới tính
        Pattern sexPattern = Pattern.compile("Giới tính[^:]*[:\\s]*([A-Za-zÀ-ỹ]+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Matcher sexMatcher = sexPattern.matcher(ocrText);
        if (sexMatcher.find()) {
            info.put("sex", sexMatcher.group(1).replaceAll("Quốc.*", "").trim());
        } else {
            Pattern sexPatternEn = Pattern.compile("Sex[^:]*[:\\s]*([A-Za-zÀ-ỹ]+)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
            Matcher sexMatcherEn = sexPatternEn.matcher(ocrText);
            if (sexMatcherEn.find()) {
                info.put("sex", sexMatcherEn.group(1).trim());
            }
        }

        // 5. Quê quán
        Pattern quequanPattern = Pattern.compile("(Quê quán|Place of origin)[^:]*[:\\s]*([^\n]+)", Pattern.CASE_INSENSITIVE);
        Matcher quequanMatcher = quequanPattern.matcher(ocrText);
        if (quequanMatcher.find()) {
            info.put("quequan", quequanMatcher.group(2).trim());
        }

        // 6. Nơi thường trú
        Pattern addressPattern = Pattern.compile("(Nơi thường trú|Place of residence)[^:]*[:\\s]*([^\n]+)\\n*([^\n]*)", Pattern.CASE_INSENSITIVE);
        Matcher addressMatcher = addressPattern.matcher(ocrText);
        if (addressMatcher.find()) {
            String diaChi = addressMatcher.group(2).trim();
            if (addressMatcher.groupCount() >= 3) {
                String nextLine = addressMatcher.group(3).trim();
                if (!nextLine.isEmpty() && !nextLine.toLowerCase().matches("^[a-z /:]{3,}.*"))
                    diaChi += " " + nextLine;
            }
            info.put("address", diaChi.trim());
        }
        return info;
    }
}
