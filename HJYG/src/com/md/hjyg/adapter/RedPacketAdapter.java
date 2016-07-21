package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.entity.RedPacketList;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: RedPacketAdapter date: 2016-5-10 下午2:45:35 remark: 我的红包适配器
 * 
 * @author pyc
 */
public class RedPacketAdapter extends MyBaseAdapter<RedPacketList> {

	// private Bitmap[] bitmaps;

	private int tab;
	private Activity mActivity;
	private float textSize;
	private int bitW, size;
	private int[][] bitmapW_Hs;
	private UseRedEnvelopeListener listener;

	/**
	 * 
	 * @param lists
	 * @param context
	 * @param tab
	 *            0为可使用，1为已兑换，2为已过期
	 */
	public RedPacketAdapter(List<RedPacketList> lists, Activity context, int tab,UseRedEnvelopeListener listener) {
		super(lists, context);
		this.mActivity = context;
		this.tab = tab;
		this.listener = listener;
		initBitmap();
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_listredpack, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lin_amount.setTag(position);
		RedPacketList modle = lists.get(position);

		// 有效期
		holder.tv_time.setText("有效期：" + modle.startTime + "-" + modle.endTime +" ");
		// 兑换要求
		holder.tv_remark.setText("兑换要求：" + modle.Remark);
		if (modle.newType == RedPacketList.体验金红包
				|| modle.newType == RedPacketList.生日红包) {
			holder.img.setImageResource(R.drawable.redpacket_tyj);
		} else if (modle.newType == RedPacketList.现金红包) {
			holder.img.setImageResource(R.drawable.redpacket_xj);
		} else {
			holder.img.setImageResource(R.drawable.redpacket_tz);
		}
		// 类型
		if (modle.newType == RedPacketList.体验金红包) {
			holder.tv_type.setText("体验金红包");
		} else if (modle.newType == RedPacketList.生日红包) {
			holder.tv_type.setText("生日礼金");
		} else if (modle.newType == RedPacketList.现金红包) {
			holder.tv_type.setText("现金红包");
		} else {
			holder.tv_type.setText("投资红包");
		}
		
		String amounttemp = "0";

		if (tab == 1) {
			if (Double.valueOf(modle.Amount) > 0) {
				amounttemp = modle.Amount;
			}else if (Double.valueOf(modle.ExperienceAmount) > 0) {
				amounttemp = modle.ExperienceAmount;
			}
			
			if (modle.newType == RedPacketList.体验金红包
					|| modle.newType == RedPacketList.生日红包) {
				setStaut(holder, "已使用", R.color.gray_sq,
						R.drawable.bg_mra_graye9_bg);
			} else {
				setStaut(holder, "已领取", R.color.gray_sq,
						R.drawable.bg_mra_graye9_bg);
			}

		} else {
			String[] Amount = null;
			if (modle.AmountInfo != null) {
				Amount = modle.AmountInfo.split("-");
			}
			String[] Experience = null;
			if (modle.ExperienceAmountInfo != null) {
				Experience = modle.ExperienceAmountInfo.split("-");
			}
			String[] LotteryActivityRedEnvelopeAmount = null;
			if (modle.LotteryActivityRedEnvelopeAmount != null) {
				LotteryActivityRedEnvelopeAmount = modle.LotteryActivityRedEnvelopeAmount.split("-");
			}
			String[] LotteryActivityExperienceAmount = null;
			if (modle.LotteryActivityExperienceAmount != null) {
				LotteryActivityExperienceAmount = modle.LotteryActivityExperienceAmount.split("-");
			}
			if (Amount != null && (Double.valueOf(Amount[0]) > 0
					|| Double.valueOf(Amount[1]) > 0)) {
				amounttemp = Amount[1];
			}else if (Experience != null && (Double.valueOf(Experience[0]) > 0
					|| Double.valueOf(Experience[1]) > 0)) {
				amounttemp = Experience[1];
			}else if (LotteryActivityRedEnvelopeAmount != null && (Double.valueOf(LotteryActivityRedEnvelopeAmount[0]) > 0
					|| Double.valueOf(LotteryActivityRedEnvelopeAmount[1]) > 0)) {
				amounttemp = LotteryActivityRedEnvelopeAmount[1];
			}else if (LotteryActivityExperienceAmount != null && (Double.valueOf(LotteryActivityExperienceAmount[0]) > 0
					|| Double.valueOf(LotteryActivityExperienceAmount[1]) > 0)) {
				amounttemp = LotteryActivityExperienceAmount[1];
			}
			
			if (tab == 0) {
				if (modle.newType == RedPacketList.体验金红包
						|| modle.newType == RedPacketList.生日红包 || modle.newType == RedPacketList.现金红包) {
					if (modle.newType == RedPacketList.现金红包){
						setStaut(holder, "立即使用", R.color.white,
								R.drawable.bg_mra_reds_bg);
					} else {
						setStaut(holder, "立即领取", R.color.white,
								R.drawable.bg_mra_reds_bg);
					}
					
				} else {
					setStaut(holder, "立即投资", R.color.white,
							R.drawable.bg_mra_reds_bg);
				}
			} else {
				setStaut(holder, "已过期", R.color.gray_sq,
						R.drawable.bg_mra_graye9_bg);

			}
		}
		
		if (Double.valueOf(amounttemp) > 100000) {
			holder.tv_amount.setText(Constants.StringToCurrency(
					Double.valueOf(amounttemp) / 10000 + "").replace(".00", "")
					+ "万元");
		} else {
			holder.tv_amount.setText(Constants.StringToCurrency(amounttemp).replace(".00", "") + "元");
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView tv_type, tv_time, tv_remark, tv_amount, tv_timetype;
		LinearLayout lin_amount;

		public ViewHolder(View v) {
			img = (ImageView) v.findViewById(R.id.img);

			tv_type = (TextView) v.findViewById(R.id.tv_type);
			tv_time = (TextView) v.findViewById(R.id.tv_time);
			tv_remark = (TextView) v.findViewById(R.id.tv_remark);
			tv_amount = (TextView) v.findViewById(R.id.tv_amount);
			tv_timetype = (TextView) v.findViewById(R.id.tv_timetype);

			lin_amount = (LinearLayout) v.findViewById(R.id.lin_amount);
			ViewParamsSetUtil.setViewHandW_lin(lin_amount, bitmapW_Hs[0][1],
					bitmapW_Hs[0][0]);

			tv_amount.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			tv_timetype.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) img
					.getLayoutParams();
			params.height = bitW;
			params.width = bitW;
			if (size > bitW) {
				// img.setPadding(0, (size-bitW)/2, 0, 0);
				params.setMargins(0, (size - bitW) / 2, 0, 0);
			} else {
				tv_type.setPadding(0, (bitW - size) / 2, 0, 0);
			}
			img.setLayoutParams(params);
			lin_amount.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (tab == 0) {
						int p = (Integer) v.getTag();
						if (lists.get(p).newType == RedPacketList.体验金红包
								|| lists.get(p).newType == RedPacketList.生日红包||lists.get(p).newType == RedPacketList.现金红包) {
							if (listener != null) {
								listener.postUseRedEnvelope(p);
							}
						} else {
							Intent intent = new Intent(mActivity,
									HomeFragmentActivity.class);
							intent.putExtra("tab", 0);
							mActivity.startActivity(intent);
							mActivity.overridePendingTransition(
									R.anim.trans_lift_in,
									R.anim.trans_right_out);
						}
					}
				}
			});
		}
	}

	private void initBitmap() {
		bitmapW_Hs = new int[2][2];
		bitmapW_Hs[0] = Save.getScaleBitmapWangH(144, 136);
		bitmapW_Hs[1] = Save.getScaleBitmapWangH(36, 36);
		// Bitmap reimg = BitmapFactory.decodeResource(context.getResources(),
		// R.drawable.novice_red_bg);
		// bitmaps = new Bitmap[2];
		// bitmaps[0] = Save
		// .ScaleBitmap(BitmapFactory.decodeResource(
		// context.getResources(), R.drawable.redpacked_m),
		// context, reimg);
		// bitmaps[1] = Save.ScaleBitmap(BitmapFactory.decodeResource(
		// context.getResources(), R.drawable.redpacket_xj), context,
		// reimg);
		float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		textSize = bitmapW_Hs[0][0] * 0.7f / (6) * 1.5f / fontScale;
		bitW = bitmapW_Hs[1][1];
		size = (int) (ScreenUtils.sp2px(context, 14) * 1.4);
	}

	private void setStaut(ViewHolder holder, String msg, int colorId,
			int drawableId) {
		holder.tv_timetype.setText(msg);
		holder.tv_timetype.setTextColor(context.getResources()
				.getColor(colorId));
		holder.tv_timetype.setBackgroundResource(drawableId);
	}
	
	public interface UseRedEnvelopeListener{
		/**
		 * 激活红包的接口
		 */
		public void postUseRedEnvelope(int p);
	}

}
