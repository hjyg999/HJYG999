package com.md.hjyg.entity;

import java.util.ArrayList;

/** 
 * ClassName: SingleLoanInterestListModel 
 * date: 2015-11-14 上午10:40:22 
 * remark:用户活期宝利息收益历史列表
 * @author pyc
 */
public class SingleLoanInterestListModel {
	
	/**消息对象：*/
	public Notification notification;
	/**利息列表：*/
	public java.util.List<SingleLoanInterestModel> list = new ArrayList<SingleLoanInterestModel>();

}
