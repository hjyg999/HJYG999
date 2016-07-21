package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.ListModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 我的投资适配器
 */
public class MyInvestmentAdapter extends BaseAdapter {
	LayoutInflater layoutInflater;
	Context context;
	String[] array;
	public ArrayList<ListModel> List = new ArrayList<ListModel>();
	String rate, my_investment, project_duration, loan_title, receipt_date, principalinterest,repaymentDate;
	/**
	 * 0:持有中 1：投标中 2：已还款
	 */
	int type;

	public MyInvestmentAdapter(Context context, ArrayList<ListModel> List, int Type) {
		this.context = context;
		this.List = List;
		this.type = Type;
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

		if (showLeaveStatus == null) {
			holder = new ViewHolder();
			//showLeaveStatus = layoutInflater.inflate(R.layout.my_investment_list_item, null);
			showLeaveStatus = layoutInflater.inflate(R.layout.my_investment_item_new, null);
			holder.tv_receipt_date  = (TextView) showLeaveStatus.findViewById(R.id.tv_receipt_date);
			holder.tv_loan_name = (TextView) showLeaveStatus
					.findViewById(R.id.tv_loan_name);
			holder.tv_receipt_date_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_receipt_date_value);
			holder.tv_my_invetment_list_rate_count = (TextView) showLeaveStatus
					.findViewById(R.id.tv_my_invetment_list_rate_count);
			holder.tv_my_investment_list_project_duration = (TextView) showLeaveStatus
					.findViewById(R.id.tv_my_investment_list_project_duration);
			holder.tv_my_investment_list_project_duration_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_my_investment_list_project_duration_value);
			holder.tv_my_investment_list_invest_amount = (TextView) showLeaveStatus
					.findViewById(R.id.tv_my_investment_list_invest_amount);
			holder.tv_my_investment_list_invest_amount_value = (TextView) showLeaveStatus
					.findViewById(R.id.tv_my_investment_list_invest_amount_value);
			holder.tv_my_invetment_list_amount = (TextView) showLeaveStatus.findViewById(R.id.tv_my_invetment_list_amount);
			holder.tv_my_invetment_rate = (LinearLayout) showLeaveStatus.findViewById(R.id.tv_my_invetment_rate);
			

		} else {
			holder = (ViewHolder) showLeaveStatus.getTag();
		}
		showLeaveStatus.setTag(holder);

		loan_title = List.get(position).Title.toString();
		receipt_date = List.get(position).CreateTime.toString();
		rate = List.get(position).LoanRate.toString();
		project_duration = List.get(position).LoanTerm.toString();
		my_investment = List.get(position).Amount.toString();
		if (List.get(position).RepaymentDate != null){
			repaymentDate = List.get(position).RepaymentDate.toString();
		}else
		{
			repaymentDate = "";
		}
		if(List.get(position).PrincipalInterest != null)
		{
			principalinterest = List.get(position).PrincipalInterest.toString();
		}else {
			principalinterest = "";
		}
		//公共的
		holder.tv_loan_name.setText(loan_title);
		holder.tv_my_invetment_list_amount.setText(my_investment + "元");
		holder.tv_my_investment_list_project_duration_value.setText(project_duration);
		holder.tv_my_invetment_list_rate_count.setText(rate);
		holder.tv_my_investment_list_project_duration.setText("项目期限");
		if(type == 1)
		{
			//holder.tv_my_invetment_rate.setVisibility(View.GONE);
			holder.tv_my_investment_list_invest_amount.setText("状态");
			if (List.get(position).Type != null && List.get(position).Type.length() > 0) {
				holder.tv_my_investment_list_invest_amount_value.setText(List.get(position).Type.toString());
			}else {
				holder.tv_my_investment_list_invest_amount_value.setText("手动");
			}
			//List.get(position).Status返回为空无法判断
//			if(List.get(position).Status.equals("4"))
//			{
//			}else {
//				holder.tv_my_investment_list_invest_amount_value.setText("审核中");
//				
//			}
			holder.tv_receipt_date.setText("创建日期：");
			holder.tv_receipt_date_value.setText(receipt_date);
			
		}else {
			//holder.tv_my_invetment_rate.setVisibility(View.VISIBLE);
//			holder.tv_my_investment_list_project_duration.setText("项目期限");
			if(type == 0)
			{
				holder.tv_my_investment_list_invest_amount.setText("应收本息");
			}else {
				holder.tv_my_investment_list_invest_amount.setText("已收本息");
			}
			holder.tv_my_investment_list_invest_amount_value.setText(principalinterest +"元");
			holder.tv_receipt_date.setText("收款日期：");
			holder.tv_receipt_date_value.setText(repaymentDate);
			
		}
//		if (type == 0) {
//			principalinterest = List.get(position).PrincipalInterest.toString();
//			holder.tv_my_investment_list_invest_amount.setText(R.string.my_principalinterest_amount);
//			holder.tv_my_investment_list_invest_amount_value.setText(principalinterest);
//		} else {
//			holder.tv_my_investment_list_invest_amount.setText(R.string.my_investment_amount);
//			holder.tv_my_investment_list_invest_amount_value.setText(my_investment);
//		}
//		holder.tv_loan_name.setText(loan_title);
//		// 根据开始时间和项目周期，计算出项目到期时间
//		String endTime = DateUtil.dateAfter(receipt_date, project_duration);
//		if (type == 1){
//			holder.tv_receipt_date_value.setText(receipt_date);
//			holder.tv_receipt_date.setText("创建日期:");
//		} else {
//			holder.tv_receipt_date_value.setText(repaymentDate);
//		}
//		holder.tv_my_invetment_list_rate_count.setText(rate);
//		holder.tv_my_investment_list_project_duration_value
//				.setText(project_duration);
		return showLeaveStatus;
	}

	public static class ViewHolder {
		/**投标标名*/
		TextView  tv_loan_name; 
		/**收款日期或创建日期提示文字*/
		TextView tv_receipt_date; 
		/**收款日期或创建日期值*/
		TextView tv_receipt_date_value;
		/**年利率值值*/
		TextView tv_my_invetment_list_rate_count;
		/**项目期限文字提示*/
		TextView tv_my_investment_list_project_duration;
		/**项目期限值*/
		TextView tv_my_investment_list_project_duration_value;
		/**应收本息文字提示*/
		TextView tv_my_investment_list_invest_amount;
		/**应收本息值*/
		TextView tv_my_investment_list_invest_amount_value;
		/**投资金额*/
		TextView tv_my_invetment_list_amount;
		/**年利率项*/
		LinearLayout tv_my_invetment_rate;
	}
}
