package com.md.hjyg.entity;

import java.util.ArrayList;

import com.md.hjyg.entity.AnnualMeetingLotteryInfoModel.MemberLotteryLogModel;

/**
 * ClassName: NewLotteryInfoModel date: 2015-12-26 下午2:56:35 remark:1月份抽奖活动信息
 * 
 * @author pyc
 */
public class NewLotteryInfoModel {
	
	public String NowTime;

	/** 中奖名单信息: */
	public java.util.List<MemberLotteryLogModel> MemberLotteryLogList = new ArrayList<MemberLotteryLogModel>();
	/** 奖品信息: */
	public java.util.List<NewLotteryActivityPrize> PrizeList = new ArrayList<NewLotteryInfoModel.NewLotteryActivityPrize>();
	/** 已有次数: */
	public int HaveNumber;
	/** 还有次数: */
	public int AlsoNumber;
	/**活动信息*/
	public LotteryActivity LotteryActivity;
	/**抽奖规则*/
	public String LotteryRule;

//	/** 中奖名单信息 */
//	public class NewMemberLotteryLog {
//		/** 活动名称: */
//		public String Title;
//		/** 手机号: */
//		public String MobilePhone;
//		/** 真实姓名: */
//		public String RealName;
//		/** 奖品名称: */
//		public String PrizeName;
//		/** 推荐人: */
//		public String CommInfoRegName;
//		/** 理财红包是否使用: */
//		public boolean IsRedEnvelope;
//		/** 奖品类型: */
//		public int type;
//		/** 现金红包中奖金额: */
//		public double RedEnvelopeAmount;
//		/** 投资红包中奖金额: */
//		public double InvestRedAmount;
//		/** 中奖时间: */
//		public String CreateTime;
//		/** 奖品类型: */
//		public int Status;
//		/** 活动开始时间: */
//		public String startTime;
//		/** 活动结束时间: */
//		public String endTime;
//		/** 备注说明: */
//		public String Remark;
//		/** 中奖用户ID: */
//		public int MemberId;
//		/** 奖品ID: */
//		public int LotteryActivityId;
//		/** 体验金红包中奖金额: */
//		public double ExperienceAmount;
//
//	}

	public class NewLotteryActivityPrize {
		/** 活动ID: */
		public int LotteryActivityId;
		/** 奖品类型：0 实物奖品、1 投资红包、2 现金红包: */
		public int type;
		/** 奖品名称: */
		public String PrizeName;
		/** 数量: */
		public int PrizeNumber;
		/** 红包金额：现金红包使用.: */
		public double RedEnvelopeAmount;
		/** 红包ID：投资红包使用: */
		public int RedEnvelopeId;
		/** 是否默认: */
		public boolean IsDefault;
		/** 体验金金额: */
		public double ExperienceAmount;
		/** 体验金有效期，天: */
		public int ExperienceTerm;

	}
	public class LotteryActivity{
		/**活动简介*/
        public String Title;
        
        /**活动Id*/
        public int Id;

        /**活动开始时间 */
        public String startTime;

        /** 结束时间  */     
        public String endTime;

        /** 创建时间 */
        public String CreateTime;

	}

}
