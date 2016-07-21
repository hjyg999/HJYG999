package com.md.hjyg.entity;

import java.util.List;

/**
 * ClassName: ExhibitionSalelotteryModel date: 2016-3-14 下午1:30:47
 * remark:会销抽奖页面信息
 * 
 * @author pyc
 */
public class ExhibitionSalelotteryModel {
	public Notification notification;
	/** 抽奖次数 */
	public MemberLotteryActivityNumberModel numberModel;
	/** 已经中奖信息 */
	public String oldPrizeInfo;
	public LotteryMeetingActionModel lotteryActivity;
	/** 中奖名单： */
	public List<MemberLotteryLog> memLotLogList;

	/** 奖品 */
	public List<LotteryActivityPrize> lotPrizeList;
	/** 服务器当前时间： */
	public String nowTime;
	/** 会销活动规则*/
    public String exhSaleRule;

	public class MemberLotteryActivityNumberModel {

		/** 已有次数 */
		public int HaveNumber;
		/** 还有次数 */
		public int AlsoNumber;

	}

	public class LotteryActivityPrize {
		/** 活动ID： */
		public int LotteryActivityId;
		/** 奖品类型：0 实物奖品、1 投资红包、2 现金红包、3 体验金红包： */
		public int type;
		/** 奖品名称： */
		public String PrizeName;
		/** 数量： */
		public int PrizeNumber;
		/** 红包金额：现金红包使用： */
		public double RedEnvelopeAmount;
		/** 红包ID：投资红包使用: */
		public int RedEnvelopeId;
		/** 是否默认: */
		public boolean IsDefault;
		/** 体验金金额: */
		public double ExperienceAmount;
		/** 投资红包: */
		public double InvestRedAmount;
		/** 体验金有效期，天: */
		public int ExperienceTerm;
		
		public int Id;
		public double IncreaseRate;
	}

}
