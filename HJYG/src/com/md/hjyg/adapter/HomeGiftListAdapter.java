package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.activities.NoviceExclusiveActivity;
import com.md.hjyg.activities.NoviceExclusiveNewActivity;
import com.md.hjyg.entity.NoviceLoan;
import com.md.hjyg.entity.NoviceLoan.LoanPicture;
import com.md.hjyg.utility.LruCacheWebBitmapManager;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: HomeGiftListAdapter date: 2016-4-6 下午3:55:11 remark:
 * 
 * @author pyc
 */
public class HomeGiftListAdapter extends MyBaseAdapter<NoviceLoan> {

	private BaseFragmentActivity mActivity;
	private LruCacheWebBitmapManager lruCacheBitmap;
	private int showType;
	private int[] Bitmap_wh;

	public HomeGiftListAdapter(List<NoviceLoan> lists, Context context,
			LruCacheWebBitmapManager lruCacheBitmap, int[] Bitmap_wh) {
		super(lists, context);
		this.lruCacheBitmap = lruCacheBitmap;
		this.Bitmap_wh = Bitmap_wh;
		this.mActivity = (BaseFragmentActivity) context;
	}

	public HomeGiftListAdapter(List<NoviceLoan> lists, Context context,
			LruCacheWebBitmapManager lruCacheBitmap, int[] Bitmap_wh, int showType) {
		super(lists, context);
		this.lruCacheBitmap = lruCacheBitmap;
		this.Bitmap_wh = Bitmap_wh;
		this.mActivity = (BaseFragmentActivity) context;
		this.showType = showType;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHold hold = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_giftlist_layout,
					null);
			hold = new ViewHold(convertView);
			convertView.setTag(hold);
		} else {
			hold = (ViewHold) convertView.getTag();
		}

		NoviceLoan model = lists.get(position);
		if (model.loanPicture != null) {
			for (int i = 0; i < model.loanPicture.size(); i++) {
				if (model.loanPicture.get(i).Type == LoanPicture.移动端列表图_10) {
					hold.img.setImageBitmap(lruCacheBitmap.getBitmap(
							mActivity.getDft(), model.loanPicture.get(i).URL,
							this));
				}
			}
		}
		// 单位
		String LoanDateType = Constants.getLoanTermType(model.LoanDateType);
		// 投资期限
		String LoanTerm = model.LoanTerm + "";
		switch (showType) {
		case 0:
			hold.rel_bot.setVisibility(View.GONE);
			hold.lin_bot.setVisibility(View.VISIBLE);
			// 投资金额
			setTextSize(hold.tv_InvestAmount,
					Constants.StringToCurrency(model.InvestAmount + "")
							.replace(".00", "") + "元", 1);
			// 剩余金额
			String InvestCount = "";
			if (model.AmountDifference > 10000) {
				InvestCount = Constants.StringToCurrency(model.AmountDifference
						/ 10000 + "");
				InvestCount = InvestCount
						.substring(0, InvestCount.length() - 1) + "万";
			} else {
				InvestCount = Constants.StringToCurrency(
						(int) model.AmountDifference + "").replace(".00", "")
						+ "元";
			}
			setTextSize(hold.tv_InvestCount, InvestCount, 1);

			// 期限
			setTextSize(hold.tv_LoanInterest, LoanTerm + LoanDateType,
					LoanDateType.length());
			// 利率
			setTextSize(hold.tv_InvestRate, model.LoanRate + "%", 1);
			// 购物卡金额
			setTextSize(hold.tv_InvestAwad, (int) model.Price + "元", 1);

			hold.line_novic.setTag(position);
			hold.line_novic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					Intent intent = new Intent(mActivity,
							NoviceExclusiveNewActivity.class);
					intent.putExtra("ActivityId", lists.get(position).Id);
					mActivity.startActivity(intent);
					mActivity.overridePendingTransition(R.anim.trans_right_in,
							R.anim.trans_lift_out);
				}
			});
			break;
		case 1:
			hold.rel_bot.setVisibility(View.VISIBLE);
			hold.lin_bot.setVisibility(View.GONE);
			// 投资金额
			String InvestAmount = Constants.StringToCurrency(
					model.InvestAmount + "").replace(".00", "");
			hold.tv_invest_vale.setText(TextUtil.getRelativeSize(InvestAmount
					+ "元", 0, InvestAmount.length(), 1.35f));
			// 项目期限
			hold.tv_invest_term.setText(TextUtil.getRelativeSize(LoanTerm
					+ LoanDateType, 0, LoanTerm.length(), 1.35f));
			// 预期收益
			String LoanInterest = Constants.StringToCurrency(
					model.LoanInterest + "").replace(".00", "");
			hold.tv_invest_income.setText(TextUtil.getRelativeSize(LoanInterest
					+ "元", 0, LoanInterest.length(), 1.35f));

			hold.line_novic.setTag(position);
			hold.line_novic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					Intent intent = null;
