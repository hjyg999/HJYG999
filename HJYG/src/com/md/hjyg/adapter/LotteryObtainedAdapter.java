package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.GoldBeanRecordActivity;
import com.md.hjyg.activities.MyRedPacketFragmentActivity;
import com.md.hjyg.entity.AnnualMeetingLotteryInfoModel.MemberLotteryLogModel;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: LotteryObtainedAdapter date: 2015-10-27 上午9:39:05 remark:我的奖品适配器
 * 
 * @author pyc
 */
public class LotteryObtainedAdapter extends BaseAdapter {

	private List<MemberLotteryLogModel> lists;
	// private Context context;
	private LayoutInflater layoutInflater;
	private Activity context;
	// 未领取 = 0, 初审通过 = 1,
	// 初审不通过 = 2,
	// 已领取 = 3,
	// 审核不通过 = 4
	private String Status[] = { "未领取", "初审通过", "初审不通过", "已领取", "审核不通过 " };

	public LotteryObtainedAdapter(List<MemberLotteryLogModel> lists,
			Activity context) {
		this.context = context;
		this.lists = lists;
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		HoldView hold = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.lotteryadapteritem_layout, null);
			hold = new HoldView(convertView);
			convertView.setTag(hold);
		} else {
			hold = (HoldView) convertView.getTag();
		}

		MemberLotteryLogModel model = lists.get(position);
		if (position == lists.size()-1) {
			convertView.setPadding(0, 0, 0, 20);
		}else {
			convertView.setPadding(0, 0, 0, 0);
		}
		switch (model.type) {
		case 0:// 实物奖品
			hold.setUI(R.drawable.lottery_bg_sw, R.drawable.lottery_sw,model.PrizeName, model.Status,
					"实物礼品", false, "");
			break;
		case 1:// 理财红包
			hold.setUI(R.drawable.lottery_bg_tzhb,R.drawable.lottery_tzhb,
					Constants.StringToCurrency(model.InvestRedAmount + "")
							.replace(".00", "") + "元", model.Status, "投资红包",
					true, "立即查看");
			break;
		case 3:// 体验金
			hold.setUI(R.drawable.lottery_bg_tyjhb,R.drawable.lottery_tyjhb,
					Constants.StringToCurrency(model.ExperienceAmount + "")
							.replace(".00", "") + "元", model.Status, "体验金红包",
					true, "立即查看");
			break;
		case 4:// 金豆
			hold.setUI(R.drawable.lottery_bg_jd,R.drawable.lottery_jd,
					Constants.StringToCurrency(model.GoldBeanNumber + "")
							.replace(".00", "") + "个", model.Status, "金豆",
					true, "立即查看");
			break;
		case 5:// 加息券
			hold.setUI(R.drawable.lottery_bg_tyjhb,R.drawable.lottery_jxq,
					 model.PrizeName.replace("加息券", "") , model.Status, "加息券",
					true, "立即查看");
			break;
		default:// 现金红包
			hold.setUI(R.drawable.lottery_bg_pthb,R.drawable.lottery_pthb,
					Constants.StringToCurrency(model.RedEnvelopeAmount + "")
							.replace(".00", "") + "元", model.Status, "现金红包",
					true, "立即查看");
			break;
		}

		hold.tv_valid.setText("有效期：" + model.time);
		hold.tv_time.setText(model.CreateTime);
		hold.lin_touse.setTag(position);

		return convertView;
	}


	class HoldView {
		TextView tv_amount, tv_state, tv_type, tv_time, tv_valid, tv_touse;
		ImageView img, img_arrow;
		LinearLayout line_bg, lin_touse;

		public HoldView(View v) {
			tv_amount = (TextView) v.findViewById(R.id.tv_amount);
			tv_state = (TextView) v.findViewById(R.id.tv_state);
			tv_type = (TextView) v.findViewById(R.id.tv_type);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_valid = (TextView) v.findViewById(R.id.tv_valid);
			img = (ImageView) v.findViewById(R.id.img);
			img_arrow = (ImageView) v.findViewById(R.id.img_arrow);
			line_bg = (LinearLayout) v.findViewById(R.id.line_bg);
			lin_touse = (LinearLayout) v.findViewById(R.id.lin_touse);
			tv_touse = (TextView) v.findViewById(R.id.tv_touse);

			ViewParamsSetUtil.setViewParams(line_bg, 660, 246, true);
			ViewParamsSetUtil.setViewParams(img, 138, 138, true);
			ViewParamsSetUtil.setViewParams(img_arrow, 16, 28, true);
		}

		public void setUI(int bgImgId,int prizeImgId, String amount, int state,
				String type, boolean isShow, String touse) {
			line_bg.setBackgroundResource(bgImgId);
			img.setImageResource(prizeImgId);
			tv_amount.setText(amount);
			tv_state.setText(Status[state]);
			if (state == 3) {
				tv_state.setTextColor(context.getResources().getColor(
						R.color.green_99CC33));
			} else {
				tv_state.setTextColor(context.getResources().getColor(
						R.color.yellow_ff9933));
			}
			tv_type.setText(type);
			if (isShow) {
				lin_touse.setVisibility(View.VISIBLE);
				lin_touse.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int p = (Integer) v.getTag();
						int type = lists.get(p).type;
						if (type== 0) {//金豆和实物不跳
							
						} else if (type == 4) {
							Intent intent = new Intent(context,
									GoldBeanRecordActivity.class);
							intent.putExtra("tab", 0);
							context.startActivity(intent);
							context.overridePendingTransition(
									R.anim.trans_right_in,
									R.anim.trans_lift_out);
						} else {
							Intent intent = new Intent(context,
									MyRedPacketFragmentActivity.class);
							intent.putExtra("tab", 0);
							context.startActivity(intent);
							context.overridePendingTransition(
									R.anim.trans_right_in,
									R.anim.trans_lift_out);
						}
//						if (lists.get(p).type == 1) {//"去投资"
//							Intent intent = new Intent(context,
//									HomeFragmentActivity.class);
//							intent.putExtra("tab", 0);
//							context.startActivity(intent);
//							context.overridePendingTransition(
//									R.anim.trans_lift_in,
//									R.anim.trans_right_out);
//						}else if (lists.get(p).type == 3) {
//							Intent intent = new Intent(context,
//									HuoQibaoBuyActivity.class);
//							context.startActivity(intent);
//							context.overridePendingTransition(
//									R.anim.trans_right_in,
//									R.anim.trans_lift_out);
//						}
						
					}
				});
			} else {
				lin_touse.setVisibility(View.GONE);
			}
			tv_touse.setText(touse);
		}
	}

}
