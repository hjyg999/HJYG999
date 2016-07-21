package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: RedEnvelopeItemModel date: 2016-7-8 上午11:04:41 remark:我的投标-投标中
 * 已经选择的红包
 * 
 * @author pyc
 */
public class RedEnvelopeItemModel {
	public int Id;
	
	/**类型*/
	public int Type;

	/** 红包Id */
	public int RedEnvelopeId;
	public String endTime;
	public String endTimes;

	/** 投资金额 */
	public double InvestAmount;

	/** 奖励金额 */
	public double RedEnvelopeAmount;

	/** 奖励消费币 */
	public double ConsumptionAmount;

	/** 二级奖励金额 */
	public double TwoLevelRedEnvelopeAmount;

	/** 二级奖励消费币 */
	public double TwoLevelConsumptionAmount;
	public String TwoLevelLotteryActivityRedEnvelopeIntroduction;
	public String OneLevelLotteryActivityRedEnvelopeIntroduction;
	public String Remark;

	/** 奖励体验金 */
	public double ExperienceAmount;

	/** 奖励体验金有效期 */
	public int ExperienceTerm;

	/** 二级奖励体验金 */
	public double TwoLevelExperienceAmount;

	/** 二级奖励体验金有效期 */
	public int TwoLevelExperienceTerm;
	/**收益*/
	public double Interest;

	/** 一级活动红包ID */
	public int OneLevelLotteryActivityRedEnvelope;

	/** 二级活动红包ID */
	public int TwoLevelLotteryActivityRedEnvelope;

	/** 加息券利率 */
	public double IncreaseRate;

	/** 加息券期限类型 */
	public int DateType;

	/** 加息券期限单位 */
	public int DateTerm;
	
	private List<RedEnvelopeListModel> mList;
	

	public List<RedEnvelopeListModel> getmList() {
		return mList;
	}


	public void setmList(List<RedEnvelopeListModel> mList) {
		this.mList = mList;
	}


}
