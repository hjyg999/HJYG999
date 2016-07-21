package com.md.hjyg.entity;

/**
 * ClassName: ShareRedPackageInfoModel date: 2015-12-19 上午10:26:42 
 * remark:用户可分享红包或者体验金信息
 * @author pyc
 */
public class ShareRedPackageInfoModel {
	
	/** 分享红包金额: */
	public double shareAmount;
	/** 分享链接: */
	public String link;
	/** 用于加解密的code: */
	public String code;
	/** 二维码图片字符串: */
	public String QrCode;
	/**是有可分享的红包（可能已经过期）*/
	public boolean IsShare;
	/**是否已过期*/
	public boolean IsOver;
	/**过期金额*/
	public String amount;
	
	/**是否可分享的描述信息：*/
	public String ShareDes;
	/**二维码描述信息：*/
	public String QRDes;
	/**分享的标题：*/
	public String ShareTittle;
	/**分享的内容：*/
	public String ShareContent;

	

}
