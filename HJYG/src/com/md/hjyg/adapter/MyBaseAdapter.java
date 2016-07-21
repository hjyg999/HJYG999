package com.md.hjyg.adapter;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ClassName: MyBaseAdapter<T> date: 2016-2-18 下午1:50:34
 * 
 * @author pyc
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter
{
    protected List<T> lists ;
    protected LayoutInflater inflater;
    protected Context context;

    public LayoutInflater getInflater() {
		return inflater;
	}

	public MyBaseAdapter(Context context)
    {
        super();
        this.context = context;
        lists = new ArrayList<T>();
        inflater = LayoutInflater.from(context);
        //inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }
    
    public MyBaseAdapter(List<T> lists, Context context)
    {
        super();
        this.context = context;
        this.lists = lists;
        inflater = LayoutInflater.from(context);
    }
    
    /**
     * 得到适配器的集合
     * @return 返回适配器的集合
     */
    public List<T> getlists()
    {
        return lists;
    }
    
    /**
     * 给适配器设置一个集合
     * @param list 传入一个新集合
     */
    public void setlists(List<T> list)
    {
        lists.clear();
        lists.addAll(list);
    }
    
    /**
     * 清空适配器里面集合的元素，使集合为空
     */
    public void clear()
    {
        lists.clear();
    }
    
    /**
     * 给集合增加一个元素
     * @param t
     */
    public void addData(T t)
    {
        lists.add(t);
    }
    
    /**
     * 给集合删除一个元素
     * @param t
     */
    public void removeData(T t)
    {
        lists.remove(t);
    }
    
    /**
     *给集合增加一个集合 
     * @param list
     */
    public void addlist(List<T> list)
    {
        lists.addAll(list);
    }
    
    /**
     * 从集合里删除一个集合
     * @param list
     */
    public void removelist(List<T> list)
    {
        lists.removeAll(list);
    }

    @Override
    public int getCount()
    {
        return lists.size();
    }

    @Override
    public T getItem(int position)
    {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
   

}
