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

        // 1. Số CCCD
        String cccd = null;
        Pattern cccdPattern = Pattern.compile("\\b\\d{9,12}\\b");
        for (String line : lines) {
            String l = line.toLowerCase();
            if (l.contains("số") || l.contains("no")) {
                Matcher m = cccdPattern.matcher(line);
                if (m.find()) {
                    cccd = m.group();
                    break;
                }
            }
        }
        if (cccd == null) {
            Matcher m = cccdPattern.matcher(ocrText);
            if (m.find()) cccd = m.group();
        }
        info.put("cccd", cccd);

        // 2. Họ tên
        String name = null;
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i].toLowerCase();
            if (l.contains("họ và tên") || l.contains("full name")) {
                String afterColon = getAfterColon(lines[i]);
                if (afterColon != null && isNameCandidate(afterColon))
                    name = afterColon;
                else if (i + 1 < lines.length && isNameCandidate(lines[i + 1]))
                    name = lines[i + 1].trim();
                break;
            }
        }
        // Lọc lỗi dính từ Việt Nam hoặc null
        if (name == null || name.equalsIgnoreCase("VIỆT NAM")) {
            for (String line : lines) {
                if (isNameCandidate(line)) {
                    name = line.trim();
                    break;
                }
            }
        }
        info.put("name", name);

        // 3. Ngày sinh
        String birthday = null;
        Pattern datePattern = Pattern.compile("(\\d{2}/\\d{2}/\\d{4})");
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i].toLowerCase();
            if (l.contains("ngày sinh") || l.contains("date of birth")) {
                Matcher m = datePattern.matcher(lines[i]);
                if (m.find()) {
                    birthday = m.group(1);
                    break;
                }
                // Dòng sau
                if (i + 1 < lines.length) {
                    Matcher m2 = datePattern.matcher(lines[i + 1]);
                    if (m2.find()) {
                        birthday = m2.group(1);
                        break;
                    }
                }
            }
        }
        if (birthday == null) {
            for (String line : lines) {
                Matcher m = datePattern.matcher(line);
                if (m.find()) {
                    String d = m.group(1);
                    try {
                        int year = Integer.parseInt(d.split("/")[2]);
                        if (year < 2020) {
                            birthday = d;
                            break;
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
        info.put("birthday", birthday);

        // 4. Giới tính
        String sex = null;
        Pattern sexPattern = Pattern.compile("(Nam|Nữ|Nử|Male|Female)", Pattern.CASE_INSENSITIVE);
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i].toLowerCase();
            if (l.contains("giới tính") || l.contains("sex")) {
                Matcher m = sexPattern.matcher(lines[i]);
                if (m.find()) {
                    sex = m.group(1);
                } else if (i + 1 < lines.length) {
                    Matcher m2 = sexPattern.matcher(lines[i + 1]);
                    if (m2.find()) {
                        sex = m2.group(1);
                    }
                }
                break;
            }
        }
        info.put("sex", sex);

        // 5. Quê quán (label chung dòng hoặc dòng sau, value không phải label, lấy luôn value ngắn nếu nhận diện không ra)
        String quequan = null;
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i].toLowerCase();
            if (l.contains("quê quán") || l.contains("place of origin")) {
                String afterColon = getAfterColon(lines[i]);
                if (afterColon != null && !isLabelLine(afterColon)) {
                    quequan = afterColon;
                } else if (i + 1 < lines.length && !isLabelLine(lines[i + 1])) {
                    quequan = lines[i + 1].trim();
                }
                break;
            }
        }
        // Bổ sung: Nếu vẫn null, tìm dòng có địa danh (dùng regex cho các tỉnh/thành)
        if (quequan == null || isLabelLine(quequan)) {
            for (String line : lines) {
                if (line.matches(".*(An Giang|Long An|Thanh Hóa|Nghệ An|Quảng Nam|Bình Dương|Gia Lai|Bến Cát|Hồ Chí Minh).*") && line.length() > 6 && !isLabelLine(line)) {
                    quequan = line.trim();
                    break;
                }
            }
        }
        info.put("quequan", quequan);

        // 6. Địa chỉ (gom tối đa 2 dòng sau label, chỉ nếu không phải label, không bị rác)
        String address = null;
        for (int i = 0; i < lines.length; i++) {
            String l = lines[i].toLowerCase();
            if (l.contains("nơi thường trú") || l.contains("place of residence")) {
                StringBuilder addressBuilder = new StringBuilder();
                String afterColon = getAfterColon(lines[i]);
                if (afterColon != null && !isLabelLine(afterColon)) {
                    addressBuilder.append(afterColon);
                }
                int lineCount = 0;
                for (int j = i + 1; j < lines.length && lineCount < 2; j++) {
                    if (!isLabelLine(lines[j]) && lines[j].length() > 5) {
                        if (addressBuilder.length() > 0) addressBuilder.append(", ");
                        addressBuilder.append(lines[j].trim());
                        lineCount++;
                    }
                }
                address = addressBuilder.length() > 0 ? addressBuilder.toString().replaceAll(",\\s*$", "") : null;
                break;
            }
        }
        // Fallback: lấy dòng cuối nếu không ra
        if ((address == null || isLabelLine(address)) && lines.length > 2) {
            address = lines[lines.length - 1].trim();
        }
        info.put("address", address);

        return info;
    }

    // Hàm phụ trợ phải nằm ngoài extractCCCDInfo
    private String getAfterColon(String line) {
        if (line == null) return null;
        int idx = line.indexOf(":");
        if (idx == -1) idx = line.indexOf("：");
        if (idx != -1 && idx + 1 < line.length()) {
            String result = line.substring(idx + 1).trim();
            return result.isEmpty() ? null : result;
        }
        return null;
    }
    private boolean isLabelLine(String line) {
        if (line == null) return false;
        String l = line.toLowerCase();
        return l.matches(".*(số|no|họ và tên|full name|ngày sinh|date of birth|giới tính|sex|quê quán|place of origin|nơi thường trú|place of residence|quốc tịch|nationality).*");
    }
    private boolean isNameCandidate(String line) {
        return line != null && line.matches("[A-ZÀ-Ỹ\\s]{8,}") && !line.equals("VIỆT NAM") && !isLabelLine(line);
    }
}
