package com.nhom5.shelhive.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.graphics.CornerPathEffect;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.nhom5.shelhive.R;

public class HexagonImageView extends AppCompatImageView {

    private Path hexagonPath;
    private Path hexagonBorderPath;
    private Paint borderPaint;

    private float borderWidth = 10f;
    private float cornerRadius;

    public HexagonImageView(Context context) {
        super(context);
        init(context);
    }

    public HexagonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HexagonImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        hexagonPath = new Path();
        hexagonBorderPath = new Path();

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);

        // Lấy màu nâu từ colors.xml
        borderPaint.setColor(ContextCompat.getColor(context, R.color.avt_border));

        // Bo góc radius 10dp
        cornerRadius = 6f * context.getResources().getDisplayMetrics().density;
        borderPaint.setPathEffect(new CornerPathEffect(cornerRadius));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float radius = w / 2f;
        float triangleHeight = (float) (Math.sqrt(3) * radius / 2);
        float centerX = w / 2f;
        float centerY = h / 2f;

        hexagonPath.reset();
        hexagonPath.moveTo(centerX, centerY - radius);
        hexagonPath.lineTo(centerX + triangleHeight, centerY - radius / 2);
        hexagonPath.lineTo(centerX + triangleHeight, centerY + radius / 2);
        hexagonPath.lineTo(centerX, centerY + radius);
        hexagonPath.lineTo(centerX - triangleHeight, centerY + radius / 2);
        hexagonPath.lineTo(centerX - triangleHeight, centerY - radius / 2);
        hexagonPath.close();

        hexagonBorderPath.set(hexagonPath);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(hexagonPath);
        super.onDraw(canvas);
        canvas.restore();

        // Vẽ viền sau khi đã bo góc
        canvas.drawPath(hexagonBorderPath, borderPaint);
    }
}
