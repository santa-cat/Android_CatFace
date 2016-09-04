package com.example.santa.catface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santa on 16/7/23.
 */
public class Beard extends View{
    private final static String DEBUG = "Beard";
    private Paint mPaint = new Paint();
    private List<Path> mPathList;
    private Path mPath = new Path();
    private int mColor = Color.BLACK;
    private int mWidth = 2;
    private final static int MSG_UPDATE_STATE =1 ;
    private float mDensity;

    //animotor
    private int mState = 0;
    private int[] mStateProcess = {0, 1, 2, 1, 0, 3, 4, 3, 0, 1, 0, 3, 0};
    private int mDuration = 10;

    //touch
    private Point mLastPoint = new Point();
    private int mActivePointerId;
    private double mAngleCur;
    private double mBoundCur;
    private final static float ANGLE_MIN = 0.01f;

    //
    private final static int STATE_IDEL = 0;
    private final static int STATE_DRAG = 1;
    private final static int STATE_RELEASE = 2;
    private int mTouchState = STATE_IDEL;
    private Listener mListener;

    public Beard(Context context) {
        this(context, null);
    }

    public Beard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Beard(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Beard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        mDensity = context.getResources().getDisplayMetrics().density;

        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mDensity*mWidth);


//        setOnClickListener(this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mPath.reset();
        mPath.moveTo(0, getMeasuredHeight()/2);
        mPath.lineTo(getMeasuredWidth(), getMeasuredHeight()/2);
        calculatePaths(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    private void calculatePaths(int width, int height) {
        int halfWidth = width/2;
        int halfHeight = height/2;



        mPathList = new ArrayList<>();
        {
            Path path = new Path();
            path.reset();
            path.moveTo(0, halfHeight);
            path.lineTo(width, halfHeight);
            mPathList.add(path);
        }
        {
            Path path = new Path();
            path.reset();
            path.moveTo(0, halfHeight);
            path.quadTo(halfWidth, halfHeight, width*15/16, height*3/4);
            mPathList.add(path);
        }
        {
            Path path = new Path();
            path.reset();
            path.moveTo(0, halfHeight);
            path.quadTo(halfWidth, halfHeight, width*7/8, height);
            mPathList.add(path);
        }
        {
            Path path = new Path();
            path.reset();
            path.moveTo(0, halfHeight);
            path.quadTo(halfWidth, halfHeight, width*15/16, height/4);
            mPathList.add(path);
        }
        {
            Path path = new Path();
            path.reset();
            path.moveTo(0, halfHeight);
            path.quadTo(halfWidth, halfHeight, width*7/8, 0);
            mPathList.add(path);
        }
    }

    private int getPxFromDp(int dp) {
        return (int) (mDensity*dp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean isConsume = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastPoint.set((int) event.getX(), (int) event.getY());
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                isConsume = isTouchOnBeard((int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e("refresh", "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }
                moveBy(new Point((int) event.getX(), (int) event.getY()));
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                release(new Point((int) event.getX(), (int) event.getY()));
                break;
        }
        return super.onTouchEvent(event) || isConsume;
    }

    private void release(Point newPoint) {
        new Thread(new Shakable()).start();
        if (null != mListener) {
            double angleNew = Math.atan((newPoint.y-getHeight()/2)*1.0f/newPoint.x);
            float percent = (float) (Math.abs(angleNew)/Math.abs(3.0f/4));
            percent = (percent > 1.0f) ? 1.0f : percent;
            mListener.onFinish(this, percent);
        }
    }

    private void moveBy(Point newPoint) {
        double angleNew = Math.atan((newPoint.y-getHeight()/2)*1.0f/newPoint.x);
        mAngleCur = angleNew;
        mBoundCur = -(angleNew - (0.1*angleNew/Math.abs(angleNew)));
//        Log.d(DEBUG, "angleNew = "+angleNew);
        updatePath(angleNew);
        invalidate();
        if (null != mListener) {
            float percent = (float) (Math.abs(angleNew)/Math.abs(3.0f/4));
            percent = (percent > 1.0f) ? 1.0f : percent;
            mListener.onPull(this, percent);
        }
    }

    private void updatePath(double angleNew) {
        double angleAll = Math.atan(3.0f/4);
        if(Math.abs(angleNew) > Math.abs(angleAll)) {
            return;
        }
        double heightNew = (angleNew*getHeight()/2)/angleAll;
        double widthNew = getWidth() - Math.abs((angleNew*(getWidth()/3))/angleAll);

//        Log.d(DEBUG, "heightNew = "+heightNew);
//        Log.d(DEBUG, "widthNew = "+widthNew);
        mPath.reset();
        mPath.moveTo(0, getHeight()/2);
        mPath.quadTo(getWidth()/2, getHeight()/2, (int)widthNew, getHeight()/2 + (int)heightNew);
    }


    public boolean isTouchOnBeard(int touchY) {
//        Log.d(DEBUG, "touchY = "+touchY);
        int beardY = getHeight()/2;
        int offset = getPxFromDp(10);
        if (touchY >= beardY - offset && touchY <= beardY + offset) {
            mTouchState = STATE_DRAG;
            if(null != mListener) {
                mListener.onStart();
            }
            return true;
        } else {
            return false;
        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, mPaint);
        //draw the beard
        canvas.drawPath(mPath, mPaint);

//        if (mTouchState == STATE_RELEASE || mTouchState == STATE_IDEL) {
//            canvas.drawPath(mPathList.get(mStateProcess[mState]), mPaint);
//        } else if(mTouchState == STATE_DRAG) {
//            canvas.drawPath(mPath, mPaint);
//        }
//        canvas.drawPath(mPathList.get(1), mPaint);
//        canvas.drawPath(mPathList.get(2), mPaint);
//        canvas.drawPath(mPathList.get(3), mPaint);
//        canvas.drawPath(mPathList.get(4), mPaint);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_STATE:
                    invalidate();
                    break;
            }
            super.handleMessage(msg);
        }
    };




    private boolean calculateAngle() {
        if (Math.abs(mBoundCur) < ANGLE_MIN) {
            mAngleCur = 0;
            mBoundCur = 0;
            return false;
        }else {
            //角度小活着在两边都要继续跳动
            double angleChange;
            angleChange = Math.abs(mBoundCur)/5;
            if(Math.abs(mAngleCur) < Math.abs(mBoundCur) || (mAngleCur/mBoundCur)<0 ) {
                mAngleCur = mAngleCur + (angleChange * (mBoundCur) / Math.abs(mBoundCur));
            } else {
                mBoundCur = - (mBoundCur - (angleChange * (mBoundCur) / Math.abs(mBoundCur)));
                mAngleCur = mAngleCur + (angleChange * (mBoundCur) / Math.abs(mBoundCur));
            }
        }
        return true;
    }

//
//    private int getDuration() {
//        if (Math.abs(mAngleCur) <= 0.2f) {
//            return mDuration;
//        }
//        return (int) (mDuration*0.1f/Math.abs(mAngleCur));
//    }

    private class Shakable implements Runnable{
        private boolean isFinish = false;

        @Override
        public void run() {
            while (!isFinish) {
                try {

                    Thread.sleep(mDuration);
                    synchronized (Beard.class) {
                        if (!calculateAngle()) {
                            setIsFinish(true);
                        }
                        updatePath(mAngleCur);
                        Message message = new Message();
                        message.what = MSG_UPDATE_STATE;
                        mHandler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void setIsFinish(Boolean isFinish) {
            this.isFinish = isFinish;
        }
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener{
        void onPull(Beard beard, float percent);
        void onFinish(Beard beard, float percent);
        void onStart();
    }

}
