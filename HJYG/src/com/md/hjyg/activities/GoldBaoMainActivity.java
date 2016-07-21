package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.MyMarkerView;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * ClassName: GoldBaoMainActivity date: 2016-1-21 下午2:49:57 remark:我的黄金宝
 * 
 * @author pyc
 */
public class GoldBaoMainActivity extends BaseActivity implements OnClickListener{

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private TextView tv_tobuy;
	/**卖出*/
	private TextView tv_tosellout;
	/**提取*/
	private TextView tv_toextract;
	/**金豆收益明细*/
	private LinearLayout lin_income;
	/**交易记录*/
	private LinearLayout lin_transaction;
	
	private ImageView img_chartbg,img_bb;
	private Bitmap chartbgbm,bb_bm;
	private LinearLayout lin_chart_bot;
	/**停盘和能够交易的布局*/
	private LinearLayout lin_nosell,lin_censell;
	
	private LineChart mLineChart;
	/**数据的总条数*/
	private int sum = 30;
	/**单屏显示数据的条数*/
	private int count = 7;
	
	private TextView tv_Realprice,tv_30price;
	/**1为实时金价 2 为30天走势*/
	private int tab = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goldbaomain_activity);
		mActivity = this;
		findViewandInit() ;
		setTabandChart();
		setLineChart();
	}

	private void findViewandInit() {
		
		new MyAsyncTask().execute(R.drawable.gold_main_bg,R.drawable.gold_main_bb);
		// 标题栏
		header = new HeaderControler(this, true, false, "我的黄金宝",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setVisibility(View.GONE);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		
		lin_income = (LinearLayout) findViewById(R.id.lin_income);
		lin_income.setOnClickListener(this);
		
		lin_transaction = (LinearLayout) findViewById(R.id.lin_transaction);
		lin_transaction.setOnClickListener(this);
		//停盘
		lin_nosell = (LinearLayout) findViewById(R.id.lin_nosell);
		lin_censell = (LinearLayout) findViewById(R.id.lin_censell);
		
		tv_tobuy = (TextView) findViewById(R.id.tv_tobuy);
		tv_tobuy.setOnClickListener(this);
		tv_tosellout = (TextView) findViewById(R.id.tv_tosellout);
		tv_tosellout.setOnClickListener(this);
		tv_toextract = (TextView) findViewById(R.id.tv_toextract);
		tv_toextract.setOnClickListener(this);
		
		tv_Realprice = (TextView) findViewById(R.id.tv_Realprice);
		tv_Realprice.setOnClickListener(this);
		tv_30price = (TextView) findViewById(R.id.tv_30price);
		tv_30price.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.tv_tobuy://购买
			intent = new Intent(mActivity, GoldBaoBuyActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_tosellout://卖出
			intent = new Intent(mActivity, GlodBaoSelloutActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_toextract://提取黄金
			intent = new Intent(mActivity,GoldBaoExtractActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.lin_income://金豆收益明细
			intent = new Intent(mActivity, GoldBaoBeanIncomeActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.lin_transaction://交易记录
			intent = new Intent(mActivity, GoldBaoTransactionActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_Realprice://实时金价
			if (tab == 2) {
				tab = 1;
				setTabandChart();
			}
			break;
		case R.id.tv_30price://30天走势
			if (tab == 1) {
				tab = 2;
				setTabandChart();
			}
			break;

		default:
			break;
		}
	}
	/**选择走势图*/
	private void setTabandChart(){
		if (tab == 1) {//实时金价
			tv_Realprice.setTextColor(getResources().getColor(R.color.white));
			tv_Realprice.setBackgroundResource(R.drawable.gold_bg_blra2);
			tv_30price.setTextColor(getResources().getColor(R.color.orange));
			tv_30price.setBackgroundResource(R.drawable.gold_bg_brra1);
		}else {//30天走势
			tv_30price.setTextColor(getResources().getColor(R.color.white));
			tv_30price.setBackgroundResource(R.drawable.gold_bg_brra2);
			tv_Realprice.setTextColor(getResources().getColor(R.color.orange));
			tv_Realprice.setBackgroundResource(R.drawable.gold_bg_blra1);
			
		}
	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}
	
	// 初始化曲线图
		private void setLineChart(){
			mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
			int left = ScreenUtils.dip2px(this, 25);// 40
			int rigt = 30;//30
			int i = ScreenUtils.dip2px(this, 20);// 65
			mLineChart.setViewPortOffsets(left, 10, rigt, i);// 设置边距，相当于padding
//			mLineChart.setBackgroundColor(getResources().getColor(R.color.white));// 设置背景颜色

			// mLineChart.setDescription("一周收益");// 设置表格数据描述
			// // 如果没有数据的时候，会显示这个，类似listview的emtpyview
			// mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

			// enable touch gestures
			mLineChart.setTouchEnabled(true);
			mLineChart.setisDrawlable(false);

			// enable scaling and dragging
			mLineChart.setDragEnabled(true);// 是否可以拖拉
			mLineChart.setDoubleTapToZoomEnabled(false);// 点击两次放大false不放大就能滑动
			mLineChart.setScaleEnabled(false);// 点击两次放大
			mLineChart.setScaleYEnabled(false); // 设置点击放大以后，是否能往Y轴方向拖动
			mLineChart.setScaleXEnabled(true);  // 设置点击放大以后，是否能往X轴方向拖动
			// if disabled, scaling can be done on x- and y-axis separately
			mLineChart.setPinchZoom(false);
			mLineChart.setDrawGridBackground(false);// 是否显示表格颜色
			// 滑动时设置X轴的默认偏移量
			PointF trans = getTrans(0.0f, 0.0f);
			float s = sum/count * 1.4f;
			mLineChart.zoom(mLineChart.isScaleXEnabled() ? s : 1f, mLineChart.isScaleYEnabled() ? 1.4f : 1f, trans.x, trans.y);

			// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
			XAxis x = mLineChart.getXAxis();
			x.setPosition(XAxis.XAxisPosition.BOTTOM);// x显示在底部
			x.setDrawGridLines(false);
			x.setTextColor(Color.GRAY);
			x.setAxisLineColor(getResources().getColor(R.color.gray_q));
//			x.setDrawGridLines(true);
//			x.setGridColor(getResources().getColor(R.color.orange));
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
			y.setGridColor(getResources().getColor(R.color.gray_q));//横线网格线条的颜色
			y.setAxisLineColor(getResources().getColor(R.color.orange));// y轴的颜色
			y.setDrawAxisLine(false);// 是否显示y轴

			mLineChart.getAxisRight().setEnabled(false);// 隐藏右边的y轴
			// 弹出收益时间和收益值设置
			MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view,
					left, rigt,"金价：");
			mv.setTextColor(getResources().getColor(R.color.orange));
			mLineChart.setMarkerView(mv);
			
			setData(7, 10);
		}
		
		// 设置X轴的默认偏移
		public PointF getTrans(float x, float y) {

	        ViewPortHandler vph = mLineChart.getViewPortHandler();

	        float xTrans = x - vph.offsetLeft();
	        float yTrans = 0f;

	        // check if axis is inverted
	        if (mLineChart.isAnyAxisInverted()) {
	            yTrans = -(y - vph.offsetTop());
	        } else {
	            yTrans = -(mLineChart.getMeasuredHeight() - y - vph.offsetBottom());
	        }

	        return new PointF(xTrans, yTrans);
	    }
		
		// 设置曲线图的数据
		private void setData(int count, float range) {
			ArrayList<String> xVals = new ArrayList<String>();// x轴数据
			ArrayList<Entry> vals1 = new ArrayList<Entry>();// y轴取值
			// 利息值
			String[] dateVa = {"1.8","5.8","6.8","3.5","9.0","5.6","6.8","4.1","1.0","5.8",
					"1.8","5.8","6.8","3.5","9.0","5.6","6.8","4.1","1.0","5.8",
					"1.8","5.8","6.8","3.5","9.0","5.6","6.8","4.1","1.0","5.8"};
			// 日期
			String[] labesVa = {"01/12","01/13","01/14","01/15","01/16","01/17","01/18","01/19","01/20","01/21",
					"01/22","01/23","01/24","01/25","01/26","01/27","01/28","01/29","01/30","01/11",
					"02/01","02/02","02/03","02/04","02/05","02/06","02/07","02/08","02/09","02/10"};
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
			set1.setDrawCubic(false);//设置曲线或者折线，true为曲线，false为折线
			set1.setCubicIntensity(0.2f);// 设置曲线光滑度
			
			set1.setDrawFilled(false);// 允许填充下方区域
			set1.setFillColor(getResources().getColor(R.color.gray_q));
			
			set1.setLineWidth(1.5f);//线宽
			
			set1.setDrawCircles(true);//画圆点
			set1.setDrawCircleHole(true);
			set1.setCircleColorHole(getResources().getColor(R.color.orange));//园点中心的颜色
			set1.setCircleSize(1.8f);
			set1.setCircleColor(getResources().getColor(R.color.orange));//圆点的颜色
			
			set1.setHighLightColor(getResources().getColor(R.color.orange));//设置点击时直线的颜色
			set1.setDrawHorizontalHighlightIndicator(false);//是否显示点击时水平的直线
			
			set1.setColor(getResources().getColor(R.color.orange));//设置曲线的颜色
			set1.setFillAlpha(100);
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
		
		private class MyAsyncTask extends AsyncTask<Integer, Void, Void> {

			@Override
			protected Void doInBackground(Integer... params) {
				Bitmap bm = BitmapFactory.decodeResource(mActivity.getResources(),
						params[0]);
				chartbgbm = Save.ScaleBitmap(bm, mActivity);
				
				bb_bm = BitmapFactory.decodeResource(mActivity.getResources(),
						params[1]);
				bb_bm = Save.ScaleBitmap(bb_bm, mActivity, bm);
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				img_chartbg = (ImageView) findViewById(R.id.img_chartbg);
				img_bb = (ImageView) findViewById(R.id.img_bb);
				lin_chart_bot = (LinearLayout) findViewById(R.id.lin_chart_bot);
				
				if (chartbgbm != null) {
					img_chartbg.setImageBitmap(chartbgbm);
					img_bb.setImageBitmap(bb_bm);
					LinearLayout.LayoutParams params = (LayoutParams) lin_chart_bot
							.getLayoutParams();
					params.height = (int) (chartbgbm.getHeight()*0.25);
					lin_chart_bot.setLayoutParams(params);
				}
				
			}

		}

}
