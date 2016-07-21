package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: GoldBeanLotteryInfoModel date: 2016-6-16 上午10:30:56 remark:金豆抽奖页详情
 * 
 * @author pyc
 */
public class GoldBeanLotteryInfoModel {
	/**
	 * 抽奖页面信息（包含奖品信息，每次抽奖消耗的金豆）
	 */
	public GoldBeanLottery goldBeanLottery;

	/** 用户剩余金豆数量 */
	public String LeaveGoldBean;
	/** 中奖名单 */
	public List<GoldBeanMemberLotteryLog> MemberLotteryLogList;

	/**
	 * 抽奖信息 ClassName: GoldBeanLottery date: 2016-6-16 上午10:53:50 remark:
	 * 
	 * @author pyc
	 */
	public class GoldBeanLottery {
		/** 开始时间: */
		public String StartTime;
		/** 结束时间: */
		public String EndTime;
		/** 抽奖一次消耗金豆数: */
		public double ConsumeGoldBean;
		/** 是否有效 */
		public boolean IsValid;
		/** 创建时间: */
		public String CreateTime;
		/** 奖品信息： */
		public List<GoldBeanLotteryPrize> Prize;
	}

	/**
	 * 抽奖奖品信息 ClassName: GoldBeanLotteryPrize date: 2016-6-16 上午10:33:44 remark:
	 * 
	 * @author pyc
	 */
	public class GoldBeanLotteryPrize {
		/** 活动ID: */
		public int GoldBeanLotteryId;
		/** 奖品类型：0 实物奖品、1 投资红包、2 现金红包、3 体验金红包、4金豆 */
		public int Type;
		/** 奖品名称: */
		public String PrizeName;
		/** 数量: */
		public int PrizeNumber;
		/** 是否默认: */
		public boolean IsDefault;
		/** 红包ID: */
		public int RedEnvelopeId;
		/** 奖励金豆数: */
		public double GoldBeanNumber;
		/** 红包金额： */
		public double RedEnvelopeAmount;
		/** 抽奖次数： */
		public int LuckNumber;
		/**
		 * 奖品ID
		 */
		public int Id;

	}

	/**
	 * 金豆抽奖获奖名单信息 ClassName: GoldBeanMemberLotteryLog date: 2016-6-16 上午10:34:24
	 * remark:
	 * 
	 * @author pyc
	 */
	public class GoldBeanMemberLotteryLog {

		/** 中奖ID： */
		public int GoldBeanLotteryPrizeId;
		/** 用户Id： */
		public int MemberId;
		/** 奖品名称： */
		public String PrizeName;
		/** 中金豆数： */
		public double GoldBeanNumber;
		/** 抽中红包ID: */
		public int RedEnvelopeId;
		/** 状态: */
		public int Status;
		/** 备注说明: */
		public String Remark;
		/** 创建时间: */
		public String CreateTime;
		/** 类型: */
		public int Type;
		/** 手机号： */
		public String MobilePhone;
		/** 真实姓名： */
		public String RealName;
		/** 用户名： */
		public String RegName;
	}

}
