package com.sm.sls_app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 不依赖布局和焦点的跑马灯Textview Created by Mike on 2015/1/8.
 */
public class MarqueeTextView extends TextView {

    /**
     * 是否停止滚动
     */
    private boolean mStopMarquee;
    private String mText;
    private float mCoordinateX;
    private float mRecorderX = 0;
    private float mTextWidth;
    private int mWidth=0;
    private int mheight;
    private boolean onece = true;
    private Paint.FontMetrics fontMetrics;
    private int flag=0;

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     *  设置滚动内容
     * @param text 滚动内容
     * @param flag 0不滚动  ，1滚动
     */
    public void setRollText(String text, int flag) {

        if (text == null) {
            mStopMarquee = true;
            if (mHandler.hasMessages(0))
                mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, 500);
            return;
        }
        this.flag=flag;
        switch (flag) {

            case 0:
                this.mText = text;
                mStopMarquee = true;

                if (mHandler.hasMessages(0))
                    mHandler.removeMessages(0);
                invalidate();
                break;
            case 1:
                this.mText = text;
                mStopMarquee = false;
                if (mWidth!=0)
                mCoordinateX = mWidth;
                mTextWidth = getPaint().measureText(mText);
                if (mHandler.hasMessages(0))
                    mHandler.removeMessages(0);
                mHandler.sendEmptyMessageDelayed(0, 1000);
                break;

        }
        System.out.println("MarqueeTextView.setText==>mStopMarquee :"+String.valueOf(mStopMarquee));

    }

    public boolean ismStopMarquee() {
        return mStopMarquee;
    }

    public void setmStopMarquee(boolean mStopMarquee) {
        this.mStopMarquee = mStopMarquee;
    }

    /**
     * 重置
     */
    public void reset(){
        onece=true;
        mRecorderX=0;
        mStopMarquee=true;
    }
//    @Override
//    protected void onAttachedToWindow() {
//        if (!TextUtils.isEmpty(mText))
//            mHandler.sendEmptyMessageDelayed(0, 1000);
//        super.onAttachedToWindow();
//        System.out.println("MarqueeTextView.onAttachedToWindow==>mStopMarquee :"+String.valueOf(mStopMarquee));
//    }

    @Override
    protected void onDetachedFromWindow() {
        mStopMarquee = true;
        if (mHandler.hasMessages(0))
            mHandler.removeMessages(0);
        super.onDetachedFromWindow();
        System.out.println("MarqueeTextView.onDetachedFromWindow==>mStopMarquee :"+String.valueOf(mStopMarquee));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(mText)) {
            fontMetrics = getPaint().getFontMetrics();
            int baseY = (int) (getTop() + mheight / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
            canvas.drawText(mText, mCoordinateX, baseY, getPaint());
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (onece) {
            mheight = getHeight();
            mCoordinateX = mWidth = getWidth();
            if (flag==0)mCoordinateX=1;
            Log.i("MarqueeTextView", "mWidth==" + mWidth + "  //  mheight==" + mheight);
        }
        onece = false;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (Math.abs(mRecorderX) > (mTextWidth + mWidth)) {
                        mCoordinateX = mWidth;
                        mRecorderX = 0;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 1000);
                        }
                    } else {
                        mCoordinateX -= 1;
                        mRecorderX++;
                        invalidate();
                        if (!mStopMarquee) {
                            sendEmptyMessageDelayed(0, 30);
                        }
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };
}
