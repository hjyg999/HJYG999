package com.md.hjyg.activities;

import java.util.Timer;
import java.util.TimerTask;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 欢迎界面
 */
public class LeadActivity extends BaseActivity {

	private Context context;
	private RelativeLayout rel_main;
	private ImageView img_logo, img_bot;
	private Animation anim;
	private boolean isfrist = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lead);
		context = getBaseContext();
		rel_main = (RelativeLayout) findViewById(R.id.rel_main);
		img_logo = (ImageView) findViewById(R.id.img_logo);
		int[] logo_WH = Save.getScaleBitmapWangH(219, 378);
		ViewParamsSetUtil.setViewHandW_rel(img_logo, logo_WH[1], logo_WH[0]);

		img_bot = (ImageView) findViewById(R.id.img_bot);
		int[] bot_WH = Save.getScaleBitmapWangH(513, 74);
		ViewParamsSetUtil.setViewHandW_rel(img_bot, bot_WH[1], bot_WH[0]);

		// GetProtocalType();
		anim = AnimationUtils.loadAnimation(this, R.anim.alpha_0_to_1);

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus && isfrist) {
			isfrist = false;
			rel_main.startAnimation(anim);
			img_logo.startAnimation(anim);
			img_bot.startAnimation(anim);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					startActivity(new Intent(context, SplashScreen.class));
					finish();
				}
			}, 2500);
		}
	}

	// private void GetProtocalType(){
	// dft.getProtocalType(Constants.GetProtocalType_URL, Request.Method.GET,
	// new Listener<JSONObject>() {
	// @Override
	// public void onResponse(JSONObject jsonObject) {
	// IsHttpsProtocalModel model = (IsHttpsProtocalModel) dft
	// .GetResponseObject(jsonObject,
	// IsHttpsProtocalModel.class);
	// if (model.notification.ProcessResult == 0) {
	//
	// } else {
	// if (Constants.isReleaseVersion) {
	// Constants.GetProtocal_URL = "http://app.xintou365.com/api/";
	// if (model.isHttpsProtocalForAndroid.equals("1")){
	// //=====现网https地址====
	// Constants.Common_URL = "https://app.xintou365.com/api/";
	// Constants.URL_JSON_Loan =
	// "https://app.xintou365.com/api/LoanApi/GetLoan";
	// Constants.URL_JSON_OBJECT_error =
	// "https://app.xintou365.com/api/LoanApi/LoanInfo/149";
	// Constants.IMAGE_URL = "https://www.xintou365.com";
	// } else {
	// //=====现网http地址====
	// Constants.Common_URL = "http://app.xintou365.com/api/";
	// Constants.URL_JSON_Loan = "http://app.xintou365.com/api/LoanApi/GetLoan";
	// Constants.URL_JSON_OBJECT_error =
	// "http://app.xintou365.com/api/LoanApi/LoanInfo/149";
	// Constants.IMAGE_URL = "http://www.xintou365.com";
	// }
	// } else {
	// // Constants.GetProtocal_URL = "https://120.55.94.176:82/api/";
	// Constants.GetProtocal_URL = "http://192.168.0.245:8081/api/";
	// if (model.isHttpsProtocalForAndroid.equals("1")){
	// //=====测试服务器https地址====
	// // Constants.Common_URL = "https://120.55.94.176:82/api/";
	// // Constants.URL_JSON_Loan =
	// "https://120.55.94.176:82/api/LoanApi/GetLoan";
	// // Constants.URL_JSON_OBJECT_error =
	// "https://120.55.94.176:82/api/LoanApi/LoanInfo/149";
	// // Constants.IMAGE_URL = "https://120.55.94.176";
	// //=====本地https测试地址====
	// Constants.Common_URL = "https://192.168.0.245:8083/api/";
	// Constants.URL_JSON_Loan =
	// "https://192.168.0.245:8083/api/LoanApi/GetLoan";
	// Constants.URL_JSON_OBJECT_error =
	// "https://192.168.0.245:8083/api/LoanApi/LoanInfo/149";
	// Constants.IMAGE_URL = "https://192.168.0.245";
	// } else {
	// //=====测试服务器http地址====
	// // Constants.Common_URL = "http://120.55.94.176:82/api/";
	// // Constants.URL_JSON_Loan =
	// "http://120.55.94.176:82/api/LoanApi/GetLoan";
	// // Constants.URL_JSON_OBJECT_error =
	// "http://120.55.94.176:82/api/LoanApi/LoanInfo/149";
	// // Constants.IMAGE_URL = "http://120.55.94.176";
	// //=====本地http测试地址====
	// Constants.Common_URL = "http://192.168.0.245:8081/api/";
	// Constants.URL_JSON_Loan =
	// "http://192.168.0.245:8081/api/LoanApi/GetLoan";
	// Constants.URL_JSON_OBJECT_error =
	// "http://192.168.0.245:8081/api/LoanApi/LoanInfo/149";
	// Constants.IMAGE_URL = "http://192.168.0.245";
	// }
	//
	// }
	// Constants.setUrl();
	// Log.i("测试", Constants.Details_URL);
	// }
	// }
	// }, null);
	// }

}
