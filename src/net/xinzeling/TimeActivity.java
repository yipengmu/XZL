package net.xinzeling;

import java.util.Calendar;
import net.xinzeling.lib.BlurBehind;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TimePicker;
import net.xinzeling2.R;

public class TimeActivity extends Activity {
	private TimePicker timePicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		BlurBehind.getInstance().withAlpha(240).setBackground(this);
		setContentView(R.layout.activity_time_picker);
		timePicker= (TimePicker) findViewById(R.id.time);
		timePicker.setIs24HourView(true);
		Calendar calendar = Calendar.getInstance();
		Intent intent = this.getIntent();
		int hour = intent.getIntExtra("hour", calendar.get(Calendar.HOUR_OF_DAY));
		int min = intent.getIntExtra("min", calendar.get(Calendar.MINUTE));
		
		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(min);
	}
	
	public void onClick(View view){
		if(view.getId()==R.id.btn_ok){
			Intent intent = new Intent();
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("hour", timePicker.getCurrentHour());
			intent.putExtra("min", timePicker.getCurrentMinute());
			this.setResult(RESULT_OK,intent);
		}
		this.finish();
	}
}
