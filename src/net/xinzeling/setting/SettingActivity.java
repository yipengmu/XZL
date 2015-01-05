package net.xinzeling.setting;

import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.FontManager;
import net.xinzeling2.R;
import net.xinzeling2.R.layout;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(SettingActivity.this), SettingActivity.this);
	}

}
