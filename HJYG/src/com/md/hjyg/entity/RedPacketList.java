package com.md.hjyg.entity;

public class RedPacketList {
	public String id;
//	public String type = "";
	public String ToType = "";
	public String First = "";
	public String Amount = "";
	public String AmountInfo = "";
	public String CreateTime = "";
	public String startTime = "";
	public String endTime = "";
	public String Introduction = "";
	public String Remark = "";
	public String ExperienceAmount;
	public String ExperienceAmountInfo;
	public String LotteryActivityRedEnvelopeAmount;
	public String LotteryActivityExperienceAmount;
	/**
	 * 投资红包 = 0, 活动红包 = 1, 充值红包 = 2, 注册红包 = 3, 赠送红包 = 4, 点击领取红包 = 5, 生日红包 = 6
	 */
	public int newType;
	public String pid;
	public static final int 投资红包 = 0;
	public static final int 活动红包 = 1;
	public static final int 充值红包  = 2;
	public static final int 注册红包 = 3;
	public static final int 赠送红包 = 4;
	public static final int 体验金红包 = 5;
	public static final int 生日红包 = 6;
	public static final int 生日投资红包无在保 = 7;
	public static final int 生日投资红包有在保 = 8;
	public static final int 现金红包 = 9;
	
	/**
	 * 是否选中
	 */
	private boolean isSelect;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	
}
