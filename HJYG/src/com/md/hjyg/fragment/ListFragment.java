package com.md.hjyg.fragment;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MyBaseAdapter;
import com.md.hjyg.layoutEntities.RefreshableView;
import com.md.hjyg.layoutEntities.RefreshableView.PullToRefreshListener;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * ClassName: BaseListFragment date: 2016-3-2 下午2:22:39 remark:公用fragment -
 * 包含加载更多的listview , 和下拉刷新
 * 
 * @author pyc
 */
public abstract class ListFragment<T> extends BaseFragment implements
		OnLoadMoreListener {

	protected LoadMoreListView mLoadMoreListView;
	protected RefreshableView refreshableView;
	protected List<T> lists;
	protected MyBaseAdapter<T> adapter;
	protected int startpage = 1;
	protected int pageIndex = 1;
	/**
	 * 是否下拉刷新
	 */
	protected boolean isPullToRefresh = true;
	/**
	 * 是否是下拉刷新状态
	 */
	private boolean isPull;
	
	protected LinearLayout lin_nodata,lin_data;
	
	public ListFragment(){
		
	}
	public ListFragment(int startpage){
		this.startpage = startpage;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list_layout, container,
				false);
		findViewandInit(v);
		CallLoanListWebservice(pageIndex);
		return v;
	}

	private void findViewandInit(View v) {
		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		lin_data = (LinearLayout) v.findViewById(R.id.lin_data);
		mLoadMoreListView = (LoadMoreListView) v
				.findViewById(R.id.mLoadMoreListView);
		refreshableView = (RefreshableView) v
				.findViewById(R.id.refreshable_view);
		refreshableView.setOnRefreshListener(new PullToRefreshListener() {

			@Override
			public void onRefresh() {
				pageIndex = startpage;
				isPull = true;
				CallLoanListWebservice(pageIndex);
			}
		}, 1);

	}

	/**
	 * 设置Listview数据
	 * 
	 * @param isFristLoad
	 *            是否是第一次加载
	 * @param loadmore
	 *            是否加载更多
	 */
	public void setListViewData(boolean isFristLoad, List<T> lists,
			boolean loadmore) {
		if (isFristLoad) {
			if (lists == null || lists.size() == 0) {
				lin_nodata.setVisibility(View.VISIBLE);
				lin_data.setVisibility(View.GONE);
			}else {
				lin_nodata.setVisibility(View.GONE);
				lin_data.setVisibility(View.VISIBLE);
			}
			this.lists = lists;
			adapter = new MyBaseAdapter<T>(this.lists, getActivity()) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					return getListItemView(position, convertView, parent);
				}
			};

			mLoadMoreListView.setAdapter(adapter);
			if (isPull) {
				refreshableView.finishRefreshing();
				isPull = false;
			}

		} else {

			if (!lists.isEmpty()) {
				this.lists.addAll(lists);
				adapter.notifyDataSetChanged();
				mLoadMoreListView.onLoadMoreComplete();
			} else {
				mLoadMoreListView.onLoadMoreComplete();
			}

		}

		if (loadmore) {
			if ((lists.size() <= Constants.PAGESIZE)) {
				mLoadMoreListView.setOnLoadMoreListener(null);
				mLoadMoreListView.onLoadMoreComplete();
			} else {
				mLoadMoreListView.setOnLoadMoreListener(this);
			}

		} else {
			mLoadMoreListView.setOnLoadMoreListener(null);
			mLoadMoreListView.onLoadMoreComplete();
		}
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		CallLoanListWebservice(pageIndex);
	}

	/**
	 * 获取网络服务，得到listView数据 网络加载完成后必须调用setListViewData方法
	 */
	public abstract void CallLoanListWebservice(int pageIndex);

	/**
	 * 设置Listview的adapter的布局
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getListItemView(int position, View convertView,
			ViewGroup parent);

}
