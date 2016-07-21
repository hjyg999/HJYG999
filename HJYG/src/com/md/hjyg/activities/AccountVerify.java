package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 修改和设置手势密码时，进行用户验证的界面
 * 
 * @author Administrator
 * 
 */
public class AccountVerify extends BaseActivity implements OnClickListener {
	private Context mcontext;
	/** 电话号码 */
	private EditText ed_name;
	/** 登录密码 */
	private EditText ed_password;
	/** 认证按钮 */
	private Button btn_verify;
	private LoanModel account_info_details;
	private String mobile_no;
	private boolean iscancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_account_verify);
		mcontext = getBaseContext();
		iscancel = getIntent().getBooleanExtra("iscancel", false);
		findViews();
		init();
		GetWebserviceAccountInformationAPI(mcontext);

		btn_verify.setOnClickListener(this);
	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "账户认证", this);
	}

	private void findViews() {

		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_password = (EditText) findViewById(R.id.ed_password);
		btn_verify = (Button) findViewById(R.id.btn_verify);
	}

	/**
	 * 获取用户手机号码
	 * 
	 * @param mcontext
	 */
	private void GetWebserviceAccountInformationAPI(Context mcontext) {

		dft.getAccounInfoDetails(Constants.GetAccounInfo_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						account_info_details = (LoanModel) dft
								.GetResponseObject(response, LoanModel.class);
						// usename = account_info_details.RealName.toString();
						mobile_no = account_info_details.MobilePhone.toString();
						ed_name.setText(Constants
								.NewreplacePhoneNumberformat(mobile_no));
						ed_name.setEnabled(false);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});
	}

	private void verifyAccount() {
		if (ed_password.getText().toString().trim().length() >= 6) {
			if (Constants.md5(ed_password.getText().toString().trim()).equals(
					Constants.GetEncryptedPassword(mcontext))) {
				if (iscancel) {
					Constants.SetGestureLockisOpend(false, mcontext);
					// startActivity(new Intent(mcontext,
					// AccountInformationActivity.class));
					// overridePendingTransition(R.anim.trans_lift_in,
					// R.anim.trans_right_out);
					finish();
					overridePendingTransition(R.anim.trans_lift_in,
							R.anim.trans_right_out);
				} else {
					Intent i = new Intent(mcontext, GestureEditActivity.class);
					startActivity(i);
					overridePendingTransition(R.anim.trans_right_in,
							R.anim.trans_lift_out);
				}

				finish();

			} else {
				Constants.showOkPopup(AccountVerify.this, "密码输入错误");
			}

		} else {
			Toast.makeText(mcontext, "请输入正确的密码", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_verify:
			verifyAccount();
			break;
		case HeaderViewControler.ID:
			finish();
			overTransition(1);
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		finish();
		overTransition(1);
	}

}
