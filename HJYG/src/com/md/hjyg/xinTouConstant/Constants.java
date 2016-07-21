package com.md.hjyg.xinTouConstant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseActivity;
import com.md.hjyg.activities.BaseFragmentActivity;
import com.md.hjyg.activities.RecommendedShareActivity;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.services.DataFetchService;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

public class Constants {

	public static boolean isReleaseVersion = false;
	/** 抽奖活动开关 */
	public static boolean lotteryIsOpen = false;

	// 接口地址
	public static String Common_URL = "";
	public static String URL_JSON_Loan = "";
	public static String URL_JSON_OBJECT_error = "";
	public static String GetProtocal_URL = "";

	// 首页图片地址
	public static String IMAGE_URL = "";

	static {
		if (isReleaseVersion) {
			// GetProtocal_URL = "http://app.xintou365.com/api/";
			// =====现网https地址====
			Common_URL = "https://app.xintou365.com/api/";
			URL_JSON_Loan = "https://app.xintou365.com/api/LoanApi/GetLoan";
			URL_JSON_OBJECT_error = "https://app.xintou365.com/api/LoanApi/LoanInfo/149";
			IMAGE_URL = "https://www.xintou365.com";
			// =====现网http地址====
			// Common_URL = "http://app.xintou365.com/api/";
			// URL_JSON_Loan = "http://app.xintou365.com/api/LoanApi/GetLoan";
			// URL_JSON_OBJECT_error =
			// "http://app.xintou365.com/api/LoanApi/LoanInfo/149";
			// IMAGE_URL = "http://www.xintou365.com";
		} else {
			// GetProtocal_URL = "http://192.168.0.245:8081/api/";
			// =====测试服务器测试地址====
//			 Common_URL = "http://120.55.94.176:82/api/";
//			 URL_JSON_Loan = "http://120.55.94.176:82/api/LoanApi/GetLoan";
//			 URL_JSON_OBJECT_error =
//			 "http://120.55.94.176:82/api/LoanApi/LoanInfo/149";
//			 IMAGE_URL = "http://120.55.94.176";
			// =====本地https测试地址====
			 Common_URL = "https://192.168.0.245:8083/api/";
			 URL_JSON_Loan = "https://192.168.0.245:8083/api/LoanApi/GetLoan";
			 URL_JSON_OBJECT_error =
			 "https://192.168.0.245:8083/api/LoanApi/LoanInfo/149";
			 IMAGE_URL = "https://192.168.0.245";
			// =====APP模拟现网测试地址====
//			Common_URL = "http://121.40.199.169:82/api/";
//			URL_JSON_Loan = "http://121.40.199.169:82/api/LoanApi/GetLoan";
//			URL_JSON_OBJECT_error = "http://121.40.199.169:82/api/LoanApi/LoanInfo/149";
//			IMAGE_URL = "http://121.40.199.169:82";
			// // =====金融云https测试地址====
			// Common_URL = "https://appt.xintou365.com/api/";
			// URL_JSON_Loan = "https://appt.xintou365.com/api/LoanApi/GetLoan";
			// URL_JSON_OBJECT_error =
			// "https://appt.xintou365.com/api/LoanApi/LoanInfo/149";
			// IMAGE_URL = "https://appt.xintou365.com";
			// =====本地http测试地址====
			// Common_URL = "http://192.168.0.245:8081/api/";
			// URL_JSON_Loan = "http://192.168.0.245:8081/api/LoanApi/GetLoan";
			// URL_JSON_OBJECT_error =
			// "http://192.168.0.245:8081/api/LoanApi/LoanInfo/149";
			// IMAGE_URL = "http://192.168.0.245";
		}
	}

