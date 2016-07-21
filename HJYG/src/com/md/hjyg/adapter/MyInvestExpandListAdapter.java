package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.InvestmentListDetailModel;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: MyInvestExpandListAdapter date: 2016-4-22 下午2:18:09
 * remark:我的投资展开还款列表适配器
 * 
 * @author pyc
 */
public class MyInvestExpandListAdapter extends
		MyBaseAdapter<InvestmentListDetailModel> {

	private Bitmap[] bitmaps;
	private int green;
	private int gray;
	private int red;

	public MyInvestExpandListAdapter(List<InvestmentListDetailModel> lists,
			Context context, Bitmap[] bitmaps) {
		super(lists, context);
		this.bitmaps = bitmaps;
		green = context.getResources().getColor(R.color.green_99CC33);
		gray = context.getResources().getColor(R.color.gray_sq);
		red = context.getResources().getColor(R.color.red);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HoldView holdView = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.myinvest_expanditem_layout,
					null);
			holdView = new HoldView(convertView);
			convertView.setTag(holdView);
		} else {
			holdView = (HoldView) convertView.getTag();
		}
		InvestmentListDetailModel detailModel = lists.get(position);
		// /** 还款状态 0-未还，1-部分已还，2-全额已还，3-作废 */
		int Status = Integer.parseInt(detailModel.Status);
		// /** 逾期状态 正常 = 0, 逾期 = 1, 代偿 = 2, 作废 = 3, 提前还款 = 4 */
		int OverStatus = Integer.parseInt(detailModel.OverStatus);
		holdView.tv_nob.setText((position + 1) + "");
		if (detailModel.isRevoke) {
			holdView.tv_state.setText("退标");
			holdView.tv_state.setTextColor(gray);
			holdView.tv_nob.setBackgroundResource(R.drawable.fq);
			holdView.tv_time.setText(detailModel.RepaymentDate);
			holdView.img_toright.setVisibility(View.GONE);
			holdView.tv_income.setText("本金：" + Constants.StringToCurrency(detailModel.Principal +""));

		} else {

			if (Status == InvestmentListDetailModel.还款状态_全额已还
					&& OverStatus == InvestmentListDetailModel.逾期状态_正常) {
				holdView.tv_state.setText("已收");
				holdView.tv_state.setTextColor(green);
				holdView.tv_nob.setBackgroundResource(R.drawable.lq);
				holdView.tv_time.setText(detailModel.ActualRepaymentDate);
				holdView.img_toright.setVisibility(View.GONE);
				if (detailModel.Principal > 0) {
					holdView.tv_income.setText("本息："
							+ detailModel.PrincipalInterest);
				} else {
					holdView.tv_income.setText("利息：" + detailModel.Interest);
				}
			} else if (Status == InvestmentListDetailModel.还款状态_未还
					&& OverStatus == InvestmentListDetailModel.逾期状态_逾期) {
				holdView.tv_state.setText("逾期");
				holdView.tv_state.setTextColor(red);
				holdView.tv_nob.setBackgroundResource(R.drawable.rq);
				holdView.tv_time.setText(detailModel.RepaymentDate);
				holdView.img_toright.setVisibility(View.GONE);
				if (detailModel.UnfinishedPrincipal > 0) {
					holdView.tv_income.setText("本息："
							+ detailModel.UnfinishedPrincipalInterest);
				} else {
					holdView.tv_income.setText("利息："
							+ detailModel.UnfinishedInterest);
				}
			} else if (Status == InvestmentListDetailModel.还款状态_全额已还
					&& OverStatus == InvestmentListDetailModel.逾期状态_逾期) {
				holdView.tv_state.setText("已收(逾期)");
				holdView.tv_state.setTextColor(green);
				holdView.tv_nob.setBackgroundResource(R.drawable.lq);
				holdView.tv_time.setText(detailModel.ActualRepaymentDate);
				holdView.img_toright.setVisibility(View.VISIBLE);
				if (detailModel.Principal > 0) {
					holdView.tv_income.setText("本息(逾期)："
							+ detailModel.PrincipalInterest + "+"
							+ detailModel.OverInterest);
				} else {
					holdView.tv_income.setText("利息(逾期)：" + detailModel.Interest
							+ "+" + detailModel.OverInterest);
				}
				lists.get(position).setCanClick(true);
			} else if (Status == InvestmentListDetailModel.还款状态_未还
					&& OverStatus == InvestmentListDetailModel.逾期状态_正常) {
				holdView.tv_state.setText("待收");
				holdView.tv_state.setTextColor(gray);
				holdView.tv_nob.setBackgroundResource(R.drawable.fq);
				holdView.tv_time.setText(detailModel.RepaymentDate);
				holdView.img_toright.setVisibility(View.GONE);
				if (detailModel.Principal > 0) {
					holdView.tv_income.setText("本息："
							+ detailModel.PrincipalInterest);
				} else {
					holdView.tv_income.setText("利息：" + detailModel.Interest);
				}
			} else if (Status == InvestmentListDetailModel.还款状态_部分已还
					&& OverStatus == InvestmentListDetailModel.逾期状态_逾期) {
				holdView.tv_state.setText("逾期");
				holdView.tv_state.setTextColor(red);
				holdView.tv_nob.setBackgroundResource(R.drawable.rq);
				holdView.tv_time.setText(detailModel.RepaymentDate);
				holdView.img_toright.setVisibility(View.GONE);
				if (detailModel.UnfinishedPrincipal > 0) {
					holdView.tv_income.setText("本息："
							+ detailModel.UnfinishedPrincipalInterest);
				} else {
					holdView.tv_income.setText("利息："
							+ detailModel.UnfinishedOverInterest);
				}
			}
		}
		return convertView;
	}

	class HoldView {
		TextView tv_nob, tv_state, tv_income, tv_time;
		ImageView img_toright;

		public HoldView(View v) {
			tv_nob = (TextView) v.findViewById(R.id.tv_nob);
			tv_state = (TextView) v.findViewById(R.id.tv_state);
			tv_income = (TextView) v.findViewById(R.id.tv_income);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			img_toright = (ImageView) v.findViewById(R.id.img_toright);
			if (bitmaps != null && bitmaps.length > 0) {
				ViewParamsSetUtil.setViewHandW_rel(tv_nob, bitmaps[0]);
				float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
				tv_nob.setTextSize(TypedValue.COMPLEX_UNIT_SP,
						(bitmaps[0].getWidth() - 4) / (2) * 1.5f / fontScale);
				img_toright.setImageBitmap(bitmaps[1]);
			}
		}
	}

}
