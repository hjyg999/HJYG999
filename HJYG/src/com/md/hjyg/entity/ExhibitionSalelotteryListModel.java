package com.md.hjyg.entity;

import java.util.List;


/** 
 * ClassName: ExhibitionSalelotteryListModel 
 * date: 2016-3-11 下午5:25:05 
 * remark:会销活动列表
 * @author pyc
 */
public class ExhibitionSalelotteryListModel {
	
	public List<LotteryMeetingActionModel> list;
	public Notification notification;
	public LotActResultModel lotActResultModel;
	
	public class LotActResultModel{
		public Notification notification;
		/**已有次数：*/
		public int HaveNumber ;
		/**还有次数：*/
		public int AlsoNumber;
		/**抽中的奖品ID：*/
		public int LotteryActivityPrizeId;
		/**结果：*/
		public boolean Result ;
		/**消息：*/
		public String Message;
	}

}
