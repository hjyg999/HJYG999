package com.md.hjyg.layoutEntities;

import java.util.Calendar;

import com.md.hjyg.R;
import com.md.hjyg.utils.DateUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * ClassName: RefreshableScrollView date: 2015-11-18 下午4:56:35
 * remark:ScrollView下拉刷新
 * 
 * @author pyc
 */
public class RefreshableScrollView extends LinearLayout {

	private static final String TAG = "LILITH";
	private Scroller scroller;
	private View refreshView;
//	private ImageView refreshIndicatorView;
	private int refreshTargetTop = -80;
	private ProgressBar bar;
//	private ImageView progress_cter;
	private TextView downTextView;
	private TextView timeTextView;
	// private LinearLayout reFreshTimeLayout;//显示上次刷新时间的layout
	private RefreshListener refreshListener;

	private String downTextString;
	private String releaseTextString;

	// private Long refreshTime = null;
	private int lastX;
	private int lastY;
	// 拉动标记
	private boolean isDragging = false;
	// 是否可刷新标记
	private boolean isRefreshEnabled = true;
	// 在刷新中标记
	private boolean isRefreshing = false;
	Calendar LastRefreshTime;

	/**
	 * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
	 */
	private static final String UPDATED_AT = "updated_at";
	/**
	 * 用于存储上次更新时间
	 */
	private SharedPreferences preferences;
	/**
	 * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 */
	private int mId = -1;
	/**
	 * 一分钟的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * 一小时的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * 一天的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 一月的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * 一年的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;
	/**
	 * 上次更新时间的毫秒值
	 */
	private long lastUpdateTime;

	private Context mContext;

	public RefreshableScrollView(Context context) {
		super(context);
		mContext = context;

	}

	public RefreshableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		mContext = context;
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		// 滑动对象，
		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(DateUtil.getDate());
		LastRefreshTime = calendar;
		scroller = new Scroller(mContext);

		// 刷新视图顶端的的view
		refreshView = LayoutInflater.from(mContext).inflate(
				R.layout.refresh_top_item, null);
//		LinearLayout.LayoutParams params = (LayoutParams) refreshView.getLayoutParams();
//		params.height = (int) (ScreenUtils.getScreenHeight(mContext)*0.1);
//		refreshView.setLayoutParams(params);
		
		// 指示器view
//		refreshIndicatorView = (ImageView) refreshView
//				.findViewById(R.id.indicator);
		// 刷新bar
		bar = (ProgressBar) refreshView.findViewById(R.id.progress);
//		progress_cter = (ImageView) refreshView.findViewById(R.id.progress_cter);
		// 下拉显示text
		downTextView = (TextView) refreshView.findViewById(R.id.refresh_hint);
		// 下来显示时间
		timeTextView = (TextView) refreshView.findViewById(R.id.refresh_time);
		// reFreshTimeLayout=(LinearLayout)refreshView.findViewById(R.id.refresh_time_layout);

		LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, -refreshTargetTop);
		lp.topMargin = refreshTargetTop;
		lp.gravity = Gravity.CENTER;
		addView(refreshView, lp);
		downTextString = mContext.getResources().getString(
				R.string.pull_to_refresh);
		releaseTextString = mContext.getResources().getString(
				R.string.release_to_refresh);
//		LinearLayout.LayoutParams params = (LayoutParams) refreshView.getLayoutParams();
//		params.height = (int) (ScreenUtils.getScreenHeight(mContext)*0.1);
//		refreshView.setLayoutParams(params);
	}

	/**
	 * 设置上次刷新时间
	 * 
	 * @param time
	 */
	private void setLastRefreshTimeText() {
		// TODO Auto-generated method stub
		// reFreshTimeLayout.setVisibility(View.VISIBLE);
		Calendar calendar =  Calendar.getInstance();
		calendar.setTime(DateUtil.getDate());
		Calendar NowTime = calendar;
		long l = NowTime.getTimeInMillis() - LastRefreshTime.getTimeInMillis();
		int days = new Long(l / (1000 * 60 * 60 * 24)).intValue();
		int hour = new Long(l / (1000 * 60 * 60)).intValue();
		int min = new Long(l / (1000 * 60)).intValue();
		if (days != 0) {
			timeTextView.setText(days + "天");
		} else if (hour != 0) {
			timeTextView.setText(hour + "小时");
		} else if (min != 0) {
			timeTextView.setText(min + "分钟");
		}

		// timeTextView.setText(time);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		int y = (int) event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 记录下y坐标
			lastY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			Log.i(TAG, "ACTION_MOVE");
			// y移动坐标
			int m = y - lastY;
			if (((m < 6) && (m > -1)) || (!isDragging)) {
				// setLastRefreshTimeText();
				refreshUpdatedAtValue();
				doMovement(m);
			}
			// 记录下此刻y坐标
			this.lastY = y;
			break;

		case MotionEvent.ACTION_UP:
			Log.i(TAG, "ACTION_UP");

			fling();

			break;
		}
		return true;
	}

	/**
	 * up事件处理
	 */
	private void fling() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LayoutParams) refreshView
				.getLayoutParams();
		Log.i(TAG, "fling()" + lp.topMargin);
		if (lp.topMargin > 0) {// 拉到了触发可刷新事件
			refresh();
		} else {
			returnInitState();
		}
	}

	private void returnInitState() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		scroller.startScroll(0, i, 0, refreshTargetTop);
		invalidate();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		// reFreshTimeLayout.setVisibility(View.GONE);
