package com.md.hjyg.layoutEntities;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName: HeaderView date: 2016-3-17 下午5:29:50 remark: 头部布局
 * 
 * @author pyc
 */
@SuppressLint("HandlerLeak")
public class HeaderView extends RelativeLayout {
	
	public final static int left_img_ID = R.id.img_top_left;
	public final static int rightimg_ID = R.id.img_top_right;
	public final static int rightv_ID = R.id.tv_top_right;

	/**
	 * 头像布局
	 */
	private RelativeLayout re_top_head;
//	private CircularImage img_top_head;
	private ImageView img_top_left, img_top_logo, img_top_right;
	private TextView tv_top_title, tv_top_right;

	private Context context;
	private Bitmap[] bitmaps;

	public HeaderView(Context context) {
		this(context, null);
	}

	public HeaderView(Context context, AttributeSet attrs, int paramInt) {
		super(context, attrs, paramInt);
		this.context = context;
		inflate(context, R.layout.top_new_layout, this);
		re_top_head = (RelativeLayout) findViewById(R.id.re_top_head);
//		img_top_head = (CircularImage) findViewById(R.id.img_top_head);
		img_top_left = (ImageView) findViewById(left_img_ID);
		img_top_logo = (ImageView) findViewById(R.id.img_top_logo);
		tv_top_title = (TextView) findViewById(R.id.tv_top_title);
		tv_top_right = (TextView) findViewById(R.id.tv_top_right);
		img_top_right = (ImageView) findViewById(rightimg_ID);
	}

	public HeaderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public void setHomeView(OnClickListener listener) {
		Save.loadingImg(mHandler, context, new int[]{R.drawable.logo,R.drawable.home_topmore}, 0);
//		img_top_logo.setImageResource(R.drawable.logo_w);
		// 设置头像
//		String savefilename = Constants.GetSaveFilename(context);
//		// 判断是否已经设置了头像
//		if (Save.isSaveBitmap(savefilename)) {
//			Bitmap image = Save.getBitmap(savefilename);
//			img_top_head.setImageBitmap(image);
//		} else {// 没有设置，用默认的图片
//			img_top_head.setImageResource(R.drawable.headimg);
//		}
		re_top_head.setVisibility(View.GONE);
		img_top_logo.setVisibility(View.GONE);
		tv_top_title.setVisibility(View.GONE);
		tv_top_right.setVisibility(View.GONE);
//		img_top_right.setOnClickListener(listener);
		re_top_head.setOnClickListener(listener);
	}
	
	public void setheadViewGone(){
		re_top_head.setVisibility(View.GONE);
	}
	public void setheadViewVISIBLE(){
		re_top_head.setVisibility(View.VISIBLE);
	}

	/**
	 * 我的账户头部
	 * 
	 * @param listener
	 */
	public void setAccountView(OnClickListener listener) {
		tv_top_right.setText("退出");
		tv_top_right.setVisibility(View.GONE);
		tv_top_right.setOnClickListener(listener);
		tv_top_title.setText("我的资产");
		re_top_head.setVisibility(View.GONE);
		img_top_left.setImageResource(R.drawable.icon_newsyes);
		img_top_left.setOnClickListener(listener);
	}
	
	public void setViewUI(OnClickListener listener,String title,String right) {
		tv_top_right.setText(right);
		tv_top_right.setOnClickListener(listener);
		tv_top_title.setText(title);
		re_top_head.setVisibility(View.GONE);
		img_top_left.setImageResource(R.drawable.goback_w);
		img_top_left.setOnClickListener(listener);
		img_top_right.setOnClickListener(listener);
		tv_top_right.setOnClickListener(listener);
	}
	
	public void setRightImg(int Rid) {
		img_top_right.setImageResource(Rid);
	}

	public void setLeftImg(int Rid) {
		img_top_left.setImageResource(Rid);
	}

	public void setUIData(String titile, OnClickListener listener) {
		tv_top_title.setText(titile);
		re_top_head.setVisibility(View.GONE);
		if (listener != null) {
			img_top_left.setImageResource(R.drawable.goback_w);
			img_top_left.setOnClickListener(listener);
		} else {
			img_top_left.setVisibility(View.GONE);
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				bitmaps = (Bitmap[]) msg.obj;
				if (bitmaps != null && bitmaps.length == 2) {
					img_top_left.setImageBitmap(bitmaps[0]);
					img_top_right.setImageBitmap(bitmaps[1]);
					
				}
				break;

			default:
				break;
			}
		};
	};

	public ImageView getImg_top_right() {
		return img_top_right;
	}
	public void setRightTvGone(){
		tv_top_right.setVisibility(View.GONE);
	}
	public void setRightTvVISIBLE(){
		tv_top_right.setVisibility(View.VISIBLE);
	}
}
