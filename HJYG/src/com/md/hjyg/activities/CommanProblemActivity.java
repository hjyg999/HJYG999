package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * ClassName: CommanproblemActivity 
 * date: 2015-11-11 上午10:06:12 
 * remark:活期宝常见问题界面
 * 
 * @author pyc
 */
public class CommanProblemActivity extends BaseActivity implements
		OnClickListener {

	// Ui variables

	LinearLayout lay_back_investment;

	// LayoutEntities
	HeaderControler header;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_commonproblem_activity);
		findViews();
		init();

	}

	private void init() {

		header = new HeaderControler(this, true, false, "常见问题",
				Constants.CheckAuthtoken(getBaseContext()));
	}

	private void findViews() {
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);
		}
	}

	@Override
	public void onBackPressed() {

		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);

	}

}
