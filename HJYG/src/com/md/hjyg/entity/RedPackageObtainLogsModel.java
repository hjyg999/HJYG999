package com.md.hjyg.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: RedPackageObtainLogsModel date: 2015-12-21 下午4:15:39 remark:
 * 
 * @author pyc
 */
public class RedPackageObtainLogsModel {
	/** 接口调用结果： */
	public Notification notification;
	/** 红包领取列表记录 */
	public List<MemberShareRedEnvelopeLog> list = new ArrayList<RedPackageObtainLogsModel.MemberShareRedEnvelopeLog>();

	public class MemberShareRedEnvelopeLog {
		/** 用户ID */
		public int MemberID;

		/** 手机号 */
		public String Mobile;

		/** 金额 */
		public double Amount;

		/** 创建时间 */
		public String CreateTime;

		/** 红包ID */
		public int WeiXinRedEnvelopeId;
		
		/**资金类型：*/
		public String Type;
		
		/**金额 V1.0.3.0以后使用：*/ 
		public String AmountV;
	}

}
