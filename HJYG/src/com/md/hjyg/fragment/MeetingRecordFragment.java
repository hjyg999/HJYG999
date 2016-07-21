package com.md.hjyg.fragment;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.MeetingMemberLotteryLogModel;
import com.md.hjyg.entity.MeetingPinOpLogModel;
import com.md.hjyg.utility.MeetingWebServiceManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ClassName: MeetingRecordFragment date: 2016-3-12 下午4:12:09 remark:
 * 
 * @author pyc
 */
public class MeetingRecordFragment extends BaseFragment implements
		OnClickListener {

	private LinearLayout lin_tab, lin_tab0, lin_tab1;
	private TextView tv_tab0title, tv_tab1title;
	private View tv_tab0line, tv_tab1line;
	private int tab;

	private MeetingRecordPinOpLogFragment pinOpLogFragment;
	private MeetingRecordLotteryLogFragment lotteryLogFragment;
	private Fragment[] fragments;
	private FragmentManager fm;
	private MeetingWebServiceManager meetingManager;
	private int ActivityId;

	public MeetingRecordFragment() {

	}

	public MeetingRecordFragment(MeetingWebServiceManager meetingManager,
			int ActivityId) {
		this.meetingManager = meetingManager;
		this.ActivityId = ActivityId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_meetingrecord_layout,
				container, false);
		findView(v);
		//
		setTabUI();
		// GetWebserviceHomeAPI();
		setFragment();
		return v;
	}

	private void findView(View v) {
		lin_tab = (LinearLayout) v.findViewById(R.id.lin_tab);
		lin_tab0 = (LinearLayout) v.findViewById(R.id.lin_tab0);
		lin_tab1 = (LinearLayout) v.findViewById(R.id.lin_tab1);
		lin_tab0.setOnClickListener(this);
		lin_tab1.setOnClickListener(this);

		tv_tab0title = (TextView) v.findViewById(R.id.tv_tab0title);
		tv_tab1title = (TextView) v.findViewById(R.id.tv_tab1title);

		tv_tab0line = v.findViewById(R.id.tv_tab0line);
		tv_tab1line = v.findViewById(R.id.tv_tab1line);

		fragments = new Fragment[2];
	}

	private void setTabUI() {
		if (tab == 0) {
			tv_tab0title.setTextColor(getResources().getColor(R.color.red));
			tv_tab1title.setTextColor(getResources()
					.getColor(R.color.gray_gold));
			tv_tab0line.setVisibility(View.VISIBLE);
			tv_tab1line.setVisibility(View.INVISIBLE);
		} else if (tab == 1) {
			tv_tab1title.setTextColor(getResources().getColor(R.color.red));
			tv_tab0title.setTextColor(getResources()
					.getColor(R.color.gray_gold));
			tv_tab1line.setVisibility(View.VISIBLE);
			tv_tab0line.setVisibility(View.INVISIBLE);
		}
	}


	/**
	 * 设置与tab相关的Fragment
	 * */
	private void setFragment() {
		fm = getChildFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		if (fragments[tab] == null) {
			if (tab == 1) {
				if (pinOpLogFragment == null) {
					pinOpLogFragment = new MeetingRecordPinOpLogFragment(
							meetingManager,ActivityId);
					fragments[tab] = pinOpLogFragment;
					transaction.add(R.id.tabcontent, fragments[tab]);
				}
			} else if (tab == 0) {
				if (lotteryLogFragment == null) {
					lotteryLogFragment = new MeetingRecordLotteryLogFragment(
							meetingManager, ActivityId);
					fragments[tab] = lotteryLogFragment;
					transaction.add(R.id.tabcontent, fragments[tab]);
				}
			}

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

	public void setTabTwoData(List<MeetingPinOpLogModel> lists) {
		pinOpLogFragment.setListViewData(true, lists, false);
	}
	
	public void setexpandableListViewDate(
			List<MeetingMemberLotteryLogModel> list) {
		if (lotteryLogFragment != null) {
			lotteryLogFragment.setexpandableListViewDate(list);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_tab0:
			if (tab == 1) {
				tab = 0;
				setTabUI();
				setFragment();
			}

			break;
		case R.id.lin_tab1:
			if (tab == 0) {
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
