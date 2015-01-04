package net.xinzeling;

import java.util.Calendar;
import net.xinzeling.lib.BlurBehind;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import net.xinzeling2.R;

public class DateActivity extends Activity  {
	private DatePicker datePicker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_input_picker);
		Intent intent = getIntent();
		Calendar calendar = Calendar.getInstance();
		
		int year = intent.getIntExtra("year", calendar.get(Calendar.YEAR));
		int month = intent.getIntExtra("month", calendar.get(Calendar.MONTH));
		int day = intent.getIntExtra("day", calendar.get(Calendar.DAY_OF_MONTH));
		
		datePicker =(DatePicker)findViewById(R.id.datePicker);
		datePicker.init(year, month, day, null);
	}
	
	public void onClick(View view){
		if(view.getId()==R.id.btn_ok){
			Intent intent = new Intent();
			intent.putExtra("year", datePicker.getYear()+0);
			intent.putExtra("month", datePicker.getMonth()+0);
			intent.putExtra("day", datePicker.getDayOfMonth()+0);
			this.setResult(RESULT_OK, intent);
		}
		this.finish();
	}
}