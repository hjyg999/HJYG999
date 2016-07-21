package com.md.hjyg.layoutEntities;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.ExhibitionSalelotteryModel.LotteryActivityPrize;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Process;
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: LotteryMeetDiskView date: 2016-2-26 下午2:10:25 remark: 会销-抽奖转盘
 * 
 * @author pyc
 */
@SuppressLint("HandlerLeak")
public class LotteryMeetDiskView extends View {

	private Context context;
	/**
	 * 圆的直径
	 */
	private int mRadius;
	/**
	 * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
	 */
//	private int mPadding;
	/**
	 * 控件的中心位置
	 */
	private int mCenter;

	/**
	 * 旋转的背景图的bitmap
	 */
	private Bitmap mRotateBgBitmap;
	private int mRotateBgBitmap_hight;
	/**
	 * 滚动的速度
	 */
	private double mSpeed;
	private volatile float mStartAngle = 0;

	/**
	 * 绘制文字的画笔
	 */
	private Paint mTextPaint;
	/**
	 * 抽奖的文字
	 */
	private String[] mStrs = new String[] { "特等奖", "3000元\n体验金", "二等奖",
			"1000元\n体验金", "三等奖", "2000元\n体验金" };
	private List<LotteryActivityPrize> lists;
	/**
	 * 盘块的个数
	 */
	private int mItemCount = mStrs.length;
	
	/**
	 * 是否点击了停止
	 */
	private boolean isShouldEnd;
	private boolean isstop;
	
	private long start,end,time;
	
	/***
	 * 是否开始旋转
	 */
	private boolean isStart;
	/***
	 * 是否开始抽奖
	 */
	private boolean isStartLottery;
	/**
	 * 抽奖结束后的停止角度
	 */
	private float target , target2;
	private Handler actvityHandle;
	
	public LotteryMeetDiskView(Context context) {
		this(context, null);
	}

	public LotteryMeetDiskView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LotteryMeetDiskView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		// 初始化绘制文字的画笔
		mTextPaint = new Paint();
		// 设置画笔宽度
		mTextPaint.setStrokeWidth(1);
		// 消除锯齿
		mTextPaint.setAntiAlias(true);
		// 设置镂空（方便查看效果）
		mTextPaint.setStyle(Style.FILL);

