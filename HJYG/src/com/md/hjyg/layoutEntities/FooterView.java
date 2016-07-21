package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ScreenUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ClassName: FooterView date: 2016-2-17 下午5:21:31 remark: 主页底部4个按钮
 * 
 * @author pyc
 */
public class FooterView extends LinearLayout {

	// 底部tab
	private ImageView img_invest, img_gift, img_account, img_more;
	private LinearLayout lin_invest, lin_gift, lin_account, lin_more;
	private Bitmap[] imgBitmaps;
	private Context context;
	private boolean isLondingOver;
	private int tab;

	// private OnClickListener listener;

	public FooterView(Context context) {
		super(context);
	}

	public FooterView(Context context, AttributeSet attrs, int paramInt) {
		super(context, attrs, paramInt);
	}

	public FooterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.footer_view_layout, this);
		this.context = context;
		// // 如果是编辑模式，则跳过
		// if (isInEditMode()) {
		// return;
		//
		// }

		img_invest = (ImageView) findViewById(R.id.img_invest);
		img_gift = (ImageView) findViewById(R.id.img_gift);
		img_account = (ImageView) findViewById(R.id.img_account);
		img_more = (ImageView) findViewById(R.id.img_more);

		lin_invest = (LinearLayout) findViewById(R.id.lin_invest);
		lin_gift = (LinearLayout) findViewById(R.id.lin_gift);
		lin_account = (LinearLayout) findViewById(R.id.lin_account);
		lin_more = (LinearLayout) findViewById(R.id.lin_more);

		
		new MyAsyncTask().execute();
	}

	public void setOnClickmListener(OnClickListener listener) {
		lin_invest.setOnClickListener(listener);
		lin_account.setOnClickListener(listener);
		lin_gift.setOnClickListener(listener);
		lin_more.setOnClickListener(listener);
	}

	/**
	 * 根据点击按钮改变底部状态
	 * 
	 * @param invesIscheck
	 * @param giftIscheck
	 * @param accountIscheck
	 * @param moreIscheck
	 */
	public void setFooter(boolean invesIscheck, boolean giftIscheck,
			boolean accountIscheck, boolean moreIscheck) {
		if (!isLondingOver) {
			 return;
		}

		if (invesIscheck) {
			img_invest.setImageBitmap(imgBitmaps[0]);
		} else {
			img_invest.setImageBitmap(imgBitmaps[1]);
		}
		if (giftIscheck) {
			img_gift.setImageBitmap(imgBitmaps[2]);
		} else {
			img_gift.setImageBitmap(imgBitmaps[3]);
		}
		if (accountIscheck) {
			img_account.setImageBitmap(imgBitmaps[4]);
		} else {
			img_account.setImageBitmap(imgBitmaps[5]);
		}
		if (moreIscheck) {
			img_more.setImageBitmap(imgBitmaps[6]);
		} else {
			img_more.setImageBitmap(imgBitmaps[7]);
		}

	}

	/**
	 * 根据点击按钮改变底部状态
	 */
	public void setFooterTab(int tab) {
		this.tab = tab;
		if (tab == 0) {
			setFooter(true, false, false, false);
		} else if (tab == 1) {
			setFooter(false, true, false, false);
		} else if (tab == 2) {
			setFooter(false, false, true, false);
		} else if (tab == 3) {
			setFooter(false, false, false, true);
		}
	}

	private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			imgBitmaps = new Bitmap[8];
			Bitmap top = BitmapFactory.decodeResource(context.getResources(),R.drawable.top_bg);
			int w = ScreenUtils.getScreenWidth(context);
			int width2 = top.getWidth();
			imgBitmaps[0] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.gold_click), w, width2);
			imgBitmaps[1] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.gold_default),  w, width2);
			imgBitmaps[2] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.jewellery_click),  w, width2);
			imgBitmaps[3] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.jewellery_default),  w, width2);
			imgBitmaps[4] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.found_click),  w, width2);
			imgBitmaps[5] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.found_default),  w, width2);
			imgBitmaps[6] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.mine_click),  w, width2);
			imgBitmaps[7] = Save.ScaleBitmap(BitmapFactory.decodeResource( context.getResources(), R.drawable.mine_default),  w, width2);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			isLondingOver = true;
			setFooterTab(tab);
		}

	}

}
