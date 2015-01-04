package net.xinzeling.lib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;

public  class BlurMaskTask extends AsyncTask<Void,Void,Void>{
	private View winView;
    private Bitmap drawCache;
    private Bitmap blurMask;
    private Runnable runnable;
    private View showView;
    Context ctx;
    public BlurMaskTask(Context ctx, View frameView, View toShow,Runnable run){
    	winView= frameView;
    	this.ctx = ctx;
    	this.showView = toShow;
    	runnable = run;
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

