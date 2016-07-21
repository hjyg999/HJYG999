package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ClassName: GoldBaoBeanIncomeActivity date: 2016-1-25 下午4:45:03
 * remark:金豆(收益)明细
 * 
 * @author pyc
 */
public class GoldBaoBeanIncomeActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private ImageView img_search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaobeanincome_activity);
		mActivity = this;
		findViewandInit();
	}

	private void findViewandInit() {
		// 标题栏

		header = new HeaderControler(this, true, false, "金豆明细",
				Constants.CheckAuthtoken(getBaseContext()));

		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		img_search = (ImageView) findViewById(R.id.img_search);
		img_search.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:// 返回
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.img_search:// 搜索
			intent = new Intent(mActivity, GoldBaoBeanSearchActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
