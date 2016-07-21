package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: InvestProgressView date: 2016-4-29 上午9:17:03 remark:首页理财进度条
 * 
 * @author pyc
 */
public class InvestProgressView extends View {
	private Context context;
	/**
	 * 控件的高度
	 */
	private int mHeight;
	private int mProgressWidth;
	/**
	 * 进度条的高度
	 */
	private int mProgressHeight;
	/**
	 * 控件的宽度
	 */
	private int mWidth;
	/**
	 * 进度条颜色
	 */
	private int color_Progress_gray;
	/**
	 * 文字颜色
	 */
	private int color_text_gray;
	/**
	 * 白色
	 */
	private int color_white;
	private Paint mPaint;
	/**
	 * 进度 0-100doub
	 */
	private double progress = 0;
	private double progress_v = 0;
	private int textSize;
	private float text_width;
	private String text = "";
	private int tlong;

	public InvestProgressView(Context context) {
		this(context, null);
	}

	public InvestProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public InvestProgressView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(1);
		textSize = ScreenUtils.sp2px(context, 12);
		mHeight = (int) (textSize * 1.5);
		mPaint.setTextSize(textSize);
		text_width = mPaint.measureText("99.9%");
		mProgressHeight = ScreenUtils.sp2px(context, 2);

		color_white = context.getResources().getColor(R.color.gray_sq);
		color_text_gray = context.getResources().getColor(R.color.gray_sq);
		color_Progress_gray = context.getResources().getColor(R.color.blue_ht);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		// int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		// if (heightSpecMode == MeasureSpec.UNSPECIFIED) {
		//
		// mHeight = 50;
		// } else {
		// mHeight = heightSpecSize;
		// }
		if (widthSpecMode == MeasureSpec.UNSPECIFIED) {

			mWidth = 50;
		} else {
			mWidth = widthSpecSize;
		}
		mProgressWidth = (int) (mWidth - text_width - 5);
		setMeasuredDimension(mWidth, mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 画进度条底色
		mPaint.setColor(color_white);
		canvas.drawRect(0, mHeight - mProgressHeight, mProgressWidth, mHeight,
				mPaint);
		// 画进度
		mPaint.setColor(color_Progress_gray);
		// 进度条的宽度
		float prw = (float) (mProgressWidth * progress/100);
		canvas.drawRect(0, mHeight - mProgressHeight, prw, mHeight, mPaint);
		// 写字
		if (progress == 1) {
			return;
		}
//		String text = String.valueOf(progress * 100);
//		if (text.indexOf(".") == -1) {
//			text = text + ".00";
//		} else {
//			text = text + "00";
//		}
//
//		if (text.indexOf(".") == 1) {
//			text = text.substring(0, 3) + "%";
//		} else {
//			text = text.substring(0, 4) + "%";
//		}

		mPaint.setColor(color_text_gray);
		if (progress == progress_v) {
			text = Constants.doubleTrans(progress) + "%";
		}else {
			
			text = getProgressText(progress+"") + "%";
		}

		canvas.drawText(text, mWidth - mPaint.measureText(text), mHeight - 1,
				mPaint);
		// if (prw - 4> text_width) {
		// canvas.drawText(text, prw - text_width-2, mHeight-2, mPaint);
		// }else {
		// canvas.drawText(text, 2, mHeight-2, mPaint);
		// }
	}

	public double getProgress() {
		return progress_v;
	}

	public void setProgress(double progress) {
		this.progress_v = progress;
		tlong = (progress+"").length();
		if (progress_v > 0 && progress_v < 100) {
			startThread();
		} else {
			this.progress = progress;
			invalidate();
		}
	}

	private void startThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				progress = 0;
				while (progress < progress_v) {
					progress = progress + 2.1;
					if (progress > progress_v) {
						progress = progress_v;
					}
					postInvalidate();
					try {
						Thread.sleep(40);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}).start();
	}
	
	private String getProgressText(String str){
		if (str==null) {
			return "";
		}
		if (str.length() > tlong) {
			return str.substring(0, tlong);
		}else {
			return str;
		}
		
	}

}
