package com.md.hjyg.entity;

/** 
 * ClassName: SingleLoanInvestMode 
 * date: 2015-11-13 上午8:52:21 
 * remark:活期宝单个用户购买记录
 * @author pyc
 */
public class SingleLoanInvestModel {
	/**用户手机号码*/
	public String MemberPhone;
	/**购买金额*/
	public String Amount;
	/**购买时间*/
	public String CreateTime;
	
	/**资金类型0=现金，1=体验金*/
    public int Type;
    public String Photo;

}
