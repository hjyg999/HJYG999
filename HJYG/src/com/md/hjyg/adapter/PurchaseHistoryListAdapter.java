package com.md.hjyg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.SingleLoanInterestModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * ClassName: PurchaseHistoryListAdapter 
 * date: 2015-11-11 上午11:42:13 
 * remark: 收益记录适配器
 * @author pyc
 */
public class PurchaseHistoryListAdapter extends BaseAdapter{
	
	Context context;

	LayoutInflater layoutInflater;

	public List<SingleLoanInterestModel> lists = new ArrayList<SingleLoanInterestModel>();
	
	public PurchaseHistoryListAdapter(Context context ,List<SingleLoanInterestModel> lists)
	{
		this.context = context;
		this.lists = lists;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.adapter_purchasehistory, null);
			holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			holder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			holder.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_date.setText(lists.get(position).CreateTime.replaceAll("-", "/"));
		holder.tv_amount.setText(lists.get(position).Amount);
		holder.tv_remark.setText(lists.get(position).Remark);

		return convertView;
	}

	 class ViewHolder {
		TextView tv_date, tv_amount, tv_remark;
	}

}
