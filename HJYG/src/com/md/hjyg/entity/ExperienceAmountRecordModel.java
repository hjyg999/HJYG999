package com.md.hjyg.entity;

import java.util.ArrayList;

/**
 * ClassName: ExperienceAmountRecordModel date: 2015-12-19 下午4:51:33
 * remark:活期宝体验金--资金记录
 * 
 * @author pyc
 */
public class ExperienceAmountRecordModel {

	/** 消息对象： */
	public Notification notification;
	public java.util.List<ExperienceAmountFinanceLog> list = new ArrayList<ExperienceAmountFinanceLog>();

	public class ExperienceAmountFinanceLog {
		/** 交易金额-收: */
		public String IncomeExperience;
		/** 交易金额-支: */
		public String OutcomeExperience;
		/** 本次余额: */
		public String LeaveExperienceAmount;
		/** 类型: */
		public String type;
		/** 交易时间: */
		public String CreateTime;
		/** 描述: */
		public String Remark;
	}

}
