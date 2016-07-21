package com.md.hjyg.activities;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.BuyNowDetails;
import com.md.hjyg.entity.InvestmentDetails;
import com.md.hjyg.entity.MemberInvestRedEnvelopeLogTypeEnum;
import com.md.hjyg.entity.RedEnvelopeModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AddRateSelectDialog;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 项目购买界面
 */
public class InvestBuyNowActivity extends BaseActivity implements
		View.OnClickListener, DftErrorListener {

	// UI variables , tv_loan_title
	private TextView tv_purchase_price, tv_bal_available, tv_rem_amt,
			tv_recharge;
	private Button btn_confirm_purchase;
	private EditText ed_purchase_amount;
	private ImageView img_delete;

	private Context mcontext;
	private InvestmentDetails InvestmentDetails;
	private BuyNowDetails BuyNowDetails;
	private LinearLayout lin_top, lin_UI;

	private String Encrypted_id, purchase_amount, loanId, status_message;
	private int status;

	// 可用余额
	private int usableMoney;
	/**
	 * 利率
	 */
	// private double LoanRate;
	/**
	 * 项目期限
	 */
	// private int LoanTerm;
	/**
	 * 100元预期收入
	 */
	private double expected_income;
	/**
	 * 剩余金额
	 */
	private int rem_amt;
	private DecimalFormat df;
	private int BiddingMin;
	private int BiddingMix;
	/** 提交数据加载框 */
	private DialogProgressManager dialog;
	private DialogManager dialogManager;
	/** 红包列表： */
	private List<RedEnvelopeModel> RedEnvelopeList;
	private AddRateSelectDialog addRateSelectDialog;
	private int InvestId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_investbuynow);
		mcontext = getBaseContext();
		getIntentData();
		findViews();
		init();

		getLoanInfo();
	}

	public void getIntentData() {
		Intent intent = getIntent();
		Encrypted_id = intent.getStringExtra("EncrytedID");
		// LoanTitle = intent.getStringExtra("LoanTitle");
	}

	private void init() {
		// AuthToken = Constants.GetResult_AuthToken(mcontext);
		HeaderViewControler.setHeaderView(this, "购买", this);
		df = new DecimalFormat("0.00");// 取两位小数
		dialog = new DialogProgressManager(this, "排队付款中...");
		dft.setOnDftErrorListener(this);
		dialogManager = new DialogManager(this);
		setViewHeight();

		btn_confirm_purchase.setOnClickListener(this);
		img_delete.setOnClickListener(this);
		tv_recharge.setOnClickListener(this);
		ed_purchase_amount.setSelection(ed_purchase_amount.getText().length());
		ed_purchase_amount.addTextChangedListener(watcher);
		ed_purchase_amount
				.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus
								&& ed_purchase_amount.getText().length() > 0) {
							img_delete.setVisibility(View.VISIBLE);
						} else {
							img_delete.setVisibility(View.GONE);
						}
					}
				});

	}

	private void findViews() {
		// textView
		tv_purchase_price = (TextView) findViewById(R.id.tv_purchase_price);
		// Button
		btn_confirm_purchase = (Button) findViewById(R.id.btn_confirm_purchase);
		// EditText
		ed_purchase_amount = (EditText) findViewById(R.id.ed_purchase_amount);
		img_delete = (ImageView) findViewById(R.id.img_delete);
		lin_top = (LinearLayout) findViewById(R.id.lin_top);
		lin_UI = (LinearLayout) findViewById(R.id.lin_UI);
		lin_UI.setVisibility(View.INVISIBLE);

		tv_bal_available = (TextView) findViewById(R.id.tv_bal_available);
		tv_rem_amt = (TextView) findViewById(R.id.tv_rem_amt);
		tv_recharge = (TextView) findViewById(R.id.tv_recharge);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			// Intent back = new Intent(this,
			// InvestmentDetailsNewActivity.class);
			// back.putExtra("EncrytedID", Encrypted_id);
			// back.putExtra("LoanTitle", LoanTitle);
			// startActivity(back);
			finish();
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);
			break;
		case R.id.img_delete:// 删除输入框里的内容
			ed_purchase_amount.setText("");
			break;
		case R.id.btn_confirm_purchase:// 购买
			// 余额不足100元，提示充值
			if (usableMoney < 100) {
				Toast.makeText(this, "余额不足100元，请充值", Toast.LENGTH_LONG).show();
				return;
			}
			purchase_amount = ed_purchase_amount.getText().toString();
			if (purchase_amount.equalsIgnoreCase("")) { // Please Enter Purchase
														// Amount
				dialogManager.initOneBtnDialog(false, "请输入购买金额", null);
			} else if (purchase_amount != null) {
				// 判断输入的金额是否大于可用余额，且大于100，并被100整除
				int amount = (int) Double.parseDouble(purchase_amount);
				if (amount > usableMoney) {
					Toast.makeText(this, "输入的金额大于可用余额，请重新输入", Toast.LENGTH_LONG)
							.show();
				} else if (amount > BiddingMix) {
					Toast.makeText(this, "此标单笔投标金额不能大于" + BiddingMix + "元",
							Toast.LENGTH_LONG).show();
				} else if ((amount < BiddingMin || amount % 100 != 0)
						&& rem_amt > BiddingMin) {
					Toast.makeText(this,
							"输入的金额必须大于" + BiddingMin + "且被100整除，请重新输入",
							Toast.LENGTH_LONG).show();
				} else {
					// 输入的金额大于投标剩余金额，是否继续投资？超过部分将自动返回到您的账户
					if (amount > rem_amt && rem_amt != 0) {
						Toast.makeText(this, "输入的金额大于项目剩余金额，请重新输入", Toast.LENGTH_LONG).show();
//						dialogManager.initOneBtnDialog("确定", "提示", "您的投资金额"
//								+ amount + "元已超过项目剩余金额" + rem_amt
//								+ "元,是否继续投资？超过部分" + (amount - rem_amt)
//								+ "元将自动返回到您的账户", null,
//								new View.OnClickListener() {
//
//									@Override
//									public void onClick(View v) {
//										dialogManager.dismiss();
//										CallBuyNowsWebservice(loanId, rem_amt
//												+ "", "", "");
//									}
//								});

					} else {

						CallBuyNowsWebservice(loanId, purchase_amount, "", "");
					}
				}
			}
			break;
		case R.id.tv_recharge:// 充值
			Intent intent = new Intent(this, RechargeActivity.class);
			intent.putExtra(RechargeActivity.FROM_BUY, true);
			startActivity(intent);
			overTransition(2);
			break;
		case AddRateSelectDialog.BTN_ID:// 弹窗确定按钮
			addRateSelectDialog
					.postAddRedEnvelopeLog(dft, dialog, InvestId,
							MemberInvestRedEnvelopeLogTypeEnum.普通标,
							dialogManager, true);
			break;

		default:
			break;
		}
	}

	/**
	 * 购买
	 * 
	 * @param loanId
	 * @param purchase_amount
	 * @param jsoncallback
	 * @param remark
	 */
	public void CallBuyNowsWebservice(String loanId, String purchase_amount,
			String jsoncallback, final String remark) {

		setBtnState(false, "处理中", R.color.gray);

		dft.getBuyNowDetails(loanId, purchase_amount, jsoncallback, remark,
				Constants.BuyInfo_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						setBtnState(true, "确    定",
								getResources().getColor(R.color.red));

						BuyNowDetails = (BuyNowDetails) dft.GetResponseObject(
								response, BuyNowDetails.class);

						status_message = BuyNowDetails.Notification.ProcessMessage
								.toString();
						status = BuyNowDetails.Notification.ProcessResult;
						if (status == 1) {
							ed_purchase_amount.setText("");
							AppController.AccountInfIsChange = true;
							if (BuyNowDetails.resultModel != null
									&& BuyNowDetails.resultModel.IsRedEnvelope) {
								InvestId = BuyNowDetails.resultModel.InvestId;
								RedEnvelopeList = BuyNowDetails.resultModel.RedEnvelopeList;
							} else {
								RedEnvelopeList = null;
							}
							// 判断是否有可用的红包加息券
							if (RedEnvelopeList != null
									&& RedEnvelopeList.size() > 0) {// 有可用的
								showSelectDialog();
								// 更新数据
								getLoanInfo();

							} else {// 没有
								dialogManager.initOneBtnDialog(true, "竞标成功!",
										new OnDismissListener() {

											@Override
											public void onDismiss(
													DialogInterface dialog) {
												// 更新数据
												// getLoanInfo();
												// 跳出此界面
												finish();
												overTransition(1);
											}
										});

							}
						} else if (status == 0) {
							dialogManager.initOneBtnDialog(false,
									status_message, new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											// 更新数据
											// getLoanInfo();
											// 跳出此界面
											finish();
											overTransition(1);
										}
									});

						} else if (status == 2) {
							dialogManager.initOneBtnDialog(false,
									status_message, new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											getLoanInfo();
										}
									});
						}

					}
				}, null);

	}

	/**
	 * 获取项目信息
	 */
	private void getLoanInfo() {
		// Log.e("TAG", "Methodname-" + MethodName + Encrypted_id);
		dft.getBuyNowInfo(Constants.InvestInfo_URL + Encrypted_id,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {

						// Log.e("Response Buy now", "-------" + jsonObject);

						InvestmentDetails = (InvestmentDetails) dft
								.GetResponseObject(jsonObject,
										InvestmentDetails.class);

						setLoanValuesToUI(InvestmentDetails);

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				});

	}

	/**
	 * 红包加息券选择框
	 */
	private void showSelectDialog() {

		addRateSelectDialog = new AddRateSelectDialog(this, RedEnvelopeList,
				this, true);
	}

	private void setLoanValuesToUI(InvestmentDetails InvestmentDetails) {
		usableMoney = (int) Double.parseDouble(InvestmentDetails.LeaveMoney
				.replace(",", ""));
		// 可用余额
		tv_bal_available.setText(InvestmentDetails.LeaveMoney);
		// 剩余余额
		tv_rem_amt.setText(InvestmentDetails.LoanDifference + "元");
		// 增加预计收入
		// LoanRate = InvestmentDetails.loanModel.LoanRate;
		// LoanTerm = InvestmentDetails.loanModel.LoanTerm;
		BiddingMin = (int) Double.parseDouble(InvestmentDetails.BiddingMin
				.replace(",", ""));
		BiddingMix = InvestmentDetails.loanModel.BiddingMax;

		String amt = InvestmentDetails.LoanDifference.substring(0,
				InvestmentDetails.LoanDifference.lastIndexOf("."));
		// 去，
		while (amt.lastIndexOf(",") != -1) {
			amt = amt.replace(",", "");
		}
		rem_amt = Integer.parseInt(amt);

		expected_income = InvestmentDetails.LoanInterest;

		// tv_purchase_price.setText(100 + "元预期收入：" + df.format(expected_income)
		// + "元");
		setPurchasePriceUI(100 + "元预期收入: ", df.format(expected_income) + "",
				"元");

		int id = InvestmentDetails.loanModel.Id;
		loanId = String.valueOf(id);

	}

	/**
	 * 对输入的金额进行监听，用来显示预期收益
	 */
	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			String amount = ed_purchase_amount.getText().toString().trim();
			if (amount.length() > 0) {
				img_delete.setVisibility(View.VISIBLE);
				if (amount.substring(0, 1).equals("0")
						|| Integer.parseInt(amount) % 100 != 0) {
					// tv_purchase_price.setText("金额必须能被100整除");
					setPurchasePriceUI("金额必须能被", "100", "整除");
				} else {
					if (Integer.parseInt(amount) > BiddingMix) {
						// tv_purchase_price.setText("此标单笔投资金额不能大于" + BiddingMix
						// + "元");
						setPurchasePriceUI("此标单笔投资金额不能大于", BiddingMix + "", "元");
					} else {

						// tv_purchase_price
						// .setText(amount
						// + "元预期收入："
						// + df.format(expected_income
						// * Integer.parseInt(amount)
						// / 100) + "元");
						setPurchasePriceUI(
								amount + "元预期收入: ",
								df.format(expected_income
										* Integer.parseInt(amount) / 100)
										+ "", "元");
					}
				}

			} else {
				// tv_purchase_price.setText(100 + "元预期收入："
				// + df.format(expected_income) + "元");
				setPurchasePriceUI(100 + "元预期收入: ", df.format(expected_income)
						+ "", "元");
				img_delete.setVisibility(View.GONE);
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	@Override
	public void onBackPressed() {
		// Intent back = new Intent(this,
		// InvestmentDetailsNewActivity.class);
		// back.putExtra("EncrytedID", Encrypted_id);
		// back.putExtra("LoanTitle", LoanTitle);
		// startActivity(back);
		finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/***
	 * 设置按钮状态，已经加载框
	 * 
	 * @param bol
	 *            是否可以点击
	 * @param msg
	 *            按钮显示的文字
	 * @param rId
	 *            文字颜色ID
	 */
	private void setBtnState(boolean bol, String msg, int rId) {
		if (bol) {
			dialog.dismiss();
		} else {
			dialog.initDialog();
		}
		btn_confirm_purchase.setEnabled(bol);
		btn_confirm_purchase.setText(msg);
		btn_confirm_purchase.setTextColor(rId);
		// btn_confirm_purchase.setBackgroundResource(rId);
	}

	/**
	 * 设置预期收益提示信息
	 * 
	 * @param str
	 *            金额前部分文字
	 * @param income
	 *            金额-红色显示
	 * @param endstr
	 *            金额后部分文字
	 */
	private void setPurchasePriceUI(String str, String income, String endstr) {
		String mincome = "";
		if (income.equals("100") || income.equals(BiddingMix + "")) {
			mincome = income;

		} else {
			mincome = Constants.StringToCurrency(income);
		}
		String mstr = str + mincome + endstr;
		tv_purchase_price.setText(TextUtil.getRelativeSizeandRedS(mstr,
				mstr.indexOf(mincome),
				mstr.indexOf(mincome) + mincome.length(), 1.2f));
	}

	/***
	 * 设置控件的高度
	 */
	private void setViewHeight() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lin_top
						.getLayoutParams();
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.buy_top_bg);
				bitmap = Save.ScaleBitmap(bitmap, mcontext);
				params.height = bitmap.getHeight();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						lin_top.setLayoutParams(params);
						lin_UI.setVisibility(View.VISIBLE);
					}
				});
			}
		}).start();

	}

	@Override
	protected void onRestart() {
		super.onRestart();
		getLoanInfo();
	}

	@Override
	public void webLoadError() {
		setBtnState(true, "确    定", getResources().getColor(R.color.red));
	}
}
