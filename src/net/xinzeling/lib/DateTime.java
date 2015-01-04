package net.xinzeling.lib;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTime {
	public static String getTodayYmd(Date date) {
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMdd",Locale.CHINA);
		if(date==null){
			date = new Date();
		}
		return sdFormat.format(date);
	}

	public static String Date2String(Date date,String format){
		SimpleDateFormat sdFormat = new SimpleDateFormat(format,Locale.CHINA);
		return sdFormat.format(date);
	}
	
	public static Date String2Date(String datestring,String format) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat(format,Locale.CHINA);
		return sdFormat.parse(datestring);		
	}
	
	public static String Timestamp2String(Long msecond,String format){
		 SimpleDateFormat sdFormat =   new SimpleDateFormat(format,Locale.CHINA);
		 Long time=Long.valueOf(msecond);
		 return sdFormat.format(time);
	}
	
	public static int String2Timestamp(String datestring,String format) throws ParseException{
		SimpleDateFormat sdFormat = new SimpleDateFormat(format,Locale.CHINA);
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(String2Date(datestring,format));
		String str = sdFormat.format(calendar.getTime());
		Date date1 = sdFormat.parse(str);

		return (int)(date1.getTime()/1000);
	}
	
	public static long diffDayNumString1String2(String endstring,String beginstring,String format) throws ParseException{
		Date end = String2Date(endstring,format);
		Date begin = String2Date(beginstring,format);
		long beginTime = begin.getTime();
		long endTime = end.getTime();
		
		long betweenDays = (long)((endTime - beginTime)/(1000*60*60*24)+0.5);
		
		return betweenDays;
	}
}
