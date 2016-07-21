package com.md.hjyg.utility;

import org.json.JSONObject;

import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.entity.LotteryActivityMessage;
import com.md.hjyg.entity.MyRewardinfoModel;
import com.md.hjyg.entity.NewLotteryInfoModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;

/** 
 * ClassName: LotteryWebServiceManager 
 * date: 2015-10-23 上午11:23:53 
 * remark:抽奖活动需要的接口
 * @author pyc
 */
public class LotteryWebServiceManager {
	
	private DataFetchService dft;
//	private Activity context;
	private Handler mHandler;
//	private String LotteryById_Method_name = "LotteryApi/GetLotteryActivityById";
//	private String LotteryPrizeByHome_Method_name = "LotteryApi/GetLotteryActivityPrizeByHome";
//	private String MemberLotteryLogByCount_Method_name = "LotteryApi/GetMemberLotteryLogByCount";
//	private String MemberLotteryLogByMember_Method_name = "LotteryApi/GetMemberLotteryLogByMember";
//	private String LotteryActivityNumber_Method_name = "LotteryApi/GetLotteryActivityNumber";
//	private String MemberLotteryActivity_Method_name = "LotteryApi/MemberLotteryActivity";
//	private String LotteryActivityHaveNumber_Method_name = "LotteryApi/GetLotteryActivityHaveNumber";
	/**用户ID*/
	private String ID;
//	private String[]  method_names = {"LotteryApi/FebruaryAction","LotteryApi/GetMyRewardinfo","LotteryApi/FebruaryGoodLuck"};
	private String[]  method_names ;
	private Message message;
	
	public LotteryWebServiceManager(Activity context,Handler mHandler,DataFetchService dft)
	{
//		this.context = context;
		this.dft = dft;
		this.mHandler = mHandler;
		method_names = new String[3];
		method_names[0] = Constants.FebruaryAction_URL;
		method_names[1] = Constants.GetMyRewardinfo_URL;
		method_names[2] = Constants.FebruaryGoodLuck_URL;
	}
	
	/**抽奖活动-根据ID获取抽奖活动信息，第一次活动，id直接传1即可*/
//	public void GetLotteryActivityById(String id) {
//		dft.GetLotteryActivityById(id, LotteryById_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//
//					@Override
//					public void onResponse(JSONObject response) {
//						LotteryActivityInfoModel lotteryActivityInfoModel = (LotteryActivityInfoModel) dft.GetResponseObject(
//								response, LotteryActivityInfoModel.class);
//						if (lotteryActivityInfoModel.notification.ProcessResult == 1) {
//							Message message = new Message();
//							message.what = 101;
//							message.obj = lotteryActivityInfoModel.Status;
//							mHandler.sendMessage(message);
//						}else {
//							Constants.showOkPopup(context, "活动还未开始", new View.OnClickListener() {
//								
//								@Override
//								public void onClick(View v) {
//									if (Constants.dialog != null && Constants.dialog.isShowing()) {
//										Constants.dialog.dismiss();
//									}
//									context.finish();
//									context.overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
//								}
//							});
//						}
//					}
//				}, null);
//	}
	
	/**抽奖活动-根据活动ID获取奖品信息，第一次活动，直接传1即可*/
//	public void GetLotteryActivityPrizeByHome(String id) {
//		dft.GetLotteryActivityById(id, LotteryPrizeByHome_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//			@Override
//			public void onResponse(JSONObject response) {
//				LotteryActivityPrizeListModel lotteryActivityPrizeListModel = (LotteryActivityPrizeListModel) dft.GetResponseObject(
//						response, LotteryActivityPrizeListModel.class);
//				if (lotteryActivityPrizeListModel.notification.ProcessResult == 1) {
//					Message message = new Message();
//					message.what = 102;
//					message.obj = lotteryActivityPrizeListModel.list;
//					mHandler.sendMessage(message);
//				}else {
//					Constants.showOkPopup(context, "活动还未开始", new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							if (Constants.dialog != null && Constants.dialog.isShowing()) {
//								Constants.dialog.dismiss();
//							}
//							context.finish();
//							context.overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
//						}
//					});
//				}
//				
//			}
//		}, null);
//	}
	
