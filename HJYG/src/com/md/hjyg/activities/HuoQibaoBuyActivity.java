package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.BuyHuoQibaoUersModel;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
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
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: HuoQibaoBuyActivity date: 2015-11-10 下午3:30:54 remark: 购买活期宝界面-新版
 * 
 * @author pyc
 */
public class HuoQibaoBuyActivity extends BaseActivity implements
		OnClickListener, TextWatcher, DftErrorListener {

	// UI variables

	// TextView
	private TextView tv_availablebalance, tv_purchaseamount, experiencegold,
			tv_date_current, tv_perperson, tv_buyhit, tv_recharge;
	private String buyhit, amount;
	private String Interest;

	// Button
	private Button btn_submit;

	private Context mcontext;

	private EditText purchaseamount;

	/** 可用余额 */
	private double usableMoney;
	/** 可购买余额 */
	private double canBuyMoney;
	private BuyHuoQibaoUersModel buyHuoQibaoUersModel;
	/** 活期宝ID */
	private int huoqibaoID;
	/** 购买限额 */
	private double PurchaseLimit;
	/** 项目剩余金额 */
	// private double remainingamount;
	/** 起投金额 */
	private double StartInvestAmount;
	/** 1元日收益 */
	private double LoanInterests;
	private double LeaveExperienceAmount;
	// RemainingAmount, , rate
	private String statusMessage;
	// LoanTerm,
	private int status;

	private Notification notification = null;
	/** 是否来自于HuoQibaoDetails界面 */
	// private boolean isFromHuoQibaoDetails = false;
	/** 提交数据加载框 */
	private DialogProgressManager dialog;
	private DialogManager dialogManager;
	private LinearLayout lin_top, lin_UI;
	private ImageView img_delete;

	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_huoqibaobuy_activity);
		mcontext = getBaseContext();
		mActivity = this;
		findViews();
		init();
		getIntentData();
		postSingleLoanInfoListWebservice();
		// setUIData();

	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "购买", this);
		setViewHeight();
		dialog = new DialogProgressManager(this, "排队付款中...");
		dft.setOnDftErrorListener(this);
		dialogManager = new DialogManager(this);
		img_delete.setOnClickListener(this);
		tv_recharge.setOnClickListener(this);

		// memberId = Constants.GetMemberId(mcontext);
		// rate = Constants.rate;
		// investmentamount = Constants.investmentamount;

		purchaseamount.addTextChangedListener(this);
		purchaseamount.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus && purchaseamount.getText().length() > 0) {
					img_delete.setVisibility(View.VISIBLE);
				} else {
					img_delete.setVisibility(View.GONE);
				}
			}
		});

		// df = new DecimalFormat("0.00");
		notification = new Notification();

	}

	public void getIntentData() {
		Intent intent = getIntent();
		huoqibaoID = intent.getIntExtra("huoqibaoID", 1);
		// PurchaseLimit = intent.getDoubleExtra("PurchaseLimit", 0);
		// remainingamount = intent.getDoubleExtra("remainingamount", 0);
		// StartInvestAmount = intent.getDoubleExtra("StartInvestAmount", 0);
		// isFromHuoQibaoDetails =
		// intent.getBooleanExtra("isFromHuoQibaoDetails",
		// false);
	}

	private void findViews() {
		// TextView
		tv_availablebalance = (TextView) findViewById(R.id.tv_availablebalance);
		tv_purchaseamount = (TextView) findViewById(R.id.tv_purchaseamount);
		tv_date_current = (TextView) findViewById(R.id.tv_date_current);
		purchaseamount = (EditText) findViewById(R.id.purchaseamount);
		img_delete = (ImageView) findViewById(R.id.img_delete);
		tv_perperson = (TextView) findViewById(R.id.tv_perperson);
		tv_buyhit = (TextView) findViewById(R.id.tv_buyhit);
		tv_recharge = (TextView) findViewById(R.id.tv_recharge);
		// 体验金提示
		experiencegold = (TextView) findViewById(R.id.experiencegold);

		// Button
		btn_submit = (Button) findViewById(R.id.btn_submit_treasure);

		btn_submit.setOnClickListener(this);

		lin_top = (LinearLayout) findViewById(R.id.lin_top);
		lin_UI = (LinearLayout) findViewById(R.id.lin_UI);
		lin_UI.setVisibility(View.INVISIBLE);

	}

	/** 获取界面需要的数据 */
	public void postSingleLoanInfoListWebservice() {
		dft.postSingleLoanInfoList(huoqibaoID, Constants.AmountCanBuyInfo_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						buyHuoQibaoUersModel = (BuyHuoQibaoUersModel) dft
								.GetResponseObject(response,
										BuyHuoQibaoUersModel.class);
						if (buyHuoQibaoUersModel != null
								&& buyHuoQibaoUersModel.notification.ProcessResult == 1) {
							setUIData();
						} else {
							dialogManager
									.initOneBtnDialog(
											false,
											buyHuoQibaoUersModel.notification.ProcessMessage,
											new OnDismissListener() {

												@Override
												public void onDismiss(
														DialogInterface dialog) {
													mActivity.finish();
													overTransition(1);

												}
											});

						}

					}

				});
	}

	public void CallBuyHuoQibaoWebservice(int huoqibaoID, String amount) {

		setBtnState(false, "处理中", R.color.gray);

		dft.postBuyHuoQibao(huoqibaoID, amount, Constants.BuyHuoqiBaoInfo_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						notification = (Notification) dft.GetResponseObject(
								response, Notification.class);

						status = notification.ProcessResult;
						statusMessage = notification.ProcessMessage.toString();
						setBtnState(true, "确    定",
								getResources().getColor(R.color.red));

						if (status == 1) {
							purchaseamount.setText("");
							AppController.AccountInfIsChange = true;

							dialogManager.initOneBtnDialog(true, statusMessage,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											mActivity.finish();
											overTransition(1);

										}
									});

						} else {
							dialogManager.initOneBtnDialog(false,
									statusMessage, null);
						}
					}

				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overTransition(1);
			break;
		case R.id.img_delete:// 删除输入框里的内容
			purchaseamount.setText("");
			break;
		case R.id.btn_submit_treasure:
			amount = purchaseamount.getText().toString();
			try {
				if (amount.length() == 0) {

					dialogManager.initOneBtnDialog(false, "请输入购买金额", null);

				} else if (Double.valueOf(amount) <= 0) {
					dialogManager.initOneBtnDialog(false, "购买金额不能为0", null);
				} else if (amount.indexOf(".") != -1
						&& (amount.length() - amount.indexOf(".") > 3)) {
					dialogManager.initOneBtnDialog(false, "最小购买金额为分，请重新输入",
							null);

				} else if (StartInvestAmount > Double.valueOf(amount)
						&& canBuyMoney > StartInvestAmount) {
					dialogManager.initOneBtnDialog(false, "购买金额不能小于"
							+ StartInvestAmount + "元，请重新输入", null);

				} else if ((usableMoney < Double.valueOf(amount))) {
					dialogManager.initOneBtnDialog(false, "您的可用余额不足", null);
				}

				else if (Double.valueOf(amount) > canBuyMoney) {
					dialogManager.initOneBtnDialog(false, "您最多还能购买"
							+ canBuyMoney + "元，请重新输入", null);
				}

				else {
					// 购买活期宝
					CallBuyHuoQibaoWebservice(huoqibaoID, amount);

				}
			} catch (Exception e) {
				e.printStackTrace();

			}
			break;
		case R.id.tv_recharge:// 充值
			Intent intent = new Intent(this, RechargeActivity.class);
			intent.putExtra(RechargeActivity.FROM_BUY, true);
			startActivity(intent);
			overTransition(2);
			break;

		default:
			break;
		}

	}

	private void setUIData() {
		PurchaseLimit = buyHuoQibaoUersModel.PurchaseBuyLimt;
		StartInvestAmount = buyHuoQibaoUersModel.StartInvenstAmt;
		LoanInterests = buyHuoQibaoUersModel.LoanInterests * 100;
		// 我的可用余额
		usableMoney = Constants.StringToDouble(buyHuoQibaoUersModel.leaveMoney
				+ "");
		// "¥ "
		tv_availablebalance.setText(Constants
				.StringToCurrency(usableMoney + ""));
		// 本次可购买金额
		canBuyMoney = buyHuoQibaoUersModel.PurchaseLimt;
		// canBuyMoney = PurchaseLimit
		// - Constants.StringToDouble(buyHuoQibaoUersModel.SingleAmount
		// + "");
		// if (canBuyMoney > remainingamount) {
		// canBuyMoney = remainingamount;
		// }
		tv_purchaseamount.setText(Constants.StringToCurrency(canBuyMoney + "")
				+ "元");
		tv_perperson.setText("每人累计可购买"
				+ Constants.StringToCurrency(PurchaseLimit + "") + "元");
		purchaseamount.setHint("建议购买" + buyHuoQibaoUersModel.suggestionBuyMoney
				+ "元以上");
		String string = (DateUtil.dateAfter(buyHuoQibaoUersModel.nowDate, "1天")
				.replaceFirst("-", "/")).replace("-", "/");
		// int indx = string.indexOf("年");
		// if (indx != -1) {
		// string = string.substring(indx+1);
		// }
		tv_date_current.setText("预计" + string + "可查看收益");
		Interest = Constants.StringToCurrency(10000 * LoanInterests + "");
		buyhit = "每万元日收益" + Interest + "元";
		tv_buyhit.setText(TextUtil.getRedString(buyhit, buyhit.length()
				- Interest.length() - 1, buyhit.length() - 1));
		LeaveExperienceAmount = buyHuoQibaoUersModel.LeaveExperienceAmount;
		// 显示体验金
		if (LeaveExperienceAmount == 0) {
			experiencegold.setVisibility(View.GONE);
		} else {
			experiencegold.setVisibility(View.VISIBLE);
			experiencegold.setText("含体验金"
					+ Constants.StringToCurrency(LeaveExperienceAmount + "")
					+ "元");
		}

		lin_UI.setVisibility(View.VISIBLE);

	}

	@Override
	public void onBackPressed() {

		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		postSingleLoanInfoListWebservice();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		amount = purchaseamount.getText().toString();
		if (amount.length() == 0) {
			img_delete.setVisibility(View.GONE);
			Interest = Constants.StringToCurrency(10000 * LoanInterests + "");
			buyhit = "每万元日收益：" + Interest + "元";
			tv_buyhit.setText(TextUtil.getRedString(buyhit, buyhit.length()
					- Interest.length() - 1, buyhit.length() - 1));

		} else if (Constants.StringToDouble(amount) < Constants
				.StringToDouble(buyHuoQibaoUersModel.suggestionBuyMoney)) {
			tv_buyhit.setText("建议购买" + buyHuoQibaoUersModel.suggestionBuyMoney
					+ "元以上");
			img_delete.setVisibility(View.VISIBLE);
		} else if (Constants.StringToDouble(amount) >= Constants
				.StringToDouble(buyHuoQibaoUersModel.suggestionBuyMoney)
				&& Constants.StringToDouble(amount) <= canBuyMoney) {
			double i = Constants.StringToDouble(amount);
			Interest = Constants.StringToCurrency(i * LoanInterests + "");
			buyhit = amount + "元日收益：" + Interest + "元";
			tv_buyhit.setText(TextUtil.getRedString(buyhit, buyhit.length()
					- Interest.length() - 1, buyhit.length() - 1));
			img_delete.setVisibility(View.VISIBLE);
		} else if (Constants.StringToDouble(amount) > canBuyMoney) {
			tv_buyhit.setText("购买金额不能大于购买限额");
			img_delete.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	/***
	 * 设置按钮状态，已经加载框
	 * 
	 * @param bol
	 *            是否可以点击
	 * @param msg
	 *            按钮显示的文字
	 * @param rId
	 *            字体颜色资源ID
	 */
	private void setBtnState(boolean bol, String msg, int rId) {
		if (bol) {
			dialog.dismiss();
		} else {
			dialog.initDialog();
		}
		btn_submit.setEnabled(bol);
		btn_submit.setText(msg);
		btn_submit.setTextColor(rId);
		// btn_submit.setBackgroundResource(rId);
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
					}
				});
			}
		}).start();

	}

	@Override
	public void webLoadError() {
		setBtnState(true, "确    定", getResources().getColor(R.color.red));
	}

}
