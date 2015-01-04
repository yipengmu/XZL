package net.xinzeling.model;

import java.util.ArrayList;

import net.xinzeling.lib.AppBase;
import android.content.ContentValues;
import android.database.Cursor;

public class GuaCntModel extends AppBase{
	
	public static void update(GuaCnt guacnt){
		ContentValues values = new ContentValues();
		values.put("cnt", guacnt.cnt);
		values.put("next_time", guacnt.next_time);
		dbh.update("gua_tj", values, "_id=?", new String[]{String.valueOf(guacnt._id)});
	}

	public static GuaCnt fetch(int typeid){
		Cursor cursor = dbh.rawQuery("SELECT _id,type,cnt,all_cnt,next_time,xz_id FROM gua_tj WHERE type=?", new String[]{String.valueOf(typeid)});
		if(cursor.moveToFirst()){
			GuaCnt guacnt = new GuaCnt();
			guacnt._id = cursor.getInt(0);
			guacnt.type = cursor.getInt(1);
			guacnt.cnt = cursor.getInt(2);
			guacnt.all_cnt = cursor.getInt(3);
			guacnt.next_time = cursor.getInt(4);
			guacnt.xz_id = cursor.getInt(5);
			checkUsable(guacnt);
			cursor.close();
			return guacnt;
		}
		return null;
	}
	
	private static void checkUsable(GuaCnt guacnt){
		if(guacnt!=null&&guacnt.next_time!=0){
			long now_timestamp = AppBase.NowTime();
			if(guacnt.next_time>now_timestamp)return;
		}
		guacnt.isUsable = true;
	}

	public static ArrayList<GuaCnt> fetchAll(){
		ArrayList<GuaCnt> list = new ArrayList<GuaCnt>();
		Cursor cursor = dbh.rawQuery("SELECT _id,type,cnt,all_cnt,next_time,xz_id FROM gua_tj ORDER BY _id DESC LIMIT 0,100", null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			GuaCnt guacnt = new GuaCnt();
			guacnt._id = cursor.getInt(0);
			guacnt.type = cursor.getInt(1);
			guacnt.cnt = cursor.getInt(2);
			guacnt.all_cnt = cursor.getInt(3);
			guacnt.next_time = cursor.getInt(4);
			guacnt.xz_id = cursor.getInt(5);
			checkUsable(guacnt);
			list.add(guacnt);
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
		
	public static class GuaCnt {
		public int _id = 0;//
		public int type=1; //卦类型
		public int cnt=0;//已算卦次数
		public int all_cnt=0;//系统默认可算次数
		public int next_time=0;//下次可算挂时间戳
		public int xz_id;//限制id 1 day 2 month 3 year
		public boolean isUsable=false;		
	}
}
