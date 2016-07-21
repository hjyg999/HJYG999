package com.md.hjyg.fragment;

import java.math.BigDecimal;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.Interface.DataChangeItf;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.HuoQibaoDetailsActivity;
import com.md.hjyg.activities.LoginActivity;
import com.md.hjyg.activities.NewsNoticeActivity;
import com.md.hjyg.activities.RecommendedActivity;
import com.md.hjyg.activities.SafetyGuaranteeActivity;
import com.md.hjyg.entity.HomeApiModel;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.HomeViewPagerManager;
import com.md.hjyg.utility.NoviceViewPagerManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;

/**
 * ClassName: HomeFragment date: 2016-2-2 下午3:10:42 remark: 主页Fragment
 * 
 * @author pyc
 */
public class HomeFragment extends Fragment implements OnClickListener {

	private DataFetchService dft;
	private HomeFragmentActivity mActivity;
	private boolean isLoading = false;// 是否在加载数据中
	private boolean isfirstLoad = true;// 是否为第一次加载数据
	private boolean restart = false;
	private boolean isfristcreate;// 是否为第一次创建
	private HomeApiModel homeApi;
	private ViewPager mPager;
	/** 放圆点的布局 */
	private LinearLayout dotLayout;
	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;

	private int loanPayCount, latestLoanCount, dingTBCount;
	private TextView tv_latestloancount, tv_dingtoubaoloancount, tv_huoqibao,
			huoqibao_rate, home_news_mark;
	/** 活期宝每万元收益 */
	private TextView huoqibao_hit;
	/*** 项目文字提示 */
	private TextView glodbao_hit, dingtoubao_hit, latestproject_hit;
	private FrameLayout home_news_fl;
	private FrameLayout ll_view_pager;
	private DataChangeItf dataChangeItf;
	private TextView home_invite_friend;
	/** 消息公告按钮 */
	private RelativeLayout home_rel_news;
	/** 安全保障 */
	private TextView home_safety;
	/** 活期宝 */
	private LinearLayout huoqibao_ll;
	/** 黄金宝 */
	private LinearLayout glodbao_ll;
	private LinearLayout rl_latest_projects, rl_dingtoubao;
	private Intent intent;
	private OnClickListener listener;

	// private RelativeLayout rel_novic;

