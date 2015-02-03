package net.xinzeling.note;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.xinzeling.MainActivity;
import net.xinzeling.lib.DateTime;
import net.xinzeling.model.ItemModel;
import net.xinzeling.model.NoteModel;
import net.xinzeling.model.NoteModel.Note;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
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
		noteTopic = (TextView) findViewById(R.id.note_topic);
		noteTime = (TextView) findViewById(R.id.note_time);
		noteContact = (TextView) findViewById(R.id.note_contact);
		noteContent = (TextView) findViewById(R.id.note_content);
		noteAlarm = (TextView) findViewById(R.id.note_alarm);

		Intent intent = this.getIntent();
		noteid = intent.getIntExtra("noteid", -1);
		Note note = NoteModel.fetch(noteid);

		if (note != null) {
			SpannableString spanStr = new SpannableString(note.topic);
			Pattern pattern = Pattern.compile("_x_icon_y_\\d{1,2}_");
			Matcher matcher = pattern.matcher(note.topic);
			while (matcher.find()) {
				String find_str = matcher.group();
				int drawable_id = Integer.valueOf(find_str.replace("_x_icon_y_", "").replace("_", ""));
				ImageSpan span = new ImageSpan(this, getResources().getIdentifier("small_note_topic_" + drawable_id, "drawable", "net.xinzeling"));
				spanStr.setSpan(span, matcher.start(), matcher.end(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			}
			noteTopic.setText(spanStr);
			noteTime.setText(DateTime.Timestamp2String((long) note.started * 1000, "MM月dd日 HH:mm") + " - " + DateTime.Timestamp2String((long) note.ended * 1000, "MM月dd日 HH:mm"));
			noteContact.setText(note.contact);
			noteContent.setText(note.content);
			noteAlarm.setText(note.iscancel == 0 ? "否" : "是");
		}
	}

	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.note_edit) {
			Intent intent = Utils.getMaintabIndexIntent(this, MainActivity.Maintab_Index_Note);
			intent.putExtra("noteid", noteid);
			this.startActivity(intent);
		} else if (id == R.id.note_del) {
			// @todo show confirm
			showDelItemdialog();
		} else if (id == R.id.btn_cancel) {
			this.finish();
		} else if (id == R.id.btn_close) {
			finish();
		}
	}

	protected void showDelItemdialog() {
		AlertDialog.Builder builder = new Builder(NoteCheckActivity.this);
		builder.setMessage("确定要删除此事件吗?");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				NoteModel.del(noteid);
				ItemModel.del(ItemModel.REFER_NOTE, noteid);

				dialog.dismiss();
				finish();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});

		builder.create().show();
	}
}
