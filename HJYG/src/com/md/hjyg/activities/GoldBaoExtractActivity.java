package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * ClassName: GoldBaoExtractActivity date: 2016-1-21 下午4:58:14 remark:提取黄金界面
 * 
 * @author pyc
 */
public class GoldBaoExtractActivity extends BaseActivity implements
		OnClickListener {

	private Activity mActivity;
	private Intent intent;
	/** 快递接收 */
	private RelativeLayout rel_express;
	/** 金店自提 */
	private RelativeLayout rel_myself;
	private int type;
//	private String AddressInfo;
	private String MobilePhone;
	private String RealName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_goldbaoextract_activity);
		mActivity = this;
		findViewandInit();
	}

	private void findViewandInit() {
		type = getIntent().getIntExtra("type", 0);
//		AddressInfo = getIntent().getStringExtra("AddressInfo");
		MobilePhone = getIntent().getStringExtra("MobilePhone");
		RealName = getIntent().getStringExtra("RealName");
		if (type == 1) {
			// 标题栏
			HeaderViewControler.setHeaderView(this, "接收方式", this);
		} else {
			HeaderViewControler.setHeaderView(this, "提取黄金", this);
		}

//		header_bottom_line = findViewById(R.id.header_bottom_line);
//		header_bottom_line.setBackgroundColor(getResources().getColor(
//				R.color.gray_line));

		rel_express = (RelativeLayout) findViewById(R.id.rel_express);
		rel_express.setOnClickListener(this);
		rel_myself = (RelativeLayout) findViewById(R.id.rel_myself);
		rel_myself.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			mActivity.finish();
			overTransition(1);
			break;
		case R.id.rel_express:// 快递接收
			if (type == 1) {

				intent = new Intent(mActivity,
						NoviceAwardExtractTypeActivity.class);
				intent.putExtra("type", 1);
//				intent.putExtra("AddressInfo", AddressInfo);
				intent.putExtra("MobilePhone", MobilePhone);
				intent.putExtra("RealName", RealName);
			} else {
				intent = new Intent(mActivity,
						GoldBaoExtractExpressActivity.class);

			}
			startActivity(intent);
			overTransition(2);
			this.finish();
			break;
		case R.id.rel_myself:// 金店自提
			if (type == 1) {
				intent = new Intent(mActivity,
						NoviceAwardExtractTypeActivity.class);
				intent.putExtra("type", 2);

			} else {
				intent = new Intent(mActivity,
						GoldBaoExtractMyselfActivity.class);
			}
			startActivity(intent);
			overTransition(2);
			this.finish();
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