	private HomeViewPagerManager viewPagerManager;
	//新手专享
	private NoviceViewPagerManager noviceManager;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_home, container, false);
		findView(v);
		init();

		GetWebserviceHomeAPI();

		return v;
	}

	private void findView(View v) {
		mPager = (ViewPager) v.findViewById(R.id.view_pager);
		dotLayout = (LinearLayout) v.findViewById(R.id.dotLayout);
		tv_latestloancount = (TextView) v.findViewById(R.id.tv_latestloancount);
		tv_dingtoubaoloancount = (TextView) v
				.findViewById(R.id.tv_dingtoubaoloancount);
		tv_huoqibao = (TextView) v.findViewById(R.id.tv_huoqibao);
		huoqibao_rate = (TextView) v.findViewById(R.id.huoqibao_rate);
		huoqibao_hit = (TextView) v.findViewById(R.id.huoqibao_hit);
		glodbao_hit = (TextView) v.findViewById(R.id.glodbao_hit);
		dingtoubao_hit = (TextView) v.findViewById(R.id.dingtoubao_hit);
		latestproject_hit = (TextView) v.findViewById(R.id.latestproject_hit);
		home_news_mark = (TextView) v.findViewById(R.id.home_news_mark);
		home_news_fl = (FrameLayout) v.findViewById(R.id.home_news_fl);

		ll_view_pager = (FrameLayout) v.findViewById(R.id.ll_view_pager);

		// 下拉刷新控件
		refreshableScrollView = (RefreshableScrollView) v
				.findViewById(R.id.refreshableScrollView);

		home_invite_friend = (TextView) v.findViewById(R.id.home_invite_friend);
		home_invite_friend.setOnClickListener(this);
		home_rel_news = (RelativeLayout) v.findViewById(R.id.home_rel_news);
		home_rel_news.setOnClickListener(this);
		home_safety = (TextView) v.findViewById(R.id.home_safety);
		home_safety.setOnClickListener(this);
		glodbao_ll = (LinearLayout) v.findViewById(R.id.glodbao_ll);
		glodbao_ll.setOnClickListener(this);
		huoqibao_ll = (LinearLayout) v.findViewById(R.id.huoqibao_ll);
		huoqibao_ll.setOnClickListener(this);
		rl_latest_projects = (LinearLayout) v
				.findViewById(R.id.rl_latest_projects);
		rl_latest_projects.setOnClickListener(listener);
		rl_dingtoubao = (LinearLayout) v.findViewById(R.id.rl_dingtoubao);
		rl_dingtoubao.setOnClickListener(listener);

		// rel_novic = (RelativeLayout) v.findViewById(R.id.rel_novic);
		// rel_novic.setOnClickListener(this);

		noviceManager = new NoviceViewPagerManager(getActivity(), v);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_invite_friend:
			inviteFriend();
			break;
		case R.id.home_rel_news:
			intent = new Intent(mActivity, NewsNoticeActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.home_safety:
			intent = new Intent(mActivity, SafetyGuaranteeActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.huoqibao_ll:
			intent = new Intent(mActivity, HuoQibaoDetailsActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;

		// case R.id.glodbao_ll:
		// intent = new Intent(mActivity,GoldBaoDetailsActivity.class);
		// startActivity(intent);
		// mActivity.overTransition(2);
		// break;

		default:
			break;
		}
	}

	/***
	 * 邀请好友
	 */
	private void inviteFriend() {
		if (Constants.GetResult_AuthToken(mActivity).length() == 0) {

			Constants.showPopup(mActivity, "您需要登录后才能邀请好友",
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							AppController.isFromHomeActivity = true;
							intent = new Intent(mActivity, LoginActivity.class);
							startActivity(intent);
							mActivity.overTransition(2);

							if (Constants.dialog != null
									&& Constants.dialog.isShowing()) {
								Constants.dialog.dismiss();
							}

						}
					});
		} else {
			intent = new Intent(mActivity, RecommendedActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		mActivity = (HomeFragmentActivity) getActivity();
		dft = mActivity.getDft();
		isfristcreate = true;
		// 设置ViewPager的高度
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll_view_pager
				.getLayoutParams();
		DisplayMetrics metric = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int height = metric.heightPixels; // 屏幕高度（像素）
		params.height = (int) ((height * 0.3f));
		ll_view_pager.setLayoutParams(params);

		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {

				handler.sendEmptyMessage(2);
			}
		}, 103);

	}

	/** 获取图片的数据和标的数据 */
	private void GetWebserviceHomeAPI() {
		isLoading = true;
		// showProgressDialog();

		dft.getProductold(Constants.GetMainInfo_URL, Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {

							homeApi = (HomeApiModel) dft.GetResponseObject(
									response, HomeApiModel.class);
							if (isLoading) {
								// 刷新完成
								refreshableScrollView.finishRefresh();
								isLoading = false;
							}
							setData();
						} catch (Exception e) {
							Toast.makeText(getActivity(), "数据异常",
									Toast.LENGTH_SHORT).show();
						}
					}

				}, null, null);
	}

	private void setData() {
		// 初始化抽奖开关
		Constants.lotteryIsOpen = homeApi.IsShowRewardActivity;
		if (dataChangeItf != null) {
			dataChangeItf.NetDataSetChanged();
		}
		// if (homeApi.IsShowRewardActivity) {
		// lottery_home.setVisibility(View.VISIBLE);
		// }else {
		// lottery_home.setVisibility(View.GONE);
		// }

		loanPayCount = Integer.valueOf(homeApi.LoanPayCount.toString());
		latestLoanCount = Integer.valueOf(homeApi.LatestLoanCount.toString());
		dingTBCount = Integer.valueOf(homeApi.DingTBCount.toString());
		// tv_loanpaycount.setText(loanPayCount);
		tv_latestloancount.setText(latestLoanCount + "");
		tv_dingtoubaoloancount.setText(dingTBCount + loanPayCount + "");
		// 活期宝剩余金额
		double LoanDifference = homeApi.LoanDifference;
		if (LoanDifference >= 1) {
			BigDecimal b = new BigDecimal(LoanDifference);
			LoanDifference = b.setScale(0, BigDecimal.ROUND_HALF_UP)
					.doubleValue();
		}
		String str = Constants.StringToCurrency(LoanDifference + "");
		// tv_huoqibao.setText(str.subSequence(0, str.length() - 1) + "元");
		tv_huoqibao.setText(str.replace(".00", ""));
		// tv_huoqibao.setText(homeApi.LoanDifference);
		huoqibao_rate.setText(Constants.StringToCurrency(homeApi.Rate + ""));
		// huoqibao_hit.setText("每万元日收益"
		// + Constants.StringToCurrency(homeApi.LoanInterest + "") + "元");
		// 文字提示
		huoqibao_hit.setText(homeApi.HQBDes);
		dingtoubao_hit.setText(homeApi.DTBDes);
		glodbao_hit.setText(homeApi.HJBDes);
		latestproject_hit.setText(homeApi.XMTZDes);
		// 年会抽奖
		// if (homeApi.IsAnnualMeetingActivity &&
		// Constants.GetResult_AuthToken(this).length()>0) {
		// lottery_year.setVisibility(View.VISIBLE);
		// lottery_year.setOnClickListener(this);
		// }else {
		// lottery_year.setVisibility(View.GONE);
		// }
		// 热门公告
		if (homeApi.IsHotCount != 0) {
			home_news_mark.setText(homeApi.IsHotCount + "");
			home_news_fl.setVisibility(View.VISIBLE);
		} else {
			home_news_fl.setVisibility(View.GONE);
		}

		if (restart) {
			restart = false;
			return;
		}
		if (isfirstLoad) {// 第一次进来时才初始化图片
			viewPagerManager = new HomeViewPagerManager(mActivity, mPager,
					homeApi.advertiseList, dft, handler, dotLayout);
			isfirstLoad = false;
		}
		// 新手专享
		noviceManager.setNetDate(homeApi.NoviceLoanList);

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				viewPagerManager.setCurrentItemMove();

				break;
			case 1:
				// isover_referrer = true;
				break;
			case 2:// 滑动过程中，如果没有在加载数据，者重新刷新加载数据
				if (!isLoading) {
					GetWebserviceHomeAPI();
				}
				// if (!isNewsNoticeLoading) {
				// CallNewsNoticeWebservice();
				// }
				break;
			case 3:
				noviceManager.setNovice_img_titile();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onStart() {
		if (mActivity.getTab() == 0) {
			if (!isfristcreate) {
				restart = true;
				GetWebserviceHomeAPI();
			}
			isfristcreate = false;
		}
		super.onStart();
	}

	public void setOnDataChangeListener(DataChangeItf l) {
		this.dataChangeItf = l;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}

}
