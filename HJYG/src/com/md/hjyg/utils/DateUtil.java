package com.md.hjyg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

import com.md.hjyg.utility.AppController;
import com.md.hjyg.xinTouConstant.Constants;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String DD_MM_YYYY_HH_MM = "dd/MM/yyyy HH:mm";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYYMMDD_HH_MM_SS = "yyyyMMddHHmmss";
	public static final String MM_DD = "MM/dd";

	public static String dateToStr(Date date, String pattern) {
		return DateFormatUtils.format(date, pattern);
	}

	public static String strToDate(long millis, String pattern) {
		return DateFormatUtils.format(millis, pattern);
	}

	@SuppressLint("SimpleDateFormat")
	public static boolean dateCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
		String isDate = sdf_d.format(new java.util.Date());
		java.util.Date date1;
		java.util.Date date0;
		try {
			date1 = sdf_d.parse(str);
			date0 = sdf_d.parse(isDate);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param dateFirst
	 * @param dateSecond
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static int dateSubtraction(String dateFirst, String dateSecond) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getDate());
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
		long between_days = 0;
		try {
			cal.setTime(sdf_d.parse(dateFirst));
			long time1 = cal.getTimeInMillis();
			cal.setTime(sdf_d.parse(dateSecond));
			long time2 = cal.getTimeInMillis();
			between_days = (time2 - time1) / (1000 * 3600 * 24);
		} catch (ParseException e) {
			System.out.println("日期格式必须为：yyyy-MM-dd；如：2010-4-4.");
			e.printStackTrace();
		}
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 计算当前日期之后的天数
	 * 
	 * @param dateFirst
	 * @param dateSecond
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String dateAfter(String dateFirst, String dateSecond) {
		String dataNum = dateSecond.substring(0, dateSecond.length() - 1);
		String dataDW = dateSecond.substring(dateSecond.length() - 1,
				dateSecond.length());
		String endTime = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.getDate());
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
		try {
			cal.setTime(sdf_d.parse(dateFirst));
			if ("天".equals(dataDW)) {
				cal.add(Calendar.DATE, Integer.parseInt(dataNum));
			} else if ("月".equals(dataDW)) {
				cal.add(Calendar.MONTH, Integer.parseInt(dataNum));
			}
			endTime = dateToStr(cal.getTime(), YYYY_MM_DD);
		} catch (ParseException e) {
			System.out.println("日期格式必须为：yyyy-MM-dd；如：2010-4-4.");
			e.printStackTrace();
		}
		return endTime;
	}

	/**
	 * 判断标是否已经开始投标
	 * 
	 * @param str
	 *            开始时间
	 * @return true 已经开始
	 */
	public static boolean isStart(String str) {
		// 2015-08-20T13:38:04
		str = str.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date newTime = getDate();
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.before(newTime);
	}
	
	/***
	 * 把时间字符串转换成时间戳
	 * @param str yyyy-MM-dd HH:mm:ss 格式
	 * @return long
	 */
	public static long getLongTime(String str)
	{
		str = str.replace("T", " ");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = 0;
		Date date =null;
		try {
			date = sdf.parse(str);
			time = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return time;
	}

	/**
	 * 比较两个时间的前后 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param newTime
	 *            待比较的时间
	 * @param oldTime
	 *            基准时间
	 * @return ture 待比较的时间在基准时间之后
	 */
	public static boolean newTimeIsAfteroldTime(String newTime, String oldTime) {
		String newtime = newTime;
		String oldtime = oldTime;
		if (newtime.indexOf("T") != -1) {
			newtime = newtime.replace("T", " ");
		}
		if (oldtime.indexOf("T") != -1) {
			oldtime = oldtime.replace("T", " ");
		}
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		try {
			Date newDate = sdf.parse(newtime);
			Date oldDate = sdf.parse(oldtime);
			return newDate.after(oldDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;

	}

	/***
	 * 
	 * @param str
	 *            yyyy-MM-dd 格式
	 * @return MM/dd
	 * @throws ParseException
	 */
	public static String changeStr(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
		Date date = null;
		try {
			date = sdf.parse(str);
			str = sdf2.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;

	}

	/**
	 * 整数(秒数)转换为时分秒格式
	 * 
	 * @param stringTime
	 *            yyyy-MM-dd HH:mm:ss
	 * @return 00时 00分 00秒
	 */
	public static String secToTime(String stringTime) {
		long nTime = getDate().getTime();
		SimpleDateFormat sdf_d = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
		Date date = null;
		try {
			date = sdf_d.parse(stringTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date == null) {
			return null;
		}
		long toTime = date.getTime();
		int time = (int) ((toTime - nTime) / 1000);
		if (time < 0) {
			return null;
		}
		String timeStr = null;
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0)
			return "00时 00分 00秒";
		else {
			minute = time / 60;
			if (minute < 60) {
				second = time % 60;
				timeStr = "00时 " + unitFormat(minute) + "分 "
						+ unitFormat(second) + "秒";
			} else {
				hour = minute / 60;
				if (hour > 99)
					return "99时 59分 59秒";
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = unitFormat(hour) + "时 " + unitFormat(minute) + "分 "
						+ unitFormat(second) + "秒";
			}
		}
		return timeStr;
	}

	public static String unitFormat(int i) {
		String retStr = null;
		if (i >= 0 && i < 10)
			retStr = "0" + Integer.toString(i);
		else
			retStr = "" + i;
		return retStr;
	}

	/*** 时间字符串转换成长整型 */
	@SuppressLint("SimpleDateFormat")
	public static long StrToLong(String startTime) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
		if (date != null) {
			return date.getTime();
		}
		return 0;
	}

	/** 移除秒 */
	@SuppressLint("SimpleDateFormat")
	public static String removeS(String startTime) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		if (date != null) {
			return sdf.format(date);
		}
		return null;
	}

	/** 移除豪秒 */
	@SuppressLint("SimpleDateFormat")
	public static String removeHS(String startTime) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		if (date != null) {
			return sdf.format(date);
		}
		return null;
	}
	
	/**
	 * 把yyyy-MM-ddTHH:mm:ss转换成MM/dd HH:mm
	 * @param startTime yyyy-MM-ddTHH:mm:ss
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String removeYS(String startTime) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		if (date != null) {
			return sdf.format(date);
		}
		return null;
	}
	/**
	 * 把yyyy-MM-ddTHH:mm:ss转换成yyyy/MM/dd HH:mm:ss
	 * @param startTime yyyy-MM-ddTHH:mm:ss
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String changto(String startTime) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		if (date != null) {
			return sdf.format(date);
		}
		return null;
	}
	
	/**
	 * 把yyyy-MM-ddTHH:mm:ss转换成format自定义的格式
	 * @param startTime
	 * @param format
	 * @return
	 */
	public static String changto(String startTime,String format) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		if (date != null) {
			return sdf.format(date);
		}
		return null;
	}
	public static String changto(String startTime,String format2,String format) {
		// String startTime = "2015-12-24 16:38:30";
		startTime = startTime.replace("T", " ");
		SimpleDateFormat sdf_d = new SimpleDateFormat(format2);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf_d.parse(startTime);
		} catch (ParseException e) {
			e.printStackTrace();
			return startTime;
		}
		if (date != null) {
			return sdf.format(date);
		}
		return startTime;
	}

	/***
	 * 获取当前的年月日
	 * 
	 * @return
	 */
	public static String getNewDate() {
		Calendar c = Calendar.getInstance();
		c.setTime(DateUtil.getDate());
		return c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月"
				+ c.get(Calendar.DATE) + "日";

	}

	/**
	 * 将当前时间 格式化
	 */
	public static String getTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}
	
	/**
	 * 获取十位时间戳
	 */
	public static String getTenTime(){
		Date date = DateUtil.getDate();
		long time = date.getTime();
		String tenStr = String.valueOf(time/1000);
		return tenStr;
	}
	
	/**
	 * 计算两个时间相差多少毫秒
	 * @return
	 */
	public static long dateDiff(String startTime, String endTime,  String format){
		startTime = startTime.replace("T", " ");
		endTime = endTime.replace("T", " ");
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long diff = 0; 
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return diff;
		
	}
	
	/**
	 * 获取服务器的当前时间
	 * @return
	 */
	public static Date getDate(){
		Date date = new Date(System.currentTimeMillis() - Constants.GetServerNowTime(AppController.getInstance().getApplicationContext()));
		return date;
	}
	/**
	 * 获取服务器的当前时间
	 * @param mcontext
	 * @param format 返回的数据格式
	 * @return
	 */
	public static String getDate(String format){
		Date date = DateUtil.getDate();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String string = sdf.format(date);
		return string;
	}
	
	

}
