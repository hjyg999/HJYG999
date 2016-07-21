package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.GoldBaoTransactionModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: GoldBaoTransactionAdapter date: 2016-2-1 上午10:15:55 remark:
 * 黄金宝交易记录列表适配器
 * 
 * @author pyc
 */
public class GoldBaoTransactionAdapter extends BaseAdapter {

	private List<GoldBaoTransactionModel> lists;
	private LayoutInflater inflater;

	public GoldBaoTransactionAdapter(List<GoldBaoTransactionModel> lists,
			Context context) {
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public GoldBaoTransactionModel getItem(int position) {
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
			convertView = inflater.inflate(R.layout.goldbaotransactionlist_adapter, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		GoldBaoTransactionModel model = lists.get(position);
		
		if (model.type == 1) {//买入
			holder.img_left.setImageResource(R.drawable.gold_transaction5);
			holder.img_right.setVisibility(View.GONE);
			
		}else if (model.type == 2) {//卖出
			holder.img_left.setImageResource(R.drawable.gold_transaction6);
			holder.img_right.setVisibility(View.GONE);
		}else if (model.type == 3) {//提取
			holder.img_left.setImageResource(R.drawable.gold_transaction7);
			holder.img_right.setVisibility(View.VISIBLE);
		}
		
		holder.tv_typeName.setText(model.name);
		holder.tv_time.setText(model.time);
		holder.tv_account.setText(model.account);
		return convertView;
	}

	class ViewHolder {
		private TextView tv_typeName, tv_time, tv_account;
		private ImageView img_left, img_right;

		public ViewHolder(View convertView) {
			img_left = (ImageView) convertView.findViewById(R.id.img_left);
			tv_typeName = (TextView) convertView.findViewById(R.id.tv_typeName);
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_account = (TextView) convertView.findViewById(R.id.tv_account);
			img_right = (ImageView) convertView.findViewById(R.id.img_right);
		}
	}

}
