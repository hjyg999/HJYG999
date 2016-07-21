package com.md.hjyg.activities;

import com.md.hjyg.R;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 服务条款页面
 * 
 */
public class RegisterServerItemActivity extends BaseActivity implements OnClickListener{

	private WebView server_item;
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_registerserveritem);
		HeaderViewControler.setHeaderView(this, "服务条款", this);
		server_item = (WebView) findViewById(R.id.serveritem_details_webview);
		server_item.getSettings().setJavaScriptEnabled(false);

		server_item.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				RegisterServerItemActivity.this.setProgress(progress * 1000);
			}
		});
		server_item.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				Toast.makeText(RegisterServerItemActivity.this, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
			}
		});
		server_item.loadUrl("file:///android_asset/serveritem.html");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			this.finish();
			overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
			break;

		}
	}
	
	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.trans_lift_in, R.anim.trans_right_out);
	}

}
