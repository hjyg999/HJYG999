package com.md.hjyg.activities;

import java.io.IOException;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.layoutEntities.SelectPopupWindow;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ClassName: InformationDetailsActivity date: 2016-5-13 上午10:56:13 remark: 资讯详情
 * 
 * @author pyc
 */
public class InformationDetailsActivity extends BaseActivity implements
		OnClickListener {

	private HeaderView mheadView;
	private SelectPopupWindow menuWindow;
	private UmengShareManager umengShareManager;
	private WebView newsnotice_webview;
	private String Contents  ,Title  ,CreateTime,shareUrl,media;
	private TextView newsnotice_title,newsnotice_time,tv_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.informationdetails_layout);
		findViewandInit();
		setWebView();
	}

	private void findViewandInit() {
		// 设置头部标题栏
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "资讯", "");
		mheadView.setRightImg(R.drawable.rec_right_img);
		newsnotice_webview = (WebView) findViewById(R.id.newsnotice_webview);
		Contents = getIntent().getStringExtra("Contents");
		Title = getIntent().getStringExtra("Title");
		CreateTime = getIntent().getStringExtra("CreateTime");
		shareUrl = getIntent().getStringExtra("shareUrl");
		media = getIntent().getStringExtra("media");
		
		newsnotice_title = (TextView) findViewById(R.id.newsnotice_title);
		newsnotice_time = (TextView) findViewById(R.id.newsnotice_time);
		tv_type = (TextView) findViewById(R.id.tv_type);
		
		newsnotice_title.setText(Title);
		newsnotice_time.setText(DateUtil.changto(CreateTime, "yyyy/MM/dd"));
		tv_type.setText(media);
		
		umengShareManager = new UmengShareManager(this);
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getResources().getAssets().open("applogo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		umengShareManager.setShareInit(shareUrl,bitmap, Title, Title);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	protected void setWebView()
	{
		WebSettings webSettings = newsnotice_webview.getSettings();
		webSettings.setJavaScriptEnabled(false);// 设置支持javascript脚本
		webSettings.setCacheMode(WebSettings.LOAD_DEFAULT); ////设置缓存模式
		webSettings.setDomStorageEnabled(true); // 开启 DOM storage API 功能 
		//加上这个属性后。。。html的图片就会适应屏幕，内容将自动缩放
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
		webSettings.setSupportZoom(true); //支持缩放
		webSettings.setLoadWithOverviewMode(true);
	
		
		newsnotice_webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				InformationDetailsActivity.this.setProgress(progress * 1000);
			}
		});
		newsnotice_webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(InformationDetailsActivity.this, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
		newsnotice_webview.loadDataWithBaseURL("about:blank", Contents, "text/html", "utf-8", null);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderView.left_img_ID:
			this.finish();
			overTransition(1);
			break;
		case HeaderView.rightimg_ID://弹出分享框
			shareMenuWindow();
			break;
		case R.id.weixin:
			umengShareManager.ShareWenXin();
			break;
		case R.id.weixin_frends:
			umengShareManager.ShareWenXinfrends();
			break;
		case R.id.qq:
			umengShareManager.ShareQQ();
			break;

		default:
			break;
		}

	}
	
	/** 弹出分享界面及相关操作 */
	private void shareMenuWindow() {
		// 实例化SelectPopupWindow
		menuWindow = new SelectPopupWindow(this, this);
		// 显示窗口 设置layout在PopupWindow中显示的位置
		menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
		ScreenUtils.backgroundAlpha(this, 0.3f);
		// 分享窗口消失后，恢复背景颜色
		menuWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ScreenUtils.backgroundAlpha(InformationDetailsActivity.this,1.0f);
			}
		});
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

}
