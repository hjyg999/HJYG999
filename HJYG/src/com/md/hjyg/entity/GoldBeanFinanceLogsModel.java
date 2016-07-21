package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: GoldBeanFinanceLogsModel date: 2016-6-16 下午4:36:12 remark:金豆交易记录信息
 * 
 * @author pyc
 */
public class GoldBeanFinanceLogsModel {
	/**
	 * 获取金豆记录结果
	 */
	public Notification notification;
	/**
	 * 金豆记录列表
	 */
	public List<GoldBeanFinanceLogModel> list;
	

	public class GoldBeanFinanceLogModel {
		public final static int 充值 = 0;
		public final static int 收回 = 1;
		public final static int 退回回收 = 2;
		public final static int 投资 = 3;
		public final static int 推荐金牌理财师 = 4;
		public final static int 推荐互联网理财师 = 5;
		public final static int 投资消费标 = 6;
		public final static int 抽奖 = 7;
		public final static int 中奖 = 8;
		public final static int 签到 = 9;
		public final static int 签到礼品 = 10;

		/** 用户Id: */
		public int MemberId;
		/** 关联ID: */
		public int RelationId;
		/** 数量: */
		public double Number;
		/** 剩余数: */
		public String LeaveGoldBean;
		/** 交易分类: */
		public int Type;
		/** 创建时间: */
		public String CreateTime;
		/** 描述: */
		public String Remark;
		/** 状态: */
		public int Status;
		/** 交易类型: */
		public int PaymentsType;
		/** 收入金豆数量: */
		public String IncomeNumber;
		/** 支出金豆数量: */
		public String PayNumber;

	}

}
