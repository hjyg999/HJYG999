package com.md.hjyg.services;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.md.hjyg.activities.LoginActivity;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utils.AppUtil;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.MD5;
import com.md.hjyg.utils.NetworkManager;
import com.md.hjyg.utils.SsX509TrustManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

/**
 * 网络数据处理
 */
@SuppressLint("DefaultLocale")
public class DataFetchService implements Response.ErrorListener {

	protected static final String TAG = "DataFetchService";
	public Context mcontext;
	public Activity activity;
	private static final int MY_SOCKET_TIMEOUT_MS = 5000;
	private final String DeviceType = "android";

	private String TimeStamp = "";
	private String Imei = "";
	/**
	 * 是否自定义了错误信息
	 */
	private boolean isError;
	private DialogManager dialogManager;

	private DftErrorListener dftErrorListener;

	public DataFetchService(Context mcontext) {
		this.mcontext = mcontext;
	}

	public DataFetchService(Activity activity) {
		this.activity = activity;

		dialogManager = new DialogManager(activity);
	}

	public void setOnDftErrorListener(DftErrorListener dftErrorListener) {
		this.dftErrorListener = dftErrorListener;
	}

	/**
	 * 
	 * @param isError
	 *            true 采用自定义的错误信息
	 */
	public void setisError(boolean isError) {
		this.isError = isError;
	}

	public void getProductold(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn, Map<String, String> requestMap) {
		makeJsonObjReq(methodname, "", callType, listener, errlsn, null);

	}

	/** 获取通讯协议类型接口 */
	public void getProtocalType(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/** 获取热门消息条数 */
	public void GetIsHotCount(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);

	}

