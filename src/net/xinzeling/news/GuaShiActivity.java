package net.xinzeling.news;

import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.FontManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

public class GuaShiActivity extends Activity implements OnClickListener {

	private ImageView goback,gohome;
	private WebView jianjie_info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_shi);
		
		goback = (ImageView)findViewById(R.id.goback);
		gohome = (ImageView)findViewById(R.id.gohome);
		
		goback.setOnClickListener(this);
		gohome.setOnClickListener(this);
		
		jianjie_info = (WebView)findViewById(R.id.dashijianjie_info);
		jianjie_info.loadUrl(AppBase.dashijianjie_info_url);
		
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(GuaShiActivity.this),GuaShiActivity.this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
		case R.id.goback:
			break;
		case R.id.gohome:
			break;
		}
	}

}
