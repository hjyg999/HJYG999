package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.Notification;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.wheel.ChangeAddressDialog;
import com.md.hjyg.layoutEntities.wheel.ChangeAddressDialog.OnAddressListener;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;

/**
 * ClassName: NoviceAwardExtractTypeActivity date: 2016-2-26 上午11:24:25
 * remark:新手专享-奖品提取方式编辑
 * 
 * @author pyc
 */
public class NoviceAwardExtractTypeActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout lin_express, lin_myself;
	private TextView tv_top1;

	private int type;
	/** 选择地址Dialog */
	private ChangeAddressDialog myDialog;
	private TextView tv_choiceaddress,tv_btn,tv_name,tv_MobilePhone;
	private String AddressInfo;
	private String MobilePhone;
	private String RealName;
	private EditText ed_address;
	private String add_1,add_2;
	private Context context;
	private DialogManager dialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_noviceawardextracttype_layout);
		context = getBaseContext();
		findViewAndInit();
	}

	private void findViewAndInit() {
		dialogManager = new DialogManager(this);

		lin_express = (LinearLayout) findViewById(R.id.lin_express);
		lin_myself = (LinearLayout) findViewById(R.id.lin_myself);

		type = getIntent().getIntExtra("type", 0);
		AddressInfo = Constants.GetAwardExtractAddressInfo(context);
		MobilePhone = getIntent().getStringExtra("MobilePhone");
		RealName = getIntent().getStringExtra("RealName");
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(this);

		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_MobilePhone = (TextView) findViewById(R.id.tv_MobilePhone);
		if (type == 1) {
			HeaderViewControler.setHeaderView(this, "快递接收", this);
			lin_express.setVisibility(View.VISIBLE);
			lin_myself.setVisibility(View.GONE);
			tv_choiceaddress = (TextView) findViewById(R.id.tv_choiceaddress);
			ed_address = (EditText) findViewById(R.id.ed_address);
			tv_choiceaddress.setOnClickListener(this);
			tv_name.setText(RealName);
			tv_MobilePhone.setText(MobilePhone);
			tv_btn.setText("保   存");

			if (AddressInfo != null && AddressInfo.length() > 0) {//湖南省|长沙市|ssssss
				String  a = AddressInfo.replaceAll(",", "");
				String adr[] = a.split("\\|");
				if (adr.length == 3) {
					tv_choiceaddress.setText(adr[0] + "/" + adr[1]);
					add_1 = adr[0] + ",|," + adr[1];
					ed_address.setText(adr[2]);
					tv_btn.setText("修   改");
				}
			}

		} else if (type == 2) {
			tv_btn.setText("确   定");
			HeaderViewControler.setHeaderView(this, "门店自取", this);
			lin_express.setVisibility(View.GONE);
			lin_myself.setVisibility(View.VISIBLE);
			tv_top1 = (TextView) findViewById(R.id.tv_top1);
//			tv_top1.setText("自取地点\n湖南长沙芙蓉中路一段475号玛丽莱大厦六楼"
//					+ "\n\n附近公交\n松桂园站<109路、102路、115路、116路、"
//					+ "348路、150路、159路、303路、9路、402路、405路>\n\n自取"
//					+ "说明\n● 请于周一至周六8：30-17：30上门自取\n● 自取时请携带"
//					+ "本人有效身份证\n● 代人领取奖品的，需携带本人和被代领人身份证"
//					+ "\n● 如有疑问请联系客服400-078-1901");
			tv_top1.setText( "松桂园站<109路、 102路、 115路、 116路、 "
					+ "348路、 150路、 159路、 303路、 9路、 402路、 405路> ");
		}
		
		

	}

	/**
	 * 获取欲购买新手标的信息
	 * 
	 * @param id
	 */
	private void GetWebservice(final String AddressInfo) {
		dft.postUpdateAddressInfo(AddressInfo, Constants.UpdateAddressInfo_URL,
				Method.POST, new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						try {
							Notification model = (Notification) dft
									.GetResponseObject(response,
											Notification.class);
							if (model.ProcessResult == 1) {
								Toast.makeText(context, model.ProcessMessage, Toast.LENGTH_LONG).show();
								Constants.SetAwardExtractType(Constants.express, context);
								Constants.SetAwardExtractAddressInfo(AddressInfo, context);
								NoviceAwardExtractTypeActivity.this.finish();
								overTransition(1);
								
							}else {
								dialogManager.initOneBtnDialog(model.ProcessMessage, null);
							}

						} catch (Exception e) {
							Toast.makeText(context, getString(R.string.data_error), Toast.LENGTH_SHORT).show();
						}

						
					}
				});
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_btn:
			if (type == 2) {
				Constants.SetAwardExtractType(Constants.myself, context);
				this.finish();
				overTransition(1);
				
			}else {
				if (add_1 == null || add_1.length() == 0) {
					dialogManager.initOneBtnDialog("请选择省市！", null);
					return;
				}
				add_2 = ed_address.getText().toString().trim();
				if ( add_2.length() == 0) {
					dialogManager.initOneBtnDialog("请填写您的详细地址！", null);
					return;
				}
				
				GetWebservice(add_1 + ",|," + add_2);
			}
			break;
		case R.id.tv_choiceaddress:// 选择地址
			if (add_1 != null && add_1.length() > 0) {
				String a = add_1.replaceAll(",", "");
				String adr[] = a.split("\\|");
				myDialog = new ChangeAddressDialog(this,R.style.m_dialogstyle, false,adr[0] ,adr[1]);
				
			}else {
				myDialog = new ChangeAddressDialog(this, R.style.m_dialogstyle,false,"湖南省","长沙市");
			}
			myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题
			Window dialogWindow = myDialog.getWindow();
			dialogWindow.setWindowAnimations(R.style.Animcardtype);
			myDialog.setCanceledOnTouchOutside(false);
			myDialog.setAddressListener(new OnAddressListener() {

				@Override
				public void onClick(String provice, String city,
						String district, String zipCode) {
					if (provice.equals(city)) {
						tv_choiceaddress.setText(provice);
					} else {
						tv_choiceaddress.setText(provice + "/" + city);
						add_1 = provice + ",|," + city;
					}
				}
			});
			
			myDialog.show();
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

}
