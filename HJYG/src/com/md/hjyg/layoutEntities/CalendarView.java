package com.md.hjyg.layoutEntities;

import java.util.Calendar;
import java.util.Date;

import com.md.hjyg.R;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * ClassName: CalendarView date: 2016-4-21 上午9:26:36 remark:自定义日历控件
 * 
 * @author pyc
 */
public class CalendarView extends View implements View.OnTouchListener {
	private final static String TAG = "anCalendar";
	private Date selectedStartDate;
	private Date selectedEndDate;
	/** 当前日历显示的月 */
	private Date curDate;
	/** 今天的日期文字显示红色 */
	private Date today;
	/** 手指按下状态时临时日期 */
	private Date downDate;
	/** 日历显示的第一个日期和最后一个日期 */
//	private Date showFirstDate, showLastDate;
	/** 按下的格子索引 */
	private int downIndex;
	private Calendar calendar;
	private Surface surface;
	/** 日历显示数字 */
	private int[] date = new int[42];
	/** 当前显示的日历起始的索引 */
	private int curStartIndex, curEndIndex;
	/** 为false表示只选择了开始日期，true表示结束日期也选择了 */
//	private boolean completed = false;
	private boolean isSelectMore = false;
	/** 给控件设置监听事件 */
	private OnItemClickListener onItemClickListener;
	private int SelectIndex;
	private boolean isDrawBg;
	private int[] days;

	public CalendarView(Context context) {
		super(context);
		init();
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void init() {
		curDate = selectedStartDate = selectedEndDate = today = DateUtil.getDate();
		calendar = Calendar.getInstance();
		calendar.setTime(curDate);
		surface = new Surface();
		surface.density = getResources().getDisplayMetrics().density;
		setBackgroundColor(surface.bgColor);
		setOnTouchListener(this);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		surface.width = ScreenUtils.getScreenWidth(getContext());
		surface.height = surface.width;
//		surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 5);
		// if (View.MeasureSpec.getMode(widthMeasureSpec) ==
		// View.MeasureSpec.EXACTLY) {
		// surface.width = View.MeasureSpec.getSize(widthMeasureSpec);
		// }
		// if (View.MeasureSpec.getMode(heightMeasureSpec) ==
		// View.MeasureSpec.EXACTLY) {
		// surface.height = View.MeasureSpec.getSize(heightMeasureSpec);
		// }
		widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,
				View.MeasureSpec.EXACTLY);
		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,
				View.MeasureSpec.EXACTLY);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		Log.d(TAG, "[onLayout] changed:"
				+ (changed ? "new size" : "not change") + " left:" + left
				+ " top:" + top + " right:" + right + " bottom:" + bottom);
		if (changed) {
			surface.init();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG, "onDraw");
		// 画框
//		canvas.drawPath(surface.boxPath, surface.borderPaint);
		// 年月
		// String monthText = getYearAndmonth();
		// float textWidth = surface.monthPaint.measureText(monthText);
		// canvas.drawText(monthText, (surface.width - textWidth) / 2f,
		// surface.monthHeight * 3 / 4f, surface.monthPaint);
		// 上一月/下一月
		// canvas.drawPath(surface.preMonthBtnPath,
		// surface.monthChangeBtnPaint);
		// canvas.drawPath(surface.nextMonthBtnPath,
		// surface.monthChangeBtnPaint);
		// 星期
//		float weekTextY = surface.monthHeight + surface.weekHeight * 3 / 4f;
		// 星期背景
		// surface.cellBgPaint.setColor(surface.textColor);
		// canvas.drawRect(surface.weekHeight, surface.width,
		// surface.weekHeight, surface.width, surface.cellBgPaint);
//		float weekTextY =  surface.weekHeight ;
		for (int i = 0; i < surface.weekText.length; i++) {
//			float weekTextX = i
//					* surface.cellWidth
//					+ (surface.cellWidth - surface.weekPaint
//							.measureText(surface.weekText[i])) / 2f;
//			canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
//					surface.weekPaint);
			drawWeekText(canvas, i, surface.weekText[i], surface.textColor);
		}

		// 计算日期
		calculateDate();
		isDrawBg = false;
		
