package com.md.hjyg.activities;

import com.md.hjyg.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/** 
 * ClassName: LotteryRecordsActivity <br/> 
 * date: 2015-10-19 下午4:38:58 <br/> 
 * remark:奖励记录页面
 * @author rw
 */
public class LotteryRecordsActivity extends BaseActivity implements OnClickListener {
	
	private TextView tv_title;
	private LinearLayout lay_back_investment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lottery_recordslist_activity);
		findViews();
		init();
	}
	
	public void findViews(){
		tv_title = (TextView) findViewById(R.id.tv_title);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
	}
	
	public void init() {
		tv_title.setText("获奖名单");
		lay_back_investment.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.lay_back_investment:
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
			break;

		default:
			break;
		}
		
	}
	
	
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

}
