package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * ClassName: GoldBaoChoiceShopActivity date: 2016-1-28 下午1:39:55 
 * remark:选择金店
 * 
 * @author pyc
 */
public class GoldBaoChoiceShopActivity extends BaseActivity implements
		OnClickListener {

	HeaderControler header;
	private View header_bottom_line;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private Button btn_comfire;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaochoiceshop_activity);
		findViewandInit();
	}

	private void findViewandInit() {
		// 标题栏
		header = new HeaderControler(this, true, false, "选择金店",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		
		btn_comfire = (Button) findViewById(R.id.btn_comfire);
		btn_comfire.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			this.finish();
			overTransition(1);
			break;
		case R.id.btn_comfire:
			Intent intent = new Intent();
			intent.putExtra("shop", "");
			setResult(2, intent);
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
