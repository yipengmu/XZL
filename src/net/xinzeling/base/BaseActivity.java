package net.xinzeling.base;

import net.xinzeling2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity implements OnClickListener{

	protected TextView txt_title;
	Button btn_back ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initTitleBar();
		
	}

	protected void initTitleBar() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		btn_back = (Button) findViewById(R.id.btn_back);
	}

	protected void setTitle(String title) {
		initTitleBar();
		if (txt_title != null) {
			txt_title.setText(title);
		}
	}

	protected String getAppName() {

		return getResources().getString(R.string.app_name);
	}
	
	public void toast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}
}