		// write date number
		// today index
		int todayIndex = -1;
		calendar.setTime(curDate);
		String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
				+ calendar.get(Calendar.MONTH);
		calendar.setTime(today);
		String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
				+ calendar.get(Calendar.MONTH);
		if (curYearAndMonth.equals(todayYearAndMonth)) {
			int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
			todayIndex = curStartIndex + todayNumber - 1;
		}
		
		drawtodayBg(canvas, todayIndex);
		// 按下状态，选择状态背景色
		drawDownOrSelectedBg(canvas);
		for (int i = 0; i < 42; i++) {
			int color = surface.textColor;
			if (days!=null && days.length > 0) {
				for (int j = 0; j < days.length; j++) {
					if (date[i] == days[j]) {
						color = surface.cellSelectedColor;
					}
				}
			}
			if (isLastMonth(i)) {
				color = surface.borderColor;
			} else if (isNextMonth(i)) {
				color = surface.borderColor;
			}
			
			if (i == SelectIndex && isDrawBg) {
				color = surface.cellSelectedTextColor;
			}
			drawCellText(canvas, i, date[i] + "", color);
		}
		super.onDraw(canvas);
	}

	/**
	 * 设置date[42]的数据
	 */
	private void calculateDate() {
		calendar.setTime(curDate);
		// 把日期设置为本月的第一天
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 本月的第一天是星期几
		int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
		Log.d(TAG, "day in week:" + dayInWeek);
		int monthStart = dayInWeek;
		if (monthStart == 1) {// 1为星期天
			monthStart = 8;
		}
		monthStart -= 1; // 以日为开头-1，以星期一为开头-2
		curStartIndex = monthStart;
		date[monthStart] = 1;
		// last month
		if (monthStart > 0) {
			// 设置0即为把日期设为上个月最后一天
			calendar.set(Calendar.DAY_OF_MONTH, 0);
			int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
			for (int i = monthStart - 1; i >= 0; i--) {
				date[i] = dayInmonth;
				dayInmonth--;
			}
			calendar.set(Calendar.DAY_OF_MONTH, date[0]);
		}
//		showFirstDate = calendar.getTime();
		// this month
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		// Log.d(TAG, "m:" + calendar.get(Calendar.MONTH) + " d:" +
		// calendar.get(Calendar.DAY_OF_MONTH));
		int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
		for (int i = 1; i < monthDay; i++) {
			date[monthStart + i] = i + 1;
		}
		curEndIndex = monthStart + monthDay;
		// next month
		for (int i = monthStart + monthDay; i < 42; i++) {
			date[i] = i - (monthStart + monthDay) + 1;
		}
		if (curEndIndex < 42) {
			// 显示了下一月的
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		calendar.set(Calendar.DAY_OF_MONTH, date[41]);
//		showLastDate = calendar.getTime();
	}

	/**
	 * 画日期数字
	 * @param canvas
	 * @param index
	 * @param text
	 */
	private void drawCellText(Canvas canvas, int index, String text, int color) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		
		surface.datePaint.setColor(color);
		float cellY = surface.monthHeight + surface.weekHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight/2  + surface.textHeight/4;
		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth - surface.datePaint.measureText(text))
				/ 2f;
		canvas.drawText(text, cellX, cellY, surface.datePaint);
	}
	
	/**
	 * 写星期
	 * @param canvas
	 * @param index
	 * @param text
	 * @param color
	 */
	private void drawWeekText(Canvas canvas, int index, String text, int color) {
		surface.datePaint.setColor(color);
		
		float cellY = surface.monthHeight + surface.weekHeight/2  + surface.textHeight/4 ;
		float cellX = (surface.cellWidth * index )
				+ (surface.cellWidth - surface.datePaint.measureText(text))
				/ 2f;
		canvas.drawText(text, cellX, cellY, surface.datePaint);
	}

	/**
	 * 选择日期的背景颜色
	 * @param canvas
	 * @param index
	 * @param color
	 */
	private void drawCellBg(Canvas canvas, int index, int color) {
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		SelectIndex = index;
		isDrawBg = true;
		surface.datePaint.setColor(color);
		float cellY = surface.monthHeight + surface.cellHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight/2 ;
		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth )
				/ 2f;
		canvas.drawCircle(cellX, cellY, surface.cellHeight/2-8, surface.datePaint);
	}
	
	/**
	 * 画当天日期的背景
	 * @param canvas
	 * @param index
	 */
	private void drawtodayBg(Canvas canvas,int index){
		int x = getXByIndex(index);
		int y = getYByIndex(index);
		surface.datePaint.setColor(surface.textColor_todaybg);
		float cellY = surface.monthHeight + surface.cellHeight + (y - 1)
				* surface.cellHeight + surface.cellHeight/2 ;
		float cellX = (surface.cellWidth * (x - 1))
				+ (surface.cellWidth )
				/ 2f;
		canvas.drawCircle(cellX, cellY, surface.cellHeight/2-3, surface.datePaint);
	}

	private void drawDownOrSelectedBg(Canvas canvas) {
		// down and not up
		if (downDate != null && downIndex != -1) {
			drawCellBg(canvas, downIndex, surface.cellDownColor);
		}
		// selected bg color
//		if (!selectedEndDate.before(showFirstDate)
//				&& !selectedStartDate.after(showLastDate)) {
//			int[] section = new int[] { -1, -1 };
//			calendar.setTime(curDate);
//			calendar.add(Calendar.MONTH, -1);
//			findSelectedIndex(0, curStartIndex, calendar, section);
//			if (section[1] == -1) {
//				calendar.setTime(curDate);
//				findSelectedIndex(curStartIndex, curEndIndex, calendar, section);
//			}
//			if (section[1] == -1) {
//				calendar.setTime(curDate);
//				calendar.add(Calendar.MONTH, 1);
//				findSelectedIndex(curEndIndex, 42, calendar, section);
//			}
//			if (section[0] == -1) {
//				section[0] = 0;
//			}
//			if (section[1] == -1) {
//				section[1] = 41;
//			}
//			for (int i = section[0]; i <= section[1]; i++) {
//				drawCellBg(canvas, i, surface.cellSelectedColor);
//			}
//		}
	}

	public void findSelectedIndex(int startIndex, int endIndex,
			Calendar calendar, int[] section) {
		for (int i = startIndex; i < endIndex; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, date[i]);
			Date temp = calendar.getTime();
			// Log.d(TAG, "temp:" + temp.toLocaleString());
			if (temp.compareTo(selectedStartDate) == 0) {
				section[0] = i;
			}
			if (temp.compareTo(selectedEndDate) == 0) {
				section[1] = i;
				return;
			}
		}
	}

	public Date getSelectedStartDate() {
		return selectedStartDate;
	}

	public Date getSelectedEndDate() {
		return selectedEndDate;
	}

	private boolean isLastMonth(int i) {
		if (i < curStartIndex) {
			return true;
		}
		return false;
	}

	private boolean isNextMonth(int i) {
		if (i >= curEndIndex) {
			return true;
		}
		return false;
	}

	private int getXByIndex(int i) {
		return i % 7 + 1; // 1 2 3 4 5 6 7
	}

	private int getYByIndex(int i) {
		return i / 7 + 1; // 1 2 3 4 5 6
	}
	
	/**
	 * 获取当前月
	 * @return
	 */
	public String getNewMonth(){
		curDate = DateUtil.getDate();
		invalidate();
		return getYearAndmonth();
	}

	// 获得当前应该显示的年月
	public String getYearAndmonth() {
		calendar.setTime(curDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return year + "-" + month;
	}

	// 上一月
	public String clickLeftMonth() {
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, -1);
		curDate = calendar.getTime();
		downIndex = -1;
		invalidate();
		return getYearAndmonth();
	}

	// 下一月
	public String clickRightMonth() {
		calendar.setTime(curDate);
		calendar.add(Calendar.MONTH, 1);
		curDate = calendar.getTime();
		downIndex = -1;
		invalidate();
		return getYearAndmonth();
	}

	// 设置日历时间
	public void setCalendarData(Date date) {
		calendar.setTime(date);
		invalidate();
	}

	// 获取日历时间
	public void getCalendatData() {
		calendar.getTime();
	}

	// 设置是否多选
	public boolean isSelectMore() {
		return isSelectMore;
	}

	public void setSelectMore(boolean isSelectMore) {
		this.isSelectMore = isSelectMore;
	}
	
	

	public void setDays(int[] days) {
		this.days = days;
		if (days == null || days.length == 0) {
//			invalidate();
			return;
		}
		invalidate();
	}

	private void setSelectedDateByCoor(float x, float y) {
		// change month
		// if (y < surface.monthHeight) {
		// // pre month
		// if (x < surface.monthChangeWidth) {
		// calendar.setTime(curDate);
		// calendar.add(Calendar.MONTH, -1);
		// curDate = calendar.getTime();
		// }
		// // next month
		// else if (x > surface.width - surface.monthChangeWidth) {
		// calendar.setTime(curDate);
		// calendar.add(Calendar.MONTH, 1);
		// curDate = calendar.getTime();
		// }
		// }
		// cell click down
		if (y > surface.monthHeight + surface.weekHeight) {
			//列
			int m = (int) (Math.floor(x / surface.cellWidth) + 1);
			//行
			int n = (int) (Math
					.floor((y - (surface.monthHeight + surface.weekHeight))
							/ Float.valueOf(surface.cellHeight)) + 1);
			Date downnewDate = null;
			downIndex = (n - 1) * 7 + m - 1;
			Log.d(TAG, "downIndex:" + downIndex);
			calendar.setTime(curDate);
			if (isLastMonth(downIndex)) {
//				calendar.add(Calendar.MONTH, -1);
			} else if (isNextMonth(downIndex)) {
//				calendar.add(Calendar.MONTH, 1);
			}else {
				calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);
				downnewDate = calendar.getTime();
				if (downDate != null && downDate.getTime() == downnewDate.getTime()) {
					downDate = null;
				}else {
					downDate =  downnewDate;
				}
//				downDate = calendar.getTime();
				if (onItemClickListener != null) {
					onItemClickListener.OnItemClick(selectedStartDate,
							selectedEndDate, downDate);
				}
				invalidate();
			}
			
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			downDate = null;
			break;
//		case MotionEvent.ACTION_MOVE:
//			setSelectedDateByCoor(event.getX(), event.getY());
//			break;
		case MotionEvent.ACTION_UP:
			setSelectedDateByCoor(event.getX(), event.getY());
//			if (downDate != null) {
//				if (isSelectMore) {
//					if (!completed) {
//						if (downDate.before(selectedStartDate)) {
//							selectedEndDate = selectedStartDate;
//							selectedStartDate = downDate;
//						} else {
//							selectedEndDate = downDate;
//						}
//						completed = true;
//						// 响应监听事件
//						if (onItemClickListener != null) {
//							onItemClickListener.OnItemClick(selectedStartDate,
//									selectedEndDate, downDate);
//						}
//					} else {
//						selectedStartDate = selectedEndDate = downDate;
//						completed = false;
//					}
//				} else {
//					selectedStartDate = selectedEndDate = downDate;
//					// 响应监听事件
//					if (onItemClickListener != null) {
//						onItemClickListener.OnItemClick(selectedStartDate,
//								selectedEndDate, downDate);
//					}
//				}
//				invalidate();
//			}

			break;
		}
		return true;
	}

	// 给控件设置监听事件
	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	// 监听接口
	public interface OnItemClickListener {
		void OnItemClick(Date selectedStartDate, Date selectedEndDate,
				Date downDate);
	}

	/**
	 * 
	 * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
	 */
	private class Surface {
		/**字体大小*/
		public float textSize;
		/**
		 * 字体的高度
		 */
		public int textHeight;
		
		public float density;
		/** 整个控件的宽度 */
		public int width;
		/** 整个控件的高度 */
		public int height;
		/** 显示月的高度 */
		public float monthHeight;
		// public float monthChangeWidth; // 上一月、下一月按钮宽度
		/** 显示星期的高度 */
		public float weekHeight;
		/** 日期方框宽度 */
		public float cellWidth;
		/** 日期方框高度 */
		public float cellHeight;
		public float borderWidth;
		public int bgColor = Color.parseColor("#FFFFFF");
		/**
		 * 当月日期和星期字体的颜色
		 */
		private int textColor = getResources().getColor(R.color.gray_gold);
		private int textColor_todaybg = getResources().getColor(R.color.gold_e8);
		// private int textColorUnimportant = Color.parseColor("#666666");
		private int btnColor = Color.parseColor("#666666");
		private int borderColor = getResources().getColor(R.color.gray_q);
		/**
		 * 按下状态的背景颜色
		 */
		public int cellDownColor = getResources().getColor(R.color.yellow_FF972E);
		/**
		 * 选择日期的背景颜色
		 */
		public int cellSelectedColor = getResources().getColor(R.color.yellow_FF972E);
		public int cellSelectedTextColor = getResources().getColor(R.color.white);
		public Paint borderPaint;
		public Paint monthPaint;
		public Paint weekPaint;
		public Paint datePaint;
		public Paint monthChangeBtnPaint;
		public Paint cellBgPaint;
		public Path boxPath; // 边框路径
		// public Path preMonthBtnPath; // 上一月按钮三角形
		// public Path nextMonthBtnPath; // 下一月按钮三角形
		public String[] weekText = { "日", "一", "二", "三", "四", "五", "六" };

		// public String[] monthText =
		// {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

		public void init() {
			float temp = height / 7f;
			monthHeight = 0;// (float) ((temp + temp * 0.3f) * 0.6);
			// monthChangeWidth = monthHeight * 1.5f;
//			weekHeight = (float) ((temp + temp * 0.3f) * 0.7);
//			cellHeight = (height - monthHeight - weekHeight) / 6f;
			weekHeight = temp;
			cellHeight = temp;
			cellWidth = width / 7f;
			borderPaint = new Paint();
			borderPaint.setColor(borderColor);
			borderPaint.setStyle(Paint.Style.STROKE);
			borderWidth = (float) (0.5 * density);
			// Log.d(TAG, "borderwidth:" + borderWidth);
			borderWidth = borderWidth < 1 ? 1 : borderWidth;
			borderPaint.setStrokeWidth(borderWidth);
			monthPaint = new Paint();
			monthPaint.setColor(textColor);
			monthPaint.setAntiAlias(true);
			textSize = cellHeight * 0.4f;
			Log.d(TAG, "text size:" + textSize);
			monthPaint.setTextSize(textSize);
//			monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
			weekPaint = new Paint();
			weekPaint.setColor(textColor);
			weekPaint.setAntiAlias(true);
			weekPaint.setTextSize(textSize);
//			weekPaint.setTypeface(Typeface.DEFAULT_BOLD);
			datePaint = new Paint();
			datePaint.setColor(textColor);
			datePaint.setAntiAlias(true);
			datePaint.setTextSize(textSize);
//			datePaint.setTypeface(Typeface.DEFAULT_BOLD);
			//字体的高度
			Paint.FontMetricsInt fm = surface.datePaint.getFontMetricsInt(); 
			textHeight = fm.bottom - fm.top;
			
			boxPath = new Path();
			// boxPath.addRect(0, 0, width, height, Direction.CW);
			// boxPath.moveTo(0, monthHeight);
			boxPath.rLineTo(width, 0);
			boxPath.moveTo(0, monthHeight + weekHeight);
			boxPath.rLineTo(width, 0);
			for (int i = 1; i < 6; i++) {
				boxPath.moveTo(0, monthHeight + weekHeight + i * cellHeight);
				boxPath.rLineTo(width, 0);
				boxPath.moveTo(i * cellWidth, monthHeight);
				boxPath.rLineTo(0, height - monthHeight);
			}
			boxPath.moveTo(6 * cellWidth, monthHeight);
			boxPath.rLineTo(0, height - monthHeight);
			// preMonthBtnPath = new Path();
			// int btnHeight = (int) (monthHeight * 0.6f);
			// preMonthBtnPath.moveTo(monthChangeWidth / 2f, monthHeight / 2f);
			// preMonthBtnPath.rLineTo(btnHeight / 2f, -btnHeight / 2f);
			// preMonthBtnPath.rLineTo(0, btnHeight);
			// preMonthBtnPath.close();
			// nextMonthBtnPath = new Path();
			// nextMonthBtnPath.moveTo(width - monthChangeWidth / 2f,
			// monthHeight / 2f);
			// nextMonthBtnPath.rLineTo(-btnHeight / 2f, -btnHeight / 2f);
			// nextMonthBtnPath.rLineTo(0, btnHeight);
			// nextMonthBtnPath.close();
			monthChangeBtnPaint = new Paint();
			monthChangeBtnPaint.setAntiAlias(true);
			monthChangeBtnPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			monthChangeBtnPaint.setColor(btnColor);
			cellBgPaint = new Paint();
			cellBgPaint.setAntiAlias(true);
			cellBgPaint.setStyle(Paint.Style.FILL);
			cellBgPaint.setColor(cellSelectedColor);
		}
	}
}