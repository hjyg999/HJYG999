package com.md.hjyg.activities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.entity.FeedbackDetails;
import com.md.hjyg.entity.ForgotPasswordDetails;
import com.md.hjyg.entity.MeetListDialogModel;
import com.md.hjyg.entity.RecommendNameModel;
import com.md.hjyg.entity.RegisterDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.KeyBoardView;
import com.md.hjyg.layoutEntities.LoginBotomImgViewControler;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.ListDialogManager;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * 注册第二步--实名认证
 */
public class VerifyRealNameActivity extends BaseActivity implements
		OnFocusChangeListener, OnClickListener {

	/** 真实姓名 */
	private EditText ed_verify_real_name;
	/** 证件类型 */
	private TextView spinner_id_card;
	private String[] type_name = { "身份证", "回乡证", "台胞证" };
	/** 证件号 */
	private EditText ed_id_number;
	/** 注册提交 */
	private Button btn_submit;

	private String selected_card_type = "0", password, mobile_no, realName,
			id_number;
	// private CheckBox checkBox0, checkBox1, checkBox2;
	// private ImageView close_dialog;
	// private RelativeLayout rel_type0, rel_type1, rel_type2;
	/** 证件类型dialog */
	// private Dialog dialog;
	private CaptchaModel captchaModel;
	/** 图片验证码 */
	private ImageView iv_captcha;
	/** 获取短信验证码按钮 */
	private TextView btn_verification;
	/**
	 * 图片验证码
	 */
	private EditText ed_type_the_code;
	/** 输入的图片验证码 */
	private String captchaCode;
	/** 语言验证码点击按钮 */
	private LinearLayout voice_verification;
	private String SendPhoneCodeType = "注册会员", SendSmsType = "普通短信",
			mobile_verificationCode, statusMessage, Message;
	private String recommendName, recommended_Type;
	// private String VerificationCode_Method_name =
	// "CommonComponents/CallPhoneCodeForReg";
	// private String IsExistsCode_Method_Name = "MemberApi/IsExitVCode";
	// private String getRecommendName = "MemberApi/IsShareMember";
	private ForgotPasswordDetails forgotPasswordDetails;
	private ProgressDialog progressDialog;
	private boolean result;
	private int countTime = 90;
	private boolean isover = false;
	/** 获取语言验证码后的提示信息 */
	private LinearLayout voice_verification_hint;
	/**
	 * 手机验证码
	 */
	private EditText ed_sms_verification;
	private LinearLayout recommended_linear;
	private CheckBox checkbox_recommended;
	private EditText ed_recommended_name;
	private boolean recommendIsExists = false;
	private FeedbackDetails feedbackDetails;

	private ImageView ed_verify_real_name_img, ed_id_number_img,
			ed_recommended_name_img;
	/** 提交数据加载框 */
	private DialogProgressManager mprogressDialog;

	// 键盘
	private KeyBoardView mKeyBoardView;
	private RelativeLayout ll;
	private ListDialogManager mListDialog;
	LoginBotomImgViewControler controler;
	private DialogManager dialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_verify_realname);
		findViewAndInit();
		getCaptchaCode();
		getRecommendNameDetail(mobile_no);

	}

	private void findViewAndInit() {
		progressDialog = Constants.getProgressDialog(this);
		mprogressDialog =  new DialogProgressManager(this, "努力加载中...");
		dialogManager = new DialogManager(this);
		ed_verify_real_name = (EditText) findViewById(R.id.ed_verify_real_name);
		spinner_id_card = (TextView) findViewById(R.id.spinner_id_card);
		spinner_id_card.setOnClickListener(this);
		ed_id_number = (EditText) findViewById(R.id.ed_id_number);
		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);
		HeaderViewControler.setHeaderView(this, "实名认证", this);

		controler = new LoginBotomImgViewControler(this, this);

		iv_captcha = (ImageView) findViewById(R.id.iv_captcha);
		iv_captcha.setOnClickListener(this);
		btn_verification = (TextView) findViewById(R.id.btn_verification);
		btn_verification.setOnClickListener(this);
		ed_type_the_code = (EditText) findViewById(R.id.ed_type_the_code);
		voice_verification = (LinearLayout) findViewById(R.id.voice_verification);
		voice_verification.setVisibility(View.GONE);
		voice_verification.setOnClickListener(this);
		voice_verification_hint = (LinearLayout) findViewById(R.id.voice_verification_hint);
		voice_verification_hint.setVisibility(View.GONE);
		ed_sms_verification = (EditText) findViewById(R.id.ed_sms_verification);
		ll = (RelativeLayout) findViewById(R.id.child_layout);

		// 删除
		ed_verify_real_name_img = (ImageView) findViewById(R.id.ed_verify_real_name_img);
		ed_id_number_img = (ImageView) findViewById(R.id.ed_id_number_img);
		ed_recommended_name_img = (ImageView) findViewById(R.id.ed_recommended_name_img);
		ed_verify_real_name_img.setOnClickListener(this);
		ed_id_number_img.setOnClickListener(this);
		ed_recommended_name_img.setOnClickListener(this);
		// 推荐人编辑
		recommended_linear = (LinearLayout) findViewById(R.id.recommended_linear);
		recommended_linear.setOnClickListener(this);
		checkbox_recommended = (CheckBox) findViewById(R.id.checkbox_recommended);
		ed_recommended_name = (EditText) findViewById(R.id.ed_recommended_name);
		ed_recommended_name.setVisibility(View.GONE);
		ed_recommended_name_img.setVisibility(View.GONE);
		// 焦点监听
		ed_recommended_name.setOnFocusChangeListener(this);
		ed_verify_real_name.setOnFocusChangeListener(this);
		ed_id_number.setOnFocusChangeListener(this);
		ed_id_number.setOnClickListener(this);
		setInputTypes(ed_id_number);

		checkbox_recommended
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							// 显示推荐人编辑框
							ed_recommended_name.setVisibility(View.VISIBLE);
						} else {
							// 隐藏推荐人编辑框
							ed_recommended_name.setVisibility(View.GONE);
							ed_recommended_name_img.setVisibility(View.GONE);
						}
					}
				});

		Intent intent = getIntent();
		mobile_no = intent.getStringExtra("mobile_no");
		password = intent.getStringExtra("password");
	}

	// 隐藏软键盘并且显示光标
	public void setInputTypes(EditText editText) {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			editText.setInputType(InputType.TYPE_NULL);
		} else {
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(editText, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.btn_submit:
			toNext();
			break;
		case R.id.spinner_id_card:
			choiceIDCardDialog();
			break;
		case R.id.btn_verification:
			if (!inputIsTrue()) {
				return;
			}

			if (captchaModel == null || captchaModel.Code == null
					|| captchaModel.Code.length() == 0) {
				dialogManager.initOneBtnDialog("验证码正在加载中，请稍后输入正确的验证码", null);
				return;
			}
			// 增加验证码输入判断
			captchaCode = ed_type_the_code.getText().toString();
			startTimer();
			SendSmsType = "普通短信";
			callWebserviceGetVerificationCode(SendPhoneCodeType, SendSmsType,
					mobile_no, captchaCode, "");
			break;
		case R.id.voice_verification:
			// 调用语言验证码的接口
			SendSmsType = "语音短信";
			callWebserviceGetVerificationCode(SendPhoneCodeType, SendSmsType,
					mobile_no, captchaCode, "");
			// 隐藏语言验证码点击按钮，显示提示信息
			voice_verification.setVisibility(View.GONE);
			voice_verification_hint.setVisibility(View.VISIBLE);
			break;
		case R.id.recommended_linear:
			if (checkbox_recommended.isChecked()) {
				checkbox_recommended.setChecked(false);
			} else {
				checkbox_recommended.setChecked(true);
			}
			break;
		case R.id.ed_verify_real_name_img:
			ed_verify_real_name.setText("");
			break;
		case R.id.ed_id_number_img:
			ed_id_number.setText("");
			break;
		case R.id.ed_recommended_name_img:
			ed_recommended_name.setText("");
			break;
		case R.id.iv_captcha:
			getCaptchaCode();
			break;
		case R.id.ed_id_number:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(ed_id_number, InputMethodManager.SHOW_FORCED);
			imm.hideSoftInputFromWindow(ed_id_number.getWindowToken(), 0);
			if (!ed_id_number.hasFocus()) {
				ed_id_number_img.setVisibility(View.GONE);
			} else {
				ed_id_number_img.setVisibility(View.VISIBLE);
			}
			toShowKeyBoard();
			break;
		case LoginBotomImgViewControler.ID:
			startActivity(new Intent(this,TaiPingYangCPICActivity.class));
			overTransition(2);
			break;

		default:
			break;
		}

	}

	/**
	 * 下一步
	 */
	private void toNext() {
		mobile_verificationCode = ed_sms_verification.getText().toString()
				.replaceAll(" ", "");
		realName = ed_verify_real_name.getText().toString().replaceAll(" ", "");
		id_number = ed_id_number.getText().toString().replaceAll(" ", "");
		if (checkCaptchaCode()) {
			try {
				if (ed_sms_verification.getText().toString()
						.equalsIgnoreCase("")) {
					dialogManager.initOneBtnDialog("请输入短信验证码", null);
				} else {
					recommendName = ed_recommended_name.getText().toString()
							.replaceAll(" ", "");
					// 判断推荐人是姓名还是电话号码
					if (isNumeric(recommendName)) {
						recommended_Type = "1";
					} else {
						recommended_Type = "0";
					}
					setBtnState(false, "处理中", R.color.gray);
					if (!recommendIsExists && recommendName != null
							&& recommendName.length() > 0) {
						// 如果填写了推荐人，者先判断推荐人是否存在
						callWebserviceReferrer(recommendName, recommended_Type);
					} else {
						callWebserviceisExistCode(mobile_verificationCode,
								mobile_no);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

	/** 选择证件类型 */
	private void choiceIDCardDialog() {
		final List<MeetListDialogModel> lists = new ArrayList<MeetListDialogModel>();
		int type = Integer.parseInt(selected_card_type);

		for (int i = 0; i < type_name.length; i++) {
			if (i == type) {
				lists.add(new MeetListDialogModel(type_name[i], true));
			} else {
				lists.add(new MeetListDialogModel(type_name[i], false));
			}
		}
		mListDialog = new ListDialogManager(this, "", lists);
		mListDialog.goneTitle();
		mListDialog.Show();
		mListDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				mListDialog = null;
				for (int i = 0; i < lists.size(); i++) {
					if (lists.get(i).isChoice()) {
						selected_card_type = i + "";
						spinner_id_card.setText(lists.get(i).getContent());
						return;
					}
				}

			}
		});
	}

	/** 判断用户名和证件类型是否输入正确 */
	private boolean inputIsTrue() {
		realName = ed_verify_real_name.getText().toString().replaceAll(" ", "");
		id_number = ed_id_number.getText().toString().replaceAll(" ", "");
		if (realName.length() == 0 || ed_id_number.length() == 0) {
			dialogManager.initOneBtnDialog("请输入正确的真实姓名和证件号", null);
			return false;
		} else if (selected_card_type == null) {
			dialogManager.initOneBtnDialog("请选择证件类型", null);
			return false;
		} else if (selected_card_type != null && selected_card_type.equals("2")) {
			if (!isTaiwanCard()) {
				dialogManager.initOneBtnDialog("请输入正确的台胞证", null);
				return false;
			}
		} else if (selected_card_type != null && selected_card_type.equals("0")) {
			if (!isChineseName(realName)) {
				dialogManager.initOneBtnDialog("真实姓名请输入2-10位中文字符", null);
				return false;
			} else if (!(id_number.length() == 15 || id_number.length() == 18)) {
				dialogManager.initOneBtnDialog("请输入正确的身份证", null);
				return false;
			}
		}
		return true;

	}

	/**
	 * 判断台胞证格式是否正确
	 * 
	 * @return
	 */
	private boolean isTaiwanCard() {
		String regex = "\\d{8}";
		return Pattern.matches(regex, ed_id_number.getText().toString());
	}

	/**
	 * 判断是否为2—10位中文字符
	 * 
	 * @param str
	 * @return
	 */
	private boolean isChineseName(String str) {
		String regexStr = "[\u4E00-\u9FA5]{2,10}";
		return Pattern.matches(regexStr, str);
	}

	@Override
	public void onBackPressed() {
		if (mKeyBoardView != null) {
			mKeyBoardView.close();
			mKeyBoardView = null;
		}
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/** 从后台获取图片验证码 */
	private void getCaptchaCode() {

		dft.getCaptcha(Constants.GetValidateCode_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {

						captchaModel = (CaptchaModel) dft.GetResponseObject(
								jsonObject, CaptchaModel.class);
						setCaptchaValuesToUI(captchaModel);
						Constants.SetSeesionId(captchaModel.Cookie,
								VerifyRealNameActivity.this);
					}
				}, null);
	}

	private void setCaptchaValuesToUI(CaptchaModel captchaModel) {
		iv_captcha.setImageBitmap(stringToBitmap(captchaModel.CaptchaImg));
	}

	public Bitmap stringToBitmap(String in) {
		byte[] bytes = Base64.decode(in, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/** 判断图片验证码输入是否正确 */
	private boolean checkCaptchaCode() {
		if (captchaModel == null || captchaModel.Code == null
				|| captchaModel.Code.length() == 0) {
			dialogManager.initOneBtnDialog("验证码正在加载中，请稍后输入正确的验证码", null);
			return false;
		}
		if (!ed_type_the_code.getText().toString().replaceAll(" ", "")
				.equalsIgnoreCase(captchaModel.Code)) {
			dialogManager.initOneBtnDialog("验证码输入不正确", null);
			return false;
		}
		return true;
	}

	/** 5秒定时器，语音验证码点击按钮5秒后显示 */
	private void startTimer() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						voice_verification.setVisibility(View.VISIBLE);
					}
				});
			}
		}, 5000);
	}

	/** 获取短信验证码 */
	private void callWebserviceGetVerificationCode(String sendPhoneCodeType,
			String sendSmsType, final String mobile_no, String captchaCode,
			String username) {
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}

		dft.getVerificactionCode(sendPhoneCodeType, sendSmsType, mobile_no,
				captchaCode, username, Constants.CallPhoneCodeForReg_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("vivi", "获取手机验证码返回的信息-->" + response);
						forgotPasswordDetails = (ForgotPasswordDetails) dft
								.GetResponseObject(response,
										ForgotPasswordDetails.class);
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						Message = forgotPasswordDetails.Message.toString();

						result = forgotPasswordDetails.Result;
						if (true == result) {
							verificationCountDown();
							dialogManager.initOneBtnDialog(Message, null);
						} else {
							dialogManager.initOneBtnDialog(Message, null);
							getCaptchaCode();
						}
					}

				}, null);

	}

	/** 验证码90秒倒计时 */
	private void verificationCountDown() {
		btn_verification.setEnabled(false);
		btn_verification.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		btn_verification.setTextColor(getResources().getColor(R.color.gray));
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
				btn_verification.setText(TextUtil.getRedString(str, 0,
						(countTime + "").length()));
				countTime--;
				if (countTime == 0) {
					isover = true;
				}
				break;
			case 2:
				btn_verification.setText("获取短信验证码");
				btn_verification.setEnabled(true);
				btn_verification
						.setBackgroundResource(R.drawable.btn_selector_red);
				btn_verification.setTextColor(getResources().getColor(
						R.color.white));
				voice_verification.setVisibility(View.GONE);
				voice_verification_hint.setVisibility(View.GONE);
				countTime = 90;
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 判断是否全为数字
	 * 
	 * @param str
	 *            需要判断的字符串
	 * @return ture 全为数字
	 */
	private static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}

	/**
	 * 验证推荐人是否存在
	 * 
	 * @param recommendName
	 *            推荐人姓名或证件号码
	 * @param recommended_type
	 *            “1”为电话号码，“0” 为姓名
	 */
	private void callWebserviceReferrer(final String recommendName,
			final String recommended_type) {
		dft.getGetReferrerRegisterWebservice(recommendName, recommended_type,
				Constants.IsExitReferrer_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						feedbackDetails = (FeedbackDetails) dft
								.GetResponseObject(response,
										FeedbackDetails.class);

						int status = feedbackDetails.ProcessResult;
						statusMessage = feedbackDetails.ProcessMessage
								.toString();
						if (status == 1) {// 推荐人存在
							callWebserviceisExistCode(mobile_verificationCode,
									mobile_no);
						} else {
							setBtnState(true, "下一步",
									R.drawable.btn_selector_red);
							dialogManager.initOneBtnDialog(statusMessage, null);
						}

					}

				}, null);

	}

	/** 手机认证 */
	private void callWebserviceisExistCode(
			final String mobile_verificationCode, final String mobile_no) {
		dft.getIsExistVerificactionCode(mobile_verificationCode, mobile_no,
				Constants.IsExitVCode_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d("vivi", "手机验证码认证返回的信息-->" + response.toString());
						feedbackDetails = (FeedbackDetails) dft
								.GetResponseObject(response,
										FeedbackDetails.class);

						int status = feedbackDetails.ProcessResult;
						statusMessage = feedbackDetails.ProcessMessage
								.toString();
						if (status == 1) {
							callVerifyWebserviceRegister(password, mobile_no,
									mobile_verificationCode, recommended_Type,
									recommendName, realName, id_number,
									selected_card_type);

						} else {
							setBtnState(true, "下一步",
									R.drawable.btn_selector_red);
							dialogManager.initOneBtnDialog(statusMessage, null);
						}
					}

				}, null);
	}

	/** 注册最后一步，实名认证 */
	private void callVerifyWebserviceRegister(final String password,
			final String mobile_no, String verification_code,
			String recommended_type, String recommendName,
			final String realName, String id_number, String selected_card_type) {

		dft.getVerifyRegisterWebservice(password, mobile_no, verification_code,
				recommended_type, recommendName, realName, id_number,
				selected_card_type, Constants.Register_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						RegisterDetails registerDetails = (RegisterDetails) dft
								.GetResponseObject(response,
										RegisterDetails.class);

						int status = registerDetails.Notification.ProcessResult;
						statusMessage = registerDetails.Notification.ProcessMessage
								.toString();
						setBtnState(true, "下一步", R.drawable.btn_selector_red);
						if (status == 1) {
							Intent back = new Intent(
									VerifyRealNameActivity.this,
									RegisterSucceedActivity.class);
							back.putExtra("mobile_no", mobile_no);
							back.putExtra("password", password);
							back.putExtra("realName", realName);
							startActivity(back);
							overridePendingTransition(R.anim.trans_right_in,
									R.anim.trans_lift_out);
							VerifyRealNameActivity.this.finish();

						} else {
							dialogManager.initOneBtnDialog(statusMessage, null);
						}
					}
				}, null);
	}

	/** 获取微信红包和体验金推荐人 */
	public void getRecommendNameDetail(String mobile) {
		dft.getRecommendName(mobile, Constants.IsShareMember_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						RecommendNameModel recommendNameModel = (RecommendNameModel) dft
								.GetResponseObject(jsonObject,
										RecommendNameModel.class);
						recommendName = recommendNameModel.PhoneNumer;
						if (recommendName == null
								|| recommendName.length() == 0) {
							recommendName = "";
						} else {
							// 推荐人存在时不能在填写推荐人，并自动填写
							ed_recommended_name.setText(recommendName);
							recommendIsExists = true;
							ed_recommended_name.setEnabled(false);
							checkbox_recommended.setChecked(true);
						}
					}
				}, null);
	}

	private void toShowKeyBoard() {
		List<EditText> ets = new ArrayList<EditText>();
		ets.add(ed_id_number);
		// EditText editText = (EditText) this.getCurrentFocus();
		// hideSoftInputMode(editText);
		if (mKeyBoardView == null) {
			mKeyBoardView = new KeyBoardView(VerifyRealNameActivity.this, ets,
					ll);
			mKeyBoardView.setWidth(LayoutParams.MATCH_PARENT);
			mKeyBoardView.setHeight(LayoutParams.WRAP_CONTENT);
			mKeyBoardView.showAtLocation(ll, Gravity.BOTTOM, 0, 0);
			mKeyBoardView.setTopMargin(true);
		} else {
			mKeyBoardView.open();
		}
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
			mprogressDialog.dismiss();
		} else {
			mprogressDialog.initDialog();
		}
		btn_submit.setEnabled(bol);
		btn_submit.setText(msg);
		btn_submit.setBackgroundResource(rId);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.ed_verify_real_name:
			if (!hasFocus) {
				ed_verify_real_name_img.setVisibility(View.GONE);
			} else {
				ed_verify_real_name_img.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ed_id_number:
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(ed_id_number, InputMethodManager.SHOW_FORCED);
			imm.hideSoftInputFromWindow(ed_id_number.getWindowToken(), 0);
			if (hasFocus) {
				toShowKeyBoard();
			} else {
				mKeyBoardView.close();
			}
			break;
		case R.id.ed_recommended_name:
			if (!hasFocus) {
				ed_recommended_name_img.setVisibility(View.GONE);
			} else {
				ed_recommended_name_img.setVisibility(View.VISIBLE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mKeyBoardView != null) {
			mKeyBoardView.dismiss();
		}
	}

}
