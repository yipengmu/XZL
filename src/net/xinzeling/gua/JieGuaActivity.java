package net.xinzeling.gua;

import java.util.HashMap;

import net.xinzeling.MyApplication.share_id;
import net.xinzeling.lib.BlurBehind;
import net.xinzeling.model.GuaModel;
import net.xinzeling.model.GuaModel.Gua;
import net.xinzeling.utils.Utils;
import net.xinzeling2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class JieGuaActivity extends Activity {
	private int guaid;
	private boolean isOld = false;//是否之前算过,即是否是从本地db拉的数据；默认为新的求卦路径近来的，不显示哭脸Linerlayout
	private ImageView ben_up,ben_down,hu_up,hu_down,bian_up,bian_down;
	private int FLAG_JIEGUA_SHARE = 2;
	private View ll_jieguo_next_time;
	private TextView txt_gua_datetime_for_next;
	private String oldInfo = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gua_jie);
		guaid = getIntent().getIntExtra("guaid", 1);
		isOld = getIntent().getBooleanExtra("isOld", false);
		oldInfo = getIntent().getStringExtra("oldInfo");
		ben_up = (ImageView)findViewById(R.id.ben_up);
		ben_down = (ImageView)findViewById(R.id.ben_down);
		hu_up = (ImageView)findViewById(R.id.hu_up);
		hu_down = (ImageView)findViewById(R.id.hu_down);
		bian_up = (ImageView)findViewById(R.id.bian_up);
		bian_down = (ImageView)findViewById(R.id.bian_down);
		ll_jieguo_next_time = findViewById(R.id.ll_jieguo_next_time);
		txt_gua_datetime_for_next= (TextView) findViewById(R.id.txt_gua_datetime_for_next)	;
		
		if(isOld){
			ll_jieguo_next_time.setVisibility(View.VISIBLE);
			txt_gua_datetime_for_next.setText(oldInfo);
		}else{
			ll_jieguo_next_time.setVisibility(View.GONE);
		}
		
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
		case R.id.btn_jiegua_share:
			openShareActivity();
			break;
		}
	}
	
	private void openShareActivity() {
		startActivityForResult(Utils.getShareIntent(this,getShareText(),getShareTitle()), FLAG_JIEGUA_SHARE );
	}

	private String getShareText() {
		Gua gua = GuaModel.fetch(guaid);
		return "大师说:" + gua.inference ;
	}

	private String getShareTitle() {
		return "我用信则聆的" + "起了一挂";
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
