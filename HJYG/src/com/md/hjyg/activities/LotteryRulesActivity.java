package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/** 
 * ClassName: LotteryRecordsActivity <br/> 
 * date: 2015-10-19 下午4:38:58 <br/> 
 * remark:奖励规则页面
 * @author rw
 */
public class LotteryRulesActivity extends BaseActivity implements OnClickListener{
	
	HeaderControler header;
	LinearLayout lay_back_investment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lottery_rules_activity);
		
		findViewAndInit();
	}
	
	public void findViewAndInit() {
		header = new HeaderControler(this, true, false, "活动规则",
				Constants.CheckAuthtoken(getBaseContext()));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		}
		
	}
	
	
	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
		
	}

}
