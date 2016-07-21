package com.md.hjyg.layoutEntities;

import com.md.hjyg.Interface.Pullable;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;


/**
 * ClassName: PullableExpandableListView date: 2016-3-4 上午10:30:21 remark:
 * 可以上拉加载更多和下拉刷新的ExpandableListView
 * 
 * @author pyc
 */
public class PullableExpandableListView extends ExpandableListView implements
		Pullable {
	
	/**是否可以上拉加载更多和下拉刷新，true为可以，fase为不可以*/
	private boolean isCanPullDown = true,isCanPullUp = true;

	public PullableExpandableListView(Context context) {
		super(context);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		
		if (!isCanPullDown) {
			return false;
		}
		if (getCount() == 0) {
			// 没有item的时候也可以下拉刷新
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0) {
			// 滑到顶部了
			return true;
		} else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (!isCanPullUp) {
			return false;
		}
		if (getCount() == 0) {
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1)) {
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}

	/**
	 * 设置是否可以下拉刷新
	 * @param isCanPullDown
	 */
	public void setCanPullDown(boolean isCanPullDown) {
		this.isCanPullDown = isCanPullDown;
	}

	/**
	 * 设置是否可以上拉加载更多
	 * @param isCanPullUp
	 */
	public void setCanPullUp(boolean isCanPullUp) {
		this.isCanPullUp = isCanPullUp;
	}
	
}
