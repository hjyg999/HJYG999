package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: ShadeProgressView date: 2016-4-23 下午3:34:09 remark:渐变进度条
 * 
 * @author pyc
 */
public class ShadeProgressView extends View {
	private int mHeight;
	private Context context;

	/**
	 * 当前进度值
	 */
	private float currentCount = 0f;
	private float myCount = 0f;
	private Paint mPaint;

	public ShadeProgressView(Context context) {
		this(context, null);
	}

	public ShadeProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ShadeProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeCap(Cap.ROUND);
		mPaint.setColor(Color.TRANSPARENT);
		mPaint.setStrokeWidth(6);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		RectF rectBlackBg = new RectF(0 + 6, 0 + 6, mHeight - 6, mHeight - 6);
		mPaint.setShader(null);
		mPaint.setColor(Color.WHITE);
		canvas.drawArc(rectBlackBg, 0, 360, false, mPaint);
		SweepGradient mSweepGradient = new SweepGradient(mHeight / 2,
				mHeight / 2, new int[] {
						getResources().getColor(R.color.red),
						getResources().getColor(R.color.red_8050A5) }, null);
		Matrix localM = new Matrix();
		localM.setRotate(-90, mHeight / 2, mHeight / 2);
		mSweepGradient.setLocalMatrix(localM);
		mPaint.setShader(mSweepGradient);
		canvas.drawArc(rectBlackBg, -90, currentCount*360, false, mPaint);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		if (heightSpecMode == MeasureSpec.UNSPECIFIED) {

			mHeight = 50;
		} else {
			mHeight = heightSpecSize;
			if (mHeight >= ScreenUtils.getScreenWidth(context)) {
				mHeight = ScreenUtils.getScreenWidth(context);
			}
		}
		setMeasuredDimension(mHeight, mHeight);
	}


	/***
	 * 设置当前的进度值
	 * 
	 * @param currentCount 0-1f
	 */
	public void setCurrentCount(float myCount) {
		this.myCount = myCount > 1 ? 1 : myCount;
//		invalidate();
		startThread();
	}
	
	private void startThread(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				currentCount = 0;
				while (currentCount < myCount) {
					currentCount = currentCount +0.02f;
					if (currentCount > myCount) {
						currentCount = myCount;
					}
					postInvalidate();
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}

}
