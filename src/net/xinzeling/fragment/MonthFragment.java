package net.xinzeling.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.xinzeling.HomeActivity;
import net.xinzeling.MainActivity;
import net.xinzeling.adapter.ItemAdapter;
import net.xinzeling.common.CommonDefine;
import net.xinzeling.gua.JieGuaActivity;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.lib.CalendarView;
import net.xinzeling.lib.CalendarView.CalendarListener;
import net.xinzeling.lib.DateTime;
import net.xinzeling.model.ItemModel;
import net.xinzeling.model.ItemModel.Item;
import net.xinzeling.model.LunarModel;
import net.xinzeling.news.GuaNewsDetailActivity;
import net.xinzeling.note.NoteCheckActivity;
import net.xinzeling2.R;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 
 * @author laomu 首页主体部分
 * 
 * */
public class MonthFragment extends Fragment implements OnLongClickListener,OnClickListener, OnItemClickListener, OnCheckedChangeListener, OnTouchListener, CalendarListener {
	private HomeActivity homeActivity;
	public CalendarView calendarView;
	private ListView listView;
	private ArrayList<Item> itemList;
	private ItemAdapter itemAdapter;
	private View collapseLayout;
	private GestureDetector gestureDetector;
	private RotateAnimation rotateAnim;
	private RotateAnimation rotateAnimUp;
	private View expandBtn;
	private ImageView todayBtn;
	private Item item;// 当前选中的item
	private TextView lunarTitle;
	private TextView lunarSubTitle;
	private TextView lunarLucky;
	private TextView lunarDemon;
	private LinearLayout back_line_widthsize;
	private TextView tv_show_recent_dashi_kanfa;
	private int selectDateYYYYMMDD;
	private int selectGuaOrNote;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeActivity = (HomeActivity) this.getActivity();
		gestureDetector = new GestureDetector(homeActivity, new GestureListener());
		rotateAnim = (RotateAnimation) AnimationUtils.loadAnimation(homeActivity, R.anim.rotate);
		rotateAnim.setFillAfter(true);
		rotateAnimUp = (RotateAnimation) AnimationUtils.loadAnimation(homeActivity, R.anim.rotate_up);
		selectDateYYYYMMDD = 0;
		selectGuaOrNote = 0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_month, container, false);
		calendarView = (CalendarView) view.findViewById(R.id.calendar_month);
		calendarView.setCalendarListener(this);

		todayBtn = (ImageView) view.findViewById(R.id.goback_today);
		todayBtn.setOnClickListener(this);
		todayBtn.setOnLongClickListener(this);
		tv_show_recent_dashi_kanfa = (TextView) view.findViewById(R.id.tv_show_recent_dashi_kanfa);
		back_line_widthsize = (LinearLayout) view.findViewById(R.id.back_line_widthsize);
		LayoutParams back_line_widthsize_layout = back_line_widthsize.getLayoutParams();
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();

		back_line_widthsize_layout.width = (int) (dm.widthPixels - 50 * dm.density);
		back_line_widthsize.setLayoutParams(back_line_widthsize_layout);

		lunarTitle = (TextView) view.findViewById(R.id.lunar_title);
		lunarTitle.getPaint().setFakeBoldText(true);// 加粗
		lunarSubTitle = (TextView) view.findViewById(R.id.lunar_sub_title);
		lunarLucky = (TextView) view.findViewById(R.id.lunar_lucky);
		lunarDemon = (TextView) view.findViewById(R.id.lunar_demon);
		
		View lunar = view.findViewById(R.id.lunar_layout);
		lunar.setOnClickListener(this);
		lunar.setOnLongClickListener(this);
		
		expandBtn = view.findViewById(R.id.btn_expand);
		expandBtn.setOnClickListener(this);
		expandBtn.setOnLongClickListener(this);
		
		itemAdapter = null;
		listView = (ListView) view.findViewById(R.id.list_note);
		listView.setOnItemClickListener(this);
		collapseLayout = view.findViewById(R.id.layout_collapse);
		RadioGroup itemChoice = (RadioGroup) view.findViewById(R.id.radio_item);
		itemChoice.setOnCheckedChangeListener(this);
		listView.setOnTouchListener(this);
		if (selectDateYYYYMMDD == 0) {
			this.onDateSelect(new Date(), true);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		IntentFilter filter = new IntentFilter();
		filter.addAction(CommonDefine.BROAD_CAST_RECENT_DASHI_KANFA);
		getActivity().registerReceiver(new MyRecentDashiKanFaReceiver(), filter);

		super.onViewCreated(view, savedInstanceState);
	}

	class MyRecentDashiKanFaReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, final Intent intent) {
			tv_show_recent_dashi_kanfa.setVisibility(View.VISIBLE);
			AlphaAnimation anim = new AlphaAnimation(0, 1);
			anim.setDuration(600);
			tv_show_recent_dashi_kanfa.startAnimation(anim);
			tv_show_recent_dashi_kanfa.setText("大师看法: " + intent.getStringExtra("recentTitle"));
			tv_show_recent_dashi_kanfa.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent news = new Intent(getActivity(), GuaNewsDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putInt("news_id", intent.getIntExtra("pushId", 0));
					news.putExtras(bundle);
					news.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					getActivity().startActivity(news);
				}
			});

		}
	}

	public void onClick(View view) {
		if (view.getId() == R.id.lunar_layout) {
			homeActivity.showLunarFragment();
		} else if (view.getId() == R.id.btn_expand) {
			collapseExpand();
		} else if (view.getId() == R.id.goback_today) {
			todayBtn.setVisibility(View.INVISIBLE);
			calendarView.goBackToday();
			homeActivity.refreshTitleTxt(true);
			selectDateYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(null));
			new LoadTask().execute(0, selectDateYYYYMMDD);
		}
	}

	private void collapseExpand() {
		if (collapseLayout.getVisibility() == View.VISIBLE) {
			collapseLayout.setVisibility(View.GONE);
			expandBtn.startAnimation(rotateAnim);
		} else {
			collapseLayout.setVisibility(View.VISIBLE);
			expandBtn.startAnimation(rotateAnimUp);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
		item = itemList.get(position);
		if (item.referType == ItemModel.REFER_GUA) {
			Intent intent = new Intent(homeActivity, JieGuaActivity.class);
			intent.putExtra("guaid", item.referId);
			this.startActivity(intent);
		} else if (item.referType == ItemModel.REFER_NOTE) {
			BlurBehind.getInstance().execute(homeActivity, new Runnable() {
				public void run() {
					Intent intent = new Intent(homeActivity, NoteCheckActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					intent.putExtra("noteid", item.referId);
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
		if (checkedId == R.id.radio_item_gua) {
			selectGuaOrNote = checkedId;
			new LoadTask().execute(ItemModel.REFER_GUA, selectDateYYYYMMDD);
		} else if (checkedId == R.id.radio_item_note) {
			selectGuaOrNote = checkedId;
			new LoadTask().execute(ItemModel.REFER_NOTE, selectDateYYYYMMDD);
		} else {
			selectGuaOrNote = 0;
			new LoadTask().execute(0, selectDateYYYYMMDD);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (selectGuaOrNote == 0) {
					new LoadTask().execute(0, selectDateYYYYMMDD);
				} else if (selectGuaOrNote == R.id.radio_item_gua) {
					new LoadTask().execute(ItemModel.REFER_GUA, selectDateYYYYMMDD);
				} else if (selectGuaOrNote == R.id.radio_item_note) {
					new LoadTask().execute(ItemModel.REFER_NOTE, selectDateYYYYMMDD);
				}
			}
		}, 600);

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
				itemAdapter = new ItemAdapter(homeActivity, itemList);
				listView.setAdapter(itemAdapter);
			} else {
				itemAdapter.notifyDataSetChanged(itemList);
			}
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		view.performClick();
	
//		switch (event.getAction()) {
//		case MotionEvent.Action_:
//			int a = 0;
//			a++;
//			break;
//		case MotionEvent.ACTION_UP:
//			int b = 0;
//			b++;
//			break;
//		case MotionEvent.ACTION_MOVE:
//			int c = 0;
//			c++;
//			break;
//		default:
//			break;
//		}
		return false;
	}
	
	private class GestureListener extends SimpleOnGestureListener {

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getY() - e2.getY() > 100 && Math.abs(velocityY) > 200) {
				collapseExpand();
			} else if (e2.getY() - e1.getY() > 100 && Math.abs(velocityY) > 200) {
				collapseExpand();
			}
			return true;
		}
	}

	private void setTodayBtnShow(boolean isCurrent) {
		if (isCurrent) {
			todayBtn.setVisibility(View.INVISIBLE);
		} else {
			todayBtn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onMonthChange(Date date, boolean isCurrent) {
		this.onDateSelect(date, isCurrent);
	}

	@Override
	public void onDateSelect(Date date, boolean isCurrent) {
		homeActivity.lunar = LunarModel.fetchByDate(date);
		homeActivity.refreshTitleTxt(false);
		lunarTitle.setText(homeActivity.lunar.month + homeActivity.lunar.day);
		lunarSubTitle.setText(homeActivity.lunar.animal + " " + homeActivity.lunar.constellation);
		lunarLucky.setText(homeActivity.lunar.luckyGod);
		lunarDemon.setText(homeActivity.lunar.demonGod);

		selectDateYYYYMMDD = Integer.valueOf(DateTime.getTodayYmd(date));
		onCheckedChanged(null, selectGuaOrNote);
		setTodayBtnShow(isCurrent);
	}

	@Override
	public void onDoubleClick(Date date, Calendar calendar) {
		Intent intent = new Intent(homeActivity, MainActivity.class);
		intent.putExtra("tabIndex", MainActivity.Maintab_Index_Note);
		intent.putExtra("calendar", calendar);
		this.startActivity(intent);
	}

	@Override
	public boolean onLongClick(View v) {
		Log.d("v", "v-long-click");
		return false;
	}

}