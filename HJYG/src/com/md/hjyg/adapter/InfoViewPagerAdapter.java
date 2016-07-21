package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.InformationDetailsActivity;
import com.md.hjyg.entity.InformationModel;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * ClassName: InfoViewPagerAdapter date: 2016-5-17 上午11:38:50
 * remark:资讯头部ViewPager适配器
 * 
 * @author pyc
 */
public class InfoViewPagerAdapter extends PagerAdapter {

	private List<InformationModel> lists;
	private Activity context;
	private LayoutInflater inflater;
	private int size;
	private String url;

	public InfoViewPagerAdapter(List<InformationModel> lists, Activity context,String url) {
		this.lists = lists;
		this.context = context;
		this.url = url;
		inflater = LayoutInflater.from(context);
		size = lists.size();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View)object);
	}

	@SuppressLint("InflateParams")
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View v = inflater.inflate(R.layout.view_pager_images, null);
		ImageView mimg = (ImageView) v.findViewById(R.id.slider_image);
		Picasso.with(context).load(lists.get(position%size).PictureHeadUrl).into(mimg);
		mimg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toInfDetails(position%size);
			}
		});
		container.addView(v, 0);
		return v;
	}
	
	/**
	 * 跳到资讯详情页
	 * @param model
	 * @param position
	 */
	private void toInfDetails(int position){
		InformationModel bean =  lists.get(position);
		Intent intent = new Intent(context,
				InformationDetailsActivity.class);
		intent.putExtra("Contents", bean.Contents);
		intent.putExtra("Title", bean.Title);
		intent.putExtra("CreateTime", bean.CreateTime);
		intent.putExtra("shareUrl", url + bean.Id);
		intent.putExtra("media", bean.media);
		
		context.startActivity(intent);
		context.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_lift_out);
	}

}