	/** 获取平台使用通讯协议类型 */
	public static String GetProtocalType_URL = GetProtocal_URL
			+ "CommonComponents/GetIsHttpsProtocal";
	/** 平台简介 */
	public static String GetAboutXintouInfo_URL = Common_URL
			+ "CommonComponents/GetAboutXintouInfoPost";
	/** 账户信息 */
	public static String GetAccounInfo_URL = Common_URL + "MemberApi/InfoPost";
	/** 银行卡验证信息 */
	public static String GetBankCardValid_URL = Common_URL
			+ "MemberApi/MemberInfoPost";
	/** 退出登录 */
	public static String LogOut_URL = Common_URL + "MemberApi/LogOutPost";
	/** 获取所有的省 */
	public static String GetProvince_URL = Common_URL
			+ "ProvinceCityApi/GetProvinceCitiesPost";
	/** 获取所有的市 */
	public static String GetCity_URL = Common_URL
			+ "ProvinceCityApi/GetProvinceCityByIdPost/";
	/** 添加银行卡 */
	public static String AddBankCard_URL = Common_URL
			+ "MemberServiceApi/AddBankCardInfoPost";
	/** 获取短信验证码 */
	public static String CallPhoneCodeForBank_URL = Common_URL
			+ "CommonComponents/NewCallPhoneCodeForBankPost";
	/** 获取银行卡信息 */
	public static String BankCardInfo_URL = Common_URL
			+ "MemberServiceApi/BankCardManagerPost";
	/** 修改银行卡信息 */
	public static String UpdateBankCard_URL = Common_URL
			+ "MemberServiceApi/UpdateBankCardInfoPost";
	/** 银行限额列表 */
	public static String BankRestraint_URL = Common_URL
			+ "RechargeApi/BankRestraintPost";
	/** 投资中的信息 */
	public static String GetInvest_URL = Common_URL
			+ "InvestApi/GetInvestingByPagePost";
	/** 活期宝购买记录 */
	public static String MemberPurchaseList_URL = Common_URL
			+ "SingleLoanApi/MemberPurchaseListPost";
	/** 账户能够购买活期宝的信息 */
	public static String AmountCanBuyInfo_URL = Common_URL
			+ "SingleLoanApi/SingleAmountCanBuyPost";
	/** 购买活期宝 */
	public static String BuyHuoqiBaoInfo_URL = Common_URL
			+ "SingleLoanApi/BidPost";
	/** 购买理财项目 */
	public static String BuyInfo_URL = Common_URL + "LoanApi/BidPost";
	/** 获取理财项目详细 */
	public static String InvestInfo_URL = Common_URL + "LoanApi/DetailsPost/";
	/** 修改交易密码 */
	public static String UpdatePayPassword_URL = Common_URL
			+ "MemberApi/UpdatePayPasswordPost";
	/** 修改登录密码 */
	public static String ChangePwd_URL = Common_URL + "MemberApi/ChangePwdPost";
	/** 设置登录密码 */
	public static String SetPwd_URL = Common_URL + "MemberApi/SetPwdPost";
	/** 设置登录密码 */
	public static String SetNewPwd_URL = Common_URL + "MemberApi/SetNewPwdPost";
	/** 设置登录密码 */
	public static String SetPwdToChange_URL = Common_URL
			+ "MemberApi/SetPwdToChangePost";
	/** 获取短信验证码 */
	public static String CallPhoneCode_URL = Common_URL
			+ "CommonComponents/CallPhoneCodePost";
	/** 修改用户设备编码 */
	public static String UpdateImeiCode_URL = Common_URL
			+ "MemberApi/updateImei";
	/** 忘记密码获取短信验证码 */
	public static String NewCallPhoneCode_URL = Common_URL
			+ "CommonComponents/NewCallPhoneCodePost";
	/** 联系我们 */
	public static String GetContractUsInfo_URL = Common_URL
			+ "CommonComponents/GetContractUsInfoPost";
	/** 红包列表 */
	// public static String GetRedEnvelope_URL = Common_URL
	// + "MemberServiceApi/GetRedEnvelopePost";
	public static String GetRedEnvelope_URL = Common_URL
			+ "MemberServiceApi/GetRedEnvelopeNewPost";
	/** 激活红包的接口 */
	public static String GetUseRedEnvelope_URL = Common_URL
			+ "MemberServiceApi/UseRedEnvelope";
	/** 我的活期宝信息 */
	public static String SingleLoanWithdrawInfo_URL = Common_URL
			+ "SingleLoanApi/SingleLoanWithdrawInfoPost";
	/** 提取活期宝 */
	public static String SingleLoanWithdraw_URL = Common_URL
			+ "SingleLoanApi/SingleLoanWithdrawPost";
	/** 意见反馈 */
	public static String AddFeedback_URL = Common_URL
			+ "FeedbackLogApi/AddFeedbackPost";
	/** 使用短信验证找回密码调用的接口 */
	public static String ForgotPwdValidateNameByMobile_URL = Common_URL
			+ "MemberApi/ForgotPwdValidateNameByMobilePost";
	/** 使用短信验证找回密码调用的接口 */
	public static String ForgotPwdByMobile_URL = Common_URL
			+ "MemberApi/ForgotPwdByMobilePost";
	/** 使用邮箱地址找回密码调用的接口 */
	public static String ForgotPwdValidateNameByEmail_URL = Common_URL
			+ "MemberApi/ForgotPwdValidateNameByEmailPost";
	/** 邮箱验证码 */
	public static String NewCallEmailCode_URL = Common_URL
			+ "CommonComponents/NewCallEmailCodePost";
	/** 邮箱验证码 */
	public static String ForgotPwdValidateEmailCode_URL = Common_URL
			+ "MemberApi/ForgotPwdValidateEmailCodePost";
	/** 邮箱验证码 */
	public static String ForgotPwdByEmailAjax_URL = Common_URL
			+ "MemberApi/ForgotPwdByEmailAjaxPost";
	/** 用户是否被锁定 */
	public static String GetLockInfo_URL = Common_URL
			+ "MemberApi/GetLockInfoPost";
	/** 我的投资 */
	public static String GetRepaymentListReceivedByPage_URL = Common_URL
			+ "InvestApi/GetRepaymentListReceivedByPagePost";
	/** 我的投资 */
	public static String GetNewRepaymentListReceivedByPage_URL = Common_URL
			+ "InvestApi/GetNewRepaymentListReceivedByPagePost";
	/** 项目回款信息 */
	public static String GetMemberInvestDetail_URL = Common_URL
			+ "InvestApi/GetMemberInvestDetail";
	/** 获取投资列表详细信息按照年月（日历页面） */
	public static String GetRepaymentCalendarDetail_URL = Common_URL
			+ "InvestApi/GetRepaymentCalendarDetail";
	/** 项目预期利息收益信息 */
	public static String GetOverTimeFinanceLogDetail_URL = Common_URL
			+ "InvestApi/GetOverTimeFinanceLogDetail";
	/** 版本更新 */
	public static String UpdateVersion_URL = Common_URL
			+ "MemberServiceApi/UpdateVersionPost";
	/** 活期宝详情 */
	public static String HuoQiBaoDetails_URL = Common_URL
			+ "SingleLoanApi/DetailsPost";
	/** 活期宝体验金详情 */
	public static String ExperienceDetails_URL = Common_URL
			+ "SingleLoanApi/ExperienceDetailsPost";
	/** 获取活期宝体验金资金记录详情 */
	public static String GetExperienceAmountFinanceLogList_URL = Common_URL
			+ "MemberServiceApi/GetExperienceAmountFinanceLogListPost";
	/** 获取活期宝体验金资金记录详情-收益记录 */
	public static String MemberExperienceInterestList_URL = Common_URL
			+ "SingleLoanApi/MemberExperienceInterestListPost";
	/** 获取活期宝交易记录 */
	public static String SingleLoanInfo_URL = Common_URL
			+ "SingleLoanApi/SingleLoanInfoPost";
	/** 项目详情 */
	public static String Details_URL = Common_URL + "LoanApi/DetailsPost/";
	/** 登录 */
	public static String Login_URL = Common_URL + "MemberApi/LoginPost";
	/** 下发Cookie */
	public static String GetCookieResule_URL = Common_URL
			+ "MemberApi/GetCookieStatusPost";
	/** 图像验证码 */
	public static String GetValidateCode_URL = Common_URL
			+ "CommonComponents/GetValidateCodePost";
	/** 图像验证码-只有数字 */
	public static String GetValidateCodeNumPost_URL = Common_URL
			+ "CommonComponents/GetValidateCodeNumPost";
	/** 获取注册按钮的文字信息 */
	public static String getConfigInfo_URL = Common_URL
			+ "CommonComponents/getConfigInfoPost";
	/** 我的活期宝 */
	public static String MemberDemand_URL = Common_URL
			+ "SingleLoanApi/MemberDemandPost";
	/** 信息列表 */
	public static String Message_URL = Common_URL
			+ "MemberServiceApi/MessagePost";
	/** 标记为已读信息 */
	public static String MessageRead_URL = Common_URL
			+ "MemberServiceApi/MessageReadPost";
	/** 删除信息 */
	public static String MessageDelete_URL = Common_URL
			+ "MemberServiceApi/MessageDeletePost";
	/** 未读信息 */
	public static String MessageUnread_URL = Common_URL
			+ "MemberServiceApi/MessageUnreadPost";
	/** 信息详情 */
	public static String MessageDetails_URL = Common_URL
			+ "MemberServiceApi/MessageDetailsPost";
	/** 系统信息列表 */
	public static String GetArticles_URL = Common_URL
			+ "Articles/GetArticlesPost";
	/** 个人信息 */
	public static String RealName_URL = Common_URL
			+ "MemberServiceApi/RealNamePost";
	/** 上传头像 */
	public static String UpdateMemberImageBinaryById_URL = Common_URL
			+ "MemberApi/UpdateMemberImageBinaryByIdPostForAndroid";
	/** 项目详情介绍 */
	public static String GetProjectDetails_URL = Common_URL
			+ "LoanApi/GetProjectDetailsPost/";
	/** 用户活期宝交易信息列表 */
	public static String MemberInterestList_URL = Common_URL
			+ "SingleLoanApi/MemberInterestListPost";
	/** 充值支付前，获取银行卡等信息，用以判断是添加银行卡还是直接充值 */
	public static String GetRechargeDetails_URL = Common_URL
			+ "RechargeApi/GetRechargeDetailsPost";
	/** 支付前处理充值记录 */
	public static String YintongPayPost_URL = Common_URL
			+ "RechargeApi/YintongPayPostPost";
	/** 支付后处理充值记录 */
	public static String YintongPayPostBack_URL = Common_URL
			+ "RechargeApi/YintongPayPostBackPost";
	/** 用户充值记录 */
	public static String GetRechargeLogs_URL = Common_URL
			+ "MemberServiceApi/GetRechargeLogsPost";
	/** 发送理财师申请信息 */
	public static String ApplyFinancialPlanner_URL = Common_URL
			+ "MemberServiceApi/ApplyFinancialPlannerPost";
	/** 推荐人信息 */
	public static String RecommendReward_URL = Common_URL
			+ "MemberServiceApi/RecommendRewardPost";
	/** 获取用户1级推荐人统计数据 */
	public static String GetOneLevelRecommendRewardList_URL = Common_URL
			+ "MemberServiceApi/GetOneLevelRecommendRewardListPost";
	/** 获取用户2级推荐人统计数据 */
	public static String GetTwoLevelRecommendRewardList_URL = Common_URL
			+ "MemberServiceApi/GetTwoLevelRecommendRewardListPost";
	/** 按月获取推荐人统计数据 */
	public static String GetRewardByMonth_URL = Common_URL
			+ "MemberServiceApi/getRewardByMonthPost";
	/** 获取理财师申请规则信息 */
	public static String FinancialPlanner_URL = Common_URL
			+ "MemberServiceApi/FinancialPlannerPost";
	/** 手机是否已注册 */
	public static String IsExitMobile_URL = Common_URL
			+ "MemberApi/IsExitMobilePost";
	/** 推荐人是否存在 */
	public static String IsExitReferrer_URL = Common_URL
			+ "MemberApi/IsExitReferrerPost";
	/** 设置交易密码 */
	public static String ChangePayPwdInfo_URL = Common_URL
			+ "MemberApi/ChangePayPwdInfoPost";
	/** 账户资产信息 */
	public static String GetMember_URL = Common_URL + "MemberApi/GetMemberPost";
	/** 项目交易记录 */
	public static String GetTransactionRecord_URL = Common_URL
			+ "LoanApi/GetTransactionRecordPost/";
	/** 短信验证码 */
	public static String CallPhoneCodeForReg_URL = Common_URL
			+ "CommonComponents/CallPhoneCodeForRegPost";
	/** 手机认证 */
	public static String IsExitVCode_URL = Common_URL
			+ "MemberApi/IsExitVCodePost";
	/** 实名认证 */
	public static String Register_URL = Common_URL + "MemberApi/RegisterPost";
	/** 获取分享推荐人 */
	public static String IsShareMember_URL = Common_URL
			+ "MemberApi/IsShareMemberPost";
	/** 全部资金记录 */
	public static String GetFinanceLogs_URL = Common_URL
			+ "MemberServiceApi/GetFinanceLogsPost";
	/** 提现记录 */
	public static String WithdrawLogsLogs_URL = Common_URL
			+ "MemberServiceApi/WithdrawLogsLogsPost";
	/** 提现界面信息 */
	public static String WithdrawLogs_URL = Common_URL
			+ "MemberServiceApi/WithdrawLogsPost";
	/** 提现 */
	public static String WithdrawLogsPost_URL = Common_URL
			+ "MemberServiceApi/WithdrawLogsPostPost";
	/** 活期宝提现记录 */
	public static String HuoQiBaoMemberWithdrawList_URL = Common_URL
			+ "SingleLoanApi/MemberWithdrawListPost";
	/** 信投宝 */
	public static String GetLoanListXinTB_URL = Common_URL
			+ "LoanApi/GetLoanListXinTBPost";
	/** 定投宝 */
	public static String GetLoanListDingTB_URL = Common_URL
			+ "LoanApi/GetLoanListDingTBPost";
	/** 项目投资 */
	public static String GetLoan_URL = Common_URL + "LoanApi/GetLoanPost";
	/** 主界面信息 */
	public static String GetMainInfo_URL = Common_URL + "HomeApi/GetPost";
	/** 理财师申请成功 */
	public static String IsFinancialplan_URL = Common_URL
			+ "MemberApi/IsFinancialplanPost";
	/** 获取用户可分享红包信息 */
	public static String ShareRedPackageInfoVersionUp_URL = Common_URL
			+ "MemberServiceApi/ShareRedPackageInfoVersionUpPost";
	/** 获取红包领取记录 */
	public static String GetRedPackageObtainLogs_URL = Common_URL
			+ "MemberServiceApi/GetRedPackageObtainLogsPost";
	/** 获取用户可分享体验金信息 */
	public static String ShareExpInfo_URL = Common_URL
			+ "MemberServiceApi/ShareExpInfoPost";
	/** 获取分享体验金领取记录 */
	public static String GetShareExpLogs_URL = Common_URL
			+ "MemberServiceApi/GetShareExpLogsPost";
	/** 获取二维码 */
	public static String GetMemberQRCode_URL = Common_URL
			+ "CommonComponents/GetMemberQRCodePost";
	/** 二月抽奖接口 */
	public static String FebruaryAction_URL = Common_URL
			+ "LotteryApi/FebruaryActionPost";
	/** 我的奖品信息 */
	public static String GetMyRewardinfo_URL = Common_URL
			+ "LotteryApi/GetMyRewardinfo";
	/** 抽奖 */
	public static String FebruaryGoodLuck_URL = Common_URL
			+ "LotteryApi/FebruaryGoodLuckPost";
	/** 新手专享获取购买项目的详细信息 */
	public static String NewUserSpecialist_URL = Common_URL
			+ "LotteryApi/NewUserSpecialist/";
	/** 更新地址 */
	public static String UpdateAddressInfo_URL = Common_URL
			+ "LotteryApi/UpdateAddressInfo";
	/** 新手标购买 */
	public static String LotteryApiBid_URL = Common_URL + "LotteryApi/Bid";
	/** 是否是主持人 */
	public static String IsCompere_URL = Common_URL + "LotteryApi/IsCompere";
	/** 是获取会销活动列表 */
	public static String GetExhibitionSalelotteryList_URL = Common_URL
			+ "LotteryApi/GetExhibitionSalelotteryList";
	/** 获取剩余兑换码输入次数 */
	public static String GetMemberLeaveCount_URL = Common_URL
			+ "LotteryApi/GetMemberLeaveCount";
	/** 获取该主持人下的所有活动列表 */
	public static String GetLotteryActivityList_URL = Common_URL
			+ "LotteryApi/GetLotteryActivityList";
	/** 主持人控制会销开始结束 */
	public static String UpdateMeetingPin_URL = Common_URL
			+ "LotteryApi/UpdateMeetingPin";
	/** 获取会销操作记录 */
	public static String GetMeetingPinOpLogList_URL = Common_URL
			+ "LotteryApi/GetMeetingPinOpLogList";
	/** 获取中奖记录集合 */
	public static String GetMemberLotteryLog_URL = Common_URL
			+ "LotteryApi/GetMemberLotteryLog";
	/** 获取抽奖页面信息 */
	public static String ExhibitionSalelottery_URL = Common_URL
			+ "LotteryApi/ExhibitionSalelottery";
	/** 会销抽奖接口 */
	public static String ExhibitionSalelotteryGoodLuck_URL = Common_URL
			+ "LotteryApi/ExhibitionSalelotteryGoodLuck";
	/** 获取更多里面的相关信息 */
	public static String GetMoreInfo_URL = Common_URL + "Articles/GetMoreInfo";
	/** 获取礼品标信息 */
	public static String ZeroActivitylist_URL = Common_URL
			+ "LotteryApi/ZeroActivitylist";
	/** 关于我们 */
	public static String GetAboutUsInfo_URL = Common_URL
			+ "CommonComponents/GetAboutUsInfo";
	/** 定投宝项目列表信息 */
	public static String GetIndexOfPage_URL = Common_URL
			+ "LoanApi/GetIndexOfPage";
	/** 0元消费节活动投标接口 */
	public static String ZeroActivityBid_URL = Common_URL
			+ "LotteryApi/ZeroActivityBid";
	/** 客户端与服务器端建立session连接接口 */
	public static String Cookie_URL = Common_URL
			+ "CommonComponents/GetSessionId";
	/** 获取新手标信息 */
	public static String GetSendGiftlist_URL = Common_URL
			+ "LotteryApi/GetSendGiftlistForAndroid";
	/** 获取消费标信息 */
	public static String SendGroupGift_URL = Common_URL
			+ "LotteryApi/SendGroupGift";
	/** 会员专享-投资有礼活动详情 */
	public static String UserSpecialHaveGift_URL = Common_URL
			+ "LotteryApi/UserSpecialHaveGift";
	/** 新手专享活动详情 */
	public static String NewUserSpecialistGift_URL = Common_URL
			+ "LotteryApi/NewUserSpecialist";
	/** 获取活动购买界面信息 */
	public static String NewUserSpecialistBuy_URL = Common_URL
			+ "LotteryApi/NewUserSpecialistBuy";
	/** 获取活动标购买接口 */
	public static String ActivityBid_URL = Common_URL
			+ "LotteryApi/ActivityBid";
	/** 太平洋保险接口 */
	public static String GetCPICInfo_URL = Common_URL + "Articles/GetCPICInfo";
	/** 更多-资讯列表接口 */
	public static String GetMobileArticlesListofNews_URL = Common_URL
			+ "Articles/GetMobileArticlesListofNews";
	/** 更多-资讯热点接口 */
	public static String InformationTechnology_URL = Common_URL
			+ "Articles/InformationTechnology";
	/** 获取银行卡开户名 */
	public static String CardBinInfo_URL = Common_URL
			+ "RechargeApi/GetCardBinInfo";
	/** 金豆签到接口 */
	public static String GBSignin_URL = Common_URL
			+ "MemberServiceApi/Signin";
	/** 金豆-获取七天签到情况接口 */
	public static String GBGetSinginLogs_URL = Common_URL
			+ "MemberServiceApi/GetSinginLogs";
	/** 金豆-获取抽奖页详情 */
	public static String GoldBeanLotteryInfo_URL = Common_URL
			+ "MemberServiceApi/GoldBean";
	/** 金豆-抽奖接口 */
	public static String GoldBeanGoodLuck_URL = Common_URL
			+ "MemberServiceApi/GoldBeanGoodLuck";
	/** 金豆-金豆记录信息获取 */
	public static String GoldBeanFinanceLogs_URL = Common_URL
			+ "MemberServiceApi/GoldBeanFinanceLogs";
	/** 金豆-我的金豆 */
	public static String MyGoldBeans_URL = Common_URL
			+ "MemberServiceApi/MyGoldBeans";
	
