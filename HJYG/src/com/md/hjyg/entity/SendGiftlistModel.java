package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: SendGiftlistModel date: 2016-4-15 下午1:49:54 remark:新手标和会员专享标
 * 
 * @author pyc
 */
public class SendGiftlistModel {

	/** 新手标列表 */
	public List<NoviceLoan> NoviceLoanList;
	/* 会员专享标列表 */
	public List<NoviceLoan> MemberLoanList;
	/**
	 * 投资须知
	 */
	public String ruleDes;
	/**
	 * 体验金
	 */
	public String expAmount;
	/** 新手标是否显示 */
	public boolean isShowNew;
	/** 会员专项是否显示 */
	public boolean isShowSep;
	/**
	 * 0,显示第一版设计，1，显示第二版设计，以此类推
	 */
	public int showType;

}
