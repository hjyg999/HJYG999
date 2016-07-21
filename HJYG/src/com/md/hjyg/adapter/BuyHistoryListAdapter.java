package com.md.hjyg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.SingleLoanSelfInvestModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * ClassName: BuyHistoryListAdapter 
 * date: 2015-11-14 上午9:50:46 
 * remark:
 * @author pyc
 */
public class BuyHistoryListAdapter extends BaseAdapter{
	
	Context context;

	LayoutInflater layoutInflater;


	public List<SingleLoanSelfInvestModel> lists = new ArrayList<SingleLoanSelfInvestModel>();
	
	public BuyHistoryListAdapter(Context context ,List<SingleLoanSelfInvestModel> lists)
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
			holder.img_remark = (ImageView) convertView.findViewById(R.id.img_remark);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_date.setText(lists.get(position).CreateTime);
//		holder.tv_date.setTextSize(12);
		if (lists.get(position).Type ==0) {
			holder.tv_amount.setText(lists.get(position).Amount);
		}else {
			holder.tv_amount.setText(lists.get(position).Amount +"(体验金)");
		}
		holder.tv_remark.setVisibility(View.GONE);
		holder.img_remark.setVisibility(View.VISIBLE);
		//PC = 0,Mobile = 1,IOSAPP = 2, AndroidAPP = 3,复投 = 4
//		String type = lists.get(position).Remarks;
//		if (type.equals("PC")) {
//			holder.img_remark.setImageResource(R.drawable.pc);
//		}else if (type.equals("Mobile")) {
//			holder.img_remark.setImageResource(R.drawable.weixin_buy);
//		}else if (type.equals("IOSAPP")) {
//			holder.img_remark.setImageResource(R.drawable.ios);
//		}else if (type.equals("AndroidAPP")) {
//			holder.img_remark.setImageResource(R.drawable.android);
//		}else if (type.equals("复投")) {
//			holder.img_remark.setImageResource(R.drawable.repetition);
//		}else {
//			holder.img_remark.setImageResource(R.drawable.pc);
//		}
		
		switch (lists.get(position).ClientType) {
		case 0:
			holder.img_remark.setImageResource(R.drawable.pc);
			break;
		case 1:
			holder.img_remark.setImageResource(R.drawable.weixin_buy);
			break;
		case 2:
			holder.img_remark.setImageResource(R.drawable.ios);
			break;
		case 3:
			holder.img_remark.setImageResource(R.drawable.android);
			break;
		case 4:
			holder.img_remark.setImageResource(R.drawable.repetition);
			break;

		default:
			break;
		}

		return convertView;
	}

	 class ViewHolder {
		TextView tv_date, tv_amount, tv_remark;
		ImageView img_remark;
	}

}
