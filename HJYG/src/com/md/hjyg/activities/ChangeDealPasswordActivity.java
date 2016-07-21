package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.OnlyIncludeNotificationModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.PasswordHitView;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.CryptLib;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.validators.EmptyValidator;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/***
 * 新增界面-修改交易密码界面
 */
public class ChangeDealPasswordActivity extends BaseActivity implements
		OnClickListener,DftErrorListener {

	/** 旧交易密码 **/
	private EditText deal_password_old;
	/** 新交易密码 **/
	private EditText deal_password_new;
	/** 确认新交易密码 **/
	private EditText deal_password_new_again;
	private TextView tv_hit;
	/** 短信验证码 **/
	// private EditText deal_passwork_verify;
	private PasswordHitView passwordHitView;
	/** 获取短信验证码按钮 **/
	// private TextView deal_passwork_verify_btn;
	/** 确认修改按钮 **/
	private TextView deal_passwork_Editbtn;
	/** 忘记交易密码，点击跳转到设置交易密码 **/
	private TextView deal_passwork_set;
	/** 输入的交易密码 **/
	String dealPassword;
	// public String method_name = "MemberApi/UpdatePayPassword";
	/**
	 * pwd:旧的交易密码 enPwd:新的交易密码 enConfirmPwd:确认交易密码
	 * */
	String pwd, enPwd, enConfirmPwd;
	OnlyIncludeNotificationModel NotificationModel;
	String mobile_no;

	/** 用于加密 */
	CryptLib _crypt;
	String key;
	String iv;
	/** 修改交易密码页面温馨提示 */
	private String ChangeDealPwdDes;
	/** 设置交易密码页面温馨提示 */
	private String ConfigeDealPwdDes;
	private Intent intent;
	private DialogManager mDialog;
	private DialogProgressManager mProgressManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_change_deal_password);

		findViewAndInit();
		SetupEncryption();
	}

	private void findViewAndInit() {

		HeaderViewControler.setHeaderView(this, "修改交易密码", this);
		mDialog = new DialogManager(this);
		mProgressManager = new DialogProgressManager(this, "数据处理中...");
		dft.setOnDftErrorListener(this);

		deal_password_old = (EditText) findViewById(R.id.deal_password_old);
		deal_password_new = (EditText) findViewById(R.id.deal_password_new);
		deal_password_new_again = (EditText) findViewById(R.id.deal_password_new_again);
		// deal_passwork_verify = (EditText)
		// findViewById(R.id.deal_passwork_verify);

		// deal_password_week = (TextView)
		// findViewById(R.id.deal_password_week);
		// deal_password_medium = (TextView)
		// findViewById(R.id.deal_password_medium);
		// deal_password_high = (TextView)
		// findViewById(R.id.deal_password_high);
		passwordHitView = (PasswordHitView) findViewById(R.id.passwordHitView);

		// deal_passwork_verify_btn = (TextView)
		// findViewById(R.id.deal_passwork_verify_btn);
		// deal_passwork_verify_btn.setOnClickListener(this);
		deal_passwork_Editbtn = (TextView) findViewById(R.id.deal_passwork_Editbtn);
		deal_passwork_Editbtn.setOnClickListener(this);

		deal_passwork_Editbtn = (TextView) findViewById(R.id.deal_passwork_Editbtn);
		deal_passwork_Editbtn.setOnClickListener(this);

		deal_passwork_set = (TextView) findViewById(R.id.deal_passwork_set);
		deal_passwork_set.setOnClickListener(this);

		// 提交时如果为空的提示信息
		addvalidator(new EmptyValidator(this, deal_password_old, "请输入旧的交易密码",mDialog));
		addvalidator(new EmptyValidator(this, deal_password_new, "请输入新的交易密码",mDialog));
		addvalidator(new EmptyValidator(this, deal_password_new_again,
				"请再次输入新的交易密码",mDialog));
		// addvalidator(new EmptyValidator(deal_passwork_verify, "请输入短信验证码"));

		intent = getIntent();
		mobile_no = intent.getStringExtra("mobile_no");
		ChangeDealPwdDes = intent.getStringExtra("ChangeDealPwdDes");
		ConfigeDealPwdDes = intent.getStringExtra("ConfigeDealPwdDes");
		tv_hit = (TextView) findViewById(R.id.tv_hit);
		tv_hit.setText(ChangeDealPwdDes);

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
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.deal_passwork_Editbtn:
			if (!validateForm()) {
				return;
			}
			enPwd = deal_password_new.getText().toString();
			enConfirmPwd = deal_password_new_again.getText().toString();
			pwd = deal_password_old.getText().toString();
			if (pwd.length() < 6 || enPwd.length() < 6
					|| enConfirmPwd.length() < 6) {
				mDialog.initOneBtnDialog(false, "请最少输入6位交易密码", null);
			} else if (!enPwd.equals(enConfirmPwd)) {
				mDialog.initOneBtnDialog(false, "两次输入的交易密码不一致", null);
			} else {
				// String ency_pwd;
				try {
					pwd = _crypt.encrypt(pwd, key, iv);
					enPwd = _crypt.encrypt(enPwd, key, iv);
					enConfirmPwd = _crypt.encrypt(enConfirmPwd, key, iv);
					mProgressManager.initDialog();
					changeDealPasswordWebservice(pwd, enPwd, enConfirmPwd, iv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case R.id.deal_passwork_set:
			intent = new Intent();
			intent.setClass(this, SetDealPasswordActivity.class);
			intent.putExtra("mobile_no", mobile_no);
			intent.putExtra("ConfigeDealPwdDes", ConfigeDealPwdDes);
			startActivity(intent);
			overTransition(2);
			this.finish();

			break;

		default:
			break;
		}
	}

	/**
	 * 修改交易密码的网络服务
	 * */
	private void changeDealPasswordWebservice(String pwd, String enPwd,
			String enConfirmPwd, String iv) {
		dft.getUpdatePayPassword(pwd, enPwd, enConfirmPwd, iv,
				Constants.UpdatePayPassword_URL, Request.Method.POST,
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
											ChangeDealPasswordActivity.this
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

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
