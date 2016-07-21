package com.md.hjyg.activities;

import java.util.ArrayList;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.AddbankCard;
import com.md.hjyg.entity.CityListById;
import com.md.hjyg.entity.ForgotPasswordDetails;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.entity.ProvinceCity;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.validators.EmptyValidator;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 添加银行卡界面--第一步
 */
public class AddBankAccountActivity extends BaseActivity implements
		View.OnClickListener {
	private static final String TAG = "AddBankAccountActivity";

	/** 确认提交按钮 */
	private TextView btn_next_add_bank_account;
	/** ed_bank_card:银行卡号 ed_bank_card_again:确认银行卡号 ed_branch_name:支行名 */
	private EditText ed_bank_card, ed_bank_card_again, ed_branch_name;
	/** ed_mobile_no_bank_card:与银行卡绑定的手机 ed_security_code_bank_card:手机短信验证码 */
	private EditText ed_mobile_no_bank_card, ed_security_code_bank_card;
	/** 真实姓名 */
	private TextView ed_account_name;
	/** 银行名 */
	private TextView ed_select_bank;
	/** 开户行所在的省 */
	private TextView ed_add_bank_province;
	/** 开户行所在的市 */
	private TextView ed_select_city;
	/** 获取短信验证码按钮 */
	private TextView btn_free_verification_code;

	private Context mcontext;
	private String bankName, bankNumber, enConfirmBankNumber, selectedProvince,
			selectedCity, subbranch, mobileCode;
	private ArrayList<String> strings;
	private ArrayList<String> strings_city;
	private String[] names;
	private String[] names_city;

	private int provinceid;
	private ProvinceCity provinceCity;
	private CityListById CityListById;

	private String verificationCode;
	private int countTime = 90;
	private boolean isover = false;
	private DialogManager mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_add_bank_account);
		mcontext = getBaseContext();
		hideKeyboard();
		findViews();
		init();

		GetWebserviceAccountInformationAPI();
	}

	private void init() {
		HeaderViewControler.setHeaderView(this, "添加银行卡", this);
		mDialog = new DialogManager(this);
		provinceCity = new ProvinceCity();
		CityListById = new CityListById();
	}

	private void findViews() {
		// TextView
		btn_next_add_bank_account = (TextView) findViewById(R.id.btn_next_add_bank_account);
		// Linear Layout
		ed_select_bank = (TextView) findViewById(R.id.ed_select_bank);
		ed_add_bank_province = (TextView) findViewById(R.id.ed_add_bank_province);
		ed_select_city = (TextView) findViewById(R.id.ed_select_city);
		//
		ed_bank_card = (EditText) findViewById(R.id.ed_bank_card);
		ed_bank_card_again = (EditText) findViewById(R.id.ed_bank_card_again);
		ed_branch_name = (EditText) findViewById(R.id.ed_branch_name);
		ed_mobile_no_bank_card = (EditText) findViewById(R.id.ed_mobile_no_bank_card);

		ed_security_code_bank_card = (EditText) findViewById(R.id.ed_security_code_bank_card);
		ed_account_name = (TextView) findViewById(R.id.ed_account_name);
		btn_free_verification_code = (TextView) findViewById(R.id.btn_free_verification_code);
		String account_name = Constants.GetRealName(mcontext);
		if (account_name != null && account_name.length() > 0) {
			ed_account_name.setText(account_name);
			// ed_account_name.setEnabled(false);
		}
		// Please select bank
		addvalidator(new EmptyValidator(this, ed_select_bank, "请选择银行", mDialog));
		// Please Select province
		addvalidator(new EmptyValidator(this, ed_add_bank_province,
				"请选择支行所在的省", mDialog));
		// Please Select City
		addvalidator(new EmptyValidator(this, ed_select_city, "请选择支行所在的城市",
				mDialog));
		// Please Enter branch name
		addvalidator(new EmptyValidator(this, ed_branch_name, "请输入支行名", mDialog));
		// Please Enter Account name
		// addvalidator(new EmptyValidator(ed_account_name, "请输入帐户名"));
		// Please Enter Bank Card
		addvalidator(new EmptyValidator(this, ed_bank_card, "请输入银行卡号", mDialog));
		addvalidator(new EmptyValidator(this, ed_bank_card_again, "请输入确认银行卡号",
				mDialog));
		addvalidator(new EmptyValidator(this, ed_mobile_no_bank_card,
				"请输入与银行卡绑定的手机", mDialog));
		// addvalidator(new EmptyValidator(this,ed_security_code_bank_card,
		// "请输入短信验证码"));

		btn_next_add_bank_account.setOnClickListener(this);
		ed_select_bank.setOnClickListener(this);
		ed_select_city.setOnClickListener(this);
		ed_add_bank_province.setOnClickListener(this);
		btn_free_verification_code.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == HeaderViewControler.ID) {
			AddBankAccountActivity.this.finish();
			overTransition(1);
		} else if (v == btn_next_add_bank_account) {

			if (validateForm()) {
				// 添加银行卡
				bankName = ed_select_bank.getText().toString().trim();
				bankNumber = ed_bank_card.getText().toString().trim();
				selectedProvince = ed_add_bank_province.getText().toString();
				selectedCity = ed_select_city.getText().toString();
				subbranch = ed_branch_name.getText().toString()
						.replaceAll(" ", "");
				enConfirmBankNumber = ed_bank_card_again.getText().toString();
				mobileCode = ed_security_code_bank_card.getText().toString()
						.replaceAll(" ", "");

				if (mobileCode.length() == 0) {
					mDialog.initOneBtnDialog(false, "请输入短信验证码", null);
					return;
				}
				CallAddBankCardWebservice(bankName, bankNumber,
						enConfirmBankNumber, selectedProvince, selectedCity,
						subbranch, mobileCode);
			}
		} else if (v == ed_select_bank) {
			hideKeyboard();

			selectBankDialog();
		} else if (v == ed_select_city) {
			hideKeyboard();
			// selectCityDialog();

			if (!ed_add_bank_province.getText().toString().equalsIgnoreCase("")) {
				WebCallprovinceCity(mcontext, provinceid);
			} else {
				mDialog.initOneBtnDialog(false, "请选择省", null);
			}
		} else if (v == ed_add_bank_province) {
			hideKeyboard();

			WebCallprovinceList(mcontext);
			// selectProvinceDialog();//call ws province and display in list
		} else if (v == btn_free_verification_code) {
			// 获取短信验证码
			if (validateForm()) {
				if (!ed_bank_card.getText().toString().trim()
						.equals(ed_bank_card_again.getText().toString().trim())) {
					mDialog.initOneBtnDialog(false, "两次输入的银行卡号不一致", null);
					return;
				}
				CallNewCallPhoneCodeWebservice("增加银行帐号", "",
						ed_mobile_no_bank_card.getText().toString());
			}
		}

	}

	public void WebCallprovinceList(Context mcontext) {

		dft.getAccounDetails(Constants.GetProvince_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						provinceCity = (ProvinceCity) dft.GetResponseObject(
								response, ProvinceCity.class);

						strings = new ArrayList<String>();

						try {
							for (int i = 0; i < provinceCity.ProvinceCityList
									.size(); i++) {
								strings.add(provinceCity.ProvinceCityList
										.get(i).Province.toString());
								names = new String[strings.size()];
								names = strings.toArray(names);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

						selectProvinceDialog(names);

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});
	}

	public void WebCallprovinceCity(Context mcontext, int selectedcity) {

		dft.getAccounDetails(Constants.GetCity_URL + selectedcity,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						CityListById = (CityListById) dft.GetResponseObject(
								response, CityListById.class);

						selectCityDialog();

					}

				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...

					}
				});
	}

	@SuppressLint("InflateParams")
	private void selectProvinceDialog(final String names[]) {
		// final String namess[] ={"Mumbai","Hydrabad","Banglor","Pune"};

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
		alertDialog.setView(convertView);
		// Select Province
		alertDialog.setTitle("选择省");
		// final ListView select_list = (ListView) convertView
		// .findViewById(R.id.select_list);

		alertDialog.setItems(names, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// Do something with the selection
				ed_add_bank_province.setText(names[item]);
				// call city Web call
				dialog.dismiss();
				String selectedcity = names[item];
				provinceid = getIDfromproviance(selectedcity);

			}
		});

		alertDialog.show();

	}

	@Override
	public void onBackPressed() {
		AddBankAccountActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

	/**
	 * @param selectedcity
	 * @return
	 */
	private int getIDfromproviance(String selectedcity) {
		int id = 0;
		for (int i = 0; i < provinceCity.ProvinceCityList.size(); i++) {
			if (provinceCity.ProvinceCityList.get(i).Province
					.equalsIgnoreCase(selectedcity)) {
				id = provinceCity.ProvinceCityList.get(i).Id;
			}

		}
		return id;
	}

	@SuppressLint("InflateParams")
	private void selectCityDialog() {

		// final String names_city[] ={"Mumbai","Hydrabad","Banglor","Pune"};

		strings_city = new ArrayList<String>();

		try {
			for (int i = 0; i < CityListById.CityListById.size(); i++) {
				strings_city.add(CityListById.CityListById.get(i).toString());
				names_city = new String[strings_city.size()];
				names_city = strings_city.toArray(names_city);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		View convertView = (View) inflater.inflate(R.layout.dialog_list, null);
		alertDialog.setView(convertView);

		// Select City
		alertDialog.setTitle("选择城市");

		alertDialog.setItems(names_city, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// Do something with the selection
				ed_select_city.setText(names_city[item]);
			}
		});

		alertDialog.show();

	}

	public void selectBankDialog() {
		final String names[] = { "中国工商银行", "中国银行", "中国农业银行", "中国建设银行", "招商银行",
				"交通银行", "中国民生银行", "中国光大银行", "广发银行", "中国邮政", "北京银行", "上海浦东发展银行",
				"兴业银行", "上海银行", "平安银行", "中信银行", "华夏银行", "深圳发展银行", "杭州银行",
				"广东南粤银行" };
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle("选择银行");

		alertDialog.setItems(names, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				ed_select_bank.setText(names[item]);
			}
		});

		alertDialog.show();

	}

	private void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 * 增加银行卡
	 * 
	 * @param bankName
	 *            银行卡名
	 * @param bankNumber
	 *            银行卡号
	 * @param enConfirmBankNumber
	 *            确认银行卡号
	 * @param selectedProvince
	 *            支行所在的省
	 * @param selectedCity
	 *            支行所在的市
	 * @param subbranch
	 *            支行名
	 * @param mobileCode
	 *            短信验证码
	 */
	private void CallAddBankCardWebservice(String bankName, String bankNumber,
			String enConfirmBankNumber, String selectedProvince,
			String selectedCity, String subbranch, String mobileCode) {

		dft.getAddBankCardWebservice(bankName, bankNumber, enConfirmBankNumber,
				selectedProvince, selectedCity, subbranch, mobileCode,
				Constants.AddBankCard_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						AddbankCard addbankcard = (AddbankCard) dft
								.GetResponseObject(response, AddbankCard.class);

						// Error if
						if (addbankcard.Notification.ProcessResult == 0) {
							mDialog.initOneBtnDialog(false,
									addbankcard.Notification.ProcessMessage
											.toString(), null);
						} else if (addbankcard.Notification.ProcessResult == 1) {
							mDialog.initOneBtnDialog(true,
									addbankcard.Notification.ProcessMessage,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											Intent intent_backdetails = new Intent(
													AddBankAccountActivity.this,
													BankAccountDetailsActivity.class);
											startActivity(intent_backdetails);
											overTransition(2);
											AddBankAccountActivity.this
													.finish();

										}
									});
						} else if (addbankcard.Notification.ProcessResult == 2) {
							mDialog.initOneBtnDialog(false,
									addbankcard.Notification.ProcessMessage,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											Intent intent_backdetails = new Intent(
													AddBankAccountActivity.this,
													BankAccountDetailsActivity.class);
											startActivity(intent_backdetails);
											overTransition(2);
										}
									});
						}

					}

				}, null);
	}

	/** 获取短信验证码 */
	private void CallNewCallPhoneCodeWebservice(String sendPhoneCodeType,
			String code, String mobileNo) {
		verificationCountDown();
		dft.getNewCallPhoneCode(sendPhoneCodeType, code, mobileNo, "普通短信",
				Constants.CallPhoneCodeForBank_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						ForgotPasswordDetails newCallPhoneCode = (ForgotPasswordDetails) dft
								.GetResponseObject(response,
										ForgotPasswordDetails.class);
						String Message = newCallPhoneCode.Message.toString();

						Log.e(TAG, "CODE.._---" + Message);

						verificationCode = Message.substring(Message.length() - 4);
						Log.e(TAG, "CODE.._---" + verificationCode);

						if (newCallPhoneCode.Result == true) {
							mDialog.initOneBtnDialog(newCallPhoneCode.Message, null);

						} else {
							mDialog.initOneBtnDialog(newCallPhoneCode.Message, null);
						}

					}

				}, null);
	}

	/**
	 * 获取用户信息,判断手机号是否已经认证
	 * */
	private void GetWebserviceAccountInformationAPI() {

		dft.getAccounInfoDetails(Constants.GetAccounInfo_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						LoanModel account_info_details = (LoanModel) dft
								.GetResponseObject(response, LoanModel.class);

						if (account_info_details.MobileValidate) {
							ed_mobile_no_bank_card.setEnabled(false);
							ed_mobile_no_bank_card
									.setBackgroundResource(R.drawable.bg_ed_noed_graysolid);
							String mobile_no = account_info_details.MobilePhone;
							ed_mobile_no_bank_card.setText(Constants
									.NewreplacePhoneNumberformat(mobile_no));
						} else {
							ed_mobile_no_bank_card
									.setBackgroundResource(R.drawable.homescreen_border);
							ed_mobile_no_bank_card.setEnabled(true);
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});
	}

	/** 验证码90秒倒计时 */
	private void verificationCountDown() {
		btn_free_verification_code.setEnabled(false);
		btn_free_verification_code.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		btn_free_verification_code.setTextColor(getResources().getColor(
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
				btn_free_verification_code.setText(TextUtil.getRedString(str,
						0, (countTime + "").length()));
				countTime--;
				if (countTime == 0) {
					isover = true;
				}
				break;
			case 2:
				btn_free_verification_code.setText("获取短信验证码");
				btn_free_verification_code.setEnabled(true);
				btn_free_verification_code
						.setBackgroundResource(R.drawable.btn_selector_red);
				btn_free_verification_code.setTextColor(getResources()
						.getColor(R.color.white));
				countTime = 90;
				break;
			default:
				break;
			}
		};
	};

}
