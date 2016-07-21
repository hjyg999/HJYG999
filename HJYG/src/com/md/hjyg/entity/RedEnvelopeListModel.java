package com.md.hjyg.entity;

/**
 * 我的投标-投标中-已经选择红包的展开列表 ClassName: RedEnvelopeListModel date: 2016-7-8
 * 上午11:40:58 remark:
 * 
 * @author pyc
 */
public class RedEnvelopeListModel {
	private int imgId;
	private String type;
	private String account;

	public RedEnvelopeListModel(int imgId, String type, String account) {
		super();
		this.imgId = imgId;
		this.type = type;
		this.account = account;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

}
