package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.fragment.LoadMoreListFragment;
import com.md.hjyg.fragment.MyAddRateRedpacketFragment;
import com.md.hjyg.fragment.MyOrdinaryRedpacketFragment;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * ClassName: MyRedPacketFragmentActivity date: 2016-5-10 下午5:07:45 remark:会员专享-我的红包
 * 
 * @author pyc
 */
public class MyRedPacketFragmentActivity extends BaseFragmentActivity implements
		OnClickListener {
	private int tab;
	@SuppressWarnings("rawtypes")
	private LoadMoreListFragment[] fragments;
	private FragmentManager fm;
	private TextView[] tab_titles;
	private View[] tab_lins;
	private int tabSize = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myredpacketfragment_activity_layout);

		findViewandInit();
		setFragment();
		setTabUI();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "红包加息券", this);

		fragments = new LoadMoreListFragment[tabSize];
		tab_titles = new TextView[tabSize];
		tab_titles[0] = (TextView) findViewById(R.id.tv_tab0);
		tab_titles[1] = (TextView) findViewById(R.id.tv_tab1);
//		tab_titles[2] = (TextView) findViewById(R.id.tv_tab2);
		for (int i = 0; i < tab_titles.length; i++) {
			tab_titles[i].setOnClickListener(this);
		}

		tab_lins = new View[tabSize];
		tab_lins[0] = findViewById(R.id.v_tab0);
		tab_lins[1] = findViewById(R.id.v_tab1);
//		tab_lins[2] = findViewById(R.id.v_tab2);
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
	@SuppressLint("Recycle")
	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		if (fragments[tab] == null) {
			if (tab == 0) {
				fragments[tab] = new MyAddRateRedpacketFragment();
			}else {
				fragments[tab] = new MyOrdinaryRedpacketFragment();
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
		case HeaderViewControler.ID:// 返回按钮
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
//		case R.id.tv_tab2:
//			if (tab != 2) {
//				tab = 2;
//				setTabUI();
//				setFragment();
//			}
//			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		for (int i = 0; i < fragments.length; i++) {
			if (fragments[i] != null) {
				if (i == tab) {
					fragments[i].reStartInit(true);
				} else {
					fragments[i].reStartInit(false);
				}
			}
		}
	}

}
