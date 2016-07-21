package com.md.hjyg.entity;

import java.util.List;

/** 
 * ClassName: Expand 
 * date: 2016-4-22 下午1:13:06 
 * remark:listview嵌套listview用于判断是否展开的变量
 * @author pyc
 */
public class Expand {
	/**
	 * 是否展开嵌套的listview
	 */
	private boolean isExpand;
	private int h;
	
	/** 还款信息 */
	private List<InvestmentListDetailModel> ListDetail;
	
	/** 获取还款信息 */
	public List<InvestmentListDetailModel> getListDetail() {
		return ListDetail;
	}
	
	/** 设置还款信息 */
	public void setListDetail(List<InvestmentListDetailModel> listDetail) {
		ListDetail = listDetail;
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void setExpand(boolean isExpand) {
		this.isExpand = isExpand;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}
	

}
