package com.md.hjyg.entity;

/**
 * ClassName: RedEnvelopeModel date: 2016-7-7 下午1:53:31 remark:新版红包加息券Model
 * 
 * @author pyc
 */
public class RedEnvelopeModel {

	public String name;
	public String RegName;
	public String RealName;
	public String LoanName;
	public String InvestRegName;
	public double Amount;
	public String CreateTime;
	public double InvestAmount;
	public double RedEnvelopeAmount;
	public double ExperienceAmount;
	/**加息券利率*/
	public double IncreaseRate;
	public String RedEnvelopeAmountInfo;
	public String ExperienceAmountInfo;
	public String LotteryActivityRedEnvelopeAmount;
	public String LotteryActivityExperienceAmount;
	public int OneLevelLotteryActivityRedEnvelope;
	public int Level;
	public int gid;
	/**体验金期限*/
	public int ExperienceTerm;
	public String TRegName;
	public String TRealName;
	public int PointRedEnvelopeId;
	public int DateType;
	public int DateTerm;
	public String dType;
	public String eTime;
	public String LoanInterest;
	public int Id;
	/** 红包类型，0-投资红包： */
	public int Type;
	/** 奖励对象，0-投资人、1-推荐人： */
	public int ToType;
	/** 奖励会员类型，0-所有、1-内部、2-外部： */
	public int MemberType;
	/** 活动开始时间 ： */
	public String startTime;
	/** 活动结束时间 ： */
	public String endTime;
	/** 活动描述： */
	public String Remark;
	/** 是否首次投资-0是、1否： */
	public int First;
	/** 投资期限单位： */
	public int InvestDateType;
	/** 投资期限： */
	public int InvestTerm;
	/** 状态，0-有效、1-无效： */
	public int Status;
	/** 简介： */
	public String Introduction;
	/** 有效期(天)： */
	public int ValidityPeriod;
	
	/**
	 * 是否选中
	 */
	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

}
