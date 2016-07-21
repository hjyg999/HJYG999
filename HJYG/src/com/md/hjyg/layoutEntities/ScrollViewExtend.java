package com.md.hjyg.layoutEntities;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * ClassName: ScrollViewExtend date: 2015-10-29 下午5:05:05 remark:
 * 重新scrollView控件，主要为了避免和其他控件产生横向滑动冲突
 * 
 * @author rw
 */
public class ScrollViewExtend extends ScrollView {

	private float xDistance;
	private float yDistance;
	private float xMove = 0f;
	private float yMove = 0f;
	private float xLast;
	private float yLast;
	private Handler handler;

	public ScrollViewExtend(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewExtend(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {//事件拦截
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0.0f;
			xMove = yMove = 0.0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);

			if (xDistance > yDistance) {
				return false;
			}
			// if (handler != null && curY - yLast >0) {//下拉刷新
			// handler.sendEmptyMessage(2);
			// }

			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {//事件响应
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_UP:
//			final float upX = ev.getX();
//			final float upY = ev.getY();
//
//			xMove += Math.abs(upX - xLast);
//			yMove += Math.abs(upY - yLast);
//
//			if (xMove > yMove) {
//				return false;
//			}
//			if (handler != null && upY - yLast > 0) {// 下拉刷新
//				handler.sendEmptyMessage(2);
//			}
//			break;
//		default:
//			break;
//		}
//		return super.onTouchEvent(ev);
//	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

}