package com.md.hjyg.Interface;

/**
 * ClassName: Pullable date: 2016-3-4 上午10:26:34 remark:判断是否可以上拉加载更多和下拉刷新的接口
 * 
 * @author pyc
 */
public interface Pullable {
	/**
	 * 判断是否可以下拉，如果不需要下拉功能可以直接return false
	 * 
	 * @return true如果可以下拉否则返回false
	 */
	public boolean canPullDown();

	/**
	 * 判断是否可以上拉，如果不需要上拉功能可以直接return false
	 * 
	 * @return true如果可以上拉否则返回false
	 */
	public boolean canPullUp();
}
