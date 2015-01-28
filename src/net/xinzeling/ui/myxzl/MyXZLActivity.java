package net.xinzeling.ui.myxzl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xinzeling.base.BaseActivity;
import net.xinzeling.setting.UsrEditActivity;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class MyXZLActivity extends BaseActivity implements OnCheckedChangeListener {
	private int FLAG_INTENT_FOR_PERSONAL_INFO;
	private RadioButton mHistoryRadioButton;
	private ListView lv_my_history_guas;
	private MyHistoryGuaAdapter mAdatper;
	private RelativeLayout rl_choose_time;
	private RelativeLayout rl_choose_type;

	private PopupWindow mPopDateSectionWindow;
	private PopupWindow mPopTypeChooseWindow;

	private DatePicker datePickerStart;
	private DatePicker datePickerEnd;
	private ListView mTypeChooseListView;
	private Button btn_submit_date_section;
	private List<? extends Map<String, ?>> mChooseTypeDataSource;

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
		if (null != mPopDateSectionWindow) {
			mPopDateSectionWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	/*
	 * 创建PopupWindow
	 */
	private void initPopuptWindow() {
		initPopDateSetion();
		initPopTypeChoose();
	}

	private void initPopTypeChoose() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.pop_window_myhistory_type_choose, null);
		popupWindow.findViewById(R.id.ll_pop_myhistory_type_choose_bg).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopTypeChooseWindow != null) {
					mPopTypeChooseWindow.dismiss();
				}
			}
		});
		mTypeChooseListView = (ListView) popupWindow.findViewById(R.id.lv_pop_myhistory_type_choose);
		mChooseTypeDataSource = getTypeChooseData();
		SimpleAdapter adapter = new SimpleAdapter(this, mChooseTypeDataSource , R.layout.layout_typechoose_item, new String[] { "iteminfo" }, new int[] { R.id.tv_iteminfo });
		mTypeChooseListView.setAdapter(adapter);
		mTypeChooseListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String ch = (String) mChooseTypeDataSource.get(position).get("iteminfo");
				toast(ch);
			}
		});
		
		// 创建一个PopupWindow
		mPopTypeChooseWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	private List<? extends Map<String, ?>> getTypeChooseData() {

		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		String[] dataSource = new String[]{"全部","恋爱","财运","日常购物"};
		
		for(int i = 0;i<dataSource.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("iteminfo", dataSource[i]);
			list.add(map);			
		}
		return list;
	}

	private void initPopDateSetion() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View popupWindow = layoutInflater.inflate(R.layout.popwindow_myhistory_datepick, null);
		popupWindow.findViewById(R.id.ll_pop_myhistory_bg).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mPopDateSectionWindow != null) {
					mPopDateSectionWindow.dismiss();
				}
			}
		});
		btn_submit_date_section = (Button) popupWindow.findViewById(R.id.btn_submit_date_section);
		datePickerStart = (DatePicker) popupWindow.findViewById(R.id.dp_start);
		datePickerEnd = (DatePicker) popupWindow.findViewById(R.id.dp_end);
		// 创建一个PopupWindow
		mPopDateSectionWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		btn_submit_date_section.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 确认time section
			}
		});
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

			mPopDateSectionWindow.dismiss();
			mPopTypeChooseWindow.dismiss();
			mPopDateSectionWindow.showAsDropDown(v, 0, 0);// X、Y方向各偏移50
			break;
		case R.id.rl_choose_type:
			// 选择类型
			getPopupWindowInstance();
			mPopDateSectionWindow.dismiss();
			mPopTypeChooseWindow.dismiss();
			mPopTypeChooseWindow.showAsDropDown(v,  0, 0);
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
