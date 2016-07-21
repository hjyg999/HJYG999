package com.md.hjyg.utility;

import java.io.IOException;

import org.json.JSONObject;

import com.md.hjyg.entity.CaptchaModel;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.xinTouConstant.Constants;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 有盟分享管理类
 */
public class UmengShareManager {

	/** 首先在您的Activity中添加如下成员变量 */
	private UMSocialService mController;
	/** 分享类型 */
	public static final SHARE_MEDIA[] postSharetypes = { SHARE_MEDIA.WEIXIN,
			SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
			SHARE_MEDIA.SINA, SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL };

	/** 分享内容 */
	private String ShareContent = "100起投，年化利率6%-15%，100%本金收益保障";

	/** 分享标提 */
	private String ShareTitle = "信投宝-新人注册投资送5000元体验金！互联网金融服务平台！";

	/** 分享图片 */
	private Bitmap bitmap;

	/** 推荐人二维码图片 */
	private Bitmap twodimensioncode_bitmap;

	/** 分享连接，如推荐人专属连接 */
	private String linkUrl;

	private DataFetchService dft;
	private CaptchaModel captchaModel;

	private Activity activity;
	private Handler handler;

	/** 微信注册ID和Secret */
	public static String weixinappID = "wxc1f51d79a4dba8f1";
	public static String weixinappSecret = "642d333b7da9aae28b91a322afbce861";

	/** 腾讯APPID和AppKey */
	public static String qqAppID = "1104834619";
	public static String qqAppKey = "s6AP9WIdRgW67gjJ";
	private boolean isimgUrl;
	private String imgUrl;

