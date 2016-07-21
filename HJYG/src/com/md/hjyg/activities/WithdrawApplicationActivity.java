package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.BuyNowDetails;
import com.md.hjyg.entity.WithdrawDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 提现
 */
public class WithdrawApplicationActivity extends BaseActivity implements
		View.OnClickListener, DftErrorListener {

	private Button btn_withdrawals;// 提现 1元/笔
	private EditText ed_withdrawal_amt, ed_withdrawal_password;
	// 可用金额 和 可提现金额 银行卡号
	private TextView ed_withdraw_available_bal, ed_bank_card_withdrawals,
			ed_withdrawal_account, freecount_tv, freeamount_tv;
	private Context mcontext;

	private WithdrawDetails withdrawDetails;
	// private BankCardList bank_cardList;
	private BuyNowDetails buyNowDetails;
	private int CardId, status;
	private double bank_card_withdrawals, available_balance;
	// withdrawType,取消提现方式 availableBalance banCardAmt,
	private String bankCardWithdrwals, withdrawAmt, withdrawPassword,
			statusMessage, bankCardId;
	// private boolean isBankCard;
	/** 提现手续费说明已经其他提示 */
	private TextView tv_commission, tv_changepwddes, tv_withdrawldes;
	/** 提交数据加载框 */
	private DialogProgressManager dialog;

	private ImageView img_aly, img_bank;
	private final int imgwhat = 1;
	private Bitmap[] bitmaps;
	private LinearLayout lin_top, line;
	private DialogManager dialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_withdraw_new);// layout_withdraw_application
		mcontext = getBaseContext();
		findViews();
		init();
		Save.loadingImg(mHandler, mcontext, new int[] { R.drawable.buy_top_bg,
				R.drawable.aliyun }, imgwhat);
		GetWebserviceWithdrawApplicationAPI(mcontext);
		btn_withdrawals.setOnClickListener(this);
	}

	private void GetWebserviceWithdrawApplicationAPI(Context mcontext) {

		dft.getWithdrawApplication(Constants.WithdrawLogs_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						withdrawDetails = (WithdrawDetails) dft
								.GetResponseObject(response,
										WithdrawDetails.class);

						setUIData();
						getData();

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});
	}

	private void setUIData() {

		available_balance = withdrawDetails.LeaveAmount;
		bank_card_withdrawals = withdrawDetails.WithdrawLogAmount;
		bankCardWithdrwals = String.valueOf(bank_card_withdrawals);

		CardId = withdrawDetails.BankCardList.get(0).Id;
		bankCardId = String.valueOf(CardId);
		String bankName = withdrawDetails.BankCardList.get(0).BankNames;
		String bank_no = withdrawDetails.BankCardList.get(0).BankNumber;

		ed_withdraw_available_bal.setText(Constants
				.StringToCurrency(available_balance + "") + "元");
		ed_bank_card_withdrawals.setText(Constants
				.StringToCurrency(bankCardWithdrwals));
		setBankCardBg(bankName);
		ed_withdrawal_account.setText(bankName + "  尾号:"
				+ bank_no.substring(bank_no.length() - 4, bank_no.length()));
		// 手续费： 1元/笔, 2016年元月1日前免费
		String s = withdrawDetails.CommissionCharge;
		tv_commission.setText(TextUtil.getHtmlText(s));
		tv_changepwddes.setText(withdrawDetails.ChangePwdDes);
		tv_withdrawldes.setText(withdrawDetails.WithdrawlDes);
		freeamount_tv.setText(TextUtil.getHtmlText(withdrawDetails.Fee + "元 "));
		freecount_tv.setText(TextUtil
				.getHtmlText(withdrawDetails.WithdrawFreeDesc));
	}
	
	// 设置银行卡背景图片
		private void setBankCardBg(String BankName){
			int[] bankims = {R.drawable.bt1,R.drawable.bt2,R.drawable.bt3,R.drawable.bt4,R.drawable.bt5,R.drawable.bt6,R.drawable.bt7,
					R.drawable.bt8,R.drawable.bt9,R.drawable.bt10,R.drawable.bt11,R.drawable.bt12,R.drawable.bt13,R.drawable.bt14,
					R.drawable.bt15,R.drawable.bt16,R.drawable.bt17,R.drawable.bt18,R.drawable.bt19,R.drawable.bt20,R.drawable.bt21,R.drawable.bt22,
					R.drawable.bt23,R.drawable.bt24,R.drawable.bt25,R.drawable.bt26,R.drawable.bt27,
					R.drawable.bt28,R.drawable.bt29,R.drawable.bt30,R.drawable.bt31, R.drawable.bt32};
			 if(BankName.indexOf("花旗银行") != -1)
	         {
	             img_bank.setImageResource(bankims[0]);
	         }
	         else if (BankName.indexOf("中国建设银行") != -1)
	         {
	        	 img_bank.setImageResource(bankims[1]);
	         }
	         else if (BankName.indexOf("浦发银行") != -1)
	         {
	              img_bank.setImageResource(bankims[2]);
	         }
	         else if (BankName.indexOf("交通银行") != -1)
	         {
	              img_bank.setImageResource(bankims[3]);
	         }
	         else if (BankName.indexOf("兴业银行") != -1)
	         {
	              img_bank.setImageResource(bankims[4]);
	         }
	         else if (BankName.indexOf("龙江银行") != -1)
	         {
	              img_bank.setImageResource(bankims[5]);
	         }
	         else if (BankName.indexOf("农业银行") != -1)
	         {
	              img_bank.setImageResource(bankims[6]);
	         }
	         else if (BankName.indexOf("中国邮政") != -1)
	         {
	              img_bank.setImageResource(bankims[0]);
	         }
	         else if (BankName.indexOf("民生银行") != -1)
	         {
	              img_bank.setImageResource(bankims[8]);
	         }
	         else if (BankName.indexOf("农村合作信用社") != -1)
	         {
	              img_bank.setImageResource(bankims[9]);
	         }
	         else if (BankName.indexOf("渤海银行") != -1)
	         {
	              img_bank.setImageResource(bankims[10]);
	         }
	         else if (BankName.indexOf("光大银行") != -1)
	         {
	              img_bank.setImageResource(bankims[11]);
	         }
	         else if (BankName.indexOf("平安银行") != -1)
	         {
	              img_bank.setImageResource(bankims[12]);
	         }
	         else if (BankName.indexOf("宁波银行") != -1)
	         {
	              img_bank.setImageResource(bankims[13]);
	         }
	         else if (BankName.indexOf("深圳发展银行") != -1)
	         {
	              img_bank.setImageResource(bankims[14]);
	         }
	         else if (BankName.indexOf("汉口银行") != -1)
	         {
	              img_bank.setImageResource(bankims[15]);
	         }
	         else if (BankName.indexOf("工商银行") != -1)
	         {
	              img_bank.setImageResource(bankims[16]);
	         }
	         else if (BankName.indexOf("招商银行") != -1)
	         {
	              img_bank.setImageResource(bankims[17]);
	         }
	         else if (BankName.indexOf("中国银行") != -1)
	         {
	              img_bank.setImageResource(bankims[18]);
	         }
	         else if (BankName.indexOf("广发银行") != -1)
	         {
	              img_bank.setImageResource(bankims[19]);
	         }
	         else if (BankName.indexOf("中信银行") != -1)
	         {
	              img_bank.setImageResource(bankims[20]);
	         }
	         else if (BankName.indexOf("华夏银行") != -1)
	         {
	              img_bank.setImageResource(bankims[21]);
	         }
	         else if (BankName.indexOf("北京银行") != -1)
	         {
	              img_bank.setImageResource(bankims[22]);
	         }
	         else if (BankName.indexOf("南京银行") != -1)
	         {
	              img_bank.setImageResource(bankims[23]);
	         }
	         else if (BankName.indexOf("上海银行") != -1)
	         {
	              img_bank.setImageResource(bankims[24]);
	         }
	         else if (BankName.indexOf("江苏银行") != -1)
	         {
	              img_bank.setImageResource(bankims[25]);
	         }
	         else if (BankName.indexOf("广东南粤银行") != -1)
	         {
	              img_bank.setImageResource(bankims[26]);
	         }
	         else if (BankName.indexOf("桂林银行") != -1)
	         {
	              img_bank.setImageResource(bankims[27]);
	         }
	         else if (BankName.indexOf("浙商银行") != -1)
	         {
	              img_bank.setImageResource(bankims[28]);
	         }
	         else if (BankName.indexOf("杭州银行") != -1)
	         {
	              img_bank.setImageResource(bankims[30]);
	         }
	         else if (BankName.indexOf("长沙银行") != -1) 
	         {
	        	 img_bank.setImageResource(bankims[31]);
	         }
	         else
	         {
	              img_bank.setImageResource(bankims[29]);
	         }
		}

	private void init() {
		dialog = new DialogProgressManager(this, "交易正在处理中...");
		dft.setOnDftErrorListener(this);
		HeaderViewControler.setHeaderView(this, "提现", this);
		withdrawDetails = new WithdrawDetails();
		buyNowDetails = new BuyNowDetails();

		// 弹出数字键盘
		ed_withdrawal_amt.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		dialogManager = new DialogManager(this);
	}

	private void findViews() {
		// TextView
		// tv_title = (TextView) findViewById(R.id.tv_title);
		tv_commission = (TextView) findViewById(R.id.tv_commission);
		tv_changepwddes = (TextView) findViewById(R.id.tv_changepwddes);
		tv_withdrawldes = (TextView) findViewById(R.id.tv_withdrawldes);
		// Button
		btn_withdrawals = (Button) findViewById(R.id.btn_withdrawals);
		freecount_tv = (TextView) findViewById(R.id.freecount_tv);
		freeamount_tv = (TextView) findViewById(R.id.freeamount_tv);
		// EditText
		ed_withdraw_available_bal = (TextView) findViewById(R.id.ed_withdraw_available_bal);
		ed_bank_card_withdrawals = (TextView) findViewById(R.id.ed_bank_card_withdrawals);
		ed_withdrawal_account = (TextView) findViewById(R.id.ed_withdrawal_account);
		ed_withdrawal_password = (EditText) findViewById(R.id.ed_withdrawal_password);
		ed_withdrawal_amt = (EditText) findViewById(R.id.ed_withdrawal_amt);
		ed_withdraw_available_bal.setFocusable(false);
		ed_bank_card_withdrawals.setFocusable(false);

		img_aly = (ImageView) findViewById(R.id.img_aly);
		img_bank = (ImageView) findViewById(R.id.img_bank);
		img_aly.setOnClickListener(this);

		lin_top = (LinearLayout) findViewById(R.id.lin_top);
		line = (LinearLayout) findViewById(R.id.line);

		setViewHight(btn_withdrawals, 0.08f);
		setViewHight(line, 0.08f);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.img_aly:
			startActivity(new Intent(this, TaiPingYangCPICActivity.class));
			overTransition(2);
			break;
		case R.id.btn_withdrawals:
			if (withdrawDetails.PayPwd == null
					|| withdrawDetails.PayPwd.length() == 0) {
				dialogManager.initTwoBtnDialog("下次再说", "马上去", "提示",
						"您还没有设置过交易密码，请先设置交易密码再提现！", new OnClickListener() {

							@Override
							public void onClick(View v) {
								switch (v.getId()) {
								case DialogManager.CANCEL_BTN:
									dialogManager.dismiss();
									break;
								case DialogManager.CONFIRM_BTN:
									dialogManager.dismiss();
									startActivity(new Intent(
											WithdrawApplicationActivity.this,
											SetDealPasswordActivity.class));
									overTransition(2);
									break;

								default:
									break;
								}

							}
						});
				return;
			}

			withdrawAmt = ed_withdrawal_amt.getText().toString();
			withdrawPassword = ed_withdrawal_password.getText().toString();
			if (withdrawAmt.length() == 0 || withdrawPassword.length() < 6) {
				dialogManager.initOneBtnDialog("确定", "提示", "请输入正确的金额和提现密码！",
						null);
				return;
			}

			CallWithdrawWebservice(withdrawPassword, withdrawAmt, bankCardId);
			break;

		default:
			break;
		}

	}

	private void getData() {

		// availableBalance = ed_withdraw_available_bal.getText().toString();
		// banCardAmt = ed_bank_card_withdrawals.getText().toString();
		withdrawAmt = ed_withdrawal_amt.getText().toString();
		withdrawPassword = ed_withdrawal_password.getText().toString();

	}

	private void CallWithdrawWebservice(String withdrawPassword,
			String withdrawAmt, String bankCardId) {

		setBtnState(false, "处理中", getResources().getColor(R.color.gray));

		dft.getWithdrawPost(withdrawPassword, withdrawAmt, bankCardId,
				Constants.WithdrawLogsPost_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// Log.e("RESPONSE WITHDRAW:----", "WITHdrawadhf---- "
						// + response);

						buyNowDetails = (BuyNowDetails) dft.GetResponseObject(
								response, BuyNowDetails.class);

						status = buyNowDetails.Notification.ProcessResult;
						statusMessage = buyNowDetails.Notification.ProcessMessage
								.toString();
						setBtnState(true, "申   请",
								getResources().getColor(R.color.red));
						if (status == 1) {
							ed_withdrawal_amt.setText("");
							ed_withdrawal_password.setText("");
							AppController.AccountInfIsChange = true;
							dialogManager.initOneBtnDialog("确定", "提现成功",
									statusMessage, new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											WithdrawApplicationActivity.this
													.finish();
											overTransition(1);
										}
									});

						} else if (status == 2) {
							dialogManager.initOneBtnDialog("确定", "提现失败",
									statusMessage, null);
						} else if (status == 0) {
							dialogManager.initOneBtnDialog("确定", "提现失败",
									statusMessage, null);
						}

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});
	}

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

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		GetWebserviceWithdrawApplicationAPI(mcontext);
	}

	/***
	 * 设置按钮状态，已及加载框
	 * 
	 * @param bol
	 *            是否可以点击
	 * @param msg
	 *            按钮显示的文字
	 * @param rId
	 *            背景资源ID
	 */
	private void setBtnState(boolean bol, String msg, int rId) {
		if (bol) {
			dialog.dismiss();
		} else {
			dialog.initDialog();
		}
		btn_withdrawals.setEnabled(bol);
		btn_withdrawals.setText(msg);
		btn_withdrawals.setTextColor(rId);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case imgwhat:
				bitmaps = (Bitmap[]) msg.obj;
				if (bitmaps != null && bitmaps.length > 0) {
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lin_top
							.getLayoutParams();
					params.height = bitmaps[0].getHeight();
					lin_top.setLayoutParams(params);
					img_aly.setImageBitmap(bitmaps[1]);
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void webLoadError() {
		setBtnState(true, "申   请", getResources().getColor(R.color.red));
	}

}
