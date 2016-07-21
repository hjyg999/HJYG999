package com.md.hjyg.entity;

public class LoanModel {
	public Member Member = new Member();// Member
	public int Id;
	public String EncryptedId = "";
	public int MemberID;
	public String Title = "";
	public String Photo = "";
	public String RegName = "";
	public String RealName = "";
	public boolean AuthStep;
	public boolean MobileValidate;
	public int Type;
	public int LoanPurpose;
	public String Amount;
	/**
	 * 总利率
	 */
	public double LoanRate;
	/**嘉奖的利率*/
	public double ActivitiesRate;
	/**
	 * 项目期限
	 */
	public int LoanTerm;
	public int RepaymentWay;
	public int LoanDateType;
	public int BiddingMin;
	public int BiddingMax;
	public String BiddingStratTime = "";
	public String Details = "";
	public String CityName = "";
	public String MobilePhone = "";
	public int BiddingDays;
	public String CreateTime = "";
	public String FullTime = "";
	public String ContractNumber = "";
	public String OverflowTime = "";
	public String CreateContractTime = "";
	public int Status;
	public String UpdateTime = "";
	public String TitleNumber = "";
	public int RegretLiquidated;
	public int AutoInvestRate;
	public String RepaymentTime = "";
	public double LoanInterest;
	// public ArrayList<Member> memberList = new ArrayList<Member>();
	// public ArrayList<AssureMember> assureMemberList = new
	// ArrayList<AssureMember>();
	/**剩余金额*/
	public double InvestPercentageRadixPoint;
	/**剩余金额*/
	public double SurplusAmount;
	
	public AssureMember AssureMember = new AssureMember();// AssureMember
	public String LoanDifference = "";
	public boolean IsNeedProgramDesignFee;
	public String PayLasttime = "";
	public int InvestCount;
	public double InvestProcess;//竞标进度
	public String Returntime = "";
	
	 /** 修改交易密码页面温馨提示*/
    public String ChangeDealPwdDes;
    /**设置交易密码页面温馨提示*/
    public String ConfigeDealPwdDes;

}