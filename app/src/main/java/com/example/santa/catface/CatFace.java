package com.example.santa.catface;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by santa on 16/7/24.
 */
public class CatFace extends ViewGroup implements Beard.Listener{
    private ImageView mFace;
    private ImageView mAngry;
    private Beard mBeardL1;
    private Beard mBeardL2;
    private Beard mBeardR1;
    private Beard mBeardR2;
    private Context mContext;
    private Drawable mFaceDrawable;
    private float mDensity;
    private int OffsetBeard;
    private Eye mEyeL;
    private Eye mEyeR;
    private Mouth mMouth;
    private int mEyeLPosition;
    private int mEyeRPosition;

    public CatFace(Context context) {
        this(context, null);
    }

    public CatFace(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CatFace(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CatFace(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;
        mContext = context;

        //face
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mFace = new ImageView(context);
        mFace.setLayoutParams(layoutParams);
        mFaceDrawable = getResources().getDrawable(R.mipmap.cat);
//        mFace.setImageDrawable(mFaceDrawable);
        mFace.setScaleType(ImageView.ScaleType.FIT_XY);

        //beard
        mBeardL1 = new Beard(context);
        mBeardL1.setListener(this);
        addView(mBeardL1);
        mBeardL2 = new Beard(context);
        mBeardL2.setListener(this);
        addView(mBeardL2);
        mBeardR1 = new Beard(context);
        mBeardR1.setListener(this);
        addView(mBeardR1);
        mBeardR2 = new Beard(context);
        mBeardR2.setListener(this);
        addView(mBeardR2);

        //eyes
        mEyeL = new Eye(context);
        addView(mEyeL);
        mEyeR = new Eye(context);
        addView(mEyeR);
        addView(mFace);

        mMouth = new Mouth(context);
        addView(mMouth);

        mAngry = new ImageView(context);
        mAngry.setLayoutParams(layoutParams);
        mAngry.setImageDrawable(getResources().getDrawable(R.mipmap.angry));
        addView(mAngry);
        mAngry.setVisibility(INVISIBLE);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(null != mFace) {
            mFace.setImageBitmap(resizeImage(getBitmap(), MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec)));
            measureChildWithMargins(mFace, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if (null != mAngry) {
            measureChildWithMargins(mAngry, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mBeardL1) {
            measureChildWithMargins(mBeardL1, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mBeardL2) {
            measureChildWithMargins(mBeardL2, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mBeardR1) {
            measureChildWithMargins(mBeardR1, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mBeardR2) {
            measureChildWithMargins(mBeardR2, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mEyeL) {
            measureChildWithMargins(mEyeL, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mEyeR) {
            measureChildWithMargins(mEyeR, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if(null != mMouth) {
            measureChildWithMargins(mMouth, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop  = getPaddingTop();
//        Log.d("DEBUG", "getMeasuredWidth = "+mFace.getMeasuredWidth());
        if (null != mFace && mFace.getRight() == 0) {
            LayoutParams lp = (LayoutParams) mFace.getLayoutParams();
            int left = (getMeasuredWidth())/2 - mFace.getMeasuredWidth()/2 - lp.leftMargin;
            int top = paddingTop + lp.topMargin;
            int right = left + mFace.getMeasuredWidth();
            int bottom = top + mFace.getMeasuredHeight();
            mFace.layout(left, top, right, bottom);
            initAll();
        }
        if (null != mBeardL1) {
            int right = mFace.getLeft() + getOffsetBeard();
            int top = (mFace.getBottom() + mFace.getTop())*2/3 - mBeardL1.getMeasuredHeight()/2;
            int left = right - mBeardL1.getMeasuredWidth();
            int bottom = top + mBeardL1.getMeasuredHeight();
            mBeardL1.layout(left, top, right, bottom);
            ValueAnimator animator = ObjectAnimator.ofFloat(mBeardL1, "rotation", 0, 182);
            animator.setDuration(0);
            animator.start();
        }
        if (null != mBeardL2) {
            int right = mFace.getLeft() + getOffsetBeard();
            int top = (mFace.getBottom() + mFace.getTop())*2/3 - mBeardL1.getMeasuredHeight()/2 + getOffsetBeard();
            int left = right - mBeardL2.getMeasuredWidth();
            int bottom = top + mBeardL2.getMeasuredHeight();
            mBeardL2.layout(left, top, right, bottom);
            ValueAnimator animator = ObjectAnimator.ofFloat(mBeardL2, "rotation", 0, 178);
            animator.setDuration(0);
            animator.start();
        }
        if (null != mBeardR1) {
            int left = mFace.getRight() - getOffsetBeard();
            int top = (mFace.getBottom() + mFace.getTop())*2/3 - mBeardL1.getMeasuredHeight()/2;
            int right = left + mBeardR1.getMeasuredWidth();
            int bottom = top + mBeardR1.getMeasuredHeight();
            mBeardR1.layout(left, top, right, bottom);
            ValueAnimator animator = ObjectAnimator.ofFloat(mBeardR1, "rotation", 0, -2);
            animator.setDuration(0);
            animator.start();
        }
        if (null != mBeardR2) {
            int left = mFace.getRight() - getOffsetBeard();
            int top = (mFace.getBottom() + mFace.getTop())*2/3 - mBeardL1.getMeasuredHeight()/2 + getOffsetBeard();
            int right = left + mBeardR2.getMeasuredWidth();
            int bottom = top + mBeardR2.getMeasuredHeight();
            mBeardR2.layout(left, top, right, bottom);
            ValueAnimator animator = ObjectAnimator.ofFloat(mBeardR2, "rotation", 0, 2);
            animator.setDuration(0);
            animator.start();
        }

        if (null != mEyeL) {
            int left = mFace.getLeft() + (int)((1.0f/3 - 1.0f/36)*(mFace.getRight() - mFace.getLeft())) - mEyeL.getMeasuredWidth()/2;
            int top = mFace.getTop() + (int)( (mFace.getBottom() - mFace.getTop())*(2.0f/3 + 1.0f/100))- mEyeL.getMeasuredHeight()/2;
            int right = left + mEyeL.getMeasuredWidth();
            int bottom = top + mEyeL.getMeasuredHeight();
            mEyeL.layout(left, top, right, bottom);
            mEyeLPosition = left;
        }
        if (null != mEyeR) {
            int right = mFace.getRight() - (int)((1.0f/3 - 1.2f/24)*(mFace.getRight() - mFace.getLeft())) + mEyeR.getMeasuredWidth()/2;
            int top = mFace.getTop() + (int)( (mFace.getBottom() - mFace.getTop())*(2.0f/3 + 1.0f/100))- mEyeR.getMeasuredHeight()/2;
            int left = right - mEyeR.getMeasuredWidth();
            int bottom = top + mEyeR.getMeasuredHeight();
            mEyeR.layout(left, top, right, bottom);
            mEyeRPosition = left;
        }
        if(null != mMouth) {
            int left = (int)((mFace.getLeft() + mFace.getRight())*(1.0f/2+ 0.8f/100)) - mMouth.getMeasuredWidth()/2;
            int top = mFace.getHeight()*7/8;
            int right = left + mMouth.getMeasuredWidth();
            int bottom = top + mMouth.getMeasuredHeight();
            mMouth.layout(left, top, right, bottom);
        }
        if (null != mAngry) {
            int left = (mFace.getLeft() + mFace.getRight())/2 + mFace.getWidth()/5 ;
            int top = (int)(mFace.getHeight()*0.9f/3);
            int right = left + mAngry.getMeasuredWidth();
            int bottom = top + mAngry.getMeasuredHeight();
            mAngry.layout(left, top, right, bottom);
        }
    }

    private void initAll() {
        initBeard();
        initEyes();
        initMouth();
        initAngry();
    }

    private void initBeard() {
        int faceLeft = mFace.getLeft();
        int faceRight = mFace.getRight();


        //beard
        setOffsetBeard((int) (mDensity*20));
        int width = faceLeft + getOffsetBeard();
        int height = width;
        LayoutParams layoutParams = new LayoutParams(width, height);
        mBeardL1.setLayoutParams(layoutParams);
        mBeardL2.setLayoutParams(layoutParams);
        mBeardR1.setLayoutParams(layoutParams);
        mBeardR2.setLayoutParams(layoutParams);
        requestLayout();
    }

    private void initEyes() {
        LayoutParams layoutParams = new LayoutParams(mFace.getHeight()*5/12, mFace.getHeight()*5/12);
        mEyeL.setLayoutParams(layoutParams);
        mEyeR.setLayoutParams(layoutParams);
    }

    private void initMouth() {
        LayoutParams layoutParams = new LayoutParams(mFace.getHeight()/10, mFace.getHeight()/20);
        mMouth.setLayoutParams(layoutParams);

    }

    private void initAngry() {
        LayoutParams layoutParams = new LayoutParams(mFace.getHeight()/4, mFace.getHeight()/4);
        mAngry.setLayoutParams(layoutParams);
    }

    private void setOffsetBeard(int offset) {
        OffsetBeard = offset;
    }

    private int getOffsetBeard() {
        return OffsetBeard;
    }

    private Bitmap getBitmap() {
        return ((BitmapDrawable)mFaceDrawable).getBitmap();
    }

    //使用Bitmap加Matrix来缩放
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h)
    {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        float scale = Math.min(scaleHeight, scaleWidth);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        return  Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
    }

    private void offsetEyes(Beard beard, float percent) {
        int offset = (int)(percent*mEyeL.getWidth()/4);
        if(beard == mBeardL1 || beard == mBeardL2) {
            offset = -offset;
        }
//        Log.d("catFace", "mEyeL = "+mEyeL.getLeft());
        if(null != mEyeL) {
            mEyeL.offsetLeftAndRight(mEyeLPosition - mEyeL.getLeft());
            mEyeL.offsetLeftAndRight(offset);
        }
        if(null != mEyeR) {
            mEyeR.offsetLeftAndRight(mEyeRPosition - mEyeR.getLeft());
            mEyeR.offsetLeftAndRight(offset);
        }
    }

    private void setMouthState(float percent) {
        if (null != mMouth) {
            if (percent > 0.5f) {
                mMouth.setState(Mouth.STATE_ANFRY);
                mAngry.setVisibility(VISIBLE);
            } else {
                mMouth.setState(Mouth.STATE_SMILE);
                mAngry.setVisibility(INVISIBLE);
            }
        }
    }

    @Override
    public void onPull(Beard beard, float percent) {
        offsetEyes(beard, percent);
        setMouthState(percent);
    }

    @Override
    public void onFinish(final Beard beard, float percent) {
        ValueAnimator animator = ObjectAnimator.ofFloat(percent,  0);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float percent = (float)animation.getAnimatedValue();
                offsetEyes(beard ,percent);
                if (percent < 0.1f) {
                    setMouthState(0);
                }
            }
        });
        animator.start();

    }

    @Override
    public void onStart() {
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }





}
