package com.md.hjyg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.ArticlesPgedListModel;
import com.md.hjyg.utils.DateUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 新闻公告适配器
 */
public class NewsNoticeListAdapter extends BaseAdapter {

	LayoutInflater layoutInflater;
	Context context;
	List<ArticlesPgedListModel> list = new ArrayList<ArticlesPgedListModel>();

	public NewsNoticeListAdapter(Context context,
			List<ArticlesPgedListModel> list) {
		this.context = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ArticlesPgedListModel getItem(int position) {
		return list.get(position);
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
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.news_notice_adepter,
					null);
			holder.news_title = (TextView) convertView
					.findViewById(R.id.news_title);
			holder.news_time = (TextView) convertView
					.findViewById(R.id.news_time);
			holder.new_ishot_iv = (ImageView) convertView
					.findViewById(R.id.new_ishot_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		boolean isHot = list.get(position).IsHot;
		if (isHot) {
			holder.new_ishot_iv.setImageResource(R.drawable.laba_red1);
		} else {
			holder.new_ishot_iv.setImageResource(R.drawable.laba_gray);
		}
		holder.news_title.setText(list.get(position).Title);
		holder.news_time
				.setText(DateUtil.changto(list.get(position).CreateTime));
		return convertView;
	}

	public class ViewHolder {
		TextView news_title, news_time;
		ImageView new_ishot_iv;
	}

}
