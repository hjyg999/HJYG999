package com.md.hjyg.adapter;

import java.util.List;
import java.util.Map;

import com.md.hjyg.R;
import com.md.hjyg.entity.WithdrawalsList;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: WithdrawalsFinancialAdapter date: 2016-4-9 下午3:53:48 remark: 提现 -
 * 资金记录
 * 
 * @author pyc
 */
public class WithdrawalsFinancialAdapter extends
		FinancialAdaper<WithdrawalsList> {

	private String[] status = { "未审核", "初审通过", "初审不通过", "审核通过", "审核不通过" };

	public WithdrawalsFinancialAdapter(Context context,
			Map<String, List<WithdrawalsList>> map, List<String> mapKey) {
		super(context, map, mapKey);
	}

	@Override
	public void setUIDate(View v, int groupPosition, int childPosition) {
		TextView tv_month = (TextView) v.findViewById(R.id.tv_month);
		TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
		TextView tv_type = (TextView) v.findViewById(R.id.tv_type);
		TextView tv_acount = (TextView) v.findViewById(R.id.tv_acount);
		LinearLayout lin_bg = (LinearLayout) v.findViewById(R.id.lin_bg);
		TextView tv_s_acount = (TextView) v.findViewById(R.id.tv_s_acount);
		TextView tv_remark = (TextView) v.findViewById(R.id.tv_remark);
		TextView tv_time_2 = (TextView) v.findViewById(R.id.tv_time_2);
		ViewParamsSetUtil.setViewHandW_lin(tv_month, sbitmapH, sbitmapH);
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		tv_month.setTextSize(TypedValue.COMPLEX_UNIT_SP, (sbitmapH - 4) / (2)
				* 1.5f / fontScale);
		tv_time_2.setVisibility(View.VISIBLE);

		lin_bg.setBackgroundResource(R.drawable.list_bg_y);
		WithdrawalsList model = map.get(mapKey.get(groupPosition)).get(
				childPosition);

		int Status = model.Status;
		if (Status < status.length) {// 结果 0-未审核，1-初审通过，2-初审不通过，3-审核通过，4-审核不通过
			tv_type.setText(status[Status]);
			if (Status == 3) {
				tv_type.setTextColor(context.getResources().getColor(
						R.color.gray_gold));
			} else {
				tv_type.setTextColor(context.getResources().getColor(
						R.color.yellow_FF9832));
			}
		}
		// 时间
		if (model.CreateTime != null) {
			tv_time_2.setText(DateUtil.changto(model.CreateTime.replace("/", "-"),
					"MM/dd HH:mm"));
			tv_month.setText(DateUtil.changto(model.CreateTime.replace("/", "-"),
					"MM"));
		}
		// 手续费
		tv_time.setText("手续费");
		tv_s_acount.setText(model.Fee);
		// 提现金额
		tv_acount.setText(model.Amount);
		// 备注
		if (model.BankName != null && model.BankName.length() > 0 && model.BankCard != null
				&& model.BankCard.length() > 0) {

			String bankNumberAfterFour = model.BankCard.substring(
					model.BankCard.length() - 4, model.BankCard.length());
			String lastBankNumber = "";
			for (int i = 0; i < model.BankCard.length() - 4; i++) {
				lastBankNumber = lastBankNumber + "*";
			}
			lastBankNumber = lastBankNumber + bankNumberAfterFour;

			tv_remark.setText("备注：" + model.BankName + lastBankNumber);
		} else if (model.Remark != null && model.Remark.length() > 0) {
			tv_remark.setText("备注：" + model.Remark);
		} else {
			tv_remark.setText("暂无");
		}

	}

}
