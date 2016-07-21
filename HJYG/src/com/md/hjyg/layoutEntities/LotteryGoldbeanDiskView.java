package com.md.hjyg.layoutEntities;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.GoldBeanLotteryInfoModel.GoldBeanLotteryPrize;
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
import android.util.AttributeSet;
import android.view.View;

/**
 * ClassName: LotteryGoldbeanDiskView date: 2016-5-26 下午3:41:59 remark:金豆抽奖转盆
 * 
 * @author pyc
 */
@SuppressLint("HandlerLeak")
public class LotteryGoldbeanDiskView extends View {
	private int mRadius;
	private int mCenter;
	private Bitmap mRotateBgBitmap;
	private Context context;
	private volatile float mStartAngle = 0;
	/**
	 * 抽奖的文字
	 */
	private String[] mStrs = new String[] { "2000个\n金豆", "50元\n投资红包",
			"10000元\n体验金", "500元\n投资红包", "5000元\n体验金", "50元\n投资红包",
			"200元\n投资红包", "10000元\n体验金", "2000元\n体验金", "谢谢\n参与", "8元\n现金红包",
			"2000元\n体验金" };
	/**
	 * 盘块的个数
	 */
	private int mItemCount = mStrs.length;
	/**
	 * 绘制文字的画笔
	 */
	private Paint mTextPaint;
	private int redColor;
	private int grayColor;
	/**
	 * 滚动的速度
	 */
	private double mSpeed;
	/**
	 * 是否点击了停止
	 */
	private boolean isShouldEnd;
	private boolean isstop;
	private long time;
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
	private float target, target2;
	private Handler actvityHandle;
	private LotteryInfo[] lotteryInfos;

	public LotteryGoldbeanDiskView(Context context) {
		this(context, null);
	}

