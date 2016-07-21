package com.md.hjyg.entity;

/**
 * ClassName: InvestmentListDetailModel date: 2016-4-28 上午9:04:57
 * remark:我的投资-还款信息列表
 * 
 * @author pyc
 */
public class InvestmentListDetailModel {
	public final static int 还款状态_未还 = 0;
	public final static int 还款状态_部分已还 = 1;
	public final static int 还款状态_全额已还 = 2;
	public final static int 还款状态_作废 = 3;
	public final static int 逾期状态_正常 = 0;
	public final static int 逾期状态_逾期  = 1;
	public final static int 逾期状态_代偿  = 2;
	public final static int 逾期状态_作废  = 3;
	public final static int 逾期状态_提前还款  = 4;
	/**
	 * 可以点击
	 */
	private boolean canClick;
	
	public int Id;
	public int RepaymentID;
	/** 当期利息 */
	public String Interest;
	/** 当期逾期利息 */
	public String OverInterest;
	/** 当期本金 */
	public double Principal;
	/** 当期未还本金 */
	public double UnfinishedPrincipal;
	/** 当期未支付逾期利息 */
	public double UnfinishedOverInterest;
	/** 还款状态 0-未还，1-部分已还，2-全额已还，3-作废 */
	public String Status;
	/** 逾期状态 正常 = 0, 逾期 = 1, 代偿 = 2, 作废 = 3, 提前还款 = 4 */
	public String OverStatus;
	/** 当期未还利息 */
	public String UnfinishedInterest;
	/** 本金+利息 */
	public String PrincipalInterest;
	/** 当期未还本金 + 当期未还利息 + 当期未支付逾期利息 */
	public String UnfinishedPrincipalInterest;
	/** 应还款时间 */
	public String RepaymentDate;
	/** 实际最后还款时间 */
	public String ActualRepaymentDate;
	/**
	 * 是否是退标
	 */
	public boolean isRevoke;
	
	public boolean isCanClick() {
		return canClick;
	}
	public void setCanClick(boolean canClick) {
		this.canClick = canClick;
	}
	
	

}
