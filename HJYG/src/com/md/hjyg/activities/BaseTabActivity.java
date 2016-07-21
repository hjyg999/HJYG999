package com.md.hjyg.activities;

import android.app.TabActivity;

import com.umeng.analytics.MobclickAgent;

/** 
 * ClassName: BaseTabActivity 
 * date: 2015-10-31 下午3:56:48 
 * remark:
 * @author pyc
 */
@SuppressWarnings("deprecation")
public class BaseTabActivity extends TabActivity{
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);

	}

}
