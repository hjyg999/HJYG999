package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.SelectPopupWindow;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ClassName: RecommendedShareActivity date: 2015-12-7 下午2:37:29
 * remark:理财师--邀请好友界面
 * 
 * @author pyc
 */
public class RecommendedShareActivity extends BaseActivity implements
		OnClickListener {
	private ImageView head_img;
	private TextView tv_sharebtn;
	/** 判断连接是否生成 */
	private boolean isover_referrer;
	/** 自定义的弹出分享界面 */
	private SelectPopupWindow menuWindow;
	private UmengShareManager umengShareManager;
	/** 0元消费节规则说明 */
//	private String ZeroRuleRewardDes;
	private HeaderView mheadView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommendedshare_activity);
		findViewandInit();
	}

	private void findViewandInit() {
		umengShareManager = new UmengShareManager(this, mHandler);
		// 设置头部标题栏
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "邀请好友赚钱", "");
		mheadView.setRightImg(R.drawable.rec_right_img);
		head_img = (ImageView) findViewById(R.id.head_img);
		ViewParamsSetUtil.setViewParams(head_img, 720, 1330, true);

		tv_sharebtn = (TextView) findViewById(R.id.tv_sharebtn);
		// int Amount = getIntent().getIntExtra("Amount", 1000);
		// condition.setText("所推荐好友至少投资单笔"+Amount+"元3个月及以上,\n推荐者方能获得推荐奖励。");
		tv_sharebtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_top_left:
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
			break;
		case R.id.img_top_right:
			shareMenuWindow();
			break;
		case R.id.tv_sharebtn:
		case R.id.weixin:
			if (umengShareManager != null) {
				umengShareManager.ShareWenXin();
			}
			break;
		case R.id.weixin_frends:
			umengShareManager.ShareWenXinfrends();
			break;
		case R.id.qq:
			umengShareManager.ShareQQ();
			break;
		case R.id.qqzone:
			umengShareManager.ShareQQzone();
			break;
		case R.id.sina:
			umengShareManager.ShareSina();
			break;
		case R.id.sms:
			umengShareManager.ShareSms();
			break;
		case R.id.email:
			umengShareManager.ShareEmail();
			break;
		case R.id.copy:
			umengShareManager.copyLinkUrl();
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

	/** 弹出分享界面及相关操作 */
	private void shareMenuWindow() {

		if (isover_referrer) {
			// 实例化SelectPopupWindow
			menuWindow = new SelectPopupWindow(this, this, false);
			// 显示窗口 设置layout在PopupWindow中显示的位置
			menuWindow.showAtLocation(this.findViewById(R.id.main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			ScreenUtils.backgroundAlpha(this, 0.3f);
			// 分享窗口消失后，恢复背景颜色
			menuWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					ScreenUtils.backgroundAlpha(RecommendedShareActivity.this,
							1.0f);
				}
			});
		} else {
			Toast.makeText(this, "您的专属连接正在生成，请稍后", Toast.LENGTH_SHORT).show();
			// 判断是否已经登录
			if (umengShareManager == null
					&& !Constants.GetResult_AuthToken(this).isEmpty()) {
				umengShareManager = new UmengShareManager(this, mHandler);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				isover_referrer = true;
//				CaptchaModel captchaModel = umengShareManager.getCaptchaModel();
				break;
			default:
				break;
			}
		};
	};

	// /**
	// * 设置添加屏幕的背景透明度
	// *
	// * @param bgAlpha
	// * 透明度取值范围： 0.0f-1.0f
	// */
	// public void backgroundAlpha(float bgAlpha) {
	// WindowManager.LayoutParams lp = getWindow().getAttributes();
	// lp.alpha = bgAlpha; // 0.0-1.0
	// getWindow().setAttributes(lp);
	// }

}