	/** 投标结束后，选择相应红包增加红包使用记录接口 */
	public static String AddRedEnvelopeLog_URL = Common_URL
			+ "LoanApi/AddRedEnvelopeLog";
	/** 我的红包-获取加息券列表 */
	public static String GetIncreaseRateCouponLog_URL = Common_URL
			+ "MemberServiceApi/GetIncreaseRateCouponLog";
	/** 我的红包-我的红包列表 */
	public static String GetRedEnvelope_ADD_URL = Common_URL
			+ "MemberServiceApi/GetRedEnvelope";
	/** 快乐微文-获取微文活动用户是否可编辑 */
	public static String HappyMicroTextInvolvement_URL = Common_URL
			+ "LotteryApi/HappyMicroTextInvolvement";
	/** 快乐微文-上传头像 */
	public static String UpLoadMicroTextImg_URL = Common_URL
			+ "MemberApi/UpLoadMicroTextImg";
	/** 快乐微文-上提交微文接口 */
	public static String AddMicroTextCompetition_URL = Common_URL
			+ "LotteryApi/AddMicroTextCompetition";

	public static void setUrl() {
		//** 快乐微文-上提交微文接口 */
		AddMicroTextCompetition_URL = Common_URL
				+ "LotteryApi/AddMicroTextCompetition";
		//** 快乐微文-上传头像 */
		UpLoadMicroTextImg_URL = Common_URL
				+ "MemberApi/UpLoadMicroTextImg";
		//** 快乐微文-获取微文活动用户是否可编辑 */
		HappyMicroTextInvolvement_URL = Common_URL
				+ "LotteryApi/HappyMicroTextInvolvement";
		//** 我的红包-我的红包列表 */
		GetRedEnvelope_ADD_URL = Common_URL
				+ "MemberServiceApi/GetRedEnvelope";
		//** 我的红包-获取加息券列表 */
		GetIncreaseRateCouponLog_URL = Common_URL
				+ "MemberServiceApi/GetIncreaseRateCouponLog";
		//** 投标结束后，选择相应红包增加红包使用记录接口 */
		AddRedEnvelopeLog_URL = Common_URL
				+ "LotteryApi/AddRedEnvelopeLog";
		//** 获取消费标信息 */
		SendGroupGift_URL = Common_URL
				+ "LoanApi/SendGroupGift";
		
		//** 金豆-我的金豆 */
		MyGoldBeans_URL = Common_URL
				+ "MemberServiceApi/MyGoldBeans";
		//** 金豆-金豆记录信息获取 */
		GoldBeanFinanceLogs_URL = Common_URL
				+ "MemberServiceApi/GoldBeanFinanceLogs";
		//** 金豆-抽奖接口 */
		GoldBeanGoodLuck_URL = Common_URL
				+ "MemberServiceApi/GoldBeanGoodLuck";
		//** 金豆-获取抽奖页详情 */
	    GoldBeanLotteryInfo_URL = Common_URL
				+ "MemberServiceApi/GoldBean";
		//** 金豆-获取七天签到情况接口 */
		GBGetSinginLogs_URL = Common_URL
				+ "MemberServiceApi/GetSinginLogs";
		//** 金豆签到接口 */
		GBSignin_URL = Common_URL
				+ "MemberServiceApi/Signin";
		// ** 获取平台使用通讯协议类型*/
		GetProtocalType_URL = GetProtocal_URL
				+ "CommonComponents/GetIsHttpsProtocal";
		// **平台简介*/
		GetAboutXintouInfo_URL = Common_URL
				+ "CommonComponents/GetAboutXintouInfoPost";
		// **账户信息*/
		GetAccounInfo_URL = Common_URL + "MemberApi/InfoPost";
		// **银行卡验证信息*/
		GetBankCardValid_URL = Common_URL + "MemberApi/MemberInfoPost";
		// **退出登录*/
		LogOut_URL = Common_URL + "MemberApi/LogOutPost";
		// **获取所有的省*/
		GetProvince_URL = Common_URL + "ProvinceCityApi/GetProvinceCitiesPost";
		// **获取所有的市*/
		GetCity_URL = Common_URL + "ProvinceCityApi/GetProvinceCityByIdPost/";
		// **添加银行卡*/
		AddBankCard_URL = Common_URL + "MemberServiceApi/AddBankCardInfoPost";
		// **获取短信验证码*/
		CallPhoneCodeForBank_URL = Common_URL
				+ "CommonComponents/NewCallPhoneCodeForBankPost";
		// **获取银行卡信息*/
		BankCardInfo_URL = Common_URL + "MemberServiceApi/BankCardManagerPost";
		// **修改银行卡信息*/
		UpdateBankCard_URL = Common_URL
				+ "MemberServiceApi/UpdateBankCardInfoPost";
		// **银行限额列表*/
		BankRestraint_URL = Common_URL + "RechargeApi/BankRestraintPost";
		// **投资中的信息*/
		GetInvest_URL = Common_URL + "InvestApi/GetInvestingByPagePost";
		// **活期宝购买记录*/
		MemberPurchaseList_URL = Common_URL
				+ "SingleLoanApi/MemberPurchaseListPost";
		// **账户能够购买活期宝的信息*/
		AmountCanBuyInfo_URL = Common_URL
				+ "SingleLoanApi/SingleAmountCanBuyPost";
		// **购买活期宝*/
		BuyHuoqiBaoInfo_URL = Common_URL + "SingleLoanApi/BidPost";
		// **购买理财项目*/
		BuyInfo_URL = Common_URL + "LoanApi/BidPost";
		// **获取理财项目详细*/
		InvestInfo_URL = Common_URL + "LoanApi/DetailsPost/";
		// **修改交易密码*/
		UpdatePayPassword_URL = Common_URL + "MemberApi/UpdatePayPasswordPost";
		// **修改登录密码*/
		ChangePwd_URL = Common_URL + "MemberApi/ChangePwdPost";
		// **设置登录密码*/
		SetPwd_URL = Common_URL + "MemberApi/SetPwdPost";
		SetNewPwd_URL = Common_URL + "MemberApi/SetNewPwdPost";
		// **设置登录密码*/
		SetPwdToChange_URL = Common_URL + "MemberApi/SetPwdToChangePost";
		// **获取短信验证码*/
		CallPhoneCode_URL = Common_URL + "CommonComponents/CallPhoneCodePost";
		// **忘记密码获取短信验证码*/
		NewCallPhoneCode_URL = Common_URL
				+ "CommonComponents/NewCallPhoneCodePost";
		// **联系我们*/
		GetContractUsInfo_URL = Common_URL
				+ "CommonComponents/GetContractUsInfoPost";
		// **红包列表*/
		GetRedEnvelope_URL = Common_URL
				+ "MemberServiceApi/GetRedEnvelopeNewPost";
		// **激活红包的接口*/
		GetUseRedEnvelope_URL = Common_URL + "MemberServiceApi/UseRedEnvelope";
		// **我的活期宝信息*/
		SingleLoanWithdrawInfo_URL = Common_URL
				+ "SingleLoanApi/SingleLoanWithdrawInfoPost";
		// **提取活期宝*/
		SingleLoanWithdraw_URL = Common_URL
				+ "SingleLoanApi/SingleLoanWithdrawPost";
		// **意见反馈*/
		AddFeedback_URL = Common_URL + "FeedbackLogApi/AddFeedbackPost";
		// **使用短信验证找回密码调用的接口*/
		ForgotPwdValidateNameByMobile_URL = Common_URL
				+ "MemberApi/ForgotPwdValidateNameByMobilePost";
		// **使用短信验证找回密码调用的接口*/
		ForgotPwdByMobile_URL = Common_URL + "MemberApi/ForgotPwdByMobilePost";
		// **使用邮箱地址找回密码调用的接口*/
		ForgotPwdValidateNameByEmail_URL = Common_URL
				+ "MemberApi/ForgotPwdValidateNameByEmailPost";
		// **邮箱验证码*/
		NewCallEmailCode_URL = Common_URL
				+ "CommonComponents/NewCallEmailCodePost";
		// **邮箱验证码*/
		ForgotPwdValidateEmailCode_URL = Common_URL
				+ "MemberApi/ForgotPwdValidateEmailCodePost";
		// **邮箱验证码*/
		ForgotPwdByEmailAjax_URL = Common_URL
				+ "MemberApi/ForgotPwdByEmailAjaxPost";
		// **用户是否被锁定*/
		GetLockInfo_URL = Common_URL + "MemberApi/GetLockInfoPost";
		// **我的投资*/
		GetRepaymentListReceivedByPage_URL = Common_URL
				+ "InvestApi/GetRepaymentListReceivedByPagePost";
		// **我的投资*/
		GetNewRepaymentListReceivedByPage_URL = Common_URL
				+ "InvestApi/GetNewRepaymentListReceivedByPagePost";
		// **版本更新*/
		UpdateVersion_URL = Common_URL + "MemberServiceApi/UpdateVersionPost";
		// **活期宝详情*/
		HuoQiBaoDetails_URL = Common_URL + "SingleLoanApi/DetailsPost";
		// **活期宝体验金详情*/
		ExperienceDetails_URL = Common_URL
				+ "SingleLoanApi/ExperienceDetailsPost";
		// **获取活期宝体验金资金记录详情*/
		GetExperienceAmountFinanceLogList_URL = Common_URL
				+ "MemberServiceApi/GetExperienceAmountFinanceLogListPost";
		// **获取活期宝体验金资金记录详情-收益记录*/
		MemberExperienceInterestList_URL = Common_URL
				+ "SingleLoanApi/MemberExperienceInterestListPost";
		// **获取活期宝交易记录*/
		SingleLoanInfo_URL = Common_URL + "SingleLoanApi/SingleLoanInfoPost";
		// **项目详情*/
		Details_URL = Common_URL + "LoanApi/DetailsPost/";
		// **登录*/
		Login_URL = Common_URL + "MemberApi/LoginPost";
		// **下发Cookie*/
		GetCookieResule_URL = Common_URL + "MemberApi/GetCookieStatusPost";
		// **图像验证码*/
		GetValidateCode_URL = Common_URL
				+ "CommonComponents/GetValidateCodePost";
		// **图像验证码-只有数字*/
		GetValidateCodeNumPost_URL = Common_URL
				+ "CommonComponents/GetValidateCodeNumPost";
		// **获取注册按钮的文字信息*/
		getConfigInfo_URL = Common_URL + "CommonComponents/getConfigInfoPost";
		// **我的活期宝*/
		MemberDemand_URL = Common_URL + "SingleLoanApi/MemberDemandPost";
		// *信息列表*/
		Message_URL = Common_URL + "MemberServiceApi/MessagePost";
		// **标记为已读信息*/
		MessageRead_URL = Common_URL + "MemberServiceApi/MessageReadPost";
		// **删除信息*/
		MessageDelete_URL = Common_URL + "MemberServiceApi/MessageDeletePost";
		// **未读信息*/
		MessageUnread_URL = Common_URL + "MemberServiceApi/MessageUnreadPost";
		// **信息详情*/
		MessageDetails_URL = Common_URL + "MemberServiceApi/MessageDetailsPost";
		// **系统信息列表*/
		GetArticles_URL = Common_URL + "Articles/GetArticlesPost";
		// **个人信息*/
		RealName_URL = Common_URL + "MemberServiceApi/RealNamePost";
		// **上传头像*/
		UpdateMemberImageBinaryById_URL = Common_URL
				+ "MemberApi/UpdateMemberImageBinaryByIdPostForAndroid";
		// **项目详情介绍*/
		GetProjectDetails_URL = Common_URL + "LoanApi/GetProjectDetailsPost/";
		// **用户活期宝交易信息列表*/
		MemberInterestList_URL = Common_URL
				+ "SingleLoanApi/MemberInterestListPost";
		// **充值支付前，获取银行卡等信息，用以判断是添加银行卡还是直接充值*/
		GetRechargeDetails_URL = Common_URL
				+ "RechargeApi/GetRechargeDetailsPost";
		// ** 支付前处理充值记录*/
		YintongPayPost_URL = Common_URL + "RechargeApi/YintongPayPostPost";
		// ** 支付后处理充值记录*/
		YintongPayPostBack_URL = Common_URL
				+ "RechargeApi/YintongPayPostBackPost";
		// ** 用户充值记录*/
		GetRechargeLogs_URL = Common_URL
				+ "MemberServiceApi/GetRechargeLogsPost";
		// 发送理财师申请信息 */
		ApplyFinancialPlanner_URL = Common_URL
				+ "MemberServiceApi/ApplyFinancialPlannerPost";
		// 推荐人信息 */
		RecommendReward_URL = Common_URL
				+ "MemberServiceApi/RecommendRewardPost";
		// 获取用户1级推荐人统计数据 */
		GetOneLevelRecommendRewardList_URL = Common_URL
				+ "MemberServiceApi/GetOneLevelRecommendRewardListPost";
		// 获取用户2级推荐人统计数据 */
		GetTwoLevelRecommendRewardList_URL = Common_URL
				+ "MemberServiceApi/GetTwoLevelRecommendRewardListPost";
		// 按月获取推荐人统计数据 */
		GetRewardByMonth_URL = Common_URL
				+ "MemberServiceApi/getRewardByMonthPost";
		// 获取理财师申请规则信息 */
		FinancialPlanner_URL = Common_URL
				+ "MemberServiceApi/FinancialPlannerPost";
		// 手机是否已注册 */
		IsExitMobile_URL = Common_URL + "MemberApi/IsExitMobilePost";
		// 推荐人是否存在 */
		IsExitReferrer_URL = Common_URL + "MemberApi/IsExitReferrerPost";
		// 设置交易密码 */
		ChangePayPwdInfo_URL = Common_URL + "MemberApi/ChangePayPwdInfoPost";
		// 账户资产信息 */
		GetMember_URL = Common_URL + "MemberApi/GetMemberPost";
		// 项目交易记录 */
		GetTransactionRecord_URL = Common_URL
				+ "LoanApi/GetTransactionRecordPost/";
		// 短信验证码 */
		CallPhoneCodeForReg_URL = Common_URL
				+ "CommonComponents/CallPhoneCodeForRegPost";
		// 手机认证 */
		IsExitVCode_URL = Common_URL + "MemberApi/IsExitVCodePost";
		// 实名认证 */
		Register_URL = Common_URL + "MemberApi/RegisterPost";
		// 获取分享推荐人 */
		IsShareMember_URL = Common_URL + "MemberApi/IsShareMemberPost";
		// 全部资金记录 */
		GetFinanceLogs_URL = Common_URL + "MemberServiceApi/GetFinanceLogsPost";
		// 提现记录 */
		WithdrawLogsLogs_URL = Common_URL
				+ "MemberServiceApi/WithdrawLogsLogsPost";
		// 提现界面信息 */
		WithdrawLogs_URL = Common_URL + "MemberServiceApi/WithdrawLogsPost";
		// 提现 */
		WithdrawLogsPost_URL = Common_URL
				+ "MemberServiceApi/WithdrawLogsPostPost";
		// 活期宝提现记录 */
		HuoQiBaoMemberWithdrawList_URL = Common_URL
				+ "SingleLoanApi/MemberWithdrawListPost";
		// 信投宝 */
		GetLoanListXinTB_URL = Common_URL + "LoanApi/GetLoanListXinTBPost";
		// **定投宝*/
		GetLoanListDingTB_URL = Common_URL + "LoanApi/GetLoanListDingTBPost";
		// **项目投资*/
		GetLoan_URL = Common_URL + "LoanApi/GetLoanPost";
		// **主界面信息*/
		GetMainInfo_URL = Common_URL + "HomeApi/GetPost";
		// **理财师申请成功*/
		IsFinancialplan_URL = Common_URL + "MemberApi/IsFinancialplanPost";
		// **获取用户可分享红包信息*/
		ShareRedPackageInfoVersionUp_URL = Common_URL
				+ "MemberServiceApi/ShareRedPackageInfoVersionUpPost";
		// **获取红包领取记录*/
		GetRedPackageObtainLogs_URL = Common_URL
				+ "MemberServiceApi/GetRedPackageObtainLogsPost";
		// **获取用户可分享体验金信息*/
		ShareExpInfo_URL = Common_URL + "MemberServiceApi/ShareExpInfoPost";
		// **获取分享体验金领取记录*/
		GetShareExpLogs_URL = Common_URL
				+ "MemberServiceApi/GetShareExpLogsPost";
		// **获取二维码*/
		GetMemberQRCode_URL = Common_URL
				+ "CommonComponents/GetMemberQRCodePost";
		// **二月抽奖接口*/
		FebruaryAction_URL = Common_URL + "LotteryApi/FebruaryActionPost";
		// **我的奖品信息*/
		GetMyRewardinfo_URL = Common_URL + "LotteryApi/GetMyRewardinfoPost";
		// **抽奖*/
		FebruaryGoodLuck_URL = Common_URL + "LotteryApi/FebruaryGoodLuckPost";
		// **新手专享获取购买项目的详细信息*/
		NewUserSpecialist_URL = Common_URL + "LotteryApi/NewUserSpecialist/";
		/** 更新地址 */
		UpdateAddressInfo_URL = Common_URL + "LotteryApi/UpdateAddressInfo";
		// **新手标购买*/
		LotteryApiBid_URL = Common_URL + "LotteryApi/Bid";
		// **是否是主持人*/
		IsCompere_URL = Common_URL + "LotteryApi/IsCompere";
		// **是获取会销活动列表*/
		GetExhibitionSalelotteryList_URL = Common_URL
				+ "LotteryApi/GetExhibitionSalelotteryList";
		// **获取剩余兑换码输入次数*/
		GetMemberLeaveCount_URL = Common_URL + "LotteryApi/GetMemberLeaveCount";
		// **获取该主持人下的所有活动列表*/
		GetLotteryActivityList_URL = Common_URL
				+ "LotteryApi/GetLotteryActivityList";
		// **主持人控制会销开始结束*/
		UpdateMeetingPin_URL = Common_URL + "LotteryApi/UpdateMeetingPin";
		// **获取会销操作记录*/
		GetMeetingPinOpLogList_URL = Common_URL
				+ "LotteryApi/GetMeetingPinOpLogList";
		// **获取中奖记录集合*/
		GetMemberLotteryLog_URL = Common_URL + "LotteryApi/GetMemberLotteryLog";
		// **获取抽奖页面信息*/
		ExhibitionSalelottery_URL = Common_URL
				+ "LotteryApi/ExhibitionSalelottery";
		// **会销抽奖接口*/
		ExhibitionSalelotteryGoodLuck_URL = Common_URL
				+ "LotteryApi/ExhibitionSalelotteryGoodLuck";
		// **获取更多里面的相关信息*/
		GetMoreInfo_URL = Common_URL + "Articles/GetMoreInfo";
		// **获取礼品标信息*/
		ZeroActivitylist_URL = Common_URL + "LotteryApi/ZeroActivitylist";
		// **关于我们*/
		GetAboutUsInfo_URL = Common_URL + "CommonComponents/GetAboutUsInfo";
		// **定投宝项目列表信息*/
		GetIndexOfPage_URL = Common_URL + "LoanApi/GetIndexOfPage";
		// 0元消费节活动投标接口
		ZeroActivityBid_URL = Common_URL + "LotteryApi/ZeroActivityBid";
		// 礼品标和新手标
		GetSendGiftlist_URL = Common_URL + "LotteryApi/GetSendGiftlist";
		// 会员专享-投资有礼活动详情
		UserSpecialHaveGift_URL = Common_URL + "LotteryApi/UserSpecialHaveGift";
		// 新手专享活动详情
		NewUserSpecialistGift_URL = Common_URL + "LotteryApi/NewUserSpecialist";
		// 活动购买界面信息
		NewUserSpecialistBuy_URL = Common_URL
				+ "LotteryApi/NewUserSpecialistBuy";
		// **获取活动标购买接口*/
		ActivityBid_URL = Common_URL + "LotteryApi/ActivityBid";
		// **太平洋保险接口*/
		GetCPICInfo_URL = Common_URL + "Articles/GetCPICInfo";
		// ** 项目回款信息 */
		GetMemberInvestDetail_URL = Common_URL
				+ "InvestApi/GetMemberInvestDetail";
		// ** 项目预期利息收益信息 */
		GetOverTimeFinanceLogDetail_URL = Common_URL
				+ "InvestApi/GetOverTimeFinanceLogDetail";
		// ** 获取投资列表详细信息按照年月（日历页面）*/
		GetRepaymentCalendarDetail_URL = Common_URL
				+ "InvestApi/GetRepaymentCalendarDetail";
		/** 更多-资讯接口 */
		GetMobileArticlesListofNews_URL = Common_URL
				+ "Articles/GetMobileArticlesListofNews";
		/** 更多-资讯列表接口 */
		InformationTechnology_URL = Common_URL
				+ "Articles/InformationTechnology";
		/** 获取银行卡开户名 */
		CardBinInfo_URL = Common_URL + "RechargeApi/GetCardBinInfo";
	}

