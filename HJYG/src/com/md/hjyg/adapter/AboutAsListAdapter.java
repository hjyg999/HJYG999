package com.md.hjyg.adapter;

import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.AboutUsInfoModel.AboutUsDesModel;
import com.md.hjyg.utility.Save;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * ClassName: AboutAsListAdapter 
 * date: 2016-3-28 上午11:20:12 
 * remark:关于我们
 * @author pyc
 */
public class AboutAsListAdapter extends MyBaseAdapter<AboutUsDesModel>{
	
	private Bitmap bitmap;

	/**
	 * @param lists
	 * @param context
	 */
	public AboutAsListAdapter(List<AboutUsDesModel> lists, Context context) {
		super(lists, context);
		bitmap = Save.ScaleBitmap(BitmapFactory.decodeResource(context.getResources(),
				R.drawable.aboutas_top_bg), context);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHold hold = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.adapter_abaoutaslist_layout, null);
			hold = new ViewHold(convertView);
			convertView.setTag(hold);
		}else {
			hold = (ViewHold) convertView.getTag();
		}
		AboutUsDesModel model = lists.get(position);
		if (position == lists.size() - 1) {
			hold.tv_content.setText(model.content);
		}else {
			hold.tv_content.setText( "       " + model.content);
		}
		if (position == 0) {
			hold.img.setImageBitmap(bitmap);
			hold.img.setVisibility(View.VISIBLE);
			hold.tv_title.setText(model.tittle);
			hold.tv_title.setVisibility(View.VISIBLE);
			hold.tv_stitle.setVisibility(View.GONE);
		}else {
			hold.img.setVisibility(View.GONE);
			hold.tv_title.setVisibility(View.GONE);
			hold.tv_stitle.setText(model.tittle);
			hold.tv_stitle.setVisibility(View.VISIBLE);
			
		}
		return convertView;
	}
	
	class ViewHold{
		TextView tv_title,tv_stitle,tv_content;
		ImageView img;
		public ViewHold(View v){
			tv_title = (TextView) v.findViewById(R.id.tv_title);
			tv_stitle = (TextView) v.findViewById(R.id.tv_stitle);
			tv_content = (TextView) v.findViewById(R.id.tv_content);
			img = (ImageView) v.findViewById(R.id.img);
		}
	}

}
