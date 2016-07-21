package com.md.hjyg.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.BaseActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.LoginActivity;
import com.md.hjyg.entity.AccountLoginDetails;
import com.md.hjyg.entity.HomeApiModel;
import com.md.hjyg.entity.MobileArticlesModel;
import com.md.hjyg.entity.MoreInfoModel;
import com.md.hjyg.entity.NewUserSpecialistModel;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.entity.SendGiftlistModel;
import com.md.hjyg.entity.VersionDetails;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utils.AppUtil;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.NetworkManager;
import com.md.hjyg.utils.UpdateVersionManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * ClassName: HomeWebServiceManager date: 2016-3-24 下午5:07:28
 * remark:首页网络服务管理(包括5个fragment 和一个侧滑界面)
 * 
 * @author pyc
 */
public class HomeWebServiceManager implements Response.ErrorListener {

	private HomeFragmentActivity mActivity;
	// private BaseActivity context;
	private DataFetchService dft;
	private Handler mHandler;
	private DialogProgressManager mDialog;
	private MoreInfoModel moreInfoModel;
	private Activity activity;
	/**
	 * 我的资产信息
	 */
	public final static int ACCOUNT_INF = 301;
	/**
	 * 更多相关信息
	 */
	public final static int MORE_INF = 302;
	/**
	 * 礼品标信息
	 */
	public final static int ZeroActivity_INF = 303;
	/**
	 * home图片及标的信息
	 */
	public final static int HOMAPI_INF = 304;
	
	/**
	 * 新手标和会员专享标
	 */
	public final static int SendGift_INF = 305;
	/**
	 * 资讯
	 */
	public final static int ZX_INF = 306;
	/**
	 * 无版本更新
	 */
	public final static int UpdateV = 307;

	private DialogManager dialogManager;

	public HomeWebServiceManager(HomeFragmentActivity mActivity,
			Handler mHandler, Context context) {
		this.mActivity = mActivity;
		this.activity = mActivity;
		this.dft = mActivity.getDft();
		this.mHandler = mHandler;
		mDialog = new DialogProgressManager(mActivity, "努力加载中...");
		dft.setisError(true);
		dialogManager = new DialogManager(mActivity);
	}

	public HomeWebServiceManager(BaseActivity context, Handler mHandler) {
		// this.context = context;
		this.dft = context.getDft();
		this.activity = context;
		this.mHandler = mHandler;
		mDialog = new DialogProgressManager(context, "努力加载中...");
		dft.setisError(true);
	}

