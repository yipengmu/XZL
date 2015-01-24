package net.xinzeling.widget.BlurMask;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

public  class DateTimeBlurMaskTask extends AsyncTask<Void,Void,Void>{
	public View winView;
	public Bitmap drawCache;
    public Bitmap blurMask;
    public Runnable runnable;
    public View showView;
    public Context ctx;
    public BlurTheme mBlurTheme;
    public Fragment mFragment;
    
    @Deprecated
    public DateTimeBlurMaskTask(Context ctx, View frameView, View toShow,Runnable run){
    	winView= frameView;
    	this.ctx = ctx;
    	this.showView = toShow;
    	runnable = run;
    }
    
    public DateTimeBlurMaskTask(Context ctx,View toShow,Runnable run){
    	this.ctx = ctx;
    	this.showView = toShow;
    	runnable = run;
    }
    
    public enum BlurTheme{
    	TopicFragment,EarlyDateTimeFragment,DateTimeFragment
    }
    
    public DateTimeBlurMaskTask setTheme(BlurTheme theme){
    	mBlurTheme = theme;
    	return this;
    }
    
    @Override
    protected void onPreExecute() {
        //winView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        winView.setDrawingCacheEnabled(true);
//        //winView.setAlpha(80);
//        winView.buildDrawingCache();
//        drawCache = winView.getDrawingCache();
    }

	@Override
	protected Void doInBackground(Void... params) {
//		blurMask= drawCache;//Blur.apply(ctx, drawCache, 12);
		return null;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(Void aVoid) {
//		 winView.destroyDrawingCache();
//         winView.setDrawingCacheEnabled(false);
         if(runnable !=null){
        	 runnable.run();
         }
         //showView.setBackgroundDrawable(new BitmapDrawable(ctx.getResources(), blurMask));
         showView.setBackgroundColor(Color.parseColor("#DF000000"));
         showView.setVisibility(View.VISIBLE);
//         drawCache.recycle();
	 }
}

