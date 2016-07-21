package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/** 
 * ClassName: LotteryStrategyActivity <br/> 
 * date: 2015-10-20 上午10:43:43 <br/> 
 * remark: 抽奖攻略
 * @author pyc
 */
public class LotteryStrategyActivity extends BaseActivity implements OnClickListener{
	
	HeaderControler header;
	LinearLayout lay_back_investment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_lottery_strategy_activity);
		
		findViewAndInit();
	}
	
	public void findViewAndInit() {
		header = new HeaderControler(this, true, false, "抽奖攻略",
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
