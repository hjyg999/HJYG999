package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/** 
 * ClassName: GoldBeanRuleActivity 
 * date: 2016-5-25 下午3:08:09 
 * remark:金豆规则
 * @author pyc
 */
public class GoldBeanRuleActivity extends BaseActivity implements OnClickListener{
	
	private ImageView img_rule;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goldbeanrule_layout);
		findViewandInit();
		
	}
	
	private void findViewandInit(){
		HeaderViewControler.setHeaderView(this, "金豆规则", this);
		img_rule = (ImageView) findViewById(R.id.img_rule);
		ViewParamsSetUtil.setViewParams(img_rule, 720, 1134, true);
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
	
	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

}
