package net.xinzeling.news;

import java.io.IOException;

import net.xinzeling.MainActivity;
import net.xinzeling.MyApplication;
import net.xinzeling.adapter.DashikanfaItemAdapter;
import net.xinzeling.net.http.RequestManager;
import net.xinzeling.ui.myzhuanfa.MyZhuanfaActivity;
import net.xinzeling2.R;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 大师介绍页
 * */
public class GuaNewsActivity extends Activity implements OnClickListener {

	private ImageView TabSel,BackHome,dashijianjie,mychuanbo;
	private ListView dashikanfa_listview;
	private DashikanfaItemAdapter listview_adapter;
	private Context context;
	private boolean showSel;
	private Handler myHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_news);
		
		TabSel = (ImageView)findViewById(R.id.TabSel);
		BackHome = (ImageView)findViewById(R.id.backHome);
		
		dashijianjie = (ImageView)findViewById(R.id.dashijianjie);
		mychuanbo = (ImageView)findViewById(R.id.mychuanbo);
		
		dashikanfa_listview = (ListView)findViewById(R.id.dashikafa_listview);
		
		context = this.getApplicationContext();
		listview_adapter = new DashikanfaItemAdapter(context);
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
		setInitShow(showSel);
		
		TabSel.setOnClickListener(this);
		BackHome.setOnClickListener(this);
		
		dashijianjie.setOnClickListener(this);
		mychuanbo.setOnClickListener(this);
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
			intent = new Intent(GuaNewsActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.dashijianjie:
			intent = new Intent(GuaNewsActivity.this, GuaShiActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.mychuanbo:
			intent = new Intent(GuaNewsActivity.this, MyZhuanfaActivity.class);
			startActivity(intent);
			this.finish();
			break;
		}
	}

	private void setInitShow(boolean show) {
//		if(showSel){
//			dashidetail_sel.setVisibility(View.VISIBLE);
//		}else{
//			dashidetail_sel.setVisibility(View.GONE);
//		}
		if(showSel){
			findViewById(R.id.ll_dashi_menu_cell).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.ll_dashi_menu_cell).setVisibility(View.GONE);
		}
	}
	
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
				GuaNewsActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(GuaNewsActivity.this, "正在为您努力加载中...", Toast.LENGTH_SHORT).show();
					}
				});
				JSONObject res = RequestManager.getGet(MyApplication.dashikanfa_url);
				this.listview_adapter.setData(res.getJSONArray("pushList"));
				Message message = new Message();
				message.what = 1;
				myHandler.sendMessage(message);
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			}
			super.run();
		}

	}
}
