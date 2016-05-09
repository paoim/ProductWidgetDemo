package com.challenge.productwidget.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatUtil {

	private String dateFormat;
	
	public static final String SLAS_DDMMYY = "dd/MM/yy";
	public static final String DAS_DDMMYYYY = "dd-MM-yyyy";
	public static final String DAS_YYYYMMDD = "yyyy/MM/dd";
	public static final String SLAS_MMDDYYYY = "MM/dd/yyyy";
	public static final String DAS_DDMMYYHHMM = "dd-MM-yy:HH:mm";
	public static final String SLAS_MMDDYYYYHHMM = "MM/dd/yyyy HH:mm";
	public static final String DAS_DDMMYYHHMMSS = "dd-MM-yy:HH:mm:SS";
	public static final String DAS_DDMMYYHHMMSSZ = "dd-MM-yy:HH:mm:SS Z";
	public static final String SLAS_MMDDYYYYHHMMSSA = "MM/dd/yyyy:HH:mm:SS a";

	public DateFormatUtil(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public static Date todayDate() {
		return new Date();
	}

	public String formateToday() {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(DateFormatUtil.todayDate());
	}

	public String doFormate(Date date) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		return format.format(date);
	}

	public Date getDateFormat(String strDate) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

		try {
			date = formatter.parse(strDate);
		} catch (ParseException e) {
			date = null;
		}

		return date;
	}
	
	public static long getMonth(Date date) {
		Calendar cal = getCalendar(date);
		long month = cal.get(Calendar.MONTH);
		return month;
	}
	
	public static long getWeekOfMonth(Date date) {
		Calendar cal = getCalendar(date);
		long week = cal.get(Calendar.WEEK_OF_MONTH);
		return week;
	}
	
	public static long getWeekOfYear(Date date) {
		Calendar cal = getCalendar(date);
		long week = cal.get(Calendar.WEEK_OF_YEAR);
		return week;
	}
	
	private static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}
