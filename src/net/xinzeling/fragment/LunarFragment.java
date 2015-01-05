package net.xinzeling.fragment;

import net.xinzeling.HomeActivity;
import net.xinzeling2.R;
import net.xinzeling.lib.FontManager;
import net.xinzeling.model.LunarModel.Lunar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class LunarFragment extends Fragment implements OnTouchListener,OnClickListener {
	private TextView txtHuangliNew,txtHuangliOld;
	private ImageView imgHuangli;
	private GestureDetector gestureDetector;
	private RadioButton radioJI;
	private RadioButton radioYI;
	private HomeActivity homeActivity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		homeActivity = (HomeActivity)this.getActivity();
		homeActivity.lunarFragment=this;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_lunar, container, false);
		
		txtHuangliNew = (TextView) view.findViewById(R.id.txt_huangli_new);
//		txtHuangliNew.setTypeface(homeActivity.tf);

		txtHuangliOld = (TextView) view.findViewById(R.id.txt_huangli_old);
//		txtHuangliOld.setTypeface(homeActivity.tf);

		imgHuangli = (ImageView) view.findViewById(R.id.img_huangli);
		radioJI = (RadioButton) view.findViewById(R.id.switch_ji);
		radioYI = (RadioButton)view.findViewById(R.id.switch_yi);

		gestureDetector = new GestureDetector(homeActivity, new GestureListener());
		txtHuangliNew.setOnTouchListener(this);
		txtHuangliNew.setClickable(true);
		txtHuangliOld.setOnTouchListener(this);
		txtHuangliOld.setClickable(true);
		view.findViewById(R.id.btn_close).setOnClickListener(this);
		radioJI.setOnClickListener(this);
		radioYI.setOnClickListener(this);
		return view;
	}
	
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_close:
			homeActivity.hideLunarFragment();
			break;
		case R.id.switch_ji:
			this.txtHuangliNew.setText("["+homeActivity.lunar.demonGod+"]");
			this.txtHuangliOld.setText("["+homeActivity.lunar.demonGodOld+"]");
			imgHuangli.setImageResource(R.drawable.img_huangli_ji);
			break;
		case R.id.switch_yi:
			this.txtHuangliNew.setText("["+homeActivity.lunar.luckyGod+"]");
			this.txtHuangliOld.setText("["+homeActivity.lunar.luckyGodOld+"]");
			imgHuangli.setImageResource(R.drawable.img_huangli_yi);
			break;
		}
	}
	
	public void onShow(){
		this.onClick(radioYI);
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		view.performClick();
		gestureDetector.onTouchEvent(event);
		return false;
	}
	
	private class GestureListener extends SimpleOnGestureListener {

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 100) {
				radioYI.performClick();
			} else if (e2.getX() - e1.getX() > 50 && Math.abs(velocityY) > 100) {
				radioJI.performClick();
			}
			return true;
		}
	}
}
