package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: HappySmallWritingMainActivity date: 2016-7-13 上午9:14:00
 * remark:快乐微文主界面
 * 
 * @author pyc
 */
public class HappySmallWritingMainActivity extends BaseActivity implements
		OnClickListener {

	private HeaderView mheadView;
	private ImageView img_top;
	private int[] arrowImgRDs = { R.id.img1, R.id.img2, R.id.img3, R.id.img4,
			R.id.img5 ,R.id.img6,R.id.img7};
	private TextView tv_btn;
	private DialogManager mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_happysmallwritingmain_layout);

		findView();
	}

	private void findView() {
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "快乐微文竞赛活动", "");
		mheadView.setRightImg(R.drawable.happysmallwriting_torule);
		mDialog = new DialogManager(this);

		img_top = (ImageView) findViewById(R.id.img_top);
		ViewParamsSetUtil.setViewParams(img_top, 623, 682, true);

		int[] arW_Hs = Save.getScaleBitmapWangH(20, 20);
		for (int i = 0; i < arrowImgRDs.length; i++) {
			ViewParamsSetUtil.setViewHandW_lin(findViewById(arrowImgRDs[i]),
					arW_Hs[1], arW_Hs[0]);
		}

		tv_btn = (TextView) findViewById(R.id.tv_btn);
		tv_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderView.left_img_ID:
			onBackPressed();
			break;
		case HeaderView.rightimg_ID:
			startActivity(new Intent(this,
					HappySmallWritingRuleActivity.class));
			overTransition(2);
			break;
		case R.id.tv_btn:
			if (Constants.GetResult_AuthToken(this).length() == 0) {
				mDialog.initTwoBtnDialog(false, "下次再说", "马上去", "提示",
						"您需要登录才能参加此活动！", new OnClickListener() {

							@Override
							public void onClick(View v) {
								switch (v.getId()) {
								case DialogManager.CANCEL_BTN:
									mDialog.dismiss();
									break;
								case DialogManager.CONFIRM_BTN:
									mDialog.dismiss();
									AppController.isFromHomeActivity = true;
									Intent intent = new Intent(
											HappySmallWritingMainActivity.this,
											LoginActivity.class);
									startActivity(intent);
									overTransition(2);
									break;

								default:
									break;
								}
							}
						});

			} else {
				startActivity(new Intent(this,
						HappySmallWritingSubmitActivity.class));
				overTransition(2);
			}
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

}
