package com.md.hjyg.entity;

/** 
 * ClassName: GoldBaoTransactionModel 
 * date: 2016-2-1 上午10:12:25 
 * remark: 黄金宝交易记录model
 * @author pyc
 */
public class GoldBaoTransactionModel {

	public int type;
	
	public String time;
	
	public String name;
	
	public String account;
	
	public GoldBaoTransactionModel(int type,String time,String name,String account){
		this.type = type;
		this.time = time;
		this.name = name;
		this.account = account;
	}
}
