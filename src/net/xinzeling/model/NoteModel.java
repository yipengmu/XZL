package net.xinzeling.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import net.xinzeling.MyApplication;
import net.xinzeling.lib.DateTime;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class NoteModel extends MyApplication{
	
	public static long add(String title,String contact,String content,long started,long ended,int befored,int repeat_type){
		ContentValues value=new ContentValues();
		value.put("topic", title);
		value.put("contact", contact);
		value.put("content", content);
		value.put("started",started);
		value.put("ended", ended);
		value.put("startedCal",new Date(started).toGMTString());
		value.put("endedCal",new Date(ended).toGMTString());
		value.put("befored", befored);
		value.put("repeat_type", repeat_type);
		long noteid=dbh.insert("note", null, value);
		String str = DateTime.getTodayYmd(null);
		Log.d("Datetime", " Datetime ：" + value.toString());
		ItemModel.add(ItemModel.REFER_NOTE, noteid,str);
		return noteid;
	}

	public static void edit(int noteid,int iscancel){
		String sql ="UPDATE note SET iscancel=%d WHERE _id=%d";
		sql = String.format(Locale.CHINA, sql, iscancel,noteid);
		dbh.execSQL(sql);
	}
	
	public static void edit(int noteid,String title,String contact,String content,int started,int ended,int befored,int iscancel){
		String sql ="UPDATE note SET topic='%s',contact='%s',content='%s',started=%d,ended=%d," +
				"befored=%d ,iscancel=%d WHERE _id=%d";
		sql = String.format(Locale.CHINA, sql, title,contact,content,started,ended,befored,iscancel,noteid);
		dbh.execSQL(sql);
	}

	public static void edit(int noteid,String title,String contact,String content,int started,int ended,int befored){
		String sql ="UPDATE note SET topic='%s',contact='%s',content='%s',started=%d,ended=%d," +
				"befored=%d WHERE _id=%d";
		sql = String.format(Locale.CHINA, sql, title,contact,content,started,ended,befored,noteid);
		dbh.execSQL(sql);
	}

	public static void edit(int noteid,String title,String contact,String content,int started,int ended,int befored,int iscancel,int repeat_type){
		String sql ="UPDATE note SET topic='%s',contact='%s',content='%s',started=%d,ended=%d," +
				"befored=%d,iscancel=%d,repeat_type=%d WHERE _id=%d";
		sql = String.format(Locale.CHINA, sql, title,contact,content,started,ended,befored,iscancel,repeat_type,noteid);
		dbh.execSQL(sql);
	}

	public static Note fetch(int noteid){
		if(dbh == null){
			return null;
		}
		Cursor cursor = dbh.rawQuery("SELECT topic,contact,content,started,ended,befored,iscancel,repeat_type FROM note WHERE _id=?", new String[]{String.valueOf(noteid)});
		Note note = null;
		if(cursor != null && cursor.moveToFirst()){
			note=new Note(cursor.getString(0),cursor.getString(1),cursor.getString(2),
					cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),cursor.getInt(7));
		}
		cursor.close();
		return note;
	}
	
	public static ArrayList<Note> fetchAll(){
		ArrayList<Note> list = new ArrayList<Note>();
		Cursor cursor = dbh.rawQuery("SELECT topic,contact,content,started,ended,befored,iscancel,repeat_type FROM note ORDER BY noteid DESC ", new String[]{});
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			list.add(new Note(cursor.getString(0),cursor.getString(1),cursor.getString(2),
					cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6),cursor.getInt(7)));
			cursor.moveToNext();
		}
		cursor.close();
		return list;
	}
	
	public static void del(int noteid){
		String sql = String.format(Locale.CHINA, "DELETE FROM note WHERE noteid=%d", noteid);
		dbh.execSQL(sql);
	}
		
	public static class Note{
		public int noteid;
		public String topic;
		public String contact;
		public String content;
		public int started;
		public int ended;
		public int befored;
		public int repeat_type = 0;
		public int iscancel = 0;
		
		public Note(String topic,String contact,String content,int started,int ended,int befored,int iscancel,int repeat_type){
			this.topic = topic;
			this.contact = contact;
			this.content = content;
			this.started = started;
			this.ended = ended;
			this.befored = befored;
			this.iscancel = iscancel;
			this.repeat_type = repeat_type;
		}
	}
}