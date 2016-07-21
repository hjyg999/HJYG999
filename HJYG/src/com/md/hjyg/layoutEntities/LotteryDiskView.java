package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 * ClassName: LotteryDiskView date: 2016-2-23 下午1:55:58 remark: 抽奖圆盘
 * 
 * @author pyc
 */
public class LotteryDiskView extends SurfaceView implements Callback, Runnable {

	private Context context;

	// 通过SurfaceHolder可以监听SurfaceView的生命周期以及获取Canvas对象。
	private SurfaceHolder mHolder;

	/**
	 * 与SurfaceHolder绑定的Canvas
	 */
	private Canvas mCanvas;
	/**
	 * 用于绘制的线程
	 */
	private Thread t;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;

	/**
	 * 抽奖的文字
	 */
	private String[] mStrs = new String[] { "特等奖", "3000元\n体验金", "二等奖",
			"1000元\n体验金", "三等奖", "2000元\n体验金" };
	/**
	 * 每个盘块的颜色
	 */
	private int[] mColors = new int[] { 0xFFFFC734, 0xFFFE9D00, 0xFFFFC734,
			0xFFFE9D00, 0xFFFFC734, 0xFFFE9D00 };
	/**
	 * 盘块的个数
	 */
	private int mItemCount = mStrs.length;
	/**
	 * 绘制盘块的范围
	 */
	private RectF mRange = new RectF();
	/**
	 * 圆的直径
	 */
	private int mRadius;
	/**
	 * 绘制盘快的画笔
	 */
	private Paint mArcPaint;

	/**
	 * 绘制文字的画笔
	 */
	private Paint mTextPaint;
	/**
	 * 滚动的速度
	 */
	private double mSpeed;
	private volatile float mStartAngle = 0;
	/**
	 * 是否点击了停止
	 */
	private boolean isShouldEnd;

