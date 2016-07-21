package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WithdrawDescriptionActivity extends BaseActivity implements
		View.OnClickListener {
	TextView tv_title;
	LinearLayout lay_back_investment;
	// LayoutEntities
	HeaderControler header;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_withdraw_description);
		findViews();
		init();
		lay_back_investment.setOnClickListener(this);
	}

	private void init() {

		header = new HeaderControler(this, true, false, "提现说明",
				Constants.CheckAuthtoken(getBaseContext()));
	}

	private void findViews() {
		tv_title = (TextView) findViewById(R.id.tv_title);

		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
			Intent next = new Intent(WithdrawDescriptionActivity.this,
					WithdrawApplicationActivity.class);
			startActivity(next);
			finish();
		}

	}
}
