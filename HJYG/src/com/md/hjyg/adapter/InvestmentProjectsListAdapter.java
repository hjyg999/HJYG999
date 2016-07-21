package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.layoutEntities.InvestProgressView;
import com.md.hjyg.utility.CustomDigitalClock;
import com.md.hjyg.utility.CustomDigitalClock.ClockListener;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: InvestmentProjectsListAdapter date: 2016-2-22 上午10:03:19
 * remark:理财频道项目列表适配器-新版
 * 
 * @author pyc
 */
public class InvestmentProjectsListAdapter extends MyBaseAdapter<LoanModel> {

	private String[] Status_Text;

	public InvestmentProjectsListAdapter(List<LoanModel> lists, Context context) {
		super(lists, context);
		// Status_Text = new String[] { "审核中", "初审中", "初审通过", "竞标中", "核保审批中",
		// "平台终（复）审", "还款中", "审核不通过", "流标", "已还款" };
		Status_Text = new String[] { "审核中", "初审中", "初审通过", "竞标中", "核保审批",
				"平台终（复）审", "收益中", "审核不通过", "流标", "还款完成" };
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.adapter_investment_list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		convertView.setBackgroundColor(context.getResources().getColor(R.color.gold_qgray_bg));
		
		LoanModel model = lists.get(position);
		holder.img_juoqi.setVisibility(View.GONE);
		holder.tv_rate_add.setVisibility(View.GONE);
		holder.tv_right_ys.setVisibility(View.GONE);
		holder.tv_right_jx.setVisibility(View.GONE);
		holder.cdc_hall_time.setVisibility(View.GONE);

		// 设置项目标题名
		holder.tv_loan_title.setText(model.Title);
		// 设置项目利率
		if (model.ActivitiesRate > 0) {
			holder.tv_rate_add.setText("+" + model.ActivitiesRate +"%");
			holder.tv_rate_value.setText(Constants
					.StringToCurrency((model.LoanRate - model.ActivitiesRate)
							+ ""));
			holder.tv_rate_add.setVisibility(View.VISIBLE);
			holder.tv_right_jx.setVisibility(View.VISIBLE);
		} else {

			holder.tv_rate_add.setText("");
			holder.tv_rate_value.setText(Constants
					.StringToCurrency(model.LoanRate + ""));
		}
		// 设置项目期限
		holder.tv_term_value.setText(model.LoanTerm + "");
		if (model.LoanDateType == 0) {
			holder.tv_dw_value.setText("个月");
		} else if (model.LoanDateType == 2) {
			holder.tv_dw_value.setText("天");
		} else if (model.LoanDateType == 4) {
			holder.tv_dw_value.setText("周");
		}
		holder.tv_term_valuehit.setText("期限");
		
		holder.mProgressView.setVisibility(View.GONE);

		// 设置状态
		switch (model.Status) {
		case 4: // 竞标中
			
			convertView.setBackgroundColor(context.getResources().getColor(R.color.white));
			holder.mProgressView.setVisibility(View.VISIBLE);
//			double Amount = Constants.StringToDouble(model.Amount);
//			holder.mProgressView.setProgress((float)((Amount-model.SurplusAmount)/Amount));
			holder.mProgressView.setProgress(model.InvestProcess);
			// 剩余金额
			holder.tv_amout_value.setText(Constants
					.StringToCurrency(model.SurplusAmount + "").replace(".00", ""));
			holder.booking_percentage_text.setText("剩余可投(元)");
			holder.tv_term_value.setTextColor(context.getResources().getColor(R.color.red));
			holder.tv_rate_value.setTextColor(context.getResources().getColor(R.color.red));
			holder.tv_rate_f.setTextColor(context.getResources().getColor(R.color.red));
			holder.tv_rate_add.setTextColor(context.getResources().getColor(R.color.yellow_ff9933));
			holder.tv_amout_value.setTextSize(18);
//			holder.tv_amout_value.setPadding(0, 0, 0, 0);

			if (DateUtil.isStart(model.BiddingStratTime)) {// 已经发布，热销

			} else {// 即将发布、黄色显示 预售
				holder.tv_right_ys.setVisibility(View.VISIBLE);
				// 设置倒计时
				holder.cdc_hall_time.setVisibility(View.VISIBLE);
				holder.cdc_hall_time.setEndTime(DateUtil
						.getLongTime(model.BiddingStratTime));
				holder.cdc_hall_time.setClockListener(new ClockListener() {

					@Override
					public void timeEnd() {
						InvestmentProjectsListAdapter.this
								.notifyDataSetChanged();
					}

					@Override
					public void remainFiveMinutes() {
					}
				});

			}
			break;

		default:// 核保审批中
			holder.booking_percentage_text
					.setText(Status_Text[model.Status - 1]);
			holder.tv_amout_value.setText(DateUtil.changto(model.FullTime, "yyyy/MM/dd"));
			holder.tv_term_value.setTextColor(context.getResources().getColor(R.color.gray_gold));
			holder.tv_rate_value.setTextColor(context.getResources().getColor(R.color.gray_gold));
			holder.tv_rate_f.setTextColor(context.getResources().getColor(R.color.gray_gold));
			holder.tv_rate_add.setTextColor(context.getResources().getColor(R.color.gray_gold));
			holder.tv_amout_value.setTextSize(14);
//			holder.tv_amout_value.setPadding(0, 0, 0, 3);
//			holder.booking_percentage_text.setVisibility(View.VISIBLE);
			break;
		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_loan_title, tv_term_value,
				tv_rate_value, tv_rate_add, tv_amout_value,
				booking_percentage_text,tv_term_valuehit,tv_right_jx,tv_right_ys,tv_dw_value,tv_rate_f;
		CustomDigitalClock cdc_hall_time;
		ImageView img_juoqi;
		InvestProgressView mProgressView;

		public ViewHolder(View v) {
			// 项目标题
			tv_loan_title = (TextView) v.findViewById(R.id.tv_loan_title);
			// 预售
			tv_right_ys = (TextView) v
					.findViewById(R.id.tv_right_ys);
			// 加息
			tv_right_jx = (TextView) v
					.findViewById(R.id.tv_right_jx);
			// 期限
			tv_term_value = (TextView) v.findViewById(R.id.tv_term_value);
			// 期限单位
			tv_term_valuehit = (TextView) v.findViewById(R.id.tv_term_valuehit);
			// 利率
			tv_rate_value = (TextView) v.findViewById(R.id.tv_rate_value);
			// 奖励利率
			tv_rate_add = (TextView) v.findViewById(R.id.tv_rate_add);
			// 剩余金额
			tv_amout_value = (TextView) v.findViewById(R.id.tv_amout_value);
			// 单位
			tv_dw_value = (TextView) v.findViewById(R.id.tv_dw_value);
			// 已截标的状态
			booking_percentage_text = (TextView) v
					.findViewById(R.id.booking_percentage_text);
			// 即将发布倒计时
			cdc_hall_time = (CustomDigitalClock) v
					.findViewById(R.id.cdc_hall_time);
			// 活期宝图标
			img_juoqi = (ImageView) v.findViewById(R.id.img_juoqi);
			tv_rate_f = (TextView) v.findViewById(R.id.tv_rate_f);
			mProgressView = (InvestProgressView) v.findViewById(R.id.mProgressView);
		}
	}

}
