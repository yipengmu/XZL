package net.xinzeling.model;

import java.util.ArrayList;

import net.xinzeling.MyApplication;
import net.xinzeling.lib.DateTime;
import net.xinzeling.utils.Utils;
import android.content.ContentValues;
import android.database.Cursor;

public class GuaModel extends MyApplication{
	
	public static long save(Gua gua){
		ContentValues values = new ContentValues();
		values.put("type", gua.type);
		values.put("title", gua.title);
		values.put("date", gua.date);
		values.put("time", gua.time);
		values.put("doubleNumID", gua.doubleNumID);
		values.put("body", gua.body);
		values.put("yao", gua.yao);
		values.put("result", gua.result);
		values.put("inference", gua.inference);
		long guaid =dbh.insert("gua", null, values);
		ItemModel.add(ItemModel.REFER_GUA, guaid,DateTime.getTodayYmd(null));
		return guaid;
	}
	
	public static Gua fetch(int guaid){
		Cursor cursor = dbh.rawQuery("SELECT _id,type,date,time,body,yao,result,inference FROM gua WHERE _id=?", new String[]{String.valueOf(guaid)});
		if(cursor.moveToFirst()){
			Gua gua = new Gua();
			gua.guaid = cursor.getInt(0);
			gua.type = cursor.getInt(1);
			gua.date = cursor.getString(2);
			gua.time = cursor.getString(3);
			gua.body = cursor.getString(4);
			gua.yao = cursor.getString(5);
			gua.result = cursor.getString(6);
			gua.inference = cursor.getString(7);
			cursor.close();
			return gua;
		}
		return null;
	}
	
	public static Gua fetchByType(int type){
		Cursor cursor = dbh.rawQuery("SELECT _id,type,date,time,body,yao,result,inference FROM gua WHERE type=?", new String[]{String.valueOf(type)});
		if(cursor.moveToFirst()){
			Gua gua = new Gua();
			gua.guaid = cursor.getInt(0);
			gua.type = cursor.getInt(1);
			gua.date = cursor.getString(2);
			gua.time = cursor.getString(3);
			gua.body = cursor.getString(4);
			gua.yao = cursor.getString(5);
			gua.result = cursor.getString(6);
			gua.inference = cursor.getString(7);
			cursor.close();
			return gua;
		}
		return null;
	}
	
	public static ArrayList<Gua> fetchAll(){
		Utils.checkDBHInited(getContext());
		ArrayList<Gua> list = new ArrayList<Gua>();
		Cursor cursor = dbh.rawQuery("SELECT _id,type,date,time,body,yao,result,inference FROM gua ORDER BY _id DESC LIMIT 0,30", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Gua gua = new Gua();
			gua.guaid = cursor.getInt(0);
			gua.type = cursor.getInt(1);
			gua.date = cursor.getString(2);
			gua.time = cursor.getString(3);
			gua.body = cursor.getString(4);
			gua.yao = cursor.getString(5);
			gua.result = cursor.getString(6);
			gua.inference = cursor.getString(7);
			list.add(gua);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	//找到有算卦的日期
	public static ArrayList<String> fetchDateList(){
		Utils.checkDBHInited(getContext());
		ArrayList<String> list = new ArrayList<String>();
		Cursor cursor = dbh.rawQuery("SELECT DISTINCT date FROM gua ORDER BY _id DESC LIMIT 0,30",null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			list.add(cursor.getString(0));
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	//按照日期对算卦进行分组
	public static ArrayList<ArrayList<Gua>> fetchByDate(ArrayList<String> dateList){
		Utils.checkDBHInited(getContext());
		ArrayList<ArrayList<Gua>> list = new ArrayList<ArrayList<Gua>>();
		for(String date:dateList){
			Cursor cursor = dbh.rawQuery("SELECT _id,title,type,date,time,body,yao,result,inference FROM gua WHERE date=?", new String[]{date});
			cursor.moveToFirst();
			ArrayList<Gua> guaList = new ArrayList<Gua>();
			while(!cursor.isAfterLast()){
			 	Gua gua = new Gua();
			 	gua.guaid = cursor.getInt(0);
			 	gua.time = cursor.getString(1);
				gua.type = cursor.getInt(2);
				gua.date = cursor.getString(3);
				gua.time = cursor.getString(4);
				gua.body = cursor.getString(5);
				gua.yao = cursor.getString(6);
				gua.result = cursor.getString(7);
				gua.inference = cursor.getString(8);
				guaList.add(gua);
				cursor.moveToNext();
			}
			cursor.close();
			list.add(guaList);
		}
		return list;
	}
	
	//根据日期和时间将求卦数据分组
	public static ArrayList<ArrayList<Gua>> fetchByHours(String date,String stime,String etime){
		ArrayList<ArrayList<Gua>> list = new ArrayList<ArrayList<Gua>>();
		
		return list;
	}
	
	public static class Gua {
		public int guaid=-1;
		public int type=1; //卦类型
		public String title="";//卦类型中文
		public String date=""; //日期
		public String time=""; // 时间
		public int doubleNumID=-1;//服务器对应记录id
		public String body="";//体卦
		public String yao="";//爻
		public String result="" ;//卦象
		public String inference="";//解卦
		
		public CharSequence getTitle() {
			return Utils.getGuaTypeFromId(type);
		}
	}
}
