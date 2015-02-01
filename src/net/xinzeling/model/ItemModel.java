package net.xinzeling.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.database.Cursor;
import net.xinzeling.MyApplication;
import net.xinzeling.lib.DateTime;
//记事和求卦的一个索引
public class ItemModel extends MyApplication{
	public static final int REFER_GUA=1;
	public static final int REFER_NOTE=2;
	public static final int REFER_MEM=3;

	public static void add(int referType, long referId,String yyyymmdd){
		checkDbH();
		String sql =String.format(Locale.CHINA, "INSERT INTO item (refer_type,refer_id,date)VALUES(%d,%d,%d)",referType,referId,Integer.valueOf(yyyymmdd));
		dbh.execSQL(sql);
	}

	public static void del(int referType, long referId){
		checkDbH();
		String sql =String.format(Locale.CHINA, "DELETE FROM item WHERE refer_type=%s AND refer_id=%d",referType,referId);
		dbh.execSQL(sql);
	}

	public static ArrayList<Item> getItemList(int referType){
		checkDbH();
		ArrayList<Item> list = new ArrayList<Item>();
		Cursor cursor;
		String sql ="SELECT refer_type,refer_id FROM item";
		if(referType>0){
			sql+=" WHERE refer_type=? ORDER BY _id DESC";
			cursor = dbh.rawQuery(sql, new String[]{String.valueOf(referType)});
		}else{
			sql+=" ORDER BY _id DESC";
			cursor = dbh.rawQuery(sql, null);
		}
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			int rtype = cursor.getInt(0);
			int rid = cursor.getInt(1);
			Item item = null;
			if(rtype == REFER_GUA){
				item=fetchGua(rid);
			}else if(rtype == REFER_MEM){
				item=fetchMem(rid);
			}else if(rtype==REFER_NOTE){
				item =fetchNote(rid);
			}
			if(item !=null){
				list.add(item);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	public static ArrayList<Item> getItemList(int referType,String yyyymmddStart,String yyyymmddEnd){
		checkDbH();
		ArrayList<Item> list = new ArrayList<Item>();
		Cursor cursor;
		String sql ="SELECT refer_type,refer_id FROM item";
		if(referType>0){
			sql+=" WHERE refer_type=? and date>=? and date<=? ORDER BY _id DESC";
			cursor = dbh.rawQuery(sql, new String[]{String.valueOf(referType),yyyymmddStart,yyyymmddEnd});
		}else{
			sql+=" WHERE  date>=? and date<=?  ORDER BY _id DESC";
			cursor = dbh.rawQuery(sql, new String[]{yyyymmddStart,yyyymmddEnd});
		}
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			int rtype = cursor.getInt(0);
			int rid = cursor.getInt(1);
			Item item = null;
			if(rtype == REFER_GUA){
				item=fetchGua(rid);
			}else if(rtype == REFER_MEM){
				item=fetchMem(rid);
			}else if(rtype==REFER_NOTE){
				item =fetchNote(rid);
			}
			if(item !=null){
				list.add(item);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	

	public static ArrayList<Item> getItemList(int referType,String yyyymmddStart){
		checkDbH();
		ArrayList<Item> list = new ArrayList<Item>();
		Cursor cursor;
		String sql ="SELECT refer_type,refer_id FROM item";
		if(referType>0){
			sql+=" WHERE refer_type=? and date=? ORDER BY _id DESC";
			cursor = dbh.rawQuery(sql, new String[]{String.valueOf(referType),yyyymmddStart});
		}else{
			sql+=" WHERE  date=? ORDER BY _id DESC";
			cursor = dbh.rawQuery(sql, new String[]{yyyymmddStart});
		}
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			int rtype = cursor.getInt(0);
			int rid = cursor.getInt(1);
			Item item = null;
			if(rtype == REFER_GUA){
				item=fetchGua(rid);
			}else if(rtype == REFER_MEM){
				item=fetchMem(rid);
			}else if(rtype==REFER_NOTE){
				item =fetchNote(rid);
			}
			if(item !=null){
				list.add(item);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}

	private static void checkDbH() {
		if(dbh == null){
			dbh = MyApplication.dbHelper.getWritableDatabase();
		}
	}

	private static Item fetchGua(int referId){
		checkDbH();
		Cursor cursor = dbh.rawQuery("SELECT title,date,time FROM gua WHERE _id=?", new String[]{String.valueOf(referId)});
		if(cursor.moveToFirst()){
			return new Item(cursor.getString(0),REFER_GUA,referId,cursor.getString(1),cursor.getString(2),1);
		}
		cursor.close();
		return null;
	}

	private static Item fetchNote(int referId){
		checkDbH();
		Cursor cursor = dbh.rawQuery("SELECT topic,started,ended FROM note WHERE _id=?", new String[]{String.valueOf(referId)});
		if(cursor.moveToFirst()){
			return new Item(cursor.getString(0),REFER_NOTE,referId,cursor.getString(1),cursor.getString(2),0);
		}
		cursor.close();
		return null;
	}

	private static Item fetchMem(int referId){
		checkDbH();
		Cursor cursor = dbh.rawQuery("SELECT contact FROM mem WHERE _id=?", new String[]{String.valueOf(referId)});
		if(cursor.moveToFirst()){
			return new Item(cursor.getString(0),REFER_MEM,referId,null,null,2);
		}
		cursor.close();
		return null;
	}

	//按日期和时间段查分组数据
	public static ArrayList<ArrayList<Item>> fetchByHours(String date, ArrayList<String> hourList){
		checkDbH();
		ArrayList<ArrayList<Item>> list =new ArrayList<ArrayList<Item>>();
		for(String hour:hourList){
			Cursor cursor = dbh.rawQuery("SELECT * FROM item WHERE date =? AND time>=? AND time<=?", new String[]{});
			cursor.moveToFirst();
			while(!cursor.isAfterLast()){
				//@todo add list
				cursor.moveToNext();
			}
			cursor.close();
		}
		return list;
	}

	public static HashMap<String,DayItem>fetchDayFlag(int startYYYYMMDD,int endYYYYMMDD) {
		checkDbH();
		Cursor cursor = dbh.rawQuery("select refer_type,date from item where date>=? and date<=? order by date asc", new String[]{startYYYYMMDD+"",endYYYYMMDD+""});
		HashMap<String,DayItem> ret = new HashMap<String,DayItem>();
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				String date_str = cursor.getString(1);
				if(!ret.containsKey(date_str)){
					DayItem it = new DayItem();
					int refer_type = Integer.valueOf(cursor.getString(0));
					if(refer_type==REFER_GUA){
						it.isGua = true;
					}else if(refer_type==REFER_NOTE){
						it.isNote = true;
					}
					ret.put(date_str,it);
				}else{
					DayItem it = ret.get(date_str);
					int refer_type = Integer.valueOf(cursor.getString(0));
					if(refer_type==REFER_GUA){
						it.isGua = true;
					}else if(refer_type==REFER_NOTE){
						it.isNote = true;
					}
					ret.put(date_str,it);
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
		return ret;
	}

	public static class DayItem {
		public boolean isGua;
		public boolean isNote;

		public DayItem(){
			isGua = false;
			isNote = false;
		}
	}

	public static class Item {
		public int referType;
		public int referId;
		public String title;
		public String showDaytime;

		public Item(String title,int referType, int referId,String start,String end,int type){
			this.title = title;
			this.referType = referType;
			this.referId=referId;

			if(start!=null&&end!=null){
				if(type==0){
					long start_l = Long.valueOf(start)*1000;
					long end_l = Long.valueOf(end)*1000;
					this.showDaytime = DateTime.Timestamp2String(start_l, "MM月dd日 HH:mm")+" - "+DateTime.Timestamp2String(end_l, "MM月dd日 HH:mm");
				}else{
					this.showDaytime = start+" "+end;
				}
			}else{
				this.showDaytime = "00月00日 00:00";
			}
		}
	}
}
