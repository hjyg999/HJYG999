package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.activities.GestureVerifyActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * SplashScreen 滑动页适配器
 */
public class PreviewScreensAdaptor extends PagerAdapter {

	Context activity;
	// String imageUrl = "http://dev.bookdrappointment.com";
	// int imageArray[];
	// ArrayList<String> clinic_image_new = new ArrayList<String>();
	ArrayList<Integer> Previewscreens = new ArrayList<Integer>();
	private ImageView imgWhtsHotBig;
	Button btnProceed;
	String FromWelcome = "";

	public PreviewScreensAdaptor(Context activity,
			ArrayList<Integer> Previewscreens, String FromWelcome) {
		this.Previewscreens = Previewscreens;
		this.activity = activity;
		this.FromWelcome = FromWelcome;
	}

	public int getCount() {
		return Previewscreens.size();
	}

	@SuppressLint("InflateParams")
	public Object instantiateItem(View collection, int position) {

		View rating_fliper_scroll = ((LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.layout_fullscreen_image_item, null);

		imgWhtsHotBig = (ImageView) rating_fliper_scroll
				.findViewById(R.id.imageView);
		btnProceed = (Button) rating_fliper_scroll
				.findViewById(R.id.btnProceed);

		imgWhtsHotBig.setBackgroundResource(Previewscreens.get(position));

		((ViewPager) collection).addView(rating_fliper_scroll, 0);

		if (position == 3) {

			btnProceed.setVisibility(View.VISIBLE);
			/*
			 * if(FromWelcome.equalsIgnoreCase("AboutUs")) {
			 * 
			 * btnProceed.setText("返回APP"); }else { btnProceed.setText("进入APP");
			 * }
			 */
			btnProceed.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Activity activity_con = (Activity) activity;
					if (FromWelcome.equalsIgnoreCase("AboutUs")) {
						// activity_con.startActivity(new Intent(activity_con,
						// HomeActivity.class));
						activity_con.finish();
						activity_con.overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
					} else {
						if (!Constants.GetResult_AuthToken(activity).isEmpty()
								&& Constants.GetGestureLockisOpend(activity)
								&& Constants.GetGestureLockPassword(activity)
										.length() > 0) {
							activity_con.startActivity(new Intent(activity,
									GestureVerifyActivity.class));

						} else
//							activity_con.startActivity(new Intent(activity_con,
//									HomeActivity.class));
							activity_con.startActivity(new Intent(activity_con,
									HomeFragmentActivity.class));
					}

					activity_con.finish();
					Constants.SetSplashscreen("splash", activity);
				}

			});
		} else {
			btnProceed.setVisibility(View.GONE);
		}

		return rating_fliper_scroll;

	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
