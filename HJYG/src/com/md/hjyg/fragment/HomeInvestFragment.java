package com.md.hjyg.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.md.hjyg.R;
import com.md.hjyg.activities.HappySmallWritingMainActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.HuoQibaoDetailsNewActivity;
import com.md.hjyg.activities.InvestListActivity;
import com.md.hjyg.activities.InvestmentDetailsNewActivity;
import com.md.hjyg.adapter.InvestmentProjectsListAdapter;
import com.md.hjyg.entity.HomeApiModel;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.HomeViewPagerManager;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * ClassName: HomeInvestFragment date: 2016-3-18 上午9:09:27 remark:新版理财
 * 
 * @author pyc
 */
public class HomeInvestFragment extends Fragment implements OnClickListener {

	private HomeFragmentActivity mActivity;
	private HeaderView mheadView;
	private HomeViewPagerManager viewPagerManager;
	private ViewPager mPager;
	/** 放圆点的布局 */
	private LinearLayout dotLayout;
	private DataFetchService dft;
	private ListView mListView;
	private InvestmentProjectsListAdapter adapter;
	private boolean isLoading;
	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	private FrameLayout ll_view_pager;
	private TextSwitcher mTextSwitcher, mTextSwitcher_r;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private List<String> strs;
	private List<String> strs_r;
	private int i = 0;
	private boolean isfrist;
	private TextView tv_huoqibao, huoqibao_rate, tv_term_value;
	private LinearLayout huoqibao_ll;
//	private LoadingMenager loadingMenager;
	private TextView[] tv_text;
	private int[] tv_textIDs = { R.id.tv_text1, R.id.tv_text2, R.id.tv_text3 };
//	private PopupWindow popupWindow;
	private ImageView img_ec;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_homeinvest_layout,
				container, false);
		findViewandInit(v);
