package com.md.hjyg.activities;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ForgotPasswordDetails;
import com.md.hjyg.entity.ForgotPasswordNotification;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: ForgotPasswordActivity date: 2016-5-7 上午9:54:49 remark:忘记密码界面
 * 
 * @author pyc
 */
public class ForgotPasswordActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener, TextWatcher, DftErrorListener {

	private EditText ed_mobileno, ed_login_password, ed_login_password_again,
			ed_code, haveFocusView;
	private ImageView delete_mobileno, delete_password, delete_password_ag,
			delete_code, yanjing_password, yanjing_password_ag, img_tishi;
	private TextView tv_hit, tv_code, tv_yuying, tv_btn;
	private LinearLayout lin_hit;
	private DialogManager mDialog;
	private String errmobileNo, code, mobileNo;
	private boolean isCallPhoneCode, isover, isYYCode;
	private int WaitOfSecond;
	private DialogProgressManager progressManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgotpassword_activity);

		findViewandInit();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "忘记登录密码", this);
		mDialog = new DialogManager(this);
		progressManager = new DialogProgressManager(this, "正在获取验证码...");
		dft.setOnDftErrorListener(this);
		// 编辑框
		ed_mobileno = (EditText) findViewById(R.id.ed_mobileno);
		ed_login_password = (EditText) findViewById(R.id.ed_login_password);
		ed_login_password_again = (EditText) findViewById(R.id.ed_login_password_again);
		ed_code = (EditText) findViewById(R.id.ed_code);
		ed_mobileno.setOnFocusChangeListener(this);
		ed_login_password.setOnFocusChangeListener(this);
		ed_login_password_again.setOnFocusChangeListener(this);
		ed_code.setOnFocusChangeListener(this);

		ed_mobileno.addTextChangedListener(this);
		ed_login_password.addTextChangedListener(this);
		ed_login_password_again.addTextChangedListener(this);
		ed_code.addTextChangedListener(this);

		// 删除按钮
		delete_mobileno = (ImageView) findViewById(R.id.delete_mobileno);
		delete_password = (ImageView) findViewById(R.id.delete_password);
		delete_password_ag = (ImageView) findViewById(R.id.delete_password_ag);
		delete_code = (ImageView) findViewById(R.id.delete_code);
		delete_mobileno.setOnClickListener(this);
		delete_password.setOnClickListener(this);
		delete_password_ag.setOnClickListener(this);
		delete_code.setOnClickListener(this);
		// 观察按钮
		yanjing_password = (ImageView) findViewById(R.id.yanjing_password);
		yanjing_password_ag = (ImageView) findViewById(R.id.yanjing_password_ag);
		yanjing_password.setOnClickListener(this);
		yanjing_password_ag.setOnClickListener(this);
		// 提示信息
		tv_hit = (TextView) findViewById(R.id.tv_hit);
		tv_yuying = (TextView) findViewById(R.id.tv_yuying);
		lin_hit = (LinearLayout) findViewById(R.id.lin_hit);
		img_tishi = (ImageView) findViewById(R.id.img_tishi);
		tv_yuying.setOnClickListener(this);
		// 验证码获取按钮
		tv_code = (TextView) findViewById(R.id.tv_code);
		tv_code.setOnClickListener(this);
		// 确定按钮
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(this);

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.ed_mobileno:
			setEditTextDeleteBtbMsg(hasFocus, delete_mobileno, ed_mobileno);
			if (hasFocus) {
				haveFocusView = ed_mobileno;
				setHitTextUI();
			}
			break;
		case R.id.ed_login_password:
			setEditTextDeleteBtbMsg(hasFocus, delete_password,
					ed_login_password);
			if (!hasFocus
					&& ed_login_password_again.getText().toString().trim()
							.length() >= 6) {
				setEditTextDeleteBtbMsg(false, delete_password_ag,
						ed_login_password_again);
			}
			if (hasFocus) {
				haveFocusView = ed_login_password;
				setHitTextUI();
			}
			break;
		case R.id.ed_login_password_again:
			setEditTextDeleteBtbMsg(hasFocus, delete_password_ag,
					ed_login_password_again);
			if (hasFocus) {
				haveFocusView = ed_login_password_again;
				setHitTextUI();
			}
			break;
		case R.id.ed_code:
			setEditTextDeleteBtbMsg(hasFocus, delete_code, ed_code);
			if (hasFocus) {
				haveFocusView = ed_code;
				setHitTextUI();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.delete_mobileno:
			ed_mobileno.setText("");
			break;
		case R.id.delete_password:
			ed_login_password.setText("");
			break;
		case R.id.delete_password_ag:
			ed_login_password_again.setText("");
			break;
		case R.id.delete_code:
			ed_code.setText("");
			break;
		case R.id.yanjing_password:
			setInputTypeandImg(ed_login_password, yanjing_password);
			break;
		case R.id.yanjing_password_ag:
			setInputTypeandImg(ed_login_password_again, yanjing_password_ag);
			break;
		case R.id.tv_code:// 获取验证码
			isYYCode = false;
			mobileNo = ed_mobileno.getText().toString().trim();
			if (verificationInf()) {
				progressManager.initDialog();
				callWebserviceValidateNameByMobile(mobileNo);
			}
			break;
		case R.id.tv_yuying:// 获取语音验证码
			// 调用语言验证码的接口
			String SendSmsType = "语音短信";
			String sendPhoneCodeType = "密码找回";
			isYYCode = true;
			progressManager.dismiss();
			progressManager.setMsg("");
			progressManager.initDialog();
			callWebserviceNewCallPhoneCode(sendPhoneCodeType, code, mobileNo,
					SendSmsType);
			break;
		case R.id.tv_btn:
			if (verificationInf()) {
				String verificationCode = ed_code.getText().toString().trim();
				if (verificationCode.length() >= 4) {
					progressManager.setMsg("数据处理中...");
					progressManager.initDialog();
					callWebserviceForgotPass(ed_login_password.getText()
							.toString().trim(), ed_login_password_again
							.getText().toString().trim(), verificationCode,
							code, mobileNo);
				} else {
					mDialog.initOneBtnDialog(false,"请输入正确的验证码！", null);
				}
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 验证输入信息
	 */
	private boolean verificationInf() {
		String mobileNo = ed_mobileno.getText().toString().trim();
		String passWord = ed_login_password.getText().toString().trim();
		String passWord_ag = ed_login_password_again.getText().toString()
				.trim();
		if (!(mobileNo.length() == 11 && Constants
				.checkStartMobileNumber(mobileNo))) {
			mDialog.initOneBtnDialog(false,"请输入正确的手机号码！", null);
			return false;
		} else if (!(passWord.length() >= 6 && (Constants.CheckNumber(passWord)
				|| Constants.ChecklowercasePassword(passWord) || Constants
					.CheckuppercasePassword(passWord)))) {
			mDialog.initOneBtnDialog(false,"请输入6-30位密码", null);
			return false;
		} else if (!(passWord_ag.length() >= 6 && passWord_ag.equals(passWord))) {
			mDialog.initOneBtnDialog(false,"两次输入的密码不一致", null);
			return false;
		}
		return true;
	}

	/**
	 * 设置编辑框的输入样式和按钮的状态
	 * 
	 * @param editText
	 * @param imageView
	 */
	private void setInputTypeandImg(EditText editText, ImageView imageView) {
		Object iv = imageView.getTag();
		if (iv != null && (Integer) iv == R.drawable.yanjing_red) {
//			editText.setInputType(EditorInfo.TYPE_CLASS_TEXT
//					| EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
			imageView.setImageResource(R.drawable.yanjing_gray);
			imageView.setTag(R.drawable.yanjing_gray);
					
            //设置EditText文本为隐藏的
			editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		} else {
			editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//			editText.setInputType(EditorInfo.TYPE_CLASS_TEXT
//					| EditorInfo.TYPE_TEXT_VARIATION_NORMAL);
			imageView.setImageResource(R.drawable.yanjing_red);
			imageView.setTag(R.drawable.yanjing_red);
		}
	}

	/**
	 * 设置编辑框删除按钮的显示状态
	 * 
	 * @param hasFocus
	 *            是否获得焦点
	 * @param img
	 * @param editText
	 */
	private void setEditTextDeleteBtbMsg(boolean hasFocus, ImageView img,
			EditText editText) {
		if (hasFocus) {
			img.setImageResource(R.drawable.delete);
			img.setEnabled(true);
			img.setVisibility(View.VISIBLE);
		} else {
			String str = editText.getText().toString().trim();
			int slong = str.length();
			if (editText == ed_mobileno) {
				if (slong == 11
						&& Constants.checkStartMobileNumber(str)
						&& (errmobileNo == null || (errmobileNo != null && !errmobileNo
								.equals(str)))) {
					img.setEnabled(false);
					img.setImageResource(R.drawable.tishzq_36x36);
					img.setVisibility(View.VISIBLE);
				} else if (slong == 0) {
					img.setVisibility(View.GONE);
				} else {
					img.setEnabled(false);
					img.setImageResource(R.drawable.tishcw_36x36);
					img.setVisibility(View.VISIBLE);
				}
			} else if (editText == ed_login_password) {
				if (slong >= 6
						&& (Constants.CheckNumber(str)
								|| Constants.ChecklowercasePassword(str) || Constants
									.CheckuppercasePassword(str))) {
					img.setEnabled(false);
					img.setImageResource(R.drawable.tishzq_36x36);
					img.setVisibility(View.VISIBLE);
				} else if (slong == 0) {
					img.setVisibility(View.GONE);
				} else {
					img.setEnabled(false);
					img.setImageResource(R.drawable.tishcw_36x36);
					img.setVisibility(View.VISIBLE);
				}
			} else if (editText == ed_login_password_again) {
				if (slong >= 6
						&& str.equals(ed_login_password.getText().toString()
								.trim())) {
					img.setEnabled(false);
					img.setImageResource(R.drawable.tishzq_36x36);
					img.setVisibility(View.VISIBLE);
				} else if (slong == 0) {
					img.setVisibility(View.GONE);
				} else {
					img.setEnabled(false);
					img.setImageResource(R.drawable.tishcw_36x36);
					img.setVisibility(View.VISIBLE);
				}
			} else {
				img.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		setHitTextUI();
	}

	/**
	 * 设置输入框文字提示信息
	 */
	private void setHitTextUI() {
		lin_hit.setVisibility(View.VISIBLE);
		String str = haveFocusView.getText().toString().trim();
		int sLong = str.length();
		if (haveFocusView == ed_mobileno) {
			if (sLong == 11 && Constants.checkStartMobileNumber(str)) {
				if (errmobileNo != null && errmobileNo.equals(str)) {
					setHitTextUI(R.drawable.tishcw_28x28, "此号码不存在", R.color.red);
				} else {
					setHitTextUI(R.drawable.tishzq_28x28, "手机号格式输入正确",
							R.color.green_99CC33);
				}
			} else {
				setHitTextUI(R.drawable.tishcw_28x28, "请输入11位手机号", R.color.red);

			}

		} else if (haveFocusView == ed_login_password) {// 输入密码获的焦点
			if (sLong >= 6
					&& (Constants.CheckNumber(str)
							|| Constants.ChecklowercasePassword(str) || Constants
								.CheckuppercasePassword(str))) {
				setHitTextUI(R.drawable.tishzq_28x28, "密码格式输入正确",
						R.color.green_99CC33);
			} else {
				setHitTextUI(R.drawable.tishcw_28x28, "请输入6-30位密码", R.color.red);
			}

		} else if (haveFocusView == ed_login_password_again) {// 再次输入密码获的焦点
			if (sLong >= 6
					&& str.equals(ed_login_password.getText().toString().trim())) {
				setHitTextUI(R.drawable.tishzq_28x28, "两次输入的密码一致",
						R.color.green_99CC33);
			} else {
				setHitTextUI(R.drawable.tishcw_28x28, "两次输入的密码不一致", R.color.red);
			}

		} else {
			if (isCallPhoneCode) {
				setHitTextUIYY();
			} else {
				lin_hit.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * 设置输入框文字提示信息
	 * 
	 * @param imgID
	 * @param str
	 * @param colorID
	 */
	private void setHitTextUI(int imgID, CharSequence str, int colorID) {
		tv_yuying.setVisibility(View.GONE);
		img_tishi.setImageResource(imgID);
		tv_hit.setText(str);
		tv_hit.setTextColor(getResources().getColor(colorID));
	}

	/**
	 * 设置语音验证码提示
	 */
	private void setHitTextUIYY() {
		if (!isYYCode) {
			lin_hit.setVisibility(View.VISIBLE);
			tv_yuying.setVisibility(View.VISIBLE);
			img_tishi.setImageResource(R.drawable.tishi_bule_28x28);
			tv_hit.setTextColor(getResources().getColor(R.color.gray));
			tv_hit.setText("收不到？");
		} else {
			img_tishi.setImageResource(R.drawable.tishi_bule_28x28);
			tv_hit.setTextColor(getResources().getColor(R.color.gray));
			tv_hit.setText("请注意接听4000-781-901的来电，我们将在电话中告知动态验证码！");
			lin_hit.setVisibility(View.VISIBLE);
			tv_yuying.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取验证码之前验证手机号码
	 * 
	 * @param mobileNo
	 */
	private void callWebserviceValidateNameByMobile(final String mobileNo) {
		// verificationCountDown();
		dft.getVerifyNameByMobileNo(mobileNo,
				Constants.ForgotPwdValidateNameByMobile_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						Log.d("vivi", "修改密码手机号验证信息-->" + response.toString());
						ForgotPasswordDetails forgot_pass_details = (ForgotPasswordDetails) dft
								.GetResponseObject(response,
										ForgotPasswordDetails.class);
						code = forgot_pass_details.Code.toString();
						Constants.Code = code;
						if (forgot_pass_details.Notification.ProcessResult == 1) {
							String sendPhoneCodeType = "密码找回";
							String SendSmsType = "普通短信";
							// startTimer();
							callWebserviceNewCallPhoneCode(sendPhoneCodeType,
									code, mobileNo, SendSmsType);
						} else {
							progressManager.dismiss();
							// status == 0 的时候，返回错误信息
							mDialog.initOneBtnDialog(false,
									forgot_pass_details.Notification.ProcessMessage,
									null);
							errmobileNo = mobileNo;
							delete_mobileno
									.setImageResource(R.drawable.tishcw_36x36);
						}
					}

				}, null);
	}

	/**
	 * 获取短信验证码
	 * 
	 * @param sendPhoneCodeType
	 * @param code
	 * @param mobileNo
	 */
	private void callWebserviceNewCallPhoneCode(String sendPhoneCodeType,
			String code, String mobileNo, String SendSmsType) {

		dft.getCallPhoneCode(sendPhoneCodeType, code, mobileNo, SendSmsType,
				Constants.NewCallPhoneCode_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						// Log.d("vivi", "修改密码短信验证码返回信息-->" +
						// response.toString());
						progressManager.dismiss();

						ForgotPasswordDetails forgot_pass_details = (ForgotPasswordDetails) dft
								.GetResponseObject(response,
										ForgotPasswordDetails.class);
						String Message = forgot_pass_details.Message.toString();
						mDialog.initOneBtnDialog(Message, null);
						if (forgot_pass_details.Result) {
							WaitOfSecond = forgot_pass_details.WaitOfSecond;
							verificationCountDown();
							ed_code.setFocusable(true);
							ed_code.setFocusableInTouchMode(true);
							ed_code.requestFocus();
							startTimer();
						} else if (isYYCode
								&& forgot_pass_details.IsValidateMobile) {
							setHitTextUIYY();
						} else if (forgot_pass_details.IsValidateMobile) {
							WaitOfSecond = forgot_pass_details.WaitOfSecond;
							verificationCountDown();
							ed_code.setFocusable(true);
							ed_code.setFocusableInTouchMode(true);
							ed_code.requestFocus();
							isCallPhoneCode = true;
							setHitTextUIYY();
						}
					}

				}, null);
	}

	/** 5秒定时器，语言验证码点击按钮5秒后显示 */
	private void startTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						isCallPhoneCode = true;
						setHitTextUIYY();
					}
				});
			}
		}, 5000);
	}

	/** 验证码90秒倒计时 */
	private void verificationCountDown() {
		tv_code.setEnabled(false);
		tv_code.setTextColor(getResources().getColor(R.color.gray_q));
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
				str = WaitOfSecond + "秒后重发";
				tv_code.setText(TextUtil.getRedString(str, 0,
						(WaitOfSecond + "").length()));
				WaitOfSecond--;
				if (WaitOfSecond == 0) {
					isover = true;
				}
				break;
			case 2:
				tv_code.setText("获取短信验证码");
				tv_code.setEnabled(true);
				tv_code.setTextColor(getResources().getColor(R.color.red));
				lin_hit.setVisibility(View.INVISIBLE);
				isYYCode = false;
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 通过手机验证码修改密码接口
	 * 
	 * @param new_pass
	 * @param confirm_pass
	 * @param verificationCode
	 * @param code
	 * @param mobileNo
	 */
	private void callWebserviceForgotPass(String new_pass, String confirm_pass,
			String verificationCode, String code, String mobileNo) {
		dft.getForgotPass(new_pass, confirm_pass, verificationCode, code,
				Constants.ForgotPwdByMobile_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						progressManager.dismiss();
						ForgotPasswordNotification notifiaction = (ForgotPasswordNotification) dft
								.GetResponseObject(response,
										ForgotPasswordNotification.class);
						int status = notifiaction.ProcessResult;
						String statusMessage = notifiaction.ProcessMessage
								.toString();
						if (status == 1) {
							mDialog.initOneBtnDialog(true,statusMessage,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											Constants.ClearSharePref(ForgotPasswordActivity.this);
											Constants.Clear_Cookie(ForgotPasswordActivity.this);
											Intent i = new Intent(
													ForgotPasswordActivity.this,
													LoginActivity.class);
											startActivity(i);
											overTransition(1);
											finish();
										}
									});
						} else {
							mDialog.initOneBtnDialog(false,statusMessage, null);
						}
					}

				}, null);

	}

	@Override
	protected void onDestroy() {
		isover = true;
		super.onDestroy();
	}

	@Override
	public void webLoadError() {
		progressManager.dismiss();
	};

}
