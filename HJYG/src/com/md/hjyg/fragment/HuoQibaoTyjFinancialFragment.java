package com.md.hjyg.fragment;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Response;

import com.md.hjyg.R;
import com.md.hjyg.adapter.FinancialAdaper;
import com.md.hjyg.entity.ExperienceAmountRecordModel;
import com.md.hjyg.entity.ExperienceAmountRecordModel.ExperienceAmountFinanceLog;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: HuoQibaoTyjFinancialFragment date: 2016-6-1 下午2:55:27 remark:
 * 
 * @author pyc
 */
public class HuoQibaoTyjFinancialFragment extends
		FinancialFragment<ExperienceAmountFinanceLog> {

	public HuoQibaoTyjFinancialFragment() {
	}

	@Override
	public void getFinancialDetailsWebservice(String ftype, final int pageIndex) {
		dft.postSingleLoanSelfInfoList(pageIndex,
				Constants.GetExperienceAmountFinanceLogList_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						ExperienceAmountRecordModel Model = (ExperienceAmountRecordModel) dft
								.GetResponseObject(response,
										ExperienceAmountRecordModel.class);
						if (pageIndex == 1) {
							SetUIData("", Model.list);
						} else {
							SetUIData("loadmore", Model.list);

						}
					}

				});

	}

	@Override
	public FinancialAdaper<ExperienceAmountFinanceLog> getAdaper(
			List<String> mapKey,
			Map<String, List<ExperienceAmountFinanceLog>> map) {
		return new HuoQibaoTyjFinancialAdaper(getActivity(), map, mapKey);
	}

	@Override
	public String getYearTime(ExperienceAmountFinanceLog t) {
		return t.CreateTime;
	}

	class HuoQibaoTyjFinancialAdaper extends
			FinancialAdaper<ExperienceAmountFinanceLog> {

		public HuoQibaoTyjFinancialAdaper(Context context,
				Map<String, List<ExperienceAmountFinanceLog>> map,
				List<String> mapKey) {
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
			ViewParamsSetUtil.setViewHandW_lin(tv_month, sbitmapH, sbitmapH);
			float fontScale = context.getResources().getDisplayMetrics().scaledDensity;

			tv_month.setTextSize(TypedValue.COMPLEX_UNIT_SP, (sbitmapH - 4)
					/ (2) * 1.5f / fontScale);
			ExperienceAmountFinanceLog model = map.get(
					mapKey.get(groupPosition)).get(childPosition);
			tv_month.setText(DateUtil.changto(
					model.CreateTime.replace("/", "-"), "MM"));
			tv_time.setText(DateUtil.changto(
					model.CreateTime.replace("/", "-"), "MM/dd HH:mm"));
			// 备注
			tv_remark.setText(model.Remark);

			tv_s_acount.setVisibility(View.VISIBLE);
			// 剩余金额
			tv_s_acount.setText("余额：" + model.LeaveExperienceAmount);

			tv_type.setText(model.type);
			// 判断是收入还是支出
			if (model.IncomeExperience != null
					&& model.IncomeExperience.length() > 0) {
				// 收入
				tv_acount.setText("+" + model.IncomeExperience);
				lin_bg.setBackgroundResource(R.drawable.list_bg_g);
				tv_acount.setTextColor(context.getResources().getColor(
						R.color.blue_ht));
				// tv_remark.setTextColor(context.getResources().getColor(
				// R.color.bulu_97D5FC));
			} else {
				// 支出
				tv_acount.setText("-" + model.OutcomeExperience);
				tv_acount.setTextColor(context.getResources().getColor(
						R.color.yellow_FF9832));
				// tv_remark.setTextColor(context.getResources().getColor(
				// R.color.yellow_FECF9B));
				lin_bg.setBackgroundResource(R.drawable.list_bg_y);
			}

		}

	}

}
