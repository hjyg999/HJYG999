package com.md.hjyg.activities;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.adapter.SafetyGuaranteeAdapter;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * 安全保障页面
 */
public class SafetyGuaranteeActivity extends BaseActivity implements
		OnClickListener {
	// ImageView btnProceed;
	private ViewPager pager;
	private CirclePageIndicator indicator;
	private ArrayList<Integer> imgIDLists;

	private SafetyGuaranteeAdapter myPager;

	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_safety_guarantee);

		findviews();
		Setviews();
	}

	public void findviews() {
		type = getIntent().getIntExtra("type", 0);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		pager = (ViewPager) findViewById(R.id.view_pager);

	}

	public void Setviews() {
		imgIDLists = new ArrayList<Integer>();
		if ( type == 1) {
			HeaderViewControler.setHeaderView(this, "活期宝", this);
			imgIDLists.add(R.drawable.huoqibao1);
			imgIDLists.add(R.drawable.huoqibao2);
			imgIDLists.add(R.drawable.huoqibao3);
			imgIDLists.add(R.drawable.huoqibao4);
			imgIDLists.add(R.drawable.huoqibao5);
			imgIDLists.add(R.drawable.huoqibao6);

		} else {
			HeaderViewControler.setHeaderView(this, "安全保障", this);

			imgIDLists.add(R.drawable.safetyguarantee1);
			imgIDLists.add(R.drawable.safetyguarantee2);
			imgIDLists.add(R.drawable.safetyguarantee3);
			imgIDLists.add(R.drawable.safetyguarantee4);
			imgIDLists.add(R.drawable.safetyguarantee5);
			imgIDLists.add(R.drawable.safetyguarantee6);
			imgIDLists.add(R.drawable.safetyguarantee7);
			imgIDLists.add(R.drawable.safetyguarantee8);
		}

		myPager = new SafetyGuaranteeAdapter(this, imgIDLists,type);
		pager.setAdapter(myPager);

		indicator.setViewPager(pager);
		// 设置选中圆点的颜色
		indicator.setFillColor(getResources().getColor(R.color.gray));
		indicator.setPageColor(getResources().getColor(R.color.gray_q));
		// 设置圆点的大小
		indicator.setRadius(6f);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {// 返回键监听
		this.finish();
		overTransition(1);
	}

}
