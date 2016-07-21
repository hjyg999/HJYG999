package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.AccountLoginDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.ShadeProgressView;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * 账户资产
 */
public class TotalAssetsAccountActivity extends BaseActivity implements
		View.OnClickListener {

	private TextView tv_available_bal_value, tv_amount_freeze_value,
			tv_principal_amt_value, tv_proceeds_value, tv_total_income_value,
			tv_huoqibao_value;

	private AccountLoginDetails account_login_details;
	private String available_balance, amount_freeze, priciple_amount,
			proceeds_to_be_received, huoqibao_amount;
	private LinearLayout line_bg, line_mx;
	private ShadeProgressView shadeProgressView;
	private ImageView img_arrow1, img_arrow2;
	private RelativeLayout rel_myhuoqibao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_total_assets_account);
		findViews();
		init();
		GetWebserviceTotalAssetsAPI();
	}

	public void GetWebserviceTotalAssetsAPI() {

		dft.getTotalAssets(Constants.GetMember_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						account_login_details = (AccountLoginDetails) dft
								.GetResponseObject(response,
										AccountLoginDetails.class);

						setUIData();
					}

				}, null);

	}

	public void setUIData() {

		available_balance = account_login_details.BreakdownOfFundsModel.可用金额
				.toString();
		amount_freeze = account_login_details.BreakdownOfFundsModel.冻结金额
				.toString();
		priciple_amount = account_login_details.BreakdownOfFundsModel.待收本金
				.toString();
		proceeds_to_be_received = account_login_details.BreakdownOfFundsModel.待收利息
				.toString();
		// total_income_amount =
		// account_login_details.BreakdownOfFundsModel.账户金额.toString();
		huoqibao_amount = account_login_details.BreakdownOfFundsModel.活期宝总额
				.toString();
		if (huoqibao_amount == null || huoqibao_amount.equals("")) {
			huoqibao_amount = "0.0";
		}

		// 可用余额
		tv_available_bal_value.setText(Constants
				.StringToCurrency(available_balance));
		// 冻结金额
		tv_amount_freeze_value.setText(Constants
				.StringToCurrency(amount_freeze) + "元");
		// 待收本金
		tv_principal_amt_value.setText(Constants
				.StringToCurrency(priciple_amount) + "元");
		// 待收利息
		tv_proceeds_value.setText(Constants
				.StringToCurrency(proceeds_to_be_received) + "元");
		// 活期宝
		tv_huoqibao_value.setText(Constants.StringToCurrency(huoqibao_amount)
				+ "元");

		double total = Constants.StringToDouble(available_balance)
				+ Constants.StringToDouble(amount_freeze)
				+ Constants.StringToDouble(priciple_amount)
				+ Constants.StringToDouble(proceeds_to_be_received)
				+ Constants.StringToDouble(huoqibao_amount);

		tv_total_income_value.setText("资产总额："
				+ Constants.StringToCurrency(total + "") + "元");

		if (total == 0) {
			shadeProgressView.setCurrentCount(0f);
		} else {
			double available = Constants.StringToDouble(available_balance);
			// shadeProgressView.setCurrentCount(0.5f);
			shadeProgressView
					.setCurrentCount((float) available / (float) total);
		}
	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "账户资产", this);
		account_login_details = new AccountLoginDetails();
	}

	private void findViews() {
		// TextView
		tv_amount_freeze_value = (TextView) findViewById(R.id.tv_amount_freeze_value);
		tv_available_bal_value = (TextView) findViewById(R.id.tv_available_bal_value);
		tv_principal_amt_value = (TextView) findViewById(R.id.tv_principal_amt_value);
		tv_proceeds_value = (TextView) findViewById(R.id.tv_proceeds_value);
		tv_huoqibao_value = (TextView) findViewById(R.id.tv_huoqibao_value);
		tv_total_income_value = (TextView) findViewById(R.id.tv_total_income_value);

		rel_myhuoqibao = (RelativeLayout) findViewById(R.id.rel_myhuoqibao);
		rel_myhuoqibao.setOnClickListener(this);
		line_mx = (LinearLayout) findViewById(R.id.line_mx);
		line_mx.setOnClickListener(this);
		line_bg = (LinearLayout) findViewById(R.id.line_bg);
		int[] line_bg_p = Save.getScaleBitmapWangH(660, 1060);
		ViewParamsSetUtil.setViewHandW_lin(line_bg, line_bg_p[1], line_bg_p[0]);

		shadeProgressView = (ShadeProgressView) findViewById(R.id.shadeProgressView);
		int[] shadeProgressView_p = Save.getScaleBitmapWangH(466, 466);
		ViewParamsSetUtil.setViewHandW_lin(shadeProgressView,
				shadeProgressView_p[1], shadeProgressView_p[0]);

		img_arrow1 = (ImageView) findViewById(R.id.img_arrow1);
		img_arrow2 = (ImageView) findViewById(R.id.img_arrow2);
		int[] img_arrow_p = Save.getScaleBitmapWangH(24, 24);
		ViewParamsSetUtil.setViewHandW_lin(img_arrow1, img_arrow_p[1],
				img_arrow_p[0]);
		ViewParamsSetUtil.setViewHandW_rel(img_arrow2, img_arrow_p[1],
				img_arrow_p[0]);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.line_mx:
			startActivity(new Intent(this, MyInvestmentFragmentActivity.class));
			overTransition(2);
			break;
		case R.id.rel_myhuoqibao:
			startActivity(new Intent(this, MyHuoQibaoActivity.class));
			overTransition(2);
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}
}
