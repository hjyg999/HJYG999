package com.md.hjyg.entity;

import java.util.List;


/**
 * ClassName: GetInvestmentListModel date: 2016-4-28 上午9:04:08 remark:我的投资 -
 * 详细信息
 * 
 * @author pyc
 */
public class GetInvestmentListModel extends Expand {
	public int Id;
	public String Title;
	/** 投标金额: */
	public String Amount;
	/** 还款方式: */
	public String RepaymentWay;
	/** 创建时间: */
	public String CreateTime;
	public String LoanRate;
	public String LoanTerm;
	public String LoanDateType;
	/** 本金 + 利息: */
	public String PrincipalInterest;
	/** 本金： */
	public double Principal;
	/** 利息： */
	public double Interest;
	/** 本月应收总额： */
	public String TotalAmount;
	public String RepaymentDate;
	/** 1：手动，2:自动: */
	public String Type;
	/** 满标时间: */
	public String FullTime;
	/** 已还款的项目数: */
	public int RepaymentCount;
	/** 还款期数: */
	public int RepaymentTerms;
    /** 状态，还款状态 0未还 1部分已还，2全部已还*/
    public int Status ;
    
    /**可选择的红包列表*/
    public List<RedEnvelopeModel> RedEnvelopeList;
    /**是否有红包*/
    public int IsRedEnvelope;
    public int MemberInvestRedEnvelopeLogType;
    public RedEnvelopeItemModel RedEnvelopeItem;

}
