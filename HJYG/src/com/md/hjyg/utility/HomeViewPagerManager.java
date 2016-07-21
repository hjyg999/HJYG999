package com.md.hjyg.utility;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.md.hjyg.R;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.adapter.ViewPagerAdapter;
import com.md.hjyg.entity.AdvertisementModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * ClassName: HomeViewPagerManager date: 2016-3-11 上午8:50:04 remark:
 * 首页轮播图管理
 * @author pyc
 */
public class HomeViewPagerManager {

	private ViewPager mPager;
	private boolean ismPagerMove;
	private ViewPagerAdapter mAdapter;
	private HomeFragmentActivity mActivity;
	private ArrayList<AdvertisementModel> advertiseList;
	private DataFetchService dft;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private int picture_arraysize;
	private Handler handler;
	/** 放圆点的布局 */
	private LinearLayout dotLayout;
	/** ViewPager底部圆点 */
	private ImageView[] mDots;

	/**
	 * 
	 * @param mActivity
	 * @param mPager
	 * @param advertiseList
	 * @param dft
	 * @param handler 
	 * @param dotLayout 底部圆点布局
	 */
	public HomeViewPagerManager(HomeFragmentActivity mActivity,
			ViewPager mPager, ArrayList<AdvertisementModel> advertiseList,
			DataFetchService dft, Handler handler, LinearLayout dotLayout) {
		this.mActivity = mActivity;
		this.mPager = mPager;
		this.advertiseList = advertiseList;
		this.dft = dft;
		this.handler = handler;
		this.dotLayout = dotLayout;
		picture_arraysize = advertiseList.size();
		initDots();
		initViewPager();

	}

	/**
	 * 自动翻页设置
	 */
	public void setCurrentItemMove() {
		if (!ismPagerMove) {
			int index = mPager.getCurrentItem();
			mPager.setCurrentItem(index + 1);
		}
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {

		mAdapter = new ViewPagerAdapter(mActivity, advertiseList, dft);
		mPager.setAdapter(mAdapter);
		mPager.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					ismPagerMove = true;
					if (mTimerTask != null) {
						mTimerTask.cancel();
						mTimerTask = null;
						// mTimer.cancel();
					}
					break;
				case MotionEvent.ACTION_UP:
					if (ismPagerMove) {
						initTimerTask();
						initTimer();
						ismPagerMove = false;
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				setCurrentDot(arg0 % picture_arraysize);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mPager.setCurrentItem(picture_arraysize * 50);
		mTimer = new Timer();

		// handler.sendEmptyMessageDelayed(0, 3000);
		initTimerTask();
		initTimer();
	}

	private void initTimer() {
		if (mTimerTask != null) {
			mTimer.schedule(mTimerTask, 3000, 3000);
		}
	}

	private void initTimerTask() {
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);

			}
		};
	}

	/**
	 * @author
	 * 
	 *         初始化ViewPager的底部小点
	 * 
	 * */
	private void initDots() {

		mDots = new ImageView[picture_arraysize];
		int newWidth = (int) (ScreenUtils.divideWidth(
				ScreenUtils.getScreenWidth(mActivity), 1080, 6) * 17);
		int padding = (int) (ScreenUtils.divideWidth(
				ScreenUtils.getScreenWidth(mActivity), 1080, 6) * 9);
		for (int i = 0; i < picture_arraysize; i++) {

			ImageView iv = new ImageView(mActivity);

			LayoutParams lp = new LayoutParams(newWidth, newWidth);

			lp.leftMargin = padding;

			lp.rightMargin = padding;

			lp.topMargin = padding;

			lp.bottomMargin = padding;

			iv.setLayoutParams(lp);

			iv.setImageResource(R.drawable.dot_normal);

			dotLayout.addView(iv);

			mDots[i] = iv;

		}

		mDots[0].setImageResource(R.drawable.dot_selected);
	}

	/**
	 * @author
	 * 
	 *         设置ViewPager当前的底部小点
	 * 
	 * 
	 * */
	private void setCurrentDot(int currentPosition) {

		for (int i = 0; i < mDots.length; i++) {

			if (i == currentPosition) {

				mDots[i].setImageResource(R.drawable.dot_selected);

			} else {

				mDots[i].setImageResource(R.drawable.dot_normal);

			}
		}
	}

}
