package com.md.hjyg.adapter;


import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.activities.NoviceExclusiveActivity;
import com.md.hjyg.entity.NoviceLoan;
import com.md.hjyg.entity.NoviceLoan.LoanPicture;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.LruCacheWebBitmapManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ClassName: NoviceViewPagerAdapter date: 2016-2-25 下午2:08:20
 * remark:新手专题ViewPager适配器
 * 
 * @author pyc
 */
public class NoviceViewPagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;
	private int size;
	private Activity mActivity;
	private boolean isCanOnClick;
	private List<NoviceLoan> lists;
	private LruCacheWebBitmapManager lruCacheBitmap;
	private DataFetchService dft;

//	public NoviceViewPagerAdapter(Bitmap[] award_imgs, Context mActivity) {
//		this.award_imgs = award_imgs;
//		inflater = LayoutInflater.from(mActivity);
//		size = award_imgs.length;
//	}

	public NoviceViewPagerAdapter( Activity mActivity,
			boolean isCanOnClick,List<NoviceLoan> lists,LruCacheWebBitmapManager lruCacheBitmap) {
		inflater = LayoutInflater.from(mActivity);
		size = lists.size();
		this.mActivity = mActivity;
		this.isCanOnClick = isCanOnClick;
		this.lists = lists;
		this.lruCacheBitmap = lruCacheBitmap;
		dft = Constants.getDft(mActivity);
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == (View) object);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@SuppressLint("InflateParams")
	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		View item = inflater.inflate(R.layout.adapter_img_layout, null);
		ImageView mimg = (ImageView) item.findViewById(R.id.mimg);
		if (size != 0) {
			List<LoanPicture> loanPicture = lists.get(position % size).loanPicture;
			mimg.setTag(position % size);
			

			if (isCanOnClick) {
				
				for (int i = 0; i < loanPicture.size(); i++) {
					if (loanPicture.get(i).Type == LoanPicture.移动端列表图_10) {
						
						mimg.setImageBitmap(lruCacheBitmap.getBitmap(dft, loanPicture.get(i).URL, this));
					}
				}
				
				mimg.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mActivity,
								NoviceExclusiveActivity.class);
						intent.putExtra("position", (Integer) v.getTag());
						intent.putExtra("ActivityId", lists.get((Integer) v.getTag()).Id);
						mActivity.startActivity(intent);
						mActivity.overridePendingTransition(
								R.anim.trans_right_in, R.anim.trans_lift_out);
					}
				});
			}else {
				for (int i = 0; i < loanPicture.size(); i++) {
					if (loanPicture.get(i).Type == LoanPicture.移动端列表图_10) {
						
						mimg.setImageBitmap(lruCacheBitmap.getBitmap(dft, loanPicture.get(i).URL, this));
					}
				}
			}
		}
		container.addView(item);
		return item;
	}

}