	/** 抽奖活动-按数量获取抽奖记录,查看其他用户的抽奖结果,参数Count为取值数量，
	 * LotteryActivityId为活动id，第一次活动直接传1即可 */
//	public void GetMemberLotteryLogByCount(String Count,String LotteryActivityId) {
//		dft.GetMemberLotteryLogByCount(Count,LotteryActivityId, MemberLotteryLogByCount_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				MemberLotteryLogList memberLotteryLogList = (MemberLotteryLogList) dft.GetResponseObject(
//						response, MemberLotteryLogList.class);
//				if (memberLotteryLogList != null){
//					ArrayList<MemberLotteryLog> memberLotteryLogsList = (ArrayList<MemberLotteryLog>) memberLotteryLogList.list;
//					Message message = new Message();
//					message.what = 103;
//					message.obj = memberLotteryLogsList;
//					mHandler.sendMessage(message);
//				}
//			}
//		}, null);
//	}
	
	/** 抽奖活动-获取单个用户的抽奖结果记录 */
//	public void GetMemberLotteryLogByMember(String MemberId) {
//		dft.GetMemberLotteryLogByMember(MemberId, MemberLotteryLogByMember_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				MemberLotteryLogList memberLotteryLogList = (MemberLotteryLogList) dft.GetResponseObject(
//						response, MemberLotteryLogList.class);
//				if (memberLotteryLogList != null){
//					ArrayList<MemberLotteryLog> memberLotteryLogsList = (ArrayList<MemberLotteryLog>) memberLotteryLogList.list;
//					Message message = new Message();
//					message.what = 101;
//					message.obj = memberLotteryLogsList;
//					mHandler.sendMessage(message);
//				}
//			}
//		}, null);
//	}
	/** 抽奖活动-根据活动ID和用户ID获取用户的抽奖次数，
	 * 第一次活动，LotteryActivityId直接传1即可 */
//	public void GetLotteryActivityNumber(String MemberId,String LotteryActivityId) {
//		if ((MemberId == null || MemberId.length() ==0)  && 
//				(ID == null || ID.length() ==0) ) {
//			GetWebserviceAccountInformationAPI();
//			return;
//		}
//		dft.MemberLotteryActivity(MemberId,LotteryActivityId, LotteryActivityNumber_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				LotteryActivityNumber lotteryActivityNumber = (LotteryActivityNumber) dft.GetResponseObject(
//						response, LotteryActivityNumber.class);
//				if (lotteryActivityNumber.notification.ProcessResult == 1) {
//					Message message = new Message();
//					message.what = 104;
//					message.obj = lotteryActivityNumber;
//					mHandler.sendMessage(message);
//				}else {
//					Constants.showOkPopup(context,  "网络异常，请重试", new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							if (Constants.dialog != null && Constants.dialog.isShowing()) {
//								Constants.dialog.dismiss();
//							}
//							context.finish();
//							context.overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
//						}
//					});
//				}
//			}
//		}, null);
//	}
	
	/** 抽奖活动-用户抽奖接口 */
//	public void MemberLotteryActivity(String MemberId,String LotteryActivityId) {
//		dft.MemberLotteryActivity(ID,LotteryActivityId, MemberLotteryActivity_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				LotteryActivityMessage lotteryActivityMessage = (LotteryActivityMessage) dft.GetResponseObject(
//						response, LotteryActivityMessage.class);
//				if (lotteryActivityMessage.notification.ProcessResult == 1) {
//					Message message = new Message();
//					message.what = 105;
//					message.obj = lotteryActivityMessage;
//					mHandler.sendMessage(message);
//				}else {
//					Constants.showOkPopup(context, "网络异常，请重试", new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							if (Constants.dialog != null && Constants.dialog.isShowing()) {
//								Constants.dialog.dismiss();
//							}
//							context.finish();
//							context.overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
//						}
//					});
//				}
//			}
//		}, null);
//	}
	