	public boolean xintoutab = false;
	public boolean latest_loantab = true;
	public String sms_email_verification = "";
	// public static AlertDialog.Builder errorDialog = null;
	// public static AlertDialog.Builder popupDialog = null;
	public static Dialog dialog = null;
	public static ProgressDialog progress_dialog;
	public static String NewsID = "";
	public static String Code = "";
	public static String VerificationCode = "";
	public static String VerificationCode_Register = "";

	public static String Recommended_Type = "";
	public static String encryption_key = "sKnc46B3$D68a#4e8F@aB7v^2cQd3cEb47b9";

	public static final String IDENTIFY = "android"; // 手机类型标识

	// */ 手势密码点的状态
	public static final int POINT_STATE_NORMAL = 0; // 正常状态

	public static final int POINT_STATE_SELECTED = 1; // 按下状态

	public static final int POINT_STATE_WRONG = 2; // 错误状态
	/** 每次最少加载条目 */
	public static final int PAGESIZE = 0;

	/**
	 * 邮寄
	 */
	public static final int express = 0;
	/**
	 * 自取
	 */
	public static final int myself = 1;

	// 应用宝appID
	public static long APP_ID = 1104834619;

	// 应用宝省流量更新渠道ID
	public static String CHANNEL_ID = "1000952";

