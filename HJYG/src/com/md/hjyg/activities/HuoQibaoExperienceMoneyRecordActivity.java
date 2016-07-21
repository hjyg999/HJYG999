package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.fragment.HuoQibaoTyjFinancialFragment;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * ClassName: HuoQibaoExperienceMoneyRecordActivity date: 2015-12-16 上午9:18:53
 * remark:活期宝体验金--资金记录
 * 
 * @author pyc
 */
public class HuoQibaoExperienceMoneyRecordActivity extends BaseFragmentActivity
		implements OnClickListener {

	private FragmentManager fm;
	private HuoQibaoTyjFinancialFragment mFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_experiencemoneyrecord_activity);

		findViewAndInit();
		setFragment();
	}

	private void findViewAndInit() {
		HeaderViewControler.setHeaderView(this, "资金记录", this);

	}

	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		mFragment = new HuoQibaoTyjFinancialFragment();
		transaction.add(R.id.id_content, mFragment);
		transaction.commit();
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
