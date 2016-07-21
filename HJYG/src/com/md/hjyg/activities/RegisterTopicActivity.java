package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderControler;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utils.JsOperator;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

/** 
 * ClassName: RegisterTopicActivity 
 * date: 2015-12-25 上午9:00:51 
 * remark: 用于加载微信端页面
 * @author pyc
 */
public class RegisterTopicActivity extends BaseActivity implements OnClickListener{
	
	private WebView webView;
	private Intent intent;
	private String linkurl = "";
	private String shortDescription = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_registertopic_activity);
		init();
	}
	
	// 初始化页面
	public void init() {
		this.webView = (WebView) this.findViewById(R.id.webView);  
		intent = getIntent();
		linkurl = intent.getStringExtra("linkurl");
		shortDescription = intent.getStringExtra("shortDescription");
		HeaderViewControler.setHeaderView(this, shortDescription, this);
	    this.initializeWebView();  
	}
	
	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })  
    private void initializeWebView(){  
        webView.addJavascriptInterface(new JsOperator(this),  
                "JsInteraction");  
        try {  
            WebSettings webSettings = webView.getSettings();  
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);  
            webSettings.setUseWideViewPort(true);//关键点
            
            webSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);// 设置html的图片适应屏幕，内容自动缩放
//    		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
//    		webSettings.setSupportZoom(true); //支持缩放
            webView.setWebViewClient(new WebViewClient() {
    			public void onReceivedError(WebView view, int errorCode,
    					String description, String failingUrl) {
    				Toast.makeText(RegisterTopicActivity.this, "Oh no! " + description,
    						Toast.LENGTH_SHORT).show();
    			}
    			@Override
    			public void onReceivedSslError(WebView view,
    					SslErrorHandler handler, SslError error) {
    				handler.proceed();
    			}
    		});
            this.webView.loadUrl(linkurl);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID://返回按钮
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
	protected void onRestart() {
		super.onRestart();
//		if (Constants.GetResult_AuthToken(this).length() > 0) {
//			btn_img.setVisibility(View.GONE);
//		}else {
//			btn_img.setVisibility(View.VISIBLE);
//		}
	}
    
}
