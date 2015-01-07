package net.xinzeling.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

public class LineEditText extends EditText {
	private Paint linePaint;
	private float margin;
	private int paperColor;
	private boolean isFirstLine = true;

	public LineEditText(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.linePaint = new Paint();
	}

	protected void onDraw(Canvas paramCanvas) {
		paramCanvas.drawColor(this.paperColor);
		int i = getLineCount();
		int j = getHeight();
		int k = getLineHeight() + 5;
		int m = 1 + j / k;
		if (i < m)
			i = m;
		int n = getCompoundPaddingTop();
		if(!isFirstLine){
			paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
		}

		
		for (int i2 = 0;; i2++) {
			if (i2 >= i) {
				setPadding(20 + (int) this.margin, 0, 0, 0);
				super.onDraw(paramCanvas);
				paramCanvas.restore();
				return;
			}
			n += k;
			if(!isFirstLine){
				paramCanvas.drawLine(0.0F, n, getRight(), n, this.linePaint);
			}
			paramCanvas.save();

			isFirstLine = false;
			
		}
	}

}
