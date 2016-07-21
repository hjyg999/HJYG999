package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.GoldTypeChoice;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * ClassName: DialogListAdapter 
 * date: 2016-1-22 下午2:15:07 
 * remark:dialogList适配器
 * @author pyc
 */
public class DialogListAdapter extends BaseAdapter{
	private List<GoldTypeChoice> lists;
	private LayoutInflater inflater;
	public DialogListAdapter(Context context,List<GoldTypeChoice> lists){
		inflater = LayoutInflater.from(context);
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public GoldTypeChoice getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = inflater.inflate(R.layout.dialoglist_adapter_layout, null);
		TextView tv = (TextView) v.findViewById(R.id.tv);
		ImageView img = (ImageView) v.findViewById(R.id.img);
		tv.setText(lists.get(position).getName());
		if (lists.get(position).isChoice()) {
			img.setVisibility(View.VISIBLE);
		}else {
			img.setVisibility(View.GONE);
		}
		return v;
	}

}
