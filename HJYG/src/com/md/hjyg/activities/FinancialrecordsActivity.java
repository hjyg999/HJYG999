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
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;



/** 
 * ClassName: FinancialrecordsActivity 
 * date: 2015-11-11 下午1:16:06 
 * remark:活期宝资金记录界面
 * @author pyc
 */
public class FinancialrecordsActivity extends BaseTabActivity implements OnClickListener{
	
	LinearLayout lay_back_investment;
	HeaderControler header;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_financialrecords);
		AppController.listActivity.add(this);
		init(); 
		setTabs();
	}
	
	private void init() {
		header = new HeaderControler(this, true, false, "资金记录",
				Constants.CheckAuthtoken(getBaseContext()));
		
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
	}
	
	private void setTabs() {

		addTab("收益记录", PurchaseHistoryActivity.class);
		addTab("购买记录", BuyHistoryActivity.class);
		addTab("提取记录", WithdrawHistoryActivity.class);
	}

	private void addTab(String labelId, Class<?> c) {

		@SuppressWarnings("deprecation")
		TabHost tabHost = getTabHost();

		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		tabHost.getTabWidget().setDividerDrawable(null);

		@SuppressWarnings("deprecation")
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator_redpacket, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);

		tabHost.addTab(spec);
	}
	
	@Override
	public void onClick(View v) {
		if(v == lay_back_investment)
		{
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		}
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
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppController.listActivity.remove(this);
	}


}