//		mActivity.getWebManager().GetWebserviceHomeAPI(false);
		return v;
	}

	private void findViewandInit(View v) {

//		loadingMenager = new LoadingMenager(getActivity(),
//				v.findViewById(LoadingMenager.Root_ID), this);
		mActivity = (HomeFragmentActivity) getActivity();
//		dft = mActivity.getDft();

		// 设置头部标题栏
		mheadView = (HeaderView) v.findViewById(R.id.mheadView);
		mheadView.setHomeView(this);
		ViewParamsSetUtil.setViewHight(mheadView, 0.076f, getActivity());
		// 轮播图
		ll_view_pager = (FrameLayout) v.findViewById(R.id.ll_view_pager);
		ViewParamsSetUtil.setViewHight(ll_view_pager, 0.184375f, getActivity());
		mPager = (ViewPager) v.findViewById(R.id.view_pager);
		dotLayout = (LinearLayout) v.findViewById(R.id.dotLayout);

		tv_term_value = (TextView) v.findViewById(R.id.tv_term_value);
		tv_huoqibao = (TextView) v.findViewById(R.id.tv_huoqibao);
		huoqibao_rate = (TextView) v.findViewById(R.id.huoqibao_rate);
		huoqibao_ll = (LinearLayout) v.findViewById(R.id.huoqibao_ll);
		huoqibao_ll.setOnClickListener(this);

		img_ec = (ImageView) v.findViewById(R.id.img_ec);
		img_ec.setVisibility(View.GONE);
		ViewParamsSetUtil.setViewParams(img_ec, 72, 72, false);
		img_ec.setOnClickListener(this);
		// if (DateUtil.isStart("2016-07-14 00:00:00")) {
		// img_ec.setVisibility(View.GONE);
		// }

		// 版权说明
		tv_text = new TextView[tv_textIDs.length];
		for (int i = 0; i < tv_textIDs.length; i++) {
			tv_text[i] = (TextView) v.findViewById(tv_textIDs[i]);
		}

		mTextSwitcher = (TextSwitcher) v.findViewById(R.id.mTextSwitcher);
		mTextSwitcher_r = (TextSwitcher) v.findViewById(R.id.mTextSwitcher_r);
		setTextSwitcherUI(mTextSwitcher, 0);
		setTextSwitcherUI(mTextSwitcher_r, 1);
		// ListView
		mListView = (ListView) v.findViewById(R.id.mListView);
		mListView.setFocusable(false);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					LoanModel model = adapter.getItem(position);
					Intent investmentdetails = new Intent(mActivity,
							InvestmentDetailsNewActivity.class);
					investmentdetails.putExtra("EncrytedID",
							model.EncryptedId.toString());
					investmentdetails.putExtra("LoanTitle",
							model.Title.toString());
					startActivity(investmentdetails);
					mActivity.overTransition(2);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		// 下拉刷新
		refreshableScrollView = (RefreshableScrollView) v
				.findViewById(R.id.refreshableScrollView);
		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {

				if (!isLoading) {
					isLoading = true;
					mActivity.getWebManager().GetWebserviceHomeAPI(false);
				}
			}
		}, 503);
	}

	/**
	 * 设置数据
	 * 
	 * @param homeApi
	 */
	public void setUIData(HomeApiModel homeApi) {
		if (!isfrist) {
			// 轮播图
			viewPagerManager = new HomeViewPagerManager(mActivity, mPager,
					homeApi.advertiseList, dft, handler, dotLayout);
			isfrist = true;
		}
		// 理财列表
		adapter = new InvestmentProjectsListAdapter(homeApi.LoanList,
				getActivity());
		mListView.setAdapter(adapter);
		Constants.setListViewHeightBasedOnChildren(mListView);
		// 活期宝
		// 活期宝剩余金额
		double LoanDifference = homeApi.LoanDifference;
		if (LoanDifference >= 1) {
			BigDecimal b = new BigDecimal(LoanDifference);
			LoanDifference = b.setScale(0, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		}
		String str = Constants.StringToCurrency(LoanDifference + "");
		tv_huoqibao.setText(str.replace(".00", ""));
		huoqibao_rate.setText(Constants.StringToCurrency(homeApi.Rate + ""));
		String HQBDes = homeApi.HQBDes;
		String value = HQBDes.substring(HQBDes.indexOf("益") + 1,
				HQBDes.length() - 1);
		tv_term_value.setText(value);
		// 滚动的文字信息
		strs = new ArrayList<String>();
		strs_r = new ArrayList<String>();
		List<String> ScrollInfoList = homeApi.ScrollInfoList;
		for (int i = 0; i < ScrollInfoList.size(); i++) {
			String[] s = ScrollInfoList.get(i).split(",");
			strs.add(s[0]);
			strs_r.add(s[1]);
		}
		if (strs.size() <= 1) {
			mTextSwitcher.setText(strs.get(0));
			mTextSwitcher_r.setText(strs_r.get(0));
		} else {
			startThread();
		}

		if (isLoading) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
			isLoading = false;
		}
		if (homeApi.bottomInfo != null) {
			String bottomInfo[] = homeApi.bottomInfo.split(",");
			if (bottomInfo != null) {
				for (int i = 0; i < bottomInfo.length; i++) {
					tv_text[i].setText(bottomInfo[i]);
				}
			}
		}

//		loadingMenager.dismiss();

	}

	private void setTextSwitcherUI(TextSwitcher mTextSwitcher, final int i) {
		mTextSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				TextView tv = new TextView(mActivity);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0F);
				tv.setTextColor(mActivity.getResources().getColor(R.color.red));
				if (i == 1) {
					tv.setGravity(Gravity.RIGHT);
				}
				return tv;
			}
		});

		mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(mActivity,
				R.anim.anim_in));
		mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mActivity,
				R.anim.anim_out));
	}

	/**
	 * 开启轮播文字定时器
	 */
	private void startThread() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			setTimerTask();

		} else {
			mTimerTask.cancel();
			mTimerTask = null;
			setTimerTask();
		}
		mTimer.schedule(mTimerTask, 0, 3000);

	}

	private void setTimerTask() {
		mTimerTask = new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(1);

			}
		};
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				viewPagerManager.setCurrentItemMove();

				break;
			case 1:
				mTextSwitcher.setText(strs.get(i));
				mTextSwitcher_r.setText(strs_r.get(i));
				i++;
				if (i == 2) {
					i = 0;
				}

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_top_right:
			Intent intent = new Intent(getActivity(), InvestListActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.re_top_head:
			// mActivity.showSlidingView();
			break;
		case R.id.huoqibao_ll:
			// intent = new Intent(mActivity, HuoQibaoDetailsActivity.class);
			intent = new Intent(mActivity, HuoQibaoDetailsNewActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		// case LoadingMenager.Root_imgID:
		// mActivity.getWebManager().GetWebserviceHomeAPI(false);
		// loadingMenager.reset();
		// break;
//		case R.id.close_btn:
//			if (popupWindow != null) {
//				popupWindow.dismiss();
//			}
//			break;
//		case R.id.btn0:
//		case R.id.btn1:
//			if (popupWindow != null) {
//				popupWindow.dismiss();
//			}
		case R.id.img_ec:
			intent = new Intent(mActivity, HappySmallWritingMainActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;

		default:
			break;
		}
	}

	// public HeaderView getHeaderView() {
	// return mheadView;
	// }

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		mTimer = null;
	}

	/**
	 * 欧洲杯弹窗
	 */
	@SuppressLint("SimpleDateFormat")
	public void showPop() {
//		if (DateUtil.isStart("2016-07-14 00:00:00")) {
//			return;
//		}
//		String openPopupWindowTime = Constants
//				.GetOpenPopupWindowTime(getActivity());
//		SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYY_MM_DD);
//		if (openPopupWindowTime == null || openPopupWindowTime.equals("")) {
//			// 第一次进入自动弹出窗口,并保存弹出的时间，控制一天只弹出一次
//			showPopupWindow();
//			String perviousDate = sdf.format(DateUtil.getDate());
//			Constants.SetOpenPopupWindowTime(perviousDate, getActivity());
//		} else {
//			// 如果当天已经弹出，则判断时间是否是第二天，如果是第二天再弹出一次
//			String nowTimeStr = sdf.format(DateUtil.getDate());
//			if (DateUtil.dateSubtraction(openPopupWindowTime, nowTimeStr) > 0) {
//				showPopupWindow();
//				Constants.SetOpenPopupWindowTime(nowTimeStr, getActivity());
//			}
//		}
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showPopupWindow() {
//		if (popupWindow != null && popupWindow.isShowing()) {
//			return;
//		}
//		LayoutInflater inflater = (LayoutInflater) getActivity()
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View mView = inflater.inflate(R.layout.home_pop_layout, null);
//		LinearLayout line_main = (LinearLayout) mView
//				.findViewById(R.id.line_main);
//		View close_btn = mView.findViewById(R.id.close_btn);
//		View btn0 = mView.findViewById(R.id.btn0);
//		View btn1 = mView.findViewById(R.id.btn1);
//
//		int[] wHs = Save.getScaleBitmapWangH(660, 350);
//		ViewParamsSetUtil.setViewHandW_lin(line_main, wHs[1], wHs[0]);
//		close_btn.setOnClickListener(this);
//		btn0.setOnClickListener(this);
//		btn1.setOnClickListener(this);
//		popupWindow = new PopupWindow(mView, wHs[0], wHs[1]);
//		popupWindow.setFocusable(true);
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.setOutsideTouchable(true);
//		popupWindow.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss() {
//				ScreenUtils.backgroundAlpha(getActivity(), 1f);
//			}
//		});
//		popupWindow.showAsDropDown(mheadView,
//				(AppController.screenWidth - wHs[0]) / 2,
//				(int) (ScreenUtils.getScreenHeight(getActivity()) * 0.09));
//		// popupWindow.showAtLocation(this.findViewById(R.id.rel_main_pop),
//		// Gravity.TOP, (AppController.screenWidth - wHs[0]) / 2, 100);
//		ScreenUtils.backgroundAlpha(getActivity(), 0.7f);
//
//		// popupWindow = new BirthdayPopupWindow(getActivity(), this, "111");
//		// popupWindow.showAtLocation(getActivity().findViewById(R.id.rel_main_pop),
//		// Gravity.CENTER, 0, 0);
	}

}
