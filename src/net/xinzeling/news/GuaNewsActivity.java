package net.xinzeling.news;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import net.xinzeling.HomeActivity;
import net.xinzeling2.R;
import net.xinzeling.adapter.DashikanfaItemAdapter;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.FontManager;
import net.xinzeling.lib.HttpCommon;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class GuaNewsActivity extends Activity implements OnClickListener {

	private ImageView TabSel,BackHome,dashijianjie,mychuanbo;
	private ListView dashikanfa_listview;
	private RelativeLayout dashidetail_sel;
	private DashikanfaItemAdapter listview_adapter;
	private Context context;
	private boolean showSel;
	private Handler myHandler;
	private Typeface tf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_news);
		
		tf = FontManager.getTypeface(this);
		TabSel = (ImageView)findViewById(R.id.TabSel);
		BackHome = (ImageView)findViewById(R.id.backHome);
		
		dashijianjie = (ImageView)findViewById(R.id.dashijianjie);
		mychuanbo = (ImageView)findViewById(R.id.mychuanbo);
		
		dashikanfa_listview = (ListView)findViewById(R.id.dashikafa_listview);
		dashidetail_sel = (RelativeLayout)findViewById(R.id.dashidetail_sel);
		
		context = this.getApplicationContext();
		listview_adapter = new DashikanfaItemAdapter(context,tf);
		dashikanfa_listview.setAdapter(listview_adapter);
		
		myHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 1:
					listview_adapter.notifyDataSetChanged();;
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		(new getMsgList(context,listview_adapter)).start();
		showSel = false;
		//setInitShow(showSel);
		
		TabSel.setOnClickListener(this);
		BackHome.setOnClickListener(this);
		
		dashijianjie.setOnClickListener(this);
		mychuanbo.setOnClickListener(this);
		FontManager.changeFonts((ViewGroup)AppBase.getRootView(GuaNewsActivity.this), GuaNewsActivity.this,tf);
	}

	@Override
	public void onClick(View arg0) {
		Intent intent;
		switch(arg0.getId()){
		case R.id.TabSel:
			showSel = !showSel;
			setInitShow(showSel);
			break;
		case R.id.backHome:
			intent = new Intent(GuaNewsActivity.this, HomeActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.dashijianjie:
			intent = new Intent(GuaNewsActivity.this, GuaShiActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.mychuanbo:
			break;
		}
	}

	private void setInitShow(boolean show) {
		if(showSel){
			dashikanfa_listview.setVisibility(View.INVISIBLE);
			dashidetail_sel.setVisibility(View.VISIBLE);
		}else{
			dashikanfa_listview.setVisibility(View.VISIBLE);
			dashidetail_sel.setVisibility(View.INVISIBLE);
		}
	}
	
	//
	private class getMsgList extends Thread {

		private Context context;
		private DashikanfaItemAdapter listview_adapter;
		public getMsgList(Context context,DashikanfaItemAdapter listview_adapter) {
			this.context = context;
			this.listview_adapter = listview_adapter;
		}

		@Override
		public void run() {
			try {
				JSONObject res = HttpCommon.getGet(AppBase.dashikanfa_url);
				this.listview_adapter.setData(res.getJSONArray("pushList"));
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			} catch (IOException | JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}

	}
}
