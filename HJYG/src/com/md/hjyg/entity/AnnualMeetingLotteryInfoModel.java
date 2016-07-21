package com.md.hjyg.entity;

import java.util.ArrayList;

/** 
 * ClassName: AnnualMeetingLotteryInfoModel 
 * date: 2015-12-25 下午2:27:42 
 * remark:年会奖品信息
 * @author pyc
 */
public class AnnualMeetingLotteryInfoModel {
	/**服务器现在时间*/
	public String NowTime;
	
	/**判断用户是否已经中奖，如果为""则没有，否则为奖品的名称*/
	public String IsPrize;
	/**用户所中奖品的信息*/
	public LotteryActivityPrize LotteryActivityPrize;
	public LotteryActivity LotteryActivity;
	/**中奖名单*/
	public java.util.List<MemberLotteryLogModel> MemberLotteryLogList = new ArrayList<AnnualMeetingLotteryInfoModel.MemberLotteryLogModel>();
	
	/**奖品信息*/
	public java.util.List<LotteryActivityPrize> LotteryActivityPrizeList = new ArrayList<AnnualMeetingLotteryInfoModel.LotteryActivityPrize>();
//	List<LotteryActivityPrize> LotteryActivityPrizeList;
	
	/**
	 * 
	 * ClassName: LotteryActivityPrize 
	 * date: 2015-12-25 下午2:43:43 
	 * remark: 奖品信息
	 * @author pyc
	 */
	public class LotteryActivityPrize{
		
		/**活动ID*/
        public int LotteryActivityId ;
        /** 奖品类型：0 实物奖品、1 投资红包、2 现金红包*/
        public int type ;
        /**奖品名称*/
        public String PrizeName ;
        /**数量*/
        public int PrizeNumber ;
        /**红包金额：现金红包使用. */
        public double RedEnvelopeAmount ;
        /**红包ID：投资红包使用*/
        public int RedEnvelopeId ;
        /**是否默认*/
        public boolean IsDefault ;
        /** 体验金金额*/
        public double ExperienceAmount ;
        /** 体验金有效期，天*/
        public int ExperienceTerm ;
		
	}
	/**
	 * 
	 * ClassName: MemberLotteryLogModel 
	 * date: 2015-12-25 下午2:48:22 
	 * remark:中奖人信息
	 * @author pyc
	 */
	public class MemberLotteryLogModel{
		
		
		/** 活动名称:*/
		public String Title;
		
		/** 手机号*/
        public String MobilePhone ;
        /** 真实姓名*/
        public String RealName ;
        /** 奖品名称*/
        public String PrizeName ;
        /** 推荐人*/
        public String CommInfoRegName ;
        /** 理财红包是否使用*/
        public boolean IsRedEnvelope ;
        /** 奖品类型*/
        public int type ;
        /** 现金红包中奖金额*/
        public double RedEnvelopeAmount ;
        /**投资红包中奖金额*/
        public double InvestRedAmount ;
        /**金豆个数*/
        public String GoldBeanNumber;
        /** 中奖时间*/
        public String CreateTime ;
        /** 奖品类型*/
        public int Status ;
        /** 活动开始时间*/
        public String startTime ;
        /**活动结束时间*/
        public String endTime ;
        /**有效期*/
        public String time;
        /**备注说明*/
        public String Remark ;
        /** 中奖用户ID*/
        public int MemberId ;
        /** 奖品ID*/
        public int LotteryActivityId ;
        /**体验金红包中奖金额*/
        public double ExperienceAmount ;
		
	}
	
	public class LotteryActivity{
		/**活动简介*/
        public String Title;

        /**活动开始时间 */
        public String startTime;

        /** 结束时间  */     
        public String endTime;

        /** 创建时间 */
        public String CreateTime;

	}

}
