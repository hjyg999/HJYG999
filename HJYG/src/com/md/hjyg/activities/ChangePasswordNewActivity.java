package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.ChangePwd;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: ChangePasswordNewActivity date: 2016-5-9 下午6:00:46 remark:修改登录密码
 * 
 * @author pyc
 */
public class ChangePasswordNewActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener, TextWatcher, DftErrorListener {

	private EditText ed_password_old, ed_password_new, ed_password_newag,
			haveFocusView;
	private ImageView delete_password_old, delete_password_new,
			delete_password_newag;
	private ImageView yanjing_password_old, yanjing_password_new,
			yanjing_password_newag;
	private TextView tv_hit, tv_forget, tv_btn;
	private LinearLayout lin_hit;
	private ImageView img_tishi;
	private DialogManager mDialog;
	private DialogProgressManager mProgressManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_changepassword);
		findViewandInit();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "修改登录密码", this);
		mDialog = new DialogManager(this);
		mProgressManager = new DialogProgressManager(this, "数据处理中...");
		dft.setOnDftErrorListener(this);

		// 输入框
		ed_password_old = (EditText) findViewById(R.id.ed_password_old);
		ed_password_new = (EditText) findViewById(R.id.ed_password_new);
		ed_password_newag = (EditText) findViewById(R.id.ed_password_newag);
		ed_password_old.setOnFocusChangeListener(this);
		ed_password_new.setOnFocusChangeListener(this);
		ed_password_newag.setOnFocusChangeListener(this);
		ed_password_old.addTextChangedListener(this);
		ed_password_new.addTextChangedListener(this);
		ed_password_newag.addTextChangedListener(this);

		// 删除按钮
		delete_password_old = (ImageView) findViewById(R.id.delete_password_old);
		delete_password_new = (ImageView) findViewById(R.id.delete_password_new);
		delete_password_newag = (ImageView) findViewById(R.id.delete_password_newag);
		delete_password_old.setOnClickListener(this);
		delete_password_new.setOnClickListener(this);
		delete_password_newag.setOnClickListener(this);

		// 观察按钮
		yanjing_password_old = (ImageView) findViewById(R.id.yanjing_password_old);
		yanjing_password_new = (ImageView) findViewById(R.id.yanjing_password_new);
		yanjing_password_newag = (ImageView) findViewById(R.id.yanjing_password_newag);
		yanjing_password_old.setOnClickListener(this);
		yanjing_password_new.setOnClickListener(this);
		yanjing_password_newag.setOnClickListener(this);

		// 提示信息
		tv_hit = (TextView) findViewById(R.id.tv_hit);
		lin_hit = (LinearLayout) findViewById(R.id.lin_hit);
		img_tishi = (ImageView) findViewById(R.id.img_tishi);
		// 忘记密码
		tv_forget = (TextView) findViewById(R.id.tv_forget);
		tv_forget.setOnClickListener(this);
		// 确定
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.delete_password_old:
			ed_password_old.setText("");
			break;
		case R.id.delete_password_new:
			ed_password_new.setText("");
			break;
		case R.id.delete_password_newag:
			ed_password_newag.setText("");
			break;
		case R.id.yanjing_password_old:
			setInputTypeandImg(ed_password_old, yanjing_password_old);
			break;
		case R.id.yanjing_password_new:
			setInputTypeandImg(ed_password_new, yanjing_password_new);
			break;
		case R.id.yanjing_password_newag:
			setInputTypeandImg(ed_password_newag, yanjing_password_newag);
			break;
		case R.id.tv_forget:// 忘记密码
			startActivity(new Intent(this, ForgotPasswordActivity.class));
			overTransition(2);
			break;
		case R.id.tv_btn:// 确定按钮
			if (verificationInf()) {
				mProgressManager.initDialog();
				SetPwdToChange(ed_password_new.getText().toString().trim(),
						ed_password_newag.getText().toString().trim());
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
		String passWord_old = ed_password_old.getText().toString().trim();
		String passWord = ed_password_new.getText().toString().trim();
		String passWord_ag = ed_password_newag.getText().toString().trim();
		if (!(passWord_old.length() >= 6 && (Constants
				.CheckNumber(passWord_old)
				|| Constants.ChecklowercasePassword(passWord_old) || Constants
					.CheckuppercasePassword(passWord_old)))) {
			mDialog.initOneBtnDialog(false, "旧密码输入错误！", null);
			return false;
		} else if (!(passWord.length() >= 6 && (Constants.CheckNumber(passWord)
				|| Constants.ChecklowercasePassword(passWord) || Constants
					.CheckuppercasePassword(passWord)))) {
			mDialog.initOneBtnDialog(false, "请输入6-30位密码！", null);
			return false;
		} else if (!(passWord_ag.length() >= 6 && passWord_ag.equals(passWord))) {
			mDialog.initOneBtnDialog(false, "两次输入的密码不一致！", null);
			return false;
		} else if (!Constants.GetEncryptedPassword(getBaseContext())
				.equalsIgnoreCase(Constants.md5(passWord_old))) {
			mDialog.initOneBtnDialog(false, "旧密码输入错误！", null);
			delete_password_old.setImageResource(R.drawable.tishcw_36x36);
			return false;
		}
		return true;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.ed_password_old:
			setEditTextDeleteBtbMsg(hasFocus, delete_password_old,
					ed_password_old);
			if (hasFocus) {
				haveFocusView = ed_password_old;
				setHitTextUI();
			}
			break;
		case R.id.ed_password_new:
			setEditTextDeleteBtbMsg(hasFocus, delete_password_new,
					ed_password_new);
			if (!hasFocus
					&& ed_password_newag.getText().toString().trim().length() >= 6) {
				setEditTextDeleteBtbMsg(false, delete_password_newag,
						ed_password_newag);
			}
			if (hasFocus) {
				haveFocusView = ed_password_new;
				setHitTextUI();
			}
			break;
		case R.id.ed_password_newag:
			setEditTextDeleteBtbMsg(hasFocus, delete_password_newag,
					ed_password_newag);
			if (hasFocus) {
				haveFocusView = ed_password_newag;
				setHitTextUI();
			}
			break;

		default:
			break;
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
			if (editText == ed_password_newag) {
				if (slong >= 6
						&& str.equals(ed_password_new.getText().toString()
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
			}

		}
	}

	/**
	 * 设置输入框文字提示信息
	 */
	private void setHitTextUI() {
		lin_hit.setVisibility(View.VISIBLE);
		String str = haveFocusView.getText().toString().trim();
		int sLong = str.length();
		if (haveFocusView == ed_password_old) {
			lin_hit.setVisibility(View.INVISIBLE);

		} else if (haveFocusView == ed_password_new) {// 输入密码获的焦点
			if (sLong >= 6
					&& (Constants.CheckNumber(str)
							|| Constants.ChecklowercasePassword(str) || Constants
								.CheckuppercasePassword(str))) {
				setHitTextUI(R.drawable.tishzq_28x28, "密码格式输入正确",
						R.color.green_99CC33);
			} else {
				setHitTextUI(R.drawable.tishcw_28x28, "请输入6-30位密码", R.color.red);
			}

		} else if (haveFocusView == ed_password_newag) {// 再次输入密码获的焦点
			if (sLong >= 6
					&& str.equals(ed_password_new.getText().toString().trim())) {
				setHitTextUI(R.drawable.tishzq_28x28, "两次输入的密码一致",
						R.color.green_99CC33);
			} else {
				setHitTextUI(R.drawable.tishcw_28x28, "两次输入的密码不一致", R.color.red);
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
		img_tishi.setImageResource(imgID);
		tv_hit.setText(str);
		tv_hit.setTextColor(getResources().getColor(colorID));
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

	@Override
	public void webLoadError() {
		mProgressManager.dismiss();
	}

	/**
	 * 修改密码接口
	 * 
	 * @param oldpwd
	 * @param NewPwd
	 * @param code
	 */
	private void SetPwdToChange(String enPwd, String enConfirmPwd) {
		dft.SetPwdToChange(enPwd, enConfirmPwd, "", Constants.SetNewPwd_URL,
				Request.Method.POST, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						mProgressManager.dismiss();
						ChangePwd changePwd = (ChangePwd) dft
								.GetResponseObject(jsonObject, ChangePwd.class);
						if (changePwd.Notification.ProcessResult == 0) {
							mDialog.initOneBtnDialog(false,
									changePwd.Notification.ProcessMessage, null);

						} else if (changePwd.Notification.ProcessResult == 1) {
							mDialog.initOneBtnDialog(false,
									changePwd.Notification.ProcessMessage,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											// 密码修改成功以后，清除当前登录信息，跳转到重新登录页面
											Constants
													.ClearSharePref(ChangePasswordNewActivity.this);
											Intent home = new Intent(
													ChangePasswordNewActivity.this,
													LoginActivity.class);
											startActivity(home);
											overTransition(1);
											ChangePasswordNewActivity.this
													.finish();
										}
									});

						}
					}
				}, null);
	}

}
