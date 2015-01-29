package net.xinzeling.ui.myxzl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xinzeling.adapter.ItemAdapter;
import net.xinzeling.base.BaseActivity;
import net.xinzeling.gua.JieGuaActivity;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.lib.DateTime;
import net.xinzeling.model.ItemModel;
import net.xinzeling.model.ItemModel.Item;
import net.xinzeling.note.NoteCheckActivity;
import net.xinzeling.setting.UsrEditActivity;
import net.xinzeling2.R;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyXZLActivity extends BaseActivity implements OnCheckedChangeListener {
	private int FLAG_INTENT_FOR_PERSONAL_INFO;
	private RadioButton mHistoryRadioButton;
	private ListView mHistoryGuas;
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
	private int FlagDateSection = 1;
	private int FlagTypeChoose = 2;
	private View mCellsecent;
	private TextView tv_choose_time;
	private TextView tv_choose_type;

	private Calendar startCalendar;
	private Calendar endCalendar;

	private ArrayList<Item> itemList;
	private ItemAdapter itemAdapter;
	private int selectDateYYYYMMDD;
	private Item item;// 当前选中的item
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_xzl);
		setTitle("我的");

		initView();
	}

	private void initView() {
		mHistoryRadioButton = (RadioButton) findViewById(R.id.rb_my_history_gua);
		mHistoryGuas = (ListView) findViewById(R.id.lv_my_history_guas);
		rl_choose_time = (RelativeLayout) findViewById(R.id.rl_choose_time);
		rl_choose_type = (RelativeLayout) findViewById(R.id.rl_choose_type);
		mCellsecent = findViewById(R.id.v_xzl_center_cent);
		tv_choose_time = (TextView) findViewById(R.id.tv_choose_time);
		tv_choose_type = (TextView) findViewById(R.id.tv_choose_type);

		rl_choose_time.setOnClickListener(this);
		rl_choose_type.setOnClickListener(this);

		mAdatper = new MyHistoryGuaAdapter(this);
		mAdatper.setData(getHistoryData());
		mHistoryGuas.setAdapter(mAdatper);

		startCalendar = Calendar.getInstance();
		endCalendar = Calendar.getInstance();

		findViewById(R.id.rb_my_personel_info).setOnClickListener(this);
		

		mHistoryGuas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
				item = itemList.get(position);
				if (item.referType == ItemModel.REFER_GUA) {
					Intent intent = new Intent(MyXZLActivity.this, JieGuaActivity.class);
					intent.putExtra("guaid", item.referId);
					MyXZLActivity.this.startActivity(intent);
				} else if (item.referType == ItemModel.REFER_NOTE) {
					BlurBehind.getInstance().execute(MyXZLActivity.this, new Runnable() {
						public void run() {
							Intent intent = new Intent(MyXZLActivity.this, NoteCheckActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
							intent.putExtra("noteid", item.referId);
							startActivity(intent);
						}
					});
				}
			}
			
		});
	}

	@Override
	protected void onResume() {
		updateMyGuas();
		super.onResume();

	}
	
	private ArrayList<HistoryGuaBean> getHistoryData() {
		return null;
	}

	/*
	 * 获取PopupWindow实例
	 */
	private void getPopupWindowInstance(int type) {
		if (type == FlagDateSection) {
			initPopDateSetion();
		} else if (type == FlagTypeChoose) {
			initPopTypeChoose();
		}
	}

	public void hidePopupWindow(int type) {
		if (type == FlagDateSection) {
			if (mPopDateSectionWindow != null) {
				mPopDateSectionWindow.dismiss();
			}
		} else if (type == FlagTypeChoose) {
			if (mPopTypeChooseWindow != null) {
				mPopTypeChooseWindow.dismiss();
			}
		}

	}

	private void initPopTypeChoose() {
		if (mTypeChooseListView != null) {
			return;
		}
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
		SimpleAdapter adapter = new SimpleAdapter(this, mChooseTypeDataSource, R.layout.layout_typechoose_item, new String[] { "iteminfo" }, new int[] { R.id.tv_iteminfo });
		mTypeChooseListView.setAdapter(adapter);
		mTypeChooseListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String ch = (String) mChooseTypeDataSource.get(position).get("iteminfo");
				tv_choose_type.setText(ch);
				toast(ch);
				hidePopupWindow(FlagTypeChoose);
			}
		});

		// 创建一个PopupWindow
		mPopTypeChooseWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	private List<? extends Map<String, ?>> getTypeChooseData() {

		List<Map<String, ?>> list = new ArrayList<Map<String, ?>>();
		String[] dataSource = new String[] { "全部", "恋爱", "财运", "日常购物" };

		for (int i = 0; i < dataSource.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("iteminfo", dataSource[i]);
			list.add(map);
		}
		return list;
	}

	private void initPopDateSetion() {
		if (mPopDateSectionWindow != null) {
			return;
		}
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

		datePickerStart.init(startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {

			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				toast("您选择的日期是：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
				startCalendar.set(Calendar.YEAR, year);
				startCalendar.set(Calendar.MONTH, monthOfYear);
				startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			}

		});
		datePickerEnd.init(endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH), new OnDateChangedListener() {

			public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				toast("您选择的日期是：" + year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
				endCalendar.set(Calendar.YEAR, year);
				endCalendar.set(Calendar.MONTH, monthOfYear);
				endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			}

		});

		// 创建一个PopupWindow
		mPopDateSectionWindow = new PopupWindow(popupWindow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		btn_submit_date_section.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 确认time section
				tv_choose_time.setText(getStartDateStr() + " ~ " + getEndDateStr());
			}
		});
	}

	protected String getStartDateStr() {
		return "" + startCalendar.get(Calendar.YEAR) + "-" + (startCalendar.get(Calendar.MONTH)+1) + "-" + startCalendar.get(Calendar.DAY_OF_MONTH);
	}

	protected String getEndDateStr() {
		return "" + endCalendar.get(Calendar.YEAR) + "-" + (endCalendar.get(Calendar.MONTH)+1) + "-" + endCalendar.get(Calendar.DAY_OF_MONTH);
	}

	public void updateMyGuas(){
		selectDateYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(startCalendar.getTime()));
		new LoadTask().execute(0, selectDateYYYYMMDD);
	}
	
	// 参数1 类型 参数2 日期
		private class LoadTask extends AsyncTask<Integer, Void, Void> {

			@Override
			protected Void doInBackground(Integer... args) {
				try {
					itemList = ItemModel.getItemList(args[0], args[1] + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (itemAdapter == null) {
					itemAdapter = new ItemAdapter(MyXZLActivity.this, itemList);
					mHistoryGuas.setAdapter(itemAdapter);
				} else {
					itemAdapter.notifyDataSetChanged(itemList);
				}
			}
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
			getPopupWindowInstance(FlagDateSection);

			hidePopupWindow(FlagTypeChoose);

			if (mPopDateSectionWindow.isShowing()) {
				mPopDateSectionWindow.dismiss();
			} else {
				mPopDateSectionWindow.showAsDropDown(mCellsecent, 0, 0);
			}
			break;
		case R.id.rl_choose_type:
			// 选择类型
			getPopupWindowInstance(FlagTypeChoose);
			hidePopupWindow(FlagDateSection);

			if (mPopTypeChooseWindow.isShowing()) {
				mPopTypeChooseWindow.dismiss();
			} else {
				mPopTypeChooseWindow.showAsDropDown(mCellsecent, 0, 0);
			}
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
