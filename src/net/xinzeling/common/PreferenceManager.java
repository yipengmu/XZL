package net.xinzeling.common;

import net.xinzeling.MyApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {

	public static PreferenceManager ins;

	private static Editor editor ;
	public static SharedPreferences sharedPreference = MyApplication.getContext().getSharedPreferences("usr", Context.MODE_PRIVATE);
	
	private PreferenceManager() {
		editor = sharedPreference.edit();
	}
	
	public static PreferenceManager getInstance(){
		if(ins == null){
			ins = new PreferenceManager();
		}
		return ins;
	}
	
	public static void setPreference(String key,boolean value){

		editor.putBoolean(key, value);
		editor.apply();
	}
	
	public static void setPreference(String key,String value){
		editor.putString(key, value);
		editor.apply();
	}
	
	public static void setPreference(String key,int value){
		editor.putInt(key, value);
		editor.apply();
	}
	
	public static String getPreferenceString(String key){
		return sharedPreference.getString(key, "");
	}
	
	public static int getPreferenceInt(String key,int defaultInt){
		return sharedPreference.getInt(key, defaultInt);
	}
	
	public static boolean getPreferenceBoolean(String key, boolean defaultBoolean){
		return sharedPreference.getBoolean(key, defaultBoolean);
	}
}
