package net.xinzeling.base;

import com.umeng.analytics.MobclickAgent;

import net.xinzeling2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity implements OnClickListener {

	protected TextView txt_title;
	Button btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initTitleBar();

	}

	private void initTitleBar() {
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

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 监听按下返回键
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			/*
			 * 已经是最后一个fragment getSupportFragmentManager()或者getFragmentManager()
			 * 具体要看你add to back stack 是用哪个
			 */
			// if no more history in stack
			if (this.getFragmentManager().getBackStackEntryCount() == 0) {
				// 显示退出框业务逻辑
				return super.onKeyDown(keyCode, event);
			} else {
				getFragmentManager().popBackStack();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
