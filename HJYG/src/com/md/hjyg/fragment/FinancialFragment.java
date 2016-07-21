package com.md.hjyg.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.md.hjyg.R;
import com.md.hjyg.Interface.OnRefreshListener;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.adapter.FinancialAdaper;
import com.md.hjyg.layoutEntities.PullToRefreshLayout;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utils.DateUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;

/**
 * ClassName: FinancialFragment date: 2016-4-9 下午2:52:47 remark:资金记录
 * 
 * @author pyc
 */
public abstract class FinancialFragment<T> extends Fragment implements
		OnRefreshListener {
	protected List<String> mapKey;
	protected Map<String, List<T>> map;
	protected DataFetchService dft;
	protected FinancialAdaper<T> adapter;
	protected ExpandableListView mExpandableListView;
	protected int pageIndex = 1;
	protected LinearLayout lin_nodata;
	protected String ftype = "";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_whole_layout, container,
				false);
		mExpandableListView = (ExpandableListView) v
				.findViewById(R.id.mExpandableListView);
		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		// 取消系统左侧的图标
		mExpandableListView.setGroupIndicator(null);
		// 取消点击
		mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				return true;
			}
		});

		dft = ((BaseFragmentActivity) getActivity()).getDft();
		getFinancialDetailsWebservice(ftype,pageIndex);
		return v;
	}

	/**
	 * 网络加载数据
	 * @param pageIndex
	 */
	public abstract void getFinancialDetailsWebservice(String ftype,int pageIndex);

	protected void SetUIData(String Loadmore, List<T> lists) {
		if (Loadmore.length() == 0) {
			if (lists == null || lists.size() == 0) {
				lin_nodata.setVisibility(View.VISIBLE);
				mExpandableListView.setVisibility(View.GONE);
				return;
			}

			lin_nodata.setVisibility(View.GONE);
			mExpandableListView.setVisibility(View.VISIBLE);
			mapKey = new ArrayList<String>();
			map = new HashMap<String, List<T>>();
			List<T> dataList = lists;
			setListData(dataList);
			adapter = getAdaper(mapKey, map);
			if (dataList.size() < 10) {
				adapter.setloadmoreover(true);
			}
			adapter.setOnRefreshListener(this);
			mExpandableListView.setAdapter(adapter);
		} else {
			List<T> dataList = lists;
			if (dataList == null || dataList.size() < 10) {
				adapter.setloadmoreover(true);
			}
			if (dataList != null) {
				setListData(dataList);
			}
			adapter.notifyDataSetChanged();
		}

		// 展开
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			mExpandableListView.expandGroup(i);
		}
	}

	public void setListData(List<T> dataList) {
		for (int i = 0; i < dataList.size(); i++) {
			String year = DateUtil.changto(getYearTime(dataList.get(i))
					.replace("/", "-"), "yyyy");
			boolean ishave = false;
			for (int j = 0; j < mapKey.size(); j++) {
				if (year.equals(mapKey.get(j))) {
					map.get(year).add(dataList.get(i));
					ishave = true;
					break;
				}
			}
			if (!ishave) {
				mapKey.add(year);
				List<T> itemList = new ArrayList<T>();
				map.put(year, itemList);
				map.get(year).add(dataList.get(i));
			}
		}
	}

	public abstract FinancialAdaper<T> getAdaper(List<String> mapKey,
			Map<String, List<T>> map);

	public abstract String getYearTime(T t);

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		pageIndex++;
		getFinancialDetailsWebservice(ftype,pageIndex);
	}

}
