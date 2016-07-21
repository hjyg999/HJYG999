package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: GoldBaoBuyActivity date: 2016-1-21 上午11:26:53 remark: 黄金宝购买界面
 * 
 * @author pyc
 */
public class GoldBaoBuyActivity extends BaseActivity implements OnClickListener {

	private Activity maActivity;
	HeaderControler header;
	private View header_bottom_line;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private Intent intent;
	/**头部两个tab,tab1按克数购买,tab2按金额购买*/
	private TextView tv_tab1,tv_tab2;
	/**头部两个tab的下划线*/
	private View tab1_line,tab2_line;
	private int tab = 1;
	/**输入框前面提示信息*/
	private TextView tv_edhit;
	/**输入框*/
	private EditText ed_amount;
	/**温馨提示信息*/
	private TextView tv_tip;
	/**根据输入值显示提示信息*/
	private TextView tv_buyhit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_goldbuy_activity);
		maActivity = this;
		findViewandInit();
		setTabUI();
	}

	private void findViewandInit() {
		header = new HeaderControler(this, true, false, "黄金宝",
				Constants.CheckAuthtoken(getBaseContext()));
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setVisibility(View.GONE);
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);
		
		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		tv_tab1.setOnClickListener(this);
		tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
		tv_tab2.setOnClickListener(this);

		tab1_line = findViewById(R.id.tab1_line);
		tab2_line = findViewById(R.id.tab2_line);
		
		tv_edhit = (TextView) findViewById(R.id.tv_edhit);
		ed_amount = (EditText) findViewById(R.id.ed_amount);
		tv_tip = (TextView) findViewById(R.id.tv_tip);
		tv_buyhit = (TextView) findViewById(R.id.tv_buyhit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:
			maActivity.finish();
			overTransition(1);
			break;
		case R.id.btn_buy:
			// intent = new Intent(maActivity, GoldBaoBuyActivity.class);
			// startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_tab1:
			if (tab == 2) {
				tab = 1;
				setTabUI();
			}
			break;
		case R.id.tv_tab2:
			if (tab == 1) {
				tab = 2;
				setTabUI();
			}
			break;

		default:
			break;
		}
	}
	
	private void setTabUI(){
		ed_amount.setText("");
		if (tab == 1) {//按克数购买
			tv_tab1.setTextColor(getResources().getColor(R.color.red));
			tv_tab2.setTextColor(getResources().getColor(R.color.gray_gold));
			tab1_line.setVisibility(View.VISIBLE);
			tab2_line.setVisibility(View.INVISIBLE);
			tv_edhit.setText("购买克数");
			ed_amount.setHint("0.0010-500.0000克");
			tv_tip.setText("1、1毫克起售；\n2、价格有波动投资需谨慎。");
			tv_buyhit.setText("所需金额：0.00元");
		}else {//按金额购买
			tv_tab1.setTextColor(getResources().getColor(R.color.gray_gold));
			tv_tab2.setTextColor(getResources().getColor(R.color.red));
			tab1_line.setVisibility(View.INVISIBLE);
			tab2_line.setVisibility(View.VISIBLE);
			tv_edhit.setText("购买金额");
			ed_amount.setHint("100元");
			tv_tip.setText("1、1元起售；\n2、价格有波动投资需谨慎。");
			tv_buyhit.setText("黄金克重：0.0000克(每人每日累计可购买500克)");
		}
	}

	@Override
	public void onBackPressed() {
		maActivity.finish();
		overTransition(1);
	}

}
