package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.utils.AppUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 版本更新界面
 */
public class VersionUpdateActivity extends BaseActivity implements
		View.OnClickListener {

	private TextView tip1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_version_update);
		findViews();
		init();
	}

	private void findViews() {
		tip1 = (TextView) findViewById(R.id.tip1);
	}

	private void init() {
		tip1.setText("1,您已更新至最新版本" + AppUtil.getVerName(this));
	}
	
	@Override
	public void onClick(View v) {

	}

}
