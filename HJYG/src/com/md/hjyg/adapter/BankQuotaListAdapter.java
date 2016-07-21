package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.YintongPayQuotaModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * ClassName: BankQuotaListAdapter 
 * date: 2015-11-5 下午3:07:36 
 * remark: 银行限额列表适配器
 * @author pyc
 */
public class BankQuotaListAdapter extends BaseAdapter{
	
	private LayoutInflater layoutInflater;
	Context context;
	private List<YintongPayQuotaModel> lists ;
	private  int count ;
	
	public BankQuotaListAdapter(Context context,List<YintongPayQuotaModel> lists) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.lists = lists;
		count = 2 + lists.size();
	}

	
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		if (lists == null|| lists.size() == 0 || position ==0 || position == count -1) {
			return null;
		}else {
			return lists.get(position - 1);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.adapter_bankquota_listview, null);
			convertView.setTag(holder);
			
			holder.bank_remark = (LinearLayout) convertView.findViewById(R.id.bank_remark);
			holder.linear_bank = (LinearLayout) convertView.findViewById(R.id.linear_bank);
			holder.bank_name = (TextView) convertView.findViewById(R.id.bank_name);
			holder.bank_limit_single = (TextView) convertView.findViewById(R.id.bank_limit_single);
			holder.bank_limit_day = (TextView) convertView.findViewById(R.id.bank_limit_day);
			holder.bank_limit_month = (TextView) convertView.findViewById(R.id.bank_limit_month);
			holder.bank_remark_value = (TextView) convertView.findViewById(R.id.bank_remark_value);
			holder.bank_remark_phone = (TextView) convertView.findViewById(R.id.bank_remark_phone);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position == 0) {
			
			//头部显示
			holder.bank_remark.setVisibility(View.GONE);
			holder.linear_bank.setVisibility(View.VISIBLE);
			holder.bank_name.setText("银行");
			holder.bank_name.setTextColor(context.getResources().getColor(R.color.gray));
			holder.bank_limit_single.setText("单笔限额");
			holder.bank_limit_day.setText("单日限额");
			holder.bank_limit_month.setText("单月限额");
			
		}else if (position == count -1) {
			holder.bank_remark.setVisibility(View.VISIBLE);
			holder.linear_bank.setVisibility(View.GONE);
			holder.bank_remark_phone.setVisibility(View.VISIBLE); 
			holder.bank_remark_value.setText("注：商户限额、用户银行卡本身限额、认证支行标准限额，三者取" +
					"最低限额。限额表仅供参考，实际已支付界面提示为准。");
			
		}else {
			//中间的内容
			holder.bank_remark.setVisibility(View.VISIBLE);
			holder.linear_bank.setVisibility(View.VISIBLE);
			holder.bank_remark_phone.setVisibility(View.GONE); 
			holder.bank_name.setTextColor(context.getResources().getColor(R.color.gray_s));
			holder.bank_name.setText(lists.get(position-1).BankName);
			if (lists.get(position-1).SingleQuota.equals("不限")) {
				holder.bank_limit_single.setText("不限额");
				
			}else {
				holder.bank_limit_single.setText("¥" + lists.get(position-1).SingleQuota.replace("元", ""));
			}
			
			if (lists.get(position-1).DayQuota.equals("不限")) {
				holder.bank_limit_day.setText("不限额");
				
			}else {
				holder.bank_limit_day.setText("¥" + lists.get(position-1).DayQuota.replace("元", ""));
			}
			
			if (lists.get(position-1).MonthQuota.equals("不限")) {
				holder.bank_limit_month.setText("不限额");
				
			}else {
				holder.bank_limit_month.setText("¥" + lists.get(position-1).MonthQuota.replace("元", ""));
			}
			
			holder.bank_remark_value.setText("备注：" + lists.get(position-1).Remark.replace("\r\n", ""));
			
		}
		
		return convertView;
	}
	
	public class ViewHolder {

		TextView bank_name,bank_limit_single,bank_limit_day,
		         bank_limit_month,bank_remark_value,bank_remark_phone;
		LinearLayout linear_bank,bank_remark;
	}

}
