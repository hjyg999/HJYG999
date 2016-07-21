package com.md.hjyg.adapter;

import com.md.hjyg.R;
import com.md.hjyg.utility.Save;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ClassName: HomeListViewAdapter date: 2016-2-18 下午1:50:34 remark: 主页项目adapter
 * 
 * @author pyc
 */
public class HomeListViewAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private Context context;

	public HomeListViewAdapter(Context context) {
		layoutInflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.adapter_homelistview,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (position == 0) {
			holder.v_line.setVisibility(View.INVISIBLE);
			holder.tv_name.setVisibility(View.INVISIBLE);
			holder.tv_huoqiname.setVisibility(View.VISIBLE);
			holder.img_bg.setImageBitmap(Save.ScaleBitmap(
					BitmapFactory.decodeResource(context.getResources(), R.drawable.home_huoqibao_bg),
					context));
		} else {
			holder.v_line.setVisibility(View.VISIBLE);
			holder.tv_name.setVisibility(View.VISIBLE);
			holder.tv_huoqiname.setVisibility(View.INVISIBLE);
			holder.img_bg.setImageBitmap(null);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img_bg;
		TextView tv_name, tv_huoqiname, tv_canbuy, tv_term, tv_termhit,
				tv_rate, tv_rateadd, tv_surplus, tv_tobuy;
		View v_line;

		public ViewHolder(View convertView) {
			img_bg = (ImageView) convertView.findViewById(R.id.img_bg);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_huoqiname = (TextView) convertView
					.findViewById(R.id.tv_huoqiname);
			tv_canbuy = (TextView) convertView.findViewById(R.id.tv_canbuy);
			tv_term = (TextView) convertView.findViewById(R.id.tv_term);
			tv_termhit = (TextView) convertView.findViewById(R.id.tv_termhit);
			tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
			tv_rateadd = (TextView) convertView.findViewById(R.id.tv_rateadd);
			tv_surplus = (TextView) convertView.findViewById(R.id.tv_surplus);
			tv_tobuy = (TextView) convertView.findViewById(R.id.tv_tobuy);
			v_line = convertView.findViewById(R.id.v_line);
		}
	}

}
