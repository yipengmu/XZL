package net.xinzeling.gua;

import net.xinzeling2.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class GuaInstruActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_instru);
	}

	public void onClick(View view){
		this.finish();
	}
}