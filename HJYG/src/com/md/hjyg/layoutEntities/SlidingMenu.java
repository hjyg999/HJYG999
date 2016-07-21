package com.md.hjyg.layoutEntities;

import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * ClassName: SlidingMenu date: 2016-3-22 下午2:25:55 remark:
 * 
 * @author rw
 */
public class SlidingMenu extends HorizontalScrollView {

	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;
	/**
	 * dp
	 */
//	private int mMenuRightPadding = 50;
	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean once;
	private int tab;
	private ScrollListener sListener;
	/**
	 * 侧滑栏是否处于显示状态显示
	 */
	private boolean isShow;

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScreenWidth = ScreenUtils.getScreenWidth(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			ViewGroup menu = (ViewGroup) wrapper.getChildAt(0);
			ViewGroup content = (ViewGroup) wrapper.getChildAt(1);
			// dp to px
//			mMenuRightPadding = (int) TypedValue.applyDimension(
//					TypedValue.COMPLEX_UNIT_DIP, mMenuRightPadding, content
//							.getResources().getDisplayMetrics());

//			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mMenuWidth = (int) (mScreenWidth*0.8);
			mHalfMenuWidth = mMenuWidth / 2;
			menu.getLayoutParams().width = mMenuWidth;
			content.getLayoutParams().width = mScreenWidth;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!isShow) {
			return false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (ev.getX() > mMenuWidth) {
				return true;
			}
			break;

		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!isShow || ( tab != 2 )) {
			return false;
		}
		int action = ev.getAction();
		switch (action) {
		
		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if (scrollX > mHalfMenuWidth/4 || ev.getX() > mMenuWidth) {
				this.smoothScrollTo(mMenuWidth, 0);
				isShow = false;
				if (sListener != null) {
					sListener.goneSliding();
				}
			} else {
				this.smoothScrollTo(0, 0);
				isShow = true;
				if (sListener != null) {
					sListener.showSliding();
				}
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}

	public void setTab(int tab) {
		this.tab = tab;
	}
	
	public void setIsShow(boolean isShow){
		this.isShow = isShow;
	}
	
	public boolean isShow() {
		return isShow;
	}

	public int getmMenuWidth() {
		return mMenuWidth;
	}

	public void setScrollListener(ScrollListener sListener) {
		this.sListener = sListener;
	}

	public interface ScrollListener {
		/**
		 * 接口，用于侧滑出现后隐藏头像
		 */
		public void showSliding();

		/**
		 * 接口，用于侧滑隐藏后出现头像
		 */
		public void goneSliding();
	}
}
