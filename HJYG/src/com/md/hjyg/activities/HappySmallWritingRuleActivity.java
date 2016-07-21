package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ClassName: HappySmallWritingRuleActivity date: 2016-7-13 上午9:40:19
 * remark:快乐微文规则页面
 * 
 * @author rw
 */
public class HappySmallWritingRuleActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_happysmallwritingrule_layout);
		findViewById();
	}

	private void findViewById() {
		HeaderViewControler.setHeaderView(this, "活动规则", this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		default:
			break;
		}
	}

}
