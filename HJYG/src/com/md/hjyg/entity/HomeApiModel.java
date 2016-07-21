package com.md.hjyg.entity;

import java.util.ArrayList;
import java.util.List;

public class HomeApiModel {
	public ArrayList<AdvertisementModel> advertiseList = new ArrayList<AdvertisementModel>();
	public String LoanPayCount;
	public String LatestLoanCount;
	public String DingTBCount;
	/** 活期宝剩余金额 */
	public double LoanDifference;
	public double Rate;
	// / <summary>
	// / 活期宝万元收益
	// / </summary>
	public double LoanInterest;
	/** 是否显示年会抽奖入口： */
	public boolean IsAnnualMeetingActivity;
	/**是否显示一月份抽奖活动入口*/
    public boolean IsShowRewardActivity ;
    /**是否显示4月份活动弹窗*/
    public boolean ZeroActionIsOpen ;
	/** 热门公告 */
	public int IsHotCount;
	/** 活期宝描述信息： */
	public String HQBDes;
	/** 黄金宝描述信息: */
	public String HJBDes;
	/** 定投宝描述信息: */
	public String DTBDes;
	/** 项目投资描述信息: */
	public String XMTZDes;
	
	/**新手标信息*/
	public java.util.List<NoviceLoan> NoviceLoanList ;
	
	/**
	 * 标的列表
	 */
	public List<LoanModel> LoanList;
	
	/**
	 * 滚动文字信息
	 */
	public List<String> ScrollInfoList;
	
	public String bottomInfo;
	
}
