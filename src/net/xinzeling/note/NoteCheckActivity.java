package net.xinzeling.note;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.lib.DateTime;
import net.xinzeling.lib.FontManager;
import net.xinzeling.model.NoteModel;
import net.xinzeling.model.NoteModel.Note;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NoteCheckActivity extends Activity {
	private int noteid = -1;
	private TextView noteTopic;
	private TextView noteTime;
	private TextView noteContact;
	private TextView noteContent;
	private TextView noteAlarm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_check);
		noteTopic = (TextView)findViewById(R.id.note_topic);
		noteTime =(TextView)findViewById(R.id.note_time);
		noteContact=(TextView)findViewById(R.id.note_contact);
		noteContent =(TextView)findViewById(R.id.note_content);
		noteAlarm = (TextView)findViewById(R.id.note_alarm);
		
		Intent intent = this.getIntent();
		noteid = intent.getIntExtra("noteid", -1);
		Note note = NoteModel.fetch(noteid);
		if(note !=null){
			SpannableString spanStr = new SpannableString(note.topic);
			Pattern pattern = Pattern.compile("_x_icon_y_\\d{1,2}_");
			Matcher matcher = pattern.matcher(note.topic);
			while(matcher.find()){
				String find_str = matcher.group();
				int drawable_id = Integer.valueOf(find_str.replace("_x_icon_y_", "").replace("_", ""));
				ImageSpan span = new ImageSpan(this, getResources().getIdentifier("small_note_topic_"+drawable_id, "drawable", "net.xinzeling"));
				spanStr.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			noteTopic.setText(spanStr);
			noteTime.setText(DateTime.Timestamp2String((long)note.started*1000, "yyyy年MM月dd日 HH点mm分")+" 至 "+DateTime.Timestamp2String((long)note.ended*1000,"yyyy年MM月dd日 HH点mm分"));
			noteContact.setText(note.contact);
			noteContent.setText(note.content);
			noteAlarm.setText(note.iscancel==0?"否":"是");
		}
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(NoteCheckActivity.this), NoteCheckActivity.this);
	}

	public void onClick(View view) {
		int id = view.getId();
		if(id==R.id.note_edit){
			Intent intent = new Intent(this,NoteActivity.class);
			intent.putExtra("noteid", noteid);
			this.startActivity(intent);
		}else if(id==R.id.note_del){
			//@todo show confirm
		}else if(id==R.id.btn_cancel){
			this.finish();
		}
	}
}
