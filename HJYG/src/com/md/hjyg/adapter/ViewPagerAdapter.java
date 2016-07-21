package com.md.hjyg.adapter;

import java.util.ArrayList;

import com.md.hjyg.R;
import com.md.hjyg.activities.LoginActivity;
import com.md.hjyg.activities.RecommendedShareActivity;
import com.md.hjyg.activities.RegisterTopicActivity;
import com.md.hjyg.entity.AdvertisementModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

/**
 * 主界面ViewPager适配器
 */
public class ViewPagerAdapter extends PagerAdapter {

	LayoutInflater layoutInflater;
	Activity activity;
	private LruCache<String, Bitmap> lruCache;
	private DataFetchService dft;
	String imageUrl = Constants.IMAGE_URL, picture_url, picture, linkurl;
	private boolean isReleaseVersion = Constants.isReleaseVersion;
	ArrayList<AdvertisementModel> advertiseList = new ArrayList<AdvertisementModel>();
	int arraysize;
	ImageView img_slider;
	private Intent intent;
	private DialogManager dialogManager;

	public ViewPagerAdapter(Activity act,
			ArrayList<AdvertisementModel> big_picture_url, DataFetchService dft) {
		this.advertiseList = big_picture_url;
		this.dft = dft;
		activity = act;
		arraysize = advertiseList.size();
		layoutInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dialogManager = new DialogManager(act);

		if (Build.VERSION.SDK_INT >= 12) {
			lruCache = new LruCache<String, Bitmap>(1024 * 1024) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount() / 1024;
				}

			};
		} else {
			lruCache = new LruCache<String, Bitmap>(10);
		}
	}

	private Bitmap getBitmap(int position) {
		AdvertisementModel model = advertiseList.get(position);
		String name[] = model.BigPictureUrl.split("/");
		if (name.length == 0) {
			return null;
		}
		final String filename = name[name.length - 1];
		if (filename != null && lruCache.get(filename) != null) {// 缓存中有这张图片直接返回
			return lruCache.get(filename);
		}
		Bitmap bitmap = null;
		if (Save.isSaveBitmap(filename)) {// sdk中存在这张图片这成sd卡中读取
			bitmap = Save.getBitmap(filename);
			if (bitmap != null && filename != null) {
				lruCache.put(filename, bitmap);// 保存到缓存区
			}
			return bitmap;
		}

		if (model.IsOSS) {
			dft.loadPhoto(model.BigPictureUrl, new Response.Listener<Bitmap>() {

				@Override
				public void onResponse(Bitmap bitmap) {
					try {
						boolean isSave = false;
						if (bitmap != null && filename != null) {
							isSave = Save.saveBitmap(filename, bitmap);
							lruCache.put(filename, bitmap);
						}
						if (isSave) {
							notifyDataSetChanged();
						}
					} catch (Exception e) {
						Toast.makeText(activity,
								activity.getString(R.string.data_error),
								Toast.LENGTH_SHORT).show();
					}
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError arg0) {

				}
			});

		} else {
			dft.loadImg(model.BigPictureUrl, new Response.Listener<Bitmap>() {

				@Override
				public void onResponse(Bitmap bitmap) {
					try {
						boolean isSave = false;
						if (bitmap != null && filename != null) {
							isSave = Save.saveBitmap(filename, bitmap);
							lruCache.put(filename, bitmap);
						}
						if (isSave) {
							notifyDataSetChanged();
						}
					} catch (Exception e) {
						Toast.makeText(activity,
								activity.getString(R.string.data_error),
								Toast.LENGTH_SHORT).show();
					}
				}
			});

		}

		return null;
	}

	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@SuppressLint("InflateParams")
	public Object instantiateItem(View collection, int position) {

		View item = ((LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
				R.layout.view_pager_images, null);

		final ImageView img_slider;
		img_slider = (ImageView) item.findViewById(R.id.slider_image);
		img_slider.setTag(position % arraysize);
		AdvertisementModel model = advertiseList.get(position % arraysize);
		picture = model.BigPictureUrl.toString();
		if (model.IsOSS) {
			picture_url = model.BigPictureUrl.toString();
		} else {
			picture_url = imageUrl + picture;
		}

		if (model.LinkUrl != null) {
			linkurl = model.LinkUrl.toString();
		}

		if (!model.BigPictureUrl.isEmpty()) {
			if (isReleaseVersion) {
				img_slider.setImageBitmap(getBitmap(position % arraysize));
			} else {
				Picasso.with(activity).load(picture_url).into(img_slider);
			}
		}

		img_slider.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int position = (Integer) v.getTag();
				AdvertisementModel model = advertiseList.get(position
						% arraysize);
				linkurl = model.LinkUrl.toString();
				if (linkurl != null && !linkurl.trim().equals("#") && linkurl.length() > 0) {
					if (model.BannerType == 1) { // app链接
						String androidLink = "";
						String links[] = linkurl.split("/");
						androidLink = links[0];
//						if (linkurl.contains("/")) {
//							if (links.length > 0) {
//							}
//						}
						try {
							if (androidLink.indexOf("com.md.hjyg.activities") != -1) {
								intent = new Intent();
//						String cassName = androidLink;
								intent.setClassName(activity, androidLink);
								activity.startActivity(intent);
								activity.overridePendingTransition(R.anim.trans_right_in,
										R.anim.trans_lift_out);
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					} else if (model.BannerType == 2) { // 微信链接
						intent = new Intent(activity, RegisterTopicActivity.class);
						intent.putExtra("linkurl", linkurl);
						intent.putExtra("shortDescription", model.ShortDescription);
						activity.startActivity(intent);
						activity.overridePendingTransition(R.anim.trans_right_in,
								R.anim.trans_lift_out);
					}
				}
			}
		});

		((ViewPager) collection).addView(item, 0);

		return item;

	}

	private void inviteFriend() {
		if (Constants.GetResult_AuthToken(activity).length() == 0) {
			dialogManager.initTwoBtnDialog("下次再说", "马上去", "提示", "您需要登录后才能邀请好友",
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (v.getId()) {
							case DialogManager.CANCEL_BTN:
								dialogManager.dismiss();
								break;
							case DialogManager.CONFIRM_BTN:
								dialogManager.dismiss();
								AppController.isFromHomeActivity = true;
								intent = new Intent(activity,
										LoginActivity.class);
								activity.startActivity(intent);
								activity.overridePendingTransition(
										R.anim.trans_right_in,
										R.anim.trans_lift_out);
								break;

							default:
								break;
							}
						}
					});

		} else {
			intent = new Intent(activity, RecommendedShareActivity.class);
			activity.startActivity(intent);
			activity.overridePendingTransition(R.anim.trans_right_in,
					R.anim.trans_lift_out);
		}
	}

	public static class ImageHolder {
		ImageView img_slider;
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
