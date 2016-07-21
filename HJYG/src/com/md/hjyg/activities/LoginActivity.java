package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.entity.ConfigInfoModel;
import com.md.hjyg.entity.LoginEntity;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.LoginBotomImgViewControler;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.CryptLib;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
	private TextView tv_forgot_password, login_checkBox_text;
	private TextView btn_register, btn_login;
	private ImageView iv_captcha;
	private ImageView ed_name_img, ed_password_img;
	private EditText ed_name, ed_password, ed_type_the_code;
	private CheckBox login_checkBox;
	private ProgressDialog progressDialog;
	/***
	 * 验证码相关布局
	 */
	private LinearLayout lin_code;

	private CaptchaModel captchaModel;
	private LoginEntity LoginEntity;
	private Context mcontext;
	private CryptLib _crypt;
	private String key;
	private String iv;

	LoginBotomImgViewControler controler;
	private DialogManager dialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		mcontext = getBaseContext();
		findViews();
		init();
		SetupEncryption();// Encyption for password
		tv_forgot_password.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		btn_register.setText("注册");
		btn_login.setOnClickListener(this);
		iv_captcha.setOnClickListener(this);
		login_checkBox_text.setOnClickListener(this);
		ed_name.setOnFocusChangeListener(editTextFocusListener);
		ed_password.setOnFocusChangeListener(editTextFocusListener);
		ed_name_img.setOnClickListener(this);
		ed_password_img.setOnClickListener(this);

		getRegisterBtnTxet();
		getCaptchaCode();
		if (!AppController.isFristLogin) {
			lin_code.setVisibility(View.VISIBLE);
		} else {
			lin_code.setVisibility(View.GONE);
		}
	}

	public void SetupEncryption() {
		try {
			_crypt = new CryptLib();
			key = CryptLib.SHA256(Constants.encryption_key, 32);
			iv = CryptLib.generateRandomIV(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "快速登录", this);
		controler = new LoginBotomImgViewControler(this, this);
		dialogManager = new DialogManager(this);
		progressDialog = Constants.getProgressDialog(this);
		LoginEntity = new LoginEntity();

	}

	private void findViews() {
		tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
		btn_register = (TextView) findViewById(R.id.btn_register);
		btn_login = (TextView) findViewById(R.id.btn_login);
		iv_captcha = (ImageView) findViewById(R.id.iv_captcha);
		ed_password_img = (ImageView) findViewById(R.id.ed_password_img);
		ed_name_img = (ImageView) findViewById(R.id.ed_name_img);
		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_password = (EditText) findViewById(R.id.ed_password);
		ed_type_the_code = (EditText) findViewById(R.id.ed_type_the_code);

		login_checkBox_text = (TextView) findViewById(R.id.login_checkBox_text);
		login_checkBox = (CheckBox) findViewById(R.id.login_checkBox);

		// LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
		// lottery_img
		// .getLayoutParams();
		// int h = ScreenUtils.getScreenWidth(this) / 2 - 20;
		// params.height = h;
		// lottery_img.setLayoutParams(params);
		// 首次进入不需要输入验证码
		lin_code = (LinearLayout) findViewById(R.id.lin_code);
	}

	/** 焦点监听 */
	OnFocusChangeListener editTextFocusListener = new OnFocusChangeListener() {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
			case R.id.ed_name:
				if (!hasFocus) {
					ed_name_img.setVisibility(View.GONE);
				} else {
					ed_name_img.setVisibility(View.VISIBLE);
				}
				break;
			case R.id.ed_password:
				if (!hasFocus) {
					ed_password_img.setVisibility(View.GONE);
				} else {
					ed_password_img.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		}
	};

	/*
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			if (AppController.isFromHomeActivity) {
				AppController.isFromHomeActivity = false;
			} else if (AppController.isFromNewsNoticeDetailsActivity) {
				AppController.isFromNewsNoticeDetailsActivity = false;
			} else if (AppController.isFromHuoQibaoDetailsActivity) {
				AppController.isFromHuoQibaoDetailsActivity = false;
			} else {
				// Intent back = new Intent(LoginActivity.this,
				// HomeActivity.class);
				Intent back = new Intent(LoginActivity.this,
						HomeFragmentActivity.class);
				startActivity(back);
			}
			finish();
			overTransition(1);
			break;
		case R.id.tv_forgot_password:
			Intent forgot_password = new Intent(LoginActivity.this,
					ForgotPasswordActivity.class);
			// Intent forgot_password = new Intent(LoginActivity.this,
			// ForgotPassword.class);
			startActivity(forgot_password);
			overTransition(2);
			break;
		case R.id.btn_register:
			Intent buynow = new Intent(LoginActivity.this,
					RegisterActivity.class);
			startActivity(buynow);
			overTransition(2);
			break;
		case R.id.btn_login:
			doLogin();

			break;
		case R.id.login_checkBox_text:
			if (login_checkBox.isChecked()) {
				login_checkBox.setChecked(false);
			} else {
				login_checkBox.setChecked(true);

			}
			break;
		case R.id.ed_name_img:
			ed_name.setText("");

			break;
		case R.id.ed_password_img:
			ed_password.setText("");

			break;
		case LoginBotomImgViewControler.ID:
			startActivity(new Intent(this, TaiPingYangCPICActivity.class));
			overTransition(2);
			break;

		default:
			break;
		}

	}

	// 登录
	private void doLogin() {
		if (ed_name.getText().toString().trim().equalsIgnoreCase("")
				|| ed_password.getText().toString().trim().equalsIgnoreCase("")) {
			dialogManager.initOneBtnDialog("请输入用户名和密码", null);
		} else {

			if (captchaModel == null || captchaModel.Code == null
					|| captchaModel.Code.length() == 0) {
				dialogManager.initOneBtnDialog("数据加载中，请稍候", null);
				return;
			}
			String ency_pwd;
			try {
				String code = "";
				ency_pwd = _crypt.encrypt(ed_password.getText().toString(),
						key, iv);
				if (AppController.isFristLogin) {
					code = captchaModel.Code;
				} else {
					code = ed_type_the_code.getText().toString()
							.replaceAll(" ", "");
					if (code.length() == 0) {
						dialogManager.initOneBtnDialog("请输入验证码！", null);
						return;
					}
				}
				callLoginService(ed_name.getText().toString().replace(" ", ""),
						ency_pwd, code, iv);
			} catch (Exception e) {
				e.printStackTrace();
				SetupEncryption();
			}
		}
	}

	/**
	 * 向后台发送登录信息
	 * 
	 * @param Username
	 *            用户名
	 * @param EncryptedPwd
	 *            密码
	 * @param iv
	 */
	private void callLoginService(final String Username, String EncryptedPwd,
			String code, String iv) {
		dft.doLogin(Username.trim(), EncryptedPwd.trim(), code, "", iv,
				Constants.Login_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							LoginEntity = (LoginEntity) dft.GetResponseObject(
									jsonObject, LoginEntity.class);
							String msg = LoginEntity.Notification.ProcessMessage;
							if (LoginEntity.Notification.ProcessResult == 0) {
								if (AppController.isFristLogin
										&& msg.indexOf("验证码") != -1) {
									msg = "登录失败，请重新登录！";
								}
								dialogManager.initOneBtnDialog(false, msg, null);
								lin_code.setVisibility(View.VISIBLE);

								ed_name.setText("");
								ed_password.setText("");
								ed_type_the_code.setText("");
								AppController.isFristLogin = false;
								getCaptchaCode();
							} else if (LoginEntity.Notification.ProcessResult == 1) {
								if (!LoginEntity.IsLock) {
									dialogManager.initOneBtnDialog(false,
											LoginEntity.LockedMessage, null);
									return;
								}
								if (LoginEntity.IsImei){
									dialogManager.initOneBtnDialog(false,
											msg, new OnDismissListener() {
												@Override
												public void onDismiss(DialogInterface dialog) {
													Intent intent = new Intent();
													intent.putExtra("mobileNo", LoginEntity.mobileNo);
													intent.setClass(LoginActivity.this, LoginVerifyUserActivity.class);
													startActivity(intent);
													overTransition(2);
												}
											});
									return;
								}
								AppController.isFristLogin = true;
								AppController.AccountInfIsChange = true;
								Constants.SetAuthToken(LoginEntity.AuthToken,
										mcontext);
								Constants.SetIsFinancialplan(
										LoginEntity.IsFinancialplan, mcontext);
								Constants.SavePassword(Constants
										.md5(ed_password.getText().toString()),
										mcontext);
								Constants.SetProtectedMobile(Constants
										.NewreplacePhoneNumberformat(Username),
										mcontext);
								Constants.SetBirthpopState(false, mcontext);
								if (AppController.isFromHomeActivity) {
									finish();
								} else if (AppController.isFromNewsNoticeDetailsActivity) {
									finish();
									AppController.isFromNewsNoticeDetailsActivity = false;
								} else if (AppController.isFromHuoQibaoDetailsActivity) {
									finish();
								} else {
									Intent intent = new Intent(
											LoginActivity.this,
											GestureEditActivity.class);
									intent.putExtra("fromLogin", true);
									startActivity(intent);
								}
								overridePendingTransition(
										R.anim.trans_right_in,
										R.anim.trans_lift_out);
							}
						} catch (Exception e) {
							Toast.makeText(mcontext,
									getString(R.string.data_error),
									Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				}, null);
	}

	/** 获取图形验证码 */
	private void getCaptchaCode() {
		dft.getCaptcha(Constants.GetValidateCode_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						captchaModel = (CaptchaModel) dft.GetResponseObject(
								jsonObject, CaptchaModel.class);
						setCaptchaValuesToUI(captchaModel);
						Constants.SetSeesionId(captchaModel.Cookie, mcontext);
						Log.i("Cookie", captchaModel.Cookie);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						dialogManager.initOneBtnDialog(volleyError.getCause()
								.toString(), null);

					}
				});
	}

	// 转图形验证码
	public Bitmap stringToBitmap(String in) {
		byte[] bytes = Base64.decode(in, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	// 设置图形验证码
	private void setCaptchaValuesToUI(CaptchaModel captchaModel) {
		iv_captcha.setImageBitmap(stringToBitmap(captchaModel.CaptchaImg));
	}

	@Override
	public void onBackPressed() {
		if (AppController.isFromHomeActivity) {
			AppController.isFromHomeActivity = false;
		} else if (AppController.isFromNewsNoticeDetailsActivity) {
			AppController.isFromNewsNoticeDetailsActivity = false;
		} else if (AppController.isFromHuoQibaoDetailsActivity) {
			AppController.isFromHuoQibaoDetailsActivity = false;
		} else {
			// Intent back = new Intent(LoginActivity.this, HomeActivity.class);
			Intent back = new Intent(LoginActivity.this,
					HomeFragmentActivity.class);
			startActivity(back);
		}
		finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/** 获取注册按钮的文字信息 */
	private void getRegisterBtnTxet() {
		dft.getNetInfoById(Constants.getConfigInfo_URL, Request.Method.GET,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						ConfigInfoModel model = (ConfigInfoModel) dft
								.GetResponseObject(jsonObject,
										ConfigInfoModel.class);
						if (model != null && model.RegistButtonInfo != null) {
							btn_register.setText(model.RegistButtonInfo);
						}
					}
				});
	}

	@Override
	protected void onRestart() {
		getCaptchaCode();
		super.onRestart();
	}
}
