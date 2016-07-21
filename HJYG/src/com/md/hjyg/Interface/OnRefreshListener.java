package com.md.hjyg.Interface;

import com.md.hjyg.layoutEntities.PullToRefreshLayout;


/** 
 * ClassName: OnRefreshListener 
 * date: 2016-3-4 上午11:18:51 
 * remark:刷新加载回调接口
 * @author pyc
 */
public interface OnRefreshListener {
	
	/**
	 * 下拉刷新操作
	 */
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout);

	/**
	 * 上拉加载操作
	 */
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout);

}
