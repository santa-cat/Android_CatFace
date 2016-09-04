package com.example.santa.catface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by santa on 16/7/24.
 */
public class Mouth extends View {
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private int mColor = 0xffaa5c53;
    private int mWidth = 5;
    private float mDensity;

    public final static int STATE_SMILE = 0;
    public final static int STATE_ANFRY = 1;
    private int mState = STATE_SMILE;

    public Mouth(Context context) {
        this(context, null);
    }

    public Mouth(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Mouth(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Mouth(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;

        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDensity*mWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updatePath(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    private void updatePath(int width, int height) {
        if (mState == STATE_SMILE) {
            mPath.reset();
            mPath.moveTo(0, 0);
            mPath.quadTo(width/2, height, width, 0);
        } else {
            mPath.reset();
            mPath.moveTo(0, height);
            mPath.quadTo(width/2, 0, width, height);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public void setState(int state) {
        mState = state;
        updatePath(getWidth(), getHeight());
        invalidate();
    }
}
