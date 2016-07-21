package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.layoutEntities.SelectPopupWindow;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.UmengShareManager;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
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
 * 公告详情
 */
public class NewsNoticeDetailsActivity extends BaseActivity implements OnClickListener{
	
	private WebView newsnotice_webview;
	private TextView newsnotice_title,newsnotice_time;
	private String Contents ="" ,Title = "" ,CreateTime = "";
	/**分享按钮*/
	private TextView news_weixin,news_weixin_frends,news_sinaze,news_more;
	
	/**自定义的弹出分享界面*/  
	private SelectPopupWindow menuWindow;
	
	private UmengShareManager umengShareManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_newsnotice_details);
		
		Contents = getIntent().getStringExtra("Contents");
		Title = getIntent().getStringExtra("Title");
		CreateTime = getIntent().getStringExtra("CreateTime");
		
		init();
		setWebView();
		
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	protected void init()
	{
		HeaderViewControler.setHeaderView(this, "公告详情", this);
		newsnotice_webview = (WebView) findViewById(R.id.newsnotice_webview);
		newsnotice_time = (TextView) findViewById(R.id.newsnotice_time);
		newsnotice_title = (TextView) findViewById(R.id.newsnotice_title);
		
		newsnotice_title.setText(Title);
		newsnotice_time.setText("时间：【" + CreateTime + "】");
		
		//保存读取的最新公告时间已便判断是否有新公告，有新公告在主界面提示
		if (Constants.GetNewsOldTime(this).length() > 0 && 
				DateUtil.newTimeIsAfteroldTime(CreateTime, Constants.GetNewsOldTime(this))) {
			Constants.SetNewsOldTime(CreateTime, this);
		}else if (Constants.GetNewsOldTime(this).length() == 0) {
			Constants.SetNewsOldTime(CreateTime, this);
		}
		
		//分享按钮初始化
		news_weixin = (TextView) findViewById(R.id.news_weixin);
		news_weixin_frends = (TextView) findViewById(R.id.news_weixin_frends);
		news_sinaze = (TextView) findViewById(R.id.news_sinaze);
		news_more = (TextView) findViewById(R.id.news_more);
		
		news_weixin.setOnClickListener(this);
		news_weixin_frends.setOnClickListener(this);
		news_sinaze.setOnClickListener(this);
		news_more.setOnClickListener(this);
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
	
		
		newsnotice_webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				NewsNoticeDetailsActivity.this.setProgress(progress * 1000);
			}
		});
		newsnotice_webview.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(NewsNoticeDetailsActivity.this, "Oh no! " + description,
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
		
		if(v.getId() == HeaderViewControler.ID)
		{//返回
			this.finish();
			overTransition(1);
		}else if (v == news_weixin) {//分享到微信好友
			if (umengShareManager == null) {
				shareMenuWindow(false);
			}else {
				umengShareManager.ShareWenXin();
			}
		}else if (v == news_weixin_frends) {//分享到微信朋友圈
			if (umengShareManager == null) {
				shareMenuWindow(false);
			}else {
				umengShareManager.ShareWenXinfrends();
			}
		}else if (v == news_sinaze) {//分享到新浪微博
			if (umengShareManager == null) {
				shareMenuWindow(false);
			}else {
				umengShareManager.ShareSina();
			}
		}else if (v == news_more) {//点击出现更多分享按钮
			shareMenuWindow(true);
		}
	}
	
	/** 
     * 设置添加屏幕的背景透明度 
     * @param bgAlpha 透明度取值范围： 0.0f-1.0f
     */  
    public void backgroundAlpha(float bgAlpha)  
    {  
        WindowManager.LayoutParams lp = getWindow().getAttributes();  
        lp.alpha = bgAlpha; //0.0-1.0  
        getWindow().setAttributes(lp);  
    }
    
    /** 
     * 初始化umengShareManager 及弹出分享界面及相关操作
     * @param isShowMenu true 弹出更多分享界面
     */
    private void shareMenuWindow(boolean isShowMenu )
    {
		if (Constants.GetResult_AuthToken(this).isEmpty()) {
			Constants.showPopup(this, "您需要登录后才能邀请好友", new View.OnClickListener() {
				
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					AppController.isFromNewsNoticeDetailsActivity = true;
//					Intent account = new Intent(NewsNoticeDetailsActivity.this,
//							LoginActivity.class);
//					startActivity(account);
//				}

				@Override
				public void onClick(View v) {
					AppController.isFromNewsNoticeDetailsActivity = true;
					Intent account = new Intent(NewsNoticeDetailsActivity.this,
							LoginActivity.class);
					startActivity(account);
					if (Constants.dialog != null && Constants.dialog.isShowing()) {
						Constants.dialog.dismiss();
					}
					
				}
			});
		}else {
			if (umengShareManager != null && isShowMenu) {
				//实例化SelectPopupWindow  
				menuWindow = new SelectPopupWindow(this, itemsOnClick,true);  
				//显示窗口  设置layout在PopupWindow中显示的位置
				menuWindow.showAtLocation(this.findViewById(R.id.newsnotice), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); 
				backgroundAlpha(0.3f);
				//分享窗口消失后，恢复背景颜色
				menuWindow.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						backgroundAlpha(1.0f);
						
					}
				});
			}else {
				if (isShowMenu) {
					Toast.makeText(this, "网络繁忙，请稍后", Toast.LENGTH_SHORT).show();
				}
				//判断是否已经登录
				if (umengShareManager == null && !Constants.GetResult_AuthToken(this).isEmpty()) {
					
					umengShareManager = new UmengShareManager(this);
					umengShareManager.setShareInit(null, null, null, null);
				}
			}
			
		}
    }
    
    
    /**分享界面的按钮监听*/
	OnClickListener itemsOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.weixin:
				umengShareManager.ShareWenXin();
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
	};
	
	@Override
	protected void onRestart() {
		super.onRestart();
		shareMenuWindow(false);
	};
	
	@Override
	public void onBackPressed() {
		NewsNoticeDetailsActivity.this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}
}
    
    
