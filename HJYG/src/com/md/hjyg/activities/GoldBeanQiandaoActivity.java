package com.md.hjyg.activities;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.entity.GoldBeanQDInfoModel;
import com.md.hjyg.entity.GoldBeanQDModel;
import com.md.hjyg.layoutEntities.HeaderViewControler;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.AppController;
import com.md.hjyg.utility.DialogManager;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.utils.DateUtil;
import com.md.hjyg.utils.ScreenUtils;
import com.md.hjyg.utils.TextUtil;
import com.md.hjyg.utils.ViewParamsSetUtil;
import com.md.hjyg.xinTouConstant.Constants;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response.Listener;

/**
 * ClassName: GoldBeanQiandaoActivity date: 2016-5-25 下午3:28:38 remark:金豆签到界面
 * 
 * @author pyc
 */
public class GoldBeanQiandaoActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {

	private LinearLayout lin_bg, lin_qiandao, lin_pop;
	private TextView tv_toqiandao, tv_qds[], tv_qdday;
	private ImageView img_qdhit, img_qds[];
	private LinearLayout lin_qds[];
	private int size = 7;
	private String datas[];
	private DialogManager mDialog;
	private DialogProgressManager progressDialog;
	private Timer mTimer;
	/**
	 * 连续签到天数
	 */
	private int ContinuityDay;
	private int[] img_RIDs = { R.drawable.gb_o, R.drawable.gb_1,
			R.drawable.gb_2, R.drawable.gb_3, R.drawable.gb_4, R.drawable.gb_5,
			R.drawable.gb_6, R.drawable.gb_7, R.drawable.gb_7gift };
	private GoldBeanQDInfoModel qdInfo;
	private TextView tv_popmsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goldbeanqiandao_layout);
		findViewandInit();
		getSinginLogsInfo();
	}

	private void findViewandInit() {
		HeaderViewControler.setHeaderView(this, "签到", this);
		mDialog = new DialogManager(this);
		progressDialog = new DialogProgressManager(this, "数据提交中...");

		lin_pop = (LinearLayout) findViewById(R.id.lin_pop);
		lin_bg = (LinearLayout) findViewById(R.id.lin_bg);
		ViewParamsSetUtil.setViewParams(lin_bg, 720, 1134, false);

		lin_qiandao = (LinearLayout) findViewById(R.id.lin_qiandao);
		ViewParamsSetUtil.setViewParams(lin_qiandao, 480, 248, true);

		img_qdhit = (ImageView) findViewById(R.id.img_qdhit);
		ViewParamsSetUtil.setViewParams(img_qdhit, 60, 60, true);

		initQD();

		tv_popmsg = (TextView) findViewById(R.id.tv_popmsg);
		tv_toqiandao = (TextView) findViewById(R.id.tv_toqiandao);
		tv_toqiandao.setOnClickListener(this);
		tv_qdday = (TextView) findViewById(R.id.tv_qdday);

		setQianDaoImg(ContinuityDay);
	}

	/**
	 * 初始化签到圆圈
	 */
	private void initQD() {
		int[] lin_qdsID = { R.id.lin_qd0, R.id.lin_qd1, R.id.lin_qd2,
				R.id.lin_qd3, R.id.lin_qd4, R.id.lin_qd5, R.id.lin_qd6 };
		lin_qds = new LinearLayout[size];

		int[] img_qdsID = { R.id.img_qd0, R.id.img_qd1, R.id.img_qd2,
				R.id.img_qd3, R.id.img_qd4, R.id.img_qd5, R.id.img_qd6 };
		img_qds = new ImageView[size];

		int[] tv_qdsID = { R.id.tv_qd0, R.id.tv_qd1, R.id.tv_qd2, R.id.tv_qd3,
				R.id.tv_qd4, R.id.tv_qd5, R.id.tv_qd6 };
		tv_qds = new TextView[size];

		for (int i = 0; i < lin_qdsID.length; i++) {
			lin_qds[i] = (LinearLayout) findViewById(lin_qdsID[i]);
			img_qds[i] = (ImageView) findViewById(img_qdsID[i]);
			tv_qds[i] = (TextView) findViewById(tv_qdsID[i]);
		}

		int[] w_h = Save.getScaleBitmapWangH(60, 60);
		int mag = (AppController.screenWidth - w_h[0] * 7 - ScreenUtils.dip2px(
				this, 40)) / 6;
		mag = ScreenUtils.px2dip(this, mag);
		LinearLayout.LayoutParams params = (LayoutParams) lin_qds[1]
				.getLayoutParams();
		params.leftMargin = mag;
		for (int i = 1; i < size; i++) {
			lin_qds[i].setLayoutParams(params);
		}

		LinearLayout.LayoutParams params_img = (LayoutParams) img_qds[6]
				.getLayoutParams();
		params_img.height = w_h[0];
		params_img.width = w_h[0];
		for (int i = 0; i < size; i++) {
			img_qds[i].setLayoutParams(params_img);
		}

		// Calendar calendar = Calendar.getInstance();
		// datas = new String[size];
		// for (int i = 0; i < size; i++) {
		// datas[i] = getData(calendar);
		// tv_qds[i].setText(datas[i]);
		// calendar.add(Calendar.DAY_OF_MONTH, 1);
		// }

	}

	/**
	 * 
	 * @param calendar
	 * @return 00/00
	 */
	private String getData(Calendar calendar) {
		String str = "";
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if (month < 10) {
			str = "0" + month;
		} else {
			str = "" + month;
		}
		if (day < 10) {
			str = str + "/0" + day;
		} else {
			str = str + "/" + day;
		}
		return str;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case HeaderViewControler.ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.tv_toqiandao:// 签到
			if (qdInfo != null) {
				getGBSignin();
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

	/**
	 * 获取金豆签到信息
	 */
	private void getSinginLogsInfo() {
		dft.getNetInfoById(Constants.GBGetSinginLogs_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						qdInfo = (GoldBeanQDInfoModel) dft.GetResponseObject(
								josnbject, GoldBeanQDInfoModel.class);
						setDate();
					}
				});
	}

	private void setDate() {
		if (qdInfo != null) {
			if (qdInfo.notification.ProcessResult == 1) {// 有连续签到记录
				ContinuityDay = qdInfo.ContinuityDay;
				if (qdInfo.IsSignin) {// 如果今天已经签到
					setQianDaoData(ContinuityDay - 1);
					lin_qiandao.setBackgroundResource(R.drawable.gb_qiandaoh);
				} else {
					setQianDaoData(ContinuityDay);
					lin_qiandao.setBackgroundResource(R.drawable.gb_qiandao);
				}
				setQianDaoImg(ContinuityDay);
			} else {// 无连续签到记录
				ContinuityDay = 0;
				setQianDaoImg(ContinuityDay);
				setQianDaoData(ContinuityDay);
				lin_qiandao.setBackgroundResource(R.drawable.gb_qiandao);
			}
		}
	}

	/**
	 * 显示签到天数的日期
	 * 
	 * @param day
	 */
	private void setQianDaoData(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtil.getDate());
		calendar.add(Calendar.DAY_OF_MONTH, -day);
		datas = new String[size];
		for (int i = 0; i < size; i++) {
			datas[i] = getData(calendar);
			tv_qds[i].setText(datas[i]);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	/**
	 * 显示签到天数的图片和文字提示
	 * 
	 * @param day
	 */
	private void setQianDaoImg(int day) {
		tv_qdday.setText(TextUtil.getRedString("连续签到" + day + "天", 4,
				3 + (day + "").length() + 1));
		for (int i = 0; i < size; i++) {
			if (i + 1 <= day) {
				img_qds[i].setImageResource(img_RIDs[i + 1]);
				tv_qds[i].setTextColor(getResources().getColor(R.color.glod));
			} else {
				img_qds[i].setImageResource(img_RIDs[0]);
				tv_qds[i]
						.setTextColor(getResources().getColor(R.color.gray_sq));
			}
		}

		if (day == 7) {
			tv_qds[6].setTextColor(getResources().getColor(R.color.glod));
			img_qdhit.setVisibility(View.INVISIBLE);
		} else {
			tv_qds[6].setTextColor(getResources().getColor(R.color.gray_sq));
			img_qdhit.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 点击签到的接口
	 */
	private void getGBSignin() {
		progressDialog.initDialog();
		dft.getNetInfoById(Constants.GBSignin_URL, Request.Method.GET,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject josnbject) {
						progressDialog.dismiss();
						final GoldBeanQDModel model = (GoldBeanQDModel) dft
								.GetResponseObject(josnbject,
										GoldBeanQDModel.class);
						if (model.notification.ProcessResult == 1) {
							String msg = "签到成功，+" + model.SignGiveNumber
									+ "颗金豆。";
							lin_qiandao.setBackgroundResource(R.drawable.gb_qiandaoh);
							ContinuityDay++;
							setQianDaoImg(ContinuityDay);
							AppController.AccountInfIsChange = true;
							tv_popmsg.setText("恭喜您，联系签到7天，\n额外获取"
									+ model.notification.ProcessMessage);
							mDialog.initOneBtnDialog(true, msg,
									new OnDismissListener() {

										@Override
										public void onDismiss(
												DialogInterface dialog) {
											if (model.IsContinuityDay) {
												lin_pop.setVisibility(View.VISIBLE);
												dismissPoP();
											}
										}
									});
							Constants.LeaveGoldBean = Constants
									.StringToCurrency(model.LeaveGoldBean)
									.replace(".00", "");
						} else {
							mDialog.initOneBtnDialog(false,
									model.notification.ProcessMessage, null);
						}
					}

				});
	}

	/**
	 * 定时取消弹窗
	 */
	private void dismissPoP() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						lin_pop.setVisibility(View.GONE);
					}
				});
			}
		}, 1500);
	}

	@Override
	public void webLoadError() {
		progressDialog.dismiss();
	}

}
