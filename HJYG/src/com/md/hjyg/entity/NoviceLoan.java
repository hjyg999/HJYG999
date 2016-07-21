package com.md.hjyg.entity;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.entity.NewUserSpecialistModel.LoanDetailsModel;

/**
 * ClassName: NoviceLoan date: 2016-3-5 下午3:45:51 remark: 新手标信息
 * 
 * @author pyc
 */
public class NoviceLoan {

	// "loanDetails":null,
	// "RealName":null,"LoanDateType":0,
	// "RegName":null,

	/**
	 * 借款人用户Id
	 */
	public int MemberID;

	/**
	 * 标题
	 */
	public String Title;

	/**
	 * 年利率
	 */
	public double LoanRate;

	/**
	 * 借款期限
	 */
	public int LoanTerm;

	/**
	 * 借款方式 4-一次性 5 按月等额本息10按月等额本息线下11按月付息到期还本
	 */
	public int RepaymentWay;

	/**
	 * 借款期限单位 0-按月 2-按天4-按周
	 */
	public int LoanDateType;

	/**
	 * 投资金额
	 */
	public double InvestAmount;
	/**
	 * 投资人数
	 */
	public int InvestCount;

	/**
	 * 开始时间
	 */
	public String startTime;

	/**
	 * 结束时间
	 */
	public String endTime;

	/**
	 * 创建时间
	 */
	public String CreateTime;

	/**
	 * 按钮的状态
	 */
	public int ButtonStatus;
	/**
	 * id
	 */
	public int Id;

	/**
	 * 预期收益
	 */
	public double LoanInterest;

	/**
	 * 项目加密Id,
	 */
	public String EncryptedId;

	/**
	 * 项目介绍
	 */
	public List<LoanDetailsModel> loanDetails;
	public ArrayList<LoanGift> loanGift;

	/**
	 * 加载的图片
	 */
	public List<LoanPicture> loanPicture;

	public String RealName;
	public String RegName;
	/**
	 * 项目总额
	 */
	public double AmountDifference;
	/**
	 * 商品价格
	 */
	public double Price;
	/**
	 * 供应商
	 */
	public String Introduction;
	/**
	 * 新手标 = 0, 礼品标 = 1, 礼品标1 = 2
	 */
	public int Type;
	public int GroupType;

	/**
	 * 图片 ClassName: LoanPicture date: 2016-4-6 下午3:12:32 remark:
	 * 
	 * @author pyc
	 */
	public class LoanPicture {
		public final static int PC小图标 = 0;
		public final static int PC首页轮播图 = 1;
		public final static int PC投标页轮播图 = 2;
		public final static int PC产品介绍图片 = 3;
		public final static int 移动端产品图标 = 4;
		public final static int 移动端产品介绍图片 = 5;
		public final static int 移动端投标页图片 = 6;
		public final static int PC列表图_7 = 7;
		public final static int PC投标页推荐图_8 = 8;
		public final static int PC投标成功图_9 = 9;
		public final static int 移动端列表图_10 = 10;

		public String URL;
		public int NoviceLoanId;
		public int Id;
		public int Type;
	}

}
