package net.xinzeling.fragment;

import java.util.ArrayList;

import net.xinzeling.base.BaseFragment;
import net.xinzeling.note.NoteActivity;
import net.xinzeling2.R;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class TopicFragment extends BaseFragment implements OnClickListener{
	private NoteActivity activity;
	private ArrayList<CheckBox> mCheckBoxList = new ArrayList<>();
	private SparseIntArray mItemSelected;
	private ArrayList<Integer> mItemSelectedList = new ArrayList<>();
	private String Tag;
	private View view;
//	private HashMap<String, Integer> mItemSelectedMap = new HashMap<String, Integer> ();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=(NoteActivity) this.getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_note_topic, container,false);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
	
		init(view);
		
		loadHistoryData();
		return view;
	}
	
	private void loadHistoryData() {
		mItemSelected = activity.mSelected;
		mItemSelectedList = activity.mSelectedList;
		
		for(int i = 0 ;i<mItemSelectedList.size();i++){
			if(i < 0 || i >23){
				break;
			}
			int RadioItemIndex  = mItemSelectedList.get(i)-1;
			if(RadioItemIndex >= 0 && RadioItemIndex < mCheckBoxList.size()){
				mCheckBoxList.get(RadioItemIndex).setChecked(true);
			}
		}
	}
	
	private void setDrawable(int viewId,int drawableId){
		Log.d(Tag, "mItemSelected="+mItemSelected.toString() + "  mItemSelectedList="+mItemSelectedList.toString() );
		if(mItemSelected.indexOfKey(viewId)>=0){
			mItemSelected.delete(viewId);
			mItemSelectedList.remove(new Integer(drawableId));
		}else{
			mItemSelected.put(viewId, drawableId);
			mItemSelectedList.add(drawableId);
		}
	}
	

	private void init(View view) {
		
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_1));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_2));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_3));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_4));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_5));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_6));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_7));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_8));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_9));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_10));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_11));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_12));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_13));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_14));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_15));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_16));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_17));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_18));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_19));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_20));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_21));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_22));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_23));
		mCheckBoxList.add((CheckBox) view.findViewById(R.id.note_topic_24));
		
		for(int i = 0 ;i<mCheckBoxList.size();i++){
			mCheckBoxList.get(i).setOnClickListener(this);
		}
	}

	
	public void onClick(View view) {
		int viewId =view.getId();
		switch(viewId){
		case R.id.btn_cancel:
//			activity.saveTopicFragment();
			popPageBackStack();
			break;
		case R.id.btn_ok:
			activity.saveTopicFragment();
			popPageBackStack();
			break;
		case R.id.note_topic_1:
			this.setDrawable(viewId, 1);
			break;
		case R.id.note_topic_2:
			this.setDrawable(viewId, 2);
			break;
		case R.id.note_topic_3:
			this.setDrawable(viewId, 3);
			break;
		case R.id.note_topic_4:
			this.setDrawable(viewId, 4);
			break;
		case R.id.note_topic_5:
			this.setDrawable(viewId, 5);
			break;
		case R.id.note_topic_6:
			this.setDrawable(viewId, 6);
			break;
		case R.id.note_topic_7:
			this.setDrawable(viewId, 7);
			break;
		case R.id.note_topic_8:
			this.setDrawable(viewId, 8);
			break;
		case R.id.note_topic_9:
			this.setDrawable(viewId, 9);
			break;
		case R.id.note_topic_10:
			this.setDrawable(viewId, 10);
			break;
		case R.id.note_topic_11:
			this.setDrawable(viewId, 11);
			break;
		case R.id.note_topic_12:
			this.setDrawable(viewId, 12);
			break;
		case R.id.note_topic_13:
			this.setDrawable(viewId, 13);
			break;
		case R.id.note_topic_14:
			this.setDrawable(viewId, 14);
			break;
		case R.id.note_topic_15:
			this.setDrawable(viewId, 15);
			break;
		case R.id.note_topic_16:
			this.setDrawable(viewId, 16);
			break;
		case R.id.note_topic_17:
			this.setDrawable(viewId, 17);
			break;
		case R.id.note_topic_18:
			this.setDrawable(viewId, 18);
			break;
		case R.id.note_topic_19:
			this.setDrawable(viewId, 19);
			break;
		case R.id.note_topic_20:
			this.setDrawable(viewId, 20);
			break;
		case R.id.note_topic_21:
			this.setDrawable(viewId, 21);
			break;
		case R.id.note_topic_22:
			this.setDrawable(viewId, 22);
			break;
		case R.id.note_topic_23:
			this.setDrawable(viewId, 23);
			break;
		case R.id.note_topic_24:
			this.setDrawable(viewId, 24);
			break;
		}
	}

}
