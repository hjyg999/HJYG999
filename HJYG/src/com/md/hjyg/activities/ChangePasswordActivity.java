package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ChangePwd;
import com.md.hjyg.entity.VerificationRspChangePass;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.PasswordHitView;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.validators.EmptyValidator;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 修改登录密码
 */
public class ChangePasswordActivity extends BaseActivity implements
		View.OnClickListener {

	String password;
	int passwordlenght;
//	TextView tv_week, tv_medium_password, tv_high_password;
	private PasswordHitView passwordHitView;
    //
	TextView tv_forgot_pass, tv_account_information, tv_title,
			tv_realtime_registration, tv_login_password, tv_bank_account_mgmt,
			tv_mobile_authentication;
	EditText ed_old_password, ed_new_password, ed_confirm_password,
			ed_id_number;
	LinearLayout lay_back_investment;
	Button btn_sign_success, btn_change_pass_submit, btn_getverificationcode;
	// LayoutEntities
	HeaderControler header;

	// dataservices
	VerificationRspChangePass verificationCodeResp;
	ChangePwd changePwd;

	String verificationCode;
	private int countTime = 90;
	private boolean isover = false;

//	public String CallPhoneCode = "CommonComponents/CallPhoneCode";
//	public String ChangePwdurl = "MemberApi/ChangePwd";
//	public String SetPwd = "MemberApi/SetPwd";
//	public String SetPwdToChange = "MemberApi/SetPwdToChange";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_change_password);
		findViews();
		init();

	}

	private void init() {
		header = new HeaderControler(this, true, false, "更改登录密码",
				Constants.CheckAuthtoken(getBaseContext()));
		changePwd = new ChangePwd();
	}

	private void findViews() {
		// EditText
		ed_old_password = (EditText) findViewById(R.id.ed_old_password);
		ed_new_password = (EditText) findViewById(R.id.ed_new_password);
		ed_confirm_password = (EditText) findViewById(R.id.ed_confirm_password);
		ed_id_number = (EditText) findViewById(R.id.ed_id_number);
		// TextView
		tv_title = (TextView) findViewById(R.id.tv_title);
//		tv_high_password = (TextView) findViewById(R.id.tv_high_password);
//		tv_week = (TextView) findViewById(R.id.tv_week);
//		tv_medium_password = (TextView) findViewById(R.id.tv_medium_password);
		passwordHitView = (PasswordHitView) findViewById(R.id.passwordHitView);
		// Linear Layout
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		// Button
		btn_change_pass_submit = (Button) findViewById(R.id.btn_change_pass_submit);
		btn_getverificationcode = (Button) findViewById(R.id.btn_getverificationcode);

		// Please Enter Old password
		addvalidator(new EmptyValidator(this,ed_old_password, "请输入旧密码"));
		// Please Enter New password
		addvalidator(new EmptyValidator(this,ed_new_password, "请输入新密码"));
		// Please Enter Confirm password
		addvalidator(new EmptyValidator(this,ed_confirm_password, "请输入确认密码"));
		// Please Enter Mobile Code"
		addvalidator(new EmptyValidator(this,ed_id_number, "请输入手机密码“"));

		// ed_new_password.addTextChangedListener(new
		// GenericTextWatcher(ed_new_password));

		ed_new_password
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {

						if (actionId == EditorInfo.IME_ACTION_NEXT) {
							// check password

							// if(ed_new_password.getText().toString().length()
							// <= 6)
							if (Constants.isValidPassword(
									ChangePasswordActivity.this,
									ed_new_password.getText().toString())) {
								ed_confirm_password.requestFocus();
							}
							return true;
						}

						return false;
					}

				});
		ed_confirm_password
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						Log.e("Change Password", "onfocus");
						if (hasFocus) {
							if (!Constants.isValidPassword(
									ChangePasswordActivity.this,
									ed_new_password.getText().toString())) {

								ed_new_password.requestFocus();

							}
						}
					}
				});

		lay_back_investment.setOnClickListener(this);
		btn_change_pass_submit.setOnClickListener(this);
		btn_getverificationcode.setOnClickListener(this);

		ed_new_password.addTextChangedListener(new GenericTextWatcher(
				ed_new_password));
	}

	@Override
	public void onClick(View v) {
		if (v == lay_back_investment) {
			// Intent back = new Intent(ChangePasswordActivity.this,
			// AccountInformationActivity.class);
			// startActivity(back);
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in,
					R.anim.trans_right_out);
		} else if (v == btn_change_pass_submit) {
			if (validateForm()) {
				if (Constants.CheckConfirmedpassword(ed_new_password.getText()
						.toString(), ed_confirm_password.getText().toString())) {
					if (ed_new_password.getText().toString().length() < 6) {
						Toast.makeText(this, "请最少输入6位密码", Toast.LENGTH_LONG).show();
					} else {

						// call ChangePwd
						ChangePwd(ed_old_password.getText().toString(),
								ed_confirm_password.getText().toString(),
								ed_id_number.getText().toString());
					}
				} else {
					Constants.showOkPopup(ChangePasswordActivity.this,
							"两次密码输入不一样,请确认");
					// Constants.showOkPopup(ChangePasswordActivity.this,
					// "两次密码输入不一样,请确认",
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface dialog,
					// int which) {
					// dialog.dismiss();
					// }
					// });
				}
			}

		}

		else if (v == btn_getverificationcode) {
			String encrpted_password = Constants.md5(ed_old_password.getText()
					.toString());

			Log.e("old password",
					"password---"
							+ encrpted_password
							+ "saved password---"
							+ (Constants.GetEncryptedPassword(getBaseContext())));
			if (!ed_old_password.getText().toString().equalsIgnoreCase("")) {
				if (Constants.GetEncryptedPassword(getBaseContext())
						.equalsIgnoreCase(encrpted_password)) {
					getVerificationCode();
				} else {
					// old pasword is not correct---原密码错误，请重新核实。
					Constants.showOkPopup(ChangePasswordActivity.this,
							"原密码错误，请重新核实。");
					// Constants.showOkPopup(ChangePasswordActivity.this,
					// "原密码错误，请重新核实。",
					// new DialogInterface.OnClickListener() {
					//
					// @Override
					// public void onClick(DialogInterface dialog,
					// int which) {
					// dialog.dismiss();
					// }
					// });

				}
			} else {// plz enter old password
				Constants.showOkPopup(ChangePasswordActivity.this, "请输入旧密码");
				// Constants.showOkPopup(ChangePasswordActivity.this, "请输入旧密码",
				// new DialogInterface.OnClickListener() {
				//
				// @Override
				// public void onClick(DialogInterface dialog,
				// int which) {
				// dialog.dismiss();
				// }
				// });
			}
		}
	}

	// Get code from ChangePwd ws
	public void ChangePwd(String oldpwd, String NewPwd, String code) {
		// {"enPwd":"abhijit@123","enConfirmPwd":"abhijit@123","code":"3223"}
		// check old pwd and new password 1st in chaneg password WS
		dft.ChangePwd(oldpwd, NewPwd, code, Constants.ChangePwd_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						changePwd = (ChangePwd) dft.GetResponseObject(
								jsonObject, ChangePwd.class);
						if (changePwd.Notification.ProcessResult == 1) {
							// if(changePwd.Code != null) {
							// Get code and call SetPwd
							SetPwd(changePwd.Code);
						} else if (changePwd.Notification.ProcessResult == 0) {
							Constants.showOkPopup(ChangePasswordActivity.this,
									changePwd.Notification.ProcessMessage);
							// Constants.showOkPopup(ChangePasswordActivity.this,
							// changePwd.Notification.ProcessMessage,
							// new DialogInterface.OnClickListener() {
							//
							// @Override
							// public void onClick(
							// DialogInterface dialog,
							// int which) {
							// dialog.dismiss();
							// }
							// });
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				});
	}

	public void SetPwd(String changePwdcode) {
		dft.SetPwd(changePwdcode, Constants.SetPwd_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {

						changePwd = (ChangePwd) dft.GetResponseObject(
								jsonObject, ChangePwd.class);

						if (changePwd.Notification.ProcessResult == 1) {

							SetPwdToChange(
									ed_new_password.getText().toString(),
									ed_confirm_password.getText().toString(),
									changePwd.Code);

						} else if (changePwd.Notification.ProcessResult == 0) {
							Constants.showOkPopup(ChangePasswordActivity.this,
									changePwd.Notification.ProcessMessage);
							// Constants.showOkPopup(ChangePasswordActivity.this,
							// changePwd.Notification.ProcessMessage,
							// new DialogInterface.OnClickListener() {
							//
							// @Override
							// public void onClick(
							// DialogInterface dialog,
							// int which) {
							// dialog.dismiss();
							//
							// }
							// });
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				});
	}

	private void SetPwdToChange(String ed_new_password,
			final String ed_confirm_password, String Setpwdcode) {

		dft.SetPwdToChange(ed_new_password, ed_confirm_password, Setpwdcode,
				Constants.SetPwdToChange_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {

						changePwd = (ChangePwd) dft.GetResponseObject(
								jsonObject, ChangePwd.class);

						if (changePwd.Notification.ProcessResult == 0) {
							Constants.showOkPopup(ChangePasswordActivity.this,
									changePwd.Notification.ProcessMessage);
							// Constants.showOkPopup(ChangePasswordActivity.this,
							// changePwd.Notification.ProcessMessage,
							// new DialogInterface.OnClickListener() {
							//
							// @Override
							// public void onClick(
							// DialogInterface dialog,
							// int which) {
							// dialog.dismiss();
							//
							// }
							// });
						} else if (changePwd.Notification.ProcessResult == 1) {
							// else if (changePwd.Code != null) {
							// SetPwd ws will throw the verification of password
							// and then call SetPwdToChange WS , for checking
							// new password and confirmed password is same or
							// not with code
							Constants.showOkPopup(ChangePasswordActivity.this,
									changePwd.Notification.ProcessMessage,
									new View.OnClickListener() {

										@Override
										public void onClick(View v) {

											// Constants.SavePassword(Constants
											// .md5(ed_confirm_password),
											// getBaseContext());
											//
											// Intent back = new Intent(
											// ChangePasswordActivity.this,
											// AccountInformationActivity.class);
											// startActivity(back);
											if (Constants.dialog != null
													&& Constants.dialog
															.isShowing()) {
												Constants.dialog.dismiss();
											}
											// 密码修改成功以后，清除当前登录信息，跳转到重新登录页面
											Constants
													.ClearSharePref(ChangePasswordActivity.this);
											Intent home = new Intent(
													ChangePasswordActivity.this,
													LoginActivity.class);
											startActivity(home);
											overridePendingTransition(
													R.anim.trans_lift_in,
													R.anim.trans_right_out);
											ChangePasswordActivity.this
													.finish();

										}
									});

						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				});
	}

	private void getVerificationCode() {
		verificationCountDown();
		dft.getVerificationCodeForPAssword(Constants.CallPhoneCode_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {

						verificationCodeResp = (VerificationRspChangePass) dft
								.GetResponseObject(jsonObject,
										VerificationRspChangePass.class);

						if (verificationCodeResp.Result == true) {
							Constants.showOkPopup(ChangePasswordActivity.this,
									verificationCodeResp.Message);
							// Constants.showOkPopup(ChangePasswordActivity.this,
							// verificationCodeResp.Message,
							// new DialogInterface.OnClickListener() {
							//
							// @Override
							// public void onClick(
							// DialogInterface dialog,
							// int which) {
							// dialog.dismiss();
							//
							// }
							// });
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError volleyError) {

					}
				});
	}
	
	/** 验证码90秒倒计时 */
	private void verificationCountDown() {
		btn_getverificationcode.setEnabled(false);
		btn_getverificationcode.setBackgroundColor(getResources().getColor(R.color.gray_line));
		btn_getverificationcode.setTextColor(getResources().getColor(R.color.gray));
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
				btn_getverificationcode.setText(TextUtil.getRedString(str, 0, (countTime+"").length()));
				countTime--;
				if (countTime == 0) {
					isover = true;
				}
				break;
			case 2:
				btn_getverificationcode.setText("获取短信验证码");
				btn_getverificationcode.setEnabled(true);
				btn_getverificationcode.setBackgroundResource(R.drawable.btn_selector_red);
				btn_getverificationcode.setTextColor(getResources().getColor(R.color.white));
				countTime = 90;
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		// Intent back = new Intent(ChangePasswordActivity.this,
		// AccountInformationActivity.class);
		// startActivity(back);
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	private class GenericTextWatcher implements TextWatcher {

//		private View view;

		private GenericTextWatcher(View view) {
//			this.view = view;
		}

		public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
//			String text = s.toString();
			password = ed_new_password.getText().toString();
			passwordlenght = ed_new_password.getText().length();
			passwordHitView.setUIbyPassword(password);

//			if (password.equals("") && password == null) { // Please Enter
//															// Password
//				Constants.showOkPopup(ChangePasswordActivity.this, "请输入密码");
//				// Constants.showOkPopup(ChangePasswordActivity.this, "请输入密码",
//				// new DialogInterface.OnClickListener() {
//				//
//				// @Override
//				// public void onClick(DialogInterface dialog,
//				// int which) {
//				//
//				// dialog.dismiss();
//				// }
//				// });
//			} else if (passwordlenght <= 6
//					&& (Constants.CheckNumber(password)
//							|| Constants.ChecklowercasePassword(password) || Constants
//								.CheckuppercasePassword(password))) {
//				tv_week.setBackgroundResource(R.color.password_strength_color);
//				tv_medium_password
//						.setBackgroundResource(R.color.password_low_strength_color);
//				tv_high_password
//						.setBackgroundResource(R.color.password_low_strength_color);
//			} else if (passwordlenght <= 10
//					&& Constants.CheckNumber(password)
//					&& (Constants.ChecklowercasePassword(password) || Constants
//							.CheckuppercasePassword(password))) {
//				tv_week.setBackgroundResource(R.color.password_low_strength_color);
//				tv_medium_password
//						.setBackgroundResource(R.color.password_strength_color);
//				tv_high_password
//						.setBackgroundResource(R.color.password_low_strength_color);
//			} else if (passwordlenght > 10
//					&& Constants.CheckNumber(password)
//					&& (Constants.ChecklowercasePassword(password) || Constants
//							.CheckuppercasePassword(password))) {
//				tv_week.setBackgroundResource(R.color.password_low_strength_color);
//				tv_medium_password
//						.setBackgroundResource(R.color.password_low_strength_color);
//				tv_high_password
//						.setBackgroundResource(R.color.password_strength_color);
//			}
		}

		public void afterTextChanged(Editable editable) {
		}
	}
}
