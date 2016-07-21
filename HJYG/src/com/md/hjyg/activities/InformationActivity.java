package com.md.hjyg.activities;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.InfListAdapter;
import com.md.hjyg.adapter.InfoViewPagerAdapter;
import com.md.hjyg.entity.InformationModel;
import com.md.hjyg.entity.MobileArticlesModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: InformationActivity date: 2016-5-13 上午10:45:32 remark:资讯
 * 
 * @author pyc
 */
public class InformationActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {

	private ListView mListView;
	private FrameLayout ll_view_pager;
	private TextView tv_VPtitle;
	private LinearLayout dotLayout;
	private ImageView[] mDots;
	private DialogProgressManager progressManager;
	private List<InformationModel> lists;
	private MobileArticlesModel modle1, model2;
	private InfoViewPagerAdapter vPAdapter;
	private ViewPager mPager;
	private boolean ismPagerMove;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private int size;
	private ScrollView mScrollView;
	private int index, pageIndex = 0, pageSize = 10;
	private boolean isLoadingMore, haveNoData;
	private InfListAdapter adapter;
	private LinearLayout line_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informationactivity_layout);

		findViewandInit();
		progressManager.initDialog();
		GetMobileArticlesListofNews(pageIndex, pageSize, 0);
		getInformationTechnology();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "资讯", this);
		progressManager = new DialogProgressManager(this, getResources()
				.getString(R.string.loading));
		dft.setOnDftErrorListener(this);

		ll_view_pager = (FrameLayout) findViewById(R.id.ll_view_pager);
		int vp_WHs[] = Save.getScaleBitmapWangH(720, 320);
		ViewParamsSetUtil.setViewHandW_lin(ll_view_pager, vp_WHs[1], vp_WHs[0]);
		tv_VPtitle = (TextView) findViewById(R.id.tv_VPtitle);
		dotLayout = (LinearLayout) findViewById(R.id.dotLayout);

		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setFocusable(false);

		mPager = (ViewPager) findViewById(R.id.mPager);
		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		setmScrollViewOnTouchListener();
		line_loading = (LinearLayout) findViewById(R.id.line_loading);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;

		default:
			break;
		}

	}

	/**
	 * 跳到资讯详情页
	 * 
	 * @param model
	 * @param position
	 */
	private void toInfDetails(MobileArticlesModel model, int position) {
		InformationModel bean = model.list.get(position);
		Intent intent = new Intent(InformationActivity.this,
				InformationDetailsActivity.class);
		intent.putExtra("Contents", bean.Contents);
		intent.putExtra("Title", bean.Title);
		intent.putExtra("CreateTime", bean.CreateTime);
		intent.putExtra("shareUrl", model.shareUrl + bean.Id);
		intent.putExtra("media", bean.media);

		startActivity(intent);
		overTransition(2);
	}

	/**
	 * 获取资讯
	 */
	private void GetMobileArticlesListofNews(int pageIndex, final int pageSize,
			int hasHot) {
		isLoadingMore = true;
		dft.postMobileArticlesListofNews(pageIndex, pageSize, hasHot,
				Constants.GetMobileArticlesListofNews_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						line_loading.setVisibility(View.GONE);
						isLoadingMore = false;
						progressManager.dismiss();
						modle1 = (MobileArticlesModel) dft.GetResponseObject(
								response, MobileArticlesModel.class);
						if (modle1 == null || modle1.list == null
								|| modle1.list.size() < pageSize) {
							haveNoData = true;
						} else {
							haveNoData = false;
						}
						setZXInf(modle1);

					}
				}, null);
	}

	/**
	 * 获取热点
	 */
	private void getInformationTechnology() {
		dft.getNetInfoById(Constants.InformationTechnology_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						progressManager.dismiss();
						model2 = (MobileArticlesModel) dft.GetResponseObject(
								response, MobileArticlesModel.class);
						if (model2 == null || model2.list.size() == 0) {
							ll_view_pager.setVisibility(View.GONE);
							return;
						} else {
							ll_view_pager.setVisibility(View.VISIBLE);
							size = model2.list.size();
							initDots();
							initViewPager();
						}
					}
				}, null);
	}

	/**
	 * 设置资讯信息
	 */
	private void setZXInf(MobileArticlesModel model) {
		if (pageIndex == 0) {
			if (model == null || model.list.size() == 0) {
				mListView.setVisibility(View.GONE);
				return;
			} else {
				mListView.setVisibility(View.VISIBLE);
			}
			lists = model.list;
			adapter = new InfListAdapter(lists, this, InfListAdapter.m_list);
			mListView.setAdapter(adapter);
			Constants.setListViewHeightBasedOnChildren(mListView);

			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					toInfDetails(modle1, position);
				}
			});
		} else {
			if (model.list.size() > 0) {
				lists.addAll(model.list);
				adapter.notifyDataSetChanged();
				Constants.setListViewHeightBasedOnChildren(mListView);
			}
		}
//		if (modle1 != null && model2 != null) {
//			progressManager.dismiss();
//		}
	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {

		vPAdapter = new InfoViewPagerAdapter(model2.list, this, model2.shareUrl);
		mPager.setAdapter(vPAdapter);
		if (modle1 != null && model2 != null) {
			progressManager.dismiss();
		}
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
				setCurrentDot(arg0 % size);
				tv_VPtitle.setText(model2.list.get(arg0 % size).Title);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mPager.setCurrentItem(size * 50);
		mTimer = new Timer();

		// handler.sendEmptyMessageDelayed(0, 3000);
		initTimerTask();
		initTimer();

		mPager.setOnClickListener(this);
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
				mHandler.sendEmptyMessage(0);

			}
		};
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (!ismPagerMove) {
					int index = mPager.getCurrentItem();
					mPager.setCurrentItem(index + 1);
				}
				break;

			default:
				break;
			}
		};
	};

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

	/**
	 * @author 初始化ViewPager的底部小点
	 * */
	private void initDots() {
		tv_VPtitle.setText(model2.list.get(0).Title);

		mDots = new ImageView[size];
		int newWidth = (int) (ScreenUtils.divideWidth(
				ScreenUtils.getScreenWidth(this), 1080, 6) * 17);
		int padding = (int) (ScreenUtils.divideWidth(
				ScreenUtils.getScreenWidth(this), 1080, 6) * 9);
		for (int i = 0; i < size; i++) {

			ImageView iv = new ImageView(this);

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

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	public void webLoadError() {
		progressManager.dismiss();
		isLoadingMore = false;
		line_loading.setVisibility(View.GONE);
	}

	/**
	 * 对ScrollView就行滑动监听，滑到最底部的时候加载数据
	 */
	private void setmScrollViewOnTouchListener() {
		mScrollView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					index++;
					break;
				default:
					break;
				}

				if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
					index = 0;
					if (!isLoadingMore && !haveNoData) {
						// getScrollY()表示ScrollView滑动的距离
						// getHeight()获取ScrollView在屏幕显示的高度
						// getScrollY()表示ScrollView滑动的距离
						View view = ((ScrollView) v).getChildAt(0);
						if (view.getMeasuredHeight() <= v.getScrollY()
								+ v.getHeight()) {
							pageIndex++;
							GetMobileArticlesListofNews(pageIndex, pageSize, 0);
							line_loading.setVisibility(View.VISIBLE);
						}
					}
				}
				return false;
			}
		});
	}

}
