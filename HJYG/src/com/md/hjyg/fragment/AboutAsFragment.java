package com.md.hjyg.fragment;

import java.util.Date;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.AboutXintouActivity;
import com.md.hjyg.activities.CustomerServiceActivity;
import com.md.hjyg.activities.FeedbackActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.NewsNoticeActivity;
import com.md.hjyg.activities.SplashScreen;
import com.md.hjyg.entity.VersionDetails;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utils.AppUtil;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.UpdateVersionManager;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: AboutAsFragment date: 2016-2-3 上午9:06:19 remark:更多-关于我们
 * 
 * @author pyc
 */
public class AboutAsFragment extends Fragment implements OnClickListener {

	private DataFetchService dft;
	private HomeFragmentActivity mActivity;

	private RelativeLayout rl_welcome_page, rl_about_us, rl_feedback,
			rl_version_update, rl_customer_service, rl_news_notice;

	/**
	 * 版本更新图标
	 * */
	private ImageView about_us_new_im;;
	private TextView tv_version_update_value, tv_hotline_service;

	private Intent intent;

	private VersionDetails versionDetails = null;
	private String official_phone = "4000781901";
	private String phone = "400-078-1901";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_aboutas, container, false);
		findView(v);
		init();

		return v;
	}

	private void findView(View v) {
		// 公告
		rl_news_notice = (RelativeLayout) v.findViewById(R.id.rl_news_notice);
		rl_news_notice.setOnClickListener(this);
		// 欢迎页
		rl_welcome_page = (RelativeLayout) v.findViewById(R.id.rl_welcome_page);
		rl_welcome_page.setOnClickListener(this);
		// 关于信投
		rl_about_us = (RelativeLayout) v.findViewById(R.id.rl_about_us);
		rl_about_us.setOnClickListener(this);
		// 意见反馈
		rl_feedback = (RelativeLayout) v.findViewById(R.id.rl_feedback);
		rl_feedback.setOnClickListener(this);
		// 版本更新
		rl_version_update = (RelativeLayout) v
				.findViewById(R.id.rl_version_update);
		rl_version_update.setOnClickListener(this);
		// 版本更新图标
		about_us_new_im = (ImageView) v.findViewById(R.id.about_us_new_im);
		// 版本号
		tv_version_update_value = (TextView) v
				.findViewById(R.id.tv_version_update_value);
		// 联系我们
		rl_customer_service = (RelativeLayout) v
				.findViewById(R.id.rl_customer_service);
		rl_customer_service.setOnClickListener(this);
		// 电话说明
		tv_hotline_service = (TextView) v.findViewById(R.id.tv_hotline_service);
		tv_hotline_service.setOnClickListener(this);

	}

	/**
	 * 初始化
	 */
	private void init() {
		mActivity = (HomeFragmentActivity) getActivity();
		dft = mActivity.getDft();

		String verStr = AppUtil.getVerName(mActivity);
		tv_version_update_value.setText("已更新至版本" + verStr);
		tv_hotline_service.setText(phone + "  365天<9:00-21:00>");
		about_us_new_im.setVisibility(View.GONE);

		updateVersionWebservice(1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_news_notice:// 公告
			intent = new Intent(mActivity, NewsNoticeActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_welcome_page:// 欢迎页
			intent = new Intent(mActivity, SplashScreen.class);
			intent.putExtra("FromWelcome", "AboutUs");
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_about_us:// 关于信投
			intent = new Intent(mActivity, AboutXintouActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_feedback:// 意见反馈
			intent = new Intent(mActivity, FeedbackActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.rl_version_update:// 版本更新
			updateVersionWebservice(0);
			break;
		case R.id.rl_customer_service:// 联系我们
			intent = new Intent(mActivity, CustomerServiceActivity.class);
			startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.tv_hotline_service:// 电话说明
			Constants.showPopup(mActivity, "您是否要拨打我们的官方电话：" + phone + " ?",
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent();
							intent.setAction(Intent.ACTION_CALL);
							intent.setData(Uri.parse("tel:" + official_phone));
							// 开启系统拨号器
							startActivity(intent);
							if (Constants.dialog != null
									&& Constants.dialog.isShowing()) {
								Constants.dialog.dismiss();
							}
						}
					});

			break;

		default:
			break;
		}
	}

	/**
	 * 版本更新接口
	 */
	private void updateVersionWebservice(final int isShowIcon) {
		String time = DateUtil
				.dateToStr(new Date(), DateUtil.YYYYMMDD_HH_MM_SS);
		String imei = AppUtil.getIMEI(mActivity);
		String appversion = AppUtil.getVerName(mActivity);
		// if (versionDetails != null) {
		// setupdateVersion(isShowIcon);
		// return;
		// }
		dft.updateVersion(time, imei, Constants.IDENTIFY, appversion,
				Constants.UpdateVersion_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							versionDetails = (VersionDetails) dft
									.GetResponseObject(response,
											VersionDetails.class);
							setupdateVersion(isShowIcon);
						} catch (Exception e) {
							Toast.makeText(getActivity(), getString(R.string.data_error), Toast.LENGTH_SHORT).show();
						}
					}
				}, null);

	}

	private void setupdateVersion(int isShowIcon) {
		String status_message = versionDetails.Notification.ProcessMessage
				.toString();
		int status = versionDetails.Notification.ProcessResult;
		if (status == 1) {
			if (versionDetails.upgrade != null
					&& versionDetails.upgrade.equals("true")) {
				if (isShowIcon == 1) {
					about_us_new_im.setVisibility(View.VISIBLE);
				} else {
					UpdateVersionManager updateVersionManager = new UpdateVersionManager(
							mActivity, versionDetails.downLoadUrl,
							versionDetails);
					updateVersionManager.checkUpdateInfo();
				}

			} else {
				if (isShowIcon == 0) {
					String message = "您已经是最新版本了！";
					Constants.showOkPopup(mActivity, message,
							new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if (Constants.dialog != null
											&& Constants.dialog.isShowing()) {
										Constants.dialog.dismiss();
									}
								}
							});
				}
			}
		} else if (status == 0) {
			Constants.showOkPopup(mActivity, status_message,
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (Constants.dialog != null
									&& Constants.dialog.isShowing()) {
								Constants.dialog.dismiss();
							}

						}
					});
		}

	}

}
