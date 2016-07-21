package com.md.hjyg.Interface;

/**
 * ClassName: DataChangeItf date: 2016-2-3 上午11:50:48
 * remark:网络加载完成接口-fragment与activity的通信
 * 
 * @author pyc
 */

public interface DataChangeItf {
	/**
	 * fragment里数据加载完成，更新UI
	 * */
	public void NetDataSetChanged();
}