		initBitmapAndHight();
	}
	
	public void setData(List<LotteryActivityPrize> lists,Handler actvityHandle){
		this.lists = lists;
		this.actvityHandle = actvityHandle;
		invalidate();
	}

	/**
	 * 设置控件为正方形
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
		// 获取圆形的直径
		mRadius = width - getPaddingLeft() - getPaddingRight();
		// padding值
//		mPadding = getPaddingLeft();
		// 中心点
		mCenter = width / 2;
		setMeasuredDimension(width, width);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (lists == null) {
			return;
		}
		drawRotateBg(canvas, mStartAngle);
		
		/**
		 * 绘制每个块块，每个块块上的文本，每个块块上的图片
		 */
		float tmpAngle = mStartAngle;
		// 每个块得角度
		float sweepAngle = (float) (360 / mItemCount);
		for (int i = 0; i < mItemCount; i++) {
			// 绘制文本
			drawText(canvas,tmpAngle, sweepAngle, i);
			// 绘制Icon
//			drawIcon(tmpAngle, i);ss

			tmpAngle += sweepAngle;
		}
		
		end = System.currentTimeMillis();
		if (isStart) {
			mHandler.sendEmptyMessageDelayed(2, 100);
			time= time + 100;
			if (time >= 1500 && !isstop && isStartLottery) {
				luckyEnd();
				isstop = true;
			}
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 如果mSpeed不等于0，则相当于在滚动
				mStartAngle += mSpeed;

				// 点击停止时，设置mSpeed为递减，为0值转盘停止
				if (isShouldEnd) {
					mSpeed -= 1;
				}
				if (mSpeed <= 0) {
					mSpeed = 0;
					isShouldEnd = false;
				}
//				System.out.println("mStartAngle = " + mStartAngle +",mSpeed = " + mSpeed );
				
				invalidate();
				break;
			case 2:
				// 点击停止时，设置mSpeed为递减，为0值转盘停止
				if (isShouldEnd) {
					if (mStartAngle < target2) {
						mStartAngle += mSpeed;
						mSpeed--;
						if (mSpeed < 0) {
							mSpeed = 1;
						}
						if (mStartAngle >= target2) {
							mStartAngle = target2;
							mSpeed = 0;
							isShouldEnd = false;
							isStart = false;
							actvityHandle.sendEmptyMessageDelayed(6, 200);
						}
					}else {
						mStartAngle += mSpeed;
						mSpeed -= 1;
						if (mSpeed < 0) {
							mSpeed = 1;
						}
					}
					
				}else {
					mStartAngle += mSpeed;
				}
				
				if (mSpeed <= 0) {
//					mSpeed = 0;
//					isShouldEnd = false;
//					isStart = false;
//					actvityHandle.sendEmptyMessageDelayed(6, 120);
				}
//				System.out.println("mStartAngle = " + mStartAngle +",mSpeed = " + mSpeed + 
//						",target2 = " + target2 + 
//						",target = " + target);
//				
				invalidate();
				break;

			default:
				break;
			}
		};
	};
	
	private void startThre(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				end = System.currentTimeMillis();
				while (mSpeed > 0) {
					start = System.currentTimeMillis();
					mHandler.sendEmptyMessage(0);
					try {
						Thread.sleep(100);
						time= time + 100;
						if (time >= 1500 && !isstop) {
							luckyEnd();
							isstop = true;
						}
						if (end - start < 100) {
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		}).start();
	}
	
	/**
	 * 点击开始旋转
	 * 
	 * @param luckyIndex
	 */
	public void luckyStart(int luckyIndex) {
		// 每项角度大小
		float angle = (float) (360 / mItemCount);
		// 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
		float from = 270 - (luckyIndex + 1) * angle;
		float to = from + angle;
		// 停下来时旋转的距离
		float targetFrom = 4 * 360 + from;
		/**
		 * <pre>
		 *  (v1 + 0) * (v1+1) / 2 = target ; 
		 *  v1*v1 + v1 - 2target = 0 ; 
		 *  v1=-1+(1*1 + 8 *1 * target)/2;
		 * </pre>
		 */
		float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
		float targetTo = 4 * 360 + to;
		float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;

		mSpeed = (float) (v1 + Math.random() * (v2 - v1));
		isShouldEnd = false;
		isstop = false;
		time = 0;
		startThre();
	}
	
	public void StartRotate(){
		mSpeed = 55;
		isShouldEnd = false;
		isstop = false;
		isStart = true;
		mHandler.sendEmptyMessage(2);
		time = 0;
	}
	
	
	public void luckyStart_2(int luckyIndex) {
		// 每项角度大小
		float angle = (float) (360 / mItemCount);
		// 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
//		float from = 270 - (luckyIndex + 1) * angle;
//		float to = from + angle;
		target = (float) (4 * 360 + Math.random() * (angle) + 270 - (luckyIndex + 1)* angle);
		target2 = (float) target;
		mSpeed = (float) (Math.sqrt(1 * 1 + 8 * 1 * target2) - 1) / 2;
		isStartLottery = true;
	}
	
	/**
	 * 停止旋转
	 */
	public void luckyEnd() {
		mStartAngle = 0;
		isShouldEnd = true;
	}
	
	public void errorEnd(){
		mStartAngle = 0;
		isShouldEnd = true;
		mSpeed = 0;
		invalidate();
	}

	/**
	 * 画旋转背景
	 * 
	 * @param startAngle
	 */
	private void drawRotateBg(Canvas canvas, float startAngle) {

		canvas.save();// 保存当前画布状态
		canvas.translate(getMeasuredWidth() / 2, getMeasuredWidth() / 2); // 将坐标中心平移到要围绕的坐标点x,y
		canvas.rotate(startAngle);// 旋转角度，这里比如90度
		canvas.drawBitmap(mRotateBgBitmap, null, new Rect(
				-(mRotateBgBitmap_hight / 2), -(mRotateBgBitmap_hight / 2),
				(mRotateBgBitmap_hight / 2), (mRotateBgBitmap_hight / 2)), null);
		canvas.restore();// 恢复画图状态到保存前
	}

	/**
	 * 绘制文本
	 * 
	 * @param rect
	 * @param startAngle
	 *            开始角度
	 * @param sweepAngle
	 *            每一块的角度
	 * @param string
	 */
	private void drawText(Canvas canvas,float startAngle, float sweepAngle, int i) {
		mTextPaint.setColor(0xFF750DAD);
//		if (lists.get(i).type == 0) {
//			strings = 
//		}else {
//			
//		}
//		String strings[] = string.split("\n");
		// 设置文字的宽度为直径的3/4
		int imgWidth = (int) (mRotateBgBitmap_hight * 3.4 / 8);

		float angle1 = (float) ((startAngle) * (Math.PI / 180));

		int x1 = (int) (mCenter + imgWidth * Math.cos(angle1));
		int y1 = (int) (mCenter + imgWidth * Math.sin(angle1));
		float angle2 = (float) ((startAngle + sweepAngle) * (Math.PI / 180));

		int x2 = (int) (mCenter + imgWidth * Math.cos(angle2));
		int y2 = (int) (mCenter + imgWidth * Math.sin(angle2));
		// path.addArc(mRange, startAngle, sweepAngle);
		// 利用水平偏移让文字居中
		Path path = new Path();
		path.reset();
		path.moveTo(x1, y1);
		path.lineTo(x2, y2);
		// 直线的长度
		float lineSize = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
				* (y2 - y1));
		if (lists.get(i).type == 0) {
			mTextPaint.setTextSize(lineSize / 6);
			String PrizeName = lists.get(i).PrizeName;
			float textWidth = mTextPaint.measureText(PrizeName);
			float hOffset = (float) (lineSize - textWidth) / 2;
//			float vOffset = mRadius / 2 / 6;// 垂直偏移
			canvas.drawTextOnPath(PrizeName, path, hOffset, 0, mTextPaint);
		}else {
			mTextPaint.setTextSize(lineSize / 6);
			String PrizeName1 = "";
			// 实物奖品 = 0, 投资红包 = 1, 现金红包 = 2, 体验金红包 = 3
			PrizeName1 = (int)lists.get(i).RedEnvelopeAmount+"";
//			int type = lists.get(i).type;
//			if ( type == 3){
//				PrizeName1 = (int)lists.get(i).ExperienceAmount+"";
//			} else if (type == 2) {
//				PrizeName1 = (int)lists.get(i).RedEnvelopeAmount+"";
//			} else if (type == 1) {
//				PrizeName1 = (int)lists.get(i).InvestRedAmount+"";
//			} 
			String PrizeName2 = "元";
			if (lists.get(i).type == 5) {
				PrizeName2 = "%";
				PrizeName1 = Constants.StringToCurrency(lists.get(i).IncreaseRate+"");
			}
			float textWidth1 = mTextPaint.measureText(PrizeName1);
			mTextPaint.setTextSize((float) (lineSize / 6 *0.8));
			float textWidth2 = mTextPaint.measureText(PrizeName2);
			float hOffset1 = (float) (lineSize - textWidth1-textWidth2) / 2;
			mTextPaint.setTextSize(lineSize / 6);
			canvas.drawTextOnPath(PrizeName1, path, hOffset1, 0, mTextPaint);
			mTextPaint.setTextSize((float) (lineSize / 6 *0.8));
			canvas.drawTextOnPath(PrizeName2, path, hOffset1 + textWidth1, 0, mTextPaint);
			
			
		}
		if (lists.get(i).type != 0) {

			mTextPaint.setColor(0xFFA1377B);

			imgWidth = (int) (mRotateBgBitmap_hight * 2.8 / 8);

			angle1 = (float) ((startAngle) * (Math.PI / 180));

			x1 = (int) (mCenter + imgWidth * Math.cos(angle1));
			y1 = (int) (mCenter + imgWidth * Math.sin(angle1));
			angle2 = (float) ((startAngle + sweepAngle) * (Math.PI / 180));

			x2 = (int) (mCenter + imgWidth * Math.cos(angle2));
			y2 = (int) (mCenter + imgWidth * Math.sin(angle2));
			// path.addArc(mRange, startAngle, sweepAngle);
			// 利用水平偏移让文字居中
			path.reset();
			path.moveTo(x1, y1);
			path.lineTo(x2, y2);
			lineSize = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
					* (y2 - y1));
			mTextPaint.setTextSize(lineSize / 6);
			String PrizeNametype ="";
			if (lists.get(i).type == 3) {
				PrizeNametype = "体验金红包";
			}else if (lists.get(i).type == 2) {
				PrizeNametype = "现金红包";
			}else if (lists.get(i).type == 1) {
				PrizeNametype = "投资红包";
			}else if (lists.get(i).type == 5) {
				PrizeNametype = "加息券";
			}
			mTextPaint.setTextSize(lineSize / 7);
			float textWidth = mTextPaint.measureText(PrizeNametype);
			float hOffset = (float) (lineSize - textWidth) / 2;
			float vOffset = mRadius / 2 / 6;// 垂直偏移
			canvas.drawTextOnPath(PrizeNametype, path, hOffset, 0, mTextPaint);
		}
	}

	/**
	 * 设置图片和高度
	 */
	private void initBitmapAndHight() {

		Bitmap topimg = BitmapFactory.decodeResource(getResources(),
				R.drawable.meeting_topimg);

		mRotateBgBitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.meeting_rotate_bg), context, topimg);
		mRotateBgBitmap_hight = mRotateBgBitmap.getHeight();

	}

}
