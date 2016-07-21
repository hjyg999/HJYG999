package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.MyGoldBeansModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.RippleView;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/** 
 * ClassName: GoldBeanMainActivity 
 * date: 2016-5-24 下午4:39:58 
 * remark:我的金豆主界面
 * @author pyc
 */
public class GoldBeanMainActivity extends BaseActivity implements OnClickListener{
	
	private LinearLayout lin_qiandao,lin_choujiang;
	private TextView tv_toqiandao,tv_record,tv_rule,tv_gbamount;
	private RippleView mRippleView;
	private Intent intent;
	public MyGoldBeansModel model;
	private RelativeLayout rel_tolot;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goldbeanmain_layout);
		findViewandInit();
		getSinginLogsInfo();
	}
	
	private void findViewandInit(){
		HeaderViewControler.setHeaderView(this, "我的金豆", this);
		
		lin_qiandao = (LinearLayout) findViewById(R.id.lin_qiandao);
		ViewParamsSetUtil.setViewParams(lin_qiandao, 480, 248, true);
		lin_choujiang = (LinearLayout) findViewById(R.id.lin_choujiang);
		ViewParamsSetUtil.setViewParams(lin_choujiang, 720, 667, true);
		tv_toqiandao = (TextView) findViewById(R.id.tv_toqiandao);
		tv_toqiandao.setOnClickListener(this);
		
		mRippleView = (RippleView) findViewById(R.id.mRippleView);
		mRippleView.setOnClickListener(this);
		rel_tolot = (RelativeLayout) findViewById(R.id.rel_tolot);
		rel_tolot.setOnClickListener(this);
		
		tv_rule = (TextView) findViewById(R.id.tv_rule);
		tv_rule.setOnClickListener(this);
		
		tv_record = (TextView) findViewById(R.id.tv_record);
		tv_record.setOnClickListener(this);
		
		tv_gbamount = (TextView) findViewById(R.id.tv_gbamount);
		
	}
	
	/**
	 * 获取金豆签到信息
	 */
	private void getSinginLogsInfo() {
		dft.getNetInfoById(Constants.MyGoldBeans_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						model = (MyGoldBeansModel) dft.GetResponseObject(
								josnbject, MyGoldBeansModel.class);
						setDate();
					}
				});
	}
	
	private void setDate(){
		if (model!=null) {
			Constants.LeaveGoldBean = Constants.StringToCurrency(model.LeaveGoldBean).replace(".00", "");
			tv_gbamount.setText(Constants.LeaveGoldBean);
			if (model.IsSignin) {
				lin_qiandao.setBackgroundResource(R.drawable.gb_qiandaoh);
			}else {
				lin_qiandao.setBackgroundResource(R.drawable.gb_toqiandao);
				
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_toqiandao://去签到
			intent = new Intent(this, GoldBeanQiandaoActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.rel_tolot://去抽奖
		case R.id.mRippleView://去抽奖
			intent = new Intent(this, GoldBeanLotteryActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_rule://规则
			intent = new Intent(this, GoldBeanRuleActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.tv_record://记录
			intent = new Intent(this, GoldBeanRecordActivity.class);
			startActivity(intent);
			overTransition(2);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mRippleView.stop();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		mRippleView.start();
		tv_gbamount.setText(Constants.LeaveGoldBean);
		if (AppController.AccountInfIsChange) {
			getSinginLogsInfo();
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}
	
	@Override
	protected void onDestroy() {
		mRippleView.stop();
		super.onDestroy();
	}
	
	

}
