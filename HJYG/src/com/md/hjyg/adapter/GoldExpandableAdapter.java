package com.md.hjyg.adapter;

import java.util.List;
import java.util.Map;

import com.md.hjyg.R;
import com.md.hjyg.entity.ChildItem;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * ClassName: GoldExpandableAdapter date: 2016-1-20 下午2:47:29
 * remark:ExpandableListView适配器
 * 
 * @author pyc
 */
public class GoldExpandableAdapter extends BaseExpandableListAdapter {
	private List<String> mapKey;
	private Context context;
	private Map<String, List<ChildItem>> map;

	public GoldExpandableAdapter(Context context,
			Map<String, List<ChildItem>> map, List<String> mapKey) {
		this.context = context;
		this.map = map;
		this.mapKey = mapKey;
	}

	@Override
	public int getGroupCount() {
		return map.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
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
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.adapter_groupview, null);
		TextView tv_group = (TextView) view.findViewById(R.id.tv_group);
		ImageView img_group = (ImageView) view.findViewById(R.id.img_group);
		tv_group.setText(mapKey.get(groupPosition));
		if (isExpanded) {
			img_group.setImageResource(R.drawable.gold_isexpanded);
		} else {
			img_group.setImageResource(R.drawable.gold_noexpanded);
		}
		return view;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.adapter_childview, null);
		TextView tv_titel = (TextView) view.findViewById(R.id.tv_titel);
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		// 必须设置TextView的宽度，不然嵌套时，当文本换行时无法准确测量此控件的高度
		LinearLayout.LayoutParams params = (LayoutParams) tv_content
				.getLayoutParams();
		params.width = ScreenUtils.getScreenWidth(context);
		tv_content.setLayoutParams(params);

		LinearLayout.LayoutParams params2 = (LayoutParams) tv_titel
				.getLayoutParams();
		params2.width = ScreenUtils.getScreenWidth(context);
		tv_titel.setLayoutParams(params2);

		ChildItem item = map.get(mapKey.get(groupPosition)).get(childPosition);
		if (item.getItemContent() == null
				|| item.getItemContent().length() == 0) {
			tv_content.setVisibility(View.GONE);
			tv_titel.setTextColor(context.getResources().getColor(
					R.color.gray_sq));
			tv_titel.setText(item.getItemTitle());
		} else {
			tv_titel.setText(TextUtil.getColorString(context.getResources()
					.getColor(R.color.gray_gold), item.getItemTitle(), 0, 1));
			tv_content.setText(item.getItemContent());
			tv_content.setVisibility(View.VISIBLE);
		}
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