	// */活期宝新增
	public static String memberId = "";
	public static String singleloanId = "";
	public static double rate;
	public static String LeaveAmount = "";
	public static double investmentamount = 0;
	public static String Leaveamount;
	/**
	 * 金豆个数
	 */
	public static String LeaveGoldBean = "";

	public static ProgressDialog getProgressDialog(Context context) {
		ProgressDialog progress_dialog1 = new ProgressDialog(context);
		progress_dialog1.setMessage("载入中 ... ");
		progress_dialog1.setCancelable(true);
		return progress_dialog1;
	}

	public static String md5(String password) {
		try {
			// Create MD5 Hash
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(password.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean checkStartMobileNumber(String mobile_number) {
		String reg = "^(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(mobile_number);

		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean checkEmail(String mobile_number) {
		String reg = "^([a-z0-9_\\.-]+\\@[\\da-z\\.-]+\\.[a-z\\.]{2,6})$";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(mobile_number);

		if (matcher.matches()) {
			return true;
		} else {

			return false;
		}

	}

	/**
	 * 加密手机号
	 * 
	 * @param mobileNo
	 * @return
	 */
	public static String NewreplacePhoneNumberformat(String mobileNo) {

		String temp_phone_no_details = null;
		try {
			String str1 = mobileNo.substring(0, 3);
			String str2 = mobileNo.substring(mobileNo.length() - 4,
					mobileNo.length());
			String str3 = "";
			for (int i = 3; i < mobileNo.length() - 4; i++) {
				str3 += "*";

				if (i == 7)
					str3 += " ";

			}
			temp_phone_no_details = str1 + " " + str3 + " " + str2;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp_phone_no_details;
	}

	public static boolean checkMobileNumberValid(Activity activity,
			String mobile_number) {
		if (!checkStartMobileNumber(mobile_number)) {
			// Enter Proper Phone numbre
			showOkPopup(activity, "请输入正确的手机号码", new View.OnClickListener() {

				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				//
				// }

				@Override
				public void onClick(View v) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
			return false;
		} else if ((mobile_number.length() < 11)) {
			// Enter 11 digit Phone numbre
			showOkPopup(activity, "请输入手机号码", new View.OnClickListener() {

				// @Override
				// public void onClick(DialogInterface dialog, int which) {
				// dialog.dismiss();
				//
				// }
				@Override
				public void onClick(View v) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 网络是否可能用
	 * 
	 * @param _activity
	 * @return
	 */
	public static boolean checkInternetConnection(Context _activity) {
		ConnectivityManager conMgr = (ConnectivityManager) _activity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() != null
				&& conMgr.getActiveNetworkInfo().isAvailable())
			return true;
		else
			return false;
	}

	public static void Toast_Error(Activity activity, String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 比较两个字符串是否相等
	 * 
	 * @param newpassword
	 * @param confirmpassword
	 * @return
	 */
	public static boolean CheckConfirmedpassword(String newpassword,
			String confirmpassword) {

		if (newpassword.equalsIgnoreCase(confirmpassword)) {
			return true;
		} else {
			return false;
		}

	}

	// public static void showError(Context context, String error_msg,
	// DialogInterface.OnClickListener listener) {
	// errorDialog = new AlertDialog.Builder(context);
	// errorDialog.setCancelable(false);
	// errorDialog.setPositiveButton("确定", listener);
	// errorDialog.setMessage(error_msg);
	// errorDialog.show();
	// }

	// public static void showPopup(Context context, String error_msg,
	// DialogInterface.OnClickListener listener) {
	// popupDialog = new AlertDialog.Builder(context);
	// popupDialog.setCancelable(false);
	// popupDialog.setPositiveButton("确定", listener);
	// popupDialog.setNegativeButton("取消", null);
	// popupDialog.setMessage(error_msg);
	// popupDialog.show();
	// }
	/** 自定义两个按钮的dialog */
	@SuppressLint("InflateParams")
	public static void showPopup(Activity context, String error_msg,
			View.OnClickListener listener) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		dialog = new Dialog(context);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				dialog = null;
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		dialog.setCanceledOnTouchOutside(false);// 点击外部不销毁
		View View = LayoutInflater.from(context).inflate(
				R.layout.layout_alertdialog_view, null);
		dialog.setContentView(View);
		// 信息提示
		TextView dialog_msg = (TextView) View.findViewById(R.id.dialog_msg);
		// 确定按钮
		TextView dialog_ok = (TextView) View.findViewById(R.id.dialog_ok);
		// 取消按钮
		TextView dialog_cancel = (TextView) View
				.findViewById(R.id.dialog_cancel);
		dialog_ok.setOnClickListener(listener);
		// 关闭按钮
		ImageView dialog_close = (ImageView) View
				.findViewById(R.id.dialog_close);
		dialog_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		dialog_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		dialog_msg.setText(error_msg);
		if (!(context).isFinishing()) {
			try {// 抓捕异常，防止程序崩溃
				dialog.show();
			} catch (Exception e) {
			}
		}

	}

	// public static void showOkPopup(Activity context, String error_msg,
	// DialogInterface.OnClickListener listener) {
	// popupDialog = new AlertDialog.Builder(context);
	// popupDialog.setCancelable(false);
	// popupDialog.setPositiveButton("确定", listener);
	// popupDialog.setMessage(error_msg);
	// if (!context.isFinishing()) {
	// try {//抓捕异常，防止程序崩溃
	// popupDialog.show();
	// } catch (Exception e) {
	// }
	// }
	// }
	/**
	 * 初始化Dialog信息，并show（）
	 * 
	 * @param context
	 * @param error_msg
	 *            setMessage信息
	 */
	public static void showOkPopup(Activity context, String error_msg) {
		showOkPopup(context, error_msg, null);
	}

	/**
	 * 初始化一个按钮的Dialog信息，并show（）
	 * 
	 * @param context
	 * @param error_msg
	 *            setMessage信息
	 * @param listener
	 *            按钮监听
	 */
	@SuppressLint("InflateParams")
	public static void showOkPopup(Activity context, String error_msg,
			View.OnClickListener listener) {
		if (dialog != null && dialog.isShowing()) {
			return;
		}
		dialog = new Dialog(context);
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				dialog = null; 
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);
		dialog.setCanceledOnTouchOutside(false);
		View View = LayoutInflater.from(context).inflate(
				R.layout.layout_alertdialog_onebtn_view, null);
		dialog.setContentView(View);
		// 关闭按钮
		ImageView dialog_close = (ImageView) View
				.findViewById(R.id.dialog_close);
		// 信息提示
		TextView dialog_msg = (TextView) View.findViewById(R.id.dialog_msg);
		// 确定按钮
		TextView dialog_ok = (TextView) View.findViewById(R.id.dialog_ok);
		if (listener != null) {
			dialog_ok.setOnClickListener(listener);
		} else {
			dialog_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			});
		}
		dialog_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (dialog.isShowing()) {
					dialog.dismiss();
				}
			}
		});
		dialog_msg.setText(error_msg);
		if (!(context).isFinishing()) {
			try {// 抓捕异常，防止程序崩溃
				dialog.show();
			} catch (Exception e) {
			}
		}

	}

	// public static void showRetryPopup(Context context, String error_msg,
	// DialogInterface.OnClickListener listener) {
	// popupDialog = new AlertDialog.Builder(context);
	// popupDialog.setCancelable(false);
	// popupDialog.setPositiveButton("重试", listener);
	// popupDialog.setMessage(error_msg);
	// popupDialog.show();
	// }

	// public static void showInternetPopup(Activity activity, String error_msg,
	// DialogInterface.OnClickListener listener) {
	// popupDialog = new AlertDialog.Builder(activity);
	// popupDialog.setCancelable(false);
	// popupDialog.setPositiveButton("确定", listener);
	// popupDialog.setMessage(error_msg);
	// popupDialog.show();
	// }

	public static void SetAuthToken(String AuthToken, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("AuthToken", AuthToken);
		editor.commit();
	}

	public static void SetSeesionId(String Cookie, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("Cookie", Cookie);
		editor.commit();
	}

	/**
	 * 设置每天弹出一次首页抽奖活动popupwindow
	 * 
	 * @param isOpenPopupWindow
	 * @param mcontext
	 */
	public static void SetOpenPopupWindowTime(String openPopupWindowTime,
			Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("openPopupWindowTime", openPopupWindowTime);
		editor.commit();
	}

	/**
	 * 获取是否打开过抽奖活动popupwindow
	 */
	public static String GetOpenPopupWindowTime(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getString("openPopupWindowTime", "");
	}

	/**
	 * 保存设置手势密码是否打开
	 * 
	 * @param isOpend
	 * @param mcontext
	 */
	public static void SetGestureLockisOpend(Boolean isOpend, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putBoolean("GestureLockisOpend", isOpend);
		editor.commit();
	}

	/**
	 * 获取手势密码开关是否打开
	 */
	public static Boolean GetGestureLockisOpend(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getBoolean("GestureLockisOpend", false);
	}

	/**
	 * 保存提取方式
	 * 
	 * @param type
	 * @param mcontext
	 */
	public static void SetAwardExtractType(int type, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putInt("AwardExtractType", type);
		editor.commit();
	}

	/**
	 * 获取提取方式
	 */
	public static int GetAwardExtractType(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getInt("AwardExtractType", -1);
	}

	/**
	 * 保存收件地址
	 * 
	 * @param AddressInfo
	 *            地址
	 * @param mcontext
	 */
	public static void SetAwardExtractAddressInfo(String AddressInfo,
			Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("AddressInfo", AddressInfo);
		editor.commit();
	}

	/**
	 * 生日弹窗
	 */
	public static boolean GetBirthpopState(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getBoolean("birthpop", false);
	}

	/**
	 * 生日弹窗
	 */
	public static void SetBirthpopState(boolean isShow, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putBoolean("birthpop", isShow);
		editor.commit();
	}

	/**
	 * 获取收件地址
	 */
	public static String GetAwardExtractAddressInfo(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getString("AddressInfo", "");
	}

	/**
	 * 保存设置的手势密码
	 * 
	 * @param Password
	 * @param mcontext
	 */
	public static void SetGestureLockPassword(String Password, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("GestureLockPassword", Password);
		editor.commit();
	}

	/**
	 * 获取新浪微博是否已经授权
	 */
	public static boolean GetdoSinaOauthVerify(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getBoolean("doSinaOauthVerify", false);
	}

	/**
	 * 保存新浪微博已经授权
	 */
	public static void SetdoSinaOauthVerify(Boolean bol, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putBoolean("doSinaOauthVerify", bol);
		editor.commit();
	}

	/**
	 * 获取保存的手势密码
	 */
	public static String GetGestureLockPassword(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getString("GestureLockPassword", "");
	}

	/**
	 * 保存带*号的手机号码
	 * 
	 * @param ProtectedMobile
	 *            带*号的手机号码
	 * @param mcontext
	 */
	public static void SetProtectedMobile(String ProtectedMobile,
			Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("ProtectedMobile", ProtectedMobile);
		editor.commit();
	}

	/**
	 * 获取带*号的手机号码
	 */
	public static String GetProtectedMobile(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getString("ProtectedMobile", "");
	}

	public static void SavePassword(String password, Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_pass.edit();
		editor.putString("EcrytPassword", password);
		editor.commit();
	}

	public static String GetEncryptedPassword(Context mcontext) {
		SharedPreferences resultpre_pass = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_pass.getString("EcrytPassword", "");
	}

	public static boolean CheckAuthtoken(Context mcontext) {
		if (!Constants.GetResult_AuthToken(mcontext).isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取登录账户
	 * 
	 * @param mcontext
	 * @return
	 */
	public static String GetResult_AuthToken(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("AuthToken", "");
	}

	public static String GetResult_Cookie(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("Cookie", "");
	}

	public static void Clear_Cookie(Context mcontext) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Cookie", "");
		editor.commit();
	}

	public static void ClearSharePref(Context mcontext) {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("savefilename", "");
		editor.putString("AuthToken", "");
		editor.commit();
	}

	/** 设置理财师弹框 */
	public static void SetIsFinancialplan(Boolean IsFinancialplan,
			Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putBoolean("IsFinancialplan", IsFinancialplan);
		editor.commit();
	}

	/** 设置理财师弹框 */
	public static boolean GetIsFinancialplan(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getBoolean("IsFinancialplan", true);
	}

	public static void SetSaveFilename(String savefilename, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("savefilename", savefilename);
		editor.commit();
	}

	public static String GetSaveFilename(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("savefilename", "");
	}

	/** 设置协议类型 */
	public static void SetProtocalType(String savefilename, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("protocalType", savefilename);
		editor.commit();
	}

	/** 获取协议类型 */
	public static String GetProtocalType(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("protocalType", "");
	}

	public static void SetNewsId(String NewsId, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("NewsId", NewsId);
		editor.commit();
	}

	public static String GetResult_NewsId(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("NewsId", "");
	}

	public static void SetSplashscreen(String splash, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("splash", splash);
		editor.commit();
	}

	public static String GetSplashscreen(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("splash", "");
	}

	public static void SetBankNumber(String bankNumber, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("bankNumber", bankNumber);
		editor.commit();
	}

	public static String GetBankNumber(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("bankNumber", "");
	}

	public static void SetBankIdNumber(String bankIdNumber, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("banIdkNumber", bankIdNumber);
		editor.commit();
	}

	public static String GetBankIdNumber(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("bankIdNumber", "");
	}

	public static void SetMemberId(String memberId, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("memberId", memberId);
		editor.commit();
	}

	public static String GetMemberId(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("memberId", "0");
	}

	public static void SetRealName(String realName, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("realName", realName);
		editor.commit();
	}

	/**
	 * 第一次充值，保存银行卡消息，充值成功后，把银行卡保存到数据库
	 * 
	 * @param mcontext
	 * @param bankName
	 * @param bankNumber
	 * @param enConfirmBankNumber
	 * @param selectedProvince
	 * @param selectedCity
	 * @param subbranch
	 * @param mobileCode
	 * @param isFirstAddBankCard
	 */
	public static void SetFirstAddBankCard(Context mcontext, String bankName,
			String bankNumber, String enConfirmBankNumber,
			String selectedProvince, String selectedCity, String subbranch,
			String mobileCode, boolean isFirstAddBankCard) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putBoolean("isFirstAddBankCard", isFirstAddBankCard);
		editor.putString("bankName", bankName);
		editor.putString("bankNumber", bankNumber);
		editor.putString("enConfirmBankNumber", enConfirmBankNumber);
		editor.putString("selectedProvince", selectedProvince);
		editor.putString("selectedCity", selectedCity);
		editor.putString("subbranch", subbranch);
		editor.putString("mobileCode", mobileCode);
		editor.commit();
		editor.clear();

	}

	public static String GetBankName(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("bankName", "");
	}

	/**
	 * 是否为第一次充值
	 * 
	 * @param mcontext
	 * @return
	 */
	public static boolean GetisFirstAddBankCard(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getBoolean("isFirstAddBankCard", false);
	}

	public static void SetisFirstAddBankCard(Boolean isFirstAddBankCard,
			Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putBoolean("isFirstAddBankCard", isFirstAddBankCard);
		editor.commit();
		editor.clear();
	}

	public static String GetRealName(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("realName", "");
	}

	/** 保存最好一次读取公告的公告发布时间 */
	public static void SetNewsOldTime(String oldTime, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putString("oldTime", oldTime);
		editor.commit();
	}

	/** 获取最好一次读取公告的的公告发布时间 */
	public static String GetNewsOldTime(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getString("oldTime", "");
	}
	
	/** 保存服务器当前时间 （yyyy-MM-dd HH:mm:ss） */
	public static void SetServerNowTime(long serverNowTime, Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		SharedPreferences.Editor editor = resultpre_patient.edit();
		editor.putLong("serverNowTime", serverNowTime);
		editor.commit();
	}

	/** 获取服务器当前时间  （yyyy-MM-dd HH:mm:ss）*/
	public static long GetServerNowTime(Context mcontext) {
		SharedPreferences resultpre_patient = PreferenceManager
				.getDefaultSharedPreferences(mcontext);
		return resultpre_patient.getLong("serverNowTime", 0);
	}

	public static Boolean isValidPassword(Activity activity, String password) {

		if (!CheckMinimumChar(password)) {
			// Toast.makeText(activity,
			// "Password must be 6 characters or greater.",
			// Toast.LENGTH_LONG).show();

			showOkPopup(activity, "至少6个长度，最长30位，建议包含大小写字母和数字",
					new View.OnClickListener() {

						// @Override
						// public void onClick(DialogInterface dialog, int
						// which) {
						// dialog.dismiss();
						//
						// }

						@Override
						public void onClick(View v) {
							if (dialog.isShowing()) {
								dialog.dismiss();
							}
						}
					});

			return false;

		} else {
			// Toast.makeText(activity, "password Correct ",
			// Toast.LENGTH_LONG).show();
			// password = pwd;
			return true;
		}
		/*
		 * else if(!ChecklowercasePassword(password)) { Toast.makeText(activity,
		 * "Invalid password - Must have a Lower Case character. ",
		 * Toast.LENGTH_LONG).show(); return false; }
		 */
		/*
		 * else if(!CheckuppercasePassword(password) && !CheckNumber(password))
		 * { //Toast.makeText(activity,
		 * "Invalid passsword - must have one uppercase character and one number. "
		 * , Toast.LENGTH_LONG).show();
		 * 
		 * showOkPopup(activity,
		 * "Invalid passsword - must have one uppercase character and one number."
		 * , new DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss();
		 * 
		 * } });
		 * 
		 * return false; } else if(!CheckuppercasePassword(password)) {
		 * //Toast.makeText(activity,
		 * "Invalid passsword - must have one upper case character. ",
		 * Toast.LENGTH_LONG).show();
		 * 
		 * showOkPopup(activity,
		 * "Invalid passsword - must have one upper case character.", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss();
		 * 
		 * } });
		 * 
		 * 
		 * return false; } else if(!CheckNumber(password)) {
		 * //Toast.makeText(activity,
		 * "Invalid passsword - must have one Number. ",
		 * Toast.LENGTH_LONG).show(); showOkPopup(activity,
		 * "Invalid passsword - must have one Number.", new
		 * DialogInterface.OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * dialog.dismiss();
		 * 
		 * } }); return false; }
		 */

	}

	public static Boolean CheckMinimumChar(String password) {
		if (password.length() >= 6 && password.length() != 0
				&& password.length() < 30) {
			return true;
		}
		return false;

	}

	public static Boolean CheckNumber(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isDigit(c)) {
				return true;
			}
		}
		return false;

	}

	public static Boolean CheckuppercasePassword(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isUpperCase(c)) {
				return true;
			}
		}
		return false;
	}

