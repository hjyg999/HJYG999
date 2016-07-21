package com.md.hjyg.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.md.hjyg.R;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.LoginActivity;
import com.md.hjyg.activities.RegisterActivity;
import com.md.hjyg.activities.TaiPingYangCPICActivity;
import com.md.hjyg.entity.MoreInfoModel;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * ClassName: HomeNologinFragment date: 2016-3-18 下午2:13:16 remark:主页-没有登录的界面
 * 
 * @author pyc
 */
public class HomeNologinFragment extends Fragment implements OnClickListener {

	private TextView tv_tologin, tv_toregister, tv_xtrate, tv_bkrate;
	private HomeFragmentActivity mActivity;
	private Intent intent;
	private RelativeLayout rel_top;
	private boolean imgLoadover;
	private int[] Rids = { R.drawable.sz_bg, R.drawable.home_nologin_maney,
			R.drawable.home_nologin_time, R.drawable.laba_white };
	private Bitmap[] imgBitmaps;
	private ImageView img_maney, img_time, img_laba;
	private LinearLayout line_amount;

	private TextView tv_putoutmoney, tv_yxtime, tv_endDate;

	private TextSwitcher mTextSwitcher, mTextSwitcher_r;
	private Timer mTimer;
	private TimerTask mTimerTask;
	private List<String> strs;
	private List<String> strs_r;
	private int i = 0;
	private MoreInfoModel model;
	private TextView tv_tpy;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_nologin_layout, container,
				false);
		findViewandInit(v);
		Save.loadingImg(mHandler, getActivity(), Rids);
		model = mActivity.getWebManager().getMoreInfoModel();
		if (model != null) {
			setUIData();
		} else {
			mActivity.getWebManager().GetMoreInfoWebService(false);
		}
		return v;
	}

	private void findViewandInit(View v) {
		mActivity = (HomeFragmentActivity) getActivity();
		// 登录
		tv_tologin = (TextView) v.findViewById(R.id.tv_tologin);
		tv_tologin.setOnClickListener(this);
		// 登录
		tv_toregister = (TextView) v.findViewById(R.id.tv_toregister);
		tv_toregister.setOnClickListener(this);
		// 标题
		rel_top = (RelativeLayout) v.findViewById(R.id.rel_top);
		ViewParamsSetUtil.setViewHight(rel_top, 0.076f, getActivity());
		// 图片
		img_maney = (ImageView) v.findViewById(R.id.img_maney);
		img_time = (ImageView) v.findViewById(R.id.img_time);
		img_laba = (ImageView) v.findViewById(R.id.img_laba);
		// 成交额布局
		line_amount = (LinearLayout) v.findViewById(R.id.line_amount);
		// 文字信息
		tv_putoutmoney = (TextView) v.findViewById(R.id.tv_putoutmoney);
		tv_yxtime = (TextView) v.findViewById(R.id.tv_yxtime);
		tv_endDate = (TextView) v.findViewById(R.id.tv_endDate);
		// 利率比较
		tv_xtrate = (TextView) v.findViewById(R.id.tv_xtrate);
		tv_bkrate = (TextView) v.findViewById(R.id.tv_bkrate);
		tv_tpy = (TextView) v.findViewById(R.id.tv_tpy);
		tv_tpy.setOnClickListener(this);

		mTextSwitcher = (TextSwitcher) v.findViewById(R.id.mTextSwitcher);
		mTextSwitcher_r = (TextSwitcher) v.findViewById(R.id.mTextSwitcher_r);
		setTextSwitcherUI(mTextSwitcher, 0);
		setTextSwitcherUI(mTextSwitcher_r, 1);

	}

	public void setMoreInfoModel(MoreInfoModel model) {
		this.model = model;
		setUIData();
	}

	private void setUIData() {
		if (imgLoadover && model != null) {
			img_maney.setImageBitmap(imgBitmaps[1]);
			img_time.setImageBitmap(imgBitmaps[2]);
			img_laba.setImageBitmap(imgBitmaps[3]);

			addView();

			// 已发放收益、运营天数
			String interest = "";
			SpannableStringBuilder style = null;
			if (model.interest_yi == null || model.interest_yi.length() == 0
					|| model.interest_yi.equals("0")) {
				interest = model.interest_wy + "万" + model.interest_y + "元";
				style = new SpannableStringBuilder(interest);
				style.setSpan(new RelativeSizeSpan(0.6f),
						model.interest_wy.length(),
						model.interest_wy.length() + 1,
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				style.setSpan(new RelativeSizeSpan(0.6f),
						interest.length() - 1, interest.length(),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

			} else {
				interest = model.interest_yi + "亿" + model.interest_wy + "万"
						+ model.interest_y + "元";
				style = new SpannableStringBuilder(interest);
				style.setSpan(new RelativeSizeSpan(0.6f),
						model.interest_yi.length(),
						model.interest_yi.length() + 1,
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				style.setSpan(new RelativeSizeSpan(0.6f), (model.interest_yi
						+ "亿" + model.interest_wy).length(), (model.interest_yi
						+ "亿" + model.interest_wy).length() + 1,
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
				style.setSpan(new RelativeSizeSpan(0.6f),
						interest.length() - 1, interest.length(),
						Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

			}
			tv_putoutmoney.setText(style);

			tv_yxtime.setText(TextUtil.getRelativeSize(model.days + "天",
					(model.days + "天").length() - 1,
					(model.days + "天").length(), 0.6f));
			tv_endDate.setText(model.endDate);

			strs = new ArrayList<String>();
			strs_r = new ArrayList<String>();
			List<String> ScrollInfoList = model.ScrollInfoList;
			for (int i = 0; i < ScrollInfoList.size(); i++) {
				String[] s = ScrollInfoList.get(i).split(",");
				strs.add(s[0]);
				strs_r.add(s[1]);
			}
			if (strs.size() <= 1) {
				mTextSwitcher.setText(strs.get(0));
				mTextSwitcher_r.setText(strs_r.get(0));
			} else {
				startThread();
			}

			tv_bkrate.setText(model.interest_bank);
			tv_xtrate.setText(model.interest_xtb);

			tv_toregister.setText(model.HomeButtonInfo);

		}
	}

	private void setTextSwitcherUI(TextSwitcher mTextSwitcher, final int i) {
		mTextSwitcher.setFactory(new ViewFactory() {

			@Override
			public View makeView() {
				TextView tv = new TextView(mActivity);
				tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12.0F);
				tv.setTextColor(mActivity.getResources()
						.getColor(R.color.white));
				if (i == 1) {
					tv.setGravity(Gravity.RIGHT);
				}
				return tv;
			}
		});

		mTextSwitcher.setInAnimation(AnimationUtils.loadAnimation(mActivity,
				R.anim.anim_in));
		mTextSwitcher.setOutAnimation(AnimationUtils.loadAnimation(mActivity,
				R.anim.anim_out));
	}

	/**
	 * 开启轮播文字定时器
	 */
	private void startThread() {
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {

			mTimerTask = new TimerTask() {

				@Override
				public void run() {
					mHandler.sendEmptyMessage(1);

				}
			};
		} else {
			mTimerTask.cancel();
		}
		mTimer.schedule(mTimerTask, 0, 3000);

	}

	/**
	 * 平台累计总成交金额
	 */
	private void addView() {
		line_amount.removeAllViews();
		int width = imgBitmaps[0].getWidth();
		int height = imgBitmaps[0].getHeight();
		float fontScale = getResources().getDisplayMetrics().scaledDensity;
		float size = (width - 6) * 1.5f / fontScale * 0.9f;
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				width, height);
		params1.gravity = Gravity.CENTER;

		params1.leftMargin = 5;
		// 添加亿元
		String amount_yi = model.amount_yi;
		for (int i = 0; i < amount_yi.length(); i++) {
			line_amount.addView(getTextView(params1, size,
					amount_yi.substring(i, i + 1), true));
		}
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params2.leftMargin = 5;
		line_amount.addView(getTextView(params2, size * 0.58f, "亿", false));
		// 添加万元
		String amount_wy = model.amount_wy;
		if (amount_wy.length() < 4) {
			for (int i = 0; i < 4 - amount_wy.length(); i++) {
				line_amount.addView(getTextView(params1, size, "0", true));
			}
		}
		for (int i = 0; i < amount_wy.length(); i++) {
			line_amount.addView(getTextView(params1, size,
					amount_wy.substring(i, i + 1), true));
		}
		line_amount.addView(getTextView(params2, size * 0.58f, "万元", false));

	}

	/**
	 * 数字-有背景的
	 * 
	 * @param params
	 * @param size
	 * @return
	 */
	private View getTextView(LinearLayout.LayoutParams params, float size,
			CharSequence text, boolean haveBg) {
		TextView v = new TextView(getActivity());
		v.setLayoutParams(params);
		v.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		v.setText(text);
		if (haveBg) {
			v.setBackgroundResource(R.drawable.sz_bg);
			v.setTextColor(getResources().getColor(R.color.red));
			v.setGravity(Gravity.CENTER);
		} else {
			v.setTextColor(getResources().getColor(R.color.white));
		}
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tologin:// 登录
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			break;
		case R.id.tv_toregister:// 注册
			intent = new Intent(getActivity(), RegisterActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);

			break;
		case R.id.tv_tpy:// 太平洋
			intent = new Intent(getActivity(), TaiPingYangCPICActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			
			break;

		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				imgLoadover = true;
				imgBitmaps = (Bitmap[]) msg.obj;
				setUIData();
				break;
			case 1:
				mTextSwitcher.setText(strs.get(i));
				mTextSwitcher_r.setText(strs_r.get(i));
				i++;
				if (i == 2) {
					i = 0;
				}

			default:
				break;
			}
		};
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
		mTimer = null;
	}

}
