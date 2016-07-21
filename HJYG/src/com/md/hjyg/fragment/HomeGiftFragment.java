package com.md.hjyg.fragment;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * ClassName: HomeGiftFragment date: 2016-3-19 下午4:06:37 remark:新版有礼界面
 * 
 * @author pyc
 */
public class HomeGiftFragment extends Fragment implements OnClickListener {

	private HeaderView mheadView;
	private TextView tv_tab0, tv_tab1;
	private View v_tab0, v_tab1;
	private int tab;
	private FragmentManager fm;
	private Fragment[] fragments;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_homegift_layout, container,
				false);
		findViewandInit(v);
		setTabUI();
		setFragment();
		return v;
	}

	private void findViewandInit(View v) {
		fragments = new Fragment[2];

		// 设置头部标题栏
		mheadView = (HeaderView) v.findViewById(R.id.mheadView);
		mheadView.setUIData("消费", null);
		ViewParamsSetUtil.setViewHight(mheadView, 0.076f, getActivity());

		tv_tab0 = (TextView) v.findViewById(R.id.tv_tab0);
		v_tab0 = v.findViewById(R.id.v_tab0);
		tv_tab1 = (TextView) v.findViewById(R.id.tv_tab1);
		v_tab1 = v.findViewById(R.id.v_tab1);

		tv_tab0.setOnClickListener(this);
		tv_tab1.setOnClickListener(this);

	}

	private void setTabUI() {
		if (tab == 0) {
			tv_tab0.setTextColor(getResources().getColor(R.color.red_BF1424));
			tv_tab1.setTextColor(getResources().getColor(R.color.gray_gold));
			v_tab0.setVisibility(View.VISIBLE);
			v_tab1.setVisibility(View.INVISIBLE);
		} else {
			tv_tab0.setTextColor(getResources().getColor(R.color.gray_gold));
			tv_tab1.setTextColor(getResources().getColor(R.color.red_BF1424));
			v_tab0.setVisibility(View.INVISIBLE);
			v_tab1.setVisibility(View.VISIBLE);
		}
	}

	private void setFragment() {
		fm = getChildFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (fragments[tab] == null) {
			if (tab == 0) {
				fragments[tab] = new HomeGiftNewFragment(0);
			}else {
				fragments[tab] = new ConsumptionFragment();
			}
			transaction.add(R.id.tabcontent, fragments[tab]);

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

		default:
			break;
		}
	}

}
