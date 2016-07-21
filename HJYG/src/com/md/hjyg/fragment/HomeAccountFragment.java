package com.md.hjyg.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BankAccountDetailsActivity;
import com.md.hjyg.activities.FinancialFragmentActivity;
import com.md.hjyg.activities.GoldBeanMainActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.HuoQibaoExperiencegoldActivity;
import com.md.hjyg.activities.MemberPrerogativeActivity;
import com.md.hjyg.activities.MyHuoQibaoActivity;
import com.md.hjyg.activities.MyInvestmentFragmentActivity;
import com.md.hjyg.activities.MyRedPacketFragmentActivity;
import com.md.hjyg.activities.NewsActivity;
import com.md.hjyg.activities.RechargeActivity;
import com.md.hjyg.activities.RecommendedActivity;
import com.md.hjyg.activities.TaiPingYangCPICActivity;
import com.md.hjyg.activities.TotalAssetsAccountActivity;
import com.md.hjyg.activities.WithdrawApplicationActivity;
import com.md.hjyg.entity.AccountLoginDetails;
import com.md.hjyg.entity.WithdrawDetails;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.LoadingMenager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * ClassName: HomeAccountFragment date: 2016-3-19 下午1:11:42 remark:新版我的账户信息
 * 
 * @author pyc
 */
public class HomeAccountFragment extends Fragment implements OnClickListener {

	private HeaderView mheadView;
	private DataFetchService dft;
	private HomeFragmentActivity mActivity;
	/*** 用户信息 */
	private RelativeLayout rl_user_info;
	/** 头像 */
	private CircularImage head_sex;
	private TextView tv_greeting;
	private TextView tv_user_name;
	/** 资产总额 */
	private RelativeLayout rl_total_asset_account;
	private TextView sum;
	/** 可用余额 */
	private RelativeLayout rl_financial_details;
	private TextView tv_available_bal_amt;
	/** 提现 */
	private TextView rl_withdrawals;
	/** 充值 */
	private TextView rl_recharge;
	/** 我的投资 */
	private LinearLayout rl_my_investment;
	private TextView myinvestment;
	/** 我的活期宝 */
	private LinearLayout myhuoqibao;
	private TextView myhuoqibao_vale;
	/** 活期宝体验金 */
	private LinearLayout huoqi_experience;
	private TextView experience_vale;
	/** 理财师 */
	private LinearLayout recommend;
	private TextView recommend_hit;
	/** 会员专享 */
	private LinearLayout member_prerogative;
	private TextView member_hit;
	/** 我的金库 */
	private LinearLayout lin_goldbao;
	private TextView tv_grodhit;
	private LinearLayout linear1, linear2, linear3, linear4, linear5;
	/** 加载框 */
	private AccountLoginDetails account_details;
	private String user_name;
	private String available_bal;
	/** 头像名 */
	private String savefilename;

	private boolean isfrist;
	private Intent intent;
	// private DataChangeItf dataChangeItf;
	private boolean isLonding;

	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	private boolean isrefresh;

	private RelativeLayout top_head;
	private LoadingMenager loadingMenager;
	private PopupWindow popupWindow;
	private View v;
	private TextView tv_tpy,tv_ph;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		v = inflater.inflate(R.layout.fragment_homeaccount_layout, container,
				false);
		findView(v);
		loadingMenager = new LoadingMenager(getActivity(),
				v.findViewById(LoadingMenager.Root_ID), this);
		
		mActivity.getWebManager().GetWebserviceAccountAPI(false);
		isLonding = true;
		checkTimimg();

