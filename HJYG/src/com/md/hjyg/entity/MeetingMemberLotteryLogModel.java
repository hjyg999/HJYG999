package com.md.hjyg.entity;


/** 
 * ClassName: MeetingMemberLotteryLogModel 
 * date: 2016-3-14 上午10:26:16 
 * remark: 会销中奖Model
 * @author pyc
 */
public class MeetingMemberLotteryLogModel {
	
    // 奖品名称
    public String PrizeName;
    // 奖品ID
    public int LotteryActivityId;
    // 中奖数量或者奖品数量
    public int PrizeNumber;
    
    public int type ;

    // 中奖列表
    public java.util.List<MemberLotteryLog> MemberLotteryLogList;
    
}
