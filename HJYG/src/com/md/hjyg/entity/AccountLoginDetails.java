package com.md.hjyg.entity;

public class AccountLoginDetails {

	public int redEnvelopeCount;

	public String MemberId;
	public int SuccessLoan;

	public String Amount;

	public Announcement announcement;

	public boolean CardValid;

	public int Returing;

	public int LoanAmount;

	public String RealName = "";

	public String TotalUnfinishedInterestReward;

	public int Fulling;

	public Help Help;

	public String RegName = "";

	public int SuccessInvest;

	public boolean AuthStep;
	public boolean IsSignin;

	public boolean MobileValidate;

	public BreakdownOfFundsModel BreakdownOfFundsModel = new BreakdownOfFundsModel();
	/** 头像二进制字符串 */
	public String ImageBinary;
	/** 头像路径字符串 */
	public String photo;
	/** 体验金总额 */
	public String ExperienceAmountTotal;
	/** 体验金总额html描述信息 */
	public String ExperienceAmountTotalStr;
	/** 是否有未读信息 */
	public boolean HaveNoRead;
	/** 我的金库描述信息： */
	public String MyGoldDes;
	/** 会员专享描述信息： */
	public String VipDes;
	/** 理财师描述信息： */
	public String FinAdvisorDes;
	public boolean IsBirthDay;
	public String phoneInfo ;
	/**
	 * 金豆个数
	 */
	public String LeaveGoldBean;

}
