package net.xinzeling.lib;

import net.xinzeling.MyApplication;
import net.xinzeling2.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImageViewWithCount extends LinearLayout implements OnClickListener {

	private OnClickListener mListener;
	private Context context;
	private ImageView mBg;
	private int cnt_font_size;
    private int cnt_font_color;
    private int circle_color;
	
    public ImageViewWithCount(Context context) {
		super(context);
		init(context);
	}

	public ImageViewWithCount(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public ImageViewWithCount(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithCount, defStyle, 0);

		cnt_font_size = a.getDimensionPixelSize(R.styleable.ImageViewWithCount_cnt_font_size, 20);
		cnt_font_color = a.getColor(R.styleable.ImageViewWithCount_cnt_font_color, Color.RED);
		circle_color = a.getColor(R.styleable.ImageViewWithCount_circle_color, Color.YELLOW);
		
        a.recycle();
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		mListener = null;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.imageviewwithcount, this,true);
		mBg = (ImageView)layout.findViewById(R.id.notification);
		reDrawCount(MyApplication.getunreadmsg_cnt());
	}

	public void reDrawCount(int count) {
		String cnt = "";
		if(count>9){
			cnt = "9+";
		}else if(count>=1&&count<=9){
			cnt = count+"";
		}
		if(count==0){
			MyApplication.setunreadmsg_cnt(0);
		}else{
			MyApplication.setunreadmsg_cnt(count);
		}
		Bitmap icon;
		if(count==0){
			icon = MyApplication.getResIcon(getResources(), R.drawable.notificationicon_read);			
		}else{
			icon = MyApplication.getResIcon(getResources(), R.drawable.notificationicon);
		}
		if(icon!=null){
			Bitmap new_icon = MyApplication.generatorContactCountIcon(icon, cnt,cnt_font_size,cnt_font_color,circle_color);
			if(new_icon!=null){
				mBg.setImageBitmap(new_icon);
			}
		}
	}
	
	public void setOnClickListner(OnClickListener listener) {
		mListener = listener;
	}
	
	@Override
	public void onClick(View arg0) {
		if(mListener!=null) {
			mListener.onClick(arg0);
		}
	}

	public interface OnClickListener {
		public void onClick(View arg0);
	}
}
