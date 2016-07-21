package com.md.hjyg.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: StatsMonthlyRewardListsModel date: 2015-11-28 下午1:13:47 remark:
 * 已收奖励-待收奖励
 * 
 * @author pyc
 */
public class StatsMonthlyRewardListsModel {
	public List<StatsMonthlyReward> list = new ArrayList<StatsMonthlyReward>();

	public class StatsMonthlyReward {
		// public String YearMonth ;
		// /**已收奖励*/
		// public String Total ;
		// /**好友已收*/
		// public String FirstInCome ;
		// /**待收奖励*/
		// public String FirstUnfinishedInterestReward ;
		// /**好友待收*/
		// public String FirstUnfinishedInterest ;

		public String YearMonth;
		/** 已收奖励 */
		public String Total;
		/** 一级好友已收 */
		public String OneFirstInCome;
		/** 二级好友已收 */
		public String TwoFirstInCome;
		/** 待收奖励 */
		public String UnfinishedInterestReward;
		/** 一级好友待收 */
		public String OneFirstUnfinishedInterest;
		/** 二级好友待收 */
		public String TwoFirstUnfinishedInterest;

	}
}
