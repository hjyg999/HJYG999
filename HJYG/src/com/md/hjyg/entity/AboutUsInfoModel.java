package com.md.hjyg.entity;

import java.util.List;

/** 
 * ClassName: AboutUsInfoModel 
 * date: 2016-3-28 上午10:59:00 
 * remark: 关于我们
 * @author pyc
 */
public class AboutUsInfoModel {
	
	/**
	 * 联系我们信息
	 */
	public String ContractUsInfo;
	public List<AboutUsDesModel> aboutUsDesModel;
	
	public class AboutUsDesModel{
		/**标题：*/
		public String tittle;
		/**内容：*/
		public String content;
	}

}
