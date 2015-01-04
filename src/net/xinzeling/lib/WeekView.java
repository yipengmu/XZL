package net.xinzeling.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class WeekView extends TextView {

	String[] week={"周日","周一","周二","周三","周四","周五","周六"};
	Paint paint;
	float txtSize ;
	int paddingTop ;
	public WeekView(Context context, AttributeSet attrs) {
		super(context, attrs);
		txtSize = this.getTextSize();
		paint = new Paint();
		paint.setColor(this.getTextColors().getDefaultColor());
		paint.setTextSize(txtSize);
		paddingTop = this.getPaddingTop();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		int width = this.getWidth()/7;
		float plus= (width-this.getTextSize()*2)/2;
		for(int i=0;i<7;i++){
			canvas.drawText(week[i], width*i+plus,txtSize+paddingTop, paint);
		}
	}
}