package com.md.hjyg.layoutEntities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: RippleView date: 2016-5-25 上午9:57:49 remark: 波纹效果
 * 
 * @author pyc
 */
public class RippleView extends View implements Runnable {

	private Paint paint;
	// private int maxWidth = 255;
	// 是否运行
	private boolean isStarting = true;
	private List<Integer> alphas = new ArrayList<Integer>();
	private List<Integer> radius = new ArrayList<Integer>();
	private int size = 6;
	private int mW = 50;

	public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public RippleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RippleView(Context context) {
		super(context);
		init();
	}

	private void init() {
		paint = new Paint(); // 设置博文的颜色
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		for (int i = 0; i < size; i++) {
			alphas.add(50 + i*30);
			radius.add(mW/2-(mW/10)*i);
		}
//		alphaList.add("255");// 圆心的不透明度
//		startWidthList.add("0");
		new Thread(this).start();
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setBackgroundColor(Color.TRANSPARENT);// 颜色：完全透明
		for (int i = 0; i < size; i++) {
			paint.setAlpha(alphas.get(i));
			canvas.drawCircle(mW / 2, mW / 2,  radius.get(i), paint);
			if (isStarting) {
				if (alphas.get(i) - 30/4 <0) {
					alphas.set(i, 0);
				}else {
					alphas.set(i, (alphas.get(i) - 30/4) );
				}
				if (radius.get(i)+ mW/10/4>= mW/2) {
					radius.set(i,  mW/2 );
				}else {
					radius.set(i, (radius.get(i) + mW/10/4) );
				}
			}
		}
		if (isStarting && radius.get(0) != mW/2 - mW/10/4) {
			alphas.add(200);
			radius.add(0);
		}
		
		if (isStarting && (radius.get(0) == mW/2 || alphas.get(0) == 0)) {
			alphas.remove(0);
			radius.remove(0);
		}
		
	} 

	public void start() {
		isStarting = true;
		new Thread(this).start();
	} // 停止动画

	public void stop() {
		isStarting = false;
	} // 判断是都在不在执行 public

	boolean isStarting() {
		return isStarting;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mW, mW);
	}

	@Override
	public void run() {
		while (isStarting) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			postInvalidate();
		}
	}
	
	public void setWandH(int mW){
//		stop();
//		this.mW = mW;
//		isStarting = true;
//		init();
	}

}
