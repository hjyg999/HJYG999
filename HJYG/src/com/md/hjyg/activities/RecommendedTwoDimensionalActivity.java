package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * ClassName: RecommendedTwoDimensionalActivity 
 * date: 2015-12-7 上午11:03:18 
 * remark:邀请好友赚钱--专属二维码界面
 * @author pyc
 */
public class RecommendedTwoDimensionalActivity extends BaseActivity implements OnClickListener{
	
	private ImageView img_two_dimensional_code;
	private TextView tv_MyTwoDimensionDes;
	private UmengShareManager umengShareManager;
	private Bitmap twodimensioncode_bitmap;
	private Dialog dialog;
	private CaptchaModel model ;
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				model = umengShareManager.getCaptchaModel();
				twodimensioncode_bitmap = umengShareManager.getTwodimensioncode_bitmap();
				img_two_dimensional_code.setImageBitmap(twodimensioncode_bitmap);
				tv_MyTwoDimensionDes.setText(model.MyTwoDimensionDes);
				img_two_dimensional_code.setOnClickListener(RecommendedTwoDimensionalActivity.this);
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedtwodimensional_activity);
		
		findViewandInit();
	}
	
	private void findViewandInit(){
		HeaderViewControler.setHeaderView(this, "我的二维码", this);
		
		img_two_dimensional_code = (ImageView) findViewById(R.id.img_two_dimensional_code);
		tv_MyTwoDimensionDes = (TextView) findViewById(R.id.tv_MyTwoDimensionDes);
		umengShareManager = new UmengShareManager(this, mHandler);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.img_two_dimensional_code:
			showDialog();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}
	
	/** 弹出大图 */
	@SuppressLint("InflateParams")
	private void showDialog() {

		dialog = new Dialog(this);
		// 取消标题栏
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setWindowAnimations(R.style.Animcardtype);

		dialog.setCanceledOnTouchOutside(true);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(this.getResources().getColor(
				R.color.white));
		final ImageView imageView = new ImageView(this);
		int width = ScreenUtils.getScreenWidth(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				(int) (width * 0.85), (int) (width * 0.85));
		params.gravity = Gravity.CENTER;

		params.setMargins(10, 10, 10, 10);
		imageView.setLayoutParams(params);
		imageView.setImageBitmap(twodimensioncode_bitmap);
		linearLayout.addView(imageView);
		imageView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				final Dialog mydialog = new Dialog(RecommendedTwoDimensionalActivity.this);
				// 取消标题栏
				mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				Window dialogWindow = mydialog.getWindow();
				dialogWindow.setWindowAnimations(R.style.Animcardtype);

				mydialog.setCanceledOnTouchOutside(true);

				View view = LayoutInflater.from(RecommendedTwoDimensionalActivity.this)
						.inflate(R.layout.mydimensional, null);
				TextView save_tv = (TextView) view.findViewById(R.id.save_tv);
				TextView cancel_tv = (TextView) view
						.findViewById(R.id.cancel_tv);
				save_tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						boolean issave = Save.saveBitmap("twodimensional.jpg",
								((BitmapDrawable) imageView.getDrawable())
										.getBitmap());
						if (issave) {
							Toast.makeText(RecommendedTwoDimensionalActivity.this,
									"二维码保存成功！", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(RecommendedTwoDimensionalActivity.this,
									"二维码保存失败，请检查内存卡！", Toast.LENGTH_SHORT)
									.show();

						}
						mydialog.dismiss();

					}
				});
				cancel_tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mydialog.dismiss();

					}
				});
				mydialog.setContentView(view);
				mydialog.show();
				
				return false;
			}
		});

		dialog.setContentView(linearLayout);

		dialog.show();

	}

}
