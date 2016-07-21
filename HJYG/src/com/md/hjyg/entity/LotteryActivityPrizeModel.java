package com.md.hjyg.entity;

/** 
 * ClassName: LotteryActivityPrizeModel 
 * date: 2015-10-23 下午1:37:56 
 * remark:活动单个奖品信息
 * @author pyc
 */
public class LotteryActivityPrizeModel {
	/**红包金额*/
	public double InvestRedAmount;
	public int LotteryActivityId;
	/**
	 * 0:实物，1：理财红包，2：现金红包
	 */
	public int type;
	public String PrizeName;
	public int PrizeNumber;
	public double RedEnvelopeAmount;
	public int RedEnvelopeId;
	public boolean IsDefaul;

}
