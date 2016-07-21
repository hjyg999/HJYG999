package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: BidLoanResultModel date: 2016-7-7 下午2:06:02 remark:购买返回的红包信息
 * 
 * @author pyc
 */
public class BidLoanResultModel {
	/** 结果： */
	public boolean Result;
	/** 消息： */
	public String Message;
	/** 投资ID： */
	public int InvestId;
	/** 是否登录： */
	public boolean Login;
	/** 是否有红包： */
	public boolean IsRedEnvelope;
	/** 红包列表： */
	public List<RedEnvelopeModel> RedEnvelopeList;

}
