package net.xinzeling.widget;

import net.xinzeling2.R;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RepeatCheckItem extends TextView {
	private Context mContext;
	private View container;
	private TextView tv_repeat_item;

	private ImageView tv_repeat_corner_ok;

	public RepeatCheckItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		container = LayoutInflater.from(mContext).inflate(R.layout.ui_repeat_check_item_layout, null);
		tv_repeat_item = (TextView) container.findViewById(R.id.tv_repeat_item);
		tv_repeat_corner_ok = (ImageView) container.findViewById(R.id.tv_repeat_corner_ok);
	}

	public void check(boolean isCheck) {
		if (isCheck) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				tv_repeat_item.setBackground(mContext.getResources().getDrawable(R.drawable.rect_btn_common_cycle_green));
			} else {
				tv_repeat_item.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rect_btn_common_cycle_green));
			}
			tv_repeat_corner_ok.setVisibility(View.VISIBLE);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				tv_repeat_item.setBackground(mContext.getResources().getDrawable(R.drawable.rect_btn_common_grey_bg));
			} else {
				tv_repeat_item.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.rect_btn_common_grey_bg));
			}
			tv_repeat_corner_ok.setVisibility(View.GONE);
		}
	}
}
