package com.md.hjyg.activities;

import com.md.hjyg.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * ClassName: LotteryNoLoginObtainedActivity 
 * date: 2015-10-26 下午1:35:58
 * remark:
 * @author rw
 */
public class LotteryNoLoginObtainedActivity extends BaseActivity implements
		OnClickListener {
	
	private TextView tv_title, toRegistorLogin_tv;
	private LinearLayout lay_back_investment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lottery_noawards_activity);
		findViews();
		init();
	}
	
	public void findViews(){
		tv_title = (TextView) findViewById(R.id.tv_title);
		toRegistorLogin_tv = (TextView) findViewById(R.id.toRegistorLogin_tv);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
	}
	
	public void init() {
		tv_title.setText("我的奖品");
		lay_back_investment.setOnClickListener(this);
		toRegistorLogin_tv.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lay_back_investment:
			this.finish();
			overTransition(1);
			break;
		case R.id.toRegistorLogin_tv:
//			Intent investment = new Intent(this,
//					InvestmentActivity.class);
			Intent investment = new Intent(this,
					HomeFragmentActivity.class);
//			investment.putExtra("DingtoubaoProjects", false);
//			investment.putExtra("LatestProjects", false);
			investment.putExtra("tab", 1);
			startActivity(investment);
			overTransition(2);
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
