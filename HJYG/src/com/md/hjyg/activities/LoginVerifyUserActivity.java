package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.Notifications;
import com.md.hjyg.entity.VerificationRspChangePass;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.LoginBotomImgViewControler;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: LoginVerifyUserActivity date: 2016-7-6 下午4:08:11 remark:
 * 登录校验用户是否更换设备登录
 * 
 * @author rw
 */
public class LoginVerifyUserActivity extends BaseActivity implements
		OnClickListener {

	private TextView verify_phoneNum, verify_sm, btn_getcode, btn_verify;
	private EditText ed_verify_the_code;
	LoginBotomImgViewControler controler;
	private Intent intent;
	private String sendPhoneCodeType = "AppIMEI号验证", SendSmsType = "普通短信",
			mobileNo, YZM = "", userName = "", code;
	private VerificationRspChangePass verificationCodeResp;
	private Notifications notification;
	private DialogManager dialog;
	private boolean isCountdown;
	private int WaitOfSecond;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loginverifyuser);
		findViewById();
		init();
		// 发送短信验证码
		callPhoneCode(sendPhoneCodeType, SendSmsType, mobileNo, YZM, userName);
	}

	private void findViewById() {
		HeaderViewControler.setHeaderView(this, "输入验证码", this);
		controler = new LoginBotomImgViewControler(this, this);
		dialog = new DialogManager(this);
		// 电话号码，提示文字，获取验证码，确定
		verify_phoneNum = (TextView) findViewById(R.id.verify_phoneNum);
		verify_sm = (TextView) findViewById(R.id.verify_sm);
		btn_getcode = (TextView) findViewById(R.id.btn_getcode);
		btn_verify = (TextView) findViewById(R.id.btn_verify);
		// 　验证码输入框
		ed_verify_the_code = (EditText) findViewById(R.id.ed_verify_the_code);

		// 设置事件
		btn_getcode.setOnClickListener(this);
		btn_verify.setOnClickListener(this);
		btn_getcode.setEnabled(false);

	}

	private void init() {
		intent = getIntent();
		mobileNo = intent.getStringExtra("mobileNo");
		verify_phoneNum.setText(changeMobileNo(mobileNo));
	}

	/**
	 * 把手机号传为***-****-****
	 */
	private String changeMobileNo(String mobileNo) {
		if (mobileNo == null || mobileNo.length() != 11) {
			return mobileNo;
		}
		String[] strs = new String[3];
		strs[0] = mobileNo.substring(0, 3);
		strs[1] = mobileNo.substring(3, 7);
		strs[2] = mobileNo.substring(7, 11);
		return strs[0] + "-" + strs[1] + "-" + strs[2];
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ed_verify_the_code:// 输入框

			break;
		case R.id.btn_getcode:// 获取验证码
			callPhoneCode(sendPhoneCodeType, SendSmsType, mobileNo, YZM, userName);
			break;
		case R.id.btn_verify:// 确定
			// 修改用户对应的设备编号
			code = ed_verify_the_code.getText().toString().trim();
			updateImei(code, mobileNo);
			break;
		case HeaderViewControler.ID: // 返回
			this.finish();
			overTransition(1);
			break;
		case LoginBotomImgViewControler.ID: // 跳转到太平洋保险
			startActivity(new Intent(this, TaiPingYangCPICActivity.class));
			overTransition(2);
			break;
		default:
			break;
		}
	}

	// 发送短信验证码接口
	private void callPhoneCode(String sendPhoneCodeType, String SendSmsType,
			String mobileNo, String YZM, String userName) {
		dft.getVerificactionCode(sendPhoneCodeType, SendSmsType, mobileNo, YZM,
				userName, Constants.CallPhoneCode_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						verificationCodeResp = (VerificationRspChangePass) dft
								.GetResponseObject(response,
										VerificationRspChangePass.class);
						if (verificationCodeResp.IsValidateMobile) {
							WaitOfSecond = verificationCodeResp.WaitOfSecond;
							Countdown();
							dialog.initOneBtnDialog(verificationCodeResp.Message, null);
							verify_sm.setText("已发送验证码到上面这个手机");
							
						}else {
							dialog.initOneBtnDialog(false, verificationCodeResp.Message, null);
							verify_sm.setText("验证码发送失败");
							btn_getcode.setEnabled(true);
							btn_getcode.setText("重新发送");
						}

					}

				}, null);
	}

	// 修改用户的设备编号接口
	private void updateImei(String code, String mobileNo) {
		dft.updateImeiCode(code, mobileNo, Constants.UpdateImeiCode_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						notification = (Notifications) dft.GetResponseObject(
								response, Notifications.class);
						if (notification.ProcessResult == 1) {
							dialog.initOneBtnDialog(true, notification.ProcessMessage, new OnDismissListener() {
								
								@Override
								public void onDismiss(DialogInterface dialog) {
									onBackPressed();
								}
							});

						} else {
							dialog.initOneBtnDialog(false, notification.ProcessMessage, null);
						}
					}
				}, null);
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	/**
	 * 倒计时
	 */
	private void Countdown() {
		isCountdown = true;
		btn_getcode.setEnabled(false);
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isCountdown) {

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					WaitOfSecond--;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (WaitOfSecond > 0) {
								btn_getcode.setTextColor(getResources()
										.getColor(R.color.gray));
								btn_getcode.setText(TextUtil.getRedString(
										WaitOfSecond + "秒后重发", 0,
										(WaitOfSecond + "").length()));
							} else {
								isCountdown = false;
								WaitOfSecond = 0;
								btn_getcode.setTextColor(getResources()
										.getColor(R.color.red));
								btn_getcode.setEnabled(true);
								btn_getcode.setText("重新发送");
							}
						}
					});
				}

			}
		}).start();

	}

	@Override
	protected void onDestroy() {
		isCountdown = false;
		super.onDestroy();
	}
}
