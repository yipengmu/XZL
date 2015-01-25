package net.xinzeling.ui.myzhuanfa;

import java.util.ArrayList;

import net.xinzeling.base.BaseActivity;
import net.xinzeling.ui.myzhuanfa.ZhuanfaLvAdapter.ZhuanfaBean;
import net.xinzeling2.R;
import android.os.Bundle;
import android.widget.ListView;

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
		mListData = new ArrayList<ZhuanfaBean>();
		mZhuanfaLvAdapter.setData(mListData);
		
		lv_my_zhuanfa_listview.setAdapter(mZhuanfaLvAdapter);
	}
}
