package com.md.hjyg.utility;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.activities.AccountVerify;
import com.md.hjyg.activities.BankAccountDetailsActivity;
import com.md.hjyg.activities.ChangeDealPasswordActivity;
import com.md.hjyg.activities.ChangePasswordNewActivity;
import com.md.hjyg.activities.HomeFragmentActivity;
import com.md.hjyg.activities.PersonalInformationActivity;
import com.md.hjyg.activities.RecommendedTwoDimensionalActivity;
import com.md.hjyg.entity.LoanModel;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.services.DataFetchService;
import com.md.hjyg.utils.AppUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * ClassName: SlidingMenuManager date: 2016-3-22 下午7:17:20 remark: 侧滑管理
 * 
 * @author pyc
 */
public class SlidingMenuManager implements OnClickListener {

	private LinearLayout mSlidingView;
	private HomeFragmentActivity mActivity;
	private DataFetchService dft;
	private LoanModel account_info_details;
	private CircularImage head_sex;
	private TextView tv_usename, tv_name;
	private String usename, name, mobile_no;
	private LinearLayout lin_info, lin_bankaccount, lin_editPassword,
			lin_editDealword, lin_gestureLockEdited, lin_on_off,lin_signOut,line_versionUpdate;
	private Intent intent;
	private View view_line;
	private ImageView gesture_lock_on_off,img_recommand_qrcode;
	private TextView tv_mobile,tv_version;

	public SlidingMenuManager(LinearLayout mSlidingView,
			HomeFragmentActivity mActivity) {
		this.mSlidingView = mSlidingView;
		this.mActivity = mActivity;
		dft = mActivity.getDft();
		findView();
		if (Constants.GetResult_AuthToken(mActivity).length() > 0) {
			GetWebserviceAccountInformationAPI();
		}
	}

	private void findView() {
		head_sex = (CircularImage) mSlidingView.findViewById(R.id.head_sex);
		tv_usename = (TextView) mSlidingView.findViewById(R.id.tv_usename);
		tv_name = (TextView) mSlidingView.findViewById(R.id.tv_name);

		lin_info = (LinearLayout) mSlidingView.findViewById(R.id.lin_info);
		lin_info.setOnClickListener(this);

		lin_bankaccount = (LinearLayout) mSlidingView
				.findViewById(R.id.lin_bankaccount);
		lin_bankaccount.setOnClickListener(this);

		lin_editPassword = (LinearLayout) mSlidingView
				.findViewById(R.id.lin_editPassword);
		lin_editPassword.setOnClickListener(this);

		lin_editDealword = (LinearLayout) mSlidingView
				.findViewById(R.id.lin_editDealword);
		lin_editDealword.setOnClickListener(this);

		lin_gestureLockEdited = (LinearLayout) mSlidingView
				.findViewById(R.id.lin_gestureLockEdited);
		view_line = mSlidingView.findViewById(R.id.view_line);
		gesture_lock_on_off = (ImageView) mSlidingView
				.findViewById(R.id.gesture_lock_on_off);
		lin_gestureLockEdited.setOnClickListener(this);

		setGesture();
		lin_on_off = (LinearLayout) mSlidingView.findViewById(R.id.lin_on_off);
		lin_on_off.setOnClickListener(this);

		tv_mobile = (TextView) mSlidingView.findViewById(R.id.tv_mobile);

		lin_signOut = (LinearLayout) mSlidingView.findViewById(R.id.lin_signOut);
		lin_signOut.setOnClickListener(this);
		img_recommand_qrcode = (ImageView) mSlidingView.findViewById(R.id.img_recommand_qrcode);
		img_recommand_qrcode.setOnClickListener(this);
		
		line_versionUpdate = (LinearLayout) mSlidingView.findViewById(R.id.line_versionUpdate);
		line_versionUpdate.setOnClickListener(this);
		
		tv_version = (TextView) mSlidingView.findViewById(R.id.tv_version);
		String verStr = AppUtil.getVerName(mActivity);
		tv_version.setText(verStr);
	}

