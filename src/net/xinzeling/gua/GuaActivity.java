package net.xinzeling.gua;

import java.util.ArrayList;
import java.util.List;

import net.xinzeling.HomeActivity;
import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.common.PreferenceManager;
import net.xinzeling.lib.RadioGroup;
import net.xinzeling.lib.RadioGroup.OnCheckedChangeListener;
import net.xinzeling2.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GuaActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private int mode;
	private TextView txtTitle;
	private View winFrame;
	private LinearLayout lastShowChildView;
	private DisplayMetrics display;
	private LayoutAnimationController fadeInAnim;
	private LayoutAnimationController popupAnim;
	private int type = 0;
	private BackHomeBroadcastReceiver backhomeBReceiver;
	private RadioGroup myradiogroup;
	private RelativeLayout r_title;
	private float ButtonOneWordWidth;
	private ImageView iv_title_mid_icon;
	private long mFirstime;
	private final String PreferTag = "gua_mode";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua);
		
		mode = getIntent().getIntExtra("mode",PreferenceManager.getInstance().getPreferenceInt(PreferTag, MyApplication.nav_gua_num));
		
		txtTitle = (TextView) findViewById(R.id.txt_title);
		iv_title_mid_icon = (ImageView) findViewById(R.id.iv_title_mid_icon);
		txtTitle.setOnClickListener(this);
		winFrame = findViewById(R.id.frame_main);
		r_title = (RelativeLayout) findViewById(R.id.select_type_menu);
		display = this.getResources().getDisplayMetrics();
		fadeInAnim = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.fade_in));
		
		popupAnim = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.popup));

		findViewById(R.id.btn_back).setVisibility(View.GONE);
		backhomeBReceiver = new BackHomeBroadcastReceiver();
		registerReceiver(backhomeBReceiver, new IntentFilter(MyApplication.GUA_BACK_HOME_BROADCAST));

		myradiogroup = (RadioGroup) findViewById(R.id.gua_myradiogroup);
		myradiogroup.setOnCheckedChangeListener(this);

		((RadioGroup) findViewById(R.id.radiogroup_type_menu)).setOnCheckedChangeListener(this);
		
		UpdateContentView();
		
	}

	private void UpdateContentView() {
		popupAnim.setDelay(1);
		fadeInAnim.setDelay(1);
		updateTitleGuaStyle(mode);
	}


	private void updateTitleGuaStyle(int mode){
		switch (mode) {
		case MyApplication.nav_gua_num:
			txtTitle.setText("数字求卦");
			iv_title_mid_icon.setBackground(getResources().getDrawable(R.drawable.btn_nav_gua_num));
			break;
		case MyApplication.nav_gua_photo:
			txtTitle.setText("照片求卦");	
			iv_title_mid_icon.setBackground(getResources().getDrawable(R.drawable.btn_nav_gua_camera));
			break;
		case MyApplication.nav_gua_time:
			txtTitle.setText("时间求卦");
			iv_title_mid_icon.setBackground(getResources().getDrawable(R.drawable.btn_nav_gua_time));
			break;
		}
		//隐藏menu
		r_title.setVisibility(View.GONE);

	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(backhomeBReceiver);
		super.onDestroy();
	}

	private void backHome() {
		Intent intent = new Intent(GuaActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	private class NewButton {
		public String buttonName;
		public OnClickListener mclickListener;

		public NewButton(String buttonName, OnClickListener l) {
			this.buttonName = buttonName;
			this.mclickListener = l;
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
		case R.id.btn_home:
			backHome();
			finish();
			break;
		case R.id.txt_title:
			if (r_title.getVisibility() != View.VISIBLE) {
				r_title.setVisibility(View.VISIBLE);
				((ScrollView) findViewById(R.id.scroll_win)).setClickable(false);
			} else {
				r_title.setVisibility(View.GONE);
				((ScrollView) findViewById(R.id.scroll_win)).setClickable(true);
			}
			break;
		default:
			break;
		}
	}

	private NewButton createNewButton(String name, final int type) {
		return new NewButton(name, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gototarget(type);
			}

		});
	}

	@SuppressLint("NewApi")
	private void createChild(int linearlayout_id, Context context, List<NewButton> list) {
		lastShowChildView = (LinearLayout) findViewById(linearlayout_id);
		LinearLayout ln = createNewLinearLayout(context);
		lastShowChildView.addView(ln);

		float now_width = 0L;
		final float ln_width = MyApplication.getWidthHeight()[0];
		for (int i = 0; i < list.size(); i++) {
			Button btn = new Button(context);
			btn.setText(list.get(i).buttonName);
			btn.setTextSize(12);
			btn.setOnClickListener(list.get(i).mclickListener);
			float btn_width = list.get(i).buttonName.length() * 2 * 16 + 15;

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 5, 50, 5);
			btn.setLayoutParams(lp);
			btn.setPadding(2,2,2,2);
			btn.setTextAppearance(this, R.style.gua_add_child_button);
			btn.setTextSize(14);
			btn.setTextColor(getResources().getColor(R.color.bg_green));
			btn.setBackground(getResources().getDrawable(R.drawable.add_child_selector));

			if (now_width == 0L) {
				now_width = btn_width;
				ln.addView(btn);
			} else if (now_width + btn_width + 80 > ln_width) {
				now_width = btn_width;
				ln.addView(btn);
				ln = createNewLinearLayout(context);
				lastShowChildView.addView(ln);
			} else {
				now_width += btn_width;
				ln.addView(btn);
			}
		}
		lastShowChildView.setVisibility(View.VISIBLE);
	}

	private LinearLayout createNewLinearLayout(Context context) {
		LinearLayout ln = new LinearLayout(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 5, 10, 5);
		ln.setLayoutParams(lp);
		ln.setOrientation(LinearLayout.HORIZONTAL);
		return ln;
	}

	private void gototarget(int type) {
		Intent intent = new Intent(GuaActivity.this, QiuGuaActivity.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		intent.putExtra("mode", mode);
		intent.putExtra("type", type);
		startActivity(intent);
	}

	private class BackHomeBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			backHome();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondtime = System.currentTimeMillis();
			if (secondtime - mFirstime > 3000) {
				Toast.makeText(this, "再按一次返回键退出信则聆", Toast.LENGTH_SHORT).show();
				mFirstime = System.currentTimeMillis();
				return true;
			} else {
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.radio_num:
			PreferenceManager.getInstance().setPreference(PreferTag, MyApplication.nav_gua_num);
			mode = MyApplication.nav_gua_num;
			UpdateContentView();
			break;
		case R.id.radio_photo:
			PreferenceManager.getInstance().setPreference(PreferTag, MyApplication.nav_gua_photo);
			mode = MyApplication.nav_gua_photo;
			UpdateContentView();
			break;
		case R.id.radio_time:
			PreferenceManager.getInstance().setPreference(PreferTag, MyApplication.nav_gua_time);
			mode = MyApplication.nav_gua_time;
			UpdateContentView();
			break;
		}

		if (lastShowChildView != null && lastShowChildView.getVisibility() == View.VISIBLE) {
			lastShowChildView.setVisibility(View.GONE);
			lastShowChildView.removeAllViewsInLayout();
		}
		List<NewButton> params = null;
		switch (checkedId) {
		// 常规类型
		case R.id.gua_menu_caiyun:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("近期财运", 10));
			params.add(createNewButton("求财之事", 11));
			params.add(createNewButton("借贷", 12));
			params.add(createNewButton("讨债", 13));
			createChild(R.id.child_tag_1, GuaActivity.this, params);
			break;
		case R.id.gua_menu_danshen:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("今日运程", 21));
			params.add(createNewButton("近期桃花", 22));
			createChild(R.id.child_tag_2, GuaActivity.this, params);
			break;
		case R.id.gua_menu_lianrenlianqing:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("今日运程", 23));
			params.add(createNewButton("恋爱状况", 24));
			createChild(R.id.child_tag_2, GuaActivity.this, params);
			break;
		case R.id.gua_menu_gouwu:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("去逛街", 35));
			params.add(createNewButton("网购", 36));
			createChild(R.id.child_tag_2, GuaActivity.this, params);
			break;
		case R.id.gua_menu_yihundashi:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("近期桃花", 25));
			params.add(createNewButton("婚姻状况", 26));
			createChild(R.id.child_tag_3, GuaActivity.this, params);
			break;
		case R.id.gua_menu_jujiajianshe:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("买房", 37));
			params.add(createNewButton("买车", 38));
			params.add(createNewButton("装修", 39));
			createChild(R.id.child_tag_3, GuaActivity.this, params);
			break;
		case R.id.gua_menu_jiankang:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("今日运程", 40));
			params.add(createNewButton("近期状况", 41));
			params.add(createNewButton("病症状况", 42));
			createChild(R.id.child_tag_4, GuaActivity.this, params);
			break;
		case R.id.gua_menu_fangwuzhuzhai:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("家宅房屋", 43));
			params.add(createNewButton("出租房屋", 44));
			params.add(createNewButton("求租房屋", 45));
			createChild(R.id.child_tag_4, GuaActivity.this, params);
			break;
		case R.id.gua_menu_shiye:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("年内事业运", 29));
			params.add(createNewButton("创业选择", 30));
			params.add(createNewButton("合作项目", 31));
			params.add(createNewButton("谈判成败", 32));
			params.add(createNewButton("另谋高就", 33));
			params.add(createNewButton("今日运程", 34));
			createChild(R.id.child_tag_4, GuaActivity.this, params);
			break;
		case R.id.gua_menu_chuxing:
			params = new ArrayList<NewButton>();
			params.add(createNewButton("公务出差", 18));
			params.add(createNewButton("旅行出游", 19));
			params.add(createNewButton("每日出行", 20));
			createChild(R.id.child_tag_5, GuaActivity.this, params);
			break;
		case R.id.gua_menu_15:
			gototarget(15);
			break;
		case R.id.gua_menu_16:
			gototarget(16);
			break;
		case R.id.gua_menu_17:
			gototarget(17);
			break;
		case R.id.gua_menu_27:
			gototarget(27);
			break;
		case R.id.gua_menu_41:
			gototarget(41);
			break;
		}
	}
}