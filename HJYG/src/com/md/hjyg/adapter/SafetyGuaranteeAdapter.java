package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.activities.HuoQibaoDetailsActivity;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 安全保障适配器
 */
public class SafetyGuaranteeAdapter extends PagerAdapter{
	
	private ArrayList<Integer> imgIDLists = new ArrayList<Integer>();
	private Activity activity;
	private LayoutInflater layoutInflater;
	private int type;
	
	public SafetyGuaranteeAdapter(Activity activity,ArrayList<Integer> imgIDLists,int type) {
		this.imgIDLists = imgIDLists;
		this.activity = activity;
		layoutInflater = LayoutInflater.from(activity);
		this.type = type;
	}

	@Override
	public int getCount() {
		return imgIDLists.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (View)arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = layoutInflater.inflate(R.layout.adapter_safety_guarantee, null);
		ImageView imgView = (ImageView) view.findViewById(R.id.imageView);
		ImageView btn_image = (ImageView) view.findViewById(R.id.btn_image);
		TextView btn_txt = (TextView) view.findViewById(R.id.btn_txt);
		RelativeLayout btn_rel = (RelativeLayout) view.findViewById(R.id.btn_rel);
		btn_rel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.startActivity(new Intent(activity, HuoQibaoDetailsActivity.class));
				activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
			}
		});
		if (position == imgIDLists.size() - 1 && type == 1) {
			btn_rel.setVisibility(View.VISIBLE);
			btn_txt.setTextSize(ScreenUtils.px2sp(activity, 30));
			Bitmap mBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.huoqibao_btn);
			Bitmap Bitmap2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.huoqibao6);
			btn_image.setImageBitmap(Save.ScaleBitmap(mBitmap,
					activity, Bitmap2));
		}else {
			btn_rel.setVisibility(View.GONE);
		}
		imgView.setBackgroundResource(imgIDLists.get(position));
		((ViewPager) container).addView(view, 0);
		return view;
	}

}
