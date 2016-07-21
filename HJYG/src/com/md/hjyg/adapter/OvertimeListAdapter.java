package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.OverTimeFinLogDetailListModel.OverTimeFinLogDetailModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** 
 * ClassName: OvertimeListAdapter 
 * date: 2016-4-28 下午2:23:14 
 * remark:我的投资-逾期利息收益列表
 * @author pyc
 */
public class OvertimeListAdapter extends MyBaseAdapter<OverTimeFinLogDetailModel>{

	
	public OvertimeListAdapter(List<OverTimeFinLogDetailModel> lists,
			Context context) {
		super(lists, context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_listovertime, null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		}else {
			holdView = (HoldView) convertView.getTag();
		}
		OverTimeFinLogDetailModel model = lists.get(position);
		holdView.tv_income.setText(model.Amount);
		holdView.tv_time.setText(model.CreateTime);
		return convertView;
	}

	class HoldView{
		TextView tv_time,tv_income;
		public HoldView(View v){
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_income = (TextView) v.findViewById(R.id.tv_income);
		}
	}
}
