package com.whf.indicatorformios;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
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
 *
 * @version 1.0.2
 */

public class IndicatorView extends View {

    private String[] mTitleArray;
    private Path[] mPathArray;
    private int mLength;
    private Paint mPaint;

    //高度(初始值为wrap_content时的高度)
    private static final int NOMAL_mHeight = 30;
    private static final int NOMAL_WIDTH_ITEM = 80;

    //实际高度
    private int mHeight;
    //单个选项的实际宽度
    private int mWidthOfItem;
    //字体大小
    private int mTextSize;
    //线的宽度
    private int mLineWidth;
    //圆角半径大小
    private int mRadius;

    //当前Indicator
    private int mCurIndex = 0;

    //未被选定时的背景色
    private int mUnSelectBg;
    //被选择定时的背景色
    private int mSelectBg;
    //未被选定时的字体颜色
    private int mUnSelectTextColor;
    //被选定时的字体颜色
    private int mSelectTextColor;
    //线的颜色
    private int mLineColor;

    private boolean isFirstDraw = true;

    private OnIndicatorItemClickListener mOnIndicatorItemClickListener;


    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);

        mUnSelectBg = typedArray.getColor(R.styleable.IndicatorView_unSelectedBackground, 0xFFFFFFFF);
        mSelectBg = typedArray.getColor(R.styleable.IndicatorView_selectedBackground, 0xFF666666);
        mSelectTextColor = typedArray.getColor(R.styleable.IndicatorView_selectedTextColor, 0xFFFFFFFF);
        mUnSelectTextColor = typedArray.getColor(R.styleable.IndicatorView_unSelectedTextColor, 0xFF666666);
        mLineColor = typedArray.getColor(R.styleable.IndicatorView_lineColor, 0xFF666666);

        mLineWidth = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_lineWidth, dp2px(1));
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_textSize, sp2px(15));
        mRadius = typedArray.getDimensionPixelSize(R.styleable.IndicatorView_radius, dp2px(5));
        typedArray.recycle();
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


    @Override
    protected void onMeasure(int widthMeasureSpec, int mHeightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, mHeightMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int mHeightMeasureMode = MeasureSpec.getMode(mHeightMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int mHeightMeasureSize = MeasureSpec.getSize(mHeightMeasureSpec);

        initTitleLength();

        if (widthMeasureMode == MeasureSpec.AT_MOST && mHeightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(mLength * NOMAL_WIDTH_ITEM), dp2px(NOMAL_mHeight));
        } else if (widthMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(dp2px(mLength * NOMAL_WIDTH_ITEM), mHeightMeasureSize);
        } else if (mHeightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSize, dp2px(NOMAL_mHeight));
        }

        if (mLength != 0) {
            mWidthOfItem = getWidth() / mLength;
        }
        mHeight = getHeight();

        initPathArray();
    }

    /**
     * 初始化标题数组长度
     */
    private void initTitleLength(){
        try {
            if (mTitleArray != null) {
                mLength = mTitleArray.length;
                if(mLength<2){
                    //抛出异常
                    throwException("标题个数小于2");
                }
            }else {
                //抛出异常
                throwException("标题数组为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化path数组
     */
    private void initPathArray() {

        mPathArray = new Path[mLength];

        for (int i = 0; i < mLength; i++) {
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
            } else if (i == mLength - 1) {
                int width = getWidth();
                path.moveTo(width - mWidthOfItem, 0);
                path.lineTo(width - mRadius, 0);
                path.quadTo(width, 0, width, mRadius);
                path.lineTo(width, mHeight - mRadius);
                path.quadTo(width, mHeight, width - mRadius, mHeight);
                path.lineTo(width - mWidthOfItem, mHeight);
                path.close();
                //中间的
            } else {
                int width = mWidthOfItem * i;
                path.moveTo(width, 0);
                path.lineTo(width + mWidthOfItem, 0);
                path.lineTo(width + mWidthOfItem, mHeight);
                path.lineTo(width, mHeight);
                path.close();
            }
            mPathArray[i] = path;
        }

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
        }

        drawCutLine(mPaint, canvas);
        drawSelectedBackground(mPaint, canvas);
        drawText(mPaint, canvas);
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
        this.setBackgroundDrawable(gd);
    }


    /**
     * 绘制分割线
     */
    private void drawCutLine(Paint paint, Canvas canvas) {
        paint.setColor(mLineColor);
        for (int i = 1; i < mLength; i++) {
            canvas.drawLine(mWidthOfItem * i, 0, mWidthOfItem * i, mHeight, paint);
        }
    }

    /**
     * 绘制被选择的背景色
     */
    private void drawSelectedBackground(Paint paint, Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mSelectBg);
        if (mPathArray != null && mPathArray.length > 0) {
            canvas.drawPath(mPathArray[mCurIndex], paint);
        }
    }

    /**
     * 绘制文本
     */
    private void drawText(Paint paint, Canvas canvas) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;//基线到字体上边框的距离
        float bottom = fontMetrics.bottom;//基线到字体下边框的距离
        int baseLineY = (int) (mHeight / 2 - top / 2 - bottom / 2);

        paint.setColor(mUnSelectTextColor);
        for (int i = 0; i < mLength; i++) {
            if (i == mCurIndex) {
                continue;
            }
            canvas.drawText(mTitleArray[i], mWidthOfItem * i + mWidthOfItem / 2, baseLineY, paint);
        }
        paint.setColor(mSelectTextColor);
        if (mTitleArray != null) {
            canvas.drawText(mTitleArray[mCurIndex]
                    , mWidthOfItem * mCurIndex + mWidthOfItem / 2, baseLineY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onItemTouch(event);
                break;
        }
        return true;
    }

    /**
     * Item的点击事件
     *
     * @param event
     */
    private void onItemTouch(MotionEvent event) {
        for (int i = 0; i < mLength; i++) {
            if (event.getX() < mWidthOfItem * (i + 1)) {
                mCurIndex = i;
                if (mOnIndicatorItemClickListener != null) {
                    mOnIndicatorItemClickListener.onIndicatorItemClick(mCurIndex);
                }
                invalidate();
                break;
            }
        }
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

    /**
     * 设置导航条文本
     *
     * @param titleArray
     */
    public void setTitleArray(String[] titleArray) {
        this.mTitleArray = titleArray;
    }

    /**
     * 设置当前选项卡的位置，从0开始
     *
     * @param index
     * @return
     */
    public String setCurIndex(int index) {
        mCurIndex = index;
        invalidate();
        return mTitleArray[index];
    }

    /**
     * Item点击事件的设置
     *
     * @param listener
     */
    public void setOnItemClickListener(OnIndicatorItemClickListener listener) {
        this.mOnIndicatorItemClickListener = listener;
    }

    public interface OnIndicatorItemClickListener {
        void onIndicatorItemClick(int position);
    }

    private void throwException(String str) {
        throw new IllegalArgumentException(str);
    }

}
