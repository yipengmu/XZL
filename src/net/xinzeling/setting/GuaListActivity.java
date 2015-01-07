package net.xinzeling.setting;

import java.util.ArrayList;

import net.xinzeling.adapter.GuaListAdapter;
import net.xinzeling.adapter.GuaListAdapter.onGuaContentClickListener;
import net.xinzeling.gua.JieGuaActivity;
import net.xinzeling.model.GuaModel;
import net.xinzeling.model.GuaModel.Gua;
import net.xinzeling2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

public class GuaListActivity extends Activity {
	private ExpandableListView listView;
	private GuaListAdapter guaListAdapter;
	private ArrayList<String> dateList;
	private ArrayList<ArrayList<Gua>> guaList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_list);
		listView = (ExpandableListView)findViewById(R.id.list_gua);
		new LoadGuaTask().execute();
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(GuaListActivity.this), GuaListActivity.this);
	}

	public void onClick(View view) {
		if(view.getId()==R.id.btn_back){
			finish();
		}
	}
	
	private class LoadGuaTask extends AsyncTask<Void,Void,Void>{

		@Override
		protected Void doInBackground(Void... args) {
			dateList = GuaModel.fetchDateList();
			guaList = GuaModel.fetchByDate(dateList);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			if(guaListAdapter ==null){
				guaListAdapter = new GuaListAdapter(GuaListActivity.this,dateList, guaList);
				listView.setAdapter(guaListAdapter);
				guaListAdapter.setGuaContentClickListener(new onGuaContentClickListener(){

					@Override
					public void onClick(int guaid) {
						Intent intent = new Intent(GuaListActivity.this,JieGuaActivity.class);
						intent.putExtra("guaid", guaid);
						startActivity(intent);
					}
					
				});
			}else{
				guaListAdapter.notifyDataSetChanged();
			}
			for(int i=0;i<dateList.size();i++){
				listView.expandGroup(i);
			}
		}
	}

}