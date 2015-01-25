package net.xinzeling.ui.myzhuanfa;

import java.util.ArrayList;

import net.xinzeling.base.BaseActivity;
import net.xinzeling.common.CommonDefine;
import net.xinzeling.common.PreferenceManager;
import net.xinzeling.webview.WebViewActivity;
import net.xinzeling2.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;

public class MyZhuanfaActivity extends BaseActivity {

	private ListView lv_my_zhuanfa_listview;
	private ZhuanfaLvAdapter mZhuanfaLvAdapter;
	private ArrayList<ZhuanfaBean> mListData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_my_zhuanfa);
	
		lv_my_zhuanfa_listview = (ListView) findViewById(R.id.lv_my_zhuanfa_listview);
	
		mZhuanfaLvAdapter = new ZhuanfaLvAdapter(this);
		initData();
		mZhuanfaLvAdapter.setData(mListData);
		lv_my_zhuanfa_listview.setAdapter(mZhuanfaLvAdapter);
		
		lv_my_zhuanfa_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(MyZhuanfaActivity.this,WebViewActivity.class);
				intent.putExtra("url",mListData.get(position).url);
				intent.putExtra("title",mListData.get(position).title);
				startActivity(intent);
				
			}
		});
	}
	private void initData() {
		try {
			String zfLists = PreferenceManager.getInstance().getPreferenceString(CommonDefine.PREF_MY_ZHUANFA_KEY);
			mListData = (ArrayList<ZhuanfaBean>) JSONArray.parseArray(zfLists, ZhuanfaBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
