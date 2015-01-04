package net.xinzeling.model;

import java.util.Date;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.DateTime;
import android.database.Cursor;

public class LunarModel extends AppBase{
	
	public static Lunar fetchByDate(Date date){
		return fetchByDate(DateTime.Date2String(date,"yyyy-MM-dd"));
	}
	
	public static Lunar fetchByDate(String date){
		Lunar lunar = new Lunar();
		String sql ="SELECT vc_LunarCalendar,vc_Month, vc_Day,vc_Constellation,vc_Animal," +
				" vc_Holiday,vc_GlobalHoliday,vc_WeekdayHoliday,vc_LunarSpecial,vc_LuckyGod,vc_LuckyGod_old,vc_DemonGod,vc_DemonGod_old" +
				",vc_SolarCalendar FROM biz_xzl_Almanac WHERE vc_SolarCalendar=? LIMIT 1";
		Cursor cursor = dbh.rawQuery(sql, new String[]{date});
		if(cursor.moveToFirst()){
			lunar.lunarCalendar = cursor.getString(0);
			lunar.month = cursor.getString(1);
			lunar.day = cursor.getString(2);
			lunar.constellation = cursor.getString(3);
			lunar.animal = cursor.getString(4);
			lunar.holiday = cursor.getString(5);
			lunar.globalHoliday = cursor.getString(6);
			lunar.weekDayHoliday = cursor.getString(7);
			lunar.lunarSpecial = cursor.getString(8);
			lunar.luckyGod = cursor.getString(9);
			lunar.luckyGodOld = cursor.getString(10);
			lunar.demonGod = cursor.getString(11);
			lunar.demonGodOld = cursor.getString(12);
			lunar.date = cursor.getString(13);
		}
		cursor.close();
		return lunar;
	}
	
	
	public static class Lunar{
		public String date="";
		public String lunarCalendar ="";
		public String month="";
		public String day="";
		public String constellation="";
		public String animal="";
		public String holiday="";
		public String globalHoliday="";
		public String weekDayHoliday="";
		public String lunarSpecial="";
		public String luckyGod="";
		public String luckyGodOld="";
		public String demonGod="";
		public String demonGodOld="";
	}
}

/*
CREATE TABLE [biz_xzl_Almanac] (
		"i_AlmanacID"		integer PRIMARY KEY AUTOINCREMENT NOT NULL,
		"vc_SolarCalendar"		varchar(100) COLLATE NOCASE,//阳历  2031-09-21
		"vc_LunarCalendar"		varchar(100) COLLATE NOCASE,//农历 辛亥年丁酉月甲子日
		"vc_Month"		varchar(100) COLLATE NOCASE, //月  八月
		"vc_Day"		varchar(100) COLLATE NOCASE,// 日  初五
		"vc_Constellation"		varchar(100) COLLATE NOCASE, //星座  仙女座
		"vc_Animal"		varchar(20) COLLATE NOCASE,//生肖 生肖猪
		"vc_Holiday"		varchar(100) COLLATE NOCASE,//假期   中秋节
		"vc_GlobalHoliday"		varchar(100) COLLATE NOCASE,//国庆节
		"vc_WeekdayHoliday"		varchar(100) COLLATE NOCASE,//感恩节
		"vc_LunarSpecial"		varchar(100) COLLATE NOCASE, //节气 小雪
		"i_Work"		smallint,
		"vc_LuckyGod"		varchar(1000) COLLATE NOCASE,//宜 ：买新床，安放土地公，下葬，给追求者一个机会，新房正式开工建造（模型也可以），祭祖先，收养子/养女，请喝订婚酒，许愿，将骨灰装入骨灰盒，入新家，建筑完工后谢神，升级操作系统，将棺木移出屋外，搬家
		"vc_DemonGod"		varchar(1000) COLLATE NOCASE,//忌：
		"vc_LuckyGod_old"		varchar(1000) COLLATE NOCASE,
		"vc_DemonGod_old"		varchar(1000) COLLATE NOCASE
	)
*/