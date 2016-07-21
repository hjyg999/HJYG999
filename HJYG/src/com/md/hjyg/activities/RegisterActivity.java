package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.FeedbackDetails;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.LoginBotomImgViewControler;
import com.md.hjyg.layoutEntities.PasswordHitView;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 快速注册界面
 */
public class RegisterActivity extends BaseActivity implements
		View.OnClickListener {

	private TextView tv_terms_tittle, tv_terms, edittext_hint;
	private boolean ed_mobilenoisTrue, ed_mobilenohasFocus,
			ed_login_passwordTrue, ed_login_passwordhasFocus,
			ed_login_password_againFocus;
	private TextView btn_next;
	private EditText ed_mobileno, ed_login_password, ed_login_password_again;
	private CheckBox checkbox;

	private ImageView ed_mobileno_img, ed_login_password_img,
			ed_login_password_again_img;

	private Context mcontext;
	private String password;
	/** recommendName：推荐人姓名或电话号码 recommended_type：推荐人类型1为电话号码，0为姓名 */
	// private String recommendName, recommended_type;
	// private String isMobileNoExistMethod_Name = "MemberApi/IsExitMobile";
	private int status, passwordlenght;
	private boolean isCheck = true;

	private FeedbackDetails feedbackDetails;

	private PasswordHitView passwordHitView;
	LoginBotomImgViewControler controler;
	private DialogManager dialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register);
		mcontext = getBaseContext();
		findViews();
		init();
	}

	/** 验证此手机号码是否可以注册 */
	private void callWebservcieIsExistMobileNo(String mobile_no) {

		dft.getisMobileNoExist(mobile_no, Constants.IsExitMobile_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						feedbackDetails = (FeedbackDetails) dft
								.GetResponseObject(response,
										FeedbackDetails.class);

						status = feedbackDetails.ProcessResult;

						if (status == 1) {
							ed_mobilenoisTrue = true;
							edittext_hint.setText("此号码可以注册");
							edittext_hint.setTextColor(mcontext.getResources()
									.getColor(R.color.green));

						} else {
							edittext_hint.setText("该手机号已被使用,请更换另一个");
							edittext_hint.setTextColor(mcontext.getResources()
									.getColor(R.color.red));
							ed_mobilenoisTrue = false;

						}
					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});

	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "快速注册", this);
		controler = new LoginBotomImgViewControler(this, this);
		dialogManager = new DialogManager(this);
		// progressDialog = Constants.getProgressDialog(this);
		feedbackDetails = new FeedbackDetails();

		btn_next.setOnClickListener(this);
		tv_terms_tittle.setOnClickListener(this);
		tv_terms.setOnClickListener(this);
		checkbox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked())
					isCheck = true;
				else
					isCheck = false;
			}

		});

	}

	private void findViews() {
		btn_next = (TextView) findViewById(R.id.btn_next);
		tv_terms_tittle = (TextView) findViewById(R.id.tv_terms_tittle);
		tv_terms = (TextView) findViewById(R.id.tv_terms);

		passwordHitView = (PasswordHitView) findViewById(R.id.passwordHitView);
		ed_login_password = (EditText) findViewById(R.id.ed_login_password);
		ed_mobileno = (EditText) findViewById(R.id.ed_mobileno);
		ed_login_password_again = (EditText) findViewById(R.id.ed_login_password_again);
		checkbox = (CheckBox) findViewById(R.id.checkbox);
		edittext_hint = (TextView) findViewById(R.id.edittext_hint);

		ed_mobileno.addTextChangedListener(mobilenoWatcher);
		ed_mobileno.setOnFocusChangeListener(editTextFocusListener);
		ed_login_password.addTextChangedListener(mobilenoWatcher);
		ed_login_password.setOnFocusChangeListener(editTextFocusListener);
		ed_login_password_again.addTextChangedListener(mobilenoWatcher);
		ed_login_password_again.setOnFocusChangeListener(editTextFocusListener);

		ed_mobileno_img = (ImageView) findViewById(R.id.ed_mobileno_img);
		ed_mobileno_img.setOnClickListener(this);
		ed_login_password_img = (ImageView) findViewById(R.id.ed_login_password_img);
		ed_login_password_img.setOnClickListener(this);
		ed_login_password_again_img = (ImageView) findViewById(R.id.ed_login_password_again_img);
		ed_login_password_again_img.setOnClickListener(this);

	}

	/** 焦点监听 */
	OnFocusChangeListener editTextFocusListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			switch (v.getId()) {
			case R.id.ed_mobileno:
				if (!hasFocus) {
					ed_mobilenohasFocus = false;
					if (ed_mobilenoisTrue) {
						ed_mobileno_img.setImageResource(R.drawable.right);
						ed_mobileno_img.setEnabled(false);
					} else {
						ed_mobileno_img.setVisibility(View.GONE);
					}

				} else {
					ed_mobileno_img.setImageResource(R.drawable.delete);
					ed_mobileno_img.setEnabled(true);

					ed_mobileno_img.setVisibility(View.VISIBLE);
					ed_mobilenohasFocus = true;
					if (!ed_mobilenoisTrue) {
						edittext_hint.setText("请输入11位手机号码");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.red));
					} else {
						edittext_hint.setText("此号码可以注册");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.green));
					}

				}
				break;
			case R.id.ed_login_password:
				if (!hasFocus) {
					ed_login_passwordhasFocus = false;

					if (ed_login_passwordTrue) {
						ed_login_password_img
								.setImageResource(R.drawable.right);
						ed_login_password_img.setEnabled(false);
					} else {
						ed_login_password_img.setVisibility(View.GONE);
					}
				} else {
					ed_login_passwordhasFocus = true;
					ed_login_password_img.setVisibility(View.VISIBLE);
					ed_login_password_img.setImageResource(R.drawable.delete);
					ed_login_password_img.setEnabled(true);
					if (!ed_login_passwordTrue) {
						edittext_hint.setText("请输入6-30位密码");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.red));
					} else {
						edittext_hint.setText("密码格式输入正确");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.green));
					}

				}
				break;
			case R.id.ed_login_password_again:
				if (!hasFocus) {
					ed_login_password_againFocus = false;

					if ((ed_login_password.getText().toString()
							.equals(ed_login_password_again.getText()
									.toString()))
							&& ed_login_password_again.getText().toString()
									.length() >= 6) {
						ed_login_password_again_img
								.setImageResource(R.drawable.right);
						ed_login_password_again_img.setEnabled(false);
					} else {
						ed_login_password_again_img.setVisibility(View.GONE);
					}
				} else {
					ed_login_password_againFocus = true;
					ed_login_password_again_img.setVisibility(View.VISIBLE);
					ed_login_password_again_img
							.setImageResource(R.drawable.delete);
					ed_login_password_again_img.setEnabled(true);
					if (!(ed_login_password.getText().toString()
							.equals(ed_login_password_again.getText()
									.toString()))) {
						edittext_hint.setText("请与上面输入的密码一致");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.red));
					} else {
						if (ed_login_password_again.getText().toString()
								.length() >= 6) {

							edittext_hint.setText("密码输入正确");
							edittext_hint.setTextColor(mcontext.getResources()
									.getColor(R.color.green));
						} else {
							edittext_hint.setText("请输入正确的密码");
							edittext_hint.setTextColor(mcontext.getResources()
									.getColor(R.color.red));
						}
					}

				}
				break;

			default:
				break;
			}

		}
	};

	// 输入的手机号码监听
	TextWatcher mobilenoWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (ed_login_passwordhasFocus) {// 输入密码获的焦点
				password = ed_login_password.getText().toString();
				passwordHitView.setUIbyPassword(password);
				passwordlenght = ed_login_password.getText().length();
			}

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			if (ed_mobilenohasFocus) {// 收入手机号获的焦点
				edittext_hint.setText("请输入11位手机号码");
				edittext_hint.setTextColor(mcontext.getResources().getColor(
						R.color.red));
			} else if (ed_login_passwordhasFocus) {// 收入密码获的焦点
				edittext_hint.setText("请输入6-30位密码");
				edittext_hint.setTextColor(mcontext.getResources().getColor(
						R.color.red));
			} else if (ed_login_password_againFocus) {// 再次输入密码获的焦点
				edittext_hint.setText("请与上面输入的密码一致");
				edittext_hint.setTextColor(mcontext.getResources().getColor(
						R.color.red));
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (ed_mobilenohasFocus) {
				String mobileno = ed_mobileno.getText().toString();
				if (mobileno.length() == 0) {
					ed_mobilenoisTrue = false;
					return;
				}
				if (Constants.checkStartMobileNumber(mobileno)) {
					callWebservcieIsExistMobileNo(mobileno);

				} else {
					edittext_hint.setText("您输入的手机号码格式不正确");
					edittext_hint.setTextColor(mcontext.getResources()
							.getColor(R.color.red));
					ed_mobilenoisTrue = false;
				}

			} else if (ed_login_passwordhasFocus) {// 输入密码获的焦点
				if (passwordlenght >= 6
						&& (Constants.CheckNumber(password)
								|| Constants.ChecklowercasePassword(password) || Constants
									.CheckuppercasePassword(password))) {
					edittext_hint.setText("密码格式输入正确");
					ed_login_passwordTrue = true;
					edittext_hint.setTextColor(mcontext.getResources()
							.getColor(R.color.green));
				} else {
					edittext_hint.setText("请输入6-30位密码");
					ed_login_passwordTrue = false;
					edittext_hint.setTextColor(mcontext.getResources()
							.getColor(R.color.red));
				}

				if (!(ed_login_password.getText().toString()
						.equals(ed_login_password_again.getText().toString()))) {
					ed_login_password_again_img.setVisibility(View.GONE);
				} else {
					if (ed_login_password.getText().toString().length() >= 6) {

						ed_login_password_again_img.setVisibility(View.VISIBLE);
					}
				}

			} else if (ed_login_password_againFocus) {// 再次输入密码获的焦点
				if (ed_login_password.getText().toString()
						.equals(ed_login_password_again.getText().toString())) {
					if (ed_login_password_again.getText().toString().length() >= 6) {
						edittext_hint.setText("密码输入正确");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.green));
					} else {
						edittext_hint.setText("请输入正确的密码");
						edittext_hint.setTextColor(mcontext.getResources()
								.getColor(R.color.red));
					}
				} else {
					edittext_hint.setText("请与上面输入的密码一致");
					// ed_login_password_againTrue = false;
					edittext_hint.setTextColor(mcontext.getResources()
							.getColor(R.color.red));
				}

			}

		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.btn_next:
			toNext();
			break;
		case R.id.tv_terms:
			if (checkbox.isChecked()) {
				checkbox.setChecked(false);
			} else {
				checkbox.setChecked(true);
			}
			break;
		case R.id.checkbox:
			if (checkbox.isChecked())
				isCheck = true;
			else
				isCheck = false;
			break;
		case R.id.tv_terms_tittle:
			Intent intent = new Intent(RegisterActivity.this,
					RegisterServerItemActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.ed_mobileno_img:
			ed_mobileno.setText("");
			break;
		case R.id.ed_login_password_img:
			ed_login_password.setText("");
			break;
		case R.id.ed_login_password_again_img:
			ed_login_password_again.setText("");
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
		if (isCheck) {
			if (ed_login_passwordTrue && ed_mobilenoisTrue) {
				if (!ed_login_password.getText().toString()
						.equals(ed_login_password_again.getText().toString())) {
					// 两次密码输入不一致
//					Constants.showOkPopup(RegisterActivity.this, "两次输入的密码不一致");
					dialogManager.initOneBtnDialog("两次输入的密码不一致", null);
				} else {
					// 没有填写推荐人
					Intent next = new Intent(RegisterActivity.this,
							VerifyRealNameActivity.class);
					String mobile_no = ed_mobileno.getText().toString();
					String password = ed_login_password.getText().toString();
					next.putExtra("mobile_no", mobile_no);
					next.putExtra("password", password);
					startActivity(next);
					overTransition(2);

				}

			} else {
//				Constants.showOkPopup(RegisterActivity.this, "请输入正确的手机号码和密码");
				dialogManager.initOneBtnDialog("请输入正确的手机号码和密码", null);
			}

		} else {
			dialogManager.initOneBtnDialog("您必须同意服务条款才能注册", null);
//			Constants.showOkPopup(RegisterActivity.this, "您必须同意服务条款才能注册");
		}
	}

	@SuppressLint("InflateParams")
	public void conditionPopUp() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater
				.inflate(R.layout.template_tips, null);
		alertDialog.setView(convertView);
		alertDialog.setTitle("服务条款");
		alertDialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		alertDialog.show();
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

}
