package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: GlodBaoSelloutActivity date: 2016-1-22 下午3:03:33 remark: 卖出黄金
 * 
 * @author pyc
 */
public class GlodBaoSelloutActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	/**显示黄金价格*/
	private TextView tv_goldPrice;
	/**显示持有黄金*/
	private TextView tv_haveGold;
	private TextView tv_sumAcount;
	private TextView tv_feel;
	/**输入卖出黄金克数的控件*/
	private EditText ed_sellamount;
	private EditText ed_Password;
	private Button btn_sellout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.glodbaosellout_activity);
		mActivity = this;
		findViewandInit();
	}

	private void findViewandInit() {
		// 标题栏
		header = new HeaderControler(this, true, false, "卖出黄金",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		//黄金价格
		tv_goldPrice = (TextView) findViewById(R.id.tv_goldPrice);
		tv_haveGold = (TextView) findViewById(R.id.tv_haveGold);
		ed_sellamount = (EditText) findViewById(R.id.ed_sellamount);
		ed_Password = (EditText) findViewById(R.id.ed_Password);
		tv_sumAcount = (TextView) findViewById(R.id.tv_sumAcount);
		tv_feel = (TextView) findViewById(R.id.tv_feel);
		//提交按钮
		btn_sellout = (Button) findViewById(R.id.btn_sellout);
		btn_sellout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.btn_sellout:
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
