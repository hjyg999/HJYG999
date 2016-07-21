package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.entity.MoreInfoModel;
import com.md.hjyg.layoutEntities.FormView;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.HomeWebServiceManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * ClassName: NoviceGuideActivity date: 2016-3-21 下午4:46:55 remark:新手指引
 * 
 * @author pyc
 */
public class NoviceGuideActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_toregister, tv_konwXTB, tv_putoutmoney, tv_yxtime,
			tv_endDate, safe_tv, pro_rich_tv, hight_interest_tv;
	private ScrollView mScrollView;
	private ImageButton to_top;
	private ImageView img_maney, img_time, img_novice_gu_1, img_novice_gu_2,
			img_novice_gu_3, img_novice_gu_4, img_novice_gu_5, img_novice_gu_6,
			img_novice_gu_7;
	private LinearLayout lin_gu_8;
	private int[] Rid = { R.drawable.novice_gu_1, R.drawable.novice_gu_2,
			R.drawable.novice_gu_3, R.drawable.novice_gu_4,
			R.drawable.novice_gu_5, R.drawable.novice_gu_6,
			R.drawable.novice_gu_7, R.drawable.novice_gu_8,
			R.drawable.novice_gu_9, R.drawable.home_nologin_maney,
			R.drawable.home_nologin_time, R.drawable.sz_bg };
	private Bitmap[] imgBitmaps;
	private HomeWebServiceManager serviceManager;
	private MoreInfoModel model;
	private LinearLayout line_amount;
	private FormView mFormView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_noviceguide_layout);

		findView();

		serviceManager = new HomeWebServiceManager(this, mHandler);
		serviceManager.GetMoreInfoWebService(false);

		Save.loadingImg(mHandler, this, Rid);
	}

	@SuppressLint("HandlerLeak")
	private void findView() {
		HeaderViewControler.setHeaderView(this, "新手指引", this);

		tv_konwXTB = (TextView) findViewById(R.id.tv_konwXTB);
		tv_putoutmoney = (TextView) findViewById(R.id.tv_putoutmoney);
		tv_yxtime = (TextView) findViewById(R.id.tv_yxtime);
		tv_endDate = (TextView) findViewById(R.id.tv_endDate);
		safe_tv = (TextView) findViewById(R.id.safe_tv);
		pro_rich_tv = (TextView) findViewById(R.id.pro_rich_tv);
		hight_interest_tv = (TextView) findViewById(R.id.hight_interest_tv);

		tv_toregister = (TextView) findViewById(R.id.tv_toregister);
		tv_toregister.setOnClickListener(this);
		if (Constants.GetResult_AuthToken(this).length() > 0) {
			tv_toregister.setVisibility(View.GONE);
		} else {
			tv_toregister.setVisibility(View.VISIBLE);
		}

		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		mScrollView.setVisibility(View.GONE);
		// ImageView
		img_maney = (ImageView) findViewById(R.id.img_maney);
		img_time = (ImageView) findViewById(R.id.img_time);
		img_novice_gu_1 = (ImageView) findViewById(R.id.img_novice_gu_1);
		img_novice_gu_2 = (ImageView) findViewById(R.id.img_novice_gu_2);
		img_novice_gu_3 = (ImageView) findViewById(R.id.img_novice_gu_3);
		img_novice_gu_4 = (ImageView) findViewById(R.id.img_novice_gu_4);
		img_novice_gu_5 = (ImageView) findViewById(R.id.img_novice_gu_5);
		img_novice_gu_6 = (ImageView) findViewById(R.id.img_novice_gu_6);
		img_novice_gu_7 = (ImageView) findViewById(R.id.img_novice_gu_7);

		lin_gu_8 = (LinearLayout) findViewById(R.id.lin_gu_8);
		to_top = (ImageButton) findViewById(R.id.to_top);
		to_top.setOnClickListener(this);

		line_amount = (LinearLayout) findViewById(R.id.line_amount);
		mFormView = (FormView) findViewById(R.id.mFormView);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				imgBitmaps = (Bitmap[]) msg.obj;
				setUI();
				break;
			case HomeWebServiceManager.MORE_INF:
				model = (MoreInfoModel) msg.obj;
				setUI();
				break;

			default:
				break;
			}
		};
	};

	private void setUI() {
		if (imgBitmaps != null && imgBitmaps.length == Rid.length
				&& model != null) {
			// 图片
			img_maney.setImageBitmap(imgBitmaps[9]);
			img_time.setImageBitmap(imgBitmaps[10]);
			img_novice_gu_1.setImageBitmap(imgBitmaps[0]);
			img_novice_gu_2.setImageBitmap(imgBitmaps[1]);
			img_novice_gu_3.setImageBitmap(imgBitmaps[2]);
			img_novice_gu_4.setImageBitmap(imgBitmaps[3]);
			img_novice_gu_5.setImageBitmap(imgBitmaps[4]);
			img_novice_gu_6.setImageBitmap(imgBitmaps[5]);
			img_novice_gu_7.setImageBitmap(imgBitmaps[6]);

			ViewParamsSetUtil.setViewHandW_lin(lin_gu_8,
					imgBitmaps[7].getHeight(), 0);
			// 数据
			tv_konwXTB.setText("       " + model.konwXTB);
			addView();
			mFormView.setData(model.interestList);
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
			tv_toregister.setText(model.RegistButtonInfo);

			mScrollView.setVisibility(View.VISIBLE);

			safe_tv.setText(model.whyDes_interest3);
			pro_rich_tv.setText(model.whyDes_interest2);
			hight_interest_tv.setText(model.whyDes_interest1);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_toregister:
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.to_top:
			mScrollView.scrollTo(0, 0);
			break;

		default:
			break;
		}
	}

	/**
	 * 平台累计总成交金额
	 */
	private void addView() {
		line_amount.removeAllViews();
		int width = imgBitmaps[11].getWidth();
		int height = imgBitmaps[11].getHeight();
		float fontScale = getResources().getDisplayMetrics().scaledDensity;
		float size = (width - 6) * 1.5f / fontScale * 0.9f;
		LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
				width, height);
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
		TextView v = new TextView(this);
		v.setLayoutParams(params);
		v.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		v.setText(text);
		if (haveBg) {
			v.setBackgroundResource(R.drawable.sz_bg);
			v.setGravity(Gravity.CENTER);
			v.setTextColor(getResources().getColor(R.color.yellow_FF9934));
		} else {
			v.setTextColor(getResources().getColor(R.color.white));
		}
		return v;
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

}
