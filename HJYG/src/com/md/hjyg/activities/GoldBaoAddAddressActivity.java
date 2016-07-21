package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.wheel.ChangeAddressDialog;
import com.md.hjyg.layoutEntities.wheel.ChangeAddressDialog.OnAddressListener;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: GoldBaoAddAddressActivity date: 2016-1-23 下午4:15:34 remark:
 * 添加/修改收件地址
 * 
 * @author pyc
 */
public class GoldBaoAddAddressActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	HeaderControler header;
	private View header_bottom_line;
	private Intent intent;
	/** 返回键 */
	private LinearLayout lay_back_investment;
	private int type;
	private LinearLayout lin_choiceaddress;
	/** 选择地址Dialog */
	private ChangeAddressDialog myDialog;
	private TextView tv_address;
	private EditText ed_zipCode;
	private Button btn_confirm;
	private EditText ed_name;
	private String name;
	private EditText ed_phone,ed_adress;
	private String phone,adress;
	
	private RelativeLayout rel_ckeck;
	private boolean isckeck;
	private ImageView img_ckeck;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goldbaoaddaddress_activity);
		mActivity = this;
		findViewandInit();
	}

	private void findViewandInit() {
		// 标题栏
		intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if (type == 0) {
			header = new HeaderControler(this, true, false, "增加地址",
					Constants.CheckAuthtoken(getBaseContext()));
		} else {
			header = new HeaderControler(this, true, false, "修改地址",
					Constants.CheckAuthtoken(getBaseContext()));
		}
		header_bottom_line = findViewById(R.id.header_bottom_line);
		header_bottom_line.setBackgroundColor(getResources().getColor(
				R.color.gray_line));
		lay_back_investment = (LinearLayout) findViewById(R.id.lay_back_investment);
		lay_back_investment.setOnClickListener(this);

		lin_choiceaddress = (LinearLayout) findViewById(R.id.lin_choiceaddress);
		lin_choiceaddress.setOnClickListener(this);
		
		rel_ckeck = (RelativeLayout) findViewById(R.id.rel_ckeck);
		img_ckeck = (ImageView) findViewById(R.id.img_ckeck);
		rel_ckeck.setOnClickListener(this);
		
		tv_address = (TextView) findViewById(R.id.tv_address);
		ed_zipCode = (EditText) findViewById(R.id.ed_zipCode);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);

		ed_name = (EditText) findViewById(R.id.ed_name);
		ed_phone = (EditText) findViewById(R.id.ed_phone);
		ed_adress = (EditText) findViewById(R.id.ed_adress);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lay_back_investment:// 返回
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.rel_ckeck:// 默认
			if (isckeck) {
				img_ckeck.setImageResource(R.drawable.gold_ckeckbox_no);
				isckeck = false;
			}else {
				img_ckeck.setImageResource(R.drawable.gold_ckeckbox_yes);
				isckeck = true;
			}
			break;
		case R.id.lin_choiceaddress:// 选择地址
			myDialog = new ChangeAddressDialog(this);
			myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
			Window dialogWindow = myDialog.getWindow();
			dialogWindow.setWindowAnimations(R.style.Animcardtype);
			myDialog.setCanceledOnTouchOutside(false);
			myDialog.setAddressListener(new OnAddressListener() {

				@Override
				public void onClick(String provice, String city,
						String district, String zipCode) {
					if (district.equals("其他")) {
						tv_address.setText(provice + city);
					} else {
						tv_address.setText(provice + city + district);
						ed_zipCode.setText(zipCode);
					}
				}
			});
			myDialog.show();
			break;
		case R.id.btn_confirm:// 确认
			name = ed_name.getText().toString().replaceAll(" ", "");
			if (name.length() == 0) {
				Constants.showOkPopup(mActivity, "请输入收件人姓名");
				return;
			}
			adress = ed_adress.getText().toString().replaceAll(" ", "");
			if (adress.length() < 3) {
				Constants.showOkPopup(mActivity, "请输入详细地址");
				return;
			}
			phone = ed_phone.getText().toString().replaceAll(" ", "");
			if (phone.length() < 11) {
				Constants.showOkPopup(mActivity, "请输入正确的电话号码");
				return;
			}
			Intent data = new Intent();
			data.putExtra("address", adress + tv_address.getText().toString());
			data.putExtra("name", name);
			data.putExtra("phone", phone);
			data.putExtra("zipCode", ed_zipCode.getText().toString());
			setResult(1022, data);
			mActivity.finish();
			overTransition(1);
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		mActivity.finish();
		overTransition(1);
	}

}
