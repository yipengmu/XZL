package net.xinzeling;

import net.xinzeling.gua.GuaActivity;
import net.xinzeling.lib.AppManager;
import net.xinzeling.note.NoteActivity;
import net.xinzeling.setting.SigninActivity;
import net.xinzeling2.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity implements OnTouchListener, OnGestureListener {

	private static final int FLING_MIN_DISTANCE = 100;
	private static final int FLING_MIN_VELOCITY = 0;
	private static final int FLING_MAX_Y_VELOCITY = 30;
	private TabHost tabHost;
	private GestureDetector mGestureDetector;
	private RelativeLayout[] tab_menu;
	private int[] tab_default_selector = { R.drawable.tab_home_selector, R.drawable.tab_gua_selector, R.drawable.tab_note_selector, R.drawable.tab_usr_selector };
	private long mFirstime = 0L;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppManager.getAppManager().addActivity(getCurrentActivity());
		mGestureDetector = new GestureDetector(this);
		RelativeLayout ll = (RelativeLayout) findViewById(R.id.linew);
		ll.setOnTouchListener(this);
		ll.setLongClickable(true);
		initTab();
	}

	private void initTab() {

		tabHost = getTabHost();

		// 初始化 4个模块
		RelativeLayout menu_view_home = createView(0);
		tabHost.addTab(tabHost.newTabSpec("home").setIndicator(menu_view_home).setContent(new Intent(this, HomeActivity.class)));
		RelativeLayout menu_view_gua = createView(1);
		tabHost.addTab(tabHost.newTabSpec("gua").setIndicator(menu_view_gua).setContent(new Intent(this, GuaActivity.class)));
		RelativeLayout menu_view_note = createView(2);
		tabHost.addTab(tabHost.newTabSpec("note").setIndicator(menu_view_note).setContent(new Intent(this, NoteActivity.class)));
		RelativeLayout menu_view_setting = createView(3);
		tabHost.addTab(tabHost.newTabSpec("setting").setIndicator(menu_view_setting).setContent(new Intent(this, SigninActivity.class)));

		tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
			public void onTabChanged(String arg0) {
			}
		});
	}

	private RelativeLayout createView(int id) {
		tab_menu = new RelativeLayout[4];
		tab_menu[id] = (RelativeLayout) getLayoutInflater().inflate(R.layout.tab_wighet, null);
		ImageView tab_imageView = (ImageView) tab_menu[id].findViewById(R.id.tab_imageView);
		// tab_imageView.setImageDrawable(getResources().getDrawable(drawable_id));
		tab_imageView.setImageResource(tab_default_selector[id]);
		return tab_menu[id];
	}

	public boolean onDown(MotionEvent arg0) {

		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		if (Math.abs(velocityY) > FLING_MAX_Y_VELOCITY)
			return false;
		int total = tabHost.getTabWidget().getChildCount();
		int current = tabHost.getCurrentTab();
		int index = 0;
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling left
			// Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();

			index = current + 1 > total ? total - 1 : current + 1;
			tabHost.setCurrentTab(index);
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			// Fling right
			index = current - 1 < 0 ? 0 : current - 1;
			// Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
			tabHost.setCurrentTab(index);
		}

		return false;
	}

	public void onLongPress(MotionEvent arg0) {

	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {

		return false;
	}

	public void onShowPress(MotionEvent arg0) {

	}

	public boolean onSingleTapUp(MotionEvent arg0) {

		return false;
	}

	public boolean onTouch(View arg0, MotionEvent event) {

		return mGestureDetector.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - mFirstime > 3000) {
				Toast.makeText(MainActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
				mFirstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * listview 和水平滑动事件冲突
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (mGestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}

		return super.dispatchTouchEvent(event);
	}

}
