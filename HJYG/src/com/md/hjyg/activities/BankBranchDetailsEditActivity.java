package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.AddbankCard;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.wheel.ChangeAddressDialog;
import com.md.hjyg.layoutEntities.wheel.ChangeAddressDialog.OnAddressListener;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * 修改支行信息
 */
public class BankBranchDetailsEditActivity extends BaseActivity implements
		OnClickListener, OnFocusChangeListener ,DftErrorListener{
	/** 显示省市 */
	private EditText ed_province;
	/** 支行名 */
	private EditText ed_branch;
	/** 确认修改按钮 */
	private TextView tv_btn;
	private AddbankCard addbankcard;
	/** 需要修改的银行卡ID */
	private String bankCardId, BankCardDes;//, BranchName;
	private TextView tv_hitmsg;
	private ImageView img_bot, img_delete;
	private String mProvice, mCity,Subbranch;
	private DialogManager mDialogManager;
	private DialogProgressManager mProgressManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_edit_branch);
		findByIdandInit();

	}

	protected void findByIdandInit() {
		HeaderViewControler.setHeaderView(this, "修改支行信息", this);
		mDialogManager = new DialogManager(this);
		mProgressManager = new DialogProgressManager(this, "数据处理中...");
		dft.setOnDftErrorListener(this);
		
		ed_province = (EditText) findViewById(R.id.ed_province);
		ed_province.setOnClickListener(this);

		ed_branch = (EditText) findViewById(R.id.ed_branch);
		ed_branch.setOnFocusChangeListener(this);


		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(this);

		tv_hitmsg = (TextView) findViewById(R.id.tv_hitmsg);
		img_delete = (ImageView) findViewById(R.id.img_delete);
		img_delete.setOnClickListener(this);
		img_bot = (ImageView) findViewById(R.id.img_bot);
		int img_botWHs[] = Save.getScaleBitmapWangH(482, 66);
		ViewParamsSetUtil.setViewHandW_lin(img_bot, img_botWHs[1],
				img_botWHs[0]);
		img_bot.setOnClickListener(this);

		Intent intent = getIntent();
		bankCardId = intent.getStringExtra("bankCardId");
		BankCardDes = intent.getStringExtra("BankCardDes");
//		BranchName = intent.getStringExtra("BranchName");
		tv_hitmsg.setText(BankCardDes);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.img_delete:
			ed_branch.setText("");
			break;
		case R.id.ed_province:// 选择省市
			ChangeAddressDialog myDialog = new ChangeAddressDialog(this,
					R.style.m_dialogstyle, false, "湖南省", "长沙市");
			myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
			Window dialogWindow = myDialog.getWindow();
			dialogWindow.setWindowAnimations(R.style.Animcardtype);
			myDialog.setCanceledOnTouchOutside(false);
			myDialog.setAddressListener(new OnAddressListener() {

				@Override
				public void onClick(String provice, String city,
						String district, String zipCode) {
					ed_province.setText(provice + "/" + city);
					mProvice = provice;
					mCity = city;
				}
			});

			myDialog.show();
			break;
		case R.id.tv_btn:
			Subbranch = ed_branch.getText().toString()
					.replaceAll(" ", "");
			if (mProvice == null || mCity == null) {
				mDialogManager.initOneBtnDialog(false, "请选择开户行所在的省、市!", null);
			}else if(Subbranch.length() < 2) {
				mDialogManager.initOneBtnDialog(false, "请填写正确的支行名!", null);
			}else {
				CAllUpdateBankCardInfoWS(bankCardId, mProvice, mCity, Subbranch.replace("支行", ""));
			}
			break;
		case DialogManager.CONFIRM_BTN:
			tv_btn.setEnabled(false);
			mDialogManager.dismiss();
			CAllUpdateBankCardInfoWS(bankCardId, mProvice, mCity, Subbranch.replace("支行", ""));
			break;
		case DialogManager.CANCEL_BTN:
			mDialogManager.dismiss();
			break;
		case R.id.img_bot:
			startActivity(new Intent(this, TaiPingYangCPICActivity.class));
			overTransition(2);
			break;

		default:
			break;
		}

	}

	/** 修改银行卡支行 */
	private void CAllUpdateBankCardInfoWS(String bankCardId, String Province,
			String City, String Subbranch) {

		dft.UpdateBankCardInfo(bankCardId, Province, City, Subbranch,
				Constants.UpdateBankCard_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						tv_btn.setEnabled(true);
						mProgressManager.dismiss();
						addbankcard = (AddbankCard) dft.GetResponseObject(
								response, AddbankCard.class);
						if (addbankcard.Notification.ProcessResult == 1) {
							Toast.makeText(BankBranchDetailsEditActivity.this,
									"支行修改成功！", Toast.LENGTH_LONG).show();
							BankBranchDetailsEditActivity.this.finish();
							overTransition(1);
						} else {
							Constants.showOkPopup(
									BankBranchDetailsEditActivity.this,
									addbankcard.Notification.ProcessMessage);
						}

					}

				}, null);
	}


//	private void hideKeyboard() {
//		View view = this.getCurrentFocus();
//		if (view != null) {
//			InputMethodManager inputManager = (InputMethodManager) this
//					.getSystemService(Context.INPUT_METHOD_SERVICE);
//			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
//					InputMethodManager.HIDE_NOT_ALWAYS);
//		}
//	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (v == ed_branch) {
			if (hasFocus) {
				img_delete.setVisibility(View.VISIBLE);
			} else {
				img_delete.setVisibility(View.GONE);
			}

		}
	}

	@Override
	public void webLoadError() {
		tv_btn.setEnabled(true);
		mProgressManager.dismiss();
	}

}
