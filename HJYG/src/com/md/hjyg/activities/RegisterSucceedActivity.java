package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.entity.ConfigInfoModel;
import com.md.hjyg.entity.LoginEntity;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.CryptLib;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;

/**
 * 注册成功页面
 * */
public class RegisterSucceedActivity extends BaseActivity implements
		OnClickListener {

	private TextView add_bankcard, tologin;
	/** 手机号和密码 */
	String mobile_no, password;

	CryptLib _crypt;
	String key;
	String iv;

	Context mcontext;
	boolean isToRecharge;
	private View vertical_lin2;
	private LinearLayout tep3;
	private TextView tep1_texthit;
	private TextView tep2_text, tep3_text;
	private TextView tep2_texthit, tep3_texthit;

	private CaptchaModel captchaModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register_succeed);
		findViewAndInit();
		getRegisterBtnTxet();
		getCaptchaCode();
	}

	private void findViewAndInit() {
		mcontext = getBaseContext();
		HeaderViewControler.setHeaderView(this, "注册完成", this);

		add_bankcard = (TextView) findViewById(R.id.add_bankcard);
		tologin = (TextView) findViewById(R.id.tologin);
		add_bankcard.setOnClickListener(this);
		tologin.setOnClickListener(this);
		// 抽奖活动相关设置
		vertical_lin2 = findViewById(R.id.vertical_lin2);
		tep3 = (LinearLayout) findViewById(R.id.tep3);
		tep3_text = (TextView) findViewById(R.id.tep3_text);
		tep1_texthit = (TextView) findViewById(R.id.tep1_texthit);
		tep2_text = (TextView) findViewById(R.id.tep2_text);
		tep2_texthit = (TextView) findViewById(R.id.tep2_texthit);
		tep3_texthit = (TextView) findViewById(R.id.tep3_texthit);

		Intent intent = getIntent();
		mobile_no = intent.getStringExtra("mobile_no");
		password = intent.getStringExtra("password");
		Constants.SetRealName(intent.getStringExtra("realName"), mcontext);
		SetupEncryption();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			startActivity(new Intent(this, HomeFragmentActivity.class));
			finish();
			overTransition(1);
			break;
		case R.id.add_bankcard:
			if (Constants.GetResult_AuthToken(mcontext).length() == 0) {
				isToRecharge = true;
				// 先登录，再添加银行卡
				String ency_pwd;
				try {
					ency_pwd = _crypt.encrypt(password, key, iv);
					callLoginService(mobile_no, ency_pwd, iv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				startActivity(new Intent(RegisterSucceedActivity.this,
						AddBankAccountActivity.class));
				overTransition(2);
			}
			break;
		case R.id.tologin:
			if (Constants.GetResult_AuthToken(mcontext).length() == 0) {
				isToRecharge = false;
				// 登录，进入我的账户界面
				String ency_pwd;
				try {
					ency_pwd = _crypt.encrypt(password, key, iv);
					callLoginService(mobile_no, ency_pwd, iv);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// startActivity(new Intent(RegisterSucceedActivity.this,
				// AccountActivity.class));
				Intent intent = new Intent(RegisterSucceedActivity.this,
						HomeFragmentActivity.class);
				intent.putExtra("tab", 2);
				startActivity(intent);
				overTransition(2);
			}
			break;

		default:
			break;
		}

	}

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
			String iv) {
		if (captchaModel == null) {
			Constants.showOkPopup(RegisterSucceedActivity.this, "数据加载中，请稍候");
			return;
		}
		dft.doLogin(Username.trim(), EncryptedPwd.trim(), captchaModel.Code,
				"", iv, Constants.Login_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						try {
							LoginEntity LoginEntity = (LoginEntity) dft
									.GetResponseObject(jsonObject,
											LoginEntity.class);
							if (LoginEntity.Notification.ProcessResult == 0) {
								Toast.makeText(mcontext,
										getString(R.string.data_error),
										Toast.LENGTH_SHORT).show();
							} else if (LoginEntity.Notification.ProcessResult == 1) {
								AppController.AccountInfIsChange = true;
								Constants.SetAuthToken(LoginEntity.AuthToken,
										mcontext);
								Constants.SavePassword(Constants.md5(password),
										mcontext);
								Constants.SetProtectedMobile(Constants
										.NewreplacePhoneNumberformat(Username),
										mcontext);
								if (isToRecharge) {
									startActivity(new Intent(
											RegisterSucceedActivity.this,
											RechargeActivity.class));
									overTransition(2);
								} else {
									// startActivity(new
									// Intent(RegisterSucceedActivity.this,
									// AccountActivity.class));
									Intent intent = new Intent(
											RegisterSucceedActivity.this,
											HomeFragmentActivity.class);
									intent.putExtra("tab", 2);
									startActivity(intent);
									overTransition(1);
								}
								
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, null);
	}

	@Override
	public void onBackPressed() {
		// startActivity(new Intent(this, HomeActivity.class));
		startActivity(new Intent(this, HomeFragmentActivity.class));
		finish();
		overTransition(1);
	}

	/** 获取图形验证码 */
	private void getCaptchaCode() {
		dft.getCaptcha(Constants.GetValidateCode_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						captchaModel = (CaptchaModel) dft.GetResponseObject(
								jsonObject, CaptchaModel.class);
						Constants.SetSeesionId(captchaModel.Cookie, mcontext);
						Log.i("Cookie", captchaModel.Cookie);
					}
				}, null);
	}

	/** 获取注册成功的文字信息 */
	private void getRegisterBtnTxet() {
		dft.getNetInfoById(Constants.getConfigInfo_URL, Request.Method.GET,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {
						ConfigInfoModel model = (ConfigInfoModel) dft
								.GetResponseObject(jsonObject,
										ConfigInfoModel.class);
						if (model != null && model.InvestInfoDes != null) {
							String str[] = model.InvestInfoDes.split("-");
							if (str.length >= 3) {

								tep1_texthit.setText(str[0]);
								tep2_text.setText(str[1]);
								tep2_texthit.setText(str[2]);
							}

							if (str.length == 5) {
								vertical_lin2.setVisibility(View.VISIBLE);
								tep3.setVisibility(View.VISIBLE);
								tep3_text.setVisibility(View.VISIBLE);
								tep3_texthit.setVisibility(View.VISIBLE);
								tep3_text.setText(str[3]);
								tep3_texthit.setText(str[4]);

							} else {
								vertical_lin2.setVisibility(View.GONE);
								tep3.setVisibility(View.GONE);
								tep3_text.setVisibility(View.GONE);
								tep3_texthit.setVisibility(View.GONE);
							}
						}
					}
				});
	}

}
