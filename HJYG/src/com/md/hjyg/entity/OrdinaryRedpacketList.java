package com.md.hjyg.entity;

import java.util.List;

/** 
 * ClassName: OrdinaryRedpacketList 
 * date: 2016-7-11 下午4:26:10 
 * remark:我的红包-普通红包
 * @author pyc
 */
public class OrdinaryRedpacketList {
	
	public boolean Result;
	public int pageIndex;
	public int pageTotal;
	public int pageCount;
	public List<RedEnvelopePgedListModel> List;
	
	public class RedEnvelopePgedListModel {
		public String type;
		public String ToType;
		public String First;
		public String Amount;
		public String AmountInfo;
		public String ExperienceAmountInfo;
		public String CreateTime;
		public String startTime;
		public String endTime;
		public String Introduction;
		public String Remark;
		public String ExperienceAmount;
		public String LotteryActivityRedEnvelopeAmount;
		public String LotteryActivityExperienceAmount;
		public String dType;
		public int newType;
		public String pid;
		public String id;
		public String iCreateTime;
	}

}
