package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ForgotPasswordDetails;
import com.md.hjyg.entity.OnlyIncludeNotificationModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.PasswordHitView;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.CryptLib;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.validators.EmptyValidator;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

public class SetDealPasswordActivity extends BaseActivity implements
		OnClickListener,DftErrorListener {

	/** 新交易密码 **/
	private EditText deal_password_new;
	/** 确认新交易密码 **/
	private EditText deal_password_new_again;
	/** 短信验证码 **/
	private EditText deal_passwork_verify;
	// /** 密码强度低、中、高 **/
	private PasswordHitView passwordHitView;
	/** 获取短信验证码按钮 **/
	private TextView deal_passwork_verify_btn;
	/** 确认修改按钮 **/
	private TextView deal_passwork_Editbtn;

	/** 输入的交易密码 **/
	String dealPassword;

	String mobile_no;
	// public String method_name = "MemberApi/ChangePayPwdInfo";
	/**
	 * code:短信验证码 enPwd:新的交易密码 enConfirmPwd:确认交易密码
	 * */
	String code, enPwd, enConfirmPwd;
	OnlyIncludeNotificationModel NotificationModel;

	/** 用于加密 */
	CryptLib _crypt;
	String key;
	String iv;
	/** 设置交易密码页面温馨提示 */
	private String ConfigeDealPwdDes;
	private TextView tv_hit;

	private int countTime = 90;
	private boolean isover = false;
	private DialogManager mDialog;
	private DialogProgressManager mProgressManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_set_deal_password);
		findViewAndInit();
		SetupEncryption();
	}

	private void findViewAndInit() {

		HeaderViewControler.setHeaderView(this, "设置交易密码", this);
		mDialog = new DialogManager(this);
		mProgressManager = new DialogProgressManager(this, "");
		dft.setOnDftErrorListener(this);

		deal_password_new = (EditText) findViewById(R.id.deal_password_new);
		deal_password_new_again = (EditText) findViewById(R.id.deal_password_new_again);
		deal_passwork_verify = (EditText) findViewById(R.id.deal_passwork_verify);

		passwordHitView = (PasswordHitView) findViewById(R.id.passwordHitView);

		deal_passwork_verify_btn = (TextView) findViewById(R.id.deal_passwork_verify_btn);
		deal_passwork_verify_btn.setOnClickListener(this);
		deal_passwork_Editbtn = (TextView) findViewById(R.id.deal_passwork_Editbtn);
		deal_passwork_Editbtn.setOnClickListener(this);

		deal_passwork_Editbtn = (TextView) findViewById(R.id.deal_passwork_Editbtn);
		deal_passwork_Editbtn.setOnClickListener(this);

		// 提交时如果为空的提示信息
		addvalidator(new EmptyValidator(this, deal_password_new, "请输入新的交易密码",
				mDialog));
		addvalidator(new EmptyValidator(this, deal_password_new_again,
				"请再次输入新的交易密码", mDialog));
		addvalidator(new EmptyValidator(this, deal_passwork_verify, "请输入短信验证码",
				mDialog));

		mobile_no = getIntent().getStringExtra("mobile_no");
		ConfigeDealPwdDes = getIntent().getStringExtra("ConfigeDealPwdDes");
		tv_hit = (TextView) findViewById(R.id.tv_hit);
		tv_hit.setText(ConfigeDealPwdDes);

		deal_password_new.addTextChangedListener(watcher);
	}

	/** EditText输入监听 */
	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			dealPassword = deal_password_new.getText().toString();
			passwordHitView.setUIbyPassword(dealPassword);

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
	public void onClick(View v) {
		if (v.getId() == HeaderViewControler.ID) {
			this.finish();
			overTransition(1);
		} else if (v == deal_passwork_Editbtn) {
			if (!validateForm()) {
				return;
			}
			dealPassword = deal_password_new.getText().toString();
			String dealPassword_again = deal_password_new_again.getText()
					.toString();
			if (dealPassword.length() < 6) {
				mDialog.initOneBtnDialog(false, "请最少输入6位交易秘密", null);
			} else if (!dealPassword.equals(dealPassword_again)) {
				mDialog.initOneBtnDialog(false, "两次输入的交易密码不一致", null);
			} else {
				enPwd = deal_password_new.getText().toString();
				enConfirmPwd = deal_password_new_again.getText().toString();
				try {
					enPwd = _crypt.encrypt(enPwd, key, iv);
					enConfirmPwd = _crypt.encrypt(enConfirmPwd, key, iv);
					code = deal_passwork_verify.getText().toString()
							.replaceAll(" ", "");
					mProgressManager.setMsg("数据处理中...");
					mProgressManager.initDialog();
					changeDealPasswordWebservice(code, enPwd, enConfirmPwd, iv);
				} catch (Exception e) {
					e.printStackTrace();
					;

				}
			}
		} else if (v == deal_passwork_verify_btn) {
			mProgressManager.setMsg("正在获取验证码...");
			mProgressManager.initDialog();
			CallNewCallPhoneCodeWebservice("重置交易密码", "", mobile_no);
		}
	}

	/** 获取短信验证码 */
	private void CallNewCallPhoneCodeWebservice(String sendPhoneCodeType,
			String code, String mobileNo) {
		verificationCountDown();
		dft.getNewCallPhoneCode(sendPhoneCodeType, code, mobileNo, "普通短信",
				Constants.CallPhoneCode_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						mProgressManager.dismiss();
						ForgotPasswordDetails newCallPhoneCode = (ForgotPasswordDetails) dft
								.GetResponseObject(response,
										ForgotPasswordDetails.class);
						String Message = newCallPhoneCode.Message.toString();
						mDialog.initOneBtnDialog(Message, null);
					}

				}, null);
	}

	/**
	 * 重置交易密码的网络服务
	 * */
	private void changeDealPasswordWebservice(String code, String enPwd,
			String enConfirmPwd, String iv) {
		dft.getChangePayPwdInfo(code, enPwd, enConfirmPwd, iv,
				Constants.ChangePayPwdInfo_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						mProgressManager.dismiss();
						NotificationModel = (OnlyIncludeNotificationModel) dft
								.GetResponseObject(response,
										OnlyIncludeNotificationModel.class);
						String msg = NotificationModel.Notification.ProcessMessage;
						if (NotificationModel.Notification.ProcessResult == 1) {
							mDialog.initOneBtnDialog(true, msg,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											SetDealPasswordActivity.this
													.finish();
											overTransition(1);
										}
									});
						}else {
							mDialog.initOneBtnDialog(false, msg, null);
						}
					}
				}, null);
	}

	/** 初始化加密信息 */
	public void SetupEncryption() {

		try {
			_crypt = new CryptLib();
			key = CryptLib.SHA256(Constants.encryption_key, 32); // 32 bytes =
			// 256 bit
			iv = CryptLib.generateRandomIV(16); // 16 bytes = 128 bit

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** 验证码90秒倒计时 */
	private void verificationCountDown() {
		deal_passwork_verify_btn.setEnabled(false);
		deal_passwork_verify_btn.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		deal_passwork_verify_btn.setTextColor(getResources().getColor(
				R.color.gray));
		isover = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				while (!isover) {
					handler.sendEmptyMessage(1);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(2);
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		String str = "";

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				str = countTime + "秒后可重发";
				deal_passwork_verify_btn.setText(TextUtil.getRedString(str, 0,
						(countTime + "").length()));
				countTime--;
				if (countTime == 0) {
					isover = true;
				}
				break;
			case 2:
				deal_passwork_verify_btn.setText("获取短信验证码");
				deal_passwork_verify_btn.setEnabled(true);
				deal_passwork_verify_btn
						.setBackgroundResource(R.drawable.btn_selector_red);
				deal_passwork_verify_btn.setTextColor(getResources().getColor(
						R.color.white));
				countTime = 90;
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	public void webLoadError() {
		mProgressManager.dismiss();
	}

}
