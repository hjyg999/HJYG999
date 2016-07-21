package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: OverTimeFinLogDetailListModel date: 2016-4-28 下午2:03:22
 * remark:我的投资-回款逾期信息列表
 * 
 * @author pyc
 */
public class OverTimeFinLogDetailListModel {
	public List<OverTimeFinLogDetailModel> List;

	public class OverTimeFinLogDetailModel {
		/** 金额： */
		public String Amount;
		/** 时间： */
		public String CreateTime;
	}

}