	public LotteryGoldbeanDiskView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LotteryGoldbeanDiskView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(mRadius, mRadius);
	}

	private void init() {
		mRadius = Save.getScaleBitmapWangH(486, 486)[0];
		mCenter = mRadius / 2;
		Bitmap topimg = BitmapFactory.decodeResource(getResources(),
				R.drawable.novice_red_bg);
		mRotateBgBitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.gb_lottery_zp), context, topimg);

		// 初始化绘制文字的画笔
		mTextPaint = new Paint();
		// 设置画笔宽度
		mTextPaint.setStrokeWidth(1);
		// 消除锯齿
		mTextPaint.setAntiAlias(true);
		// 设置镂空（方便查看效果）
		mTextPaint.setStyle(Style.FILL);
		redColor = context.getResources().getColor(R.color.red);
		grayColor = context.getResources().getColor(R.color.gray_s);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (lotteryInfos == null || lotteryInfos.length == 0) {
			return;
		}
		drawRotateBg(canvas, mStartAngle);

		/**
		 * 绘制每个块块，每个块块上的文本，每个块块上的图片
		 */
		float tmpAngle = mStartAngle - 90 - 15;
		// 每个块得角度
		float sweepAngle = (float) (360 / mItemCount);
		for (int i = 0; i < mItemCount; i++) {
			// 绘制文本
			drawText(canvas, tmpAngle, sweepAngle, i);
			// 绘制Icon
			// drawIcon(tmpAngle, i);ss

			tmpAngle += sweepAngle;
		}

		if (isStart) {
			mHandler.sendEmptyMessageDelayed(2, 100);
			time = time + 100;
			if (time >= 1500 && !isstop && isStartLottery) {
				luckyEnd();
				isstop = true;
			}
		}
	}

	/**
	 * 画旋转背景
	 * 
	 * @param startAngle
	 */
	private void drawRotateBg(Canvas canvas, float startAngle) {

		canvas.save();// 保存当前画布状态
		canvas.translate(mRadius / 2, mRadius / 2); // 将坐标中心平移到要围绕的坐标点x,y
		canvas.rotate(startAngle);// 旋转角度，这里比如90度
		canvas.drawBitmap(mRotateBgBitmap, null, new Rect(-(mRadius / 2),
				-(mRadius / 2), (mRadius / 2), (mRadius / 2)), null);
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
	private void drawText(Canvas canvas, float startAngle, float sweepAngle,
			int i) {
		mTextPaint.setColor(0xFF750DAD);
		// if (lists.get(i).type == 0) {
		// strings =
		// }else {
		//
		// }
		// String strings[] = string.split("\n");
		// 设置文字的宽度为直径的3/4
		int imgWidth = (int) (mRadius * 3.4 / 8);

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
		mTextPaint.setTextSize(lineSize / 5.5f);
		// String string[] = new String[2];

		float textWidth = mTextPaint.measureText(lotteryInfos[i].str1);
		float hOffset = (float) (lineSize - textWidth) / 2;
		mTextPaint.setColor(lotteryInfos[i].color1);
		canvas.drawTextOnPath(lotteryInfos[i].str1, path, hOffset, 0,
				mTextPaint);
		// mTextPaint.setTextSize(lineSize / 6);

		imgWidth = (int) (mRadius * 3 / 8);

		angle1 = (float) ((startAngle) * (Math.PI / 180));

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
		// mTextPaint.setTextSize(lineSize / 6);
		textWidth = mTextPaint.measureText(lotteryInfos[i].str2);
		hOffset = (float) (lineSize - textWidth) / 2;

		mTextPaint.setColor(lotteryInfos[i].color2);
		canvas.drawTextOnPath(lotteryInfos[i].str2, path, hOffset, 0,
				mTextPaint);

	}

	/**
	 * 停止旋转
	 */
	private void luckyEnd() {
		stopThread();
	}

	private void stopThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (mStartAngle > 360) {
					if (mStartAngle % 360 >= 280) {
						mStartAngle = 0;
						isShouldEnd = true;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public void errorEnd() {
		mStartAngle = 0;
		isShouldEnd = true;
		mSpeed = 0;
		invalidate();
	}

	public void StartRotate() {
		mSpeed = 30;
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
		// float from = 270 - (luckyIndex + 1) * angle;
		// float to = from + angle;
		target = (float) (4 * 360 + Math.random() * (angle) + 90 + 180 - (luckyIndex + 1)
				* angle);
		// target = (float) (4 * 360 + Math.random() * (angle) +180 -
		// (luckyIndex + 1)* angle/2);
		// target2 = (float) (target - (mSpeed + 0) * (mSpeed +1) / 2);
		target2 = (float) target;
		isStartLottery = true;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
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
				// System.out.println("mStartAngle = " + mStartAngle
				// +",mSpeed = " + mSpeed );

				invalidate();
				break;
			case 2:
				// 点击停止时，设置mSpeed为递减，为0值转盘停止
				if (isShouldEnd) {
					if (mStartAngle < target2) {
						mStartAngle += mSpeed;
						if (mStartAngle >= target2) {
							mStartAngle = target2;
						}
					} else {
						mStartAngle += mSpeed;
						mSpeed -= 1;
					}

				} else {
					mStartAngle += mSpeed;
				}

				if (mSpeed <= 0) {
					mSpeed = 0;
					isShouldEnd = false;
					isStart = false;
					if (actvityHandle != null) {
						actvityHandle.sendEmptyMessageDelayed(6, 220);
					}
				}
				//
				invalidate();
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 设置奖品信息
	 */
	public void setLottryInfo(List<GoldBeanLotteryPrize> Prize, Handler mHandler) {
		this.actvityHandle = mHandler;
		if (Prize == null || Prize.size() == 0) {
			return;
		}
		mItemCount = Prize.size();
		lotteryInfos = new LotteryInfo[mItemCount];
		String str[] = new String[2];
		String PrizeName;
		for (int i = 0; i < mItemCount; i++) {
			switch (Prize.get(i).Type) {
			case 0:
				PrizeName = Prize.get(i).PrizeName;
				if (PrizeName.indexOf("手机") != -1) {
					str[0] = PrizeName.replace("手机", "");
					str[1] = "手机";
				} else if (PrizeName.indexOf("美的") != -1) {
					str[1] = PrizeName.replace("美的", "");
					str[0] = "美的";
				} else if (PrizeName.indexOf("九阳") != -1) {
					str[1] = PrizeName.replace("九阳", "");
					str[0] = "九阳";
				} else {
					int n = PrizeName.length();
					str[0] = PrizeName.substring(0, n / 2);
					str[1] = PrizeName.substring(n / 2, n);
				}
				lotteryInfos[i] = new LotteryInfo(str[0], str[1], redColor,
						redColor);
				break;
			case 1:
				lotteryInfos[i] = new LotteryInfo(
						(int) Prize.get(i).RedEnvelopeAmount + "元", "投资红包",
						redColor, grayColor);
				break;
			case 2:
				lotteryInfos[i] = new LotteryInfo(
						(int) Prize.get(i).RedEnvelopeAmount + "元", "现金红包",
						redColor, grayColor);
				break;
			case 3:
				lotteryInfos[i] = new LotteryInfo(
						(int) Prize.get(i).RedEnvelopeAmount + "元", "体验金",
						redColor, grayColor);
				break;
			case 4:
				lotteryInfos[i] = new LotteryInfo(
						(int) Prize.get(i).GoldBeanNumber + "个", "金豆",
						redColor, grayColor);
				break;
			case 5://加息券
				lotteryInfos[i] = new LotteryInfo(
						"+" + Constants.StringToCurrency(Prize.get(i).RedEnvelopeAmount +"")  + "%", "加息券",
						redColor, grayColor);
				break;
			default:
				lotteryInfos[i] = new LotteryInfo("", "", redColor, redColor);
				break;
			}
		}

		invalidate();
	}

	class LotteryInfo {

		public String str1, str2;
		public int color1, color2;

		public LotteryInfo(String str1, String str2, int color1, int color2) {
			this.str1 = str1;
			this.str2 = str2;
			this.color1 = color1;
			this.color2 = color2;

		}
	}

}
