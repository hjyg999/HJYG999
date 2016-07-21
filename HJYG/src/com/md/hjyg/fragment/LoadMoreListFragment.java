package com.md.hjyg.fragment;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.adapter.MyBaseAdapter;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.LoadMoreListView;
import com.md.hjyg.utility.LoadMoreListView.OnLoadMoreListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: LoadMoreListFragment date: 2016-7-5 下午2:12:44
 * remark:加载更多基础Fragment
 * 
 * @author pyc
 */
public abstract class LoadMoreListFragment<T> extends Fragment implements
		DftErrorListener, OnLoadMoreListener {
	protected LoadMoreListView mLoadMoreListView;
	protected LinearLayout lin_nodata;
	protected DialogProgressManager dialog;
	protected DataFetchService dft;
	protected int tab;
	protected  int startPage = 1;
	protected List<T> lists;
	protected MyBaseAdapter<T> adapter;
	protected int page = startPage;
	protected String[] ftype = { "0", "1", "2" };
	protected TextView tv_nodate;
	protected ImageView img_nodate;
	protected DialogManager dialogManager;
	protected RelativeLayout fra_main;
	protected boolean isneedLoadMore = true;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_redpacket_layout,
				container, false);
		findViewandInit(v);
		setInit();
		getWebData(ftype[tab], page);
		return v;
	}

	private void findViewandInit(View v) {
		dft = ((BaseFragmentActivity) getActivity()).getDft();
		dft.setOnDftErrorListener(this);
		dialog = new DialogProgressManager(getActivity(), "努力加载中...");
		dialogManager = new DialogManager(getActivity());

		lin_nodata = (LinearLayout) v.findViewById(R.id.lin_nodata);
		fra_main = (RelativeLayout) v.findViewById(R.id.fra_main);
		tv_nodate = (TextView) v.findViewById(R.id.tv_nodate);
		img_nodate = (ImageView) v.findViewById(R.id.img_nodate);
		mLoadMoreListView = (LoadMoreListView) v.findViewById(R.id.mListView);
	}
	
	/**
	 * 初始化一些信息
	 */
	protected void setInit(){
		
	}

	/**
	 * 获取网络数据，加载完后需要调用setData()方法
	 * @param ftype
	 * @param page 页码
	 */
	protected abstract void getWebData(String ftype, int page);

	/**
	 * 设置数据
	 * @param isLoadmore 如果是第一页，传fase，否则传ture
	 * @param lists
	 */
	protected void setData(boolean isLoadmore, List<T> lists) {
		if (!isLoadmore) {
			if (lists == null || lists.size() == 0) {
				lin_nodata.setVisibility(View.VISIBLE);
//				if (tab == 0) {
//					tv_nodate.setText("红包福利陆续来袭，敬请留意平台动态！");
//					img_nodate.setImageResource(R.drawable.jxw_kx_142x180);
//				} else {
//					tv_nodate.setText("暂无数据！");
//					img_nodate.setImageResource(R.drawable.jxw_ku_142x180);
//				}
				mLoadMoreListView.setVisibility(View.GONE);
			} else {
				lin_nodata.setVisibility(View.GONE);
				mLoadMoreListView.setVisibility(View.VISIBLE);
				this.lists = lists;
				adapter = getAdapter(lists, getActivity());
				mLoadMoreListView.setAdapter(adapter);
			}
			dialog.dismiss();
		} else {
			if (lists != null && lists.size() > 0) {
				this.lists.addAll(lists);
				adapter.notifyDataSetChanged();
			}
		}

		if (lists == null || (lists.size() <= Constants.PAGESIZE)|| !isneedLoadMore) {
			mLoadMoreListView.setOnLoadMoreListener(null);
		} else {
			mLoadMoreListView.setOnLoadMoreListener(this);
		}
		mLoadMoreListView.onLoadMoreComplete();
	}
	
	protected abstract MyBaseAdapter<T> getAdapter(List<T> lists,Activity activity);

	/**
	 * 刷新
	 * @param isShowDia 是否显示加载框
	 */
	public void reStartInit(boolean isShowDia) {
		if (isShowDia) {
			dialog.initDialog();
		}
		page = startPage;
		getWebData(ftype[tab], page);
	}

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

	@Override
	public void onLoadMore() {
		page++;
		getWebData(ftype[tab], page);
	}

}
