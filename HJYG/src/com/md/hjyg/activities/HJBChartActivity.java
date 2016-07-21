package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.MyMarkerView;
import com.md.hjyg.utils.ScreenUtils;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
 * ClassName: HJBChartActivity 
 * date: 2016-1-20 下午3:01:47
 * remark:
 * @author rw
 */
public class HJBChartActivity extends FragmentActivity {

	private LineChart mLineChart;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        findViews();
        setLineChart();
    }
	
	// 控件初始化
	public void findViews(){
		
	}
	
	// 初始化曲线图
	private void setLineChart(){
		mLineChart = (LineChart) findViewById(R.id.spread_line_chart);
		int left = ScreenUtils.dip2px(this, 25);// 40
		int rigt = 30;//30
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
		mLineChart.setScaleYEnabled(false); // 设置点击放大以后，是否能往Y轴方向拖动
		mLineChart.setScaleXEnabled(true);  // 设置点击放大以后，是否能往X轴方向拖动
		// if disabled, scaling can be done on x- and y-axis separately
		mLineChart.setPinchZoom(false);
		mLineChart.setDrawGridBackground(false);// 是否显示表格颜色
		// 设置X轴的默认偏移量
		PointF trans = getTrans(0.0f, 0.0f);
		mLineChart.zoom(mLineChart.isScaleXEnabled() ? 1.4f : 1f, mLineChart.isScaleYEnabled() ? 1.4f : 1f, trans.x, trans.y);

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
		String[] dateVa = {"1.8","5.8","6.8","3.5","9.0","5.6","6.8","4.1","1.0","5.8"};
		// 日期
		String[] labesVa = {"01/12","01/13","01/14","01/15","01/16","01/17","01/18","01/19","01/20","01/21"};
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
