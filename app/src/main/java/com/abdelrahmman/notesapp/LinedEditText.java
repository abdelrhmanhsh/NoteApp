package com.abdelrahmman.notesapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;

public class LinedEditText extends AppCompatEditText {

    Rect mRect;
    Paint mPaint;

    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(0xFFFFD966);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int height = ((View)this.getParent()).getHeight();
        int lineHeight = getLineHeight();
        int numberOfLines = height / lineHeight;

        Rect r = mRect;
        Paint paint = mPaint;

        int baseLine = getLineBounds(0, r);

        for (int i = 0; i < numberOfLines; i++) {
            canvas.drawLine(r.left, baseLine + 1, r.right, baseLine + 1, paint);

            baseLine += lineHeight;
        }

        super.onDraw(canvas);
    }
}
