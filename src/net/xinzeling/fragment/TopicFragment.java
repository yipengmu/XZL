package net.xinzeling.fragment;

import net.xinzeling2.R;
import net.xinzeling.MyApplication;
import net.xinzeling.note.NoteActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;

public class TopicFragment extends Fragment implements OnClickListener{
	private NoteActivity activity;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=(NoteActivity) this.getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_note_topic, container,false);
		view.findViewById(R.id.btn_cancel).setOnClickListener(this);
		view.findViewById(R.id.btn_ok).setOnClickListener(this);
		view.findViewById(R.id.note_topic_1).setOnClickListener(this);
		view.findViewById(R.id.note_topic_2).setOnClickListener(this);
		view.findViewById(R.id.note_topic_3).setOnClickListener(this);
		view.findViewById(R.id.note_topic_4).setOnClickListener(this);
		view.findViewById(R.id.note_topic_5).setOnClickListener(this);
		view.findViewById(R.id.note_topic_6).setOnClickListener(this);
		view.findViewById(R.id.note_topic_7).setOnClickListener(this);
		view.findViewById(R.id.note_topic_8).setOnClickListener(this);
		view.findViewById(R.id.note_topic_9).setOnClickListener(this);
		view.findViewById(R.id.note_topic_10).setOnClickListener(this);
		view.findViewById(R.id.note_topic_11).setOnClickListener(this);
		view.findViewById(R.id.note_topic_12).setOnClickListener(this);
		view.findViewById(R.id.note_topic_13).setOnClickListener(this);
		view.findViewById(R.id.note_topic_14).setOnClickListener(this);
		view.findViewById(R.id.note_topic_15).setOnClickListener(this);
		view.findViewById(R.id.note_topic_16).setOnClickListener(this);
		view.findViewById(R.id.note_topic_17).setOnClickListener(this);
		view.findViewById(R.id.note_topic_18).setOnClickListener(this);
		view.findViewById(R.id.note_topic_19).setOnClickListener(this);
		view.findViewById(R.id.note_topic_20).setOnClickListener(this);
		view.findViewById(R.id.note_topic_21).setOnClickListener(this);
		view.findViewById(R.id.note_topic_22).setOnClickListener(this);
		view.findViewById(R.id.note_topic_23).setOnClickListener(this);
		view.findViewById(R.id.note_topic_24).setOnClickListener(this);
		
		return view;
	}
		
	public void onClick(View view) {
		int viewId =view.getId();
		switch(viewId){
		case R.id.btn_cancel:
			activity.hideTopicFragment();
			break;
		case R.id.btn_ok:
			activity.hideTopicFragment();
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
	
	private void setDrawable(int viewId,int drawableId){
		if(activity.selected.indexOfKey(viewId)>0){
			activity.selected.delete(viewId);
		}else{
			activity.selected.put(viewId, drawableId);
		}
	}
}
