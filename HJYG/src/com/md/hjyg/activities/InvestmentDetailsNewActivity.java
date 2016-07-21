package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.GoldExpandableAdapter;
import com.md.hjyg.entity.AssureList;
import com.md.hjyg.entity.ChildItem;
import com.md.hjyg.entity.InvestmentDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.ProjectDetailsView;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 项目详情界面
 */
public class InvestmentDetailsNewActivity extends BaseActivity implements
		View.OnClickListener {

	private Button btn_login_invest_details;
	private InvestmentDetails InvestmentDetails;

	private String est_date, loan_title, remaining_investment_amount,
			investment_amount, Intent_EncryptedID, LoanTitle, encrypted_id;
	private int term;
	private String item_amount;
	private double progress_value, rate;
	private Context mcontext;

	private TextView tv_time;

	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	/** 是否在刷新 */
	private boolean isrefreshing = false;
	/**
	 * 头部项目详情
	 */
	private ProjectDetailsView mProjectDetailsView;
	private ExpandableListView expandableListView;
	private GoldExpandableAdapter adapter;
	private List<String> mapKey;
	private Map<String, List<ChildItem>> map;

	private TextView tv_records, tv_safty;
	private LinearLayout line;
	private Intent intent;
	/**
	 * 0为登录，1为购买
	 */
	private int tab;
//	private ProjectDetails project_details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_investment_details);
		mcontext = getBaseContext();
		findViews();
		getIntentData();
		init();
//		GetWebserviceProjectDetailsAPI();
		GetWebserviceInvestmentDetailAPI();
		btn_login_invest_details.setOnClickListener(this);

	}

	public void getIntentData() {
		intent = getIntent();
		Intent_EncryptedID = intent.getStringExtra("EncrytedID");
		LoanTitle = intent.getStringExtra("LoanTitle");
		if (intent.getBooleanExtra("isJoin", false)) {
			Constants.isFinancialplan(this, dft);
		}
	}

	private void init() {
		setViewHight(btn_login_invest_details, 0.08f);
		setViewHight(line, 0.08f);
		HeaderViewControler.setHeaderView(this, LoanTitle, this);
		tv_safty.setOnClickListener(this);
		tv_records.setOnClickListener(this);

		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {
				if (!isrefreshing) {
					GetWebserviceInvestmentDetailAPI();
					isrefreshing = true;
				}
			}
		}, 102);

		mProjectDetailsView.setHaveQuestion(false);

	}

	private void findViews() {
		mProjectDetailsView = (ProjectDetailsView) findViewById(R.id.mProjectDetailsView);
		tv_time = (TextView) findViewById(R.id.tv_time);

		btn_login_invest_details = (Button) findViewById(R.id.btn_login_invest_details);
		// 下拉刷新控件
		refreshableScrollView = (RefreshableScrollView) findViewById(R.id.refreshableScrollView);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		tv_records = (TextView) findViewById(R.id.tv_records);
		tv_safty = (TextView) findViewById(R.id.tv_safty);
		line = (LinearLayout) findViewById(R.id.line);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_records:// 交易记录
			intent = new Intent(this, TransactionRecordActivity.class);
			intent.putExtra("EncrytedID", encrypted_id);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_safty:// 安全保障
			intent = new Intent(this, SafetyGuaranteeActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.btn_login_invest_details:// 购买按钮
			if (tab == 1) {
				intent = new Intent(this, InvestBuyNowActivity.class);
				intent.putExtra("EncrytedID", encrypted_id);
				intent.putExtra("LoanTitle", loan_title);
				startActivity(intent);
				overTransition(2);
			} else if (tab == 0) {
				intent = new Intent(this, LoginActivity.class);
				AppController.isFromHuoQibaoDetailsActivity = true;
				startActivity(intent);
				overTransition(2);

			}
			break;

		default:
			break;
		}

	}

	public void GetWebserviceInvestmentDetailAPI() {

		dft.getInvestmentDetails(Constants.Details_URL + Intent_EncryptedID,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						InvestmentDetails = (InvestmentDetails) dft
								.GetResponseObject(response,
										InvestmentDetails.class);
						setData();
						
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
					}
				}, null);

	}

	public void setData() {

//		setScrollViewHightForGroup();
		if (InvestmentDetails == null) {
			return;
		}
		setScrollViewHightForGroup(InvestmentDetails.AssureList);

		rate = InvestmentDetails.loanModel.LoanRate;
		if (InvestmentDetails.loanModel.ActivitiesRate > 0) {// 含有奖励的利率
			mProjectDetailsView
					.setRate(
							Constants
									.StringToCurrency((rate - InvestmentDetails.loanModel.ActivitiesRate)
											+ ""),
							"+" +InvestmentDetails.loanModel.ActivitiesRate + "%",
							"预期年化收益率");
		} else {
			mProjectDetailsView.setRate(Constants.StringToCurrency(rate + ""),
					"", "预期年化收益率");

		}
		// 期限
		term = InvestmentDetails.loanModel.LoanTerm;
		String LoanDate = "";
		if (InvestmentDetails.loanModel.LoanDateType == 0) {
			LoanDate = "个月";
		} else if (InvestmentDetails.loanModel.LoanDateType == 2) {
			LoanDate = "天";
		} else if (InvestmentDetails.loanModel.LoanDateType == 4) {
			LoanDate = "周";
		}
		mProjectDetailsView.setLeftInfo(term + LoanDate, "项目期限");
		// 起投金额
		investment_amount = InvestmentDetails.BiddingMin.toString();
		mProjectDetailsView.setCentreInfo(investment_amount.replace(".00", "")
				+ "元", "起投金额");
		// 项目总额
		item_amount = InvestmentDetails.loanModel.Amount;
		String total = "";
		if (Constants.StringToCurrency(item_amount).indexOf("0,000.00") == -1) {
			total = Constants.StringToCurrency(item_amount);
		} else {
			total = (Constants.StringToCurrency(item_amount)).replace(
					"0,000.00", "").replace(",", "")
					+ "万";
		}
		mProjectDetailsView.setRightInfo(total, "项目总额");

		remaining_investment_amount = InvestmentDetails.LoanDifference
				.toString();

		progress_value = InvestmentDetails.InvestPercentageRadixPoint;
		mProjectDetailsView.setProgressInfo(
				"剩余可投: "
						+ Constants
								.StringToCurrency(remaining_investment_amount)
						+ "元", progress_value, false);
		// 还款方式
		// repayment = InvestmentDetails.Repayment.toString();
		// tv_payment_value.setText(repayment);

		// tv_project_details_value.setText(InvestmentDetails.ReleaseDate);
		// tv_people_join.setText(InvestmentDetails.NumberOfUsers +
		// " 个用户进行了投标");
		// tv_title_loan.setText(LoanTitle);

		est_date = InvestmentDetails.EstablishmentDate.toString();// 2015-08-22
																	// 13:47:53
		// establish_date = separated[0];
		// tv_establish_date.setText(establish_date);
		tv_time.setText("开始时间 ： " + DateUtil.removeYS(est_date));
		encrypted_id = InvestmentDetails.loanModel.EncryptedId.toString();
		// Log.e(TAG, "IDDDd" + encrypted_id);
		loan_title = InvestmentDetails.loanModel.Title.toString();

		if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
			setButnUI(0, true, R.color.red, true,
					R.drawable.bg_white_selector);
		} else {
			int status = InvestmentDetails.loanModel.Status;
			switch (status) {
			case 4:// 竞标中
				if (DateUtil.isStart(est_date)) {
					setButnUI(1, true, R.color.red, true,
							R.drawable.bg_white_selector);
				} else {
					setButnUI("即将发布", false, R.color.yellow_ff9933, true,
							R.drawable.bg_white_selector);
				}
				break;
			case 7:// 还款中
				setButnUI("收益中", false, R.color.gray, true,
						R.drawable.bg_white_selector);
				break;
			case 5:// 核保审批中
				setButnUI("核保审批", false, R.color.gray, true,
						R.drawable.bg_white_selector);
				break;
			default:
				String[] Status_Text = new String[] { "审核中", "初审中", "初审通过",
						"竞标中", "核保审批", "平台终（复）审", "收益中", "审核不通过", "流标", "还款完成" };
				setButnUI(Status_Text[status - 1], false, R.color.gray, true,
						R.drawable.bg_white_selector);
				break;
			}
		}

		if (isrefreshing) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
			isrefreshing = false;
		}
	}

	/**
	 * 设置底部按钮状态
	 * 
	 * @param text
	 *            文字
	 * @param isEnabled
	 *            是否可点击
	 * @param textColorID
	 *            字体颜色
	 * @param isBgdrawable
	 *            背景是否为图片
	 * @param bgRID
	 *            背景Id
	 */
	private void setButnUI(String text, boolean isEnabled, int textColorID,
			boolean isBgdrawable, int bgRID) {
		btn_login_invest_details.setText(text);
		btn_login_invest_details.setEnabled(isEnabled);
		if (textColorID != 0) {
			btn_login_invest_details.setTextColor(getResources().getColor(
					textColorID));
		}
		if (bgRID != 0) {
			if (isBgdrawable) {
				btn_login_invest_details.setBackgroundResource((bgRID));
			} else {
				btn_login_invest_details.setBackgroundColor(getResources()
						.getColor(bgRID));
			}
		}
	}
	private void setButnUI(int tab, boolean isEnabled, int textColorID,
			boolean isBgdrawable, int bgRID) {
		this.tab = tab;
		if (tab == 0) {
			btn_login_invest_details.setText("登录");
			
		}else {
			btn_login_invest_details.setText("购买");
			
		}
		btn_login_invest_details.setEnabled(isEnabled);
		if (textColorID != 0) {
			btn_login_invest_details.setTextColor(getResources().getColor(
					textColorID));
		}
		if (bgRID != 0) {
			if (isBgdrawable) {
				btn_login_invest_details.setBackgroundResource((bgRID));
			} else {
				btn_login_invest_details.setBackgroundColor(getResources()
						.getColor(bgRID));
			}
		}
	}

	@Override
	public void onBackPressed() { // 返回键监听

		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (AppController.isFromHuoQibaoDetailsActivity) {
			Constants.isFinancialplan(this, dft);
			AppController.isFromHuoQibaoDetailsActivity = false;
		}
		GetWebserviceInvestmentDetailAPI();
	}
	
	/**
	 * 获取项目说明列表
	 */
