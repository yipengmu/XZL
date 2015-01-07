package net.xinzeling.setting;

import net.xinzeling.base.BaseActivity;
import net.xinzeling2.R;
import android.os.Bundle;

public class MainSettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_setting);
		
		initView();
		
	}

	private void initView() {
		setTitle(getAppName());
		
		
	}

}
