package com.md.hjyg.entity;

/**
 * ClassName: AwardInfo date: 2015-10-23 下午3:00:42 remark:
 * 
 * @author pyc
 */
public class AwardInfo {
	/** 显示的小图 */
	private int bitmapId;
	/** 中奖时的大图 */
	private int mbitmapId;
	/** 红包金额 */
	private int redAmount;

	public AwardInfo(int bitmapId, int mbitmapId) {
		this.bitmapId = bitmapId;
		this.mbitmapId = mbitmapId;
	}

	public AwardInfo(int bitmapId, int mbitmapId, int redAmount) {
		this.bitmapId = bitmapId;
		this.mbitmapId = mbitmapId;
		this.redAmount = redAmount;
	}

	public int getBitmapId() {
		return bitmapId;
	}

	public void setBitmapId(int bitmapId) {
		this.bitmapId = bitmapId;
	}

	public int getMbitmapId() {
		return mbitmapId;
	}

	public void setMbitmapId(int mbitmapId) {
		this.mbitmapId = mbitmapId;
	}

	public int getRedAmount() {
		return redAmount;
	}

	public void setRedAmount(int redAmount) {
		this.redAmount = redAmount;
	}

}