	/**
	 * 控件的中心位置
	 */
	private int mCenter;
	/**
	 * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
	 */
	private int mPadding;
	/**
	 * 大背景图的bitmap
	 */
	private Bitmap mBg;
	/**
	 * 背景图的bitmap
	 */
	private Bitmap mBgBitmap;
	private int mBgBitmap_hight;
	/**
	 * 旋转的背景图的bitmap
	 */
	private Bitmap mRotateBgBitmap;
	private int mRotateBgBitmap_hight;
	/**
	 * 文字的大小
	 */
	private float mTextSize = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics());

	public LotteryDiskView(Context context) {
		this(context, null);
	}

	public LotteryDiskView(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		mHolder = getHolder();
		mHolder.addCallback(this);

		// sz
		// setZOrderOnTop(true);// 设置画布 背景透明
		// mHolder.setFormat(PixelFormat.TRANSLUCENT);

		// 设置可获得焦点
		setFocusable(true);
		setFocusableInTouchMode(true);
		// 设置常亮
		this.setKeepScreenOn(true);
		initBitmapAndHight();

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

		// 初始化绘制圆弧的画笔
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);
		mArcPaint.setDither(true);
		// 初始化绘制文字的画笔
		mTextPaint = new Paint();
		// 设置画笔宽度
		mTextPaint.setStrokeWidth(1);
		// 消除锯齿
		mTextPaint.setAntiAlias(true);
		// 设置镂空（方便查看效果）
		mTextPaint.setStyle(Style.FILL);
		// 圆弧的绘制范围
		mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius
				+ getPaddingLeft(), mRadius + getPaddingLeft());

		// 初始化图片
		// mImgsBitmap = new Bitmap[mItemCount];
		// for (int i = 0; i < mItemCount; i++)
		// {
		// mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(),
		// mImgs[i]);
		// }

		// 开启线程
		isRunning = true;
		t = new Thread(this);
		t.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

		// 通知关闭线程
		isRunning = false;

	}

	@Override
	public void run() {
		int[] location = new int[2];
		getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
		// getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标

		// 不断的进行draw
		while (isRunning) {
			long start = System.currentTimeMillis();
			draw();
			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 画图
	 */
	private void draw() {
		try {
			// 获得canvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null) {
				// 绘制背景图
				drawBg();
				drawRotateBg(mStartAngle);

				/**
				 * 绘制每个块块，每个块块上的文本，每个块块上的图片
				 */
				float tmpAngle = mStartAngle;
				// 每个块得角度
				float sweepAngle = (float) (360 / mItemCount);
				for (int i = 0; i < mItemCount; i++) {
					// 绘制快快
					// mArcPaint.setColor(mColors[i]);
					// mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,
					// mArcPaint);
					// 绘制文本
					drawText(tmpAngle, sweepAngle, mStrs[i]);
					// 绘制Icon
					drawIcon(tmpAngle, i);

					tmpAngle += sweepAngle;
				}

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
				// 根据当前旋转的mStartAngle计算当前滚动到的区域
				calInExactArea(mStartAngle);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mCanvas != null)
				mHolder.unlockCanvasAndPost(mCanvas);
		}
	}

	/**
	 * 绘制图片
	 * 
	 * @param startAngle
	 * @param sweepAngle
	 * @param i
	 */
	private void drawIcon(float startAngle, int i) {
		// // 设置图片的宽度为直径的1/8
		// int imgWidth = mRadius / 8;
		//
		// float angle = (float) ((30 + startAngle) * (Math.PI / 180));
		//
		// int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
		// int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));
		//
		// // 确定绘制图片的位置
		// Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
		// / 2, y + imgWidth / 2);
		//
		// mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);

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
	private void drawText(float startAngle, float sweepAngle, String string) {
		mTextPaint.setColor(0xFF750DAD);
		String strings[] = string.split("\n");
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
		//直线的长度
		float lineSize = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
				* (y2 - y1));
		mTextPaint.setTextSize(lineSize / 6);
		float textWidth = mTextPaint.measureText(strings[0]);
		float hOffset = (float) (lineSize - textWidth) / 2;
		float vOffset = mRadius / 2 / 6;// 垂直偏移
		mCanvas.drawTextOnPath(strings[0], path, hOffset, 0, mTextPaint);
		if (strings.length == 2) {
			
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
			textWidth = mTextPaint.measureText(strings[1]);
			hOffset = (float) (lineSize - textWidth) / 2;
			vOffset = mRadius / 2 / 6;// 垂直偏移
			mTextPaint.setTextSize(lineSize / 6);
			mCanvas.drawTextOnPath(strings[1], path, hOffset, 0, mTextPaint);
		}
	}

	/**
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域
	 * 
	 * @param startAngle
	 */
	public void calInExactArea(float startAngle) {
		// 让指针从水平向右开始计算
		float rotate = startAngle + 90;
		rotate %= 360.0;
		for (int i = 0; i < mItemCount; i++) {
			// 每个的中奖范围
			float from = 360 - (i + 1) * (360 / mItemCount);
			float to = from + 360 - (i) * (360 / mItemCount);

			if ((rotate > from) && (rotate < to)) {
//				Log.d("TAG", mStrs[i]);
				return;
			}
		}
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
		mPadding = getPaddingLeft();
		// 中心点
		mCenter = width / 2;
		setMeasuredDimension(width, width);
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
	}

	/**
	 * 停止旋转
	 */
	public void luckyEnd() {
		mStartAngle = 0;
		isShouldEnd = true;
	}

	/**
	 * 是否正在旋转
	 * 
	 * @return
	 */
	public boolean isStart() {
		return mSpeed != 0;
	}

	/**
	 * 是否在停止过程中
	 * 
	 * @return
	 */
	public boolean isShouldEnd() {
		return isShouldEnd;
	}

	/**
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域 绘制背景，不重要，完全为了美观
	 */
	private void drawBg() {
		mCanvas.drawColor(0xFFFFFFFF);
		mCanvas.drawBitmap(mBg, null, new Rect(0, 0, getMeasuredWidth(),
				getMeasuredWidth()), null);
		mCanvas.drawBitmap(mBgBitmap, null,
				new Rect((getMeasuredWidth() - mBgBitmap_hight) / 2,
						(getMeasuredWidth() - mBgBitmap_hight) / 2,
						getMeasuredWidth()
								- (getMeasuredWidth() - mBgBitmap_hight) / 2,
						getMeasuredWidth()
								- (getMeasuredWidth() - mBgBitmap_hight) / 2),
				null);

	}

	/**
	 * 画旋转背景
	 * 
	 * @param startAngle
	 */
	private void drawRotateBg(float startAngle) {
		// 创建矩阵控制图片旋转和平移
		// Matrix matrix = new Matrix();
		// // 设置旋转角度
		// matrix.setRotate(startAngle);
		// // 设置左边距和上边距
		// // matrix.postTranslate(0, 0);
		// Bitmap bitmap = Bitmap.createBitmap(mRotateBgBitmap, 0, 0,
		// mRotateBgBitmap_hight, mRotateBgBitmap_hight, matrix, true);
		// // 绘制旋转图片
		// // mCanvas.drawBitmap(mRotateBgBitmap, matrix, null);
		// // mCanvas.drawBitmap

		mCanvas.save();// 保存当前画布状态
		mCanvas.translate(getMeasuredWidth() / 2, getMeasuredWidth() / 2); // 将坐标中心平移到要围绕的坐标点x,y
		mCanvas.rotate(startAngle);// 旋转角度，这里比如90度
		// mCanvas.drawBitmap(mRotateBgBitmap, null, new Rect(
		// (getMeasuredWidth() - mRotateBgBitmap_hight) / 2,
		// (getMeasuredWidth() - mRotateBgBitmap_hight) / 2,
		// getMeasuredWidth()
		// - (getMeasuredWidth() - mRotateBgBitmap_hight) / 2,
		// getMeasuredWidth()
		// - (getMeasuredWidth() - mRotateBgBitmap_hight) / 2),
		// null);
		mCanvas.drawBitmap(mRotateBgBitmap, null, new Rect(
				-(mRotateBgBitmap_hight / 2), -(mRotateBgBitmap_hight / 2),
				(mRotateBgBitmap_hight / 2), (mRotateBgBitmap_hight / 2)), null);
		mCanvas.restore();// 恢复画图状态到保存前
	}

	/**
	 * 设置图片和高度
	 */
	private void initBitmapAndHight() {

		mBg = BitmapFactory.decodeResource(getResources(),
				R.drawable.meeting_lotteryimg);
		Bitmap topimg = BitmapFactory.decodeResource(getResources(),
				R.drawable.meeting_topimg);
		mBgBitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.meeting_viewbg), context, topimg);
		mBgBitmap_hight = mBgBitmap.getHeight();

		mRotateBgBitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.meeting_rotate_bg), context, topimg);
		mRotateBgBitmap_hight = mRotateBgBitmap.getHeight();

	}

}
