
package com.md.hjyg.activities;

import java.util.ArrayList;
import java.util.Timer;

import com.md.hjyg.R;
import com.md.hjyg.adapter.PreviewScreensAdaptor;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * 欢迎页
 */
public class SplashScreen extends BaseActivity implements View.OnClickListener {

	// ImageView btnProceed;
	ViewPager pager;
	CirclePageIndicator indicator;
	private ArrayList<Integer> previewscreens;

	PreviewScreensAdaptor myPager;

	Timer timer;
	Context context;
	int currentposition = 0;
	String FromWelcome = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);

		context = getBaseContext();
		getIntentData();
		if(!Constants.GetResult_AuthToken(context).isEmpty()
				&& !FromWelcome.equalsIgnoreCase("AboutUs")
				&& Constants.GetGestureLockisOpend(context) &&
				Constants.GetGestureLockPassword(context).length() > 0)
		{
			startActivity(new Intent(this, GestureVerifyActivity.class));
			finish();
		}else if (!Constants.GetSplashscreen(context).equalsIgnoreCase("")
				&& !FromWelcome.equalsIgnoreCase("AboutUs")) {
//			startActivity(new Intent(this, HomeActivity.class));
			startActivity(new Intent(this, HomeFragmentActivity.class));
			finish();
		}
		
		findviews();
		Setviews();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if(!Constants.GetResult_AuthToken(context).isEmpty()
				&& !FromWelcome.equalsIgnoreCase("AboutUs")
				&& Constants.GetGestureLockisOpend(context) &&
				Constants.GetGestureLockPassword(context).length() > 0)
		{
			startActivity(new Intent(this, GestureVerifyActivity.class));
			finish();
		}else if (!Constants.GetSplashscreen(context).equalsIgnoreCase("")
				&& !FromWelcome.equalsIgnoreCase("AboutUs")) {
//			startActivity(new Intent(this, HomeActivity.class));
			startActivity(new Intent(this, HomeFragmentActivity.class));
			finish();
		}
		
	}

	public void getIntentData() {
		Intent intent = getIntent();
		if (intent.getStringExtra("FromWelcome") != null) {
			FromWelcome = intent.getStringExtra("FromWelcome");
		}
	}

	public void findviews() {
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		pager = (ViewPager) findViewById(R.id.view_pager);

	}

	public void Setviews() {
		previewscreens = new ArrayList<Integer>();
		previewscreens.add(R.drawable.splashscreen1);
		previewscreens.add(R.drawable.splashscreen2);
		previewscreens.add(R.drawable.splashscreen3);
		previewscreens.add(R.drawable.splashscreen4);

		myPager = new PreviewScreensAdaptor(this, previewscreens, FromWelcome);
		pager.setAdapter(myPager);

		indicator.setViewPager(pager);
		//设置选中圆点的颜色
		indicator.setFillColor(getResources().getColor(R.color.red));
		indicator.setPageColor(getResources().getColor(R.color.gray_q));
	}

	@Override
	public void onClick(View v) {
	}
	
}
