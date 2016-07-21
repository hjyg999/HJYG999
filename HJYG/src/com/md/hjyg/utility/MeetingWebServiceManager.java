package com.md.hjyg.utility;

import org.json.JSONException;
import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ExhibitionSalelotteryListModel;
import com.md.hjyg.entity.ExhibitionSalelotteryModel;
import com.md.hjyg.entity.LotteryActionModel;
import com.md.hjyg.entity.MeetingGoodluckModel;
import com.md.hjyg.entity.MeetingMemberLotteryLogListModel;
import com.md.hjyg.entity.MeetingPinOpLogListModel;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: MeetingWebServiceManager date: 2016-3-11 下午4:18:43 remark:会销抽奖相关接口
 * 
 * @author pyc
 */
public class MeetingWebServiceManager {

	private DataFetchService dft;
	private Handler mHandler;
	private Context context;

	public MeetingWebServiceManager(Context context, DataFetchService dft,
			Handler mHandler) {
		this.context = context;
		this.dft = dft;
		this.mHandler = mHandler;
	}

	/**
	 * 或取是否是主持人
	 */
	public void getIsCompere() {
		dft.getNetInfoById(Constants.IsCompere_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							boolean IsCompere = jsonObject
									.getBoolean("isCompere");
							Message msg = mHandler.obtainMessage();
							msg.obj = IsCompere;
							msg.what = 201;
							mHandler.sendMessage(msg);
						} catch (JSONException e) {
							Toast.makeText(context,
									context.getString(R.string.data_error),
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}

					}
				});
	}

	/**
	 * 判断兑换码，正确获取会销活动列表，错误返回剩余次数
	 */
	public void getExhibitionSalelotteryList(String hxcode) {
		dft.postExhibitionSalelotteryList(hxcode,Constants.GetExhibitionSalelotteryList_URL,
				Request.Method.POST, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				ExhibitionSalelotteryListModel model = (ExhibitionSalelotteryListModel) dft
						.GetResponseObject(jsonObject,
								ExhibitionSalelotteryListModel.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 202;
				mHandler.sendMessage(msg);
				
			}
		});
	}
	/**
	 * 获取该主持人下的所有活动列表
	 */
	public void GetLotteryActivityList() {
		dft.getNetInfoById(Constants.GetLotteryActivityList_URL,
				Request.Method.GET, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				LotteryActionModel model = (LotteryActionModel) dft
						.GetResponseObject(jsonObject,
								LotteryActionModel.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 203;
				mHandler.sendMessage(msg);
				
			}
		});
	}
	/**
	 * 主持人控制会销开始结束
	 */
	public void UpdateMeetingPin(int ActivityId,boolean Start) {
		dft.postUpdateMeetingPin(ActivityId,Start,Constants.UpdateMeetingPin_URL,
				Request.Method.POST, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				Notification model = (Notification) dft
						.GetResponseObject(jsonObject,
								Notification.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 204;
				mHandler.sendMessage(msg);
				
			}
		});
	}
	
	/**
	 * 获取会销操作记录
	 */
	public void GetMeetingPinOpLogList(int ActivityId) {
		dft.postGetMemberLotteryLog(ActivityId,Constants.GetMeetingPinOpLogList_URL,
				Request.Method.POST, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				MeetingPinOpLogListModel model = (MeetingPinOpLogListModel) dft
						.GetResponseObject(jsonObject,
								MeetingPinOpLogListModel.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 205;
				mHandler.sendMessage(msg);
				
			}
		});
	}
	
	/**
	 *主持人 获取中奖名单
	 */
	public void GetMemberLotteryLog(int ActivityId) {
		dft.postGetMemberLotteryLog(ActivityId,Constants.GetMemberLotteryLog_URL,
				Request.Method.POST, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				MeetingMemberLotteryLogListModel model = (MeetingMemberLotteryLogListModel) dft
						.GetResponseObject(jsonObject,
								MeetingMemberLotteryLogListModel.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 206;
				mHandler.sendMessage(msg);
				
			}
		});
	}
	
	/**
	 * 获取抽奖页面信息
	 */
	public void ExhibitionSalelottery(int ActivityId,int HxCodeId) {
		dft.postExhibitionSalelottery(ActivityId,HxCodeId,Constants.ExhibitionSalelottery_URL,
				Request.Method.POST, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				ExhibitionSalelotteryModel model = (ExhibitionSalelotteryModel) dft
						.GetResponseObject(jsonObject,
								ExhibitionSalelotteryModel.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 207;
				mHandler.sendMessage(msg);
				
			}
		});
	}
	/**
	 * 会销抽奖接口
	 * @param ActivityId
	 * @param HxCodeId
	 */
	public void ExhibitionSalelotteryGoodLuck(int ActivityId,int HxCodeId) {
		dft.postExhibitionSalelottery(ActivityId,HxCodeId,Constants.ExhibitionSalelotteryGoodLuck_URL,
				Request.Method.POST, new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject jsonObject) {
				
				MeetingGoodluckModel model = (MeetingGoodluckModel) dft
						.GetResponseObject(jsonObject,
								MeetingGoodluckModel.class);
				
				Message msg = mHandler.obtainMessage();
				msg.obj = model;
				msg.what = 208;
				mHandler.sendMessage(msg);
				
			}
		});
	}

}
