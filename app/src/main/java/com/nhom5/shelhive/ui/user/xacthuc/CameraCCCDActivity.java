package com.nhom5.shelhive.ui.user.xacthuc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.nhom5.shelhive.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraCCCDActivity extends AppCompatActivity {
    private PreviewView previewView;
    private ImageView btnCapture, backBtn, overlay;
    private View popupChup;
    private MaterialButton btnPopupNext;
    private TextView headerTitle;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private static final int CAMERA_PERMISSION_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Nhận side: "front" hoặc "back"
        String side = getIntent().getStringExtra("side");
        if ("back".equals(side)) {
            setContentView(R.layout.camera_back);
        } else {
            setContentView(R.layout.camera_front);
        }

        previewView = findViewById(R.id.previewView);
        btnCapture = findViewById(R.id.btnCapture);
        backBtn = findViewById(R.id.back);
        overlay = findViewById(R.id.overlay);
        headerTitle = findViewById(R.id.header_title);
        popupChup = findViewById(R.id.popup_chup);
        btnPopupNext = popupChup.findViewById(R.id.next);

        btnPopupNext.setOnClickListener(v -> popupChup.setVisibility(View.GONE));
        backBtn.setOnClickListener(v -> finish());
        cameraExecutor = Executors.newSingleThreadExecutor();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        btnCapture.setOnClickListener(v -> takePhoto());
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                imageCapture = new ImageCapture.Builder().build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.unbindAll();
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void takePhoto() {
        if (imageCapture == null) return;

        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Bitmap bitmap = imageProxyToBitmap(image);
                image.close();

                if (bitmap != null) {
                    // Crop ảnh theo overlay
                    Bitmap cropped = cropToOverlay(
                            bitmap,
                            overlay,
                            previewView
                    );

                    // Lưu cropped bitmap ra file
                    File file = saveBitmapToFile(cropped);
                    if (file != null) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("imagePath", file.getAbsolutePath());
                        setResult(RESULT_OK, resultIntent);
                    }
                    finish();
                }
            }
            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(CameraCCCDActivity.this, "Chụp ảnh thất bại: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        if (image.getFormat() == android.graphics.ImageFormat.JPEG) {
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
        return null;
    }

    // CROP dựa vào overlay
    private Bitmap cropToOverlay(Bitmap bitmap, View overlay, View previewView) {
        int[] overlayPos = new int[2];
        overlay.getLocationOnScreen(overlayPos);
        int[] previewPos = new int[2];
        previewView.getLocationOnScreen(previewPos);

        int relLeft = overlayPos[0] - previewPos[0];
        int relTop  = overlayPos[1] - previewPos[1];
        int relWidth = overlay.getWidth();
        int relHeight = overlay.getHeight();

        float scaleX = 1f * bitmap.getWidth() / previewView.getWidth();
        float scaleY = 1f * bitmap.getHeight() / previewView.getHeight();

        int cropLeft = (int)(relLeft * scaleX);
        int cropTop  = (int)(relTop * scaleY);
        int cropWidth  = (int)(relWidth * scaleX);
        int cropHeight = (int)(relHeight * scaleY);

        cropLeft = Math.max(0, cropLeft);
        cropTop = Math.max(0, cropTop);
        if (cropLeft + cropWidth > bitmap.getWidth()) cropWidth = bitmap.getWidth() - cropLeft;
        if (cropTop + cropHeight > bitmap.getHeight()) cropHeight = bitmap.getHeight() - cropTop;

        try {
            return Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropWidth, cropHeight);
        } catch (Exception e) {
            return bitmap; // nếu crop lỗi, trả ảnh gốc
        }
    }

    // Lưu bitmap ra file và trả về file (file path)
    private File saveBitmapToFile(Bitmap bitmap) {
        File file = new File(getCacheDir(), "cccd_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraExecutor != null) cameraExecutor.shutdown();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Cần cấp quyền camera để sử dụng", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
