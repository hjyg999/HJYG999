package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.ListModel;
import com.md.hjyg.entity.WithdrawalsList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 全部资金记录 和 充值记录资金 适配器
 */
public class FinancialDetailsAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;
	Context context;
	String[] array;
	int value;
	String income, balance, expenditure, remark, type, date;
	public ArrayList<ListModel> List = new ArrayList<ListModel>();
	public ArrayList<WithdrawalsList> WithdList ;
	/**1.全部资金记录 2.充值记录*/
	int sort;

	public FinancialDetailsAdapter(Context context, ArrayList<ListModel> List ,int sort) {
		this.context = context;
		this.List = List;
		this.sort = sort;
		layoutInflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
//	public FinancialDetailsAdapter(Context context, ArrayList<WithdrawalsList> List) {
//		this.context = context;
//		this.WithdList = List;
//		this.sort =3;
//		layoutInflater = (LayoutInflater) context
//				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//	}

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
//			showLeaveStatus = layoutInflater.inflate(
//					R.layout.financial_details_list_item, null);
//			holder.tv_type = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_type);
//			holder.tv_bonus_date = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_date);
//			holder.tv_income_value = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_income_value);
//			holder.tv_bonus_expenditure_value = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_expenditure_value);
//			holder.tv_bonus_balance_value = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_balance_value);
//			holder.tv_remark_note = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_remark_note);
			//新版
//			TextView type_or_amount,type_or_time,status_or_amount,
//	         status_or_type,amount_or_time,type,status,remark,rel_bot;
			showLeaveStatus = layoutInflater.inflate(R.layout.financial_details_list_item_new, null);
			holder.type_or_amount = (TextView) showLeaveStatus.findViewById(R.id.type_or_amount);
			holder.type_or_time = (TextView) showLeaveStatus.findViewById(R.id.type_or_time);
			holder.status_or_amount = (TextView) showLeaveStatus.findViewById(R.id.status_or_amount);
			holder.status_or_type = (TextView) showLeaveStatus.findViewById(R.id.status_or_type);
			holder.amount_or_time = (TextView) showLeaveStatus.findViewById(R.id.amount_or_time);
			holder.type = (TextView) showLeaveStatus.findViewById(R.id.type);
			holder.status = (TextView) showLeaveStatus.findViewById(R.id.status);
			holder.remark = (TextView) showLeaveStatus.findViewById(R.id.remark);
			holder.rel_bot = (RelativeLayout) showLeaveStatus.findViewById(R.id.rel_bot);

		} else {
			holder = (ViewHolder) showLeaveStatus.getTag();
		}
		showLeaveStatus.setTag(holder);

		
		remark = List.get(position).Remark.toString();
		date = List.get(position).Time.toString();
		
		
		switch (sort) {
		case 1://全部记录
			balance = List.get(position).Amount.toString();
			expenditure= List.get(position).PayMemberMoney.toString();
			income = List.get(position).IncomeMemberMoney.toString();
			type = List.get(position).Type.toString();
			if(type.equals("7"))
			{
				type = "微信红包";
			}
			
			holder.type_or_amount.setText(type);
			holder.type_or_amount.setTextColor(context.getResources().getColor(R.color.gray_s));
			holder.type_or_time.setText(date);
			//判断是支出还是收入
			if(expenditure!=null && expenditure.length()>0)
			{//支出
				holder.status_or_amount.setText("¥ "+ expenditure);
				if(type.indexOf("手续费") != -1)
				{
					holder.status_or_type.setText("手续费支出");
				}else {
					holder.status_or_type.setText(type + "支出");
				}
			}else if (income != null && income.length() >0) {
				//收入
				holder.status_or_amount.setText("¥ "+ income);
				if(type.indexOf("利息") != -1)
				{
					holder.status_or_type.setText("利息收入");
				}else if (type.indexOf("红包") != -1) {
					holder.status_or_type.setText("红包收入");
				}else {//本金，充值收入等
					holder.status_or_type.setText(type + "收入");
				}
			}else {
				holder.status_or_amount.setText("");
				holder.status_or_type.setText("");
			}
			holder.status_or_amount.setTextColor(context.getResources().getColor(R.color.red));
			
			holder.amount_or_time.setText("¥ "+ balance);
			holder.amount_or_time.setTextColor(context.getResources().getColor(R.color.red));
			holder.type.setText("剩余金额");
			holder.remark.setText("备注：" + remark);
			break;
		case 2://充值记录
			String Amount = List.get(position).Amount.toString();
			String Status = List.get(position).Status.toString();
			
			holder.type_or_amount.setText("¥ " + Amount);
			holder.type_or_amount.setTextColor(context.getResources().getColor(R.color.red));
			//判断是用户充值还是系统的推荐奖励
			if(remark.indexOf("代发充值") != -1)
			{
				holder.type_or_time.setText("充值奖励");
			}else {
				holder.type_or_time.setText("充值金额");
			}
			//未支付显示黄色，成功显示红色
			if (Status.equals("未支付")) {
				holder.status_or_amount.setTextColor(context.getResources().getColor(R.color.yellow));
			} else {
				holder.status_or_amount.setTextColor(context.getResources().getColor(R.color.red));
			}
			holder.status_or_amount.setText(Status);
			holder.status_or_type.setText("状态");
			
			holder.amount_or_time.setText(date);
			holder.amount_or_time.setTextColor(context.getResources().getColor(R.color.gray));
			holder.type.setText("充值时间");
			
			//holder.status = (TextView) showLeaveStatus.findViewById(R.id.status);
			holder.remark.setText("备注：" + remark);
			
			
			break;
        
		default:
			break;
		}
		
//		holder.tv_type.setText(type);//类型
//		holder.tv_bonus_date.setText(date);//时间
//		if(expenditure == null || expenditure.length() ==0 )
//		{
//			holder.tv_bonus_expenditure_value.setText("");//没有数据者不显示
//		}else {
//			
//			holder.tv_bonus_expenditure_value.setText("¥ "+ expenditure );//支出
//		}
//		holder.tv_remark_note.setText(remark); //备注
//		holder.tv_bonus_balance_value.setText("¥ "+ balance ); //剩余
//		
//		if(income == null || income.length() ==0 )
//		{
//			holder.tv_income_value.setText("");//没有数据者不显示
//		}else {
//			holder.tv_income_value.setText("¥ "+ income );//收入
//		}

		return showLeaveStatus;
	}

	public class ViewHolder {
//		TextView tv_type, tv_bonus_date, tv_income_value,
//				tv_bonus_expenditure_value, tv_bonus_balance_value,
//				tv_remark_note;
		TextView type_or_amount,type_or_time,status_or_amount,
		         status_or_type,amount_or_time,type,status,remark;
		RelativeLayout rel_bot;
	}

}