	/** 初始化手势密码布局 */
	public void setGesture() {
		// 初始化手势密码布局
		if (Constants.GetGestureLockisOpend(mActivity)
				&& Constants.GetGestureLockPassword(mActivity).length() > 0) {
			gesture_lock_on_off.setImageResource(R.drawable.check_switch_on);
			lin_gestureLockEdited.setVisibility(View.VISIBLE);
			view_line.setVisibility(View.VISIBLE);
		} else {
			gesture_lock_on_off.setImageResource(R.drawable.check_switch_off);
			lin_gestureLockEdited.setVisibility(View.GONE);
			view_line.setVisibility(View.GONE);
		}
	}

	/**
	 * 开启和关闭手势密码
	 */
	public void openoroffGesture() {
		intent = new Intent(mActivity, AccountVerify.class);
		if (Constants.GetGestureLockisOpend(mActivity)
				&& Constants.GetGestureLockPassword(mActivity).length() > 0) {
			intent.putExtra("iscancel", true);
		} else {
		}
		mActivity.startActivity(intent);
		mActivity.overTransition(2);
	}

	public void GetWebserviceAccountInformationAPI() {

		dft.getAccounInfoDetails(Constants.GetAccounInfo_URL,
				Request.Method.GET, new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						account_info_details = (LoanModel) dft
								.GetResponseObject(response, LoanModel.class);
						setUIData();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// Show error or whatever...
					}
				});
	}

	private void setUIData() {
		String savefilename = account_info_details.RegName.toString() + ".jpg";
		// 判断是否已经设置了头像
		Bitmap image = null;
		if (Save.isSaveBitmap(savefilename)) {
			image = Save.getBitmap(savefilename);
		} else {// 没有设置，用默认的图片
			image = BitmapFactory.decodeResource(mActivity.getResources(),
					R.drawable.headimg);
		}
		head_sex.setImageBitmap(image);

		if (account_info_details.Member.RealName != null
				&& account_info_details.Member.RealName.length() > 0) {
			usename = account_info_details.Member.RealName;
		} else if (account_info_details.Member.RegName != null
				&& account_info_details.Member.RegName.length() > 0) {
			usename = account_info_details.Member.RegName;
		} else {
			usename = "";
		}

		name = account_info_details.Member.RegName;
		mobile_no = account_info_details.MobilePhone;

		tv_usename.setText(usename);
		tv_name.setText(name);

		if (!account_info_details.MobileValidate) {
			tv_mobile.setText("未认证");
		} else {
			tv_mobile.setText(Constants.NewreplacePhoneNumberformat(mobile_no));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin_info://

			intent = new Intent(mActivity, PersonalInformationActivity.class);
			mActivity.startActivity(intent);
			mActivity.overTransition(2);

			break;
		case R.id.lin_bankaccount:
			intent = new Intent(mActivity, BankAccountDetailsActivity.class);
			mActivity.startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.lin_editPassword:
//			intent = new Intent(mActivity, ChangePasswordActivity.class);
			intent = new Intent(mActivity, ChangePasswordNewActivity.class);
			mActivity.startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.lin_editDealword:
			if (account_info_details == null) {
				Toast.makeText(mActivity, "数据正在加载，请稍候！", Toast.LENGTH_LONG)
						.show();
				return;
			}
			intent = new Intent(mActivity, ChangeDealPasswordActivity.class);
			intent.putExtra("mobile_no", mobile_no);
			intent.putExtra("ChangeDealPwdDes",
					account_info_details.ChangeDealPwdDes);
			intent.putExtra("ConfigeDealPwdDes",
					account_info_details.ConfigeDealPwdDes);
			mActivity.startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.lin_gestureLockEdited:
			mActivity.startActivity(new Intent(mActivity, AccountVerify.class));
			mActivity.overTransition(2);

			break;
		case R.id.lin_on_off:
			openoroffGesture();
			break;
		case R.id.lin_signOut:
			mActivity.getWebManager().LoginOutService();
			break;
		case R.id.img_recommand_qrcode:
			intent = new Intent(mActivity, RecommendedTwoDimensionalActivity.class);
			mActivity.startActivity(intent);
			mActivity.overTransition(2);
			break;
		case R.id.line_versionUpdate:
			mActivity.getWebManager().updateVersionWebservice(true);
			break;

		default:
			break;
		}
	}

	public void onRestart() {
		setGesture();
		if (Constants.GetResult_AuthToken(mActivity).length() > 0) {
			GetWebserviceAccountInformationAPI();
		}else {
			
		}
	}

}