	public UmengShareManager(Activity mactivity, Handler handler) {
		this.activity = mactivity;
		this.handler = handler;
		dft = new DataFetchService(mactivity);
		try {
			bitmap = BitmapFactory.decodeStream(mactivity.getResources()
					.getAssets().open("applogo.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getQRCode();

	}

	public UmengShareManager(Activity mactivity) {
		this.activity = mactivity;
		dft = new DataFetchService(mactivity);
		try {
			bitmap = BitmapFactory.decodeStream(mactivity.getResources()
					.getAssets().open("applogo.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 设置分享内容
	 * 
	 * @param linkUrl
	 *            分享连接
	 * @param bitmap
	 *            分享图片
	 * @param ShareContent
	 *            分享内容
	 * @param ShareTitle
	 *            分享标题
	 */
	public void setShareInit(String linkUrl, Bitmap bitmap,
			String ShareContent, String ShareTitle) {
		this.linkUrl = linkUrl;
		this.bitmap = bitmap;
		this.ShareContent = ShareContent;
		this.ShareTitle = ShareTitle;
		mController = UMServiceFactory.getUMSocialService(linkUrl);
	}

	/**
	 * 
	 * @param linkUrl  分享连接
	 * @param imgUrl  图片网址
	 * @param ShareContent 分享内容
	 * @param ShareTitle 分享标题
	 */
	public void setShareInit(String linkUrl, String imgUrl,
			String ShareContent, String ShareTitle,boolean isimgUrl) {
		this.linkUrl = linkUrl;
		this.imgUrl = imgUrl;
		this.ShareContent = ShareContent;
		this.ShareTitle = ShareTitle;
		this.isimgUrl = true;
		mController = UMServiceFactory.getUMSocialService(linkUrl);
	}

	/** 获取二维码和专属连接 */
	public void getQRCode() {

		dft.getQRCode(Constants.GetMemberQRCode_URL, Request.Method.GET,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject jsonObject) {

						captchaModel = (CaptchaModel) dft.GetResponseObject(
								jsonObject, CaptchaModel.class);
						byte[] bytes = Base64.decode(captchaModel.QrCode,
								Base64.DEFAULT);
						twodimensioncode_bitmap = BitmapFactory
								.decodeByteArray(bytes, 0, bytes.length);
						linkUrl = captchaModel.linkUrl;
						ShareTitle = captchaModel.ShareTittle;
						ShareContent = captchaModel.ShareContent;
						if (handler != null) {
							mController = UMServiceFactory
									.getUMSocialService(linkUrl);
							handler.sendEmptyMessage(1);
						}
					}
				}, null);

	}

	/** 初始化分享内容 */
	public void inintShareContent() {

	}

	/** 分享到微信好友 */
	public void ShareWenXin() {

		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, weixinappID,
				weixinappSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, weixinappID,
				weixinappSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(ShareContent);
		// 设置title
		weixinContent.setTitle(ShareTitle);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(linkUrl);
		// 设置分享图片
		UMImage arg0 = getUMImage();
		weixinContent.setShareImage(arg0);
		mController.setShareMedia(weixinContent);
		postShare(0);

	}

	/** 分享到微信朋友圈 */
	public void ShareWenXinfrends() {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, weixinappID,
				weixinappSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, weixinappID,
				weixinappSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(ShareContent);
		// 设置朋友圈title
		circleMedia.setTitle(ShareTitle);

		UMImage arg0 = getUMImage();
		circleMedia.setShareImage(arg0);
		circleMedia.setTargetUrl(linkUrl);
		mController.setShareMedia(circleMedia);

		postShare(1);
	}

	/** 分享到QQ好友 */
	public void ShareQQ() {
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, qqAppID,
				qqAppKey);
		qqSsoHandler.addToSocialSDK();

		QQShareContent qqShareContent = new QQShareContent();
		// 设置分享文字
		qqShareContent.setShareContent(ShareContent);
		// 设置分享title
		qqShareContent.setTitle(ShareTitle);
		// 设置分享图片
		qqShareContent.setShareImage(getUMImage());
		// 设置点击分享内容的跳转链接
		qqShareContent.setTargetUrl(linkUrl);
		mController.setShareMedia(qqShareContent);
		postShare(2);
	}

	/** 分享到QQ空间 */
	public void ShareQQzone() {
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				qqAppID, qqAppKey);
		qZoneSsoHandler.addToSocialSDK();

		QZoneShareContent qzone = new QZoneShareContent();
		// 设置分享文字
		qzone.setShareContent(ShareContent);
		// 设置点击消息的跳转URL
		qzone.setTargetUrl(linkUrl);
		// 设置分享内容的标题
		qzone.setTitle(ShareTitle);
		// 设置分享图片
		qzone.setShareImage(getUMImage());
		mController.setShareMedia(qzone);

		postShare(3);
	}

	/** 邮件分享 */
	public void ShareEmail() {

		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
		// 设置分享内容
		mController.setShareContent(ShareTitle + linkUrl);

		postShare(6);
	}

	/** 短信分享 */
	public void ShareSms() {
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		// 设置分享内容
		mController.setShareContent(ShareTitle + linkUrl);

		postShare(5);
	}

	/** 分享到新浪微博 */
	public void ShareSina() {
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.setShareContent(ShareTitle + linkUrl);
		SinaShareContent shareContent = new SinaShareContent();
		shareContent.setTitle(ShareTitle);
		shareContent.setShareContent(ShareTitle + ShareContent + " " + linkUrl);
		shareContent.setShareImage(getUMImage());
		mController.setShareMedia(shareContent);

		postShare(4);
	}

	/** 复制链接到粘贴板 */
	public void copyLinkUrl() {
		ClipboardManager clipboardManager = (ClipboardManager) activity
				.getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData clipData = ClipData.newPlainText("linkUrl", ShareTitle
				+ linkUrl);
		clipboardManager.setPrimaryClip(clipData);
		Toast.makeText(activity, "您的推荐人专属连接已经复制到粘贴板上", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 自定义分享面板分享调用 indx 0：为微信好友 1：为微信朋友圈 2:qq; 3:qq 空间 4:新浪微博, 5:短信, 6:邮件
	 * */
	private void postShare(int indx) {
		// 关闭友盟自带的提示
		mController.getConfig().closeToast();
		// mController.postShare 里面第二个参数必须用activity,不然邮件，微博分享不会成功
		mController.postShare(activity, postSharetypes[indx],
				new SnsPostListener() {
					@Override
					public void onStart() {
						// Toast.makeText(mcontext, "正在努力加载，请耐心等待",
						// Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onComplete(SHARE_MEDIA platform, int eCode,
							SocializeEntity entity) {
						if (eCode == 200) {
							// Toast.makeText(mcontext, "分享成功.",
							// Toast.LENGTH_SHORT).show();
						} else {
							String eMsg = "";
							if (eCode == -101) {
								eMsg = "没有授权";
							}
							Toast.makeText(activity,
									"分享失败[" + eCode + "] " + eMsg,
									Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

	private UMImage getUMImage() {
		if (isimgUrl) {
			if (imgUrl != null && imgUrl.length() > 0) {
				return new UMImage(activity, imgUrl);
			} else {
				return new UMImage(activity, bitmap);
			}
		} else {
			return new UMImage(activity, bitmap);
		}
	}

	/** 设置新的分享连接 Url：新的分享连接 */
	public void setNewmController(String Url) {
		mController = UMServiceFactory.getUMSocialService(Url);
	}

	public UMSocialService getmController() {
		return mController;
	}

	public void setmController(UMSocialService mController) {
		this.mController = mController;
	}

	public String getShareContent() {
		return ShareContent;
	}

	public void setShareContent(String shareContent) {
		ShareContent = shareContent;
	}

	public String getShareTitle() {
		return ShareTitle;
	}

	public void setShareTitle(String shareTitle) {
		ShareTitle = shareTitle;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public Bitmap getTwodimensioncode_bitmap() {
		return twodimensioncode_bitmap;
	}

	public void setTwodimensioncode_bitmap(Bitmap twodimensioncode_bitmap) {
		this.twodimensioncode_bitmap = twodimensioncode_bitmap;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public CaptchaModel getCaptchaModel() {
		return captchaModel;
	}

	public void setCaptchaModel(CaptchaModel captchaModel) {
		this.captchaModel = captchaModel;
	}
}
