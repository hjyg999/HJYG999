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
import com.md.hjyg.entity.HuoQibaoDetailsModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.ProjectDetailsView;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: HuoQIbaoDetailsNewActivity date: 2015-11-9 下午5:10:33
 * remark:活期宝项目详情界面-主页面
 * 
 * @author pyc
 */
public class HuoQibaoDetailsNewActivity extends BaseActivity implements
		OnClickListener {


	private HuoQibaoDetailsModel huoQibaoDetailsModel;

	private Context mcontext;

	/** 购买限额 */
	private double PurchaseLimit;
	/** 收益起始日 */
	private String PaymentDate;
	/**
	 * Rate:利率 IncomeCalculation：万元收益
	 * */
	private double Rate, IncomeCalculation;
	/** 起投金额 */
	private double StartInvestAmount;
	/** 活期宝总额 */
//	private double Total;
	/** 项目剩余金额 */
	private double remainingamount;
	/** 活期宝ID */
	private int huoqibaoID;
	private boolean istoBuy;
	/** 下拉刷新控件 */
	private RefreshableScrollView refreshableScrollView;
	/** 是否在刷新 */
	private boolean isrefreshing = false;
	/*** 用户可用余额中包含的体验金额度 */
	private double LeaveExperienceAmount;
	/*** 活期宝项目详情 */
	private ArrayList<AssureList> HQBDetailsList;

	/*** 活期宝常见问题 */
	private ArrayList<AssureList> HQBQuestionList;
	/**
	 * 活期宝详情
	 */
	private ProjectDetailsView mProjectDetailsView;
	private Intent intent;
	private TextView tv_InterestWayDes, tv_LeastAmtDes, tv_label;
	private Button btn_tobuy;
	private TextView tv_records, tv_safty;
	private ExpandableListView expandableListView;
	private GoldExpandableAdapter adapter;
	private List<String> mapKey;
	private Map<String, List<ChildItem>> map;
	private LinearLayout line;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_huoqibaodetailsnew_activity);
		mcontext = getBaseContext();
		findViews();
		init();
		// setUIData();

		CallgetHuoQibaoDetailsWebservice();

	}

	private void init() {
		setViewHight(btn_tobuy, 0.08f);
		setViewHight(line, 0.08f);
		HeaderViewControler.setHeaderView(this, "活期宝", this);
		mProjectDetailsView.setHaveQuestion(true);
		mProjectDetailsView.setOnMClickListener(this);
		btn_tobuy.setOnClickListener(this);
		tv_safty.setOnClickListener(this);
		tv_records.setOnClickListener(this);

		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {
				if (!isrefreshing) {
					CallgetHuoQibaoDetailsWebservice();
					isrefreshing = true;
				}

			}
		}, 101);

	}

	private void findViews() {
		// 下拉刷新控件
		refreshableScrollView = (RefreshableScrollView) findViewById(R.id.refreshableScrollView);

		mProjectDetailsView = (ProjectDetailsView) findViewById(R.id.mProjectDetailsView);
		tv_InterestWayDes = (TextView) findViewById(R.id.tv_InterestWayDes);
		tv_LeastAmtDes = (TextView) findViewById(R.id.tv_LeastAmtDes);
		tv_label = (TextView) findViewById(R.id.tv_label);
		tv_records = (TextView) findViewById(R.id.tv_records);
		tv_safty = (TextView) findViewById(R.id.tv_safty);
		btn_tobuy = (Button) findViewById(R.id.btn_tobuy);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		
		line = (LinearLayout) findViewById(R.id.line);

	}

	public void CallgetHuoQibaoDetailsWebservice() {

		dft.getHuoQibaoDetails(Constants.HuoQiBaoDetails_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						try {
							huoQibaoDetailsModel = (HuoQibaoDetailsModel) dft
									.GetResponseObject(response,
											HuoQibaoDetailsModel.class);
							setUIData();
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				});
	}

	public void setUIData() {
		huoqibaoID = huoQibaoDetailsModel.Id;
		// 利率
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

//		Total = huoQibaoDetailsModel.Total;

		mProjectDetailsView.setRate(Constants.StringToCurrency(Rate + ""), "",
				"预期年化收益率");

		mProjectDetailsView.setLeftInfo(
				Constants.StringToCurrency(IncomeCalculation + "") + " 元",
				"每万元日收益");

		mProjectDetailsView.setCentreInfo("T+" + PaymentDate + "日", "收益起始日");

		mProjectDetailsView.setRightInfo(
				(Constants.StringToCurrency(StartInvestAmount + "") + "元"),
				"起投金额");
		mProjectDetailsView.setProgressInfo(
				"剩余可投: " + Constants.StringToCurrency(remainingamount + "")
						+ "元", 0, true);

		tv_InterestWayDes.setText("● " +TextUtil
				.getHtmlText(huoQibaoDetailsModel.InterestWayDes));
		tv_LeastAmtDes.setText(TextUtil
				.getHtmlText("● " +huoQibaoDetailsModel.LeastAmtDes));
		tv_label.setText(TextUtil
				.getHtmlText(huoQibaoDetailsModel.UpdateTimeDes));

		HQBDetailsList = huoQibaoDetailsModel.HQBDetailsListForAndroid;
		HQBQuestionList = huoQibaoDetailsModel.HQBQuestionList;
		setScrollViewHightForGroup();
		if (isrefreshing) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
			isrefreshing = false;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case HeaderViewControler.ID:// 返回
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_safty:// 安全保障
			intent = new Intent(this, SafetyGuaranteeActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.img_question:// 常见问题
			if (HQBQuestionList == null) {
				Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			intent = new Intent(this, HuoQibaoProjectDetailActivity.class);
			intent.putExtra("type", 1);
			intent.putParcelableArrayListExtra("HQBQuestionList",
					HQBQuestionList);

			startActivity(intent);
			overTransition(2);
			break;
		case R.id.btn_tobuy:// 购买 或登录
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
				intent = new Intent(this, HuoQibaoBuyActivity.class);
				intent.putExtra("remainingamount", remainingamount);
				intent.putExtra("PurchaseLimit", PurchaseLimit);
				intent.putExtra("huoqibaoID", huoqibaoID);
				intent.putExtra("StartInvestAmount", StartInvestAmount);
				intent.putExtra("isFromHuoQibaoDetails", true);
				startActivity(intent);
			}

			overTransition(2);
			break;
		case R.id.tv_records:// 交易记录
			if (huoqibaoID == 0) {
				Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			intent = new Intent(this, HuoqiBaoTransactionRecordActivity.class);
			intent.putExtra("huoqibaoID", huoqibaoID);
			startActivity(intent);
			overTransition(2);
			break;

		default:
			break;
		}

//		if (v == rl_projectdetails) {// 项目详情
//			if (HQBDetailsList == null) {
//				Toast.makeText(this, "数据正在加载中，请稍候再试！", Toast.LENGTH_SHORT)
//						.show();
//				return;
//			}
//			Intent projectdetails = new Intent(this,
//					HuoQibaoProjectDetailActivity.class);
//			// projectdetails.putExtra("Rate", Rate);
//			// projectdetails.putExtra("StartInvestAmount", StartInvestAmount);
//			// projectdetails.putExtra("Total", Total);
//			// projectdetails.putExtra("PurchaseLimit", PurchaseLimit);
//			projectdetails.putExtra("type", 0);
//			projectdetails.putParcelableArrayListExtra("HQBDetailsList",
//					HQBDetailsList);
//
//			startActivity(projectdetails);
//			overTransition(2);
//		}

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
			btn_tobuy.setText("登录");
			btn_tobuy.setEnabled(true);
			btn_tobuy.setBackgroundResource(R.drawable.bg_white_selector);

		} else {
			if (remainingamount > 0 || LeaveExperienceAmount > 0) {
				btn_tobuy.setText("购买");
				btn_tobuy.setEnabled(true);
				btn_tobuy.setBackgroundResource(R.drawable.bg_white_selector);
			} else {
				btn_tobuy.setText("已满额");
				btn_tobuy.setEnabled(false);
				btn_tobuy.setBackgroundResource(R.color.gray);
			}

		}

	}
	
	/** 设置expandableListView,更新ScrollView的高度 */
	private void setScrollViewHightForGroup() {
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
		for (int i = 0; i < HQBDetailsList.size(); i++) {
			List<ChildItem> item = new ArrayList<ChildItem>();
			mapKey.add(HQBDetailsList.get(i).Title);
			String str[] = HQBDetailsList.get(i).Content.split("\n");
			for (int j = 0; j < str.length; j++) {
				item.add(new ChildItem( str[j], ""));
			}
			map.put(mapKey.get(i), item);
		}

		adapter = new GoldExpandableAdapter(this, map, mapKey);
		expandableListView.setAdapter(adapter);
		Constants.setListViewHeightBasedOnChildren(expandableListView);
		// setExpandableListViewHeightBasedOnChildren(expandableListView);
	}
}
