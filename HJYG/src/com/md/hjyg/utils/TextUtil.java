package com.md.hjyg.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

/** 
 * ClassName: TextUtil 
 * date: 2015-11-21 下午1:45:52 
 * remark:字符串管理类
 * @author pyc
 */
public class TextUtil {
	
	/**加粗*/
	public static CharSequence getBoldString(String str,int start,int end){
		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		CharacterStyle span=new UnderlineSpan(); 
		style.setSpan(new StyleSpan(Typeface.BOLD),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}
	
	/**红色字符串*/
	public static CharSequence getRedString(String str,int start,int end){
		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		CharacterStyle span=new UnderlineSpan(); 
		style.setSpan(new  ForegroundColorSpan(Color.parseColor("#BE1423")),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}
	/**变色*/
	public static CharSequence getColorString(int rId,String str,int start,int end){
		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		CharacterStyle span=new UnderlineSpan(); 
		style.setSpan(new  ForegroundColorSpan(rId),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}
	public static CharSequence getColorandSizeString(int rID,String str,int start,int end,float size){
		SpannableStringBuilder style = new SpannableStringBuilder(str);
//		CharacterStyle span=new UnderlineSpan(); 
		style.setSpan(new  ForegroundColorSpan(rID),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new RelativeSizeSpan(size),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}
	
	/**设置文字相对大小，指相对于文本设定的大小的相对比例*/
	public static CharSequence getRelativeSize(String str,int start,int end,float size){
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new RelativeSizeSpan(size),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}
	
	/**设置文字相对大小，指相对于文本设定的大小的相对比例,并把其设置为红色*/
	public static CharSequence getRelativeSizeandRedS(String str,int start,int end,float size){
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new RelativeSizeSpan(size),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#BE1423")),start, end,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		return style;
	}
	
	/***
	 *  获取html格式的字符串数据
	 * @param htmlstr html格式字符串
	 * @return
	 */
	public static Spanned getHtmlText(String htmlstr){
		if (htmlstr == null) {
			htmlstr = "";
		}
		return Html.fromHtml(htmlstr);
	}
	
	/**
	 * 是否为空或者长度为0
	 * @param str
	 * @return true表示为空或者长度为0
	 */
	public static boolean isEmpty(String str){
		if (str == null || str.length() == 0) {
			return true;
		}
		return false;
	}

}
