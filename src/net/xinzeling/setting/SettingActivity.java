package net.xinzeling.setting;

import net.xinzeling.base.BaseActivity;
import net.xinzeling2.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		setTitle(getResources().getString(R.string.setting));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
