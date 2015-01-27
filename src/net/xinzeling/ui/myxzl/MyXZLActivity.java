package net.xinzeling.ui.myxzl;

import java.util.ArrayList;

import net.xinzeling.base.BaseActivity;
import net.xinzeling.setting.UsrEditActivity;
import net.xinzeling2.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

public class MyXZLActivity extends BaseActivity implements OnCheckedChangeListener {
	private int FLAG_INTENT_FOR_PERSONAL_INFO;
	private RadioButton mHistoryRadioButton;
	private ListView lv_my_history_guas;
	private MyHistoryGuaAdapter mAdatper;
	private RelativeLayout rl_choose_time;
	private RelativeLayout rl_choose_type;

	private PopupWindow mPopupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_xzl);
		setTitle("我的");

		initView();
	}

	private void initView() {
		mHistoryRadioButton = (RadioButton) findViewById(R.id.rb_my_history_gua);
		lv_my_history_guas = (ListView) findViewById(R.id.lv_my_history_guas);
		rl_choose_time = (RelativeLayout) findViewById(R.id.rl_choose_time);
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		rl_choose_time.setOnClickListener(this);
		rl_choose_type.setOnClickListener(this);

		mAdatper = new MyHistoryGuaAdapter(this);
		mAdatper.setData(getHistoryData());
		lv_my_history_guas.setAdapter(mAdatper);
		findViewById(R.id.rb_my_personel_info).setOnClickListener(this);
	}

	private ArrayList<HistoryGuaBean> getHistoryData() {
		return null;
	}

	/*
	 * 获取PopupWindow实例
	 */
	private void getPopupWindowInstance() {
		if (null != mPopupWindow) {
			mPopupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.popwindow_myhistory_datepick, null);
		DatePicker datePicker = (DatePicker) popupWindow.findViewById(R.id.dp_start);

		// 创建一个PopupWindow
		// 参数1：contentView 指定PopupWindow的内容
		// 参数2：width 指定PopupWindow的width
		// 参数3：height 指定PopupWindow的height
		mPopupWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		// 获取屏幕和PopupWindow的width和height
		// mScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
		// mScreenWidth = getWindowManager().getDefaultDisplay().getHeight();
		// mPopupWindowWidth = mPopupWindow.getWidth();
		// mPopupWindowHeight = mPopupWindow.getHeight();
	}

	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rb_my_history_gua:
			// 卜过的卦

			break;
		case R.id.rb_my_personel_info:
			// 个人信息
			intent.setClass(MyXZLActivity.this, UsrEditActivity.class);
			startActivityForResult(intent, FLAG_INTENT_FOR_PERSONAL_INFO);
			break;
		case R.id.rl_choose_time:
			// 选择时间
			getPopupWindowInstance();
			mPopupWindow.showAsDropDown(v, 0, 0);// X、Y方向各偏移50
			break;
		case R.id.rl_choose_type:
			// 选择类型
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mHistoryRadioButton.setChecked(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub

	}
}
