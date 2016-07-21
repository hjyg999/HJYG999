package com.md.hjyg.activities;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.FinancialPlannerModel;
import com.md.hjyg.layoutEntities.CircularImage;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * 理财师
 */
public class RecommendedActivity extends BaseActivity implements
		View.OnClickListener {

	private LinearLayout lin_top, recommand_request_ll, recommand_red_ll,
			recommand_qrcode_ll, recommand_rule_tv;
	private Intent intent;
	private FinancialPlannerModel model;
	private TextView tv_sharehit, recaward_get_yes_tv, recaward_get_no_tv,
			recaward_reg_sum_tv, recommonder_levele_tv, recommonder_des_tv;
	private ImageView img_friend, img_share, img_twodimensional, img_bottom,
			img_arrow1, img_arrow2, img_arrow3, recommonder_levele_im;
	private RelativeLayout recaward_get_yes_rl, recaward_get_no_rl,
			recaward_reg_sum_rl;
	private CircularImage head_sex;
	private String savefilename;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_recommeded);
		findViewandInit();
		getFinancialPlannerServices();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "理财师", this);

		lin_top = (LinearLayout) findViewById(R.id.lin_top);
		ViewParamsSetUtil.setViewParams(lin_top, 720, 360, true);

		recommand_request_ll = (LinearLayout) findViewById(R.id.recommand_request_ll);
		recommand_request_ll.setOnClickListener(this);
		recommand_red_ll = (LinearLayout) findViewById(R.id.recommand_red_ll);
		recommand_red_ll.setOnClickListener(this);
		recommand_qrcode_ll = (LinearLayout) findViewById(R.id.recommand_qrcode_ll);
		recommand_qrcode_ll.setOnClickListener(this);

		recommand_rule_tv = (LinearLayout) findViewById(R.id.recommand_rule_tv);
		recommand_rule_tv.setOnClickListener(this);

		recaward_get_yes_rl = (RelativeLayout) findViewById(R.id.recaward_get_yes_rl);
		recaward_get_yes_rl.setOnClickListener(this);
		recaward_get_no_rl = (RelativeLayout) findViewById(R.id.recaward_get_no_rl);
		recaward_get_no_rl.setOnClickListener(this);
		recaward_reg_sum_rl = (RelativeLayout) findViewById(R.id.recaward_reg_sum_rl);
		recaward_reg_sum_rl.setOnClickListener(this);

		recaward_get_yes_tv = (TextView) findViewById(R.id.recaward_get_yes_tv);
		recaward_get_no_tv = (TextView) findViewById(R.id.recaward_get_no_tv);
		recaward_reg_sum_tv = (TextView) findViewById(R.id.recaward_reg_sum_tv);
		recommonder_levele_tv = (TextView) findViewById(R.id.recommonder_levele_tv);
		recommonder_des_tv = (TextView) findViewById(R.id.recommonder_des_tv);

		tv_sharehit = (TextView) findViewById(R.id.tv_sharehit);

		int[] w_h_a = Save.getScaleBitmapWangH(16, 29);
		img_arrow1 = (ImageView) findViewById(R.id.img_arrow1);
		ViewParamsSetUtil.setViewHandW_rel(img_arrow1, w_h_a[1], w_h_a[0]);
		img_arrow2 = (ImageView) findViewById(R.id.img_arrow2);
		ViewParamsSetUtil.setViewHandW_rel(img_arrow2, w_h_a[1], w_h_a[0]);
		img_arrow3 = (ImageView) findViewById(R.id.img_arrow3);
		ViewParamsSetUtil.setViewHandW_rel(img_arrow3, w_h_a[1], w_h_a[0]);

		int[] w_h = Save.getScaleBitmapWangH(68, 68);
		img_friend = (ImageView) findViewById(R.id.img_friend);
		ViewParamsSetUtil.setViewHandW_lin(img_friend, w_h[1], w_h[0]);
		img_share = (ImageView) findViewById(R.id.img_share);
		ViewParamsSetUtil.setViewHandW_lin(img_share, w_h[1], w_h[0]);
		img_twodimensional = (ImageView) findViewById(R.id.img_twodimensional);
		ViewParamsSetUtil.setViewHandW_lin(img_twodimensional, w_h[1], w_h[0]);
		img_bottom = (ImageView) findViewById(R.id.img_bottom);
		ViewParamsSetUtil.setViewParams(img_bottom, 720, 630, true);
		recommonder_levele_im = (ImageView) findViewById(R.id.recommonder_levele_im);
		ViewParamsSetUtil.setViewParams(recommonder_levele_im, 48, 48, true);

		head_sex = (CircularImage) findViewById(R.id.head_sex);
		savefilename = Constants.GetSaveFilename(this);
		// 判断是否已经设置了头像
		if (Save.isSaveBitmap(savefilename)) {
			Bitmap image = Save.getBitmap(savefilename);
			head_sex.setImageBitmap(image);
			head_sex.setVisibility(View.VISIBLE);
		} else {// 没有设置，用默认的图片
			// head_img.setImageResource(R.drawable.circle_head_mr);
			head_sex.setVisibility(View.GONE);
		}

	}

	/** 获取用户统计数据 */
	private void getFinancialPlannerServices() {
		dft.getNetInfoById(Constants.FinancialPlanner_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject jsonObject) {
						model = (FinancialPlannerModel) dft.GetResponseObject(
								jsonObject, FinancialPlannerModel.class);
						if (model != null) {
							recaward_get_no_tv.setText(Constants
									.StringToCurrency(model.UncollectedReward));
							recaward_get_yes_tv.setText(Constants
									.StringToCurrency(model.ReceivedReward));
							recaward_reg_sum_tv
									.setText(model.OneLevelRegisterTotal + "人");
							tv_sharehit.setText(model.ShareExpDes);

							recommonder_levele_tv.setText(model.RealName);
							recommonder_des_tv
									.setText(model.RewardPercent.name);
							/** 显示的理财师图片ID */
							int levele_im_id = 0;
							switch (model.RewardPercent.Id) {
							case 3:// 银牌理财师/互联网理财师
								levele_im_id = R.drawable.rec_silver_level;
								break;
							case 2:// 员工
								levele_im_id = R.drawable.rec_com_level;
								break;
							case 4:// 金牌理财师
								levele_im_id = R.drawable.rec_gold_level;
								break;
							case 5:// 商户
								levele_im_id = R.drawable.rec_sh_level;
								break;

							default:
								levele_im_id = R.drawable.rec_pt_level;
								break;
							}
							recommonder_levele_im
									.setImageResource(levele_im_id);
							// recommonder_levele_im.setImageBitmap(Save
							// .ScaleBitmap(BitmapFactory.decodeResource(
							// getResources(), levele_im_id),
							// mcontext, 22));
						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.recommand_request_ll:// 邀请好友
			if (model == null) {
				Toast.makeText(this, "数据正在加载，请稍后", Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(this, RecommendedShareActivity.class);
//			if (model.MemberFinancialPlannerInfo.ThreeLevelInfo != null) {
//
//				intent.putExtra(
//						"Amount",
//						model.MemberFinancialPlannerInfo.ThreeLevelInfo.FriendsSingleBuyAmount);
//			}
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.recommand_red_ll:// 分享现金红包
			// intent = new Intent(this, ShareWeixinRedPacketActivity.class);
			intent = new Intent(this, HuoQiExperiencegoldShareActivity.class);
//			if (model.MemberFinancialPlannerInfo.ThreeLevelInfo != null) {
//
//				intent.putExtra(
//						"Amount",
//						model.MemberFinancialPlannerInfo.ThreeLevelInfo.FriendsSingleBuyAmount);
//			}
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.recommand_qrcode_ll:// 我的二维码
			intent = new Intent(this, RecommendedTwoDimensionalActivity.class);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.recaward_get_yes_rl:// 已收奖励
			intent = new Intent(this, RecommendedRewardActivity.class);
			intent.putExtra("awardType", 1);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.recaward_get_no_rl:// 待收奖励
			intent = new Intent(this, RecommendedRewardActivity.class);
			intent.putExtra("awardType", 2);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.recaward_reg_sum_rl:// 推荐好友
			intent = new Intent(this, RecommendMemberStatisticsActivity.class);
			intent.putExtra("awardType", 2);
			startActivity(intent);
			overTransition(2);
			break;
		case R.id.recommand_rule_tv:// 活动规则
			intent = new Intent(this, RecommendedRuleActivity.class);
			intent.putExtra("awardType", 2);
			startActivity(intent);
			overTransition(2);
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
