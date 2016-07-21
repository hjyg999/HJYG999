package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * 我的投资
 */
public class MyInvestmentActivity extends BaseTabActivity implements
		View.OnClickListener {
	// UI variables
	TextView tv_title;
	LinearLayout lay_back_investment;
	Button btn_sign_success;
	// LayoutEntities
	HeaderControler header;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_my_investment);
		AppController.listActivity.add(this);
		findViews();
		init();
		setTabs();
		lay_back_investment.setOnClickListener(this);
	}

	private void init() {

		header = new HeaderControler(this, true, false, "我的投资",
				Constants.CheckAuthtoken(getBaseContext()));
	}

	private void setTabs() {
		addTab("持有中", HoldInActivity.class);
		addTab("投标中", BiddingActivity.class);
		addTab("已还款 ", RepaidActivity.class);
	}

	private void findViews() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
	}

	@Override
	public void onClick(View v) {

		if (v == lay_back_investment) {
//			Intent back = new Intent(MyInvestmentActivity.this,
//					AccountActivity.class);
//			startActivity(back);
			this.finish();
        	overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		}

	}

	@SuppressWarnings("deprecation")
	private void addTab(String labelId, Class<?> c) {
		TabHost tabHost = getTabHost();

		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		tabHost.getTabWidget().setDividerDrawable(null);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator_redpacket, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);

		tabHost.addTab(spec);
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppController.listActivity.remove(this);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		}
		return super.dispatchKeyEvent(event);
	}
}