//					int type = lists.get(position).Type;
//					if (type == 0) {//新手标
//
//						intent = new Intent(mActivity,
//								NoviceExclusiveNewActivity.class);
//					} else if (type == 3 || type == 2) {//消费标和礼品标
//
//						intent = new Intent(mActivity,
//								NoviceExclusiveNewActivity.class);
//					} else {
//
//						intent = new Intent(mActivity,
//								NoviceExclusiveNewActivity.class);
//					}
					intent = new Intent(mActivity,
							NoviceExclusiveNewActivity.class);
					intent.putExtra("position", position);
					intent.putExtra("GroupType", lists.get(position).GroupType);
					intent.putExtra("ActivityId", lists.get(position).Id);
					intent.putExtra(NoviceExclusiveActivity.showViewPager,
							false);

					mActivity.startActivity(intent);
					mActivity.overridePendingTransition(R.anim.trans_right_in,
							R.anim.trans_lift_out);
				}
			});
			break;

		default:
			break;
		}
		return convertView;
	}

	private void setTextSize(TextView v, String str, int idx) {
		v.setText(TextUtil.getRelativeSize(str, str.length() - idx,
				str.length(), 0.8f));
	}

	class ViewHold {
		LinearLayout line_novic, lin_bot;
		RelativeLayout rel_bot;
		TextView tv_InvestAmount, tv_LoanInterest, tv_InvestRate,
				tv_InvestCount, tv_InvestAwad;
		ImageView img;
		TextView tv_invest_vale, tv_invest_term, tv_invest_income;

		public ViewHold(View v) {
			line_novic = (LinearLayout) v.findViewById(R.id.line_novic);
			lin_bot = (LinearLayout) v.findViewById(R.id.lin_bot);
			tv_InvestAmount = (TextView) v.findViewById(R.id.tv_InvestAmount);
			tv_LoanInterest = (TextView) v.findViewById(R.id.tv_LoanInterest);
			tv_InvestRate = (TextView) v.findViewById(R.id.tv_InvestRate);
			tv_InvestCount = (TextView) v.findViewById(R.id.tv_InvestCount);
			tv_InvestAwad = (TextView) v.findViewById(R.id.tv_InvestAwad);

			rel_bot = (RelativeLayout) v.findViewById(R.id.rel_bot);
			tv_invest_vale = (TextView) v.findViewById(R.id.tv_invest_vale);
			tv_invest_term = (TextView) v.findViewById(R.id.tv_invest_term);
			tv_invest_income = (TextView) v.findViewById(R.id.tv_invest_income);

			img = (ImageView) v.findViewById(R.id.img);
		    ViewParamsSetUtil.setViewHandW_lin(img, Bitmap_wh[1],Bitmap_wh[0]);
			// line_novic.setPadding(20 - (int)(bitmap.getWidth()*0.02866f), 20,
			// 20, 20);
			// lin_bot.setPadding( (int)(bitmap.getWidth()*0.02866f)-2, 0, 0,
			// 0);
			line_novic.setPadding(20, 20, 20, 20);
			lin_bot.setPadding(0, 0, 0, 0);
		}
	}

}
