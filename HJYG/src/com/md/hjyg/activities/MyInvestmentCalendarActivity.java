package com.md.hjyg.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.md.hjyg.R;
import com.md.hjyg.adapter.MyInvestListAdapter;
import com.md.hjyg.entity.GetInvestmentListModel;
import com.md.hjyg.entity.MyInvestmentModel;
import com.md.hjyg.layoutEntities.CalendarView;
import com.md.hjyg.layoutEntities.CalendarView.OnItemClickListener;
import com.md.hjyg.layoutEntities.HeaderView;
import com.md.hjyg.services.DataFetchService.DftErrorListener;
import com.md.hjyg.utility.DialogProgressManager;
import com.md.hjyg.utility.Save;
import com.md.hjyg.xinTouConstant.Constants;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;

/**
 * ClassName: MyInvestmentCalendarActivity date: 2016-4-20 上午11:19:19
 * remark:投资回款日历
 * 
 * @author pyc
 */
public class MyInvestmentCalendarActivity extends BaseActivity implements
		OnClickListener, DftErrorListener {
	private final static int handler_what = 1;
	private Bitmap[] bitmaps;
	private ImageView img_top, img_rili, img_toleft, img_toright, img_nodata;
	private LinearLayout lin_toleft, lin_toright, lin_nodata;
	private CalendarView calendar;
	private SimpleDateFormat format;
	private TextView calendarCenter, tv_dataTitle;
	private ListView mListView;
	private DialogProgressManager dialog;
	private Map<String, MyInvestmentModel> dataMap;
	private TextView tv_sum, tv_incomesum;
	private String nowData;
	private String[] ya;
	private HeaderView mheadView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinvestmentcalendar_activity_layout);

		findViewandInit();
		initCalendarView();
		Save.loadingImg(handler, getApplicationContext(), new int[] {
				R.drawable.rilibj, R.drawable.rili,
				R.drawable.arrow_left_28x28, R.drawable.arrow_right_28x28,
				R.drawable.jxw_zm_76_96 }, handler_what);
	}

	private void findViewandInit() {
		dialog = new DialogProgressManager(this, "努力加载中...");
		dft.setOnDftErrorListener(this);
		
		mheadView = (HeaderView) findViewById(R.id.mheadView);
		mheadView.setViewUI(this, "投资回款日历", "回到今天");
		mheadView.setRightTvGone();

		img_top = (ImageView) findViewById(R.id.img_top);
		img_rili = (ImageView) findViewById(R.id.img_rili);
		img_toleft = (ImageView) findViewById(R.id.img_toleft);
		img_toright = (ImageView) findViewById(R.id.img_toright);
		img_nodata = (ImageView) findViewById(R.id.img_nodata);

		tv_dataTitle = (TextView) findViewById(R.id.tv_dataTitle);
		mListView = (ListView) findViewById(R.id.mListView);
		mListView.setFocusable(false);

		tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_incomesum = (TextView) findViewById(R.id.tv_incomesum);
		lin_nodata = (LinearLayout) findViewById(R.id.lin_nodata);

	}

	/**
	 * 初始化日历控件
	 */
	@SuppressLint("SimpleDateFormat")
	private void initCalendarView() {

		format = new SimpleDateFormat("yyyy/MM/dd");
		dataMap = new HashMap<String, MyInvestmentModel>();
		// 获取日历控件对象
		calendar = (CalendarView) findViewById(R.id.calendar);
		calendar.setSelectMore(false); // 单选

		calendarCenter = (TextView) findViewById(R.id.calendarCenter);
		lin_toleft = (LinearLayout) findViewById(R.id.lin_toleft);
		lin_toright = (LinearLayout) findViewById(R.id.lin_toright);

		lin_toleft.setOnClickListener(this);
		lin_toright.setOnClickListener(this);

		// 获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
		ya = calendar.getYearAndmonth().split("-");
		if (ya[1].length() == 1) {
			calendarCenter.setText(ya[0] + "年0" + ya[1] + "月");
		} else {
			calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
		}
		nowData = calendar.getYearAndmonth();
		lin_toleft.setVisibility(View.INVISIBLE);
		getWebData(ya[0], ya[1]);

		// 设置控件监听，可以监听到点击的每一天
		calendar.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void OnItemClick(Date selectedStartDate,
					Date selectedEndDate, Date downDate) {
				if (calendar.isSelectMore()) {
					Toast.makeText(
							getApplicationContext(),
							format.format(selectedStartDate) + "到"
									+ format.format(selectedEndDate),
							Toast.LENGTH_SHORT).show();
				} else {
					if (downDate != null) {

						String selectDate = format.format(downDate);
						String[] data = selectDate.split("/");
						if (data[1].indexOf("0") == 0) {
							data[1] = data[1].substring(1, 2);
						}
						String key = data[0] + data[1];
						MyInvestmentModel myInvestmentModel = dataMap.get(key);
						if (myInvestmentModel == null
								|| myInvestmentModel.List.size() == 0) {
							setData(selectDate, null);
							return;
						}
						List<GetInvestmentListModel> list = new ArrayList<GetInvestmentListModel>();
						for (int i = 0; i < myInvestmentModel.List.size(); i++) {
							if (selectDate.equals(myInvestmentModel.List.get(i).RepaymentDate
									.replaceAll("-", "/"))) {
								list.add(myInvestmentModel.List.get(i));
							}
						}
						setData(selectDate, list);
					}else {
						getWebData(ya[0], ya[1]);
					}

				}
			}
		});

	}

	/**
	 * 获取网络数据
	 * 
	 * @param pageIndex
	 */
	private void getWebData(String Year, String Month) {
		final String key = Year + Month;
		MyInvestmentModel myInvestmentModel = dataMap.get(key);
		if (myInvestmentModel != null) {
			setData(myInvestmentModel);
			return;
		}
		dialog.initDialog();
		dft.GetRepaymentCalendarDetail(Year, Month,
				Constants.GetRepaymentCalendarDetail_URL, Request.Method.POST,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						dialog.dismiss();

						MyInvestmentModel myInvestmentModel = (MyInvestmentModel) dft
								.GetResponseObject(response,
										MyInvestmentModel.class);
						dataMap.put(key, myInvestmentModel);

						setData(myInvestmentModel);
					}

				}, null);

	}

	private void setImg() {
		if (bitmaps != null && bitmaps.length > 0) {
			img_top.setImageBitmap(bitmaps[0]);
			img_rili.setImageBitmap(bitmaps[1]);
			img_toleft.setImageBitmap(bitmaps[2]);
			img_toright.setImageBitmap(bitmaps[3]);
			img_nodata.setImageBitmap(bitmaps[4]);
		}
	}

	/**
	 * 本月无数据
	 */
	private void noData() {
		lin_nodata.setVisibility(View.VISIBLE);
		tv_dataTitle.setText("本月无收益");
		mListView.setVisibility(View.GONE);
		tv_sum.setText(0 + "");
		tv_incomesum.setText(0.00 + "");
	}

	/**
	 * 设置本月数据
	 * 
	 * @param myInvestmentModel
	 */
	private void setData(MyInvestmentModel myInvestmentModel) {
		if (myInvestmentModel == null || myInvestmentModel.List == null) {
			return;
		}
		if (myInvestmentModel.List.size() > 0) {
			calendar.setDays(getDay(myInvestmentModel.List));
			lin_nodata.setVisibility(View.GONE);
			tv_dataTitle.setText("本月应收");
			MyInvestListAdapter adapter = new MyInvestListAdapter(
					myInvestmentModel.List, this, 4);
			mListView.setAdapter(adapter);
			Constants.setListViewHeightBasedOnChildren(mListView);
			mListView.setVisibility(View.VISIBLE);
			tv_sum.setText(myInvestmentModel.List.size() + "");
			tv_incomesum.setText(myInvestmentModel.TotalAmount);
		} else {
			noData();
		}
	}

	/**
	 * 设置本日数据
	 * 
	 * @param getInvestmentListModel
	 */
	private void setData(String data, List<GetInvestmentListModel> list) {
		if (list != null && list.size() > 0) {
			lin_nodata.setVisibility(View.GONE);
			tv_dataTitle.setText(data + " 应收");
			MyInvestListAdapter adapter = new MyInvestListAdapter(list, this, 4);
			mListView.setAdapter(adapter);
			Constants.setListViewHeightBasedOnChildren(mListView);
			mListView.setVisibility(View.VISIBLE);
		} else {
			lin_nodata.setVisibility(View.VISIBLE);
			tv_dataTitle.setText("本日无收益");
			mListView.setVisibility(View.GONE);
		}
	}

	private int[] getDay(List<GetInvestmentListModel> list) {
		if (list == null || list.size() == 0) {
			return null;
		}
		int[] days = new int[list.size()];
		for (int i = 0; i < days.length; i++) {
			String data = list.get(i).RepaymentDate.replaceAll("-", "/");
			String[] dataString = data.split("/");
			if (dataString.length == 3) {
				if (dataString[2].indexOf("0") == 0) {
					dataString[2] = dataString[2].substring(1, 2);
				}
				days[i] = Integer.parseInt(dataString[2]);
			}
		}
		return days;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case handler_what:
				bitmaps = (Bitmap[]) msg.obj;
				setImg();

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case HeaderView.left_img_ID:
			this.finish();
			overTransition(1);
			break;
		case R.id.lin_toleft:// 上一个月
			// 点击上一月 同样返回年月
			calendar.setDays(null);
			String leftYearAndmonth = calendar.clickLeftMonth();
			setleftArrowStaut(leftYearAndmonth);
			ya = leftYearAndmonth.split("-");
			if (ya[1].length() == 1) {
				calendarCenter.setText(ya[0] + "年0" + ya[1] + "月");
			} else {
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
			getWebData(ya[0], ya[1]);
			break;
		case R.id.lin_toright:// 下一个月
			// 点击下一月
			calendar.setDays(null);
			String rightYearAndmonth = calendar.clickRightMonth();
			setleftArrowStaut(rightYearAndmonth);
			ya = rightYearAndmonth.split("-");
			if (ya[1].length() == 1) {
				calendarCenter.setText(ya[0] + "年0" + ya[1] + "月");
			} else {
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
			getWebData(ya[0], ya[1]);
			break;
		case HeaderView.rightv_ID:// 返回当前月
			// 返回当前月
			calendar.setDays(null);
			String newMonth = calendar.getNewMonth();
			setleftArrowStaut(newMonth);
			ya = newMonth.split("-");
			if (ya[1].length() == 1) {
				calendarCenter.setText(ya[0] + "年0" + ya[1] + "月");
			} else {
				calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
			}
			getWebData(ya[0], ya[1]);
			break;

		default:
			break;
		}

	}

	/**
	 * 设置这边箭头的状态
	 * 
	 * @param time
	 */
	private void setleftArrowStaut(String time) {
		if (time.equals(nowData)) {
			lin_toleft.setVisibility(View.INVISIBLE);
			mheadView.setRightTvGone();
		} else {
			lin_toleft.setVisibility(View.VISIBLE);
			mheadView.setRightTvVISIBLE();
		}
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overTransition(1);
	}

	@Override
	public void webLoadError() {
		dialog.dismiss();
	}

}
