package com.md.hjyg.fragment;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MeetingExpandaListAdapter;
import com.md.hjyg.entity.MeetingMemberLotteryLogModel;
import com.md.hjyg.layoutEntities.RefreshableScrollView;
import com.md.hjyg.layoutEntities.RefreshableScrollView.RefreshListener;
import com.md.hjyg.utility.MeetingWebServiceManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;

/**
 * ClassName: MeetingRecordLotteryLogFragment date: 2016-3-14 上午10:05:38 remark:
 * 中奖记录
 * 
 * @author pyc
 */
public class MeetingRecordLotteryLogFragment extends BaseFragment {
	private MeetingWebServiceManager meetingManager;
	private int ActivityId;
	private LinearLayout lin_nodata;
	// private PullableExpandableListView expandableListView;
	// private PullToRefreshLayout refresh_view;
	private ExpandableListView expandableListView;
	private MeetingExpandaListAdapter adapter;
	private RefreshableScrollView refreshableScrollView;
	private boolean isrefreshing;

	public MeetingRecordLotteryLogFragment(
			MeetingWebServiceManager meetingManager, int ActivityId) {
		this.meetingManager = meetingManager;
		this.ActivityId = ActivityId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(
				R.layout.fragment_meetingrecordlotterylog_layout, container,
				false);
		findViewandInit(v);
		meetingManager.GetMemberLotteryLog(ActivityId);

		// 下拉刷新监听
		refreshableScrollView.setRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh(RefreshableScrollView view) {
				if (!isrefreshing) {
					meetingManager.GetMemberLotteryLog(ActivityId);
					isrefreshing = true;
				}

			}
		}, 302);
		return v;
	}

	private void findViewandInit(View v) {
		expandableListView = (ExpandableListView) v
				.findViewById(R.id.expandableListView);
		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		refreshableScrollView = (RefreshableScrollView) v
				.findViewById(R.id.refreshableScrollView);
		// refresh_view = (PullToRefreshLayout)
		// v.findViewById(R.id.refresh_view);
	}

	public void setexpandableListViewDate(
			List<MeetingMemberLotteryLogModel> list) {
		if (list == null || list.size() == 0) {
			lin_nodata.setVisibility(View.VISIBLE);
			refreshableScrollView.setVisibility(View.GONE);
			return;
		}

		// 取消系统左侧的图标
		expandableListView.setGroupIndicator(null);
		// 子条目展开时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupExpandListener(new OnGroupExpandListener() {

					@Override
					public void onGroupExpand(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});
		// 子条目收缩时重新计算expandableListView的高度，用于刷新ScrollView的高度
		expandableListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						Constants
								.setListViewHeightBasedOnChildren(expandableListView);
						// setExpandableListViewHeightBasedOnChildren(expandableListView);
					}
				});

		adapter = new MeetingExpandaListAdapter(list, getActivity());
		expandableListView.setAdapter(adapter);
		Constants.setListViewHeightBasedOnChildren(expandableListView);
		lin_nodata.setVisibility(View.GONE);
		refreshableScrollView.setVisibility(View.VISIBLE);

		if (isrefreshing) {
			// 刷新完成
			refreshableScrollView.finishRefresh();
			isrefreshing = false;
		}
	}

}