	public static Boolean ChecklowercasePassword(String password) {
		for (char c : password.toCharArray()) {
			if (Character.isLowerCase(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * sting类型转换成Double类型
	 * 
	 * @param str
	 * @return
	 */
	public static Double StringToDouble(String str) {
		if (str == null) {
			return 0.00;
		}
		while (str.lastIndexOf(",") != -1) {
			str = str.replace(",", "");
		}
		return Double.parseDouble(str);
	}

	/**
	 * 转换成货币格式
	 * 
	 * @param str
	 * @return
	 */
	public static String StringToCurrency(String str) {
		if (str == null) {
			return "";
		}
		/*
		 * for (char c : str.toCharArray()) { if ( Character.isLowerCase(c) ||
		 * Character.isUpperCase(c)) { return str; } }
		 */
		while (str.lastIndexOf(",") != -1) {
			str = str.replace(",", "");
		}
		Double dou = Double.parseDouble(str);
		DecimalFormat df = new DecimalFormat("###,###,###.##");
		str = df.format(dou);
		if (str.indexOf(".") == -1) {
			str += ".00";
		} else if (str.length() - 1 - str.indexOf(".") == 1) {
			str += "0";
		}
		return str;
	}

	/**
	 * ScrollView嵌套ListView,手动设置ListView高度, item的布局必须为LinearLayout
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 获取ListView高度,
	 */
	public static int getListViewHeight(ListView listView) {
		if (listView == null)
			return 0;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		return params.height;
	}

	/** 弹出理财师申请成功按钮 */
	public static void isFinancialplan(final Activity activity,
			final DataFetchService dft) {
		if (Constants.GetResult_AuthToken(activity).length() > 0
				&& !Constants.GetIsFinancialplan(activity)) {
			Constants.showPopup(activity, "您的理财师申请已成功受理，恭喜您成为理财师。邀请好友，赚取更多收益。",
					new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (Constants.dialog.isShowing()) {
								Constants.dialog.dismiss();
							}
							dft.getNetInfoById(IsFinancialplan_URL, Method.GET,
									new Listener<JSONObject>() {

										@Override
										public void onResponse(
												JSONObject response) {

											Notification model = (Notification) dft
													.GetResponseObject(
															response,
															Notification.class);
											if (model.ProcessResult == 1) {
												Constants.SetIsFinancialplan(
														false, activity);
											}
										}
									});
							activity.startActivity(new Intent(activity,
									RecommendedShareActivity.class));
							activity.overridePendingTransition(
									R.anim.trans_right_in,
									R.anim.trans_lift_out);
						}
					});
		}
	}

	/**
	 * 获取Dft
	 * 
	 * @param mActivity
	 * @return
	 */
	public static DataFetchService getDft(Activity mActivity) {
		if (mActivity instanceof BaseActivity) {
			return ((BaseActivity) mActivity).getDft();
		} else if (mActivity instanceof BaseFragmentActivity) {
			return ((BaseFragmentActivity) mActivity).getDft();
		} else {
			return new DataFetchService(mActivity);
		}
	}

	/**
	 * 获取项目期限的单位
	 * 
	 * @param datatype
	 * @return
	 */
	public static String getLoanTermType(int datatype) {
		String type = "";
		if (datatype == 0) {
			type = "个月";
		} else if (datatype == 2) {
			type = "天";
		} else if (datatype == 4) {
			type = "周";
		}
		return type;
	}

	/**
	 * double类型如果小数点后为零显示整数否则保留
	 * @param num
	 * @return
	 */
	public static String doubleTrans(double num) {
		if (num % 1.0 == 0) {
			return String.valueOf((long) num);
		}
		return String.valueOf(num);
	}

}
