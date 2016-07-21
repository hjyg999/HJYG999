package com.md.hjyg.adapter;

import java.util.List;
import java.util.Map;

import com.md.hjyg.R;
import com.md.hjyg.entity.ListModel;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: WholeFinancialAdaper date: 2016-3-23 上午11:18:49 remark:全部和充值资金记录
 * 
 * @author pyc
 */
public class WholeFinancialAdaper extends FinancialAdaper<ListModel> {
	/**
	 * 全部
	 */
	public final static int Whole = 0;
	/**
	 * 充值
	 */
	public final static int Recharge = 1;
	/**
	 * 收益
	 */
	public final static int Income = 2;
	private int sort;

	public WholeFinancialAdaper(Context context,
			Map<String, List<ListModel>> map, List<String> mapKey, int sort) {
		super(context, map, mapKey);
		this.sort = sort;
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
		ViewParamsSetUtil.setViewHandW_lin(tv_month, sbitmapH, sbitmapH);
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

		tv_month.setTextSize(TypedValue.COMPLEX_UNIT_SP, (sbitmapH - 4) / (2)
				* 1.5f / fontScale);
		ListModel model = map.get(mapKey.get(groupPosition)).get(childPosition);
		tv_month.setText(DateUtil.changto(model.Time.replace("/", "-"), "MM"));
		tv_time.setText(DateUtil.changto(model.Time.replace("/", "-"),
				"MM/dd HH:mm"));
		// 备注
		tv_remark.setText("备注：" + model.Remark);
		switch (sort) {
		case Whole:
		case Income:
			tv_s_acount.setVisibility(View.VISIBLE);
			// 剩余金额
			tv_s_acount.setText("余额：" + model.Amount);

			// 类型提示
			String type = model.Type;
			if (("7").equals(type)) {
				type = "微信红包";
			}
			tv_type.setText(type);
			// 判断是收入还是支出
			if (model.PayMemberMoney != null
					&& model.PayMemberMoney.length() > 0) {
				// 支出
				tv_acount.setText("-" + model.PayMemberMoney);
				tv_acount.setTextColor(context.getResources().getColor(
						R.color.yellow_FF9832));
//				tv_remark.setTextColor(context.getResources().getColor(
//						R.color.yellow_FECF9B));
				lin_bg.setBackgroundResource(R.drawable.list_bg_y);

			} else if (model.IncomeMemberMoney != null
					&& model.IncomeMemberMoney.length() > 0) {
				// 收入
				tv_acount.setText("+" + model.IncomeMemberMoney);
				lin_bg.setBackgroundResource(R.drawable.list_bg_g);
				tv_acount.setTextColor(context.getResources().getColor(
						R.color.blue_ht));
//				tv_remark.setTextColor(context.getResources().getColor(
//						R.color.bulu_97D5FC));
			}

			break;
		case Recharge:
			lin_bg.setBackgroundResource(R.drawable.list_bg_g);
			tv_s_acount.setVisibility(View.GONE);
			// 金额
			tv_acount.setText(model.Amount);
			tv_acount.setTextColor(context.getResources().getColor(
					R.color.blue_ht));
			// 状态
			tv_type.setText(model.Status);
			if ("未支付".equals(model.Status)) {
				tv_type.setTextColor(context.getResources().getColor(
						R.color.blue_ht));
			} else {
				tv_type.setTextColor(context.getResources().getColor(
						R.color.gray_gold));
			}
			// 备注
//			tv_remark.setTextColor(context.getResources().getColor(
//					R.color.bulu_97D5FC));

			break;

		default:
			break;
		}

	}

}
