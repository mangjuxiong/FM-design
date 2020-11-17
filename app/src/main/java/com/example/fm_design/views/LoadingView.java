package com.example.fm_design.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.fm_design.R;

/**
 * Created by Lee on 2020/11/2.
 * Practice make perfect
 */
@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    private int rotateDegree=0;
    private boolean mNeedRotate=false;

    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        mNeedRotate=true;
        super.onAttachedToWindow();
        //绑定到windows时
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree +=30;
                rotateDegree = rotateDegree <=360?rotateDegree:0;
                invalidate();
                //是否继续选择
                if (mNeedRotate) {
                    postDelayed(this,100);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //从window解绑
        mNeedRotate=false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 第一个是旋转的角度，第二个是选择的x坐标，第三个是选择的y坐标，
         */
        canvas.rotate(rotateDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