//	private void GetWebserviceProjectDetailsAPI() {
//		dft.getProjectDetails(Constants.GetProjectDetails_URL + Intent_EncryptedID,
//				Request.Method.GET, new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject response) {
//
//						project_details = (ProjectDetails) dft
//								.GetResponseObject(response,
//										ProjectDetails.class);
//
//						setScrollViewHightForGroup(project_details.AssureList);
//					}
//
//				}, new Response.ErrorListener() {
//					@Override
//					public void onErrorResponse(VolleyError volleyError) {
//					}
//
//				}, null);
//	}

	/** 设置expandableListView,更新ScrollView的高度 */
	private void setScrollViewHightForGroup(List<AssureList> lists) {
		if (lists ==null || lists.size() ==0) {
			return;
		}
		// 取消系统左侧的图标
		expandableListView.setGroupIndicator(null);
		// 子条目展开时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});
		// 子条目收缩时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});

		mapKey = new ArrayList<String>();
		map = new HashMap<String, List<ChildItem>>();
		for (int i = 0; i < lists.size(); i++) {
			List<ChildItem> item = new ArrayList<ChildItem>();
			mapKey.add(lists.get(i).Title);
			String[] str = lists.get(i).Content.split("<br />");
			for (int j = 0; j < str.length; j++) {
				item.add(new ChildItem( "● " +str[j] , ""));
			}
			map.put(mapKey.get(i), item);
		}

		adapter = new GoldExpandableAdapter(this, map, mapKey);
		expandableListView.setAdapter(adapter);
		Constants.setListViewHeightBasedOnChildren(expandableListView);
		// setExpandableListViewHeightBasedOnChildren(expandableListView);
	}
}
