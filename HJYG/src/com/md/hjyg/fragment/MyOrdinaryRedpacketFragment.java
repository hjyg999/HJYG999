package com.md.hjyg.fragment;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.HuoQibaoBuyActivity;
import com.md.hjyg.adapter.MyBaseAdapter;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.entity.OrdinaryRedpacketList;
import com.md.hjyg.entity.OrdinaryRedpacketList.RedEnvelopePgedListModel;
import com.md.hjyg.entity.RedEnvelopeType;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;

/**
 * ClassName: MyOrdinaryRedpacketFragment date: 2016-7-5 下午4:25:32
 * remark:我的红包-普通红包fragment
 * 
 * @author pyc
 */
public class MyOrdinaryRedpacketFragment extends
		LoadMoreListFragment<RedEnvelopePgedListModel> {

	@Override
	protected void setInit() {
		super.setInit();
		isneedLoadMore = false;
		dialog.initDialog();
	}

	@Override
	protected void getWebData(String ftype, final int page) {
		dft.getNetInfoById(Constants.GetRedEnvelope_ADD_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						OrdinaryRedpacketList ordinaryRedpacketList = (OrdinaryRedpacketList) dft
								.GetResponseObject(response,
										OrdinaryRedpacketList.class);
						if (page == startPage) {
							setData(false, ordinaryRedpacketList.List);
						} else {
							setData(true, null);
						}
					}
				});
	}

	@Override
	protected MyBaseAdapter<RedEnvelopePgedListModel> getAdapter(
			List<RedEnvelopePgedListModel> lists, Activity activity) {
		return new OrdinaryRedpacketAdapter(lists, activity);
	}

	public void postUseRedEnvelope(final int p) {
		dialog.initDialog();
		dft.postUseRedEnvelope(lists.get(p).id, lists.get(p).pid,
				Constants.GetUseRedEnvelope_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dialog.dismiss();
						Notification notification = (Notification) dft
								.GetResponseObject(response, Notification.class);
						if (notification.ProcessResult == 1) {
							page = startPage;
							// 刷新
							getWebData("", p);

							AppController.AccountInfIsChange = true;
							int type = Integer.parseInt(lists.get(p).type);
							if (type == RedEnvelopeType.现金红包) {
								Intent intent = new Intent(getActivity(),
										HomeFragmentActivity.class);
								intent.putExtra("tab", 2);
								getActivity().startActivity(intent);
								getActivity().overridePendingTransition(
										R.anim.trans_lift_in,
										R.anim.trans_right_out);
							} else {
								startActivity(new Intent(getActivity(),
										HuoQibaoBuyActivity.class));
								getActivity().overridePendingTransition(
										R.anim.trans_right_in,
										R.anim.trans_lift_out);
							}
						} else {
							dialogManager.initOneBtnDialog(
									notification.ProcessMessage, null);
						}

					}
				}, null);

	}

	/**
	 * 普通红包list适配器
	 */
	public class OrdinaryRedpacketAdapter extends
			MyBaseAdapter<RedEnvelopePgedListModel> {
		private float textSize;
		private int[] amount_WHs;
		private int[] imgtype_WHs;
		private int bitW, size;

		public OrdinaryRedpacketAdapter(List<RedEnvelopePgedListModel> lists,
				Context context) {
			super(lists, context);
			amount_WHs = Save.getScaleBitmapWangH(144, 136);
			imgtype_WHs = Save.getScaleBitmapWangH(36, 36);
			float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
			textSize = amount_WHs[0] * 0.7f / (6) * 1.5f / fontScale;
			bitW = imgtype_WHs[1];
			size = (int) (ScreenUtils.sp2px(context, 14) * 1.4);
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HoldView holdView = null;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.adapter_listredpack_redp, null);
				holdView = new HoldView(convertView);
				convertView.setTag(holdView);
			} else {
				holdView = (HoldView) convertView.getTag();

			}
			if (position == lists.size() - 1) {
				convertView.setPadding(0, 30, 0, 30);
			} else {
				convertView.setPadding(0, 30, 0, 0);
			}

			RedEnvelopePgedListModel model = lists.get(position);
			String typetemp = "";
			int icon = 0;
			int type = Integer.parseInt(model.type);
			if (type == RedEnvelopeType.体验金红包) {
				typetemp = "体验金红包";
				icon = R.drawable.redpacket_tyj;
			} else if (type == RedEnvelopeType.生日红包) {
				typetemp = "生日红包";
				icon = R.drawable.redpacket_tyj;
			} else if (type == RedEnvelopeType.现金红包) {
				icon = R.drawable.redpacket_xj;
				typetemp = "现金红包";
			} else if (type == RedEnvelopeType.充值红包) {
				icon = R.drawable.redpacket_tz;
				typetemp = "充值红包";
			} else if (type == RedEnvelopeType.注册红包) {
				icon = R.drawable.redpacket_tz;
				typetemp = "注册红包";
			} else {
				icon = R.drawable.redpacket_tz;
				typetemp = "投资红包";
			}
			holdView.tv_type.setText(typetemp);
			holdView.img.setImageResource(icon);
			String amounttemp = "0";
			int dType = Integer.parseInt(model.dType);
			if (dType == 0 || dType == 2) {
				String[] Amount = null;
				if (model.AmountInfo != null) {
					Amount = model.AmountInfo.split("-");
				}
				String[] Experience = null;
				if (model.ExperienceAmountInfo != null) {
					Experience = model.ExperienceAmountInfo.split("-");
				}
				String[] LotteryActivityRedEnvelopeAmount = null;
				if (model.LotteryActivityRedEnvelopeAmount != null)
					LotteryActivityRedEnvelopeAmount = model.LotteryActivityRedEnvelopeAmount
							.split("-");

				String[] LotteryActivityExperienceAmount = null;
				if (model.LotteryActivityExperienceAmount != null)
					LotteryActivityExperienceAmount = model.LotteryActivityExperienceAmount
							.split("-");
				if (Amount != null
						&& (Double.parseDouble(Amount[0]) > 0 || Double
								.parseDouble(Amount[1]) > 0)) {
					amounttemp = Amount[1];
				} else if (Experience != null
						&& (Double.parseDouble(Experience[0]) > 0 || Double
								.parseDouble(Experience[1]) > 0)) {
					amounttemp = Experience[1];
				} else if (LotteryActivityRedEnvelopeAmount != null
						&& (Double
								.parseDouble(LotteryActivityRedEnvelopeAmount[0]) > 0 || Double
								.parseDouble(LotteryActivityRedEnvelopeAmount[1]) > 0)) {
					amounttemp = LotteryActivityRedEnvelopeAmount[1];
				} else if (LotteryActivityExperienceAmount != null
						&& (Double
								.parseDouble(LotteryActivityExperienceAmount[0]) > 0 || Double
								.parseDouble(LotteryActivityExperienceAmount[1]) > 0)) {
					amounttemp = LotteryActivityExperienceAmount[1];
				}
				// if (amounttemp.split(".")[0] != null) {
				// amounttemp = amounttemp.split(".")[0];
				// }

			} else {
				if (Double.parseDouble(model.Amount) > 0) {
					amounttemp = model.Amount;
				} else if (Double.parseDouble(model.ExperienceAmount) > 0) {
					amounttemp = model.ExperienceAmount;
				}
				// if (amounttemp.split(".")[0] != null) {
				// amounttemp = amounttemp.split(".")[0];
				// }
			}
			if (Double.parseDouble(amounttemp) > 100000) {
				holdView.tv_amount.setText(Constants.StringToCurrency(
						Double.parseDouble(amounttemp) / 10000 + "").replace(".00",
						"")
						+ "万元");
			} else {
				holdView.tv_amount.setText(Constants.StringToCurrency(
						amounttemp).replace(".00", "")
						+ "元");
			}

			holdView.tv_term.setText("有效期：" + model.startTime + "-"+ model.endTime);
			holdView.tv_condition.setText("兑换要求：" + model.Remark);
			holdView.tv_timetype.setTag(position);
			if (dType == 1) {// 已使用
				holdView.img_usetype.setVisibility(View.GONE);
				holdView.tv_timetype.setText("已使用");
				holdView.tv_timetype
						.setBackgroundResource(R.drawable.bg_mra_redq_bg);
				holdView.tv_timetype.setTextColor(getResources().getColor(
						R.color.white));
				holdView.tv_timetype.setEnabled(false);

			} else if (dType == 2) {// 已过期
				holdView.img_usetype.setVisibility(View.GONE);
				holdView.tv_timetype.setText("已过期");
				holdView.tv_timetype
						.setBackgroundResource(R.drawable.bg_mra_graye9_bg);
				holdView.tv_timetype.setTextColor(getResources().getColor(
						R.color.gray_sq));
				holdView.tv_timetype.setEnabled(false);

			} else if (dType == 0) {// 可使用
				holdView.img_usetype.setVisibility(View.VISIBLE);
				holdView.tv_timetype
						.setBackgroundResource(R.drawable.bg_mra_reds_bg);
				holdView.tv_timetype.setTextColor(getResources().getColor(
						R.color.white));
				holdView.tv_timetype.setEnabled(true);
				if (type == RedEnvelopeType.体验金红包
						|| type == RedEnvelopeType.生日红包
						|| type == RedEnvelopeType.现金红包) {
					holdView.tv_timetype.setText("立即领取");
				} else {
					holdView.tv_timetype.setText("立即投资");
				}
				holdView.tv_timetype.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int p = (Integer) v.getTag();
						int type = Integer.parseInt(lists.get(p).type);
						if (type == RedEnvelopeType.体验金红包
								|| type == RedEnvelopeType.生日红包
								|| type == RedEnvelopeType.现金红包) {
							postUseRedEnvelope(p);
						} else {
							Intent intent = new Intent(getActivity(),
									HomeFragmentActivity.class);
							intent.putExtra("tab", 0);
							getActivity().startActivity(intent);
							getActivity().overridePendingTransition(
									R.anim.trans_lift_in,
									R.anim.trans_right_out);
						}
					}
				});
			}

			return convertView;
		}

		class HoldView {
			FrameLayout mFrameLayout;
			ImageView img_usetype, img;
			TextView tv_amount, tv_type, tv_term, tv_condition, tv_timetype;
			LinearLayout lin_amount;

			public HoldView(View v) {
				mFrameLayout = (FrameLayout) v.findViewById(R.id.mFrameLayout);
				img_usetype = (ImageView) v.findViewById(R.id.img_usetype);
				tv_amount = (TextView) v.findViewById(R.id.tv_amount);
				tv_type = (TextView) v.findViewById(R.id.tv_type);
				tv_term = (TextView) v.findViewById(R.id.tv_term);
				tv_timetype = (TextView) v.findViewById(R.id.tv_timetype);
				tv_condition = (TextView) v.findViewById(R.id.tv_condition);
				lin_amount = (LinearLayout) v.findViewById(R.id.lin_amount);
				img = (ImageView) v.findViewById(R.id.img);

				ViewParamsSetUtil.setViewParams(mFrameLayout, 660, 210, true);
				ViewParamsSetUtil.setViewParams(img_usetype, 114, 114);
				ViewParamsSetUtil.setViewHandW_rel(lin_amount, amount_WHs[1],
						amount_WHs[0]);
				tv_amount.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
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

			}
		}

	}

}