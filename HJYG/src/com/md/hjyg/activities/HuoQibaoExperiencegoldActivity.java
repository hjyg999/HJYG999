package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ExperienceDetailsModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: HuoQibaoExperiencegoldActivity date: 2015-12-15 下午4:16:17 remark:
 * 活期宝体验金界面
 * 
 * @author pyc
 */
public class HuoQibaoExperiencegoldActivity extends BaseActivity implements
		OnClickListener {

	/** 跳转按钮 */
	private LinearLayout  income_record,line,lin_bg;
	private RelativeLayout money_record;
	private Button buy_huoqibao;
	private TextView usable_amount, income_amount,total_amount ,freeze_amount,buy_amount,day_income;
	private Intent intent;
	private ExperienceDetailsModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_huoqibao_experiencegold_activity);

		findViewAndInit();
		getWebservice();
	}

	private void findViewAndInit() {
        HeaderViewControler.setHeaderView(this, "活期体验金", this);
		money_record = (RelativeLayout) findViewById(R.id.money_record);
		money_record.setOnClickListener(this);

		income_record = (LinearLayout) findViewById(R.id.income_record);
		income_record.setOnClickListener(this);

		usable_amount = (TextView) findViewById(R.id.usable_amount);
		income_amount = (TextView) findViewById(R.id.income_amount);
		total_amount = (TextView) findViewById(R.id.total_amount);
		freeze_amount = (TextView) findViewById(R.id.freeze_amount);
		buy_amount = (TextView) findViewById(R.id.buy_amount);
		day_income = (TextView) findViewById(R.id.day_income);
		// 购买活期宝
		buy_huoqibao = (Button) findViewById(R.id.buy_huoqibao);
		buy_huoqibao.setOnClickListener(this);
		
		line = (LinearLayout) findViewById(R.id.line);

		setViewHight(buy_huoqibao, 0.08f);
		setViewHight(line, 0.08f);
		lin_bg = (LinearLayout) findViewById(R.id.lin_bg);
		ViewParamsSetUtil.setViewParams(lin_bg, 660, 980, true);
	}

	/** 获取活期宝体验金详情 */
	private void getWebservice() {
		dft.getNetInfoById(Constants.ExperienceDetails_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						model = (ExperienceDetailsModel) dft.GetResponseObject(
								jsonObject, ExperienceDetailsModel.class);
						if (model != null) {
							setUIData(model);
						}
					}
				});

	}
	
	/**设置数据*/
	private void setUIData(ExperienceDetailsModel model){
		usable_amount.setText(Constants.StringToCurrency(model.LeaveExperienceAmount +"") + "元");
		income_amount.setText(Constants.StringToCurrency(model.CumulativeExperienceInterest +""));
		total_amount.setText(Constants.StringToCurrency(model.ExperienceAmountTotal +"") + "元");
		freeze_amount.setText(Constants.StringToCurrency(model.FrozenExperienceAmount +"") + "元");
		buy_amount.setText(Constants.StringToCurrency(model.SingleExperience +"") + "元");
		day_income.setText("昨日收益：" +Constants.StringToCurrency(model.YesterdayExperienceInterest +"") + "元");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.money_record:// 资金记录
			intent = new Intent(this,
					HuoQibaoExperienceMoneyRecordActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.income_record:// 收益记录
			intent = new Intent(this, HuoQibaoExperiencePurchaseActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.buy_huoqibao:// 购买活期宝
			intent = new Intent(this, MyHuoQibaoActivity.class);
			startActivity(intent);
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
