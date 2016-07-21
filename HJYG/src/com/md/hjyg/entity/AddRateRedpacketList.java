package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: AddRateRedpacketList date: 2016-7-11 下午4:31:03 remark:我的红包-加息券
 * 
 * @author pyc
 */
public class AddRateRedpacketList {
	public List<IncreaseRateCouponItemModel> list;

	public class IncreaseRateCouponItemModel {
		public String IncreaseRate;
		public String Remark;
		public String startTime;
		public String endTime;
		public String DateTerm;
		public String DateType;
		public String dType;
	}

}
