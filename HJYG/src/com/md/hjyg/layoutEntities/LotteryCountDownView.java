package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: LotteryCountDownView date: 2015-12-23 上午9:35:21 remark:
 * 
 * @author pyc 抽奖倒计时
 */
public class LotteryCountDownView extends View {
	
	private Context context;
	private int angle;
	private boolean start = true;
	private int n = 10;
	
	private Bitmap[] bitmaps ;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 115:
				
				postInvalidate();
				if (n == 0) {
					start = false;
				}
				break;

			default:
				break;
			}
		};
	};

	public LotteryCountDownView(Context context) {
		this(context, null);
	}

	public LotteryCountDownView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LotteryCountDownView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
//		initBitmips();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		int rW =  getWidth()/2;
		int rH =  getHeight()/2;
		
		int r1 = (int) (getWidth()*0.3);
		int StrokeWidth = (int) (r1 * 0.1);
		
		Paint paint3 = new Paint();
		paint3.setStrokeWidth(5);
		paint3.setStyle(Paint.Style.STROKE);
		paint3.setColor(context.getResources().getColor(R.color.lottery_red));
		paint3.setAntiAlias(true);
		canvas.drawLine(rW -r1 -3*StrokeWidth, rH, rW -r1 +5, rH, paint3);
		canvas.drawLine(rW + r1 -5, rH, rW +r1 +3*StrokeWidth, rH, paint3);
		canvas.drawLine(rW , rH -r1 -3*StrokeWidth, rW , rH -r1 +5, paint3);
		canvas.drawLine(rW, rH +r1 - 5, rW,rH + r1 + 3*StrokeWidth, paint3);
		
		Paint paint = new Paint();
		paint.setStrokeWidth(StrokeWidth);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(context.getResources().getColor(R.color.lottery_red));
		paint.setAntiAlias(true);
		
		
		RectF oval = new RectF(rW - r1, rH - r1, rW + r1, rH + r1);
		canvas.drawArc(oval , 270, 360, false, paint);
		
		int r2 = (int) (r1*0.85);
		Paint paint2 = new Paint();
//		paint2.setStrokeWidth();
//		paint2.setStyle(Paint.Style.STROKE);
		paint2.setColor(context.getResources().getColor(R.color.lottery_red));
		paint2.setAntiAlias(true);
		canvas.drawArc(new RectF(rW - r2, rH - r2, rW + r2, rH + r2) , 270, 360, true, paint2);
		
		paint.setColor(context.getResources().getColor(R.color.lottery_r));
		paint2.setColor(context.getResources().getColor(R.color.lottery_r));
		canvas.drawArc(oval , 270, angle, false, paint);
		canvas.drawArc(new RectF(rW - r2, rH - r2, rW + r2, rH + r2) , 270, angle, true, paint2);
		
		if (angle >= 360) {
			angle = 0;
			n -- ;
		}
//		Paint bpaint = new Paint();
//		bpaint.setAntiAlias(true);
//		int br = (int) (r2*0.5);
//		Rect src = new Rect(rW - bitmaps[n].getWidth()/2, rH - bitmaps[n].getHeight()/2, rW + bitmaps[n].getWidth()/2, rH + bitmaps[n].getHeight()/2);
////		Rect dst = new Rect(0, 0, bitmaps[n].getWidth(), bitmaps[n].getHeight());
//		canvas.drawBitmap(bitmaps[n], src , src, bpaint);
		
	}
	
	public void timeStart(int n ) {
		this.n = n;
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (start) {
					angle = angle + 9;
					
					mHandler.sendEmptyMessage(115);
					
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		}).start();
	}
	public void timeStart(int n ,boolean sta) {
		this.start = sta;
		this.n = n;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (start) {
					angle = angle + 9;
					
					mHandler.sendEmptyMessage(115);
					
					try {
						Thread.sleep(25);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
			}
		}).start();
	}
	
//	private void initBitmips(){
//		bitmaps = new Bitmap[11];
//		bitmaps[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year10);
//		bitmaps[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year9);
//		bitmaps[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year8);
//		bitmaps[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year7);
//		bitmaps[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year6);
//		bitmaps[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year5);
//		bitmaps[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year4);
//		bitmaps[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year3);
//		bitmaps[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year2);
//		bitmaps[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year1);
//		bitmaps[10] = BitmapFactory.decodeResource(context.getResources(), R.drawable.year0);
//	}
	
//	private void setSweepAngle(){
//		Timer timer = new Timer();
//		timer.schedule(new TimerTask() {
//			
//			@Override
//			public void run() {
//				angle = angle + 9;
//				postInvalidate();
//				
//			}
//		}, 25, 25);
//	}

}