//		refreshIndicatorView.setVisibility(View.GONE);
		bar.setVisibility(View.VISIBLE);
//		progress_cter.setVisibility(View.VISIBLE);
		timeTextView.setVisibility(View.VISIBLE);
		downTextView.setVisibility(View.VISIBLE);
		downTextView.setText("正在刷新...");
		scroller.startScroll(0, i, 0, 0 - i);
		invalidate();
		if (refreshListener != null) {
			refreshListener.onRefresh(this);
			isRefreshing = true;
		}
	}

	/**
	     * 
	     */
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (scroller.computeScrollOffset()) {
			int i = this.scroller.getCurrY();
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
					.getLayoutParams();
			int k = Math.max(i, refreshTargetTop);
			lp.topMargin = k;
			this.refreshView.setLayoutParams(lp);
			this.refreshView.invalidate();
			invalidate();
		}
	}

	/**
	 * 下拉move事件处理
	 * 
	 * @param moveY
	 */
	private void doMovement(int moveY) {
		// TODO Auto-generated method stub
		LinearLayout.LayoutParams lp = (LayoutParams) refreshView
				.getLayoutParams();
		if (moveY > 0) {
			// 获取view的上边距
			float f1 = lp.topMargin;
			float f2 = moveY * 0.3F;
			int i = (int) (f1 + f2);
			// 修改上边距
			lp.topMargin = i;
			// 修改后刷新
			refreshView.setLayoutParams(lp);
			refreshView.invalidate();
			invalidate();
		} else {
			float f1 = lp.topMargin;
			int i = (int) (f1 + moveY * 0.9F);
			Log.i("aa", String.valueOf(i));
			if (i >= refreshTargetTop) {
				lp.topMargin = i;
				// 修改后刷新
				refreshView.setLayoutParams(lp);
				refreshView.invalidate();
				invalidate();
			} else {

			}
		}
		refreshView.setVisibility(View.VISIBLE);
		timeTextView.setVisibility(View.VISIBLE);
		// if(refreshTime!= null){
		// setRefreshTime(refreshTime);
		// }
		downTextView.setVisibility(View.VISIBLE);

//		refreshIndicatorView.setVisibility(View.VISIBLE);
//		bar.setVisibility(View.GONE);
//		progress_cter.setVisibility(View.GONE);
		if (lp.topMargin > 0) {
			downTextView.setText(R.string.release_to_refresh);
//			refreshIndicatorView.setImageResource(R.drawable.up_list);
		} else {
			downTextView.setText(R.string.pull_to_refresh);
//			refreshIndicatorView.setImageResource(R.drawable.arrow_list);
		}

	}

	public void setRefreshEnabled(boolean b) {
		this.isRefreshEnabled = b;
	}

	public void setRefreshListener(RefreshListener listener, int id) {
		this.refreshListener = listener;
		mId = id;
	}

	/**
	 * 结束刷新事件
	 */
	public void finishRefresh() {
		Log.i(TAG, "执行了=====finishRefresh");
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) this.refreshView
				.getLayoutParams();
		int i = lp.topMargin;
		// timeTextView.setVisibility(View.VISIBLE);
		scroller.startScroll(0, i, 0, refreshTargetTop);
		refreshView.setVisibility(View.GONE);
		invalidate();
		isRefreshing = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.getDate());
		LastRefreshTime = calendar;
		
		preferences.edit()
				.putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
	}

	/*
	 * 该方法一般和ontouchEvent 一起用 (non-Javadoc)
	 * 
	 * @see
	 * android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		int action = e.getAction();
		int y = (int) e.getRawY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;

		case MotionEvent.ACTION_MOVE:
			// y移动坐标
			int m = y - lastY;

			// 记录下此刻y坐标
			this.lastY = y;
			if (m > 6 && canScroll()) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:

			break;

		case MotionEvent.ACTION_CANCEL:

			break;
		}
		return false;
	}

	private boolean canScroll() {
		// TODO Auto-generated method stub
		View childView;
		if (getChildCount() > 1) {
			childView = this.getChildAt(1);
			if (childView instanceof ListView) {
				int top = ((ListView) childView).getChildAt(0).getTop();
				int pad = ((ListView) childView).getListPaddingTop();
				if ((Math.abs(top - pad)) < 3
						&& ((ListView) childView).getFirstVisiblePosition() == 0) {
					return true;
				} else {
					return false;
				}
			} else if (childView instanceof ScrollView) {
				if (((ScrollView) childView).getScrollY() == 0) {
					return true;
				} else {
					return false;
				}
			}

		}
		return false;
	}

	/**
	 * 刷新下拉头中上次更新时间的文字描述。
	 */
	private void refreshUpdatedAtValue() {
		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = getResources().getString(R.string.not_updated_yet);
		} else if (timePassed < 0) {
			updateAtValue = getResources().getString(R.string.time_error);
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = getResources().getString(R.string.updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "小时";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "天";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "个月";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "年";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		}
		timeTextView.setText(updateAtValue);
	}

	/**
	 * 刷新监听接口
	 * 
	 * @author Nono
	 * 
	 */
	public interface RefreshListener {
		public void onRefresh(RefreshableScrollView view);
	}

}
