package net.xinzeling.gua;

import java.util.HashMap;

import net.xinzeling2.R;
import net.xinzeling.lib.AppBase;
import net.xinzeling.lib.AppBase.share_id;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.lib.FontManager;
import net.xinzeling.model.GuaModel;
import net.xinzeling.model.GuaModel.Gua;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class JieGuaActivity extends Activity {
	private int guaid;
	private boolean isOld;//是否之前算过
	private ImageView ben_up,ben_down,hu_up,hu_down,bian_up,bian_down;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_jie);
		guaid = getIntent().getIntExtra("guaid", 1);
		isOld = getIntent().getBooleanExtra("isOld", true);
		ben_up = (ImageView)findViewById(R.id.ben_up);
		ben_down = (ImageView)findViewById(R.id.ben_down);
		hu_up = (ImageView)findViewById(R.id.hu_up);
		hu_down = (ImageView)findViewById(R.id.hu_down);
		bian_up = (ImageView)findViewById(R.id.bian_up);
		bian_down = (ImageView)findViewById(R.id.bian_down);

		Gua gua = GuaModel.fetch(guaid);
		if(gua !=null){
			TextView inferenceTxt = (TextView)this.findViewById(R.id.txt_inference);
			inferenceTxt.setText(gua.inference);
			
			if("0".equals(gua.body)){
				((TextView)findViewById(R.id.body_up)).setText("体");
				((TextView)findViewById(R.id.body_down)).setText("用");
			}
			String[] result = gua.result.split("\\|");
			HashMap<String,Object> resIdList = new HashMap<String,Object>();
			resIdList.put("000", R.drawable.gua_000);
			resIdList.put("001", R.drawable.gua_001);
			resIdList.put("010", R.drawable.gua_010);
			resIdList.put("011", R.drawable.gua_011);
			resIdList.put("100", R.drawable.gua_100);
			resIdList.put("101", R.drawable.gua_101);
			resIdList.put("110", R.drawable.gua_110);
			resIdList.put("111", R.drawable.gua_111);
			
			ben_up.setImageResource((int)resIdList.get(result[0].substring(0, 3)));
			ben_down.setImageResource((int)resIdList.get(result[0].substring(3)));

			hu_up.setImageResource((int)resIdList.get(result[1].substring(0, 3)));
			hu_down.setImageResource((int)resIdList.get(result[1].substring(3)));
			
			bian_up.setImageResource((int)resIdList.get(result[2].substring(0, 3)));
			bian_down.setImageResource((int)resIdList.get(result[2].substring(3)));
			
		}
		//如果是old就显示一个抱歉
		if(isOld){
			findViewById(R.id.linearlayout_inference).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.linearlayout_inference).setVisibility(View.GONE);			
		}
//		FontManager.changeFonts((ViewGroup)AppBase.getRootView(JieGuaActivity.this),JieGuaActivity.this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_gua_info:
			this.startActivity(new Intent(this, GuaInstruActivity.class));
			break;
		case R.id.header_home:
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_share_pengyouquan:
		case R.id.btn_share_qq:
		case R.id.btn_share_weibo:
			share_id share_type = share_id.SHARE_QQ_PENGYOUQUAN;
			if(view.getId()==R.id.btn_share_pengyouquan){
				share_type = share_id.SHARE_QQ_PENGYOUQUAN;
			}else if(view.getId()==R.id.btn_share_qq){
				share_type = share_id.SHARE_QQZONE;
			}else if(view.getId()==R.id.btn_share_weixin){
				share_type = share_id.SHARE_WEIXIN;
			}else if(view.getId()==R.id.btn_share_weibo){
				share_type = share_id.SHARE_WEIBO;
			}
			BlurBehind.getInstance().execute(JieGuaActivity.this,new ShareRun(share_type));
			break;
		}
	}
	
	class ShareRun implements Runnable{
		private share_id share_type;
		public ShareRun(share_id share_type){
			this.share_type = share_type;
		}
		@Override
		public void run() {
			Intent intent = new Intent(JieGuaActivity.this,ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("share_type", this.share_type);
			intent.putExtra("guaid", guaid);
			startActivity(intent);
		}
	}
}
