package com.md.hjyg.layoutEntities.wheel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.md.hjyg.R;
import com.md.hjyg.utils.ScreenUtils;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * ClassName: ChangeAddressDialog date: 2016-1-25 上午10:08:16 remark: 选择地址-三联动
 * 
 * @author pyc
 */
public class ChangeAddressDialog extends Dialog implements
		OnWheelChangedListener, android.view.View.OnClickListener,
		OnWheelScrollListener {

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName = "";
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName = "";
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private int itemsConunt = 3;
	private int maxTextSize = 24;
	private int minTextSize = 14;
	private ArrayWheelAdapter mProvinceAdapter;
	private ArrayWheelAdapter mCityAdapter;
	private ArrayWheelAdapter mDistrictdapter;
	private TextView btnSure;
	private TextView btnCancel;
	private OnAddressListener onAddressListener;
	// provice, city, district, zipCode
	private String provice = "";
	private String city = "";
	// private String district="";
	// private String zipCode="";

	private Context context;
	private boolean isShowDistrict;

	public ChangeAddressDialog(Context context) {
		super(context);
		this.context = context;
		this.isShowDistrict = true;
	}

	public ChangeAddressDialog(Context context, boolean isShowDistrict) {
		super(context);
		this.context = context;
		this.isShowDistrict = isShowDistrict;
	}

	public ChangeAddressDialog(Context context, boolean isShowDistrict,
			String provice, String city) {
		super(context);
		this.context = context;
		this.isShowDistrict = isShowDistrict;
		this.provice = provice;
		this.city = city;
	}
	
	public ChangeAddressDialog(Context context,int theme,boolean isShowDistrict,
			String provice, String city){
		super(context, theme);
		this.context = context;
		this.isShowDistrict = isShowDistrict;
		this.provice = provice;
		this.city = city;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_choiceaddress);
		initProvinceDatas();
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);
		mViewProvince.setAddress(true);
		mViewDistrict.setAddress(true);
		mViewCity.setAddress(true);
		if (isShowDistrict) {
			mViewDistrict.setVisibility(View.VISIBLE);
		} else {
			mViewDistrict.setVisibility(View.GONE);
		}
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);
		// 添加Scrolling事件,防止滑出后还回不变色
		mViewProvince.addScrollingListener(this);
		// 添加Scrolling事件
		mViewCity.addScrollingListener(this);
		// 添加Scrolling事件
		mViewDistrict.addScrollingListener(this);

		// int conunt = 0;
		//
		// mProvinceAdapter = new ArrayWheelAdapter(context, mProvinceDatas,
		// conunt,
		// maxTextSize, minTextSize);
		// mViewProvince.setViewAdapter(mProvinceAdapter);
		// mViewProvince.setVisibleItems(itemsConunt);
		// mViewCity.setCurrentItem(conunt);
		// setTextviewSize(mProvinceDatas[conunt], mProvinceAdapter);
		// updateCities();
		initProvince(provice);
		
		getWindow().setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = (int) (ScreenUtils.getScreenWidth(context) * 0.8);
		getWindow().setAttributes(lp);
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {

	}

	@Override
	public void onScrollingFinished(WheelView wheel) {
		String currentText = "";
		if (wheel == mViewProvince) {
			currentText = (String) mProvinceAdapter.getItemText(wheel
					.getCurrentItem());
			setTextviewSize(currentText, mProvinceAdapter);
		} else if (wheel == mViewCity) {
			currentText = (String) mCityAdapter.getItemText(wheel
					.getCurrentItem());
			setTextviewSize(currentText, mCityAdapter);
		} else if (wheel == mViewDistrict) {
			currentText = (String) mDistrictdapter.getItemText(wheel
					.getCurrentItem());
			setTextviewSize(currentText, mDistrictdapter);

		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		String currentText = "";
		if (wheel == mViewProvince) {
			currentText = (String) mProvinceAdapter.getItemText(wheel
					.getCurrentItem());
			setTextviewSize(currentText, mProvinceAdapter);
			updateCities();
		} else if (wheel == mViewCity) {
			currentText = (String) mCityAdapter.getItemText(wheel
					.getCurrentItem());
			setTextviewSize(currentText, mCityAdapter);
			updateAreas();
		} else if (wheel == mViewDistrict) {
			currentText = (String) mDistrictdapter.getItemText(wheel
					.getCurrentItem());
			setTextviewSize(currentText, mDistrictdapter);
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mCityAdapter = new ArrayWheelAdapter(context, cities, 0, maxTextSize,
				minTextSize);
		mViewCity.setViewAdapter(mCityAdapter);
		mViewCity.setVisibleItems(itemsConunt);
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 初始化省
	 * 
	 * @param Province
	 */
	private void initProvince(String Province) {
		int conunt = 0;
		if (mProvinceDatas == null) {
			mProvinceDatas = new String[] { "" };
		} else {
			if (Province != null && Province.length() > 0) {

				for (int i = 0; i < mProvinceDatas.length; i++) {
					if (Province.equals(mProvinceDatas[i])) {
						conunt = i;
					}
				}
			}
		}
		mProvinceAdapter = new ArrayWheelAdapter(context, mProvinceDatas,
				conunt, maxTextSize, minTextSize);
		mViewProvince.setViewAdapter(mProvinceAdapter);
		mViewProvince.setVisibleItems(itemsConunt);
		mViewProvince.setCurrentItem(conunt);
		setTextviewSize(mProvinceDatas[conunt], mProvinceAdapter);
		// updateCities();
		initCities(city);
	}

	/***
	 * 初始化市
	 * 
	 * @param city
	 */
	private void initCities(String city) {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		int conunt = 0;
		if (cities == null) {
			cities = new String[] { "" };
		} else {
			if (city != null && city.length() > 0) {
				for (int i = 0; i < cities.length; i++) {
					if (city.equals(cities[i])) {
						conunt = i;
					}
				}
			}
		}

		mCityAdapter = new ArrayWheelAdapter(context, cities, conunt,
				maxTextSize, minTextSize);
		mViewCity.setViewAdapter(mCityAdapter);
		mViewCity.setVisibleItems(itemsConunt);
		mViewCity.setCurrentItem(conunt);
		updateAreas();

	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}

		mDistrictdapter = new ArrayWheelAdapter(context, areas, 0, maxTextSize,
				minTextSize);
		mViewDistrict.setViewAdapter(mDistrictdapter);
		mViewDistrict.setVisibleItems(itemsConunt);
		mViewDistrict.setCurrentItem(0);
		mCurrentDistrictName = areas[0];
		mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
	}

	public void setAddressListener(OnAddressListener onAddressListener) {
		this.onAddressListener = onAddressListener;
	}

	public interface OnAddressListener {
		public void onClick(String provice, String city, String district,
				String zipCode);
	}

	/**
	 * 设置选中条目字体大小 和颜色
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText,
			ArrayWheelAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(minTextSize);
				textvew.setTextColor(Color.RED);
			} else {
				textvew.setTextSize(minTextSize);
				textvew.setTextColor(context.getResources().getColor(
						R.color.gray));
			}
		}
	}

	private class ArrayWheelAdapter extends AbstractWheelTextAdapter {
		String[] list;

		protected ArrayWheelAdapter(Context context, String[] list,
				int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_address_wheel, NO_RESOURCE, currentItem,
					maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.length;
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list[index] + "";
		}
	}

	/**
	 * 解析省市区的XML数据
	 */
	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = context.getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnSure) {
			if (onAddressListener != null) {

				onAddressListener
						.onClick(mCurrentProviceName, mCurrentCityName,
								mCurrentDistrictName, mCurrentZipCode);
			}
		}

		dismiss();

	}

}
