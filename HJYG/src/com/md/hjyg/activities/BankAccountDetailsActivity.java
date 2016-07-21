package com.md.hjyg.activities;

import java.util.List;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.AvailablebankCard;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.BankManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * 银行卡信息
 */
public class BankAccountDetailsActivity extends BaseActivity implements
		View.OnClickListener,DftErrorListener {

	private TextView tv_BankName, tv_branch_name, ed_card_no,
			edit_bank_details,tv_add;
	private ImageView img_bot,bank_img;
	private AvailablebankCard availablebankCard;
	private String BankName, BranchName, CardNumber;
	private String bankCardId;
	private TextView tv_hitmsg;
	private RelativeLayout rel_top;
	private LinearLayout line_bot;
	private DialogProgressManager progressManager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bank_account_details);
		findViews();
		init();
		progressManager.initDialog();
		GetWebserviceAccountManagementAPI();
//		GetWebserviceBankCardValidAPI();
		
	}
	
	private void findViews() {
		tv_BankName = (TextView) findViewById(R.id.tv_BankName);
		tv_branch_name = (TextView) findViewById(R.id.tv_branch_name);
		edit_bank_details = (TextView) findViewById(R.id.edit_bank_details);
		ed_card_no = (TextView) findViewById(R.id.ed_card_no);
		tv_hitmsg = (TextView) findViewById(R.id.tv_hitmsg);
		rel_top = (RelativeLayout) findViewById(R.id.rel_top);
		line_bot = (LinearLayout) findViewById(R.id.line_bot);
		img_bot = (ImageView) findViewById(R.id.img_bot);
		tv_add = (TextView) findViewById(R.id.tv_add);
		bank_img = (ImageView) findViewById(R.id.bank_img);
	}
	
	private void init() {
		HeaderViewControler.setHeaderView(this, "银行卡管理", this);
		progressManager = new DialogProgressManager(this, getResources()
				.getString(R.string.loading));
		dft.setOnDftErrorListener(this);

		int rel_topWHs[] = Save.getScaleBitmapWangH(660, 200);
		ViewParamsSetUtil.setViewHandW_lin(rel_top, rel_topWHs[1],
				rel_topWHs[0]);
		ViewParamsSetUtil.setViewHandW_lin(tv_add, rel_topWHs[1],
				rel_topWHs[0]);
		ViewParamsSetUtil.setViewHandW_lin(line_bot,0,
				rel_topWHs[0]);
		
		int img_botWHs[] = Save.getScaleBitmapWangH(482, 66);
		ViewParamsSetUtil.setViewHandW_lin(img_bot, img_botWHs[1],
				img_botWHs[0]);

		rel_top.setVisibility(View.INVISIBLE);
		edit_bank_details.setOnClickListener(this);
		img_bot.setOnClickListener(this);
		img_bot.setOnClickListener(this);
		tv_add.setOnClickListener(this);
	}
//	/**
//	 * 是否绑定了银行卡
//	 */
//	private void GetWebserviceBankCardValidAPI() {
//
//		dft.getBankCardValidDetails(Constants.GetBankCardValid_URL,
//				Request.Method.GET, new Response.Listener<JSONObject>() {
//					@Override
//					public void onResponse(JSONObject response) {
//						Memberinfo Memberinfo = (Memberinfo) dft
//								.GetResponseObject(response, Memberinfo.class);
//
//						isValid = Memberinfo.CardValid;
//						if (isValid) {//已认证
//							rel_top.setVisibility(View.VISIBLE);
//							tv_add.setVisibility(View.GONE);
//							GetWebserviceAccountManagementAPI();
//						} else {
//							rel_top.setVisibility(View.GONE);
//							tv_add.setVisibility(View.VISIBLE);
//							progressManager.dismiss();
//						}
//					}
//				},null);
//	}

	/**
	 * 或取银行卡信息
	 */
	private void GetWebserviceAccountManagementAPI() {

		dft.getBankAccountDetails(Constants.BankCardInfo_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						progressManager.dismiss();

						availablebankCard = (AvailablebankCard) dft
								.GetResponseObject(response,
										AvailablebankCard.class);
						List<com.md.hjyg.entity.BankCard> bList = availablebankCard.BankCard;
						if (bList != null && bList.size() > 0 && bList.get(0).IsValid) {
							BankName = availablebankCard.BankCard.get(0).BankNames;
							BranchName = availablebankCard.BankCard.get(0).BankBranchName;
							CardNumber = availablebankCard.BankCard.get(0).BankNumber;
							rel_top.setVisibility(View.VISIBLE);
							tv_add.setVisibility(View.GONE);
							setUIData();
						}else {
							rel_top.setVisibility(View.GONE);
							tv_add.setVisibility(View.VISIBLE);
							tv_hitmsg.setText(availablebankCard.BankCardDes);
						}

					}
				}, null);
	}

	public void setUIData() {
		ed_card_no.setText(Newreplacecardformat(CardNumber));// bank card number
		tv_BankName.setText(BankName);// bank name
		tv_branch_name.setText(BranchName);// banch name
		tv_hitmsg.setText(availablebankCard.BankCardDes);
		BankManager.setBankCardBg(BankName, bank_img);
	}
	
	

	public static String Newreplacecardformat(String cardnum) {

		String temp_card_details = null;
		try {
//			String str1 = cardnum.substring(0, 4);
			String str2 = cardnum.substring(cardnum.length() - 4,
					cardnum.length());
			String str3 = "";
			for (int i = 0; i < cardnum.length() - 4; i++) {
				str3 += "*";

				if (i == 7||i==3)
					str3 += " ";

			}
			temp_card_details = str3 + " " + str2;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp_card_details;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.img_bot:
			startActivity(new Intent(this, TaiPingYangCPICActivity.class));
			overTransition(2);
			break;
		case R.id.tv_add:
			startActivity(new Intent(this, AddBankAccountActivity.class));
			overTransition(2);
			this.finish();
			break;
		case R.id.edit_bank_details:
			if (availablebankCard.BankCard != null
					&& availablebankCard.BankCard.size() > 0) {
				Intent intent = new Intent(this,
						BankBranchDetailsEditActivity.class);
				bankCardId = String
						.valueOf(availablebankCard.BankCard.get(0).Id);
				intent.putExtra("bankCardId", bankCardId);
				intent.putExtra("BankCardDes", availablebankCard.BankCardDes);
				intent.putExtra("BranchName", BranchName);
				startActivity(intent);
				overTransition(2);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	public void webLoadError() {
		progressManager.dismiss();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		GetWebserviceAccountManagementAPI();
//		if (isValid) {
//		}else {
//			GetWebserviceBankCardValidAPI();
//		}
	}

}
