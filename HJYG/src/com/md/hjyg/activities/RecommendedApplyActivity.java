package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.OnlyIncludeNotificationModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.xinTouConstant.Constants;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: RecommendedApplyActivity date: 2015-12-10 下午2:09:54
 * remark:理财师申请界面--申请理财师
 * 
 * @author pyc
 */
public class RecommendedApplyActivity extends BaseActivity implements
		OnClickListener {

	private TextView btn_apply, apply_mark, apply_name;
	private EditText apply_phone;
	private String phoneNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedapply_activity);

		findViewandInit();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "申请理财师", this);

		apply_name = (TextView) findViewById(R.id.apply_name);
		apply_name.setText("姓名：" + Constants.GetRealName(this));
		apply_phone = (EditText) findViewById(R.id.apply_phone);

		apply_mark = (TextView) findViewById(R.id.apply_mark);
		btn_apply = (TextView) findViewById(R.id.btn_apply);
		btn_apply.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.btn_apply:
			String phone = apply_phone.getText().toString().trim();
			if (phone.length() == 0
					|| !Constants.checkStartMobileNumber(phone)) {
				Constants.showOkPopup(this, "请输入正确的手机号码");
				return;
			}
			if (phoneNumber!=null && phoneNumber.equals(phone)) {
				return;
			}
			phoneNumber = phone;
			apply_mark.setVisibility(View.GONE);
			postWebService();
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

	/** 向后台发送理财师申请信息 */
	private void postWebService() {
		dft.postApplyFinancialPlanner(phoneNumber,
				Constants.ApplyFinancialPlanner_URL, Request.Method.POST,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						OnlyIncludeNotificationModel model = (OnlyIncludeNotificationModel) dft
								.GetResponseObject(response,
										OnlyIncludeNotificationModel.class);
						setResponseDate(model);

					}
				});
	}
	
	/**设置返回数据*/
	private void setResponseDate(OnlyIncludeNotificationModel model){
		if (model.Notification.ProcessResult == 1) {//设置成功信息
			Constants.showOkPopup(this, model.Notification.ProcessMessage, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (Constants.dialog!= null && Constants.dialog.isShowing()) {
						Constants.dialog.dismiss();
					}
					setResult(101);
					RecommendedApplyActivity.this.finish();
					overTransition(1);
				}
			});
		}else {//设置失败信息
			apply_mark.setText(model.Notification.ProcessMessage);
			apply_mark.setVisibility(View.VISIBLE);
		}
	}

}
