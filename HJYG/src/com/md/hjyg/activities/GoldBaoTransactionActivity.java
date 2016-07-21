package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.fragment.GoldBaoTransactionFragment;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: GoldBaoTransactionActivity date: 2016-1-26 下午4:32:43 remark:
 * 黄金宝交易记录
 * 
 * @author pyc
 */
public class GoldBaoTransactionActivity extends BaseFragmentActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private ImageView img_search;
	private TextView tv_tab1, tv_tab2, tv_tab3;
	/** 头部三个tab的下划线 */
	private View tab1_line, tab2_line, tab3_line;
	private int tab = 1;
	private TextView tv_center, tv_right;

	private FragmentManager fm;
	private GoldBaoTransactionFragment[] fragmentTab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaotransaction_activity);
		mActivity = this;
		findViewandInit();
		setTabUI();
		setFragment();
	}

	private void findViewandInit() {
		// 标题栏

		header = new HeaderControler(this, true, false, "交易记录",
				Constants.CheckAuthtoken(getBaseContext()));

		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		img_search = (ImageView) findViewById(R.id.img_search);
		img_search.setOnClickListener(this);

		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		tv_tab1.setOnClickListener(this);
		tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
		tv_tab2.setOnClickListener(this);
		tv_tab3 = (TextView) findViewById(R.id.tv_tab3);
		tv_tab3.setOnClickListener(this);

		tab1_line = findViewById(R.id.tab1_line);
		tab2_line = findViewById(R.id.tab2_line);
		tab3_line = findViewById(R.id.tab3_line);

		tv_center = (TextView) findViewById(R.id.tv_center);
		tv_right = (TextView) findViewById(R.id.tv_right);

		fragmentTab = new GoldBaoTransactionFragment[3];

	}

	/**
	 * 设置与tab相关的Fragment
	 * */
	private void setFragment() {
		fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (fragmentTab[tab - 1] == null) {

			fragmentTab[tab - 1] = new GoldBaoTransactionFragment(tab);
			transaction.add(R.id.id_content, fragmentTab[tab - 1]);
			// fragmentTab[1] = new GoldBaoTransactionFragment(2);
			// transaction.add(R.id.id_content, fragmentTab[1]);
			// fragmentTab[2] = new GoldBaoTransactionFragment(3);
			// transaction.add(R.id.id_content, fragmentTab[2]);
		}
		for (int i = 0; i < fragmentTab.length; i++) {
			if (i == tab - 1) {
				transaction.show(fragmentTab[i]);
			} else {
				if (fragmentTab[i] != null) {

					transaction.hide(fragmentTab[i]);
				}
			}
		}

		transaction.commit();
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

	private void setTabUI() {
		if (tab == 1) {// 买入记录
			tv_tab1.setTextColor(getResources().getColor(R.color.red));
			tv_tab2.setTextColor(getResources().getColor(R.color.gray_gold));
			tv_tab3.setTextColor(getResources().getColor(R.color.gray_gold));
			tab1_line.setVisibility(View.VISIBLE);
			tab2_line.setVisibility(View.INVISIBLE);
			tab3_line.setVisibility(View.INVISIBLE);
			tv_center.setText("状态");
			tv_right.setText("黄金(克)");
		} else if (tab == 2) {// 卖出记录
			tv_tab1.setTextColor(getResources().getColor(R.color.gray_gold));
			tv_tab2.setTextColor(getResources().getColor(R.color.red));
			tv_tab3.setTextColor(getResources().getColor(R.color.gray_gold));
			tab1_line.setVisibility(View.INVISIBLE);
			tab2_line.setVisibility(View.VISIBLE);
			tab3_line.setVisibility(View.INVISIBLE);
			tv_center.setText("状态");
			tv_right.setText("金额(元)");
		} else {// 提取记录
			tv_tab1.setTextColor(getResources().getColor(R.color.gray_gold));
			tv_tab2.setTextColor(getResources().getColor(R.color.gray_gold));
			tv_tab3.setTextColor(getResources().getColor(R.color.red));
			tab1_line.setVisibility(View.INVISIBLE);
			tab2_line.setVisibility(View.INVISIBLE);
			tab3_line.setVisibility(View.VISIBLE);
			tv_center.setText("提取方式 ");
			tv_right.setText("数量(条)");
		}
	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
