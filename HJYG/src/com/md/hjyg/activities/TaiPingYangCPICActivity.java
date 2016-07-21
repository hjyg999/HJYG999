package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.CpicInfoModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: TaiPingYangCPICActivity date: 2016-4-19 下午2:49:58 remark:太平洋保险说明页面
 * 
 * @author pyc
 */
public class TaiPingYangCPICActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {

	private Bitmap[] bitmaps;
	private DialogProgressManager dialog;
	private ScrollView mScrollView;
	private LinearLayout lin_bj_06;
	private ImageView img_01, img_main, img_center1, img_center2, img_center3,
			img_center4, img_center5;
	private TextView tv_top1, tv_top2, tv_title1, tv_title2, tv_title3,
			tv_title4, tv_title5,tv_title6,tv_title7;
	private CpicInfoModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.taipingyangcpicactivity_layout);
		findViewandInit();
		dialog.initDialog();

		Save.loadingImg(mHandler, getApplicationContext(), new int[] {
				R.drawable.taipy_01, R.drawable.taipy_02, R.drawable.taipy_03,
				R.drawable.taipy_04, R.drawable.taipy_05,
				R.drawable.taipy_bj_06, R.drawable.taipy_07 });
		getWebData();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "太平洋保险承保", this);
		dialog = new DialogProgressManager(this, "努力加载中...");
		dft.setOnDftErrorListener(this);

		mScrollView = (ScrollView) findViewById(R.id.mScrollView);
		mScrollView.setVisibility(View.INVISIBLE);

		lin_bj_06 = (LinearLayout) findViewById(R.id.lin_bj_06);
		img_01 = (ImageView) findViewById(R.id.img_01);
		img_main = (ImageView) findViewById(R.id.img_main);
		img_center1 = (ImageView) findViewById(R.id.img_center1);
		img_center2 = (ImageView) findViewById(R.id.img_center2);
		img_center3 = (ImageView) findViewById(R.id.img_center3);
		img_center4 = (ImageView) findViewById(R.id.img_center4);
		img_center5 = (ImageView) findViewById(R.id.img_center5);

		tv_top1 = (TextView) findViewById(R.id.tv_top1);
		tv_top2 = (TextView) findViewById(R.id.tv_top2);
		tv_title1 = (TextView) findViewById(R.id.tv_title1);
		tv_title2 = (TextView) findViewById(R.id.tv_title2);
		tv_title3 = (TextView) findViewById(R.id.tv_title3);
		tv_title4 = (TextView) findViewById(R.id.tv_title4);
		tv_title5 = (TextView) findViewById(R.id.tv_title5);
		tv_title6 = (TextView) findViewById(R.id.tv_title6);
		tv_title7 = (TextView) findViewById(R.id.tv_title7);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				bitmaps = (Bitmap[]) msg.obj;
				setDataUI();
				break;

			default:
				break;
			}
		};
	};

	private void getWebData() {
		dft.getNetInfoById(Constants.GetCPICInfo_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject arg0) {
						model = (CpicInfoModel) dft.GetResponseObject(arg0,
								CpicInfoModel.class);
						setDataUI();

					}
				});
	}

	private void setDataUI() {
		if (bitmaps != null && bitmaps.length > 0 && model != null) {
			ViewParamsSetUtil.setViewHandW_lin(lin_bj_06,
					bitmaps[5].getHeight(), 0);
			img_01.setImageBitmap(bitmaps[0]);
			img_main.setImageBitmap(bitmaps[1]);
			img_center1.setImageBitmap(bitmaps[2]);
			img_center2.setImageBitmap(bitmaps[6]);
			img_center3.setImageBitmap(bitmaps[3]);
			img_center4.setImageBitmap(bitmaps[6]);
			img_center5.setImageBitmap(bitmaps[4]);

			int h = bitmaps[0].getHeight();
			float fontScale = getResources().getDisplayMetrics().scaledDensity;
			tv_top1.setTextSize(TypedValue.COMPLEX_UNIT_SP,
					(h * 0.5f * 0.4815f) * 1.5f / fontScale);
			tv_top2.setTextSize(TypedValue.COMPLEX_UNIT_SP,
					(h * 0.5f * 0.5185f) * 1.5f / fontScale);
			tv_top1.setText(model.cpicInfo1);
			tv_top2.setText(model.cpicInfo2);
			tv_title1.setText(model.cpicInfo3);
			tv_title2.setText(model.cpicInfo4);
			tv_title3.setText(model.cpicInfo5);
			tv_title4.setText(model.cpicInfo6);
			tv_title5.setText(model.cpicInfo7);
			tv_title6.setText(model.cpicInfo8);
			tv_title7.setText(model.cpicInfo9);

			mScrollView.setVisibility(View.VISIBLE);
			dialog.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
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

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

}
