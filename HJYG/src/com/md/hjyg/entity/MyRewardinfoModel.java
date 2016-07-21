package com.md.hjyg.entity;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.entity.AnnualMeetingLotteryInfoModel.MemberLotteryLogModel;

/**
 * ClassName: MyRewardinfoModel date: 2015-12-28 上午10:06:09 remark: 我的中奖记录
 * 
 * @author pyc
 */
public class MyRewardinfoModel {
	

    // 结束时间
    public String EndTime;

    // 当前时间
    public String NowTime;

    // 满足条件的投资次数
    public int HavetotalNumber;

    // 已经抽过的次数
    public int HaveGet;


	/** 剩余次数: */
	public int HaveNumber;
    /**用户中奖列表*/
	public List<MemberLotteryLogModel> MemberLotteryLogList = new ArrayList<AnnualMeetingLotteryInfoModel.MemberLotteryLogModel>();
	
	/**我的奖品温馨提示*/
    public String Notice;
    public Notification notification;

}
