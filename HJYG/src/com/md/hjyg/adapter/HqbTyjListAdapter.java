package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.SingleLoanInterestModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/** 
 * ClassName: HqbTyjListAdapter 
 * date: 2016-6-1 下午2:30:28 
 * remark:活期宝体验金资金记录（收益）
 * @author pyc
 */
public class HqbTyjListAdapter extends MyBaseAdapter<SingleLoanInterestModel>{

	public HqbTyjListAdapter(List<SingleLoanInterestModel> lists,
			Context context) {
		super(lists, context);
	}
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.adapter_tyjpurchasehistory, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_date.setText(lists.get(position).CreateTime.replaceAll("-", "/"));
		holder.tv_amount.setText(lists.get(position).Amount);

		return convertView;
	}
	
	class ViewHolder {
		TextView tv_date, tv_amount;
	}

}
