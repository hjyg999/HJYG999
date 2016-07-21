package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.AssureList;
import com.md.hjyg.entity.HuoQibaoDetailsModel;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: HuoQIbaoDetailsActivity date: 2015-11-9 下午5:10:33
 * remark:活期宝项目详情界面-主页面
 * 
 * @author pyc
 */
public class HuoQibaoDetailsActivity extends BaseActivity implements
		OnClickListener {

	// TextView
	TextView tv_number, tv_millionperday, tv_earningsdate, tv_investmentamount,
			tv_remainingamount, tv_investment, tv_line, tv_incomeway, text1,
			label;

	// Relative Layout
	RelativeLayout rl_financial_records, rl_projectdetails, rl_common_problem;

	/** 返回键和圆的布局 */
	LinearLayout lay_back_investment, circle;

	// LayoutEntities
	HeaderControler header;

	// Button
	Button btn_register_demand;

	// Entities
	public HuoQibaoDetailsModel huoQibaoDetailsModel;

	// ProgressDialog
	private ProgressDialog pDialog;

	// URL
//	public String MethodName = "SingleLoanApi/Details";

	Context mcontext;

	String Demand_proj_details, LeaveAmount, TotalAmt, singleloanId, memberId;
	/** 购买限额 */
	double PurchaseLimit;
	/** 收益起始日 */
	String PaymentDate;
	/**
	 * Rate:利率 IncomeCalculation：万元收益
	 * */
	double Rate, IncomeCalculation;
	/** 起投金额 */
	double StartInvestAmount;
	/** 活期宝总额 */
	double Total;
	/** 项目剩余金额 */
	double remainingamount;
	/** 活期宝ID */
	int huoqibaoID;
	boolean istoBuy;
	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	/** 是否在刷新 */
	boolean isrefreshing = false;
	/*** 用户可用余额中包含的体验金额度 */
	private double LeaveExperienceAmount;
	/*** 活期宝项目详情 */
	private ArrayList<AssureList> HQBDetailsList;

	/*** 活期宝常见问题 */
	private ArrayList<AssureList> HQBQuestionList;

	// Handler mHandler = new Handler(){
	// public void handleMessage(android.os.Message msg) {
	// if (msg.what == 0) {
	// if (!isrefreshing) {
	// CallgetHuoQibaoDetailsWebservice();
	// isrefreshing = true;
	// }
	// }
	// else if (msg.what == 2) {
	// if (isrefreshing) {
	// //刷新完成
	// refreshableScrollView.finishRefresh();
	// isrefreshing = false;
	// Toast.makeText(HuoQibaoDetailsActivity.this, "刷新完成",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// };
	// };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_huoqibaodetails_activity);
		mcontext = getBaseContext();
		findViews();
		init();
		// setUIData();
		singleloanId = Constants.singleloanId;

		// System.out.println("number:" + Constants.memberId);
		CallgetHuoQibaoDetailsWebservice();

	}

	private void init() {
		header = new HeaderControler(this, true, false, "项目详情",
				Constants.CheckAuthtoken(getBaseContext()));
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);
		huoQibaoDetailsModel = new HuoQibaoDetailsModel();
		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {
				if (!isrefreshing) {
					CallgetHuoQibaoDetailsWebservice();
					isrefreshing = true;
				}

				// mHandler.sendEmptyMessage(0);
			}
		}, 101);

	}

	private void findViews() {

		// TextView
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_line = (TextView) findViewById(R.id.tv_line);
		tv_millionperday = (TextView) findViewById(R.id.tv_millionperday);
		tv_earningsdate = (TextView) findViewById(R.id.tv_earningsdate);
		tv_investmentamount = (TextView) findViewById(R.id.tv_investmentamount);
		tv_remainingamount = (TextView) findViewById(R.id.tv_remainingamount);
		tv_investment = (TextView) findViewById(R.id.tv_investment);
		tv_incomeway = (TextView) findViewById(R.id.tv_incomeway);
		text1 = (TextView) findViewById(R.id.text1);
		label = (TextView) findViewById(R.id.label);

		// Relative Layout
		rl_financial_records = (RelativeLayout) findViewById(R.id.rl_financial_records);
		rl_projectdetails = (RelativeLayout) findViewById(R.id.rl_projectdetails);
		rl_common_problem = (RelativeLayout) findViewById(R.id.rl_common_problem);

		// Button
		btn_register_demand = (Button) findViewById(R.id.btn_register_demand);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		// 下拉刷新控件
		refreshableScrollView = (RefreshableScrollView) findViewById(R.id.refreshableScrollView);

		rl_projectdetails.setOnClickListener(this);
		rl_common_problem.setOnClickListener(this);
		rl_financial_records.setOnClickListener(this);
		btn_register_demand.setOnClickListener(this);
		lay_back_investment.setOnClickListener(this);

		// circle = (LinearLayout) findViewById(R.id.circle);
		// LinearLayout.LayoutParams params = (LayoutParams)
		// circle.getLayoutParams();
		// // DisplayMetrics metric = new DisplayMetrics();
		// // getWindowManager().getDefaultDisplay().getMetrics(metric);
		// // int height = metric.heightPixels; // 屏幕高度（像素）
		// // int width= metric.widthPixels;
		// // float density = metric.density;
		// //// params.height = (int) (height*density*0.1);
		// //// params.width = (int) (height*density*0.1);
		// params.height = ScreenUtils.px2dip(this, 120);
		// params.width = ScreenUtils.px2dip(this, 120);
		//
		// circle.setLayoutParams(params);
		// tv_number.setTextSize(ScreenUtils.px2sp(this, 26));
		// tv_line.setTextSize(ScreenUtils.px2sp(this, 12));

	}

	public void CallgetHuoQibaoDetailsWebservice() {

		// if (!pDialog.isShowing()) {
		// pDialog.show();
		// }

		dft.getHuoQibaoDetails(Constants.HuoQiBaoDetails_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							huoQibaoDetailsModel = (HuoQibaoDetailsModel) dft
									.GetResponseObject(response,
											HuoQibaoDetailsModel.class);

							// if (pDialog.isShowing()) {
							// pDialog.dismiss();
							// }

							setUIData();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				});
	}

	public void hideProgressDialog() {
		if (pDialog.isShowing())
			pDialog.hide();
	}

	public void setUIData() {
		huoqibaoID = huoQibaoDetailsModel.Id;

		if (huoQibaoDetailsModel.Rate != 0) {
			Rate = huoQibaoDetailsModel.Rate;
			Constants.rate = Rate;
		}
		if (huoQibaoDetailsModel.LoanInterest != 0) {
			IncomeCalculation = huoQibaoDetailsModel.LoanInterest;
		}

		if (huoQibaoDetailsModel.StartInvenstAmt != 0) {
			StartInvestAmount = huoQibaoDetailsModel.StartInvenstAmt;
			Constants.investmentamount = StartInvestAmount;
		}

		if (huoQibaoDetailsModel.LoanDifference != 0) {
			remainingamount = huoQibaoDetailsModel.LoanDifference;
		}
		if (huoQibaoDetailsModel.PurchaseLimt != 0) {
			PurchaseLimit = huoQibaoDetailsModel.PurchaseLimt;
		}

		if (huoQibaoDetailsModel.PaymentDate != null) {
			PaymentDate = huoQibaoDetailsModel.PaymentDate.toString();
		}

		remainingamount = huoQibaoDetailsModel.LoanDifference;
		LeaveExperienceAmount = huoQibaoDetailsModel.LeaveExperienceAmount;
		initButton();

		Total = huoQibaoDetailsModel.Total;

		tv_number.setText((int) Rate + "");

		tv_millionperday.setText(Constants.StringToCurrency(IncomeCalculation
				+ "")
				+ " 元");

		tv_earningsdate.setText("T+" + PaymentDate + "日");

		tv_investmentamount.setText(Constants
				.StringToCurrency(StartInvestAmount + "") + "元");

		tv_remainingamount.setText("¥"
				+ Constants.StringToCurrency(remainingamount + ""));

		// tv_investment.setText(Constants.StringToCurrency(StartInvestAmount+"")
		// + "元 ");
		tv_investment.setText(TextUtil
				.getHtmlText(huoQibaoDetailsModel.StartInvenstAmtDes));
		tv_incomeway.setText(TextUtil
				.getHtmlText(huoQibaoDetailsModel.InterestWayDes));
		text1.setText(TextUtil.getHtmlText(huoQibaoDetailsModel.LeastAmtDes));
		label.setText(TextUtil.getHtmlText(huoQibaoDetailsModel.UpdateTimeDes));
		// text1.setText("当总金额低于"+huoQibaoDetailsModel.suggestionBuyMoney+"元时 ,不产生收益");

		HQBDetailsList = huoQibaoDetailsModel.HQBDetailsList;
		HQBQuestionList = huoQibaoDetailsModel.HQBQuestionList;
		// mHandler.sendEmptyMessage(2);
		if (isrefreshing) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
			isrefreshing = false;
			// Toast.makeText(HuoQibaoDetailsActivity.this, "刷新完成",
			// Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btn_register_demand) {
			istoBuy = true;
			if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
				AppController.isFromHuoQibaoDetailsActivity = true;
				startActivity(new Intent(this, LoginActivity.class));
			} else {
				if (remainingamount == 0 && PurchaseLimit == 0) {
					Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Intent buy = new Intent(this, HuoQibaoBuyActivity.class);
				buy.putExtra("remainingamount", remainingamount);
				buy.putExtra("PurchaseLimit", PurchaseLimit);
				buy.putExtra("huoqibaoID", huoqibaoID);
				buy.putExtra("StartInvestAmount", StartInvestAmount);
				buy.putExtra("isFromHuoQibaoDetails", true);
				startActivity(buy);
			}

			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
		} else if (v == rl_projectdetails) {// 项目详情
			if (HQBDetailsList == null) {
				Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Intent projectdetails = new Intent(this,
					HuoQibaoProjectDetailActivity.class);
			// projectdetails.putExtra("Rate", Rate);
			// projectdetails.putExtra("StartInvestAmount", StartInvestAmount);
			// projectdetails.putExtra("Total", Total);
			// projectdetails.putExtra("PurchaseLimit", PurchaseLimit);
			projectdetails.putExtra("type", 0);
			projectdetails.putParcelableArrayListExtra("HQBDetailsList",
					HQBDetailsList);

			startActivity(projectdetails);
			overTransition(2);
		} else if (v == rl_common_problem) {// 常见问题
		// Intent commanproblem = new Intent(this,
		// CommanProblemActivity.class);
		// startActivity(commanproblem);
		// overridePendingTransition(R.anim.trans_right_in,
		// R.anim.trans_lift_out);
			if (HQBQuestionList == null) {
				Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Intent commanproblem = new Intent(this,
					HuoQibaoProjectDetailActivity.class);
			commanproblem.putExtra("type", 1);
			commanproblem.putParcelableArrayListExtra("HQBQuestionList",
					HQBQuestionList);

			startActivity(commanproblem);
			overTransition(2);

		} else if (v == rl_financial_records) {// 交易记录
			if (huoqibaoID == 0) {
				Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			Intent financialrecords = new Intent(this,
					HuoqiBaoTransactionRecordActivity.class);
			financialrecords.putExtra("huoqibaoID", huoqibaoID);
			startActivity(financialrecords);
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
		} else if (v == lay_back_investment) {
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);

		}
	}

	@Override
	public void onBackPressed() {

		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		CallgetHuoQibaoDetailsWebservice();
		if (istoBuy) {
			istoBuy = false;
		}

		if (AppController.isFromHuoQibaoDetailsActivity) {
			Constants.isFinancialplan(this, dft);
			AppController.isFromHuoQibaoDetailsActivity = false;
		}
	}

	/** 初始化按钮显示的文字及状态 */
	private void initButton() {
		if (Constants.GetResult_AuthToken(mcontext).isEmpty()) {
			btn_register_demand.setText("登录");
			btn_register_demand.setEnabled(true);
			btn_register_demand
					.setBackgroundResource(R.drawable.btn_selector_red);

		} else {
			if (remainingamount > 0 || LeaveExperienceAmount > 0) {
				btn_register_demand.setText("购买");
				btn_register_demand.setEnabled(true);
				btn_register_demand
						.setBackgroundResource(R.drawable.btn_selector_red);
			} else {
				btn_register_demand.setText("已满额");
				btn_register_demand.setEnabled(false);
				btn_register_demand.setBackgroundResource(R.color.gray);
			}

		}

	}

}