	/** 获取图片的数据和标的数据 */
	public void GetWebserviceHomeAPI(boolean isShowDialog) {
		if (isShowDialog) {
			mDialog.setMsg("努力加载中...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		dft.getProductold(Constants.GetMainInfo_URL, Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						mDialog.dismiss();
						HomeApiModel homeApi = (HomeApiModel) dft
								.GetResponseObject(response, HomeApiModel.class);
						Message msg = mHandler.obtainMessage(HOMAPI_INF,
								homeApi);
						mHandler.sendMessage(msg);

					}

				}, this, null);
	}

	/**
	 * 版本更新接口
	 * 
	 * @param showhit
	 *            已经是新版本是否提示信息
	 */
	public void updateVersionWebservice(final boolean showhit) {
		if (showhit) {
			mDialog.setMsg("正在检查版本信息...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		String timeStr = DateUtil.dateToStr(new Date(),
				DateUtil.YYYYMMDD_HH_MM_SS);
		String imei = AppUtil.getIMEI(mActivity);
		String appversion = AppUtil.getVerName(mActivity);
		dft.updateVersion(timeStr, imei, Constants.IDENTIFY, appversion,
				Constants.UpdateVersion_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@SuppressLint("SimpleDateFormat")
					@Override
					public void onResponse(JSONObject response) {
						mDialog.dismiss();
						VersionDetails versionDetails = (VersionDetails) dft
								.GetResponseObject(response,
										VersionDetails.class);
						String status_message = versionDetails.Notification.ProcessMessage
								.toString();
						int status = versionDetails.Notification.ProcessResult;
						if (status == 1) {
							// 计算服务器时间和当前手机时间的差距
							SimpleDateFormat phoneTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							//获取手机的时间，用来计算与服务器的时间差
							Date date = new Date();
							String phoneTime =  phoneTimeFormat.format(date);
							long timeStr = DateUtil.dateDiff(versionDetails.nowTime, phoneTime, "yyyy-MM-dd HH:mm:ss");
							// 保存差距时间
							Constants.SetServerNowTime(timeStr, mActivity);
							
							if (versionDetails.upgrade != null
									&& versionDetails.upgrade.equals("true")) {
								UpdateVersionManager updateVersionManager = new UpdateVersionManager(
										mActivity, versionDetails.downLoadUrl,
										versionDetails);
								updateVersionManager.checkUpdateInfo();
							} else {
								mHandler.sendEmptyMessage(UpdateV);
								if (showhit) {
									dialogManager.initOneBtnDialog("确定", "提示",
											"您已经是最新版本了！", null);
								}

							}
						} else if (status == 0) {
							dialogManager.initOneBtnDialog("确定", "提示",
									status_message, null);
						}
					}
				}, this);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param showDialog
	 *            是否显示加载框
	 */
	public void GetWebserviceAccountAPI(boolean showDialog) {
		if (Constants.GetResult_AuthToken(mActivity).length() == 0) {
			return;
		}
		if (showDialog) {
			mDialog.setMsg("努力加载中...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		dft.getAccounDetails(Constants.GetMember_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							mDialog.dismiss();
							AccountLoginDetails account_details = (AccountLoginDetails) dft
									.GetResponseObject(response,
											AccountLoginDetails.class);
							Message msg = mHandler.obtainMessage(ACCOUNT_INF,
									account_details);
							mHandler.sendMessage(msg);

						} catch (Exception e) {
							Toast.makeText(mActivity,
									mActivity.getString(R.string.data_error),
									Toast.LENGTH_SHORT).show();
						}
					}

				}, this);
	}

	/**
	 * 退出登录
	 */
	public void LoginOutService() {
		mDialog.setMsg("正在安全退出...");
		if (!mDialog.isShowing()) {
			mDialog.initDialog();
		}
		dft.LogoutService(Constants.LogOut_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Notification notification = (Notification) dft
								.GetResponseObject(response, Notification.class);
						mDialog.dismiss();
						if (notification.ProcessResult == 1) {
							mActivity.goneSlidingView();
							Constants.ClearSharePref(mActivity);
							Constants.Clear_Cookie(mActivity);
							mActivity.signOut();
						} else {
							Toast.makeText(mActivity, "操作失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				}, this);
	}

	/** 获取更多相关信息 */
	public void GetMoreInfoWebService(boolean isShowDialog) {
		if (isShowDialog) {
			mDialog.setMsg("努力加载中...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		dft.getNetInfoById(Constants.GetMoreInfo_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						mDialog.dismiss();
						moreInfoModel = (MoreInfoModel) dft.GetResponseObject(
								response, MoreInfoModel.class);
						Message msg = mHandler.obtainMessage(MORE_INF,
								moreInfoModel);
						mHandler.sendMessage(msg);
					}

				}, this);
	}

	/**
	 * 获取礼品标信息
	 */
	public void getZeroActivitylistinfo(boolean isShowDialog) {
		if (isShowDialog) {
			mDialog.setMsg("努力加载中...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		dft.getNetInfoById(Constants.ZeroActivitylist_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						mDialog.dismiss();
						NewUserSpecialistModel model = (NewUserSpecialistModel) dft
								.GetResponseObject(response,
										NewUserSpecialistModel.class);
						Message msg = mHandler.obtainMessage(ZeroActivity_INF,
								model);
						mHandler.sendMessage(msg);

					}
				}, this);
	}
	/**
	 * 获取新手标和礼品标信息
	 */
	public void getSendGiftlistlistinfo(boolean isShowDialog) {
		if (isShowDialog) {
			mDialog.setMsg("努力加载中...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		dft.getNetInfoById(Constants.GetSendGiftlist_URL, Request.Method.GET,
				new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				mDialog.dismiss();
				SendGiftlistModel model = (SendGiftlistModel) dft
						.GetResponseObject(response,
								SendGiftlistModel.class);
				Message msg = mHandler.obtainMessage(SendGift_INF,
						model);
				mHandler.sendMessage(msg);
				
			}
		}, this);
	}
	/**
	 * 获取资讯
	 */
	public void GetMobileArticlesListofNews(int pageIndex , int pageSize,boolean isShowDialog) {
		if (isShowDialog) {
			mDialog.setMsg("努力加载中...");
			if (!mDialog.isShowing()) {
				mDialog.initDialog();
			}
		}
		dft.postMobileArticlesListofNews(pageIndex,pageSize,1,Constants.GetMobileArticlesListofNews_URL, Request.Method.POST,
				new Listener<JSONObject>() {
			
			@Override
			public void onResponse(JSONObject response) {
				mDialog.dismiss();
				MobileArticlesModel model = (MobileArticlesModel) dft
						.GetResponseObject(response,
								MobileArticlesModel.class);
				Message msg = mHandler.obtainMessage(ZX_INF,
						model);
				mHandler.sendMessage(msg);
				
			}
		}, this);
	}

	/**
	 * 获取更多接口返回的信息
	 * 
	 * @return
	 */
	public MoreInfoModel getMoreInfoModel() {
		return moreInfoModel;
	}

	@Override
	public void onErrorResponse(VolleyError volleyError) {
		mDialog.dismiss();
		if (!NetworkManager.isNetworkAvailable(activity)) {
			dialogManager.initOneBtnDialog("确定", "提示", "网络不给力", null);
			return;
		}
		Log.e("Volly Eror in commomn.", "" + volleyError.toString());
		if (volleyError instanceof TimeoutError) {
			Toast.makeText(activity, "请求超时", Toast.LENGTH_SHORT).show();
		} else if ((volleyError instanceof NetworkError)
				|| (volleyError instanceof NoConnectionError)) {
			Toast.makeText(activity, "网络不给力", Toast.LENGTH_SHORT).show();
		}

		if (volleyError.networkResponse != null) {
			Log.e("Volly Eror in commomn.", "" + volleyError.getCause());
			Log.e("Volly Eror in commomn.", "" + volleyError.getMessage());
			Log.e("Volly networkResponse.", ""
					+ volleyError.networkResponse.statusCode);

			String response = new String(volleyError.networkResponse.data);
			Log.i("response", response);

			Constants.ClearSharePref(activity.getBaseContext());
			Constants.Clear_Cookie(activity.getBaseContext());

			if (response.equals("Invalid Username/Password")) {

				dialogManager.initOneBtnDialog("确定", "提示", "您的登录状态已失效，请重新登录！",
						new OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								Intent loginIntent = new Intent(activity,
										LoginActivity.class);
								activity.startActivity(loginIntent);

							}
						});

				// }

			} else {
				dialogManager.initOneBtnDialog("确定", "提示", "网络不给力", null);
			}
		}
	}

}
