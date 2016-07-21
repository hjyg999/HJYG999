package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.MyHuoQibaoDetailsModel;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.MyMarkerView;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.LineDataProvider;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * ClassName: MyHuoQibaoActivity date: 2015-11-10 下午1:26:27 remark: 我的活期宝界面
 * 
 * @author pyc
 */
public class MyHuoQibaoActivity extends BaseActivity implements OnClickListener {

	// Linear Layout
	private LinearLayout lay_back_investment;

	// TextView ,
	private TextView tv_number, tv_millionperday, tv_earningsdate,
			tv_freezamount, rl_extract, rl_purchase, tv_investment, text1,
			mostInterest_vale, atLeastInterest_vale, tv_number_mytreasure_e,
			experiencegold, experiencegold_in, tv_sumtext, tv_incomeway;

	// Relative Layout
	private RelativeLayout rl_financial_records;

	// ProgressDialog
	private ProgressDialog pDialog;

	HeaderControler header;

	// Entities
	public MyHuoQibaoDetailsModel myHuoQibaoDetails;

	Context mcontext;
	/** 购买限额 */
	private double PurchaseLimit;
	/** 起投金额 */
	private double StartInvestAmount;
	/** 项目剩余金额 */
	private double remainingamount;
	/** 活期宝ID */
	private int huoqibaoID;
	/** 我的活期宝总额中含有的体验金额度 */
	private double TotalExperience;
	private boolean isChange = false;

	// public String MethodName =
	// "InvestFrozenPurchaseApi/getAllByMemberId?memberId=";
	// public String MethodName = "SingleLoanApi/MemberDemand";
	// public String MethodName_MemberInterest =
	// "SingleLoanApi/MemberInterestList";

