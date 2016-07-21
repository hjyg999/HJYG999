package com.md.hjyg.adapter;

import java.util.List;
import java.util.Map;

import com.md.hjyg.R;
import com.md.hjyg.Interface.OnRefreshListener;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * ClassName: FinancialAdaper date: 2016-4-9 下午3:09:27 remark:资金记录
 * 
 * @author pyc
 */
public abstract class FinancialAdaper<T> extends BaseExpandableListAdapter {
	protected List<String> mapKey;
	protected Map<String, List<T>> map;
	protected Context context;
	protected int mbitmapH;
	protected int sbitmapH;
	protected LayoutInflater inflater;
	protected boolean loadmoreover;
	protected OnRefreshListener listener;

	public FinancialAdaper(Context context, Map<String, List<T>> map,
			List<String> mapKey) {
		this.context = context;
		this.map = map;
		this.mapKey = mapKey;
		inflater = LayoutInflater.from(context);
		Bitmap reimg = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.novice_red_bg);
		Bitmap[] bitmaps = new Bitmap[2];
		bitmaps[0] = Save
				.ScaleBitmap(BitmapFactory.decodeResource(
						context.getResources(), R.drawable.list_bg_c_m),
						context, reimg);
		mbitmapH = bitmaps[0].getHeight();
		bitmaps[1] = Save
				.ScaleBitmap(BitmapFactory.decodeResource(
						context.getResources(), R.drawable.list_bg_c_s),
						context, reimg);
		sbitmapH = bitmaps[1].getHeight();
	}

	@Override
	public int getGroupCount() {
		if (loadmoreover) {
			return mapKey.size();
		}
		return mapKey.size() + 1;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == mapKey.size()) {
			return 0;
		}
		return map.get(mapKey.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mapKey.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return map.get(mapKey.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (groupPosition == mapKey.size()) {
			View v = inflater.inflate(R.layout.load_more, null);
			if (listener != null) {
				listener.onLoadMore(null);
			}
			return v;
		}
		View v = inflater.inflate(
				R.layout.expandablelistview_accountgroup_layout, null);
		TextView tv_year = (TextView) v.findViewById(R.id.tv_year);
		TextView tv_k = (TextView) v.findViewById(R.id.tv_k);
		if (groupPosition == 0) {
			tv_k.setVisibility(View.VISIBLE);
		} else {
			tv_k.setVisibility(View.GONE);
		}
		ViewParamsSetUtil.setViewHandW_lin(tv_year, mbitmapH, mbitmapH);
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

		tv_year.setTextSize(TypedValue.COMPLEX_UNIT_SP, (mbitmapH - 4) / (4)
				* 1.5f / fontScale);
		tv_year.setText(mapKey.get(groupPosition));
		return v;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v = inflater.inflate(
				R.layout.expandablelistview_accountchild_layout, null);
		LinearLayout line_left = (LinearLayout) v.findViewById(R.id.line_left);
		LinearLayout.LayoutParams params = (LayoutParams) line_left
				.getLayoutParams();
		params.leftMargin = (mbitmapH - sbitmapH) / 2;
		line_left.setLayoutParams(params);
		setUIDate(v, groupPosition, childPosition);

		return v;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public interface LoadMoreListener {
		public void loadMore();
	}

	public void setloadmoreover(boolean loadmoreover) {
		this.loadmoreover = loadmoreover;
	}

	public void setOnRefreshListener(OnRefreshListener listener) {
		this.listener = listener;
	}

	public abstract void setUIDate(View v, int groupPosition,
			int childPosition);

}
