package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * ClassName: GoldBaoExtractDetailsActivity date: 2016-1-27 上午9:13:40 remark:
 * 提取详情
 * 
 * @author pyc
 */
public class GoldBaoExtractDetailsActivity extends BaseActivity implements
		OnClickListener {

	HeaderControler header;
	private View header_bottom_line;
	/** 返回键 */
	private LinearLayout lay_back_investment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaoextractdetails_activity);
		findViewandInit();
	}

	private void findViewandInit() {
		header = new HeaderControler(this, true, false, "黄金宝",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setVisibility(View.GONE);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.lay_back_investment:
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
