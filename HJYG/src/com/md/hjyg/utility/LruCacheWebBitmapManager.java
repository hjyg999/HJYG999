package com.md.hjyg.utility;

import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utils.ScreenUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;

/**
 * ClassName: LruCacheWebBitmapManager date: 2016-4-14 下午1:33:26
 * remark:带缓存的网络图片加载管理（下载的图片保存到SDK上）
 * 
 * @author pyc
 */
public class LruCacheWebBitmapManager {

	private LruCache<String, Bitmap> lruCache;
	private Context context;
	private int w;
	private volatile static LruCacheWebBitmapManager LruCacheBitmap;

	private LruCacheWebBitmapManager() {
		context = AppController.getInstance().getApplicationContext();
		w = ScreenUtils.getScreenWidth(context);
		if (Build.VERSION.SDK_INT >= 12) {
			lruCache = new LruCache<String, Bitmap>(1024 * 1024*2) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getByteCount() / 1024;
				}

			};
		} else {
			lruCache = new LruCache<String, Bitmap>(10);
		}
	}

	public static LruCacheWebBitmapManager getInstance() {
		if (LruCacheBitmap == null) {
			synchronized (LruCacheWebBitmapManager.class) {
				if (LruCacheBitmap == null) {
					LruCacheBitmap = new LruCacheWebBitmapManager();
				}
			}
		}
		return LruCacheBitmap;
	}
	

	/**
	 * 根据网址获取图片
	 * 
	 * @param Url
	 *            网址
	 * @param what
	 *            mHandler的信息标记
	 * @param arg1
	 */
	public void getBitmap(final DataFetchService dft,final String Url, final Handler mHandler,
			final int what, final int arg1) {
		if (Url == null || Url.length() == 0 || mHandler == null) {
			return;
		}
		String name[] = Url.split("/");
		if (name.length == 0) {
			return;
		}
		final String filename = name[name.length - 1];
		if (filename != null && lruCache.get(filename) != null) {// 缓存中有这张图片直接返回
			Message msg = mHandler.obtainMessage(what, lruCache.get(filename));
			msg.arg1 = arg1;
			mHandler.sendMessage(msg);
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
				Bitmap bitmap = null;
				if (Save.isSaveBitmap(filename)) {// sdk中存在这张图片这成sd卡中读取
					bitmap = Save.getBitmap(filename);
					bitmap = Save.ScaleBitmap(bitmap, w, 720);
					Message msg = mHandler.obtainMessage(what, bitmap);
					msg.arg1 = arg1;
					mHandler.sendMessage(msg);
					if (bitmap != null) {
						lruCache.put(filename, bitmap);
					}
					return;
				}

				dft.loadPhoto(Url, new Response.Listener<Bitmap>() {

					@Override
					public void onResponse(Bitmap bitmap) {
						try {
							if (bitmap != null && filename != null) {
								Bitmap bitmap2 = Save.ScaleBitmap(bitmap, w,
										720);
								Message msg = mHandler.obtainMessage(what,
										bitmap2);
								msg.arg1 = arg1;
								mHandler.sendMessage(msg);
								Save.saveBitmap(filename, bitmap);
								if (bitmap2 != null) {
									lruCache.put(filename, bitmap2);
								}
							}
						} catch (Exception e) {
						}
					}
				}, new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError arg0) {
						Toast.makeText(context, "网络连接异常，请检查您的网络！",
								Toast.LENGTH_SHORT).show();
					}
				});

			}
		}).start();

	}

	/**
	 * 根据网址获取图片
	 * 
	 * @param Url
	 *            网址
	 * @param adapter
	 *            适配器
	 * @return
	 */
	public Bitmap getBitmap(DataFetchService dft,String Url, final BaseAdapter adapter) {
		if (Url == null || Url.length() == 0 || adapter == null) {
			return null;
		}
		String name[] = Url.split("/");
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

		dft.loadPhoto(Url, new Response.Listener<Bitmap>() {

			@Override
			public void onResponse(Bitmap bitmap) {
				try {
					boolean isSave = false;
					if (bitmap != null && filename != null) {
						isSave = Save.saveBitmap(filename, bitmap);
						lruCache.put(filename, bitmap);
					}
					if (isSave) {
						adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				adapter.notifyDataSetChanged();
			}
		});

		return null;
	}
	
	/**
	 * 根据网址获取图片
	 * 
	 * @param Url
	 *            网址
	 * @param adapter
	 *            适配器
	 * @return
	 */
	public Bitmap getBitmap(DataFetchService dft,String Url, final PagerAdapter adapter) {
		if (Url == null || Url.length() == 0 || adapter == null) {
			return null;
		}
		String name[] = Url.split("/");
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
		
		dft.loadPhoto(Url, new Response.Listener<Bitmap>() {
			
			@Override
			public void onResponse(Bitmap bitmap) {
				try {
					boolean isSave = false;
					if (bitmap != null && filename != null) {
						isSave = Save.saveBitmap(filename, bitmap);
						lruCache.put(filename, bitmap);
					}
					if (isSave) {
						adapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				adapter.notifyDataSetChanged();
			}
		});
		
		return null;
	}

}