	/** 抽奖活动-获取用户剩余抽奖次数(能抽的次数) */
//	public void GetLotteryActivityHaveNumber(String MemberId,String LotteryActivityId) {
//		dft.MemberLotteryActivity(MemberId,LotteryActivityId, LotteryActivityHaveNumber_Method_name, Request.Method.POST, 
//				new Response.Listener<JSONObject>() {
//			
//			@Override
//			public void onResponse(JSONObject response) {
//				LotteryActivityHaveNumber lotteryActivityHaveNumber = (LotteryActivityHaveNumber) dft.GetResponseObject(
//						response, LotteryActivityHaveNumber.class);
//				if (lotteryActivityHaveNumber != null){
//					int haveNumber = lotteryActivityHaveNumber.haveNumer;
//					Message message = new Message();
//					message.what = 102;
//					message.obj = haveNumber;
//					mHandler.sendMessage(message);
//				}
//			}
//		}, null);
//	}
	
	/**获取用户信息*/
	public void GetWebserviceAccountInformationAPI() {

		dft.getAccounInfoDetails(Constants.GetAccounInfo_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						LoanModel loanModel = (LoanModel) dft
								.GetResponseObject(response, LoanModel.class);
						ID = loanModel.Member.Id + "";
						mHandler.sendEmptyMessage(106);
						//GetLotteryActivityNumber(ID,"1");
					}
				}, null);
	}
	
	public String getMemberId()
	{
		return ID;
	}
	
	/**
	 * @param id
	 *            : 0:获取抽奖活动相关信息（包括奖品信息，中间记录等） 1:我的抽奖记录信息 2:抽奖
	 */
	public void GetNetInfo(final int id) {
		dft.getNetInfoById(method_names[id], Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						switch (id) {
						case 0:// 获取年会抽奖活动相关信息（包括奖品信息，中间记录等）
							NewLotteryInfoModel Model = (NewLotteryInfoModel) dft
									.GetResponseObject(josnbject,
											NewLotteryInfoModel.class);
							message = new Message();
							message.what = 102;
							message.obj = Model;
							mHandler.sendMessage(message);

							break;
						case 1:
							MyRewardinfoModel myRewardinfoModel = (MyRewardinfoModel) dft
									.GetResponseObject(josnbject,
											MyRewardinfoModel.class);
							message = new Message();
							message.what = 101;
							message.obj = myRewardinfoModel;
							mHandler.sendMessage(message);

							break;
						case 2:
							LotteryActivityMessage lotteryActivityMessage = (LotteryActivityMessage) dft
									.GetResponseObject(josnbject,
											LotteryActivityMessage.class);
							message = new Message();
							message.what = 105;
							message.obj = lotteryActivityMessage;
							mHandler.sendMessage(message);

							break;

						default:
							break;
						}

					}
				});
	}
	
	/**
	 * @param id
	 *            : 0:获取抽奖活动相关信息（包括奖品信息，中间记录等） 1:我的抽奖记录信息 2:抽奖
	 */
	public void GetMyRewardInfo(int page, final int id) {
		dft.postMyRewardInfo(page, method_names[id], Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						switch (id) {
						case 0:// 获取年会抽奖活动相关信息（包括奖品信息，中间记录等）
							NewLotteryInfoModel Model = (NewLotteryInfoModel) dft
									.GetResponseObject(josnbject,
											NewLotteryInfoModel.class);
							message = new Message();
							message.what = 102;
							message.obj = Model;
							mHandler.sendMessage(message);

							break;
						case 1:
							MyRewardinfoModel myRewardinfoModel = (MyRewardinfoModel) dft
									.GetResponseObject(josnbject,
											MyRewardinfoModel.class);
							message = new Message();
							message.what = 101;
							message.obj = myRewardinfoModel;
							mHandler.sendMessage(message);

							break;
						case 2:
							LotteryActivityMessage lotteryActivityMessage = (LotteryActivityMessage) dft
									.GetResponseObject(josnbject,
											LotteryActivityMessage.class);
							message = new Message();
							message.what = 105;
							message.obj = lotteryActivityMessage;
							mHandler.sendMessage(message);

							break;

						default:
							break;
						}

					}
				});
	}

}
