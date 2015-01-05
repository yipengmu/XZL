package net.xinzeling.lib;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;  

public class FontManager {
	private static String fontsname = "fonts/fangzlt.ttf";
	public static Typeface getTypeface(Context context) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(),fontsname);
		return tf;
	}
	
	public static void changeFonts(ViewGroup root, Activity act) {
//		Typeface tf = Typeface.createFromAsset(act.getAssets(),fontsname);
//		for (int i = 0; i < root.getChildCount(); i++) {
//			View v = root.getChildAt(i);
//			if (v instanceof TextView) {
//				((TextView) v).setTypeface(tf);
//			} else if (v instanceof Button) {
//				((Button) v).setTypeface(tf);
//			} else if (v instanceof EditText) {
//				((EditText) v).setTypeface(tf);
//			} else if (v instanceof ViewGroup) {
//				changeFonts((ViewGroup) v, act);
//			}    
//		}
	}

	public static void changeFonts(ViewGroup root, Activity act,Typeface tf) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act);
			}    
		}
	}

}