	// MyChartView tu;
	/** 曲线图 */
	private LineChart mLineChart;
	private int screenHeight;
	private LinearLayout linear1, linear2, linear3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_myhuoqibao_activity);
		mcontext = getBaseContext();
		findViewsandInit();
		setLineChart();
		GetWebserviceProjectDetailsAPI();
		// GetWebserviceTransactionDetailsAPI(page);
	}

	private void findViewsandInit() {

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Loading...");
		pDialog.setCancelable(false);

		header = new HeaderControler(this, true, false, "我的活期宝",
				Constants.CheckAuthtoken(getBaseContext()));
		// 手机屏幕的高度
		screenHeight = ScreenUtils.getScreenHeight(this);

		// TextView
		tv_number = (TextView) findViewById(R.id.tv_number_mytreasure);
		tv_millionperday = (TextView) findViewById(R.id.tv_millionperday_mytreasure);
		tv_earningsdate = (TextView) findViewById(R.id.tv_earningsdate_mytreasure);
		tv_freezamount = (TextView) findViewById(R.id.tv_freezamount);
		rl_extract = (TextView) findViewById(R.id.rl_extract);
		rl_purchase = (TextView) findViewById(R.id.rl_purchase);
		tv_investment = (TextView) findViewById(R.id.tv_investment);
		text1 = (TextView) findViewById(R.id.text1);
		mostInterest_vale = (TextView) findViewById(R.id.mostInterest_vale);
		atLeastInterest_vale = (TextView) findViewById(R.id.atLeastInterest_vale);
		tv_sumtext = (TextView) findViewById(R.id.tv_sumtext);
		tv_incomeway = (TextView) findViewById(R.id.tv_incomeway);
		// tu = (MyChartView) findViewById(R.id.menulist);
		// 体验金
		experiencegold = (TextView) findViewById(R.id.experiencegold);
		experiencegold_in = (TextView) findViewById(R.id.experiencegold_in);
		tv_number_mytreasure_e = (TextView) findViewById(R.id.tv_number_mytreasure_e);
		// Relative Layout
		rl_financial_records = (RelativeLayout) findViewById(R.id.rl_financial_records);

		rl_extract.setOnClickListener(this);
		rl_purchase.setOnClickListener(this);

		rl_financial_records.setOnClickListener(this);

		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		linear1 = (LinearLayout) findViewById(R.id.linear1);
		setLayoutHeight(linear1, 0.23);// 0.23
		linear2 = (LinearLayout) findViewById(R.id.linear2);
		setLayoutHeight(linear2, 0.11);// 0.11
		linear3 = (LinearLayout) findViewById(R.id.linear3);
		setLayoutHeight(linear3, 0.33);// 0.58
	}

	/**
	 * 设置控件的高度
	 * 
	 * @param weight
	 *            占总高度的比重 0.0-01
	 */
	private void setLayoutHeight(LinearLayout view, double weight) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.height = (int) (screenHeight * weight);
		view.setLayoutParams(params);

	}

	@Override
	public void onClick(View v) {
		if (v == rl_extract) {// 提取
			isChange = true;
			Intent extract_information = new Intent(this,
					ExtractHuoQibaoActivity.class);
			startActivity(extract_information);
			overTransition(2);
		} else if (v == rl_purchase) {// 购买
			isChange = true;
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
			startActivity(buy);
			overTransition(2);
		} else if (v == rl_financial_records) {// 资金记录
			Intent financialrecords = new Intent(this,
					FinancialrecordsActivity.class);
			startActivity(financialrecords);
			overTransition(2);
		} else if (v == lay_back_investment) {// 返回

			this.finish();
			overTransition(1);
		}

	}

	/***
	 * 从后台获取界面需要的数据
	 */
	public void GetWebserviceProjectDetailsAPI() {

		dft.getHuoQibaoDetails(Constants.MemberDemand_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {
							myHuoQibaoDetails = (MyHuoQibaoDetailsModel) dft
									.GetResponseObject(response,
											MyHuoQibaoDetailsModel.class);
							setUIData();
							setData(7, 10);

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

				});
	}

	public void setUIData() {

		if (myHuoQibaoDetails == null) {
			Toast.makeText(this, getString(R.string.data_error), Toast.LENGTH_SHORT).show();
			return;
		}

		PurchaseLimit = myHuoQibaoDetails.PurchaseLimt;
		StartInvestAmount = myHuoQibaoDetails.StartInvenstAmt;
		remainingamount = myHuoQibaoDetails.LoanDifference;
		huoqibaoID = myHuoQibaoDetails.Id;

		// 昨日收益
		tv_millionperday.setText(Constants
				.StringToCurrency(myHuoQibaoDetails.YesterdayInterest + ""));
		// 历史累计收益
		tv_earningsdate.setText(Constants
				.StringToCurrency(myHuoQibaoDetails.CumulativeInterest + ""));
		// 冻结金额
		tv_freezamount.setText(Constants
				.StringToCurrency(myHuoQibaoDetails.FrozenAmount + ""));

		tv_investment
				.setText(Constants
						.StringToCurrency(myHuoQibaoDetails.StartInvenstAmt
								+ "")
						+ "元");
		text1.setText("当总金额低于" + myHuoQibaoDetails.suggestionBuyMoney
				+ "元时 ,不产生收益");
		mostInterest_vale.setText(Constants
				.StringToCurrency(myHuoQibaoDetails.MostInterest + "") + "元");
		atLeastInterest_vale
				.setText(Constants
						.StringToCurrency(myHuoQibaoDetails.AtLeastInterest
								+ "")
						+ "元");
		// 体验金
		TotalExperience = Constants
				.StringToDouble(myHuoQibaoDetails.TotalExperience);

		String Total = Constants.StringToCurrency(myHuoQibaoDetails.TotalAmount
				+ "");
		int idx = Total.indexOf(".");
		// 总金额小数部分
		tv_number_mytreasure_e.setText(Total.subSequence(idx, Total.length()));
		// 总金额总数部分
		tv_number.setText(Total.subSequence(0, idx));
		tv_sumtext.setVisibility(View.VISIBLE);

		if (TotalExperience == 0) {
			experiencegold.setVisibility(View.GONE);
			experiencegold_in.setVisibility(View.GONE);
		} else {
			experiencegold.setText("体验金"
					+ Constants.StringToCurrency(TotalExperience + ""));
			experiencegold_in.setText("体验金"
					+ Constants.StringToCurrency(TotalExperience + ""));
			experiencegold.setVisibility(View.VISIBLE);
			experiencegold_in.setVisibility(View.VISIBLE);
			experiencegold_in.setVisibility(View.INVISIBLE);
		}

		tv_investment.setText(TextUtil
				.getHtmlText(myHuoQibaoDetails.StartInvenstAmtDes));
		tv_incomeway.setText(TextUtil
				.getHtmlText(myHuoQibaoDetails.InterestWayDes));
		text1.setText(TextUtil.getHtmlText(myHuoQibaoDetails.LeastAmtDes));

	}

	@Override
	protected void onRestart() {

		super.onRestart();
		if (isChange) {
			GetWebserviceProjectDetailsAPI();
			isChange = false;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	// /** 获取用户本身的交易信息列表 */
	// public void GetWebserviceTransactionDetailsAPI(final int page) {
	//
	// dft.postSingleLoanSelfInfoList(page, MethodName_MemberInterest,
	// Request.Method.POST, new Response.Listener<JSONObject>() {
	// @Override
	// public void onResponse(JSONObject response) {
	// singleLoanInterestListModel = (SingleLoanInterestListModel) dft
	// .GetResponseObject(response,
	// SingleLoanInterestListModel.class);
	// status = singleLoanInterestListModel.notification.ProcessResult;
	// msg = singleLoanInterestListModel.notification.ProcessMessage;
	// if (status == 1) {
	// // ----------与曲线图有关的设置-开始---------
	// lists = new ArrayList<SingleLoanInterestModel>();
	// if (singleLoanInterestListModel.list != null
	// && singleLoanInterestListModel.list.size() > 0) {// 有数据的情况
	// lists = singleLoanInterestListModel.list;
	// }
	// setData(7, 10);
	//
	// } else {
	// Constants.showOkPopup(MyHuoQibaoActivity.this, msg);
	// // Constants.showOkPopup(MyHuoQibaoActivity.this, msg,
	// // new DialogInterface.OnClickListener() {
	// //
	// // @Override
	// // public void onClick(
	// // DialogInterface dialog,
	// // int which) {
	// // dialog.dismiss();
	// // }
	// // });
	// }
	// }
	//
	// });
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private void setLineChart() {
		mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
		int left = ScreenUtils.dip2px(this, 25);// 40
		int rigt = 30;// 30
		int i = ScreenUtils.dip2px(this, 35);// 65
		mLineChart.setViewPortOffsets(left, 10, rigt, i);// 设置边距，相当于padding
		mLineChart.setBackgroundColor(getResources().getColor(R.color.white));// 设置背景颜色

		// mLineChart.setDescription("一周收益");// 设置表格数据描述
		// // 如果没有数据的时候，会显示这个，类似listview的emtpyview
		// mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

		// enable touch gestures
		mLineChart.setTouchEnabled(true);

		// enable scaling and dragging
		mLineChart.setDragEnabled(true);// 是否可以拖拉
		mLineChart.setScaleEnabled(false);// 点击两次放大
		// if disabled, scaling can be done on x- and y-axis separately
		mLineChart.setPinchZoom(false);
		mLineChart.setDrawGridBackground(false);// 是否显示表格颜色

		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
		XAxis x = mLineChart.getXAxis();
		x.setPosition(XAxis.XAxisPosition.BOTTOM);// x显示在底部
		x.setDrawGridLines(false);
		x.setTextColor(Color.GRAY);
		x.setTextSize(9);
		x.setSpaceBetweenLabels(0);// 可控制x坐标的数据隐藏
		x.setLabelsToSkip(0);// 可控制x坐标的数据隐藏
		x.setAvoidFirstLastClipping(false);
		//
		YAxis y = mLineChart.getAxisLeft();
		// //y.setTypeface(tf);
		y.setLabelCount(6, true);
		y.setStartAtZero(true);// y轴是否从0开始
		y.setTextColor(Color.GRAY);
		// y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);//设置y轴数字在y轴的位置
		y.setDrawGridLines(true);// 是否显示y轴横线
		y.setGridColor(getResources().getColor(R.color.gray_q));
		y.setAxisLineColor(Color.RED);// y轴的颜色
		y.setDrawAxisLine(false);// 是否显示y轴

		mLineChart.getAxisRight().setEnabled(false);// 隐藏右边的y轴
		// 弹出收益时间和收益值设置
		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,
				left, rigt);
		mLineChart.setMarkerView(mv);
	}

	private void setData(int count, float range) {
		ArrayList<String> xVals = new ArrayList<String>();// x轴数据
		ArrayList<Entry> vals1 = new ArrayList<Entry>();// y轴取值
		// 利息值
		String[] dateVa = myHuoQibaoDetails.data.split(",");
		// 日期
		String[] labesVa = myHuoQibaoDetails.labels.split(",");
		float max = 0;// 最大收益值
		@SuppressWarnings("unused")
		List<String> timeList = new ArrayList<String>();
		for (int i = 0; i < labesVa.length; i++) {
			xVals.add(labesVa[i]);
		}

		for (int i = 0; i < dateVa.length; i++) {
			float val = Float.valueOf(dateVa[i]);
			if (val > max) {
				max = val;
			}
			vals1.add(new Entry(val, i));
		}

		YAxis y = mLineChart.getAxisLeft();
		if (max == 0) {
			max = 2f;
		} else {
			max = max * 5 / 4f + 1;
		}
		y.setAxisMaxValue(max);

		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(vals1, "一周收益");
		set1.setDrawCubic(true);
		set1.setCubicIntensity(0.2f);// 设置曲线光滑度
		set1.setDrawFilled(true);// 允许填充
		set1.setDrawCircles(true);
		set1.setLineWidth(1.8f);
		set1.setCircleSize(2f);
		set1.setCircleColor(Color.GRAY);
		set1.setHighLightColor(Color.rgb(244, 117, 117));
		set1.setColor(Color.RED);
		set1.setFillColor(getResources().getColor(R.color.gray_q));
		set1.setFillAlpha(100);
		set1.setDrawHorizontalHighlightIndicator(false);
		set1.setFillFormatter(new FillFormatter() {
			@Override
			public float getFillLinePosition(LineDataSet dataSet,
					LineDataProvider dataProvider) {
				return -10;
			}
		});

		// create a data object with the datasets
		LineData data = new LineData(xVals, set1);
		// data.setValueTypeface(tf);
		data.setValueTextSize(9f);
		data.setDrawValues(false);

		// set data
		mLineChart.setData(data);
		mLineChart.notifyDataSetChanged();
		mLineChart
				.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

					@Override
					public void onValueSelected(Entry e, int dataSetIndex,
							Highlight h) {
						// Toast.makeText(MyHuoQibaoActivity.this, e.toString()
						// + " dataSetIndex = "+ dataSetIndex+
						// " h = "+h.toString(), Toast.LENGTH_LONG).show();
						Log.d("vivi", e.toString() + ";  dataSetIndex = "
								+ dataSetIndex + ";  h = " + h.toString());
					}

					@Override
					public void onNothingSelected() {
						// Toast.makeText(MyHuoQibaoActivity.this, "bb",
						// Toast.LENGTH_SHORT).show();
					}
				});
	}

}
