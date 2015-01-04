package net.xinzeling;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.xinzeling.gua.GuaActivity;
import net.xinzeling.gua.JieGuaActivity;
import net.xinzeling.gua.QiuGuaActivity;
import net.xinzeling.lib.AppBase;
import net.xinzeling.news.GuaNewsActivity;
import net.xinzeling.news.GuaNewsDetailActivity;
import net.xinzeling.news.GuaNewsShareActivity;
import net.xinzeling.news.GuaShiActivity;
import net.xinzeling.note.NoteActivity;
import net.xinzeling.note.NoteCheckActivity;
import net.xinzeling.setting.AboutActivity;
import net.xinzeling.setting.GuaListActivity;
import net.xinzeling.setting.MainSettingActivity;
import net.xinzeling.setting.SettingActivity;
import net.xinzeling.setting.SigninActivity;
import net.xinzeling.setting.UsrActivity;
import net.xinzeling.setting.UsrEditActivity;
import net.xinzeling2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TestActivity extends Activity {
	private HashMap<String,Intent> params;

	private ListView test_lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		File is_debug_file = new File(AppBase.sdcardPath()+"/debug.txt");
		
//		if(!is_debug_file.exists()){
//			startActivity(new Intent(TestActivity.this,MainActivity.class));
//			this.finish();
//		}
		init();
		Set<Map.Entry<String,Intent>> entryset = params.entrySet();
		List<String> list_key = new ArrayList<String>();
		for(Map.Entry<String,Intent> ent:entryset){
			list_key.add(ent.getKey().toString());
		}
		test_lv = (ListView)findViewById(R.id.test_lv);
		test_lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list_key.toArray(new String[]{})));
		test_lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String key = ((TextView)arg1).getText().toString();
				if(params.containsKey(key)){
					TestActivity.this.startActivity(params.get(key));
				}
			}
			
		});
	}

	private void init(){
		params = new HashMap<String,Intent>();
		params.put("主页", new Intent(TestActivity.this,HomeActivity.class));
		params.put("求卦页", new Intent(TestActivity.this,GuaActivity.class));
		params.put("注册webview", new Intent(TestActivity.this,WebViewActivity.class).putExtra("target", "regist"));
		params.put("找回密码webview", new Intent(TestActivity.this,WebViewActivity.class).putExtra("target", ""));
		params.put("选择年月日", new Intent(TestActivity.this,DateActivity.class));
		params.put("记事页", new Intent(TestActivity.this,NoteActivity.class));
		params.put("记事小页", new Intent(TestActivity.this,NoteCheckActivity.class));
		params.put("关于页", new Intent(TestActivity.this,AboutActivity.class));
		params.put("求卦列表页", new Intent(TestActivity.this,GuaListActivity.class));
		params.put("登陆页", new Intent(TestActivity.this,SigninActivity.class));
		params.put("用户信息页", new Intent(TestActivity.this,UsrActivity.class));
		params.put("用户信息编辑页", new Intent(TestActivity.this,UsrEditActivity.class));
		params.put("卦新闻页", new Intent(TestActivity.this,GuaNewsActivity.class));
		params.put("卦新闻详细页", new Intent(TestActivity.this,GuaNewsDetailActivity.class).putExtra("news_id", 16));
		params.put("卦新闻分享页", new Intent(TestActivity.this,GuaNewsShareActivity.class).putExtra("news_id", 16));
		params.put("大师页", new Intent(TestActivity.this,GuaShiActivity.class));
		params.put("MAIN页", new Intent(TestActivity.this,MainActivity.class));
		params.put("数字求卦", new Intent(TestActivity.this,QiuGuaActivity.class).putExtra("mode", 1).putExtra("type", 15));
		params.put("照片求卦", new Intent(TestActivity.this,QiuGuaActivity.class).putExtra("mode", 2).putExtra("type", 15));
		params.put("时辰求卦", new Intent(TestActivity.this,QiuGuaActivity.class).putExtra("mode", 3).putExtra("type", 15));		
		params.put("解卦页面", new Intent(TestActivity.this,JieGuaActivity.class).putExtra("mode", 3).putExtra("type", 15).putExtra("isOld", false));		
		params.put("解卦已算页面", new Intent(TestActivity.this,JieGuaActivity.class).putExtra("mode", 3).putExtra("type", 15).putExtra("isOld", true));		
		params.put("设置页面", new Intent(TestActivity.this,MainSettingActivity.class));		
		params.put("内设置页面", new Intent(TestActivity.this,SettingActivity.class));		
	}
}
