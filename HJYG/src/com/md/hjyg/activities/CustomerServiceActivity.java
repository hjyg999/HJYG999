package com.md.hjyg.activities;

import org.json.JSONException;
import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * 联系我们
 */
public class CustomerServiceActivity extends BaseActivity implements
		View.OnClickListener {

	private LinearLayout lay_back_investment;
	HeaderControler header;

	private TextView tv_contact;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_customer_service);
		findViews();
		init();
		lay_back_investment.setOnClickListener(this);
		getContractUsInfo();
	}

	private void init() {
		header = new HeaderControler(this, true, false, "联系我们",
				Constants.CheckAuthtoken(getBaseContext()));
	}

	private void findViews() {
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		tv_contact = (TextView) findViewById(R.id.tv_contact);
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
			CustomerServiceActivity.this.finish();
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);
		}
	}

	@Override
	public void onBackPressed() {
		CustomerServiceActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	private void getContractUsInfo() {
		dft.getNetInfoById(Constants.GetContractUsInfo_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							String contractUsInfo = (String) jsonObject.get("ContractUsInfo");
							tv_contact.setText(contractUsInfo);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
