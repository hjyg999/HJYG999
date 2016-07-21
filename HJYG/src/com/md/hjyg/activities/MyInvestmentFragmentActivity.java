package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.fragment.MyInvestmentFragment;
import com.md.hjyg.fragment.MyInvestmentingFragment;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.utility.Save;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * ClassName: MyInvestmentFragmentActivity date: 2016-4-19 下午4:57:16
 * remark:新版我的投资
 * 
 * @author pyc
 */
public class MyInvestmentFragmentActivity extends BaseFragmentActivity
		implements OnClickListener {
	private HeaderView mheadView;
	private int tab;
	private Fragment[] fragments;
	private FragmentManager fm;
	private TextView[] tab_titles;
	private View[] tab_lins;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinvestmentfragment_activity_layout);

		findViewandInit();
		setFragment();
		setTabUI();
		Save.loadingImg(mHandler, this, new int[]{R.drawable.money2});
	}

	private void findViewandInit() {
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "我的投资", "");
//		mheadView.setRightImg(R.drawable.money2);

		fragments = new Fragment[3];
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
				fragments[tab] = new MyInvestmentingFragment();
			}else {
				
				fragments[tab] = new MyInvestmentFragment(tab);
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
		case HeaderView.left_img_ID:// 返回按钮
			this.finish();
			overTransition(1);
			break;
		case HeaderView.rightimg_ID:// 日历
			startActivity(new Intent(this, MyInvestmentCalendarActivity.class));
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
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				Bitmap[] bitmaps = (Bitmap[]) msg.obj;
				mheadView.getImg_top_right().setImageBitmap(bitmaps[0]);
				break;

			default:
				break;
			}
		};
	};

}
