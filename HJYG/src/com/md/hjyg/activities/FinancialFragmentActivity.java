package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.adapter.WholeFinancialAdaper;
import com.md.hjyg.fragment.WholeFinancialFragment;
import com.md.hjyg.fragment.WithdrawalsFinancialFragment;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * ClassName: FinancialFragmentActivity date: 2016-3-23 下午1:16:37 remark:资金记录
 * 
 * @author pyc
 */
public class FinancialFragmentActivity extends BaseFragmentActivity implements
		OnClickListener {
	private int tab;
	private FragmentManager fm;
	private WholeFinancialFragment wholeFinancialFragment;
	private WholeFinancialFragment RechargeeFinancialFragment;
	private WholeFinancialFragment IncomeFinancialFragment;
	private WithdrawalsFinancialFragment withdrawalsFinancialFragment;
	private Fragment[] fragments;
	private TextView[] tab_titles;
	private View[] tab_lins;
	private int sum = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_financialfragment_layout);
		findViewandInit();
		setFragment();
		setTabUI();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "资金记录", this);
		fragments = new Fragment[sum];

		tab_titles = new TextView[sum];
		tab_titles[0] = (TextView) findViewById(R.id.tv_tab0);
		tab_titles[1] = (TextView) findViewById(R.id.tv_tab1);
		tab_titles[2] = (TextView) findViewById(R.id.tv_tab2);
		tab_titles[3] = (TextView) findViewById(R.id.tv_tab3);
		for (int i = 0; i < tab_titles.length; i++) {
			tab_titles[i].setOnClickListener(this);
		}

		tab_lins = new View[sum];
		tab_lins[0] = findViewById(R.id.v_tab0);
		tab_lins[1] = findViewById(R.id.v_tab1);
		tab_lins[2] = findViewById(R.id.v_tab2);
		tab_lins[3] = findViewById(R.id.v_tab3);
	}

	private void setTabUI() {
		for (int i = 0; i < tab_titles.length; i++) {
			if (i == tab) {
				tab_titles[i].setTextColor(getResources().getColor(
						R.color.red_BF1424));
				tab_lins[i].setVisibility(View.VISIBLE);
			} else {
				tab_titles[i].setTextColor(getResources().getColor(
						R.color.gray_gold));
				tab_lins[i].setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 设置与tab相关的Fragment
	 * */
	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		if (fragments[tab] == null) {

			if (tab == 0) {
				if (wholeFinancialFragment == null) {
					wholeFinancialFragment = new WholeFinancialFragment(
							WholeFinancialAdaper.Whole);
				}
				fragments[tab] = wholeFinancialFragment;

			} else if (tab == 1) {
				if (RechargeeFinancialFragment == null) {
					RechargeeFinancialFragment = new WholeFinancialFragment(
							WholeFinancialAdaper.Recharge);
				}
				fragments[tab] = RechargeeFinancialFragment;

			} else if (tab == 2) {
				if (withdrawalsFinancialFragment == null) {
					withdrawalsFinancialFragment = new WithdrawalsFinancialFragment();
				}
				fragments[tab] = withdrawalsFinancialFragment;

			} else if (tab == 3) {
				if (IncomeFinancialFragment == null) {
					IncomeFinancialFragment = new WholeFinancialFragment(
							WholeFinancialAdaper.Income);
				}
				fragments[tab] = IncomeFinancialFragment;

			} 
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
		case R.id.tv_tab3:
			if (tab != 3) {
				tab = 3;
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
		super.onBackPressed();
		this.finish();
		overTransition(1);
	}

}
