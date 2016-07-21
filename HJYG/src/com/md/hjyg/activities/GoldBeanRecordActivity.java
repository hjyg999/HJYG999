package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.fragment.GoldBeanRecordFragment;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: GoldBeanRecordActivity date: 2016-5-25 下午5:59:46 remark:金豆记录
 * 
 * @author pyc
 */
public class GoldBeanRecordActivity extends BaseFragmentActivity implements
		OnClickListener {

	private LinearLayout lin_top;
	private TextView tv_rule;
	private int tab;
	private GoldBeanRecordFragment[] fragments;
	private FragmentManager fm;
	private TextView[] tab_titles;
	private View[] tab_lins;
	private TextView tv_mybean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goldbeanrecord_layout);

		findViewandInit();
		setFragment();
		setTabUI();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "我的记录", this);

		lin_top = (LinearLayout) findViewById(R.id.lin_top);
		ViewParamsSetUtil.setViewParams(lin_top, 720, 283, true);

		tv_rule = (TextView) findViewById(R.id.tv_rule);
		tv_rule.setOnClickListener(this);
		
		tv_mybean = (TextView) findViewById(R.id.tv_mybean);
		tv_mybean.setText(Constants.LeaveGoldBean);

		fragments = new GoldBeanRecordFragment[3];
		tab_titles = new TextView[3];
		tab_titles[0] = (TextView) findViewById(R.id.tv_tab0);
		tab_titles[1] = (TextView) findViewById(R.id.tv_tab1);
		tab_titles[2] = (TextView) findViewById(R.id.tv_tab2);
		for (int i = 0; i < tab_titles.length; i++) {
			tab_titles[i].setOnClickListener(this);
		}

		tab_lins = new View[3];
		tab_lins[0] = findViewById(R.id.v_tab0);
		tab_lins[1] = findViewById(R.id.v_tab1);
		tab_lins[2] = findViewById(R.id.v_tab2);
	}

	private void setTabUI() {
		for (int i = 0; i < tab_titles.length; i++) {
			if (i == tab) {
				tab_titles[i].setTextColor(getResources().getColor(
						R.color.red_BF1424));
				tab_lins[i].setVisibility(View.VISIBLE);
			} else {
				tab_titles[i].setTextColor(getResources().getColor(
						R.color.gray));
				tab_lins[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 设置与tab相关的Fragment
	 * */
	@SuppressLint("Recycle")
	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		if (fragments[tab] == null) {
			fragments[tab] = new GoldBeanRecordFragment(tab);

			transaction.add(R.id.id_content, fragments[tab]);

		}
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] != null) {
				if (i == tab) {
					transaction.show(fragments[i]);
				} else {
					transaction.hide(fragments[i]);
				}
			}
		}

		transaction.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_rule:
			startActivity(new Intent(this, GoldBeanRuleActivity.class));
			overTransition(2);
			break;
		case R.id.tv_tab0:
			if (tab != 0) {
				tab = 0;
				setTabUI();
				setFragment();
			}
			break;
		case R.id.tv_tab1:
			if (tab != 1) {
				tab = 1;
				setTabUI();
				setFragment();
			}
			break;
		case R.id.tv_tab2:
			if (tab != 2) {
				tab = 2;
				setTabUI();
				setFragment();
			}
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
