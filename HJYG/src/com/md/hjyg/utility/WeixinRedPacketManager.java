package com.md.hjyg.utility;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.entity.RedPackageObtainLogsModel;
import com.md.hjyg.entity.RedPackageObtainLogsModel.MemberShareRedEnvelopeLog;
import com.md.hjyg.entity.ShareRedPackageInfoModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: WeixinRedPacketManager date: 2015-12-19 上午10:14:32 
 * remark: 微信红包以及体验金管理类
 * 
 * @author pyc
 */
public class WeixinRedPacketManager {

	private DataFetchService dft;
	private String code;
	public static double shareAmount = -1;

	private Activity mActivity;
	private ShareRedPackageInfoModel shareRedPackageInfoModel;
	private RedPackageObtainLogsModel redPackageObtainLogsModel;
	private List<MemberShareRedEnvelopeLog> lists;


	private Handler handler;

	public WeixinRedPacketManager(Activity mActivity, Handler handler,DataFetchService dft) {
		this.mActivity = mActivity;
		this.handler = handler;
		this.dft = dft;
	}

	/** 获取用户可分享红包信息 */
	public void getShareRedPackageInfo() {
		//ShareRedPackageInfo    ShareRedPackageInfoVersionUp
		dft.getNetInfoById(Constants.ShareRedPackageInfoVersionUp_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						shareRedPackageInfoModel = (ShareRedPackageInfoModel) dft
								.GetResponseObject(jsonObject,
										ShareRedPackageInfoModel.class);
						if (shareRedPackageInfoModel == null) {
							Toast.makeText(mActivity, "数据加载异常",
									Toast.LENGTH_SHORT).show();
							return;
						}
						code = shareRedPackageInfoModel.code;
						shareAmount = shareRedPackageInfoModel.shareAmount;
						if (handler != null) {
							handler.sendEmptyMessage(0);
						}
					}
				});
	}

	/** 获取红包领取记录 */
	public void getRedPackageObtainLogs(int page) {
		if (code == null) {
			handler.sendEmptyMessage(1);
			return;
		}
		dft.postRedPackageObtainLogs(code, page,
				Constants.GetRedPackageObtainLogs_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						redPackageObtainLogsModel = (RedPackageObtainLogsModel) dft
								.GetResponseObject(jsonObject,
										RedPackageObtainLogsModel.class);
						if (redPackageObtainLogsModel != null) {
							lists = redPackageObtainLogsModel.list;
							handler.sendEmptyMessage(1);
						}
						
					}
				});
	}
	
	/** 获取用户可分享体验金信息 */
	public void getShareExpInfo() {
		dft.getNetInfoById(Constants.ShareExpInfo_URL,
				Request.Method.GET, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						shareRedPackageInfoModel = (ShareRedPackageInfoModel) dft
								.GetResponseObject(jsonObject,
										ShareRedPackageInfoModel.class);
						if (shareRedPackageInfoModel == null) {
							Toast.makeText(mActivity, "数据加载异常",
									Toast.LENGTH_SHORT).show();
							return;
						}
						
						if (handler != null) {
							handler.sendEmptyMessage(0);
						}
					}
				});
	}
	
	/** 获取分享体验金领取记录 */
	public void getShareExpLogs(int page, String yearmonth) {
		dft.postShareExpLogs(yearmonth, page,
				Constants.GetShareExpLogs_URL,
				Request.Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						redPackageObtainLogsModel = (RedPackageObtainLogsModel) dft
								.GetResponseObject(jsonObject,
										RedPackageObtainLogsModel.class);
						if (redPackageObtainLogsModel != null) {
							lists = redPackageObtainLogsModel.list;
							handler.sendEmptyMessage(1);
						}
						
					}
				});
	}

	public ShareRedPackageInfoModel getShareRedPackageInfoModel() {
		return shareRedPackageInfoModel;
	}

	public void setShareRedPackageInfoModel(
			ShareRedPackageInfoModel shareRedPackageInfoModel) {
		this.shareRedPackageInfoModel = shareRedPackageInfoModel;
	}
	
	public RedPackageObtainLogsModel getRedPackageObtainLogsModel() {
		return redPackageObtainLogsModel;
	}

	public void setRedPackageObtainLogsModel(
			RedPackageObtainLogsModel redPackageObtainLogsModel) {
		this.redPackageObtainLogsModel = redPackageObtainLogsModel;
	}

	public List<MemberShareRedEnvelopeLog> getLists() {
		return lists;
	}

	public void setLists(List<MemberShareRedEnvelopeLog> lists) {
		this.lists = lists;
	}

}
