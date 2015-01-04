package net.xinzeling.lib;

import net.xinzeling2.R;
import net.xinzeling.model.GuaCntModel;
import net.xinzeling.model.GuaCntModel.GuaCnt;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IconButton  extends LinearLayout{
    private Context mContext;
    private int mDefaultBackgroundColor 		= Color.BLACK;
    private int mFocusBackgroundColor 			= 0;
    private int mDefaultTextColor 				= Color.WHITE;
    private int mDefaultTextSize 				= 15;
    private String mText 						= null;
    private int mType							= 0;
    private Drawable mIconResource 				= null;
    private int mIconPosition 					= 1;
    private int mBorderColor 					= Color.TRANSPARENT;
    private int mBorderWidth 					= 0;
    private int mRadius 						= 0;

    public static final int POSITION_LEFT  		= 1;
    public static final int POSITION_TOP  		= 3;

    private void init(){
    	if(mIconPosition==POSITION_TOP){
            this.setOrientation(LinearLayout.VERTICAL);
        }else{
            this.setOrientation(LinearLayout.HORIZONTAL);
        }
        LayoutParams containerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(containerParams);
        this.setGravity(Gravity.CENTER);
        this.setClickable(true);
        this.setFocusable(true);

        this.removeAllViews();
        setupBackground();
        ImageView iconView = setupIconView();
        if(iconView !=null){
        	this.addView(iconView);
        }
        TextView textView = setupTextView();
        if(textView !=null){
        	this.addView(textView);
        }
    }
    
    public IconButton(Context context, AttributeSet attrs){
        super(context,attrs);
        this.mContext = context;
        TypedArray attrsArray 	= context.obtainStyledAttributes(attrs,R.styleable.IconButton, 0, 0);
        mDefaultBackgroundColor = attrsArray.getColor(R.styleable.IconButton_defaultColor,mDefaultBackgroundColor);
        mFocusBackgroundColor 	= attrsArray.getColor(R.styleable.IconButton_focusColor,mFocusBackgroundColor);

        mDefaultTextColor 		= attrsArray.getColor(R.styleable.IconButton_textColor,mDefaultTextColor);
        mDefaultTextSize		= (int) attrsArray.getDimension(R.styleable.IconButton_textSize,mDefaultTextSize);

        mBorderColor 			= attrsArray.getColor(R.styleable.IconButton_borderColor,mBorderColor);
        mBorderWidth			= (int) attrsArray.getDimension(R.styleable.IconButton_borderWidth,mBorderWidth);

        mRadius 				= (int)attrsArray.getDimension(R.styleable.IconButton_radius,mRadius);

        mText 					= attrsArray.getString(R.styleable.IconButton_text);
        mType					= attrsArray.getInt(R.styleable.IconButton_type, 0);
        mIconPosition 			= attrsArray.getInt(R.styleable.IconButton_iconPosition,mIconPosition);
        mIconResource 			= attrsArray.getDrawable(R.styleable.IconButton_iconResource);
       
        attrsArray.recycle();
        this.setGravity(Gravity.CENTER);
        init();
    }
    
    private TextView setupTextView(){
        if (mText != null) {
            TextView textView = new TextView(mContext);
            textView.setText(mText);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mDefaultTextColor);
            textView.setTextSize(mDefaultTextSize);
            LayoutParams layoutParams =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity =  Gravity.CENTER;
            if(this.mIconPosition==POSITION_LEFT){
            	layoutParams.leftMargin=0;
            }else{
            	layoutParams.topMargin=0;
            }
            textView.setLayoutParams(layoutParams);
            return textView;
        }
        return null;
    }
    
    private ImageView setupIconView(){
        if (mIconResource != null){
            ImageView iconView = new ImageView(mContext);
            iconView.setImageDrawable(mIconResource);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
          
            layoutParams.gravity = Gravity.CENTER;
           
            iconView.setLayoutParams(layoutParams);
            return iconView;
        }
        return null;
    }
        
    @SuppressLint("NewApi")
	private void setupBackground(){
    	boolean isUsable = true;//这个地方最好是动态查数据库,mType
    	if(mType!=0){
//    		GuaCnt result = GuaCntModel.fetch(mType);
//    		if(!result.isUsable){
//    			isUsable = result.isUsable;
//    		}
    	}
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(mRadius);
        drawable.setColor(mDefaultBackgroundColor);
        if (mBorderColor != 0) {
            drawable.setStroke(mBorderWidth, mBorderColor);
        }
        if(!isUsable){
        	drawable.setCornerRadii(new float[]{0,0,20,20,0,0,0,0});
        }
        // Focus/Pressed 
        GradientDrawable drawable2 = new GradientDrawable();
        drawable2.setCornerRadius(mRadius);
        drawable2.setColor(mFocusBackgroundColor);
        if (mBorderColor != 0) {
            drawable2.setStroke(mBorderWidth, mBorderColor);
        }
        if(!isUsable){
        	drawable2.setCornerRadii(new float[]{0,0,20,20,0,0,0,0});
        }

        StateListDrawable states = new StateListDrawable();
        if(mFocusBackgroundColor!=0){
            states.addState(new int[] {android.R.attr.state_pressed }, drawable2);
            states.addState(new int[] {android.R.attr.state_focused }, drawable2);
        }
        states.addState(new int[] {}, drawable);

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            this.setBackgroundDrawable(states);
        } else {
            this.setBackground(states);
        }
    }
}