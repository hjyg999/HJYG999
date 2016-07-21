package com.md.hjyg.entity;

/**
 * ClassName: MyInvestmentModel date: 2016-4-28 上午9:03:26 remark: 我的投资信息
 * 
 * @author pyc
 */
public class MyInvestmentModel {

	public boolean Result;
	public int pageIndex;
	public int pageTotal;
	/**本月应收总额：*/
	public String TotalAmount;
	public java.util.List<GetInvestmentListModel> List;

}
