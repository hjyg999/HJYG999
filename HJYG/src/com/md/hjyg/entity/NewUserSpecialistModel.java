package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: NewUserSpecialistModel date: 2016-3-5 下午5:31:17
 * remark:新手专享获取购买项目的详细信息
 * 
 * @author pyc
 */
public class NewUserSpecialistModel {

	/**
	 * 投资需知
	 */
	public List<LoanDetailsModel> RemarkList;
	// 新手标信息
	public List<NoviceLoan> NoviceLoanList;

	public String RedirectToAction;

	public int Index;

	public String RealName;

	public String MobilePhone;

	public String AddressInfo;

	public String LeaveAmount;
	public String ruleDes;

	public class LoanDetailsModel {
		/**
		 * 内容
		 */
		public String Content;
		/**
		 * 标题
		 */
		public String Title;
		public int NoviceLoanId;
		public int Id;
	}

}
