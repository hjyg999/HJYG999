package com.md.hjyg.adapter;

import java.util.ArrayList;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.AssureList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 项目简介adapter
 */
public class ProjectDetailsAdapter extends BaseAdapter{
	
	LayoutInflater layoutInflater;
	Context context;
	List<AssureList> Lists = new ArrayList<AssureList>();
	public ProjectDetailsAdapter(Context context,List<AssureList> Lists)
	{
		this.context = context;
		this.Lists = Lists;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return Lists.size();
	}

	@Override
	public AssureList getItem(int position) {
		return Lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.layout_project_details_adapter, null);
			holder.project_title = (TextView) convertView.findViewById(R.id.project_title);
			holder.project_content = (TextView) convertView.findViewById(R.id.project_content);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.project_title.setText(Lists.get(position).Title);
		holder.project_content.setText(brToN(Lists.get(position).Content));
		return convertView;
	}
	
	class ViewHolder {
		TextView project_title ,project_content;
	}
	
	/**把pc端换行符<br/>替换为安卓换行符\n*/
	private String brToN(String str) {
		while(str.indexOf("<br />") != -1)
		{
			str = str.replace("<br />", "\n");
		}
		return str;
	}

}
