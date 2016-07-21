package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;

/**
 * ClassName: MyMarkerView date: 2015-11-16 上午10:37:39 remark:
 * 
 * @author pyc
 */
public class MyMarkerView extends MarkerView {
	// private TextView tvTime;

	private TextView tvContent;
	private Context context;
	private int left, rigt;
	private String type;

	public MyMarkerView(Context context, int layoutResource, int left, int rigt) {
		super(context, layoutResource);
		// tvTime = (TextView) findViewById(R.id.tvTime);
		tvContent = (TextView) findViewById(R.id.tvContent);
		this.context = context;
		this.left = left;
		this.rigt = rigt;
		this.type = "收益:";
	}
	
	public MyMarkerView(Context context, int layoutResource, int left, int rigt,String type) {
		this(context, layoutResource, left, rigt);
		this.type = type;
	}

	public void setTextColor(int rid) {
		if (tvContent != null) {
			tvContent.setTextColor(rid);
		}
	}

	// callbacks everytime the MarkerView is redrawn, can be used to update the
	// content (user-interface)
	@Override
	public void refreshContent(Entry e, String date, Highlight highlight) {

		if (e instanceof CandleEntry) {
			CandleEntry ce = (CandleEntry) e;
			// tvTime.setText("收益时间:" + date);
			tvContent.setText(type + Utils.formatNumber(ce.getHigh(), 0, true)
					+ "元");
		} else {
			// tvTime.setText("收益时间:" + date);
			tvContent.setText(type
					+ Constants.StringToCurrency(e.getVal() + "") + "元");
		}
	}

	@Override
	public int getXOffset(float xpos) {
		// this will center the marker-view horizontally
		int i = ScreenUtils.getScreenWidth(context)
				- ScreenUtils.dip2px(context, rigt) - left;// 65
		int w = (int) (left + (i / 6) * 5.5);
		if (xpos == left) {
			return 0;
		} else if (xpos > w) {
			return -getWidth();
		} else {
			return -(getWidth() / 2);
		}
	}

	@Override
	public int getYOffset(float ypos) {
		// this will cause the marker-view to be above the selected value
		return -getHeight() - 5;
	}
}
