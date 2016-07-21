package com.md.hjyg.activities;

import android.app.ListActivity;

import com.umeng.analytics.MobclickAgent;

/** 
 * ClassName: BaseListActivity 
 * date: 2015-10-31 下午3:53:51 
 * remark:
 * @author pyc
 */
public class BaseListActivity extends ListActivity{
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