		return v;
	}

	@SuppressLint("SimpleDateFormat")
	private void checkTimimg() {

		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.getDate());
		SimpleDateFormat df = new SimpleDateFormat("HH");
		String time = df.format(c.getTime());

		if (Integer.valueOf(time) < 12) {
			tv_greeting.setText("早上好，");
		} else if (Integer.valueOf(time) >= 12 && Integer.valueOf(time) <= 17) {
			tv_greeting.setText("下午好，");
		} else if (Integer.valueOf(time) >= 17 && Integer.valueOf(time) <= 21) {
			tv_greeting.setText("晚上好，");
		} else {
			tv_greeting.setText("晚安，");
		}

	}

	private void findView(View v) {
		mActivity = (HomeFragmentActivity) getActivity();
		dft = mActivity.getDft();
		isfrist = true;

		// 设置头部标题栏
		mheadView = (HeaderView) v.findViewById(R.id.mheadView);
		mheadView.setAccountView(this);
		ViewParamsSetUtil.setViewHight(mheadView, 0.076f, getActivity());

		// 用户信息
		rl_user_info = (RelativeLayout) v.findViewById(R.id.rl_user_info);
		rl_user_info.setOnClickListener(this);
		ViewParamsSetUtil.setViewHight(rl_user_info, 0.1f, getActivity());
		// 头像
		head_sex = (CircularImage) v.findViewById(R.id.head_sex);
		tv_greeting = (TextView) v.findViewById(R.id.tv_greeting);
		tv_user_name = (TextView) v.findViewById(R.id.tv_user_name);
		top_head = (RelativeLayout) v.findViewById(R.id.top_head);

		linear1 = (LinearLayout) v.findViewById(R.id.linear1);
		ViewParamsSetUtil.setViewHight(linear1, 0.1f, getActivity());
		// 资产总额
		rl_total_asset_account = (RelativeLayout) v
				.findViewById(R.id.rl_total_asset_account);
		sum = (TextView) v.findViewById(R.id.sum);
		rl_total_asset_account.setOnClickListener(this);
		// 可用余额
		rl_financial_details = (RelativeLayout) v
				.findViewById(R.id.rl_financial_details);
		tv_available_bal_amt = (TextView) v
				.findViewById(R.id.tv_available_bal_amt);
		rl_financial_details.setOnClickListener(this);

		linear2 = (LinearLayout) v.findViewById(R.id.linear2);
		ViewParamsSetUtil.setViewHight(linear2, 0.13f, getActivity());
		// 提现
		rl_withdrawals = (TextView) v.findViewById(R.id.rl_withdrawals);
		rl_withdrawals.setOnClickListener(this);
		// 充值
		rl_recharge = (TextView) v.findViewById(R.id.rl_recharge);
		rl_recharge.setOnClickListener(this);

		linear3 = (LinearLayout) v.findViewById(R.id.linear3);
		ViewParamsSetUtil.setViewHight(linear3, 0.12f, getActivity());
		linear4 = (LinearLayout) v.findViewById(R.id.linear4);
		ViewParamsSetUtil.setViewHight(linear4, 0.12f, getActivity());
		linear5 = (LinearLayout) v.findViewById(R.id.linear5);
		ViewParamsSetUtil.setViewHight(linear5, 0.12f, getActivity());
		// 我的投资
		rl_my_investment = (LinearLayout) v.findViewById(R.id.rl_my_investment);
		myinvestment = (TextView) v.findViewById(R.id.myinvestment);
		rl_my_investment.setOnClickListener(this);
		// 我的活期宝
		myhuoqibao = (LinearLayout) v.findViewById(R.id.myhuoqibao);
		myhuoqibao_vale = (TextView) v.findViewById(R.id.myhuoqibao_vale);
		myhuoqibao.setOnClickListener(this);
		// 活期宝体验金
		huoqi_experience = (LinearLayout) v.findViewById(R.id.huoqi_experience);
		experience_vale = (TextView) v.findViewById(R.id.experience_vale);
		huoqi_experience.setOnClickListener(this);
		// 理财师
		recommend = (LinearLayout) v.findViewById(R.id.recommend);
		recommend.setOnClickListener(this);
		recommend_hit = (TextView) v.findViewById(R.id.recommend_hit);
		// 会员专享
		member_prerogative = (LinearLayout) v
				.findViewById(R.id.member_prerogative);
		member_prerogative.setOnClickListener(this);
		member_hit = (TextView) v.findViewById(R.id.member_hit);
		// 我的金库
		lin_goldbao = (LinearLayout) v.findViewById(R.id.lin_goldbao);
		lin_goldbao.setOnClickListener(this);
		tv_grodhit = (TextView) v.findViewById(R.id.tv_grodhit);

		// 下拉刷新
		refreshableScrollView = (RefreshableScrollView) v
				.findViewById(R.id.refreshableScrollView);
		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {

				if (!isrefresh) {
					isrefresh = true;
					isLonding = true;
					mActivity.getWebManager().GetWebserviceAccountAPI(false);
				}
			}
		}, 103);
		
		tv_ph = (TextView) v.findViewById(R.id.tv_ph);
		tv_tpy = (TextView) v.findViewById(R.id.tv_tpy);
		tv_tpy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_user_info:
			mActivity.showSlidingView();
			break;
		case R.id.img_top_left:
			intent = new Intent(mActivity, NewsActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_total_asset_account:
			intent = new Intent(mActivity, TotalAssetsAccountActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_my_investment:
			// intent = new Intent(mActivity, MyInvestmentActivity.class);
			intent = new Intent(mActivity, MyInvestmentFragmentActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_financial_details:
			intent = new Intent(mActivity, FinancialFragmentActivity.class);
			// intent = new Intent(mActivity, FinancialDetailsActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_recharge:
			intent = new Intent(mActivity, RechargeActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_withdrawals:
			GetWebserviceWithdrawApplicationAPI();
			break;
		case R.id.recommend:
			if (savefilename == null || savefilename.length() == 0) {
				Toast.makeText(mActivity, "数据正在加载，请稍候", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			intent = new Intent(mActivity, RecommendedActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.myhuoqibao:
			intent = new Intent(mActivity, MyHuoQibaoActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.huoqi_experience:
			intent = new Intent(mActivity, HuoQibaoExperiencegoldActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.member_prerogative:
			if (account_details == null) {
				Toast.makeText(mActivity, "数据正在加载，请稍候", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			intent = new Intent(mActivity, MemberPrerogativeActivity.class);
			intent.putExtra("redpacket_acount",
					account_details.redEnvelopeCount);
			intent.putExtra("MemberId", account_details.MemberId);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.tv_top_right:
			mActivity.getWebManager().LoginOutService();

			break;
		case R.id.tv_btn:
			if (popupWindow != null) {
				popupWindow.dismiss();
			}
			Constants.SetBirthpopState(true, getActivity());
			intent = new Intent(mActivity, MyRedPacketFragmentActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			
			break;
		case R.id.tv_tpy:
			intent = new Intent(mActivity, TaiPingYangCPICActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.lin_goldbao://我的金豆
			if (Constants.LeaveGoldBean== null || Constants.LeaveGoldBean.length() == 0) {
				Toast.makeText(mActivity, "数据加载中，请稍候！", Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(mActivity, GoldBeanMainActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;

		default:
			break;
		}

	}

	@SuppressLint("NewApi")
	public void setUIData(AccountLoginDetails account_details) {
		this.account_details = account_details;
		if (isrefresh) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
		}
		isrefresh = false;
		isLonding = false;
		if (account_details.RealName != null
				&& account_details.RealName.length() > 0) {
			user_name = account_details.RealName;
		} else if (account_details.RegName != null
				&& account_details.RegName.length() > 0) {
			user_name = account_details.RegName;
		} else {
			user_name = "";
		}
		available_bal = account_details.BreakdownOfFundsModel.可用金额.toString();
		Double total_amt_kyye = Constants
				.StringToDouble(account_details.BreakdownOfFundsModel.可用金额);
		// Double total_amt_1 = Constants
		// .StringToDouble(account_details.BreakdownOfFundsModel.待收金额);
		// Double total_amt_2 = Constants
		// .StringToDouble(account_details.BreakdownOfFundsModel.竞标冻结金额);
		Double total_amt_lxi = Constants
				.StringToDouble(account_details.BreakdownOfFundsModel.待收利息);
		Double total_amt_dj = Constants
				.StringToDouble(account_details.BreakdownOfFundsModel.冻结金额);
		Double total_amt_dsbj = Constants
				.StringToDouble(account_details.BreakdownOfFundsModel.待收本金);
		Double total_amt_huoqinbao = 0.00;
		if (account_details.BreakdownOfFundsModel.活期宝总额 != null
				&& !account_details.BreakdownOfFundsModel.活期宝总额.equals("")) {
			total_amt_huoqinbao = Constants
					.StringToDouble(account_details.BreakdownOfFundsModel.活期宝总额);
		}
		tv_user_name.setText(user_name);
		Constants.SetRealName(user_name, mActivity);
		// DecimalFormat df = new DecimalFormat("###,###,###.##");
		// 可用余额
		tv_available_bal_amt.setText(Constants.StringToCurrency(available_bal));
		// tv_total_amt_value.setText(String.valueOf(Crrency_symbol +
		// Constants.StringToCurrency((total_amt_1 + total_amt_2)+"")));
		// 待收利息
		// tv_total_amt_value.setText(Crrency_symbol
		// + Constants.StringToCurrency(total_amt_lxi + ""));
		// 冻结金额
		// tv_freeze_value.setText(Crrency_symbol
		// + Constants.StringToCurrency(total_amt_dj + ""));
		// 我的投资-待收本金
		myinvestment.setText(Constants.StringToCurrency(total_amt_dsbj + "")
				+ "元");
		// 总额
		sum.setText(Constants.StringToCurrency((total_amt_dsbj + total_amt_dj
				+ total_amt_lxi + total_amt_kyye + total_amt_huoqinbao)
				+ ""));
		// 我的活期宝
		myhuoqibao_vale.setText(Constants
				.StringToCurrency((total_amt_huoqinbao + "")) + "元");

		savefilename = account_details.RegName.toString() + ".jpg";
		Constants.SetSaveFilename(savefilename, mActivity);
		Constants.SetMemberId(account_details.MemberId, mActivity);

		// 体验金总额
		experience_vale.setText(TextUtil
				.getHtmlText(account_details.ExperienceAmountTotalStr));
		// if (dataChangeItf != null) {
		// dataChangeItf.NetDataSetChanged();
		// }
		// 未读信息
		if (account_details.HaveNoRead) {
			mheadView.setLeftImg(R.drawable.icon_newsyes);

		} else {
			mheadView.setLeftImg(R.drawable.icon_newsno);
		}
//		tv_grodhit.setText(TextUtil.getHtmlText(account_details.MyGoldDes));
		Constants.LeaveGoldBean = Constants.StringToCurrency(account_details.LeaveGoldBean).replace(".00", "");
		String str = "";
		if (account_details.IsSignin) {
			str = Constants.LeaveGoldBean + "个(已签到)";
			tv_grodhit.setText(TextUtil.getColorString(getResources().getColor(R.color.green_99CC33), str, str.length()-5, str.length()));
		}else {
			str = Constants.LeaveGoldBean + "个(未签到)";
			tv_grodhit.setText(TextUtil.getColorString(getResources().getColor(R.color.red), str, str.length()-5, str.length()));
			
		}
		recommend_hit.setText(TextUtil
				.getHtmlText(account_details.FinAdvisorDes));
		member_hit.setText(TextUtil.getHtmlText(account_details.VipDes));

		if (isfrist) {
			setAnimation();
			isfrist = false;
		}

		String photo = account_details.photo;
		if (photo == null || photo.equals("")) {
			Bitmap bmp = null;
			bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.headimg);
			head_sex.setImageBitmap(bmp);
			Save.saveBitmap(savefilename, bmp);
		} else {
			dft.loadPhoto(photo, new Listener<Bitmap>() {
				@SuppressLint("NewApi")
				@Override
				public void onResponse(Bitmap bmp) {
					Save.saveBitmap(savefilename, bmp);
					head_sex.setImageBitmap(bmp);
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Bitmap bmp = null;
					if (Save.isSaveBitmap(savefilename)) {
						bmp = Save.getBitmap(savefilename);
					} else {
						bmp = BitmapFactory.decodeResource(getResources(),
								R.drawable.headimg);
						Save.saveBitmap(savefilename, bmp);
					}
					head_sex.setImageBitmap(bmp);
				}
			});
		}
		
		if (account_details.phoneInfo != null) {
			String [] info = account_details.phoneInfo.split(",");
			tv_ph.setText(info[0]);
			tv_tpy.setText(info[1]);
		}
		
		AppController.AccountInfIsChange = false;
		loadingMenager.dismiss();
		if (account_details.IsBirthDay && !Constants.GetBirthpopState(getActivity())) {
			showPopupWindow();
		}

	}

	/**
	 * 判断是否绑定了银行卡，如果绑定了，则跳转到WithdrawApplicationActivity，
	 * 否则跳转到BankAccountManagementActivity
	 * 
	 * @param mcontext
	 */
	private void GetWebserviceWithdrawApplicationAPI() {

		dft.getWithdrawApplication(Constants.WithdrawLogs_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {

							WithdrawDetails withdrawDetails = (WithdrawDetails) dft
									.GetResponseObject(response,
											WithdrawDetails.class);
							if (withdrawDetails.BankCardList != null
									&& withdrawDetails.BankCardList.size() > 0) {
								boolean IsValid = withdrawDetails.BankCardList
										.get(0).IsValid;
								if (IsValid == true) {
									intent = new Intent(mActivity,
											WithdrawApplicationActivity.class);
									startActivity(intent);
									mActivity.overTransition(2);
								}
							} else {
								intent = new Intent(mActivity,
										BankAccountDetailsActivity.class);
								startActivity(intent);
								mActivity.overTransition(2);
							}
						} catch (Exception e) {
							Toast.makeText(getActivity(),
									getString(R.string.data_error),
									Toast.LENGTH_SHORT).show();
						}

					}

				}, null);
	}

	/**
	 * 退出登录
	 */
	// private void LoginOutService() {
	// mDialog.showProgressDialog();
	// dft.LogoutService(Constants.LogOut_URL, Request.Method.GET,
	// new Response.Listener<JSONObject>() {
	// @Override
	// public void onResponse(JSONObject response) {
	// Notification notification = (Notification) dft
	// .GetResponseObject(response, Notification.class);
	// mDialog.dismiss();
	// if (notification.ProcessResult == 1) {
	// Constants.ClearSharePref(getActivity());
	// Constants.Clear_Cookie(getActivity());
	// mActivity.signOut();
	// } else {
	// Toast.makeText(getActivity(), "操作失败",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// }, null);
	// }

	/** 设置动画 */
	private void setAnimation() {
		// MyAnimation.setScaleAnimation(myhuoqibao_vale);
		// MyAnimation.setScaleAnimation(myinvestment);
		// MyAnimation.setScaleAnimation(experience_vale);
		// MyAnimation.setScaleAnimation(tv_available_bal_amt);
		// MyAnimation.setScaleAnimation(sum);
		// MyAnimation.setScaleAnimation(head_sex);
		// MyAnimation.setScaleAnimation(tv_user_name);
		// // tv_grodhit,member_hit,recommend_hit
		// MyAnimation.setScaleAnimation(tv_grodhit);
		// MyAnimation.setScaleAnimation(member_hit);
		// MyAnimation.setScaleAnimation(recommend_hit);
	}

	// public void setOnDataChangeListener(DataChangeItf l) {
	// this.dataChangeItf = l;
	// }

	public AccountLoginDetails getAccountLoginDetails() {
		return account_details;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 数据改版，刷新数据
		if (AppController.AccountInfIsChange) {
			if (!isLonding) {
				mActivity.getWebManager().GetWebserviceAccountAPI(false);
			}
		}
	}

	// 隐藏头像信息
	public void setHeadGone() {
		top_head.setVisibility(View.GONE);
		tv_greeting.setVisibility(View.GONE);
		tv_user_name.setVisibility(View.GONE);
	}

	// 显示头像信息头像信息
	public void setHeadVISIBLE() {
		top_head.setVisibility(View.VISIBLE);
		tv_greeting.setVisibility(View.VISIBLE);
		tv_user_name.setVisibility(View.VISIBLE);
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	private void showPopupWindow() {
		if (popupWindow!=null && popupWindow.isShowing()) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.birthdayview_layout, null);
		LinearLayout line_main = (LinearLayout) mView
				.findViewById(R.id.line_main);
		TextView tv_text = (TextView) mView.findViewById(R.id.tv_text);
		TextView tv_btn = (TextView) mView.findViewById(R.id.tv_btn);

		int[] wHs = Save.getScaleBitmapWangH(434, 666);
		ViewParamsSetUtil.setViewHandW_lin(line_main, wHs[1], wHs[0]);
		tv_btn.setOnClickListener(this);
		tv_text.setText("尊敬的用户：" + user_name
				+ "\n今天是您的生日，信投宝为您\n准备了一份礼物，祝您生日快乐！");
		popupWindow = new PopupWindow(mView, wHs[0], wHs[1]);
		popupWindow.setFocusable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ScreenUtils.backgroundAlpha(getActivity(), 1f);
			}
		});
		popupWindow.showAsDropDown(mheadView,
				(AppController.screenWidth - wHs[0]) / 2, 0);
		ScreenUtils.backgroundAlpha(getActivity(), 0.7f);

		// popupWindow = new BirthdayPopupWindow(getActivity(), this, "111");
		// popupWindow.showAtLocation(getActivity().findViewById(R.id.rel_main_pop),
		// Gravity.CENTER, 0, 0);
	}

}
