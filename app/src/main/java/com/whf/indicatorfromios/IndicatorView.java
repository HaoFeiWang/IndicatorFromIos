package com.whf.indicatorfromios;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * 自定义ViewPager的指示器（仿IOS）
 * Created by WHF on 2017/3/18.
 */

public class IndicatorView extends View {

    private String[] mTitleArray = new String[]{"消息", "发现", "朋友圈","新闻","娱乐","美食"};
    private Path[] mPathArray;
    private Paint mPaint;

    //高度
    private int mHeight = dp2px(30);
    //单个选项的宽度
    private int mWidthOfItem = dp2px(80);
    //字体大小
    private int mTextSize = sp2px(15);
    //线的宽度
    private int mLineWidth = dp2px(1);
    //圆角半径大小
    private int mRadius = dp2px(5);

    //当前Indicator
    private int mCurIndex = 0;

    //未被选定时的背景色
    private int mUnSelectBg = Color.WHITE;
    //被选择定时的背景色
    private int mSelectBg = Color.GRAY;
    //未被选定时的字体颜色
    private int mUnSelectTextColor = Color.GRAY;
    //被选定时的字体颜色
    private int mSelectTextColor = Color.WHITE;
    //线的颜色
    private int mLineColor = Color.GRAY;


    private boolean isFirstDraw = true;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        //使文字居中对齐
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
    }

    /**
     * 初始化path数组
     */
    private void initPathArray() {

        mPathArray = new Path[mTitleArray.length];

        for (int i = 0; i < mTitleArray.length; i++) {
            Path path = new Path();
            //第一个
            if (i == 0) {
                path.moveTo(0, mRadius);
                path.quadTo(0, 0, mRadius, 0);
                path.lineTo(mWidthOfItem, 0);
                path.lineTo(mWidthOfItem, mHeight);
                path.lineTo(mRadius, mHeight);
                path.quadTo(0, mHeight, 0, mHeight - mRadius);
                path.close();
                //最后一个
            } else if (i == mTitleArray.length - 1) {
                int width = getWidth();
                path.moveTo(width - mWidthOfItem, 0);
                path.lineTo(width - mRadius, 0);
                path.quadTo(width, 0, width, mRadius);
                path.lineTo(width, mHeight - mRadius);
                path.quadTo(width, mHeight, width - mRadius, mHeight);
                path.lineTo(width - mWidthOfItem , mHeight);
                path.close();
                //中间的
            } else {
                path.moveTo(mWidthOfItem * i, 0);
                path.lineTo(mWidthOfItem * (i + 1), 0);
                path.lineTo(mWidthOfItem * (i + 1), mHeight);
                path.lineTo(mWidthOfItem * i, mHeight);
                path.close();
            }
            mPathArray[i] = path;

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMeasureMode == MeasureSpec.AT_MOST && heightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mTitleArray.length * mWidthOfItem, mHeight);
        } else if (widthMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mTitleArray.length * mWidthOfItem, heightMeasureSize);
        } else if (heightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSize, mHeight);
        } else if (widthMeasureMode == MeasureSpec.EXACTLY) {
            mWidthOfItem = getWidth() / mTitleArray.length;
        }

        initPathArray();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirstDraw) {
            isFirstDraw = false;
            drawFrame();
            Log.e("::::::::", "重绘边框");
        }
        drawCutLine(canvas);
        drawSelectedBackground(canvas);
        drawText(canvas);
    }


    /**
     * 绘制边框
     */
    private void drawFrame() {
        GradientDrawable gd = new GradientDrawable();
        gd.setDither(true);
        gd.setColor(mUnSelectBg);
        gd.setCornerRadius(mRadius);
        gd.setStroke(mLineWidth, mLineColor);
        this.setBackground(gd);
    }


    /**
     * 绘制分割线
     */
    private void drawCutLine(Canvas canvas) {
        mPaint.setColor(mLineColor);
        for (int i = 1; i < mTitleArray.length; i++) {
            canvas.drawLine(mWidthOfItem * i, 0, mWidthOfItem * i, mHeight, mPaint);
        }
    }

    /**
     * 绘制被选择的背景色
     */
    private void drawSelectedBackground(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPathArray[mCurIndex], mPaint);
    }

    /**
     * 绘制文本
     */
    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//基线到字体上边框的距离
        float bottom = fontMetrics.bottom;//基线到字体下边框的距离

        int baseLineY = (int) (mHeight / 2 - top / 2 - bottom / 2);
        for (int i = 0; i < mTitleArray.length; i++) {
            if (i == mCurIndex) {
                mPaint.setColor(Color.RED);
            } else {
                mPaint.setColor(mUnSelectTextColor);
            }
            canvas.drawText(mTitleArray[i], mWidthOfItem * i + mWidthOfItem / 2, baseLineY, mPaint);
        }
    }

    /**
     * 设置导航条文本
     *
     * @param titleArray
     */
    public void setTitleArray(String[] titleArray) {
        this.mTitleArray = titleArray;
    }


    /**
     * density = dpi/160 （dpi是每英寸的像素数，常见有：120、160、240、320）
     *
     * @param dpValue
     * @return
     */
    private int dp2px(float dpValue) {
//        //dp->px工具转换方法
//        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpValue,getResources().getDisplayMetrics());
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < mTitleArray.length; i++) {
                    if (event.getX() < mWidthOfItem * (i + 1) && event.getX() > mWidthOfItem * i) {
                        mCurIndex = i;
                        invalidate();
                        break;
                    }
                }
                break;

        }
        return true;
    }
}
