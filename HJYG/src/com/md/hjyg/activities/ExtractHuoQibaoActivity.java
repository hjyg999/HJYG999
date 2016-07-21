package com.md.hjyg.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.HuoQibaoWithdrawInfo;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: ExtractHuoQibaoActivity date: 2015-11-10 下午4:48:57 remark: 提取活期宝界面
 * 
 * @author pyc
 */
@SuppressLint("SimpleDateFormat")
public class ExtractHuoQibaoActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {

	// TextView
	private TextView lumpsumamount, arrivetime, tv_withdrawTotal;

	// EditText
	private EditText etPassword, etamount;

	private String amount, password, statusMessage, purchasedamount;

	// Button
	private Button btn_confirmextract;

	private Notification notification = null;

	private int status;
	/** 用户可提取金额 */
	private double MemberAmount;
	/** 项目ID */
	private int huoqibaoID;
	/** 体验金金额提示框 */
	private TextView experiencegold;

	// private String MethodName_info = "SingleLoanApi/SingleLoanWithdrawInfo";
	// private String MethodName = "SingleLoanApi/SingleLoanWithdraw";
	private HuoQibaoWithdrawInfo huoQibaoWithdrawInfo;
	private TextView tv_descriptionarrive;
	/** 提现处理加载框 */
	private DialogProgressManager dialog;
	private DialogManager dialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_extracthuoqibao_activity);
		findViews();

		init();
		GetWebserviceProjectDetailsAPI();

	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "提取活期宝", this);
		dialog = new DialogProgressManager(this, "交易处理中...");
		dft.setOnDftErrorListener(this);
		dialogManager = new DialogManager(this);
		notification = new Notification();

	}

	private void findViews() {

		// TextView
		lumpsumamount = (TextView) findViewById(R.id.lumpsumamount);
		etPassword = (EditText) findViewById(R.id.etPassword);
		experiencegold = (TextView) findViewById(R.id.experiencegold);

		etamount = (EditText) findViewById(R.id.etamount);

		arrivetime = (TextView) findViewById(R.id.arrivetime);
		tv_withdrawTotal = (TextView) findViewById(R.id.tv_withdrawTotal);
		tv_descriptionarrive = (TextView) findViewById(R.id.tv_descriptionarrive);

		// Button
		btn_confirmextract = (Button) findViewById(R.id.btn_confirmextract);

		btn_confirmextract.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.btn_confirmextract:
			// 如果没有设置过交易密码，则跳转到设置交易密码界面
			if (huoQibaoWithdrawInfo.PayPwd == null
					|| huoQibaoWithdrawInfo.PayPwd.length() == 0) {
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
											ExtractHuoQibaoActivity.this,
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

			purchasedamount = etamount.getText().toString();

			try {
				if (etamount.getText().toString().equalsIgnoreCase("")
						|| (etPassword.getText().toString().equals(""))) {
					dialogManager.initOneBtnDialog("确定", "提示", "请输入提取金额和密码",
							null);

				} else if ((MemberAmount < Double.valueOf(purchasedamount))) {
					dialogManager.initOneBtnDialog("确定", "提示", "你输入的提取金额不能大于"
							+ Constants.StringToCurrency(MemberAmount + "")
							+ "元", null);

				}

				else {

					amount = etamount.getText().toString();

					password = etPassword.getText().toString();

					WithdrawHuoQibaoWebservice(password,
							Double.valueOf(amount), huoqibaoID);

				}
			} catch (Exception e) {
				e.printStackTrace();

			}

			break;

		default:
			break;
		}

	}

	public void setUIData() {
		huoqibaoID = huoQibaoWithdrawInfo.SingleId;
		// 活期宝可提现总金额
		tv_withdrawTotal.setText(Constants
				.StringToCurrency(huoQibaoWithdrawInfo.WithdrawTotal) + "元");
		// 到账说明
		tv_descriptionarrive.setText(huoQibaoWithdrawInfo.ExtractDes);
		// 总金额
		lumpsumamount.setText(huoQibaoWithdrawInfo.TotalAmount);
		// 可提取金额
		MemberAmount = Constants
				.StringToDouble(huoQibaoWithdrawInfo.MemberAmount);
		if (MemberAmount < 0) {
			MemberAmount = 0.00;
		}
		etamount.setHint("本次可提取 "
				+ Constants.StringToCurrency(MemberAmount + "") + "元");

		arrivetime.setText("预计" + getTime() + " 18:00左右资金转入您的信投宝账户");
		// 体验金
		if (huoQibaoWithdrawInfo.ExperienceAmount != null
				&& !huoQibaoWithdrawInfo.ExperienceAmount
						.equalsIgnoreCase("0.00")) {// 有则显示提示框
			experiencegold
					.setText("含体验金"
							+ Constants
									.StringToCurrency(huoQibaoWithdrawInfo.ExperienceAmount)
							+ "元");
			experiencegold.setVisibility(View.VISIBLE);
		} else {// 没有则隐藏提示框
			experiencegold.setVisibility(View.GONE);
		}

	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	/** 获取到账时间 */
	@SuppressWarnings("static-access")
	private String getTime() {
		String time = null;
		String str = "yyyy/MM/dd";
		SimpleDateFormat sdf = new SimpleDateFormat(str);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.getDate());

		if (huoQibaoWithdrawInfo.PaymentDate != 0) {
			calendar.add(calendar.DAY_OF_MONTH,
					huoQibaoWithdrawInfo.PaymentDate);
			time = sdf.format(calendar.getTime());
		} else {
			int hour = calendar.get(Calendar.HOUR_OF_DAY);// 小时
			if (hour < 14) {
				time = sdf.format(calendar.getTime());
			} else {
				calendar.add(calendar.DAY_OF_MONTH, 1);
				time = sdf.format(calendar.getTime());
			}
		}
		return time;
	}

	/** 获取界面提现相关信息 */
	public void GetWebserviceProjectDetailsAPI() {

		dft.getHuoQibaoDetails(Constants.SingleLoanWithdrawInfo_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						try {
							huoQibaoWithdrawInfo = (HuoQibaoWithdrawInfo) dft
									.GetResponseObject(response,
											HuoQibaoWithdrawInfo.class);
							if (huoQibaoWithdrawInfo.notification.ProcessResult == 1) {
								setUIData();
							} else {
								dialogManager
										.initOneBtnDialog(
												"确定",
												"提示",
												huoQibaoWithdrawInfo.notification.ProcessMessage,
												new OnDismissListener() {

													@Override
													public void onDismiss(
															DialogInterface dialog) {
														ExtractHuoQibaoActivity.this
																.finish();
														overTransition(1);

													}
												});
							}

						} catch (Exception e) {

							e.printStackTrace();
						}
					}

				});
	}

	/** 向后台发送提取活期宝请求 */
	public void WithdrawHuoQibaoWebservice(String PayPwd, double withdrawMoeny,
			int huoqibaoID) {
		setBtnState(false, "处理中", R.color.gray);
		dft.postWithdrawHuoQibao(PayPwd, withdrawMoeny, huoqibaoID,
				Constants.SingleLoanWithdraw_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						notification = (Notification) dft.GetResponseObject(
								response, Notification.class);
						status = notification.ProcessResult;
						statusMessage = notification.ProcessMessage.toString();
						setBtnState(true, "确认提取", R.drawable.btn_selector_red);

						if (status == 1) {
							etamount.setText("");
							etPassword.setText("");
							btn_confirmextract.setEnabled(true);
							dialogManager.initOneBtnDialog("确定", "提取成功",
									statusMessage, new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											ExtractHuoQibaoActivity.this
													.finish();
											overTransition(1);

										}
									});

						} else {
							dialogManager.initOneBtnDialog("确定", "提取失败",
									statusMessage, null);
						}
					}

				});
	}

	@Override
	protected void onRestart() {

		super.onRestart();
		GetWebserviceProjectDetailsAPI();
	}

	/***
	 * 设置按钮状态，已经加载框
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
		btn_confirmextract.setEnabled(bol);
		btn_confirmextract.setText(msg);
		btn_confirmextract.setBackgroundResource(rId);
	}

	@Override
	public void webLoadError() {
		setBtnState(true, "确认提取", R.drawable.btn_selector_red);
	}

}
