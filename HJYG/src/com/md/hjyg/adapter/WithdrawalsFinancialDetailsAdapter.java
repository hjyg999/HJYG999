package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.entity.WithdrawalsList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 提现记录适配器
 */
public class WithdrawalsFinancialDetailsAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;
	Context context;
	String[] array;
	int value;
	String Amount, fee , date, remark, bankName, bankCard;
	int result;
	public ArrayList<WithdrawalsList> List = new ArrayList<WithdrawalsList>();

	public WithdrawalsFinancialDetailsAdapter(Context context, ArrayList<WithdrawalsList> List) {
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
		
		if (showLeaveStatus == null) {
			holder = new ViewHolder();
//			showLeaveStatus = layoutInflater.inflate(
//					R.layout.financial_details_list_item, null);
//			holder.tv_type = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_type);
//			holder.tv_income = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_income);
//			holder.tv_bonus_expenditure = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_expenditure);
//			holder.tv_bonus_balance = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_balance);
//			holder.tv_bonus_date = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_date);
//			holder.tv_income_value = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_income_value);
//			holder.tv_bonus_expenditure_value = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_expenditure_value);
//			holder.tv_bonus_balance_value = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_bonus_balance_value);
//			holder.tv_remark = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_remark);
//			holder.tv_remark_note = (TextView) showLeaveStatus
//					.findViewById(R.id.tv_remark_note);
			//新版
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

//		holder.tv_income.setText("金额");
//		holder.tv_bonus_expenditure.setText("手续费");
//		holder.tv_bonus_balance.setText("结果");
//		holder.tv_remark.setText("银行卡号：");
		
		WithdrawalsList wList = List.get(position);
				
		bankName = wList.BankName.toString();
		Amount = wList.Amount.toString();
		fee = wList.Fee.toString();
		result = wList.Status;
		date = wList.CreateTime.toString();
		remark = wList.Remark.toString();
		bankCard = wList.BankCard.toString();
		
//		holder.tv_type.setText(bankName);
//		holder.tv_income_value.setText("¥ " + Amount); // 金额
//		holder.tv_bonus_expenditure_value.setText("¥ " + fee);// 手续费
//		if (result == 0) {// 结果 0-未审核，1-初审通过，2-初审不通过，3-审核通过，4-审核不通过
//			holder.tv_bonus_balance_value.setText("未审核");
//		} else if (result == 1) {
//			holder.tv_bonus_balance_value.setText("初审通过");
//		} else if (result == 2) {
//			holder.tv_bonus_balance_value.setText("初审不通过");
//		} else if (result == 3) {
//			holder.tv_bonus_balance_value.setText("审核通过");
//		} else if (result == 4) {
//			holder.tv_bonus_balance_value.setText("审核不通过");
//		} else {
//			holder.tv_bonus_balance_value.setText("未处理");
//		}
//		
//		holder.tv_bonus_date.setText(date);// 时间
//		if (bankCard == null || bankCard.equals("")){
//			holder.tv_remark_note.setText("暂无");
//		} else {
//			String bankNumberAfterFour = bankCard.substring(bankCard.length() - 4, bankCard.length());
//			String lastBankNumber = "";
//			for (int i = 0; i < bankCard.length() - 4; i++) {
//				lastBankNumber = lastBankNumber +  "*";
//			}
//			lastBankNumber = lastBankNumber + bankNumberAfterFour;
//			holder.tv_remark_note.setText(lastBankNumber); //备注
//		}

		holder.type_or_amount.setText("¥ " + Amount);
		holder.type_or_amount.setTextColor(context.getResources().getColor(R.color.red));
		if(Amount.equals(1.00))
		{
			holder.type_or_time.setText("会员提现手续费");
		}else {
			holder.type_or_time.setText("提现金额");
		}
		
		holder.status_or_amount.setText("¥ " + fee);
		holder.status_or_type.setText("手续费");
		
		holder.amount_or_time.setText(date);
		holder.type.setText("提现时间");
		
		if (result == 0) {// 结果 0-未审核，1-初审通过，2-初审不通过，3-审核通过，4-审核不通过
			holder.status.setText("未审核--");
		} else if (result == 1) {
			holder.status.setText("初审通过--");
		} else if (result == 2) {
			holder.status.setText("初审不通过--");
		} else if (result == 3) {
			holder.status.setText("审核通过--");
		} else if (result == 4) {
			holder.status.setText("审核不通过--");
		} else {
			holder.status.setText("未处理--");
		}
		
		if (bankName != null && bankName.length() > 0 && bankCard != null &&  bankCard.length() > 0) {
			
			String bankNumberAfterFour = bankCard.substring(bankCard.length() - 4, bankCard.length());
			String lastBankNumber = "";
			for (int i = 0; i < bankCard.length() - 4; i++) {
				lastBankNumber = lastBankNumber +  "*";
			}
			lastBankNumber = lastBankNumber + bankNumberAfterFour;
			
			holder.remark.setText(bankName + "--" + "银行卡号：" +lastBankNumber);
		}else if (remark !=null && remark.length() > 0) {
			holder.remark.setText("备注：" + remark);
		}else {
			holder.remark.setText("暂无");
		}
		return showLeaveStatus;
	}

	public class ViewHolder {
//		TextView tv_type, tv_bonus_date, tv_income_value,tv_income,tv_bonus_expenditure,
//				tv_bonus_expenditure_value, tv_bonus_balance_value,tv_bonus_balance,tv_remark,
//				tv_remark_note;
		TextView type_or_amount,type_or_time,status_or_amount,
                 status_or_type,amount_or_time,type,status,remark;
        RelativeLayout rel_bot;
	}

}
