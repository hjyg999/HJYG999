package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.fragment.HomeGiftNewFragment;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

/** 
 * ClassName: ConsumptionFragmentActivity 
 * date: 2016-6-28 下午4:08:14 
 * remark:消费标分类列表
 * @author pyc
 */
public class ConsumptionFragmentActivity extends BaseFragmentActivity implements OnClickListener{
	
	private FragmentManager fm;
	private HomeGiftNewFragment mFragment;
	public static final String TYPE = "type";
	public static final String ID = "GroupType";
	private int GroupType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consumptionfragment_layout);

		findViewAndInit();
		setFragment();
	}

	private void findViewAndInit() {
		String type = getIntent().getStringExtra(TYPE);
		GroupType = getIntent().getIntExtra(ID, 0);
		if (type != null && type.length() > 0) {
			HeaderViewControler.setHeaderView(this, "消费-" + type, this);
		}else {
			HeaderViewControler.setHeaderView(this, "消费", this);
		}

	}

	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		mFragment = new HomeGiftNewFragment(GroupType);
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