	/** 获取热门消息条数 */
	public void LogoutService(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);

	}

	/** 在登录获取图像验证码之前下发Cookie */
	public void postCookie(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * Investment Details
	 * */
	public void getInvestmentDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn, Map<String, String> requestMap) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * Transaction Details
	 * */
	public void getTransactionDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn, Map<String, String> requestMap) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * Project Details
	 * */
	public void getProjectDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn, Map<String, String> requestMap) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * Captcha
	 * */
	public void getCaptcha(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeCookieJsonObjReq(methodname, sign, callType, listener, errlsn, null);

	}

	/**
	 * 获取数字验证码
	 * */
	public void getNumCaptcha(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);

	}

	/** 获取微信红包推荐人信息 */
	public void getRecommendName(String mobile, String methodname,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("mobile", mobile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * QR Code
	 * */
	public void getQRCode(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * BuyNow
	 * */
	public void getBuyNowInfo(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * AccountDetails
	 * */
	public void getAccounDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * AccountInfoDetails
	 * */
	public void getAccounInfoDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * Verify
	 * */
	public void getVerifiyDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, null);
	}

	/**
	 * WithdrawApplication
	 * */
	public void getWithdrawApplication(String Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_Name, sign, callType, listener, errlsn, null);
	}

	/**
	 * AccountDetails
	 * */
	public void getBankAccountDetails(String Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_Name, sign, callType, listener, errlsn, null);
	}

	/**
	 * BankCardValid
	 * */
	public void getBankCardValidDetails(String Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_Name, sign, callType, listener, errlsn, null);
	}

	/**
	 * 充值支付前，获取银行卡等信息,以此来判断下一步的跳转，是跳转到添加银行卡还是直接显示银行卡信息界面
	 * */
	public void getRecharge(String Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_Name, sign, callType, listener, errlsn, null);
	}

	/**
	 * 获取连连支付银行限额列表
	 * */
	public void getBankRestraint(String Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_Name, sign, callType, listener, errlsn, null);
	}

	/**
	 * Total Assets
	 * */
	public void getTotalAssets(String Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_Name, sign, callType, listener, errlsn, null);
	}

	/**
	 * 获取更多里面的相关信息
	 * 
	 * @param page
	 *            分页
	 * @param MethodName
	 * @param callType
	 * @param listener
	 */
	public void getMoreInfoWebService(String MethodName, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(MethodName, sign, callType, listener, errlsn, null);
	}

	/**
	 * Join Now
	 * */
	public void doLogin(String userName, String encryptedpwd, String code,
			String returl, String iv, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("userName", userName);
			jsonParams.put("encryptedpwd", encryptedpwd);
			jsonParams.put("code", code);
			jsonParams.put("returl", returl);
			jsonParams.put("iv", iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);

	}

	/**
	 * Loan List
	 * */
	public void getLoanList(String loantitle, String RepaymentWay,
			String LoanPurpose, String AmountStart, String AmountEnd,
			String sort, String status, String BiddingStratTime,
			String pageIndex, String pageSize, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("loantitle", "");
			jsonParams.put("RepaymentWay", null);
			jsonParams.put("LoanPurpose", "");
			jsonParams.put("AmountStart", null);
			jsonParams.put("AmountEnd", null);
			jsonParams.put("sort", "");
			jsonParams.put("status", "");
			jsonParams.put("BiddingStratTime", "");
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);

		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * WholeList Details
	 * */

	public void getWholeListDetails(String ftype, String start, String end,
			String page, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ftype", ftype);
			jsonParams.put("start", "");
			jsonParams.put("end", "");
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);

		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * WithdrawalRecord Details
	 * */
	public void getWithdrawalRecordDetails(String ftype, String start,
			String end, String page, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ftype", ftype);
			jsonParams.put("start", "");
			jsonParams.put("end", "");
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);

		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * Recharge Record Details
	 * */
	public void getRechargeRecordDetails(String ftype, String start,
			String end, String page, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("ftype", "0");
			jsonParams.put("start", "");
			jsonParams.put("end", "");
			jsonParams.put("page", page);

		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 
	 * @param ftype
	 *            0竞标中 1持有中 2 还款完成
	 * @param page
	 * @param Method_name
	 * @param callType
	 * @param listener
	 * @param errlsn
	 */
	public void getHoldInDetails(String ftype, String page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("ftype", 1);
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 根据行业类型获取会员专享列表
	 * 
	 * @param GroupType
	 * @param Method_name
	 * @param callType
	 * @param listener
	 * @param errlsn
	 */
	public void postSendGroupGift(int GroupType, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("GroupType", GroupType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 
	 * @param ftype
	 *            0竞标中 1持有中 2 还款完成
	 * @param page
	 * @param Method_name
	 * @param callType
	 * @param listener
	 * @param errlsn
	 */
	public void getMyInvestmentInfo(int ftype, int page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("ftype", ftype);
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	public void GoldBeanFinanceLogs(String ftype, int page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("ftype", ftype);
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 换取我投资的项目的还款信息
	 * 
	 * @param InvestID
	 *            项目id
	 * @param Method_name
	 * @param callType
	 * @param listener
	 * @param errlsn
	 */
	public void GetMemberInvestDetail(int InvestID, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("InvestID", InvestID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 获取投资列表详细信息按照年月（日历页面）
	 * 
	 * @param Year
	 *            年
	 * @param Month
	 *            月
	 * @param Method_name
	 * @param callType
	 * @param listener
	 * @param errlsn
	 */
	public void GetRepaymentCalendarDetail(String Year, String Month,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("Year", Year);
			jsonParams.put("Month", Month);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * Repaid Details
	 * */
	public void getRepaidDetails(String ftype, String page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("ftype", "0");
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);

		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 获取红包详情
	 * */
	public void getRedEnvelope(String ftype, String page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("ftype", ftype);
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);

		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 激活红包
	 * */
	public void postUseRedEnvelope(String id, String pid, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("id", id);
			jsonParams.put("pid", pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);

		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 修改交易密码
	 * */
	public void getUpdatePayPassword(String pwd, String enPwd,
			String enConfirmPwd, String iv, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("pwd", pwd);
			jsonParams.put("enPwd", enPwd);
			jsonParams.put("enConfirmPwd", enConfirmPwd);
			jsonParams.put("iv", iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 修改交易密码
	 * */
	public void getChangePayPwdInfo(String code, String enPwd,
			String enConfirmPwd, String iv, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("enPwd", enPwd);
			jsonParams.put("enConfirmPwd", enConfirmPwd);
			jsonParams.put("code", code);
			jsonParams.put("iv", iv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * Bidding Details
	 * */
	public void getBiddingDetails(String page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * Recommended Details
	 * */
	public void getRecommendedDetails(String year, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("year", "-1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * BuyNow Details
	 * */
	public void getBuyNowDetails(String loanId, String purchase_amount,
			String jsoncallback, String remark, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("loanId", loanId);
			jsonParams.put("amount", purchase_amount);
			jsonParams.put("jsoncallback", "");
			jsonParams.put("remark", "APPA");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * News List
	 * */
	public void getNewsList(String pageIndex, String pageSize,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/** 官方公告 */
	public void getNewsNoticeList(String page, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * News List
	 * */
	public void getNewsDetails(String newsId, String pageIndex,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("id", newsId);
			jsonParams.put("pageIndex", pageIndex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * News Read
	 * */
	public void getNewsRead(String NewsId, String pageIndex, String pageSize,
			String ReadMethod_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("MsgDataID", NewsId);
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(ReadMethod_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * News Read
	 * */
	public void getNewsDelete(String NewsId, String pageIndex, String pageSize,
			String DeleteMethod_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("MsgDataID", NewsId);
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(DeleteMethod_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * News UnRead
	 * */
	public void getNewsUnRead(String NewsId, String pageIndex, String pageSize,
			String UnreadMethod_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("MsgDataID", NewsId);
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(UnreadMethod_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * News UnRead
	 * */
	public void getAddBankCardWebservice(String bankName, String bankNumber,
			String enConfirmBankNumber, String selectedProvince,
			String selectedCity, String subbranch, String mobileCode,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("BankName", bankName);
			jsonParams.put("BankNumber", bankNumber);
			jsonParams.put("EnConfirmBankNumber", enConfirmBankNumber);
			jsonParams.put("Province", selectedProvince);
			jsonParams.put("City", selectedCity);
			jsonParams.put("Subbranch", subbranch);
			jsonParams.put("MobileCode", mobileCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 获取银行卡是哪个银行
	 * 
	 * @param BankNumber
	 * @param Method_name
	 * @param callType
	 * @param listener
	 * @param errlsn
	 */
	public void GetCardBinInfo(String BankNumber, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("BankNumber", BankNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * Verify
	 * */
	public void getVerifyWebservice(String realName, String id_number,
			String selected_card_type, String recommendName,
			String recommended_type, String hdFileFacade, String hdFileBack,
			String hdFileInside, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("realName", "真实姓名");
			// jsonParams.put("realName",realName);
			jsonParams.put("IDNumber", id_number);
			jsonParams.put("CardType", "1");
			jsonParams.put("recommendName", recommendName);
			jsonParams.put("recommendType", recommended_type);
			jsonParams.put("hdFileFacade", "");
			jsonParams.put("hdFileBack", "");
			jsonParams.put("hdFileInside", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * WithdrawApplication
	 * */
	public void getWithdrawPost(String withdrawPassword, String withdrawAmt,
			String bankCardId, String WithdrawMethod_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		// String withdrawPassword, String withdrawAmt,
		// String bankCardId, String withdrawType, String WithdrawMethod_Name,
		// int callType, Response.Listener<JSONObject> listener,
		// Response.ErrorListener errlsn

		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("PayPwd", withdrawPassword);
			jsonParams.put("withdrawMoeny", withdrawAmt);
			jsonParams.put("bankCardId", bankCardId);
			// jsonParams.put("WithdrawType", withdrawType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(WithdrawMethod_Name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * VerifyNameByMobileNo
	 * */
	public void getVerifyNameByMobileNo(String mobileNo, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("EmailMobile", mobileNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 没有登录找回密码 -必须传手机号
	 * */
	public void getCallPhoneCode(String sendPhoneCodeType, String code,
			String mobileNo, String SendSmsType, String PhoneCallMethod_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("sendPhoneCodeType", sendPhoneCodeType);
			jsonParams.put("code", code);
			jsonParams.put("Phone", mobileNo);
			jsonParams.put("SendSmsType", SendSmsType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(PhoneCallMethod_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 已经登录 -修改和设置密码的验证码
	 * */
	public void getNewCallPhoneCode(String sendPhoneCodeType, String code,
			String mobileNo, String SendSmsType, String PhoneCallMethod_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("sendPhoneCodeType", sendPhoneCodeType);
			// jsonParams.put("code", code);
			// jsonParams.put("Phone", mobileNo);
			jsonParams.put("SendSmsType", SendSmsType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(PhoneCallMethod_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * ValidateNameByMobileNo
	 * */
	public void getValidatePhoneCode(String code, String verificationCode,
			String ValidatePhoneNoMethod_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("code", code);
			jsonParams.put("MobileCode", verificationCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(ValidatePhoneNoMethod_name, sign, callType, listener,
				errlsn, jsonParams);
	}

	/**
	 * ForgotPassword
	 * */
	public void getForgotPass(String new_pass, String confirm_pass,
			String verificationCode, String code, String ForgotPassMethod_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("enPwd", new_pass);
			jsonParams.put("enConfirmPwd", confirm_pass);
			jsonParams.put("MobileCode", verificationCode);
			jsonParams.put("code", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(ForgotPassMethod_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * Validate by EmailId
	 * */
	public void getVerifyNameByEmail(String emailId, String EmailMethod_Name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("EmailMobile", emailId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(EmailMethod_Name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * New call by EmailId
	 * */
	public void getNewCallEmailCode(String sendPhoneCodeType, String code,
			String NewCallEmailCode_Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("SendEmailCodeType", sendPhoneCodeType);
			jsonParams.put("code", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(NewCallEmailCode_Method_Name, sign, callType, listener,
				errlsn, jsonParams);
	}

	/**
	 * verify EmailId
	 * */
	public void getVerifyEmailCode(String code, String verificationCode,
			String ValidateEmailCode_Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("code", code);
			jsonParams.put("EmailCode", verificationCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(ValidateEmailCode_Method_Name, sign, callType, listener,
				errlsn, jsonParams);
	}

	/**
	 * ForgotPasswordEmailId
	 * */
	public void getForgotPassEmailId(String new_pass, String confirm_pass,
			String emailCode, String code,
			String Forgotpass_EmailCode_Method_Name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("enPwd", new_pass);
			jsonParams.put("enConfirmPwd", confirm_pass);
			jsonParams.put("EmailCode", emailCode);
			jsonParams.put("code", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Forgotpass_EmailCode_Method_Name, sign, callType,
				listener, errlsn, jsonParams);
	}

	/**
	 * 向后台发送消息，请求数据， 通过重写的onResponse（）方法，对返回的JSONObject进行操作
	 * 
	 * @param idNumber
	 *            创建JSONObject对象的一个键值对
	 * @param rechargeMoney
	 *            创建JSONObject对象的一个键值对
	 * @param BankName
	 *            创建JSONObject对象的一个键值对
	 * @param Method_name
	 *            网站的方法名 和Constants.Common_URL组成完总的网址
	 * @param callType
	 *            访问方式：如 Request.Method.POST
	 * @param listener
	 *            返回消息监听 ，通过重写的onResponse（）方法，对返回的JSONObject进行操作
	 * @param errlsn
	 *            错误消息监听
	 */
	public void getRechargeDetailsDetails(String idNumber,
			String rechargeMoney, String BankName, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("RechargeMoney", rechargeMoney);
			jsonParams.put("BankCode", idNumber);
			jsonParams.put("BankName", "");
			jsonParams.put("DeviceType", DeviceType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * UpdateBank Card
	 * */
	public void UpdateBankCardInfo(String bankCardId, String Province,
			String City, String Subbranch, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("bankCardId", bankCardId);
			jsonParams.put("Province", Province);
			jsonParams.put("City", City);
			jsonParams.put("Subbranch", Subbranch);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * Feedback
	 * */
	public void getFeedbackDetails(String emailid, String username,
			String feedback, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("Email", emailid);
			jsonParams.put("UserName", username);
			jsonParams.put("Content", feedback);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * IsMobileExist
	 * */
	public void getisMobileNoExist(String mobile_no, String methodname,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("mobile", mobile_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * VerificationCode
	 * */
	public void getVerificactionCode(String sendPhoneCodeType,
			String sendSmsType, String mobile_no, String captchaCode,
			String username, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("SendPhoneCodeType", sendPhoneCodeType);
			jsonParams.put("SendSmsType", sendSmsType);
			jsonParams.put("mobileNumber", mobile_no);
			jsonParams.put("YZM", captchaCode);
			jsonParams.put("userName", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	public void updateImeiCode(String code, String mobileNo, String methodname,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("code", code);
			jsonParams.put("mobileNo", mobileNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * IsExistVerificationCode
	 * */
	public void getIsExistVerificactionCode(String mobile_verificationCode,
			String mobile_no, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("vcode", mobile_verificationCode);
			jsonParams.put("mobile", mobile_no);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * 注册最后一步的接口接口
	 * */
	public void getVerifyRegisterWebservice(String password, String mobile_no,
			String verification_code, String recommended_type,
			String recommendName, String realName, String id_number,
			String selected_card_type, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("enPwd", password);
			jsonParams.put("mobile", mobile_no);
			jsonParams.put("vcode", verification_code);
			jsonParams.put("recommendType", recommended_type);
			jsonParams.put("Referrer", recommendName);
			jsonParams.put("realName", realName);
			jsonParams.put("IDNumber", id_number);
			jsonParams.put("CardType", selected_card_type);
			jsonParams.put("DeviceType", DeviceType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * Refeerer
	 * */
	public void getGetReferrerRegisterWebservice(String recommendName,
			String recommended_type, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {

		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("Referrer", recommendName);
			jsonParams.put("recommendType", recommended_type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * ChangePwd
	 * */
	public void ChangePwd(String enPwd, String enConfirmPwd, String code,
			String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("enPwd", enPwd);
			jsonParams.put("enConfirmPwd", enConfirmPwd);
			jsonParams.put("code", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * VerificationCodeForPAssword
	 * */
	public void getVerificationCodeForPAssword(String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("sendPhoneCodeType", "修改密码");
			jsonParams.put("sendSmsType", "普通短信");
			jsonParams.put("mobileNumber", "");
			jsonParams.put("YZM", "");
			jsonParams.put("userName", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	/**
	 * SetPAssword
	 * */
	public void SetPwdToChange(String enPwd, String enConfirmPwd, String code,
			String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("enPwd", enPwd);
			jsonParams.put("enConfirmPwd", enConfirmPwd);
			jsonParams.put("code", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	public void SetPwd(String code, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("code", code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	public void updateVersion(String time, String imei, String identify,
			String appversion, String methodname, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			// jsonParams.put("time", time);
			// jsonParams.put("imei", imei);
			jsonParams.put("identify", identify);
			jsonParams.put("appversion", appversion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(methodname, sign, callType, listener, errlsn, jsonParams);
	}

	// ———————————————————————————————————————抽奖的接口———————————————————————————————
	/**
	 * 抽奖活动-根据ID获取抽奖活动信息，第一次活动，id直接传1即可 抽奖活动-根据活动ID获取奖品信息，第一次活动，直接传1即可
	 * GetLotteryActivityPrizeByHome也用此方法
	 * */
	public void GetLotteryActivityById(String id, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();

		try {
			jsonParams.put("id", id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 抽奖活动-按数量获取抽奖记录,查看其他用户的抽奖结果,参数Count为取值数量，
	 * LotteryActivityId为活动id，第一次活动直接传1即可
	 */
	public void GetMemberLotteryLogByCount(String Count,
			String LotteryActivityId, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("Count", Count);
			jsonParams.put("LotteryActivityId", LotteryActivityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/** 抽奖活动-获取单个用户的抽奖结果记录 */
	public void GetMemberLotteryLogByMember(String MemberId,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("MemberId", MemberId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	/**
	 * 抽奖活动-根据活动ID和用户ID获取用户的抽奖次数，第一次活动，LotteryActivityId直接传1即可 抽奖活动-用户抽奖接口
	 * 抽奖活动-获取用户剩余抽奖次数(能抽的次数)
	 */
	public void MemberLotteryActivity(String MemberId,
			String LotteryActivityId, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("MemberId", MemberId);
			jsonParams.put("LotteryActivityId", LotteryActivityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn,
				jsonParams);
	}

	// /----------------------------活期宝网络请求-----------------------------------------

	/***
	 * 从后台获取活期宝详情
	 * 
	 * @param methodname
	 * @param callType
	 * @param listener
	 */
	public void getHuoQibaoDetails(String methodname, int callType,
			Response.Listener<JSONObject> listener) {
		String sign = "";
		makeJsonObjReq(methodname, sign, callType, listener, null, null);
	}

	// /**
	// * 购买活期宝的接口
	// */
	// public void postBuyHuoQibao(String SingleloanId,String amount, String
	// memberId, String rate,String MethodName,
	// int callType, Response.Listener<JSONObject> listener) {
	//
	// JSONObject jsonParams = new JSONObject();
	//
	// try {
	// jsonParams.put("SingleloanId",SingleloanId);
	// jsonParams.put("amount",amount);
	// jsonParams.put("memberId",memberId);
	// jsonParams.put("rate",rate);
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// makeJsonObjReq( MethodName, callType, listener,
	// null, jsonParams);
	//
	// }

	/***
	 * 
	 * @param enpwd
	 *            交易密码
	 * @param withdrawMoeny
	 *            提取金额
	 * @param huoqibaoID
	 *            活期宝项目ID
	 * @param MethodName
	 *            接口名
	 * @param callType
	 *            post方法
	 * @param listener
	 *            反会数据监听接口
	 */
	public void postWithdrawHuoQibao(String enpwd, double WithdrawMoney,
			int huoqibaoID, String MethodName, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("enpwd", enpwd);
			jsonParams.put("WithdrawMoney", WithdrawMoney + "");
			jsonParams.put("SingleLoanId", huoqibaoID + "");
			jsonParams.put("deviceType", DeviceType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(MethodName, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 根据项目ID获取数据的一些接口 如：根据项目ID获取活期宝交易记录列表
	 * 
	 * @param huoqibaoID
	 *            项目ID
	 * @param MethodName
	 * @param callType
	 * @param listener
	 */
	public void postSingleLoanInfoList(int huoqibaoID, String MethodName,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("id", huoqibaoID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(MethodName, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 获取用户本身的交易信息
	 * 
	 * @param page
	 *            分页
	 * @param MethodName
	 * @param callType
	 * @param listener
	 */
	public void postSingleLoanSelfInfoList(int page, String MethodName,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(MethodName, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 购买活期宝接口
	 * 
	 * @param huoqibaoID
	 *            活期宝项目ID
	 * @param amount
	 *            购买金额
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postBuyHuoQibao(int huoqibaoID, String amount,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("id", huoqibaoID);
			jsonParams.put("amount", amount);
			jsonParams.put("deviceType", DeviceType);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/***
	 * 上传头像图片
	 * 
	 * @param ImageBinary
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postHeadimg(String ImageBinary, String Method_name,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ImageBinary", ImageBinary);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/** 我的奖品信息 */
	public void postMyRewardInfo(int page, String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/** 登录后获取网络信息 */
	public void getNetInfoById(String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		String sign = "";
		makeJsonObjReq(Method_name, sign, callType, listener, null, null);
	}

	/** 登录后获取网络信息 */
	public void getNetInfoById(String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		String sign = "";
		makeJsonObjReq(Method_name, sign, callType, listener, errlsn, null);
	}

	/***
	 * 已收奖励-待收奖励
	 * 
	 * @param page
	 *            页码
	 * @param type
	 *            类型 (1=已收，2=未收)
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postRewardByMonth(int page, int type, String Method_name,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("page", page);
			jsonParams.put("type", type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 欧洲杯竞猜
	 * 
	 * @param TeamName
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postEuroCupGuessing(String TeamName, String Method_name,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("TeamName", TeamName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/***
	 * 一级奖励信息
	 * 
	 * @param page
	 *            页码
	 * @param type
	 *            类型 (1=已收，2=未收)
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postRewardOnelevel(int page, String searchCondition,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("page", page);
			jsonParams.put("searchCondition", searchCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/***
	 * 二级奖励信息
	 * 
	 * @param page
	 *            页码
	 * @param type
	 *            类型 (1=已收，2=未收)
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postRewardTwolevel(int page, String searchCondition,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("page", page);
			jsonParams.put("searchCondition", searchCondition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 申请理财师接口
	 * 
	 * @param Mobile
	 *            手机号
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postApplyFinancialPlanner(String Mobile, String Method_name,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("Mobile", Mobile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/** 获取红包领取记录 */
	public void postRedPackageObtainLogs(String code, int page,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("code", code);
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/** 获取分享体验金领取记录 */
	public void postShareExpLogs(String yearmonth, int page,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("yearmonth", yearmonth);
			jsonParams.put("page", page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	// --------------------------------下载图片-------------------------------
	public void loadImg(String url, Response.Listener<Bitmap> listener) {
		ImageRequest imageRequest = new ImageRequest(Constants.IMAGE_URL + url,
				listener, 0, 0, Config.RGB_565, this);
		AppController.getInstance().addToRequestQueue(imageRequest);

	}

	public void loadPhoto(String url, Response.Listener<Bitmap> listener,
			ErrorListener errorListener) {
		ImageRequest imageRequest = new ImageRequest(url, listener, 0, 0,
				Config.RGB_565, errorListener);
		AppController.getInstance().addToRequestQueue(imageRequest);

	}

	// --------------------------------新手专享接口-------------------------------
	/** 新手专享获取购买项目的详细信息 */
	public void postNewUserSpecialist(String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		String sign = "";
		makeJsonObjReq(Method_name, sign, callType, listener, null, null);

	}

	/** 更新地址信息 */
	public void postUpdateAddressInfo(String AddressInfo, String Method_name,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("AddressInfo", AddressInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 新手专享-购买接口
	 * 
	 * @param Id
	 *            新手标的ID
	 * @param ExtractionMethod
	 *            提取方式：(邮寄 = 0,自提 = 1)
	 * @param Code
	 *            验证码
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postNewUserBid(int Id, int ExtractionMethod, String Code,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("Id", Id);
			jsonParams.put("ExtractionMethod", ExtractionMethod);
			jsonParams.put("Code", Code);
			jsonParams.put("ClientType", DeviceType);// 客户端类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 活动标购买接口
	 * 
	 * @param Id
	 * @param ExtractionMethod
	 * @param Code
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postActivityBid_URL(int Id, int ExtractionMethod, String Code,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("Id", Id);
			jsonParams.put("ExtractionMethod", ExtractionMethod);
			jsonParams.put("Code", Code);
			jsonParams.put("GiftId", 0);
			jsonParams.put("ClientType", DeviceType);// 客户端类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 0元消费节活动投标接口
	 * 
	 * @param Id
	 *            标的ID
	 * @param ExtractionMethod
	 *            提取方式
	 * @param Code
	 *            验证码
	 * @param GiftId
	 *            礼品ID
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postZeroActivityBid(int Id, int ExtractionMethod, String Code,
			int GiftId, String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("Id", Id);
			jsonParams.put("GiftId", GiftId);
			jsonParams.put("ExtractionMethod", ExtractionMethod);
			jsonParams.put("Code", Code);
			jsonParams.put("ClientType", DeviceType);// 客户端类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	// --------------------------------会销相关接口-------------------------------
	public void postExhibitionSalelotteryList(String hxcode,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("hxcode", hxcode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 主持人控制会销开始结束
	 * 
	 * @param ActivityId
	 *            活动ID
	 * @param Start
	 *            开始：ture,结束：false
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postUpdateMeetingPin(int ActivityId, boolean Start,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ActivityId", ActivityId);
			jsonParams.put("Start", Start);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 获取定投宝项目列表信息
	 * 
	 * @param type
	 * @param pageIndex
	 * @param pageSize
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void GetIndexOfPage(int type, int pageIndex, int pageSize,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("type", type);
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 获取中奖记录集合
	 * 
	 * @param ActivityId
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postGetMemberLotteryLog(int ActivityId, String Method_name,
			int callType, Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ActivityId", ActivityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 获取抽奖页面信息
	 * 
	 * @param ActivityId
	 * @param HxCodeId
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postExhibitionSalelottery(int ActivityId, int HxCodeId,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("ActivityId", ActivityId);
			jsonParams.put("HxCodeId", HxCodeId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	public void postMobileArticlesListofNews(int pageIndex, int pageSize,
			int hasHot, String Method_name, int callType,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("pageIndex", pageIndex);
			jsonParams.put("pageSize", pageSize);
			jsonParams.put("hasHot", hasHot);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/** 快乐微文-上提交微文接口 */
	public void postAddMicroTextCompetition(int MemberId, String Photo,
			String Name, int Age, String School, String Title,
			String Introduction, String Text, int Status, int Score,
			int PraiseNumber, String CreateTime, String Method_name,
			int callType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("MemberId", MemberId);
			jsonParams.put("Photo", Photo);
			jsonParams.put("Name", Name);
			jsonParams.put("Age", Age);
			jsonParams.put("School", School);
			jsonParams.put("Introduction", Introduction);
			jsonParams.put("Title", Title);
			jsonParams.put("Text", Text);
			jsonParams.put("Status", Status);
			jsonParams.put("Score", Score);
			jsonParams.put("PraiseNumber", PraiseNumber);
			jsonParams.put("CreateTime", CreateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);
	}

	/**
	 * 投标结束后，选择相应红包增加红包使用记录接口
	 * 
	 * @param InvestId
	 *            标的ID
	 * @param RedEnvelopeId
	 *            红包ID
	 * @param RelationId
	 *            红包gid
	 * @param MemberInvestRedEnvelopeLogType
	 *            标的类型
	 * @param Method_name
	 * @param callType
	 * @param listener
	 */
	public void postAddRedEnvelopeLog(int InvestId, int RedEnvelopeId,
			int RelationId, int MemberInvestRedEnvelopeLogType,
			String Method_name, int callType,
			Response.Listener<JSONObject> listener) {
		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("InvestId", InvestId);
			jsonParams.put("RedEnvelopeId", RedEnvelopeId);
			jsonParams.put("RelationId", RelationId);
			jsonParams.put("MemberInvestRedEnvelopeLogType",
					MemberInvestRedEnvelopeLogType);// 客户端类型
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sign = getSignbyJSONObject(jsonParams);
		makeJsonObjReq(Method_name, sign, callType, listener, null, jsonParams);

	}

	/**
	 * 向后台发送消息
	 * 
	 * @param url
	 *            网址
	 * @param callMethodeType
	 *            访问方式：如 Request.Method.POST
	 * @param listener
	 *            返回消息监听 ，通过重写的onResponse（）方法，对返回的JSONObject进行操作
	 * @param errlsn
	 *            错误消息监听
	 * @param requestMap
	 *            发送的JSONObject类型参数
	 */
	public void makeJsonObjReq(String url, final String sign,
			final int callMethodeType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn, JSONObject requestMap) {
		JsonObjectRequest req = null;
		Log.i("url", url);
		if (Constants.checkInternetConnection((Context) activity)) {
			SsX509TrustManager.allowAllSSL();
			if (!isError || errlsn == null) {
				errlsn = this;
			}
			req = new JsonObjectRequest(callMethodeType, url, requestMap,
					listener, errlsn) {
				/**
				 * 重写父类方法，获取登录账户名
				 */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String, String> headers = new HashMap<String, String>();
					headers.put("AuthToken",
							Constants.GetResult_AuthToken((Context) activity));
					String cookie = Constants.GetResult_Cookie(
							(Context) activity).replace("Cookie=SESSIONID=",
							"ASP.NET_SessionId=");
					Log.i("cookie", cookie);
					headers.put("Cookie", cookie);
					headers.put("clientType", DeviceType);
					if (callMethodeType == Request.Method.GET) {
						TimeStamp = DateUtil.getTenTime();
						Imei = AppUtil.getIMEI(activity);
						headers.put("TimeStamp", TimeStamp);
						headers.put("Imei", Imei);
						String sign = "";
						String encSign = "";
						Map<String, String> paramMap = new TreeMap<String, String>(
								new Comparator<String>() {
									@Override
									public int compare(String o1, String o2) {
										String str1 = o1.toLowerCase();
										String str2 = o2.toLowerCase();
										if (str1.compareTo(str2) == 0) {
											return o1.compareTo(o2);
										}
										return str1.compareTo(str2);
									}
								});
						paramMap.put("TimeStamp", TimeStamp);
						paramMap.put("Imei", Imei);
						for (Iterator<String> it = paramMap.keySet().iterator(); it
								.hasNext();) {
							String key = it.next();
							encSign = encSign
									+ (key.toLowerCase() + "="
											+ paramMap.get(key) + "&");
						}
						sign = MD5.md5(encSign.substring(0,
								encSign.length() - 1)
								+ Constants.encryption_key);
						headers.put("Sign", sign);
					} else {
						headers.put("TimeStamp", TimeStamp);
						headers.put("Imei", Imei);
						headers.put("Sign", sign);
					}
					return headers;
				}

				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}

				@Override
				public RetryPolicy getRetryPolicy() {
					return super.getRetryPolicy();
				}

			};

			req.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			AppController.getInstance().addToRequestQueue(req);

		} else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {

				@Override
				public void run() {
					dialogManager.initOneBtnDialog("确定", "提示", "网络不给力", null);
					if (dftErrorListener != null) {
						dftErrorListener.webLoadError();
					}
				}
			});
		}

	}

	/**
	 * 向后台发送消息
	 * 
	 * @param url
	 *            网址
	 * @param callMethodeType
	 *            访问方式：如 Request.Method.POST
	 * @param listener
	 *            返回消息监听 ，通过重写的onResponse（）方法，对返回的JSONObject进行操作
	 * @param errlsn
	 *            错误消息监听
	 * @param requestMap
	 *            发送的JSONObject类型参数
	 */
	public String cookieFromResponse;
	private String mHeader;

	public void makeCookieJsonObjReq(String url, final String sign,
			final int callMethodeType, Response.Listener<JSONObject> listener,
			Response.ErrorListener errlsn, JSONObject requestMap) {
		JsonObjectRequest req = null;
		if (Constants.checkInternetConnection((Context) activity)) {
			SsX509TrustManager.allowAllSSL();
			req = new JsonObjectRequest(callMethodeType, url, requestMap,
					listener, this) {
				/**
				 * 重写父类方法，获取登录账户名
				 */
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					Map<String, String> headers = new HashMap<String, String>();
					headers.put("AuthToken",
							Constants.GetResult_AuthToken((Context) activity));
					headers.put("clientType", DeviceType);
					if (callMethodeType == Request.Method.GET) {
						TimeStamp = DateUtil.getTenTime();
						Imei = AppUtil.getIMEI(activity);
						headers.put("TimeStamp", TimeStamp);
						headers.put("Imei", Imei);
						String sign = "";
						String encSign = "";
						Map<String, String> paramMap = new TreeMap<String, String>(
								new Comparator<String>() {
									@Override
									public int compare(String o1, String o2) {
										String str1 = o1.toLowerCase();
										String str2 = o2.toLowerCase();
										if (str1.compareTo(str2) == 0) {
											return o1.compareTo(o2);
										}
										return str1.compareTo(str2);
									}
								});
						paramMap.put("TimeStamp", TimeStamp);
						paramMap.put("Imei", Imei);
						for (Iterator<String> it = paramMap.keySet().iterator(); it
								.hasNext();) {
							String key = it.next();
							encSign = encSign
									+ (key.toLowerCase() + "="
											+ paramMap.get(key) + "&");
						}
						sign = MD5.md5(encSign.substring(0,
								encSign.length() - 1)
								+ Constants.encryption_key);
						headers.put("Sign", sign);
					} else {
						headers.put("TimeStamp", TimeStamp);
						headers.put("Imei", Imei);
						headers.put("Sign", sign);
					}
					return headers;
				}

				@Override
				public String getBodyContentType() {
					return "application/json; charset=utf-8";
				}

				@Override
				public RetryPolicy getRetryPolicy() {
					return super.getRetryPolicy();
				}

				@Override
				protected Response<JSONObject> parseNetworkResponse(
						NetworkResponse response) {
					try {
						String jsonString = new String(response.data,
								HttpHeaderParser.parseCharset(response.headers));
						mHeader = response.headers.toString();
						// 使用正则表达式从reponse的头中提取cookie内容的子串
						Pattern pattern = Pattern.compile("Set-Cookie.*?;");
						Matcher m = pattern.matcher(mHeader);
						if (m.find()) {
							cookieFromResponse = m.group();
						}
						// 去掉cookie末尾的分号
						cookieFromResponse = cookieFromResponse.substring(11,
								cookieFromResponse.length() - 1);
						// 将cookie字符串添加到jsonObject中，该jsonObject会被deliverResponse递交，调用请求时则能在onResponse中得到
						JSONObject jsonObject = new JSONObject(jsonString);
						jsonObject.put("Cookie", cookieFromResponse);
						return Response.success(jsonObject,
								HttpHeaderParser.parseCacheHeaders(response));
					} catch (Exception e) {
						return Response.error(new ParseError(e));
					}
				}

			};

			req.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

			AppController.getInstance().addToRequestQueue(req);

		} else {
			Handler handler = new Handler(Looper.getMainLooper());
			handler.post(new Runnable() {

				@Override
				public void run() {
					dialogManager.initOneBtnDialog("确定", "提示", "网络不给力", null);
					if (dftErrorListener != null) {
						dftErrorListener.webLoadError();
					}
				}
			});
		}

	}

	@SuppressWarnings("unchecked")
	public Object GetResponseObject(JSONObject jsonResponse,
			@SuppressWarnings("rawtypes") Class class1) {
		Object obj = null;
		try {
			obj = new Gson().fromJson(jsonResponse.toString(), class1);
		} catch (Exception e) {
			e.printStackTrace();
			dialogManager.initOneBtnDialog("确定", "提示", "网络不给力", null);
			if (dftErrorListener != null) {
				dftErrorListener.webLoadError();
			}
		}

		return obj;
	}

	@Override
	public void onErrorResponse(VolleyError volleyError) {
		if (dftErrorListener != null) {
			dftErrorListener.webLoadError();
		}
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
				// Constants.ClearSharePref(activity.getBaseContext());
				// Constants.Clear_Cookie(activity.getBaseContext());
				// ActivityManager activityManager = (ActivityManager) activity
				// .getSystemService(Context.ACTIVITY_SERVICE);
				// String runningActivity = activityManager.getRunningTasks(1)
				// .get(0).topActivity.getClassName();

				// com.md.hjyg.activities.HomeActivity
				// if (runningActivity
				// .equals("com.md.hjyg.activities.HomeFragmentActivity"))
				// {
				// // 如果当前活动的为HomeActivity，则不跳转
				// } else
				// {

				// Constants.ClearSharePref(activity);
				dialogManager.initOneBtnDialog("确定", "提示", "您的登录状态已失效，请重新登录！",
						new OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {
								Intent loginIntent = new Intent(activity,
										LoginActivity.class);
								activity.startActivity(loginIntent);

							}
						});

			} else {
				dialogManager.initOneBtnDialog("确定", "提示", "网络不给力", null);
			}
		}

	}

	/**
	 * post方法加密 根据传的参数的到加密信息
	 * 
	 * @param jsonParams
	 *            传的参数
	 * @return sign
	 */
	public String getSignbyJSONObject(JSONObject jsonParams) {
		String sign = "";
		String encSign = "";
		if (jsonParams == null || jsonParams.length() == 0) {
			return sign;
		}
		TimeStamp = DateUtil.getTenTime();
		Imei = AppUtil.getIMEI(activity);
		Map<String, String> paramMap = new TreeMap<String, String>(
				new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						String str1 = o1.toLowerCase();
						String str2 = o2.toLowerCase();
						if (str1.compareTo(str2) == 0) {
							return o1.compareTo(o2);
						}
						return str1.compareTo(str2);
					}
				});
		paramMap.put("TimeStamp", TimeStamp);
		paramMap.put("Imei", Imei);
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> it = jsonParams.keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				String value = jsonParams.getString(key);
				paramMap.put(key, value);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		for (Iterator<String> it = paramMap.keySet().iterator(); it.hasNext();) {
			String key = it.next();
			if (paramMap.get(key) != null && !paramMap.get(key).equals("")) {
				encSign = encSign
						+ (key.toLowerCase() + "=" + paramMap.get(key) + "&");
			}
		}
		sign = MD5.md5(
				encSign.substring(0, encSign.length() - 1)
						+ Constants.encryption_key).toLowerCase();

		return sign;
	}

	/**
	 * 网络接口加载错误接口 ClassName: DftErrorListener date: 2016-4-14 上午8:48:30 remark:
	 * 
	 * @author pyc
	 */
	public interface DftErrorListener {
		/**
		 * 网络加载错误
		 */
		public void webLoadError();
	}
}
