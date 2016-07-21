package com.md.hjyg.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.md.hjyg.R;
import com.md.hjyg.entity.StatsMonthlyRewardListsModel.StatsMonthlyReward;
import com.md.hjyg.layoutEntities.RecommendedView;
import com.md.hjyg.utils.DateUtil;
import android.content.Context;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/** 
 * ClassName: RecommendedViewPagerAdapter 
 * date: 2015-12-5 上午11:25:31 
 * remark:
 * @author pyc
 */
public class RecommendedViewPagerAdapter extends PagerAdapter {
	private List<StatsMonthlyReward> list;
	private Context context;
	private int awardType;
	private Handler mHandler;
	private String nowMoth;

	public RecommendedViewPagerAdapter(Context context,List<StatsMonthlyReward> list,int awardType,Handler mHandler) {
		this.context = context;
		this.list = list;
		this.awardType = awardType;
		this.mHandler = mHandler;
		nowMoth = getNowMonth();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return (view == (View)object);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View item = LayoutInflater.from(context).inflate(R.layout.layout_recommend_viewpager_adapter, null);
		RecommendedView mv = (RecommendedView) item.findViewById(R.id.myView);
		StatsMonthlyReward model = list.get(position);
		if (position == list.size()-1) {
			mHandler.sendEmptyMessage(0);
		}
		if (nowMoth.equals(model.YearMonth)) {
			mv.setColor(context.getResources().getColor(R.color.red), 
					context.getResources().getColor(R.color.red));
		}else {
			mv.setColor(context.getResources().getColor(R.color.gray_q),
					context.getResources().getColor(R.color.gray));
		}
		if (awardType == 1) {
			mv.SetAcount(model.YearMonth, model.Total, model.OneFirstInCome, model.TwoFirstInCome);
			mv.SetHitText("已收奖励(元)", "一级好友已收(元)", "二级好友已收(元)");
			
		}else {
			mv.SetAcount(model.YearMonth, model.UnfinishedInterestReward, 
					model.OneFirstUnfinishedInterest, model.TwoFirstUnfinishedInterest);
			mv.SetHitText("待收奖励(元)", "一级好友待收(元)", "二级好友待收(元)");
		}
		mv.commit();
		container.addView(item);
		return item;
	}
	/**判断是否为当月*/
	private String getNowMonth(){
		Date newTime = DateUtil.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
		String nowMoth = sdf.format(newTime);
		return nowMoth;
	}

}
