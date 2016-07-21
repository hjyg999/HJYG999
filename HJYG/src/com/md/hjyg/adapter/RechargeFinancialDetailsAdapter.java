package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.ListModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 充值记录适配器
 */
public class RechargeFinancialDetailsAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;
	Context context;
	String[] array;
	int value;
	String Amount, Status, time, date, remark;
	public ArrayList<com.md.hjyg.entity.ListModel> List = new ArrayList<ListModel>();

	public RechargeFinancialDetailsAdapter(Context context, ArrayList<ListModel> List) {
		this.context = context;
		this.List = List;
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return List.size();
	}

	@Override
	public Object getItem(int position) {
		return List.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		View showLeaveStatus = convertView;

		TextView tv_type, tv_bonus_date, tv_income_value, tv_bonus_expenditure_value, tv_bonus_balance_value, tv_remark_note;

		if (showLeaveStatus == null) {
			holder = new ViewHolder();
			showLeaveStatus = layoutInflater.inflate(
					R.layout.financial_details_list_item, null);
			holder.tv_type = (TextView) showLeaveStatus
					.findViewById(R.id.tv_type);
			holder.tv_income = (TextView) showLeaveStatus
					.findViewById(R.id.tv_income);
			holder.tv_bonus_expenditure = (TextView) showLeaveStatus
					.findViewById(R.id.tv_bonus_expenditure);
			holder.tv_bonus_balance = (TextView) showLeaveStatus
					.findViewById(R.id.tv_bonus_balance);
			holder.tv_bonus_date = (TextView) showLeaveStatus
					.findViewById(R.id.tv_bonus_date);
			holder.tv_income_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_income_value);
			holder.tv_bonus_expenditure_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_bonus_expenditure_value);
			holder.tv_bonus_balance_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_bonus_balance_value);
			holder.tv_remark_note = (TextView) showLeaveStatus
					.findViewById(R.id.tv_remark_note);

		} else {
			holder = (ViewHolder) showLeaveStatus.getTag();
		}
		showLeaveStatus.setTag(holder);
		
		holder.tv_income.setText("金额");
		holder.tv_bonus_expenditure.setText("状态");
		holder.tv_bonus_balance.setText("时间");

		ListModel list = List.get(position);
		Amount = list.Amount.toString();
		date = list.Time.toString();
		Status = list.Status.toString();
		remark = list.Remark.toString();
		String bigDate = "";
		String smallDate = "";
		if (date.indexOf("上") != -1){
			bigDate = date.substring(0, date.indexOf("上") + 2);
			smallDate = date.substring(date.indexOf("上") + 2, date.length());
		} else if(date.indexOf("下") != -1){
			bigDate = date.substring(0, date.indexOf("下") + 2);
			smallDate = date.substring(date.indexOf("下") + 2, date.length());
		} else {
			bigDate = date.substring(0, date.indexOf(" ") + 1);
			smallDate = date.substring(date.indexOf(" ") + 1, date.length());
		}
		
		holder.tv_bonus_date.setText(bigDate);// 时间
		
		if(Amount == null || Amount.length() ==0 )
		{
			holder.tv_income_value.setText("");
		}else {
			holder.tv_income_value.setText("￥"+ Amount );// 金额
		}
		holder.tv_bonus_expenditure_value.setText(Status);// 状态
		
		holder.tv_bonus_balance_value.setText(smallDate.trim());// 时间
		holder.tv_remark_note.setText(remark);
		
		return showLeaveStatus;
	}

	public static class ViewHolder {
		TextView tv_type, tv_bonus_date, tv_income_value,tv_income,tv_bonus_expenditure,tv_bonus_balance,
				tv_bonus_expenditure_value, tv_bonus_balance_value,
				tv_remark_note;
	}

}
