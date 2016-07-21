package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.OrderDetail;
import com.md.hjyg.entity.RechargeDetails;
import com.md.hjyg.entity.YintongSubmit;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.BankManager;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.BaseHelper;
import com.md.hjyg.utils.MobileSecurePayer;
import com.md.hjyg.utils.PayOrder;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 充值界面
 */
public class RechargeActivity extends BaseActivity implements
		View.OnClickListener {
	public final static String FROM_BUY = "fromBuy";
	private LinearLayout   query_bankquota;
	private EditText ed_top_up,  ed_bank_card;
	private Context mcontext;
	private RechargeDetails rechargeDetails;
	private OrderDetail orderDetail;
	private Handler mHandler;
	private String idNumber, RechargeMoney, statusMessage, real_name, bank_no,
			memberId, bankName;
	private boolean isBankCard;
	private int status;
	private TextView tv_rechargedes, tv_changeCardDes;
	private Bitmap aly;
	private ImageView img_aly;
	private boolean fromBuy = false;
	private Intent intent;
	private DialogManager dialogManager;
	private RelativeLayout rel_top;
	private TextView tv_add, edit_bank_details,tv_BankName,tv_branch_name,ed_card_no,btn_recharge;
	private ImageView bank_img;
	private LinearLayout line_bank_card;
	private View view_line;
	private boolean toBank;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recharge);
		mcontext = getBaseContext();
		findViews();
		setImg();
		init();
		btn_recharge.setOnClickListener(this);
		// rl_add_bank_card.setOnClickListener(this);
		// ed_bank_card.setFocusable(false);

		// if (Constants.GetisFirstAddBankCard(mcontext)) {
		// GetWebserviceAccountInformationAPI(mcontext);
		// } else {
		// getWebserviceRechargeAPI(mcontext);
		// }

		getWebserviceRechargeAPI(mcontext);
	}

	/**
	 * 界面控件初始化
	 */
	private void findViews() {
		// TextView
		tv_rechargedes = (TextView) findViewById(R.id.tv_rechargedes);
		tv_changeCardDes = (TextView) findViewById(R.id.tv_changeCardDes);
		// LinearLayout
		query_bankquota = (LinearLayout) findViewById(R.id.query_bankquota);
		// Button
		btn_recharge =  (TextView) findViewById(R.id.btn_recharge);
		// EditText
		ed_bank_card = (EditText) findViewById(R.id.ed_bank_card);
		ed_top_up = (EditText) findViewById(R.id.ed_top_up);
		img_aly = (ImageView) findViewById(R.id.img_aly);
		img_aly.setOnClickListener(this);
		// Relative Layout
		// rl_add_bank_card = (RelativeLayout)
		// findViewById(R.id.rl_add_bank_card);
		rel_top = (RelativeLayout) findViewById(R.id.rel_top);
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_BankName = (TextView) findViewById(R.id.tv_BankName);
		tv_branch_name = (TextView) findViewById(R.id.tv_branch_name);
		edit_bank_details = (TextView) findViewById(R.id.edit_bank_details);
		ed_card_no = (TextView) findViewById(R.id.ed_card_no);
		bank_img = (ImageView) findViewById(R.id.bank_img);
		line_bank_card = (LinearLayout) findViewById(R.id.line_bank_card);
		view_line = findViewById(R.id.view_line);
		int rel_topWHs[] = Save.getScaleBitmapWangH(660, 200);
		ViewParamsSetUtil.setViewHandW_lin(rel_top, rel_topWHs[1],
				rel_topWHs[0]);
		ViewParamsSetUtil
				.setViewHandW_lin(tv_add, rel_topWHs[1], rel_topWHs[0]);
		edit_bank_details.setOnClickListener(this);
	}

	/**
	 * 初始化
	 */
	private void init() {
		HeaderViewControler.setHeaderView(this, "充值", this);
		fromBuy = getIntent().getBooleanExtra(FROM_BUY, false);
		rechargeDetails = new RechargeDetails();
		mHandler = createHandler();
		query_bankquota.setOnClickListener(this);

		dialogManager = new DialogManager(this);
	}

	/**
	 * 充值支付前，获取银行卡等信息，用以判断是添加银行卡还是直接充值
	 */
	private void getWebserviceRechargeAPI(final Context mcontext) {

		dft.getRecharge(Constants.GetRechargeDetails_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// Log.e(TAG, "Response" + response);
						// 获取充值信息，用以判断用户是否有过充值的过程
						rechargeDetails = (RechargeDetails) dft
								.GetResponseObject(response,
										RechargeDetails.class);
						tv_rechargedes.setText(rechargeDetails.RechargeDes);
						tv_changeCardDes.setText(rechargeDetails.ChangeCardDes);
						isBankCard = rechargeDetails.IsBankCard;
						if (isBankCard == false) {// 显示添加银行卡的界面
							tv_add.setVisibility(View.VISIBLE);
							line_bank_card.setVisibility(View.VISIBLE);
							view_line.setVisibility(View.VISIBLE);
							rel_top.setVisibility(View.GONE);
							tv_add.setText("用户："+rechargeDetails.RealName);
							
							// rl_add_bank_card.setVisibility(View.VISIBLE);
							ed_bank_card.setEnabled(true);
						} else {// 直接显示充值界面
							tv_add.setVisibility(View.GONE);
							view_line.setVisibility(View.GONE);
							rel_top.setVisibility(View.VISIBLE);
							line_bank_card.setVisibility(View.GONE);
							setUIData();
							// rl_add_bank_card.setVisibility(View.GONE);
							// 身份证号
							idNumber = rechargeDetails.BankList.get(0).Member.IDNumber;
							// 用户ID
							memberId = String.valueOf(rechargeDetails.BankList
									.get(0).Member.Id);
							// 用户真实姓名
							real_name = rechargeDetails.BankList.get(0).Member.RealName
									.toString();
							// 银行卡号
							bank_no = rechargeDetails.BankList.get(0).BankNumber
									.toString();
							// 银行名称
							bankName = rechargeDetails.BankList.get(0).BankNames;
							// 把消息保存到SharedPreferences
							Constants.SetBankIdNumber(idNumber, mcontext);
							Constants.SetMemberId(memberId, mcontext);
							Constants.SetRealName(real_name, mcontext);
							Constants.SetBankNumber(bank_no, mcontext);

							// Log.e(TAG, "idNumber--" + idNumber +
							// "--memberId--"
							// + memberId + "---real_name-" + real_name
							// + "-bank_no--" + bank_no);
						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});
	}

	/**
	 * 更新充值界面数据，设置银行卡等信息
	 */
	private void setUIData() {
		// idNumber = rechargeDetails.IDNumber.toString();
		try {
			bank_no = rechargeDetails.BankList.get(0).BankNumber.toString();
			bankName = rechargeDetails.BankList.get(0).BankNames;
			ed_bank_card.setText(bankName + " "
					+ NewreplaceBankNumberformat(bank_no));
			tv_BankName.setText(bankName);
			tv_branch_name.setText(rechargeDetails.BankList.get(0).BankBranchName);
			ed_card_no.setText(BankManager.Newreplacecardformat(bank_no));
			BankManager.setBankCardBg(bankName, bank_img);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 设置点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			if (fromBuy) {
				fromBuy = false;
				this.finish();
				overTransition(1);
			} else {
				intent = new Intent(RechargeActivity.this,
						HomeFragmentActivity.class);
				intent.putExtra("tab", 2);
				startActivity(intent);
				overTransition(1);
				this.finish();
			}
			break;
		case R.id.btn_recharge:// 直接充值
			RechargeMoney = ed_top_up.getText().toString().replaceAll(" ", "");
			if (RechargeMoney == null || RechargeMoney.equals("")) {
				Toast.makeText(this, "请输入充值金额", Toast.LENGTH_SHORT).show();
			} else if (RechargeMoney.indexOf(".") != -1
					&& ((RechargeMoney.length() - 1
							- RechargeMoney.indexOf(".") >= 3))) {
				Toast.makeText(this, "最少充值单位为分，请重新输入金额", Toast.LENGTH_LONG)
						.show();
			} else {
				btn_recharge.setEnabled(false);
				btn_recharge.setText("正在充值");
				btn_recharge.setTextColor(getResources().getColor(R.color.gray));
				if (!isBankCard) {
					// 没有绑定银行卡，银行卡号取输入值
					bank_no = ed_bank_card.getText().toString();
					// CallAddBankCardWebservice("", bank_no, bank_no, "", "",
					// "", "");
					callWebservicePostRecharge(bank_no, RechargeMoney, idNumber);
				} else {

					callWebservicePostRecharge(bank_no, RechargeMoney, idNumber);
				}
				// callStandPayment();
			}
			break;
		case R.id.query_bankquota:// 银行限额列表
			startActivity(new Intent(this, BankQuotaActivity.class));
			overTransition(2);
			break;
		case R.id.img_aly:
			startActivity(new Intent(this, TaiPingYangCPICActivity.class));
			overTransition(2);
			break;
		case R.id.edit_bank_details:
			if (rechargeDetails.BankList != null
					&& rechargeDetails.BankList.size() > 0) {
				toBank = true;
				Intent intent = new Intent(this,
						BankBranchDetailsEditActivity.class);
				String bankCardId = String
						.valueOf(rechargeDetails.BankList.get(0).Id);
				intent.putExtra("bankCardId", bankCardId);
				intent.putExtra("BankCardDes", rechargeDetails.BankCardDes);
				intent.putExtra("BranchName", rechargeDetails.BankList.get(0).BankBranchName);
				startActivity(intent);
				overTransition(2);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		if (fromBuy) {
			fromBuy = false;
			this.finish();
			overTransition(1);
		} else {
			intent = new Intent(RechargeActivity.this,
					HomeFragmentActivity.class);
			intent.putExtra("tab", 2);
			startActivity(intent);
			overTransition(1);
			RechargeActivity.this.finish();
		}
	}

	/**
	 * 开始支付
	 */
	private void callStandPayment() {
		PayOrder order = null;
		// 构建订单信息
		order = constructPreCardPayOrder();
		String content4Pay = BaseHelper.toJSONString(order);
		// String a = BaseHelper.sortParam(order).toString();
		MobileSecurePayer msp = new MobileSecurePayer();
		// 调用连连支付sdk开始支付 boolean bRet =
		msp.pay(content4Pay, mHandler,
				com.md.hjyg.utils.Constants.RQF_PAY, this,
				false);

	}

	/**
	 * 构建订单数据
	 */
	private PayOrder constructPreCardPayOrder() {
		PayOrder order = new PayOrder();
		YintongSubmit yintongSubmit = orderDetail.YintongSubmit;
		if (yintongSubmit != null) {
			order.setBusi_partner(yintongSubmit.Busi_partner);// 商户业务类型
			order.setNo_order(yintongSubmit.No_order);// 订单号
			order.setDt_order(yintongSubmit.Timestamp);// 订单时间
			order.setName_goods(yintongSubmit.Name_goods); // 商品名称
			order.setNotify_url(yintongSubmit.Notify_url); // 异步通知地址
			order.setSign_type(yintongSubmit.Sign_type); // 签名方式 MD5
			order.setValid_order(yintongSubmit.Valid_order); // 订单有效时间，单位：分钟
			order.setUser_id(yintongSubmit.User_id); // 商户用户唯一编号
			order.setId_no(yintongSubmit.Id_no); // 证件号码
			order.setAcct_name(yintongSubmit.Acct_name); // 银行账户名称
			order.setMoney_order(yintongSubmit.Money_order); // 交易金额
			order.setCard_no(yintongSubmit.Card_no); // 银行卡号
			order.setNo_agree(yintongSubmit.No_agree); // 签约协议号
			order.setFlag_modify(yintongSubmit.Flag_modify);
			order.setRisk_item(yintongSubmit.Risk_item); // 风险控制参数
			// 订单信息签名
			order.setOid_partner(yintongSubmit.Oid_partner);// 商户号
			order.setSign(yintongSubmit.Sign);
		}
		return order;
	}

	/**
	 * 支付完成后的处理
	 * 
	 * @return
	 */
	@SuppressLint("HandlerLeak")
	private Handler createHandler() {
		return new Handler() {
			public void handleMessage(Message msg) {
				String strRet = (String) msg.obj;
				initBtn();
				switch (msg.what) {
				case com.md.hjyg.utils.Constants.RQF_PAY: {
					JSONObject objContent = BaseHelper.string2JSON(strRet);
					String ret_code = objContent.optString("ret_code");
					String ret_msg = objContent.optString("ret_msg");
					// 先判断状态码，状态码为 成功或处理中 的需要 验签
					if (com.md.hjyg.utils.Constants.RET_CODE_SUCCESS
							.equals(ret_code)) {// 支付成功
						String result_pay = objContent.optString("result_pay");
						if (com.md.hjyg.utils.Constants.RESULT_PAY_SUCCESS
								.equalsIgnoreCase(result_pay)) {
							// String sign = objContent.optString("sign");
							// String oid_paybill = objContent
							// .optString("oid_paybill");
							// String no_order =
							// objContent.optString("no_order");
							// String oid_partner = objContent
							// .optString("oid_partner");
							// String agreementno = objContent
							// .optString("agreementno");
							// String sign_type = objContent
							// .optString("sign_type");
							// String dt_order =
							// objContent.optString("dt_order");
							// String money_order = objContent
							// .optString("money_order");
							// String settle_date = objContent
							// .optString("settle_date");
							// String info_order = objContent
							// .optString("info_order");
							// TODO 支付成功后续处理
							// callWebserviceAfterRecharge(sign, oid_paybill,
							// ret_code, result_pay, no_order,
							// oid_partner, agreementno, sign_type,
							// ret_msg, dt_order, money_order,
							// settle_date, info_order);
							AppController.AccountInfIsChange = true;
							dialogManager.initOneBtnDialog();
							dialogManager.setButText("确定");
							dialogManager.setTitleandContent("提示", "支付成功!");
							dialogManager
									.setOnDismissListener(new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											// 交易成功，跳转到我的账户界面
											if (fromBuy) {
												fromBuy = false;
												RechargeActivity.this.finish();
												overTransition(1);
											} else {
												intent = new Intent(
														RechargeActivity.this,
														HomeFragmentActivity.class);
												intent.putExtra("tab", 2);
												startActivity(intent);
												overTransition(1);
												RechargeActivity.this.finish();
											}

										}
									});
							dialogManager.Show();
							// BaseHelper.showDialog(context, "提示",
							// "支付成功，交易状态码："
							// + ret_code,
							// android.R.drawable.ic_dialog_alert,
							// new DialogInterface.OnClickListener() {
							//
							// @Override
							// public void onClick(
							// DialogInterface dialog,
							// int which) {
							// // TODO Auto-generated method stub
							//
							// // 交易成功，跳转到我的账户界面
							// if (fromBuy) {
							// fromBuy = false;
							// RechargeActivity.this.finish();
							// overTransition(1);
							// } else {
							// intent = new Intent(
							// RechargeActivity.this,
							// HomeFragmentActivity.class);
							// intent.putExtra("tab", 2);
							// startActivity(intent);
							// overTransition(1);
							// RechargeActivity.this.finish();
							// }
							// }
							// });

						} else {
							// BaseHelper.showDialog(context, "提示", ret_msg
							// + "，交易状态码:" + ret_code,
							// android.R.drawable.ic_dialog_alert, null);
							dialogManager.initOneBtnDialog();
							dialogManager.setButText("确定");
							dialogManager
									.setTitleandContent("提示", ret_msg + "");
							dialogManager.Show();
						}

					} else if (com.md.hjyg.utils.Constants.RET_CODE_PROCESS
							.equals(ret_code)) { // 支付处理中
						String resulPay = objContent.optString("result_pay");
						if (com.md.hjyg.utils.Constants.RESULT_PAY_PROCESSING
								.equalsIgnoreCase(resulPay)) {
							dialogManager.initOneBtnDialog();
							dialogManager.setButText("确定");
							dialogManager
									.setTitleandContent("提示", ret_msg + "");
							dialogManager.Show();
							// BaseHelper.showDialog(context, "提示",
							// objContent.optString("ret_msg") + "交易状态码："
							// + ret_code,
							// android.R.drawable.ic_dialog_alert, null);
						}

					} else {// 其他情况
						// BaseHelper.showDialog(context, "提示", ret_msg
						// + "，交易状态码:" + ret_code,
						// android.R.drawable.ic_dialog_alert, null);
						dialogManager.initOneBtnDialog();
						dialogManager.setButText("确定");
						dialogManager.setTitleandContent("提示", ret_msg + "");
						dialogManager.Show();
					}
				}
					break;
				}
				super.handleMessage(msg);
			}
		};

	}

	/**
	 * 支付前处理充值记录
	 * 
	 * @param rechargeMoney
	 *            交易金额
	 * @param idNumber
	 *            银行账号
	 * @param BankName
	 *            银行名
	 */
	private void callWebservicePostRecharge(String rechargeMoney,
			String idNumber, String BankName) {

		dft.getRechargeDetailsDetails(rechargeMoney, idNumber, BankName,
				Constants.YintongPayPost_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {
							orderDetail = (OrderDetail) dft.GetResponseObject(
									response, OrderDetail.class);
							status = orderDetail.Notification.ProcessResult;
							statusMessage = orderDetail.Notification.ProcessMessage
									.toString();
							if (status == 1) {
								// 当充值记录插入成功以后，跳到连连支付页面进行支付
								callStandPayment();
							} else if (status == 0) {
								initBtn();
								Constants.showOkPopup(RechargeActivity.this,
										statusMessage);
							} else if (status == 2) {
								initBtn();
								Constants.showOkPopup(RechargeActivity.this,
										statusMessage);
							}
						} catch (Exception ex) {
							initBtn();
							Toast.makeText(mcontext,
									getString(R.string.data_error),
									Toast.LENGTH_SHORT).show();
							ex.printStackTrace();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});

	}

	/**
	 * 支付后处理充值记录
	 * 
	 * @param rechargeMoney
	 *            交易金额
	 * @param idNumber
	 *            银行账号
	 * @param BankName
	 *            银行名
	 */
	// private void callWebserviceAfterRecharge(String sign, String oid_paybill,
	// String ret_code, String result_pay, String no_order,
	// String oid_partner, String agreementno, String sign_type,
	// String ret_msg, String dt_order, String money_order,
	// String settle_date, String info_order) {
	//
	// dft.postAfterPaySuccess(sign, oid_paybill, ret_code, result_pay,
	// no_order, oid_partner, agreementno, sign_type, ret_msg,
	// dt_order, money_order, settle_date, info_order,
	// Constants.YintongPayPostBack_URL, Request.Method.POST,
	// new Response.Listener<JSONObject>() {
	// @Override
	// public void onResponse(JSONObject response) {
	//
	// try {
	// Notification notification = (Notification) dft.GetResponseObject(
	// response, Notification.class);
	// status = notification.ProcessResult;
	// statusMessage = notification.ProcessMessage
	// .toString();
	// if (status == 0) {
	// Constants.showOkPopup(RechargeActivity.this, statusMessage);
	// // Constants.showOkPopup(RechargeActivity.this,
	// // statusMessage,
	// // new DialogInterface.OnClickListener() {
	// // @Override
	// // public void onClick(
	// // DialogInterface dialogInterface,
	// // int i) {
	// // }
	// // });
	// } else if (status == 1) {
	//
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	//
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// // Show error or whatever...
	// }
	// });
	//
	// }

	// 设置银行卡的显示模式
	public String NewreplaceBankNumberformat(String bank_no) {
		String temp_bank_number = null;
		try {
			String str1 = bank_no.substring(0, 5);
			String str2 = bank_no.substring(bank_no.length() - 4,
					bank_no.length());
			String str3 = "";
			for (int i = 4; i < bank_no.length() - 4; i++) {
				str3 += "*";
				if (i == 7)
					str3 += " ";
			}
			temp_bank_number = str1 + " " + str3 + " " + str2;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp_bank_number;
	}

	// /**第一次充值添加银行卡*/
	// private void CallAddBankCardWebservice(String bankName, String
	// bankNumber,
	// String enConfirmBankNumber, String selectedProvince,
	// String selectedCity, String subbranch, String mobileCode) {
	//
	// dft.getAddBankCardWebservice(bankName, bankNumber, enConfirmBankNumber,
	// selectedProvince, selectedCity, subbranch, mobileCode,
	// Constants.AddBankCard_URL, Request.Method.POST,
	// new Response.Listener<JSONObject>() {
	// @Override
	// public void onResponse(JSONObject response) {
	//
	// // notifications = (Notification)
	// // dft.GetResponseObject(response, Notification.class);
	// AddbankCard addbankcard = (AddbankCard) dft.GetResponseObject(
	// response, AddbankCard.class);
	//
	// Toast.makeText(context, addbankcard.Notification.ProcessMessage,
	// 1).show();
	// // Error if
	// if (addbankcard.Notification.ProcessResult == 0) {
	//
	//
	// } else if (addbankcard.Notification.ProcessResult == 1) {
	//
	// } else if (addbankcard.Notification.ProcessResult == 2) {
	//
	// }
	//
	// }
	//
	// }, null);
	//
	// }

	/** 把充值按钮设置成可点击 */
	private void initBtn() {
		btn_recharge.setEnabled(true);
		btn_recharge.setText("充   值");
		btn_recharge.setTextColor(getResources().getColor(R.color.red));
	}

	/***
	 * 加载和缩放图片
	 */
	private void setImg() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				Bitmap reimg = BitmapFactory.decodeResource(getResources(),
						R.drawable.novice_red_bg);
				aly = Save.ScaleBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.aliyun), mcontext, reimg);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						img_aly.setImageBitmap(aly);
					}
				});

			}
		}).start();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if (toBank) {
			getWebserviceRechargeAPI(mcontext);
			toBank = false;
		}
	}
	
	
}
