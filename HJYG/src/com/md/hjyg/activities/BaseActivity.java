package com.md.hjyg.activities;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.Interface.IValidator;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utils.ScreenUtils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

/**
 * activity基础类
 */
public class BaseActivity extends Activity {

	// List of validators
	private ArrayList<IValidator> validatorRuleSet = new ArrayList<IValidator>();

	protected DataFetchService dft;

	public void addvalidator(IValidator validator) {
		validatorRuleSet.add(validator);

	}

	public boolean validateForm() {

		for (IValidator validator : validatorRuleSet) {
			if (!validator.validate()) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * // Remove title bar
		 * this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Remove
		 * notification bar
		 * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		AppController.listActivity.add(this);
		dft = new DataFetchService(this);
	}

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

	@Override
	protected void onDestroy() {
		AppController.listActivity.remove(this);
		super.onDestroy();
	}

	/**
	 * 定义窗口进出方式
	 * 
	 * @param sty
	 *            int型 1为左进右出，2为右进左出
	 */
	public void overTransition(int sty) {
		switch (sty) {
		case 1:
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);
			break;
		case 2:
			overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
			break;
		default:
			break;
		}
	}

	/**
	 * 设置控件的高度
	 * 
	 * @param v
	 *            要设置的控件
	 * @param weight
	 *            占屏幕的比例
	 */
	protected void setViewHight(View v, float weight) {
		if (weight > 1 || v == null) {
			return;
		}
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) v
				.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenHeight(getBaseContext())*weight);
		v.setLayoutParams(params);
	}
	
	public DataFetchService getDft(){
		return dft;
	}

}
